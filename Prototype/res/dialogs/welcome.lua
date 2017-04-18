dialog =  {
hierarchic=1,
label="",
directed=1,
node
	 = {
	id=0,
	label="hi !",
},
node
	 = {
	id=1,
},
node
	 = {
	id=2,
	label="Hey !",
  answer=true
},
node
	 = {
	id=3,
	label="What's up ?",
  answer=true
},
node
	 = {
	id=4,
	label="...",
	graphics
		 = {
		x=189.0,
		y=150.0,
		w=30.0,
		h=30.0,
		type="diamond",
		raisedBorder=0,
		fill="#FFCC00",
		outline="#000000"
	},
	LabelGraphics
		 = {
		text="...",
		fontSize=12,
		fontName="Dialog",
		model="null"
	},
},
node
	 = {
	id=5,
	label="Yes",
  answer=true
},
edge
	 = {
	source=0,
	target=2
},
edge
	 = {
	source=0,
	target=3
},
edge
	 = {
	source=0,
	target=4,
	graphics
},
edge
	 = {
	source=2,
	target=1
},
edge
	 = {
	source=3,
	target=1
},
edge
	 = {
	source=1,
	target=5
},
}
