local dialog = {
	{
		text = 'Your cat is starving',
		answers = {
			{
				text = 'Let\'s feed him !',
				response = function(player)
          player.kindness = player.kindness + 1 
          player.isLate = true
          player.__hasFedCat = true
        end
			},
			{
				text = 'Ignore him',
				response = function(player)
          player.kindness = player.kindness - 1
          player.__hasFedCat = false
        end
			}
		}
	}
}

return dialog