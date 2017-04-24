local sti = require 'lib.sti'
local bump = require 'lib.bump'
local gamera = require 'lib.gamera'

function table.contains(table, value)
	for _, val in pairs(table) do
		if val == value then return true end
	end

	return false
end

function buildCamera()
    camera = gamera.new(0,0, 20000, 20000)
    camera.__x = player.x
    camera.__y = player.y
    camera.__lastX = camera.__x
    camera.__lastY = camera.__y
    camera:setScale(4.0)
    camera:setPosition(player.x + player.width / 2, player.y + player.height / 2)

    return camera
end

function analyse(object)
  for k,v in pairs(object) do
    print(k,v)
  end
end

function updateCameraPosition(scene)
    if not isSmallScene(scene) then
      if math.abs(player.offsetX) > 16 * 7 then
        Animation(camera, "__x", camera.__x, player.x + player.width / 2, 0.5)
        player.offsetX = 0
      end
      if math.abs(player.offsetY) > 16 * 7 then
        Animation(camera, "__y", camera.__y, player.y + player.height / 2, 0.5)
        player.offsetY = 0
      end
      camera:setPosition(camera.__x, camera.__y)
    else
      camera:setPosition(0, 0)
    end
end

function isSmallScene(scene)
  local screenW, screenH, mode = love.window.getMode()
  local sceneW = scene.currentMap.width * scene.currentMap.tilewidth * camera.scale
  local sceneH = scene.currentMap.height * scene.currentMap.tileheight * camera.scale
  return sceneW < screenW and sceneH < screenH
end

function loadMapAndWorld(mapName, spawnName, scene)

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
                local object = Object(
                    obj.x, obj.y, 
                    obj.name, 
                    obj.type, 
                    obj.properties.imagefile,
                    obj.properties.dialogues,
                    obj.properties.pickable)
                
                if (object.type == 'mapchanger' 
                    and table.contains(scene.maps, object.name)) 
                    or table.contains(scene.objects, object.name) then
                    table.insert(spriteLayer.sprites, object)
                	table.insert(objects, object)
                end

                world:add(object, object.x, object.y + object.height/2, object.width, object.height/2)
            end
        end
    end

    function spriteLayer:update(dt)
        for _, object in pairs(self.sprites) do
            object:update(dt, scene)
        end
    end

    function spriteLayer:draw()
        table.sort(self.sprites, function (a,b) return a.y < b.y end) 
        
        for _, object in pairs(self.sprites) do

            if object.type ~= 'mapchanger' then
                object:draw(scene)
            end
        end
    end

    map:removeLayer('Objects')

    return map, world
end

function changeMap(scene, newMap)
    scene.currentMap, scene.currentWorld = loadMapAndWorld(newMap, scene.currentMapName, scene)
    scene.currentMapName = newMap
    scene.camera = buildCamera()
    player.offsetX, player.offsetY = 0, 0
end
