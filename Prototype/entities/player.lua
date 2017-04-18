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

local speed = 96
local imageFile = 'res/player.png'

function Player:__init(x, y, world)
	Sprite.__init(self, x, y, imageFile, world)
end

function Player:update(dt)
    local dx, dy = 0, 0
    if love.keyboard.isDown('right') then
        dx = speed * dt
    elseif love.keyboard.isDown('left') then
        dx = -speed * dt
    end
        
    if love.keyboard.isDown('down') then
        dy = speed * dt
    elseif love.keyboard.isDown('up') then
        dy = -speed * dt
    end

    if dx ~= 0 or dy ~= 0 then
        self.x, self.y, cols, cols_len = self.world:move(self, self.x + dx, self.y + dy)
    end

    local actualX, actualY, cols, len = self.world:check(self, self.x, self.y)
    for i=1,len do
        print(cols[i].type)
    end

end

function Player:interact()
    local centerX, centerY = self:getCenter() 

    local items, len = 
        self.world:queryRect(
            self.x - 1,
            self.y - 1,
            self.width + 2,
            self.height + 2,  
            function(e) return e ~= self and e.interactWith end
        )

    if len > 0 then
        local item = items[1]
        item:interactWith(self)
    end
end