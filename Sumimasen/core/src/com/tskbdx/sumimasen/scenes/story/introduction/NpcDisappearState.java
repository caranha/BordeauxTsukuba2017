package com.tskbdx.sumimasen.scenes.story.introduction;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.story.StoryState;

import java.util.Map;

/*/
 * Created by viet khang on 14/05/2017.
 */
public class NpcDisappearState implements StoryState {

    @Override
    public void process(World world) {
        System.out.println(description());
    }

    @Override
    public Map<Class<? extends Interaction>, StoryState> next() {
        return null;
    }

    @Override
    public String description() {
        return "The NPC was so happy that you took the present that" +
                "he decided to go in your inventory !\n" +
                "End of the test scene";
    }
}
