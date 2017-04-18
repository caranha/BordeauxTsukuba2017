local sti = require 'lib.sti'
local bump = require 'lib.bump'

require 'lib.EGS.Class'
require 'lib.EGS.GUIElements.GUIMain'

require 'entities.sprite'
require 'entities.player'
require 'entities.object'
require 'entities.interaction.answer'
require 'entities.interaction.message'
require 'entities.animation'

local world, map
local camera = {zoom, x, y, width, height, marginHorizontal, marginVertical}
local player
local objects = {}
local objects_count = 0
local dialog


function love.load()
    love.graphics.setDefaultFilter( 'nearest', 'nearest' )
    love.window.setTitle("Prototype")
    
    -- Load map file and bump world for collisions
    world = bump.newWorld(8)
    map = sti("res/map.lua", {"bump"})
    map:bump_init(world)

    -- Create the player at his spawn 
    for k, object in pairs(map.objects) do
        if object.name == 'spawn' then
            player = Player(object.x, object.y, world)
        end

        if object.type == 'npc' then
            objects_count = objects_count + 1
            objects[objects_count] = Object(object.x, object.y, world, object.name)
        end
    end

    camera.zoom = 4
    camera.x = player.x + player.width / 2 
    camera.y = player.y + player.height / 2 
    camera.width = love.graphics.getWidth() / camera.zoom
    camera.height = love.graphics.getHeight() / camera.zoom
    camera.marginHorizontal = camera.width / 4
    camera.marginVertical = camera.height / 4

    map:addCustomLayer("Sprite Layer", 4)
    local spriteLayer = map.layers["Sprite Layer"]
    
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
    map:removeLayer('Character Objects')
end

function love.update(dt)
    map:update(dt)
    GUIUpdate(dt)
    updateAnimations(dt)
end


function love.draw()

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
        Animation(camera, "x", camera.x, camera.x - correction, 0.5) 
        --camera.x = camera.x - correction  
    end
    
    if math.abs(playerCamOffsetY) > camera.height/2 - camera.marginVertical then
        local correction
        if playerCenterY > camera.y then
            correction = playerCamOffsetY + (camera.height/2 - camera.marginVertical)
        else
            correction = playerCamOffsetY - (camera.height/2 - camera.marginVertical)
        end 
        Animation(camera, "y", camera.y, camera.y - correction, 0.5) 
        --camera.y = camera.y - correction 
    end

    love.graphics.scale(camera.zoom, camera.zoom)
    love.graphics.translate(-camera.x + camera.width / 2, -camera.y + camera.height / 2)

    map:draw()

    love.graphics.translate(camera.x - camera.width/2, camera.y - camera.height/2)
    love.graphics.scale(1/camera.zoom)
    GUIDraw()
end

function love.keypressed(key)
    if key == 'escape' then love.event.quit()
    elseif key == 'space' then player:interact() 
    end 
end
