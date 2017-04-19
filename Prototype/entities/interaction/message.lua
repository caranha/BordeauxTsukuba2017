Message = {}
Message.__index = Message

setmetatable(Message, {
	__call = function (cls, ...)
  local self = setmetatable({}, cls)
  self:__init(...)
  return self
  end,
  })

function Message:__init(content, delay, timeToAnswer, emitter, receiver, possibleAnswers)
  self.text = love.graphics.newText(love.graphics.getFont(), content)
  self.delay = delay
  self.timeToAnswer = timeToAnswer
  self.elapsed = 0
  self.progress = 0
  self.sent = false
  self.emitter = emitter
  self.receiver = receiver
  self.possibleAnswers = possibleAnswers
end

-- return false if the message no longer needs to be updated
function Message:update(dt)
    self.elapsed = self.elapsed + dt
    if self.elapsed > self.delay then
      self.progress = self.elapsed / (self.timeToAnswer + self.delay)
      if not self.sent and self.possibleAnswers ~= nil then
        self.sent = true
        setAnswers(self.possibleAnswers, self.emitter, self.receiver)
        revealAnswers()
      end
    end
    if self.elapsed > self.timeToAnswer + self.delay then
      hideAnswers()
      return false
    end
    return true
end

function Message:draw(x, y)
  love.graphics.setColor(0, 0, 0, 255 - 255 * self.progress)
  love.graphics.draw(self.text, x, y)
end