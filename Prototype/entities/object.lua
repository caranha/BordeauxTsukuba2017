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

function Object:__init(x, y, name, type, imagefile)

    local imageFile = ('res/imgs/%s'):format(imagefile)

	Sprite.__init(self, x, y, imageFile)
    self.name = name
    self.type = type
end

function Object:disappear()
    removeObject(self)
    world:remove(self)
end

function Object:pickedBy(e)
    e:addToInventory(self)
    self:disappear()
end

function Object:interactWith(e)
    if self.type == 'npc' then
        Sprite.interactWith(self, e)
    elseif self.type == 'item' then
        self:pickedBy(e)
    end
end