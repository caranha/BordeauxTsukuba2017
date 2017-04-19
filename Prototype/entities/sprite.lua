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
    self.world= world
    self.world:add(self, x, y, self.width, self.height)
    self.isTalking = nil
end

function Sprite:update(dt)
    if self.isTalking then
        if not self.message:update(dt) then
          self.isTalking = nil
        end
    end
end

function Sprite:draw()
  love.graphics.setColor(255, 255, 255)
	love.graphics.draw(
		self.image,
		math.floor(self.x),
		math.floor(self.y),
		0,
		1,
		1
	)
  if self.isTalking then
      self:drawMessage()
  end
end

function Sprite:getCenter()
	return self.x + self.width/2 , self.y + self.height/2
end

function Sprite:drawMessage()
    self.message:draw(2 * self.x - self.isTalking.x , 2 * self.y - self.isTalking.y)
end

function Sprite:interactWith(e)
    self.message = Message("Hi", 2, 4, e, self, { "yo", "hey" })
    self.isTalking = e
end