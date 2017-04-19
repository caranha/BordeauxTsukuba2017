Button = {}
Button.__index = Button

setmetatable(Button,{ 
  	-- To get a constructor "Java like"
	__call = function (cls, ...)
		local self = setmetatable({}, cls)
		self:__init(...)
		return self
	end,
	}
)

function Button:__init(o)
	self.text = o.text or ''
	self.color = o.color or {150,150,150}
	self.hovercolor = o.hovercolor or {self.color[1] + 50, self.color[2] + 50, self.color[3] + 50}
	self.currentcolor = self.color
	self.x = o.x or 0
	self.y = o.y or 0
	self.width = o.width or 0
	self.height = o.height or 0
	self.func = o.func
	self.args = o.args
end

function Button:mousehover()

	local x, y = love.mouse.getPosition()

	return x < self.x + self.width
		and x > self.x
		and y < self.y + self.height
		and y > self.y 
end

function Button:update()

	if self:mousehover() then
    	self.currentcolor = self.hovercolor
	else
		self.currentcolor = self.color
	end

end

function Button:draw()
	love.graphics.push()
	love.graphics.setColor(self.currentcolor)

	love.graphics.rectangle("fill", self.x, self.y, self.width, self.height)
	love.graphics.setColor(0, 0, 0)
	love.graphics.rectangle("line", self.x, self.y, self.width, self.height)
	love.graphics.printf(self.text, self.x, self.y + self.height / 4, self.width, 'center')

	love.graphics.pop()
end

