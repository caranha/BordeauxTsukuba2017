return {
    {
        text = "Pr. Noname : Yes this machine ! Please push the right button to start.",
        answers =  {
          {
            text = "Let's push the left button.",
            callback = function(player, scene)
              player.animation = false
              scene.machineBroken = true
              scene["Pr. Noname"].isMoving = false
              scene["machine"]:disappear(scene)
            end
          },
          {
            text = "He said the 'right' one, let's push right button.",
            callback = function(player, scene) 
              scene.machineBroken = true 
              player.animation = false
              scene["Pr. Noname"].isMoving = false
              scene["machine"]:disappear(scene)
            end
          }
        }
    }
}