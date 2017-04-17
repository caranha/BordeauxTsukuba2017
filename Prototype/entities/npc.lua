NPC = {}
NPC.__index = NPC

setmetatable(NPC, {
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
local imageFile = 'res/npc.png'

function NPC:__init(x, y, world, name)
	Sprite.__init(self, x, y, imageFile, world)
    self.name = name
end

function NPC:draw()
    Sprite.draw(self)
    love.graphics.printf(self.name, self.x, self.y - 16, self.width, 'center')
end

function NPC:interactWith(e)
    if getmetatable(e) == Player then
        print('Interacting with player !')
    end
end
