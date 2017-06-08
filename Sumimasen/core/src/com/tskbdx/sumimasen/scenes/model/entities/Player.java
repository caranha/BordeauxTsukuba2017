package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Walk;
import com.tskbdx.sumimasen.scenes.utility.Utility;

/*Z
 * Created by Sydpy on 4/27/17.
 */
public class Player extends Entity {

    private boolean canThink = true;

    public Player() {
        super();
        setMovement(new Walk());
        setName("player");
        setWidth(2);
        setHeight(1);
    }


    /**
     * Can only interact if there is a SceneObject
     * in front of the entity
     * Otherwise think about the current goal
     */
    @Override
    public boolean tryInteract() {
        if (!super.tryInteract() && canThink) {
            String description = GameScreen.getCurrentScene().description();
            think(description != null ? description : "");
            canThink = false;
            Utility.setTimeout(() -> canThink = true, getMessage().getTotalDuration());
            return true;
        }
        return false;
    }

}
