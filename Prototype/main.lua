local sti = require 'lib.sti'
local bump = require 'lib.bump'
local gamera = require 'lib.gamera'

require 'entities.animation'
require 'entities.sprite'
require 'entities.player'
require 'entities.object'
require 'dialogue'
require 'narration'
require 'gui.button'
require 'answerpicker'

local camera = {}
local player = {} 
local objects = {}

world, map, currentMap = nil, nil, nil

-- local currentNarration = Narration('res/narrs/controls.txt')
-- local currentDialogue
-- currentNarration:setOnFinished(function() setCurrentDialogue(Dialogue('res/dials/welcome', {name = 'Pr. Noname'})) end)

function buildCamera()
    camera = gamera.new(0,0, 20000, 20000)
    camera:setScale(4.0)
    camera:setPosition(player.x, player.y)
end

function loadMapAndWorld(mapName, spawnName)

    currentMap = mapName
    mapFile = ('res/maps/%s.lua'):format(mapName)

    sti:flush()
    objects = {}
    
    world = bump.newWorld(8)
    map = sti(mapFile, {"bump"})
    map:bump_init(world)

    local spriteLayer = map.layers["Sprites"]
    spriteLayer.sprites = {}

    for k, obj in pairs(map.objects) do

        if obj.type then
            if obj.type == 'spawn' then 
                if obj.name == spawnName then
                    player.x, player.y = obj.x, obj.y
                    table.insert(spriteLayer.sprites, player)
                    world:add(player, player.x, player.y + player.height/2, player.width, player.height/2)
                end
            else
                print(obj.name, obj.type)
                local object = Object(
                    obj.x, obj.y, 
                    obj.name, 
                    obj.type, 
                    obj.properties.imagefile)
                
                table.insert(spriteLayer.sprites, object)
                table.insert(objects, object)

                world:add(object, object.x, object.y + object.height/2, object.width, object.height/2)
            end
        end
    end

    function spriteLayer:update(dt)
        for _, object in pairs(self.sprites) do
            object:update(dt, world)
        end
    end

    function spriteLayer:draw()
        table.sort(self.sprites, function (a,b) return a.y < b.y end) 
        for _, object in pairs(self.sprites) do

            if object.type ~= 'mapchanger' then
                object:draw()
            end
        end
    end

    buildCamera()
    map:removeLayer('Objects')
end

function love.load()

    love.graphics.setFont(love.graphics.newFont(20))
    love.graphics.setDefaultFilter( 'nearest', 'nearest' )
    love.window.setTitle("Prototype")
    
    player = Player(0,0)    

    loadMapAndWorld('start', 'home')

end

function love.update(dt)

    if (not currentNarration or not currentNarration.isBlocking or currentNarration.isDone)
        and (not currentDialogue or currentDialogue.isDone) then
        map:update(dt)
        updateAnimations(dt)

    end
    AnswerPicker.update()
end


function drawPlayerInventory()
    for _, obj in pairs(player.inventory) do
        obj:draw()
    end
end

function love.draw()
    love.graphics.setColor(255, 255, 255)

    camera:setPosition(player.x, player.y)

    -- Draw the map
    camera:draw(
        function(l,t,w,h)
            map:draw()
        end
    )
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

function setCurrentNarration(narration)
    currentNarration = narration
end

function setCurrentDialogue(dialog)
    currentDialogue = dialog
end