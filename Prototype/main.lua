local sti = require 'lib.sti'
local bump = require 'lib.bump'

require 'entities.animation'
require 'entities.sprite'
require 'entities.player'
require 'entities.object'
require 'dialogue'
require 'narration'
require 'gui.button'
require 'answerpicker'

local camera = {zoom, x, y, width, height, marginHorizontal, marginVertical}
local player
local objects = {}
local dialog

local currentNarration = Narration('res/narrs/intro.txt')
local currentDialogue

function love.load()

    love.graphics.setFont(love.graphics.newFont(20))

    love.graphics.setDefaultFilter( 'nearest', 'nearest' )
    love.window.setTitle("Prototype")
    
    -- Load map file and bump world for collisions
    world = bump.newWorld(8)
    map = sti("res/maps/start.lua", {"bump"})
    map:bump_init(world)

    -- Create the player at his spawn 
    for k, object in pairs(map.objects) do
        if object.name == 'spawn' then
            player = Player(object.x, object.y)
        elseif object.type and object.type ~= '' then
            objects[#objects + 1] = Object(object.x, object.y, object.name, object.type, object.properties.imagefile)
        end
    end

    camera.zoom = 4
    camera.x = player.x + player.width / 2 
    camera.y = player.y + player.height / 2 
    camera.width = love.graphics.getWidth() / camera.zoom
    camera.height = love.graphics.getHeight() / camera.zoom
    camera.marginHorizontal = camera.width / 4
    camera.marginVertical = camera.height / 4

    map:addCustomLayer("Sprites", 4)
    local spriteLayer = map.layers["Sprites"]
    
    function spriteLayer:update(dt)
        player:update(dt)
        for _, object in pairs(objects) do
            object:update(dt)
        end
    end

    function spriteLayer:draw()
        player:draw()
        for _, object in pairs(objects) do
            object:draw()
        end
    end

    -- Remove the object layer as it is not needed anymore
    map:removeLayer('Objects')
end

function love.update(dt)

    if (not currentNarration or currentNarration.isDone)
        and (not currentDialogue or currentDialogue.isDone) then
        map:update(dt)
        updateAnimations(dt)
    end

    AnswerPicker.update()
end


function drawPlayerInventory()
    for _, obj in pairs(player.inventory) do
        love.graphics.push()

        love.graphics.scale(camera.zoom, camera.zoom)

        obj:draw()

        love.graphics.pop()
    end
end

function love.draw()
    love.graphics.setColor(255, 255, 255)
    local playerCenterX, playerCenterY = player:getCenter()

    local playerCamOffsetX, playerCamOffsetY = 
        camera.x - playerCenterX,
        camera.y - playerCenterY

    if math.abs(playerCamOffsetX) > camera.width/2 - camera.marginHorizontal then
        local correction
        if playerCenterX > camera.x then
            correction = playerCamOffsetX + (camera.width/2 - camera.marginHorizontal)
        else
            correction = playerCamOffsetX - (camera.width/2 - camera.marginHorizontal)
        end 
        Animation(camera, "x", camera.x, camera.x - correction, 0.4) 
    end
    
    if math.abs(playerCamOffsetY) > camera.height/2 - camera.marginVertical then
        local correction
        if playerCenterY > camera.y then
            correction = playerCamOffsetY + (camera.height/2 - camera.marginVertical)
        else
            correction = playerCamOffsetY - (camera.height/2 - camera.marginVertical)
        end 
        Animation(camera, "y", camera.y, camera.y - correction, 0.4)
    end

    -- Draw the map
    love.graphics.push()
    love.graphics.scale(camera.zoom, camera.zoom)
    love.graphics.translate(-camera.x + camera.width / 2, -camera.y + camera.height / 2)
    map:draw()
    love.graphics.pop()

    -- Draw the player's inventory
    drawPlayerInventory()
    
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
    if key == 'escape' then love.event.quit()
    elseif key == 'space' then player:interact()
    elseif key == 'return' and not currentNarration.isDone then currentNarration:nextLine()
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

function setCurrentNarration(filename)
    currentNarration = Narration(filename)
end

function setCurrentDialogue(dialog)
    currentDialogue = dialog
end