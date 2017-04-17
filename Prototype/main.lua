local sti = require 'lib.sti'
local bump = require 'lib.bump'
local bump_debug = require 'lib.bump_debug'

require 'entities.sprite'
require 'entities.player'
require 'entities.npc'

local world, map
local player
local npcs = {}
local npcs_count = 0

function love.load()

    -- Load map file and bump world for collisions
    world = bump.newWorld(32)
    map = sti("res/map.lua", {"bump"})
    map:bump_init(world)

    -- Create the player at his spawn 
    for k, object in pairs(map.objects) do
        if object.name == 'spawn' then
            player = Player(object.x, object.y, world)
        end

        if object.type == 'npc' then
            npcs_count = npcs_count + 1
            npcs[npcs_count] = NPC(object.x, object.y, world, object.name)
        end
    end

    -- Remove the object layer as it is not needed anymore
    map:removeLayer('Objects')
end

function love.update(dt)
	map:update(dt)

    player:update(dt)

    for i=1,npcs_count do
        npcs[i]:update(dt)
    end

end

function love.draw()
	-- Scale world
    local scale = 2
    local screen_width = love.graphics.getWidth() / scale
    local screen_height = love.graphics.getHeight() / scale

    -- Translate world so that player is always centred
    local tx = math.floor(player.x - screen_width / 2)
    local ty = math.floor(player.y - screen_height / 2)

    -- Transform world
    love.graphics.scale(scale)
    love.graphics.translate(-tx, -ty)

    -- Draw world
    map:draw()
    player:draw()
    for i=1,npcs_count do
        npcs[i]:draw()
    end
    
    -- Collision debug
    bump_debug.draw(world)
    love.graphics.setColor(255, 255, 255)
end

function love.keypressed(key)
    if key == 'escape' then love.event.quit()
    elseif key == 'space' then player:interact() 
    end 
end

