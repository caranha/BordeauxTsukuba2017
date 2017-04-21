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

function Object:__init(x, y, name, type, imagefile, dialogues)

    local imageFile = ('res/imgs/%s'):format(imagefile)

	Sprite.__init(self, x, y, imageFile)
    self.name = name
    self.type = type
    self.dialogues = dialogues

    if self.dialogues then 
        self.dialogue = dialogues .. '/default'
    end
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
    if self.dialogue then
        if self.type == 'npc' then
            setCurrentDialogue(Dialogue('res/dials/' .. self.dialogue , self, e))
        elseif self.type == 'item' then
            setCurrentDialogue(Dialogue('res/dials/' .. self.dialogue , e, e))
        end
    else
        self:pickedBy(e)
    end
end

function setDialogue(dialogName)
    if self.dialogues then
        self.dialogue = dialogues .. '/' .. dialogName
    end
end