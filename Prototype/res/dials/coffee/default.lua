local dialog = {
	{
		text = 'Do I have time for a coffee ?',
		answers = {
			{
				text = 'Yes, coffee is mandatory !',
				response = function(e1,e2) e2.isLate = true end
			},
			{
				text = 'No, maybe later at the lab ...'
			}
		}
	}
}

return dialog