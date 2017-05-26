package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Message;
import com.tskbdx.sumimasen.scenes.utility.Utility;
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

        buildDialogue(FOLDER + active.getName() + '/' + xmlFile); // by convention
        currentExchange = exchanges.get(1);

        printCurrentState();
    }

    public void pickAnswer(int index) { // passive entity answers
        try {
            DialogueAnswer dialogueAnswer = currentExchange.getAnswers().get(index);
            dialogueAnswer.processCallbacks();
            getPassive().setMessage(dialogueAnswer.getText(), 1.5f, 1.f, getActive());
            Message answer = getPassive().getMessage();
            answer.notifyObservers();

            // when passive talk, active stop
            getActive().setMessage("", 0.f, 0.f, getPassive());
            getActive().getMessage().notifyObservers();

            if (dialogueAnswer.getNextExchange() != null) {
                currentExchange = exchanges.get(dialogueAnswer.getNextExchange());
            }

            getActive().notifyObservers();
            getPassive().notifyObservers();
            Utility.setTimeout(this::printCurrentState, answer.getTimeToUnderstand());
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public void end() {
        super.end();
        getActive().setInteraction(new Dialogue("default.xml"));
    }

    private void printCurrentState() { // active entity talks
        getActive().setMessage(currentExchange.getText(), 2.f, 0.f, getPassive());
        getActive().getMessage().notifyObservers();

        List<DialogueAnswer> answers = currentExchange.getAnswers();

        if (!answers.isEmpty()) {
            Utility.setTimeout(() -> {
                getActive().notifyObservers(this);
                getPassive().notifyObservers(this);
            }, getActive().getMessage().getTimeToUnderstand());
        } else {
            getActive().setMessage(currentExchange.getText(), 2.f, 2.f, getPassive());
            getActive().getMessage().notifyObservers();
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

                    DialogueAnswer answer = new DialogueAnswer();
                    answer.setText(answerText);
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
        private NodeList callbacks;


        public DialogueAnswer() {
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
                    Element argument = (Element) arguments.item(i);
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

        public DialogueExchange() {
        }


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
    }
}
