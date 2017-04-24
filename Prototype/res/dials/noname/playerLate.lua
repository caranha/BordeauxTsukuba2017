local dialog = {
	{
		text = 'What were you doing ? This is your first day and you find a way to be late !',
		answers = {
			{
				text = 'Sumimasen...',
        nextIndex = 2
			},
      {
        text = "Quiproquo...",
        nextIndex = 3
      }
		}
	},
  {
    text = "I don't speak Japanese. Anyway. I see you dressed appropriately, great.",
    answers = {
      {
        text = "This is my dressing gown actually."
      },
      {
        text = "I meant : sorry for my late sir."
      }
    }
  },
  {
    text = "I don't speak Japanese. Anyway. I see you dressed appropriately, great.",
    answers = {
      {
        text = "This is my dressing gown actually.",
        nextIndex = 6
      },
      {
        text = "It's French.",
        nextIndex = 4
      }
    }
  },
  {
    text = "La Baguette ?",
    answers = {
      {
        text = "Yes sir, what have I to do ?",
        nextIndex = 5
      },
      {
        text = "*acquiesce*",
        nextIndex = 7
      }
    }
  }
}

return dialog