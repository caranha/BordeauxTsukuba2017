local dialog = {
	{
		text = 'Why not watch what\'s new in Kardashians\'s lives',
		answers = {
			{
				text = 'Just kidding, this is shit.',
				response = function(e1,e2) e2.isLate = true end
			},
			{
				text = 'Okay just five minutes.'
			}
		}
	}
}

return dialog