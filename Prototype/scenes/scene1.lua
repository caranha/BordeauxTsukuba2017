require 'scenes.sceneutils'

local scene = {
  camera = {},
  objects = {},
  currentWorld, 
  currentMap,
  currentMapName = 'labo',
  maps = {'home', 'labo', 'bakutsu'},
  objectsAccepted = {'Cat', 'TV', 'Coffee', 'magnet', 'Pr. Noname', 'machine'},
  playerInteractions = {},
  machineBroken = false
}

function scene:init()
  self.currentMap, self.currentWorld = loadMapAndWorld(self.currentMapName, 'bakutsu', self)
  self.camera = buildCamera() 
end

function scene:pickDialogue(entity)
  local dialogueName

  if entity.name == 'Pr. Noname' then


    if table.contains(self.playerInteractions, entity.name) then

      dialogueName = 'default'

    else

      if self.machineBroken then
        dialogueName = 'magnetBrokeTheMachine'
      elseif player.isLate then
        dialogueName = 'playerLate'
      else
        dialogueName = 'playerNotLate'
      end

    end 

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
    if table.contains(self.playerInteractions, "Pr. Noname") then 
      dialogueName = 'interaction'
    else
      dialogueName = 'default'
    end
  end

  if dialogueName then
    setCurrentDialogue(Dialogue('res/dials/' .. entity.dialogues .. '/' .. dialogueName))
  end
end

function scene:onMapChanged()
  if self.currentMapName == "labo" then

    local playerHasMagnet = false
    
    for i, item in pairs(player.inventory) do
      if item.name == "magnet" then
        playerHasMagnet = true
        player.inventory[i] = nil
        break
      end
    end



    if self.machineBroken and playerHasMagnet then
      self.machineBroken = true
      local pr = currentScene["Pr. Noname"]
      local machine = currentScene["machine"]
      pr:resetMouvements()
      player.z = 1
      Animation(player, "z", 1, 2, 1.5, machine.disappear, machine)
      Animation(player, "z", 1, 2, 2.2, 
        function()
          for i = 1, 5 do
            pr.mouvements:push_left(2)
          end
        end
      )
      player.animation = false      
      pr.isMoving = false
    end
  end
end

return scene