local dialog = {
	{
		text = 'Do I have time for a coffee ?',
		answers = {
			{
				text = 'Yes, coffee is mandatory !',
				callback = function(player, currentScene) player.isLate = true end
			},
			{
				text = 'No, maybe later at the lab ...'
			}
		}
	}
}

return dialog