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

end

function Object:disappear()
    removeObject(self)
    world:remove(self)
end

function Object:pickedBy(e)
    e:addToInventory(self)
    self:disappear()
end

function Object:interactWithPlayer(scene, player)
    if self.dialogues then
        scene:pickDialogue(self)
    else
        self:pickedBy(player)
    end
end

