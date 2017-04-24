require 'scenes.sceneutils'

local scene = {

	camera = {},
	objects = {},

	currentWorld, 
	currentMap,

	currentMapName = 'labo',

	maps = {'home', 'labo', 'start'},
	objects = {'Cat', 'TV', 'Coffee', 'magnet', 'Pr. Noname', 'machine'},

	playerInteractions = {} 
}

function scene:init()
	self.currentMap, self.currentWorld = loadMapAndWorld(self.currentMapName, 'start', self)
	self.camera = buildCamera() 
end

function scene:pickDialogue(entity)
		local dialogueName

		if entity.name == 'Pr. Noname' then
			dialogueName = 'playerLate'
		elseif entity.name == 'Cat' then

			if table.contains(self.playerInteractions, entity.name) then
        		if player.__hasFedCat then
          			dialogueName = 'happy'
        		else
          			dialogueName = 'unhappy'
        		end

			else
				dialogueName = 'feed'
			end
      
		elseif entity.name == 'Coffee' then

			if table.contains(self.playerInteractions, entity.name) then
				dialogueName = 'default'			
			else
				dialogueName = 'late'
			end

		elseif entity.name == 'TV' then

			if table.contains(self.playerInteractions, entity.name) then
				dialogueName = 'default'
			else
				dialogueName = 'late'
			end
		
  elseif entity.name == 'machine' then    
    analyse(entity)
      if table.contains(self.playerInteractions, entity.name) then 
        dialogueName = 'default'
      else
        dialogueName = 'default'
      end
		end

		if dialogueName then
			setCurrentDialogue(Dialogue('res/dials/' .. entity.dialogues .. '/' .. dialogueName))
		end
end

return scene