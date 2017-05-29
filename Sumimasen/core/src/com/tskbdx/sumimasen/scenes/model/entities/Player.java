package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.entities.movements.Walk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*Z
 * Created by Sydpy on 4/27/17.
 */
public class Player extends Entity implements Serializable {

    List<String> tags = new ArrayList<>();

    public Player() {
        setMovement(new Walk());
        setName("player");
        setWidth(2);
        setHeight(2);
    }

    public void addTag(String tag) {
        assert ! tags.contains(tag) : "Player already has tag : " + tag;
        tags.add(tag);
    }
}
