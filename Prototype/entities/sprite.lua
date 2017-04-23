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

end

function Sprite:update(dt, scene)
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

