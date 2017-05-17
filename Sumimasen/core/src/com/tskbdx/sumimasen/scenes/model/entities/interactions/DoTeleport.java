package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Teleport;

/**
 * Created by Sydpy on 5/16/17.
 */
public class DoTeleport extends Interaction {

    private Movement teleport;

    public DoTeleport(int x, int y) {
        super();

        teleport = new Teleport(x,y);
    }

    @Override
    public void start(Entity active, Entity passive) {
        super.start(active, passive);

        Movement entityMovementBackup = passive.getMovement();

        passive.setMovement(teleport);
        teleport.move(passive);

        passive.setMovement(entityMovementBackup);

        end();
    }

    @Override
    public void end() {
        super.end();
    }
}