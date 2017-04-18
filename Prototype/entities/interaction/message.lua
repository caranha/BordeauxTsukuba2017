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
  self.sent = false
  self.done = false
end

function Message:update(dt)
  if not self.done then
    self.elapsed = self.elapsed + dt
    if self.elapsed > self.delay then
      self.progress = self.elapsed / (self.timeToAnswer + self.delay)
      if not self.sent then
        self.sent = true
        setAnswers("Hey", "Ho")
        revealAnswers()
      end
    end
    if self.elapsed > self.timeToAnswer + self.delay then
      hideAnswers()
      self.done = true
    end
  end
end

function Message:draw(x, y)
  love.graphics.setColor(0, 0, 0, 255 - 255 * self.progress)
  love.graphics.draw(self.text, x, y)
end