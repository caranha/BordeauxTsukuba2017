package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viet khang on 08/05/2017.
 */
public class Dialogue extends Interaction {

    private class DialogueAnswer {

        String text = "";
        Integer nextExchange;

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
    }

    private class DialogueExchange {
        private String text ="";

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
    }


    private static final String FOLDER = "dialogues/";
    private Map<Integer, DialogueExchange> exchanges = new HashMap<>();
    private DialogueExchange currentExchange = new DialogueExchange();
    private float talkClock = 0.f;
    private float answerClock = 0.f;

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

    @Override
    public void update() {

        if (currentExchange.getAnswers().isEmpty()) {
            end();
        }
        if (talkClock != 0.f) {
            talkClock -= Gdx.graphics.getDeltaTime();
            if (talkClock < 0.f) {
                talkClock = 0.f;
            }
        }
        if (answerClock != 0.f) {
            answerClock -= Gdx.graphics.getDeltaTime();
            if (answerClock < 0.f) {
                answerClock = 0.f;
                printCurrentState();
            }
        }
    }

    public void pickAnswer(int index) { // passive entity answers
        try {
            DialogueAnswer dialogueAnswer = currentExchange.getAnswers().get(index);
            getPassive().setMessage(dialogueAnswer.getText(), 3.5f, 0.5f, getActive());
            Message message = getPassive().getMessage();
            message.notifyObservers();

            // when passive talk, active stop
            getActive().setMessage("", 0.f, 0.f, getPassive());
            getActive().getMessage().notifyObservers();

            if (dialogueAnswer.getNextExchange() != null) {
                currentExchange = exchanges.get(dialogueAnswer.getNextExchange());
            }

            answerClock = message.getTimeToUnderstand();
        } catch (IndexOutOfBoundsException ignored) {}
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

        if (! answers.isEmpty()) {
            talkClock = getActive().getMessage().getTimeToUnderstand();
        } else {
            getActive().setMessage(currentExchange.getText(), 2.f, 2.f, getPassive());
            getActive().getMessage().notifyObservers();
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
}
