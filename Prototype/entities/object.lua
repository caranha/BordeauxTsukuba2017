require 'res.dialogs.welcome'

Object = {}
Object.__index = Object

setmetatable(Object, {
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

function Object:__init(x, y, world, name)
	Sprite.__init(self, x, y, imageFile, world)
    self.name = name
end