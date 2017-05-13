package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.scenes.inputprocessors.DialogueInputProcessor;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viet khang on 08/05/2017.
 */
public class Dialogue extends Interaction {

    private Map<Integer, DialogueExchange> exchanges = new HashMap<>();
    private DialogueExchange currentExchange = new DialogueExchange();
    private float talkClock = 0.f;
    private float answerClock = 0.f;

    public Dialogue(Entity producer, Entity consumer, String xmlFile) {
        super(producer, consumer);

        buildDialogue(xmlFile);
        currentExchange = exchanges.get(1);
    }

    @Override
    public void start() {
        super.start();
        active.setMovement(null);
        passive.setMovement(null);

        Gdx.input.setInputProcessor(new DialogueInputProcessor(this));
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
                ((DialogueInputProcessor) Gdx.input.getInputProcessor()).start();
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

    public void pickAnswer(int index) {
        try {
            DialogueAnswer dialogueAnswer = currentExchange.getAnswers().get(index);
            passive.setMessage(dialogueAnswer.getText(), 2.f, 2.f, active);
            Message message = passive.getMessage();
            message.notifyObservers();

            // when passive talk, active stop
            active.setMessage("", 0.f, 0.f, passive);
            active.getMessage().notifyObservers();

            if (dialogueAnswer.getNextExchange() != null) {
                currentExchange = exchanges.get(dialogueAnswer.getNextExchange());
            }

            ((DialogueInputProcessor) Gdx.input.getInputProcessor()).update();
            ((DialogueInputProcessor) Gdx.input.getInputProcessor()).stop();
            answerClock = message.getTimeToUnderstand();
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private void printCurrentState() {
        active.setMessage(currentExchange.getText(), 2.f, 0.f, passive);
        active.getMessage().notifyObservers();
        // when active talk, passive stop
        passive.setMessage("", 0.f, 0.f, active);
        passive.getMessage().notifyObservers();

        List<DialogueAnswer> answers = currentExchange.getAnswers();

        System.out.println(currentExchange.getText());
        for (int i = 0; i < answers.size(); i++) {
            System.out.println((i + 1) + "\t" + answers.get(i).getText());
        }

        if (! answers.isEmpty()) {
            talkClock = active.getMessage().getTimeToUnderstand();
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
