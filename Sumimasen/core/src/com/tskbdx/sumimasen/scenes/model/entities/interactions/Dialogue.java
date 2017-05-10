package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.scenes.inputprocessors.DialogueInputProcessor;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
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
    }

    public void pickAnswer(int index) {

        try {
            DialogueAnswer dialogueAnswer = currentExchange.getAnswers().get(index);
            passive.setMessage(dialogueAnswer.getText(), 5.f, 2.f, active);
            passive.getMessage().notifyObservers();
            ((DialogueInputProcessor) Gdx.input.getInputProcessor()).update();
            if (dialogueAnswer.getNextExchange() != null) {
                currentExchange = exchanges.get(dialogueAnswer.getNextExchange());
            }
            printCurrentState();
        } catch (IndexOutOfBoundsException ignored) {
            System.out.println("ignored : " + ignored.getMessage());
        }

    }

    private void printCurrentState() {
        active.setMessage(currentExchange.getText(), 5.f, 2.f, passive);
        active.getMessage().notifyObservers();

        List<DialogueAnswer> answers = currentExchange.getAnswers();

        System.out.println(currentExchange.getText());
        for (int i = 0; i < answers.size(); i++) {
            System.out.println((i + 1) + "\t" + answers.get(i).getText());
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
