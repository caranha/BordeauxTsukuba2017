local deque = require "lib.deque"

Sprite = {}
Sprite.__index = Sprite

setmetatable(Sprite, {
    -- To get a constructor "Java like"
    __call = function (cls, ...)
      local self = setmetatable({}, cls)
      self:__init(...)
      return self
    end,
  })

function Sprite:__init(x, y, imageFile)
  self.x = x
  self.y = y
  self.image = love.graphics.newImage(imageFile)
  self.width = self.image:getWidth()
  self.height = self.image:getHeight()
  self.mouvements = deque:new()
  self.mouvements.onfinished = nil
  self.isMoving = false
end

function endMove(sprite)
  sprite.isMoving = false
end

function Sprite:move(scene, dx, dy)
  dx = dx or 0
  dy = dy or 0
  self.x, self.y, cols, cols_len = scene.currentWorld:move(self, self.x + dx, self.y + dy + self.height / 2)
  self.y = self.y - self.height/2
end

function Sprite:resetMouvements()
  while self.mouvements:length() ~= 0 do
    self.mouvements:pop_right()
  end
end

function Sprite:update(dt, scene)
  self:move(scene)
  if not self.isMoving and self.mouvements:length() ~= 0 then
    self.isMoving = true
    local mouvement = self.mouvements:pop_left()
    if mouvement == 0 and not self:collisionAt(scene, deltaMovement, 0) then
      Animation(self, "x", self.x, self.x + deltaMovement, 0.1, endMove, self)
    elseif mouvement == 1 and not self:collisionAt(scene, -deltaMovement, 0)  then
      Animation(self, "x", self.x, self.x - deltaMovement, 0.1, endMove, self)
    elseif mouvement == 2 and not self:collisionAt(scene, 0, deltaMovement)  then
      Animation(self, "y", self.y, self.y + deltaMovement, 0.1, endMove, self)
    elseif mouvement == 3 and not self:collisionAt(scene, 0, -deltaMovement)  then
      Animation(self, "y", self.y, self.y - deltaMovement, 0.1, endMove, self)
    elseif type(mouvement) == "function" then
      mouvement(currentScene, self.mouvements:pop_left())
      while type(self.mouvements:peek_left()) == "function" do
        self.mouvements:pop_left()(self.mouvements:pop_left())
      end
    else
      self.isMoving = false
    end
  end
end

function Sprite:getObjectsInRange(scene, h, v)
    return scene.currentWorld:queryRect(
            self.x - h,
            self.y - v,
            self.width + 2*h,
            self.height + 2*v,  
            function(e) return e ~= self and e.interactWithPlayer end
        )
end

function Sprite:moveTo(scene, x, y)
    self.x, self.y, cols, cols_len = scene.currentWorld:move(self, x, y + self.height / 2)
    self.y = self.y - self.height/2
end


function Sprite:collisionAt(scene, dx, dy)
  local xBefore, yBefore = self.x, self.y
  self:move(scene, dx, dy)
  local collision = xBefore + dx ~= self.x or yBefore + dy ~= self.y
  local items, len = self:getObjectsInRange(scene, 1,1)
  for _, item in pairs(items) do
      if item.type == 'mapchanger' then
        self:moveTo(scene, xBefore, yBefore)          
        return false
      end
  end    
  self:moveTo(scene, xBefore, yBefore)    
  return collision
end

function Sprite:draw(scene)
  love.graphics.setColor(255, 255, 255)
  love.graphics.draw(
    self.image,
    math.floor(self.x),
    math.floor(self.y),
    0,
    1,
    1
  )
end

function Sprite:getCenter()
  return self.x + self.width/2 , self.y + self.height/2
end

