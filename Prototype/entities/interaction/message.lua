Message = {}
Message.__index = Message

setmetatable(Message, {
	__call = function (cls, ...)
		local self = setmetatable({}, cls)
		self:__init(...)
		return self
	end,
})

function Message:__init(content, delay, timeToAnswer)
    self.text = love.graphics.newText(love.graphics.getFont(), content)
    self.delay = delay
    self.timeToAnswer = timeToAnswer
    self.elapsed = 0
    self.progress = 0
end

function Message:update(dt)
    self.elapsed = self.elapsed + dt
    if self.elapsed > self.delay then
        self.progress = self.elapsed / (self.timeToAnswer + self.delay)
        initAnswer({"a", "b", "c", "d"})
    end
end

function Message:draw(x, y)
    love.graphics.setColor(0, 0, 0, 255 - 255 * self.progress)
    love.graphics.draw(self.text, x, y)
end