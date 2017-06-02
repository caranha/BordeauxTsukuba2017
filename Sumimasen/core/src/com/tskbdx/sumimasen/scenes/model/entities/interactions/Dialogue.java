package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.inputprocessors.GameCommands;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Message;
import com.tskbdx.sumimasen.scenes.utility.Utility;
import com.tskbdx.sumimasen.scenes.view.ui.UserInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by viet khang on 08/05/2017.
 */
public class Dialogue extends Interaction {

    private static final String FOLDER = "dialogues/";
    private Map<Integer, DialogueExchange> exchanges = new HashMap<>();
    private DialogueExchange currentExchange = new DialogueExchange();
    private String xmlFile;

    public Dialogue(String xmlFile) {
        super();
        this.xmlFile = xmlFile;
    }

    @Override
    public void start(Entity active, Entity passive) {
        super.start(active, passive);

        Gdx.input.setInputProcessor(UserInterface.getInstance());

        buildDialogue(FOLDER +
                GameScreen.getCurrentScene().getName() +
                '/' + active.getName() + '/' + xmlFile); // by convention
        currentExchange = exchanges.get(1);

        printCurrentState();
    }

    @Override
    public void end() {
        super.end();
        Interaction nextInteraction = getActive().getNextInteraction();
        getActive().setInteraction(nextInteraction != null ? nextInteraction : this);

        Gdx.input.setInputProcessor(GameCommands.getInstance());
    }

    public void pickAnswer(int index) { // passive entity answers
        try {
            DialogueAnswer dialogueAnswer = currentExchange.getAnswers().get(index);
            dialogueAnswer.processCallbacks();
            getPassive().setMessage(dialogueAnswer.getText(), 1.f, getActive(),  dialogueAnswer.isImportant);
            Message answer = getPassive().getMessage();

            // when passive talk, active stop
            getActive().setMessage("", 0.f, getPassive(), dialogueAnswer.isImportant);

            if (dialogueAnswer.getNextExchange() != null) {
                currentExchange = exchanges.get(dialogueAnswer.getNextExchange());
            }

            getActive().notifyObservers();
            getPassive().notifyObservers();
            if (currentExchange.text.equals("")) {
                printCurrentState();
            } else {
                Utility.setTimeout(this::printCurrentState, answer.getTimeToUnderstand());
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
    }

    private void printCurrentState() { // active entity talks
        if (!currentExchange.nextDialogue.equals("")) {
            getActive().setNextInteraction(new Dialogue(currentExchange.nextDialogue));
        }

        if (currentExchange.triggerWonder) {
            getPassive().think(currentExchange.getText());
        } else {
            getActive().setMessage(currentExchange.getText(), 0.f, getPassive(),  currentExchange.isImportant);
        }
        List<DialogueAnswer> answers = currentExchange.getAnswers();

        if (!answers.isEmpty()) {
            Utility.setTimeout(() -> {
                getActive().notifyObservers(this);
                getPassive().notifyObservers(this);
            }, getActive().getMessage().getTimeToUnderstand());
        } else {
            if (!currentExchange.triggerWonder) {
                getActive().setMessage(currentExchange.getText(), 2.f, getPassive(),  currentExchange.isImportant);
            }
            end();
        }
    }

    private void buildDialogue(String xmlFile) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(xmlFile);
            Element docEle = dom.getDocumentElement();
            buildExchanges(docEle);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }


    }

    private void buildExchanges(Element docEle) {
        NodeList exchangesNodes = docEle.getElementsByTagName("exchange");
        if (exchangesNodes != null && exchangesNodes.getLength() > 0) {
            for (int i = 0; i < exchangesNodes.getLength(); i++) {

                Element exchangeNode = (Element) exchangesNodes.item(i);

                Integer id = Integer.valueOf(exchangeNode.getAttribute("id"));
                String text = exchangeNode.getAttribute("text");


                DialogueExchange exchange = new DialogueExchange();
                exchange.setText(text);
                try {
                    Integer triggerWonder = Integer.valueOf(exchangeNode.getAttribute("triggerWonder"));
                    exchange.setTriggerWonder(triggerWonder != null && triggerWonder.equals(1));
                } catch (Exception ignored) {
                }
                try {
                    Integer important = Integer.valueOf(exchangeNode.getAttribute("important"));
                    exchange.setTriggerWonder(important != null && important.equals(1));
                } catch (Exception ignored) {
                }
                exchange.nextDialogue = exchangeNode.getAttribute("nextDialogue");

                buildAnswers(exchangeNode, exchange);

                exchanges.put(id, exchange);
            }
        }
    }

    private void buildAnswers(Element exchangeNode, DialogueExchange exchange) {
        NodeList answersNodes = exchangeNode.getElementsByTagName("answers");
        if (answersNodes != null && answersNodes.getLength() > 0) {
            Element answersNode = (Element) answersNodes.item(0);

            NodeList answerNodes = answersNode.getElementsByTagName("answer");
            if (answerNodes != null && answerNodes.getLength() > 0) {
                for (int j = 0; j < answerNodes.getLength(); j++) {
                    Element answerNode = (Element) answerNodes.item(j);

                    Integer nextExchange = Integer.valueOf(answerNode.getAttribute("nextExchange"));
                    String answerText = answerNode.getAttribute("text");
                    String answerIdea = answerNode.getAttribute("idea");

                    DialogueAnswer answer = new DialogueAnswer();
                    answer.setText(answerText);
                    answer.idea = answerIdea;
                    answer.setNextExchange(nextExchange);

                    exchange.addAnswer(answer);
                    answer.setCallbacks(answerNode.getElementsByTagName("callback"));
                }
            }
        }
    }

    public final Map<Integer, DialogueExchange> getExchanges() {
        return exchanges;
    }

    public final DialogueExchange getCurrentExchange() {
        return currentExchange;
    }

    public class DialogueAnswer implements Serializable {

        String text = "";
        Integer nextExchange;
        String idea;
        private NodeList callbacks;
        boolean isImportant = false;

        public String getIdea() {
            return idea;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getNextExchange() {
            return nextExchange;
        }

        public void setNextExchange(int nextExchange) {
            this.nextExchange = nextExchange;
        }

        void setCallbacks(NodeList callbacks) {
            this.callbacks = callbacks;
        }

        /**
         * Parse answer's children node with tag "callback".
         * Call a method from its property with reflection.
         * // to do : consider the target object with "on" property.
         */
        void processCallbacks() {
            for (int i = 0; i != callbacks.getLength(); ++i) {
                Element callback = (Element) callbacks.item(i);
                /*
                 * for each callback we take its method name
                 */
                String methodName = callback.getAttribute("methodName");
                /*
                 * a method can have multiple arguments
                 * each argument has a type and a value
                 */
                NodeList arguments = callback.getElementsByTagName("arg");
                List<Class> argsType = new ArrayList<>();
                List<Object> argsValue = new ArrayList<>();
                for (int j = 0; j != arguments.getLength(); ++j) {
                    Element argument = (Element) arguments.item(j);
                    /*
                     * for each argument we store its type and value
                     */
                    String type = argument.getAttribute("type");
                    try {
                        /*
                         * Get class from name
                         */
                        argsType.add(Class.forName(type));
                    } catch (ClassNotFoundException e) {
                        /*
                         * if it doesn't work maybe it was a native type
                         * (int, double etc...)
                         */
                        argsType.add(Utility.getPrimitiveType(type));
                    }
                    /*
                     * We get typed value from the string in value property
                     */
                    argsValue.add(Utility.interpret(argument.getAttribute("value")));
                }

                /*
                 * Now let's call the method
                 */
                Object target = GameScreen.getPlayer();
                try {
                    Method method = target.getClass().getMethod(methodName,
                            argsType.toArray(new Class[argsType.size()]));
                    method.invoke(target, argsValue.toArray());
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException("Does " + methodName + " exist ?");
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Is " + methodName + " public ?");
                } catch (InvocationTargetException e) {
                    throw new IllegalStateException("Is " + methodName + " not abstract ?");
                }
            }
        }

    }

    public class DialogueExchange implements Serializable {
        private String text = "";

        private List<DialogueAnswer> answers = new ArrayList<>();
        private boolean triggerWonder = false;
        private String nextDialogue = "";
        boolean isImportant = false;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<DialogueAnswer> getAnswers() {
            return answers;
        }

        public void addAnswer(DialogueAnswer answer) {
            answers.add(answer);
        }

        public void removeAnswer(DialogueAnswer answer) {
            answers.remove(answer);
        }

        public void setCallbacks(NodeList callbacks) {
        }

        public void setTriggerWonder(boolean triggerWonder) {
            this.triggerWonder = triggerWonder;
        }
    }
}
