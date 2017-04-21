Player = {}
Player.__index = Player

setmetatable(Player, {
    -- To make Player extends Sprite 
    __index = Sprite,
    -- To get a constructor "Java like"
	__call = function (cls, ...)
        local self = setmetatable({}, cls)
        self:__init(...)
        return self
        end,
    }
)

local speed = 128
local imageFile = 'res/imgs/player.png'

function Player:__init(x, y)
	Sprite.__init(self, x, y, imageFile)
    self.name = 'Player'
    self.inventory = {}
    self.kindness = 0
end

function Player:update(dt, world)
    Sprite.update(self, dt, world)

    local dx, dy = 0, 0 
    
    if love.keyboard.isScancodeDown('d') then
        dx = speed * dt
    elseif love.keyboard.isScancodeDown('a') then
        dx = -speed * dt
    elseif love.keyboard.isScancodeDown('s') then
        dy = speed * dt
    elseif love.keyboard.isScancodeDown('w') then
        dy = -speed * dt
        
    end

    if dx ~= 0 or dy ~= 0 then
        local xBefore, yBefore = self.x, self.y
        self.x, self.y, cols, cols_len = world:move(self, self.x + dx, self.y + dy + self.height/2)
        self.y = self.y - self.height/2
    end

    local items, len = self:getObjectsInRange(1,1)

    for _, item in pairs(items) do

        if item.type == 'mapchanger' then
            loadMapAndWorld(item.name, currentMap)
            break
        end
    end
end

function Player:interact()
    local centerX, centerY = self:getCenter() 

    local items, len = self:getObjectsInRange(1,1)

    if len > 0 then
        local item = items[1]
        item:interactWith(self)
    end
end

function Player:getObjectsInRange(h, v)
    return world:queryRect(
            self.x - h,
            self.y - v,
            self.width + 2*h,
            self.height + 2*v,  
            function(e) return e ~= self and e.interactWith end
        )
end

function Player:draw()
    Sprite.draw(self)

    local items, len = self:getObjectsInRange(32,32)

    for _, item in pairs(items) do
        if item.type == 'mapchanger' then
            item:draw()
        end
    end
end

function Player:addToInventory(o)

    o.width = 16
    o.height = 16
    o.y = 4

    if #self.inventory > 0 then
        o.x = self.inventory[#self.inventory].x + 4
    else
        o.x = 4
    end 

    self.inventory[#self.inventory + 1] = o

end