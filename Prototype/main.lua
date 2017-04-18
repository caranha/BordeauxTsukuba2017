local sti = require 'lib.sti'
local bump = require 'lib.bump'

require 'lib.EGS.Class'
require 'lib.EGS.GUIElements.GUIMain'

require 'entities.sprite'
require 'entities.player'
require 'entities.object'
require 'entities.interaction.answer'
require 'entities.interaction.message'

local world, map
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
end

function love.draw()
	-- Scale world
    local scale = 4
    local screen_width = love.graphics.getWidth() / scale
    local screen_height = love.graphics.getHeight() / scale

    -- Translate world so that player is always centred
    local tx = math.floor(player.x - (screen_width - player.width) / 2)
    local ty = math.floor(player.y - (screen_height - player.width) / 2)

    -- Transform world
    love.graphics.scale(scale, scale)
    love.graphics.translate(-tx, -ty)

    -- Draw world
    map:draw()

    love.graphics.translate(tx, ty)
    love.graphics.scale(1/scale)
    GUIDraw()
end

function love.keypressed(key)
    if key == 'escape' then love.event.quit()
    elseif key == 'space' then player:interact() 
    end 
end
