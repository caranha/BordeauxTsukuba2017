local dialog = {
	{
		text = 'Hey come here this is your first day and you find a way to be late !',
		answers = {
			{
				text = 'Sumimasen...',
        nextIndex = 2
			},
      {
        text = "The bus was late...",
        nextIndex = 2
      }
		}
	},
  {
    text = "Very nice weather by the way. Tell me your name again ?",
    answers = {
      {
        text = "I'm ...",
        nextIndex = 3
      }
    }
  },
  {
    text = "It's really sunny today. You seem dressed for the work.",
    answers = {
      {
        text = "Actually this is my dressing gown.",
        callback = function(player, scene) player.naiveness = player.naiveness + 1 end, 
        nextIndex = 4
      },
      {
        text = "Yes ! I bought it especially for this job.",
        callback = function(player, scene) player.naiveness = player.naiveness - 1 end,
        nextIndex = 5
      }
    }
  },
  {
    text = "At least it looks comfortable.",
    answers = {
      {
        text = "Yeah it's made of coton and ...",
        nextIndex = 5
      },
      {
        text = "Not really, I should have put something under ...",
        callback = function(player, scene) player.naiveness = player.naiveness - 1 end,
        nextIndex = 5
      }
    } 
  },
  {
    text = "What year is it ? Anyway ... Maybe you could take care of this machine over there.",
    answers = {
      {
        text = "Okay sure !"
      },
      {
        text = "Which one, there are too many machines in here.",
        nextIndex = 6
      }
    }
  },
  {
    text = "The one with the buttons and which makes *bip* every 2 seconds"
  }

}

return dialog