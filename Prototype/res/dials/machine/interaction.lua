return {
    {
        text = "Pr. Noname : Yes this machine ! Please push the right button to start.",
        answers =  {
          {
            text = "Let's push the left button.",
            callback = function(player, scene) player.brokeTheMachine = true end
          },
          {
            text = "He said the 'right' one, let's push right button.",
            callback = function(player, scene) player.brokeTheMachine = true end
          }
        }
    }
}