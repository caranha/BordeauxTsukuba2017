local dialog = {
	{
		text = "Hi ! Right on time ! Do you want some coffee ?",
		answers = {
			{
				text = "Yes why not !",
				nextIndex = 2
			},
			{
				text = "Well, I would prefer some Peruvian tea with some soy milk and English biscuits",
       			callback = function(player, scene) player.naiveness = player.naiveness + 1 end,
				nextIndex = 3
			}
		}	
	},
	{
		text = "Well, I just remembered that the coffee machine disappeared during my experiences, sorry. What time is it ?",
		answers = {
			{
				text = "I have a watch but I don't know how to read the time on it.",
				callback = function(player, scene) player.naiveness = player.naiveness + 1 end,
				nextIndex = 4
			},
			{
				text = "9 am sir !",
				callback = function(player, scene) player.naiveness = player.naiveness - 1 end,
				nextIndex = 4
			}
		}
	},
	{
		text = "Please make simple answers to simple questions, there's no Peruvian biscuit here ! Anyway, are you ready to work ?",
		answers = {
			{
				text = "Yes, of course !",
				nextIndex = 5
			},
			{
				text = "Yes, tell me what to do !",
				nextIndex = 5
			}
		}

	},
	{
		text = "Oh doesn't matter ! Can you take care of the machine over there ?"
	},
	{
		text = "Please take care, of the machine over there."
	}
}

return dialog