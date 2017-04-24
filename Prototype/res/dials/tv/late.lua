local dialog = {
  {
		text = 'Why not watch what\'s new in Kardashians\' lives ?',
		answers = {
			{
				text = 'Just kidding, this is shit.'
			},
			{
				text = 'Okay just five minutes.',
				callback = function(player, currentScene) player.isLate = true end
			}
		}
	}
}

return dialog