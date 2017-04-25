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

function Object:__init(x, y, name, type, imagefile, dialogues, pickable)

  local imageFile = ('res/imgs/%s'):format(imagefile)
  Sprite.__init(self, x, y, imageFile)
  self.name = name
  self.type = type
  self.dialogues = dialogues
  self.pickable = pickable
  currentScene[self.name] = self
  if self.name == "Pr. Noname" then
    for i=1, 5 do
      self.mouvements:push_right(0)
    end
    self.mouvements:push_right(currentScene.pickDialogue)
    self.mouvements:push_right(self)
    self.mouvements:push_right(function(p) p.animation = true end)
    self.mouvements:push_right(player)
  end
  self.isMoving = true
end

function Object:disappear()
  if not self.removed then
    self.removed = true
    removeObject(self)
    world:remove(self)
  end
end

function Object:pickedBy(e)
  e:addToInventory(self)
  self:disappear()
end

function Object:interactWithPlayer(scene, player)
  if self.dialogues then
    scene:pickDialogue(self)
  end
  if self.pickable then
    self:pickedBy(player)
  end
end

function Object:update(dt, scene)
  if not self.removed and self.type ~= "mapchanger" and self.type ~= "item" then
    Sprite.update(self, dt, scene)
  end
end

function Object:draw()
  if not self.removed then
    Sprite.draw(self)
  end
end