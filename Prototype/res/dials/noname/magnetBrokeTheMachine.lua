local dialog = {
	-- 1
	{
		text = "Hey what did you do to the machine ? It is very sensible to magnetism",
		answers = {
			{
				text = "[ tell him about the magnet in your pocket ]",
				callback = function (player, scene ) player.naiveness = player.naiveness - 1 end,
				nextIndex = 2
			},
			{
				text = "[ tell him you don't understand ]",
				callback = function (player, scene ) player.naiveness = player.naiveness + 1 end,
				nextIndex = 6
			}
		}
	},
	-- 2
	{
		text = "Oh my god... Well you couldn't know... It reminds me of a mistake I made when I was a young researcher with mice and acid... Anyway, who are you ?",
		answers = {
			{
				text = "I'm the new assistant, my name is ...",
				nextIndex = 3
			}
		}
	},
	-- 3
	{
		text = "Oh I see, and you make the work of my life disappear on your first day of work. Fortunately, I have a backup in ... oh can't remember the name of the city. I have your first task : you will go to the neighbouring city and ask the laboratory the location of the backup, they use the same service for theirs.",
		answers = {
			{
				text = "That was pretty clear !"
				callback = function (player, scene ) player.naiveness = player.naiveness - 1 end,
			},
			{
				text = "I drop my pen while you were speaking, can you repeat ?",
				callback = function (player, scene ) player.naiveness = player.naiveness + 1 end,
				nextIndex = 4
			}
		}
	},
	-- 4
	{
		text = "You go to the laboratory of the next city and ask them for the location of their backup",
		answers = {
			{
				text = "Can't you go by yourself ? That's your problem ...",
				callback = function (player, scene ) player.kindness = player.kindness - 1 end,
				nextIndex = 5
			},
			{
				text = "Okay I think that's normal to repair my mistakes !",
				callback = function (player, scene ) player.kindness = player.kindness + 1 end,
			}
		}
	},
	-- 5
	{
		text = "If you care about your job you better do it ! And once knew a man who refused to... ",
		answers = {
			{
				text = "Okay I'm leaving now !",
				callback = function(player, scene) player.animation = true end
			}
		}
	},
	-- 7
	{
		text = "Wait I couldn't have made a mistake.. This machine must be disappear-proof !",
		answers = {
			{
				text = "Well, it happens when you're getting old...",
				callback = function (player, scene ) player.kindness = player.kindness - 1 end,
				nextIndex = 8
			},
			{
				text = "Oh maybe that's because of the magnet I have in my right pocket",
				callback = function (player, scene ) player.kindness = player.kindness + 1 end,
				nextIndex = 2
			}
		}
	},
	-- 8
	{
		text = "Wait ! What's on your left pocket ? No the right one ? Never knew which side is right ... Is it a magnet ? Well this just made the serum inside the machine unstable and it disappeared ...",
		answers = {
			{
				text = "And what this serum supposed to do ? Cure the sunburns ?",
				callback = function (player, scene ) player.naiveness = player.naiveness + 1 end,
				nextIndex = 9
			},
			{
				text = "I'm really sorry sir ... Anyway, nice to meet too, I'm your new assistant.",
				nextIndex = 3
			}
		}
	},
	-- 9
	{
		text = "Its purpose is to make anything you want disappear... And my machine disappeared... Well, are you my new assistant ?",
		answers = {
			{
				text = "Yes !",
				nextIndex = 3
			}
		}
	}
}


return dialog