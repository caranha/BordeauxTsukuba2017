local dialog = {
	{
		text = 'Your cat is starving',
		answers = {
			{
				text = 'Let\'s feed him !',
				callback = function(player, scene)
          player.kindness = player.kindness + 1 
          player.isLate = true
          player.__hasFedCat = true
        end
			},
			{
				text = 'Ignore him',
				callback = function(player, scene)
          player.kindness = player.kindness - 1
          player.__hasFedCat = false
        end
			}
		}
	}
}

return dialog