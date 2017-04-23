require 'scenes.sceneutils'

local scene = {

	camera = {},
	player = {} ,
	objects = {},

	currentWorld, 
	currentMap,

	currentMapName = 'home',

	maps = {'home', 'labo', 'start'},

	playerInteractions = {} 
}

function scene:init()
	self.currentMap, self.currentWorld = loadMapAndWorld(self.currentMapName, 'home_upstairs', self)
	self.camera = buildCamera() 
end

function scene:pickDialogue(entity)
		local dialogueName

		if entity.name == 'Pr. Noname' then

			dialogueName = 'default'

		elseif entity.name == 'Cat' then

			dialogueName = 'default'

		elseif entity.name == 'Coffee' then

			if table.contains(self.playerInteractions, entity) then
				dialogueName = 'default'			
			else
				dialogueName = 'late'
			end

		elseif entity.name == 'TV' then

			if table.contains(self.playerInteractions, entity) then
				dialogueName = 'default'
			else
				dialogueName = 'late'
			end
			
		end

		if dialogueName then
			setCurrentDialogue(Dialogue('res/dials/' .. entity.dialogues .. '/' .. dialogueName))
		end
end

return scene