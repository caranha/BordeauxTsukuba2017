local dialog = {
	-- 1
	{
		text = "What did you do ??? Where is the machine ???",

		answers = {
			{
				text = "[ tell the truth]",
				nextIndex = 2
			},
			{
				text = "[ accuse the machine ]",
				nextIndex = 8
			}
		}
	},
	-- 2
	{
		text = "You pushed the wrong button ! But I told to push the right one ! Not the wrong one !",
		answers = {
			{
				text = "I'm a bit lost, the right one or the left one ?",
				callback = function(player, scene) player.naiveness = player.naiveness + 1 end,
				nextIndex = 3
			},
			{
				text = "I think there is a quiproquo ...",
				callback = function(player, scene) player.naiveness = player.naiveness - 1 end,
				nextIndex = 6
			}
		}
	},
	-- 3
	{
		text = "You're not that smart, it reminds me of a guy back in the 60's who ... Anyway ! Hopefully I have a backup in... can't remember the name of this city ! You should go and ask to the laboratory of the neighbouring city where it is. They use the same company for their backup.",
		answers = {
			{
				text = "Can't you go yourself ? I'm afraid of being lost.",
				nextIndex = 4
			},
			{
				text = "Okay I'll go there",
				nextIndex = 5
			}
		}
	},
	-- 4
	{
		text = "Ahahah that's your mistake that's you who should repair it !",
		answers = {
			{
				text = "Okay then ... Tell me where to go.",
				nextIndex = 5
			}
		}
	},
	-- 5
	{
		text = "Alright, I called a taxi, he mights be here when you leave the building"
	},
	-- 6
	{
		text = "I don't speak Japanese.",
		answers = {
			{
				text = "That's French actually ... Or Belgian, don't remember.",
				nextIndex = 3
			},
			{
				text = "Is there a way I can help ?",
				nextIndex = 7
			}
		}
	},
	-- 7
	{
		text = "You will bring me the backup which is located in ... Can't remember the name of the city, go to the next city and ask the laboratory, they use the same service for their backup.",
		answers = {
			{
				text = "Okay I'll go as fast as I can !",
				nextIndex = 5
			}
		}
	},
	-- 8
	{
		text = "No that's your fault ! This machine is the smartest that I have ever built, except maybe the one that I designed back in the 50's ... ",
		answers = {
			{
				text = "Maybe it wasn't that perfect.",
				nextIndex = 9 
			},
			{
				text = "And what was its purpose ?",
				nextIndex = 11
			}
		}
	},
	-- 9
	{
		text = "Or maybe I shouldn't ask a monkey to use a rifle ...",
		answers = {
			{
				text = "Ahahah a monkey",
				nextIndex = 10
			},
			{
				text = "I'm really sorry sir.",
				nextIndex = 10
			}
		}
	},
	-- 10
	{
		text = "So now you must repair your mistakes !",
		answers = {
			{
				text = "Okay !",
				nextIndex = 7
			},
			{
				text = "Okay ...",
				nextIndex = 7
			}
		}
	},
	-- 11
	{
		text = "This machine was supposed to synthetize a serum that would make anything disappear",
		answers = {
			{
				text = "Ahahah and it disappeared itself !",
				nextIndex = 3
			},
			{
				text = "Woo can something like this be real ?",
				nextIndex = 3
			}
		}
	}
}

return dialog