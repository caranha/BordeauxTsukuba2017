package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viet khang on 08/05/2017.
 */
public class Dialog extends Interaction {

    private Map<Integer, DialogueExchange> exchanges = new HashMap<>();

    private DialogueExchange currentExchange = new DialogueExchange();

    public Dialog(Entity producer, Entity consumer) {
        super(producer, consumer);

        DialogueExchange exchange1 = new DialogueExchange();
        exchange1.setText("How are you doing ?");

            DialogueAnswer answer11 = new DialogueAnswer();
            answer11.setText("Well !");
            answer11.setNextExchange(2);

            DialogueAnswer answer12 = new DialogueAnswer();
            answer12.setText("Bof !");
            answer12.setNextExchange(3);

        exchange1.addAnswer(answer11);
        exchange1.addAnswer(answer12);

        DialogueExchange exchange2 = new DialogueExchange();
        exchange2.setText("Nice");

        DialogueExchange exchange3 = new DialogueExchange();
        exchange3.setText("Too bad");

        exchanges.put(1, exchange1);
        exchanges.put(2, exchange2);
        exchanges.put(3, exchange3);

        currentExchange = exchange1;
    }

    @Override
    public void start() {
        super.start();
        printCurrentState();
        active.setMessage(currentExchange.getText(), 5.f, passive);
        active.notifyObservers();
    }

    @Override
    public void update() {
        if (currentExchange.getAnswers().isEmpty()) {
            end();
        }
    }

    public void pickAnswer(int index) {

        DialogueAnswer dialogueAnswer = currentExchange.getAnswers().get(index);
        if (dialogueAnswer.getNextExchange() != null) {
            currentExchange = exchanges.get(dialogueAnswer.getNextExchange());
        }

        printCurrentState();
    }

    private void printCurrentState() {
        System.out.println(currentExchange.getText());

        List<DialogueAnswer> answers = currentExchange.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            System.out.println( (i + 1 ) + "\t" + answers.get(i).getText() );
        }

    }
}
