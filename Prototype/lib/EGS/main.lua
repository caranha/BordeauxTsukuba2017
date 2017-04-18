require("Class")
require("GUIElements/GUIMain")

--------------NOTE

-----This project (GUIUI/ and GUIElements/) uses lpglv2 open source license. Just give me credit, please. 
-----Trappingnoobs

function giveTheme(element)
	element.backgroundColor = {math.random(1,255),math.random(1,255),math.random(1,255)}
	element.borderColor = {math.random(1,255),math.random(1,255),math.random(1,255)}
	element.toolTipVisible = true
	element.toolTipText = element.className..": "..element.name
	--Check out 'GUIClassDecleration.lua' for more GUI properties. They've got tens of properties!
end

function load()
	love.graphics.setFont(love.graphics.setNewFont(10))
	love.graphics.setBackgroundColor(255,255,255)
	
	TextButton:new("TB1")
	TB1.position = { 200, 200 }
  TB1.size = {
    400, 200 
  }
	TB1.text = "This is a TextButton! It'll resize until textFitting is true!"
	giveTheme(TB1)
end

function update(Time)
	GUIUpdate(Time)
end

function draw()
	GUIDraw()
	love.graphics.setColor(0,0,0,255)
end