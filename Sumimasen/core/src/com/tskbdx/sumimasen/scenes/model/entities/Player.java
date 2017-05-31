package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.entities.movements.Walk;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/*Z
 * Created by Sydpy on 4/27/17.
 */
public class Player extends Entity {

    private List<String> tags = new ArrayList<>();
    private boolean canTalkAlone = true;

    public Player() {
        super();
        setMovement(new Walk());
        setName("player");
        setWidth(2);
        setHeight(1);
    }

    public void addTag(String tag) {
        assert !tags.contains(tag) : "Player already has tag : " + tag;
        tags.add(tag);
    }

    /**
     * Can only interact if there is a SceneObject
     * in front of the entity
     * Otherwise think about the current goal
     */
    @Override
    public boolean tryInteract() {
        if (!super.tryInteract() && canTalkAlone) {
            think("I don't want to be late...");
            canTalkAlone = false;
            Utility.setTimeout(() -> canTalkAlone = true, getMessage().getTotalDuration());
            return true;
        }
        return false;
    }

}
