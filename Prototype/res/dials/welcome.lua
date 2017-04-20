local dialog = {
	{
		text = 'Well, we worked hard today !',
		answers = {
			{
				nextIndex = 2,
				text = '[ agree ]'
			}
		}
	},
	
	{
		text = 'I think I can finish the serum tonight, you can go home. See you tomorrow !',
		answers = {
			{
				text = 'See you !'
			},
			{
				nextIndex = 3,
				text = 'I would like to stay here to work with you.'
			}
		}
	},
	
	{
		text = 'Oh no I insist, your cat must be waiting for you !',
		answers = {
			{
				text = 'Ok, see you tomorrow !'
			}
		}
	},
}

return dialog