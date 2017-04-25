require 'entities.animation'
require 'entities.sprite'
require 'entities.player'
require 'entities.object'
require 'gui.button'

require 'dialogue'
require 'narration'
require 'answerpicker'

currentNarration, currentDialogue = nil, nil

function setCurrentNarration(narration)
  currentNarration = narration
end

function setCurrentDialogue(dialogue)
  currentDialogue = dialogue 
end


function setCurrentScene(sceneName)
  currentScene = require('scenes/' .. sceneName)
  currentScene:init()
end

function love.load()

  love.graphics.setFont(love.graphics.newFont(20))
  love.graphics.setDefaultFilter( 'nearest', 'nearest' )
  love.window.setTitle("Prototype")

  player = Player(0,0)    

  setCurrentScene('scene1')

end

function love.update(dt)

  if (not currentNarration or not currentNarration.isBlocking or currentNarration.isDone)
  and (not currentDialogue or currentDialogue.isDone) then

    currentScene.currentMap:update(dt)

  end

  AnswerPicker.update()
  updateAnimations(dt)
end


function drawPlayerInventory()

  love.graphics.push()
  love.graphics.scale(4)

  for _, obj in pairs(player.inventory) do
    obj:draw()
  end

  love.graphics.pop()
end

function drawItemsName()
    local items, len = player:getObjectsInRange(currentScene, 32,32)

    for _, item in pairs(items) do

        if item.name and item.type ~= 'mapchanger' then
            love.graphics.push()

            local x, y = currentScene.camera:toScreen(item.x, item.y)
            local width, _ = currentScene.camera:toScreen(item.width, 0)

            local text = love.graphics.newText(love.graphics.getFont(), item.name)

            love.graphics.setColor(255,255,255, 150)
            love.graphics.rectangle(
                "fill", 
                x + width/2 - text:getWidth()/2 - 10, 
                y - 40, 
                text:getWidth() + 20, 
                math.ceil(text:getWidth() / love.graphics.getWidth()) * text:getHeight() )

            love.graphics.setColor(0,0,0)

            love.graphics.printf(
                item.name, 
                x + width/2 - text:getWidth()/2, y - 40, 
                text:getWidth(), 
                'center'
            )

            love.graphics.pop()
        end
    end
end

function love.draw()
  love.graphics.setColor(255, 255, 255)

  updateCameraPosition(currentScene)
  -- Draw the map
  currentScene.camera:draw( function(l,t,w,h) currentScene.currentMap:draw() end )

  -- Draw the player's inventory
  drawPlayerInventory()
  drawItemsName()
  if currentNarration then
    if not currentNarration.isStarted then currentNarration:nextLine() end
    if not currentNarration.isDone then currentNarration:printLine() end
  end

  if currentDialogue then
    if not currentDialogue.isStarted then currentDialogue:start() end
    if not currentDialogue.isDone then 
      currentDialogue:drawMessage()
      AnswerPicker.draw() 
    end
  end
end

function love.keypressed(key)

  local scancode = love.keyboard.getScancodeFromKey(key)

  if scancode == 'escape' then love.event.quit()
  elseif scancode == 'space' then player:interact(currentScene)
  elseif scancode == 'return' and currentNarration and not currentNarration.isDone then currentNarration:nextLine()
  end

end

function love.mousepressed(x, y, button, isTouch)
  for _, btn in pairs(AnswerPicker.buttons) do
    if btn:mousehover() and button == 1 then
      btn.func(btn.args)
    end
  end
end

function removeObject(o)
  for i, object in pairs(objects) do
    if object == o then
      objects[i] = nil
      break
    end
  end 
end
