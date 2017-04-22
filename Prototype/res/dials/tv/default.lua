local dialog = {
	{
		text = 'Why not watch what\'s new in Kardashians\'s lives ?',
		answers = {
			{
				text = 'Just kidding, this is shit.'
			},
			{
				text = 'Okay just five minutes.',
				response = function(e1, e2) print('You\'re dumb') e2.isLate = true end
			}
		}
	}
}

return dialog