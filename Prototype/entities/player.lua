local deque = require "lib.deque"

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
local deltaMovement = 8
local imageFile = 'res/imgs/player.png'

function Player:__init(x, y)
	Sprite.__init(self, x, y, imageFile)
    self.name = 'Player'
    self.inventory = {}
    self.naiveness = 0
    self.kindness = 0
    self.offsetX, self.offsetY = 0, 0
    self.mouvements = deque:new()
    self.isMoving = false
end

function Player:pushMove(direction)
  if self.mouvements:peek_right() ~= direction and self.mouvements:length() < 2 then
    local peekRight = self.mouvements:peek_right()
    if peekRight ~= nil and peekRight ~= direction then
      self.mouvements:pop_right()
    end
    self.mouvements:push_right(direction)
  end  
end

function Player:move(scene, dx, dy)
    dx = dx or 0
    dy = dy or 0
    self.x, self.y, cols, cols_len = scene.currentWorld:move(self, self.x + dx, self.y + dy + self.height / 2)
    self.y = self.y - self.height/2
end

function Player:moveTo(scene, x, y)
    self.x, self.y, cols, cols_len = scene.currentWorld:move(self, x, y + self.height / 2)
    self.y = self.y - self.height/2
end

function Player:endMove()
  player.isMoving = false
end

function Player:update(dt, scene)
    Sprite.update(self, dt, scene.currentWorld)
    if self.__x == nil or self.__y == nil then
      self.__x, self.__y = self.x, self.y
    end

    local dx, dy = 0, 0 
    
    if love.keyboard.isScancodeDown('d') and not self:collisionAt(scene, deltaMovement, 0) then
        self:pushMove(0)
        dx = deltaMovement
    end
    if love.keyboard.isScancodeDown('a') and not self:collisionAt(scene, -deltaMovement, 0) then
        self:pushMove(1)
        dx = -deltaMovement
    end
    if love.keyboard.isScancodeDown('s') and not self:collisionAt(scene, 0, deltaMovement) then
        self:pushMove(2)
        dy = deltaMovement 
    end
    if love.keyboard.isScancodeDown('w') and not self:collisionAt(scene, 0, -deltaMovement) then
        self:pushMove(3)
        dy = -deltaMovement       
    end

    if dx ~= 0 or dy ~= 0 then
        local xBefore, yBefore = self.x, self.y
        self:move(scene)
        if not player.isMoving then
          player.isMoving = true
          local mouvement = self.mouvements:pop_left()
          if mouvement == 0 then
            Animation(self, "x", self.x, self.x + deltaMovement, 0.1, self.endMove)
          elseif mouvement == 1 then
            Animation(self, "x", self.x, self.x - deltaMovement, 0.1, self.endMove)
          elseif mouvement == 2 then
            Animation(self, "y", self.y, self.y + deltaMovement, 0.1, self.endMove)
          elseif mouvement == 3 then
            Animation(self, "y", self.y, self.y - deltaMovement, 0.1, self.endMove)
          end
        end
        self.offsetX = self.offsetX + dx
        self.offsetY = self.offsetY + dy
    end
    
    local items, len = self:getObjectsInRange(scene, 1,1)
    for _, item in pairs(items) do
        if item.type == 'mapchanger' then
            changeMap(scene, item.name)
            break
        end
    end
end


function Player:interact(scene)

    local items, len = self:getObjectsInRange(scene, 1,1)

    if len > 0 
        and (not currentDialogue or currentDialogue.isDone) 
        and (not currentNarration or currentNarration.isDone) then
        
        local item = items[1]

        item:interactWithPlayer(scene, self)
        
        if not table.contains(scene.playerInteractions, item.name) then
            table.insert(scene.playerInteractions, item.name)
        end

    end
end

function Player:getObjectsInRange(scene, h, v)
    return scene.currentWorld:queryRect(
            self.x - h,
            self.y + self.height/2 - v,
            self.width + 2*h,
            self.height/2 + 2*v,  
            function(e) return e ~= self and e.interactWithPlayer end
        )
end

function Player:collisionAt(scene, dx, dy)
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

function Player:draw(scene)
    Sprite.draw(self, scene)

    local items, len = self:getObjectsInRange(scene, 32,32)

    for _, item in pairs(items) do
        if item.type == 'mapchanger' then
            item:draw(scene)
        end
    end
end

function Player:addToInventory(o)

    o.width = deltaMovement
    o.height = deltaMovement
    o.y = 4

    if #self.inventory > 0 then
        o.x = self.inventory[#self.inventory].x + 4
    else
        o.x = 4
    end 

    self.inventory[#self.inventory + 1] = o
end