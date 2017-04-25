require 'scenes.sceneutils'

local scene = {
  camera = {},
  objects = {},
  currentWorld, 
  currentMap,
  currentMapName = 'home',
  maps = {'home', 'labo', 'bakutsu'},
  objectsAccepted = {'Cat', 'TV', 'Coffee', 'magnet', 'Pr. Noname', 'machine'},
  playerInteractions = {},
  machineBroken = false,
  endOfInteractions = false,
  finished = false
}

function scene:init()
  self.currentMap, self.currentWorld = loadMapAndWorld(self.currentMapName, 'home_upstairs', self)
  self.camera = buildCamera()
  setCurrentNarration(Narration('res/narrs/controls.txt', true))
end

function scene:pickDialogue(entity)
  local dialogueName

  if entity.name == 'Pr. Noname' then


    if table.contains(self.playerInteractions, entity.name) then

      if self.machineBroken and not self.endOfInteractions then
        dialogueName = 'playerBrokeTheMachine'
        self.endOfInteractions = true
      else
        dialogueName = 'default'
      end

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
    if not self.machineBroken and playerHasMagnet then
      self.machineBroken = true
      local pr = currentScene["Pr. Noname"]
      local machine = currentScene["machine"]
      pr:resetMouvements()
      player.z = 1
      Animation(player, "z", 1, 2, 1.5, machine.disappear, machine)
      Animation(player, "z", 1, 2, 2.2, 
        function()
          for i = 1, 5 do
            pr.mouvements:push_right(2)
          end
          pr.mouvements:push_right(self.pickDialogue)
          pr.mouvements:push_right(pr)
        end
      )
      player.animation = false      
      pr.isMoving = false
    end
  end

  if self.currentMapName == "bakutsu" then

    if self.machineBroken then
      self.finished = true
      print(player.kindness, player.naiveness)
    end

  end

end

return scene