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

local speed = 72
local imageFile = 'res/imgs/player.png'

function Player:__init(x, y)
	Sprite.__init(self, x, y, imageFile)
    self.inventory = {}
    self.kindness = 0
    self.intelligence = 0
    self.stealth = 0
end

function Player:update(dt)
    Sprite.update(self, dt)
    --local dx, dy = 0, 0
    local dx, dy = 0, 0 
    
    if love.keyboard.isDown('right') then
        --dx = speed * dt
        dx = 8
    elseif love.keyboard.isDown('left') then
        --dx = -speed * dt
        dx = -8
    elseif love.keyboard.isDown('down') then
        --dy = speed * dt
        dy =  8
    elseif love.keyboard.isDown('up') then
        --dy = -speed * dt
        dy = -8
        
    end

    if dx ~= 0 or dy ~= 0 then
        local xBefore, yBefore = self.x, self.y
        self.x, self.y, cols, cols_len = world:move(self, self.x + dx, self.y + dy)
        Animation(self, "x", xBefore, self.x, 0.1, "walk")
        Animation(self, "y", yBefore, self.y, 0.1, "walk")
    end
    local actualX, actualY, cols, len = world:check(self, self.x, self.y)
    for i=1,len do
        print(cols[i].type)
    end
end

function Player:interact()
    local centerX, centerY = self:getCenter() 

    local items, len = 
        world:queryRect(
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