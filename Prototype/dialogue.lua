Dialogue = {}
Dialogue.__index = Dialogue

setmetatable(Dialogue, {
  	-- To get a constructor "Java like"
	__call = function (cls, ...)
		local self = setmetatable({}, cls)
		self:__init(...)
		return self
	end,
})

function Dialogue:__init(file)
	self.dialog = require(file)

	self.currentExchange = self.dialog[1]

	self.isDone = false
	self.isStarted = false

	if not self.dialog or self.dialog == {} or not self.dialog then
		self.isDone = true
	end

	self.onFinished = nil
	self.args = nil
end

function Dialogue:start()
	self.isStarted = true
	if self.isDone then return end

	AnswerPicker.setAnswers(self, self.currentExchange.answers)
end

function Dialogue:drawMessage()
	if self.isStarted and not self.isDone then

		love.graphics.setColor(0,0,0)

		local text = love.graphics.newText(love.graphics.getFont(), self.currentExchange.text)

		love.graphics.setColor(255,255,255, 150)
		love.graphics.rectangle(
			"fill", 
			(love.graphics.getWidth() - text:getWidth())/2 - 10, 
			0, 
			text:getWidth() + 20, 
			math.ceil(text:getWidth() / love.graphics.getWidth()) * text:getHeight() + 20)

		love.graphics.setColor(0,0,0)
		love.graphics.printf(
			self.currentExchange.text, 
			0 , 10 , 
			love.graphics.getWidth(), 
			'center'
		)
	end
end

function Dialogue:setCurrentExchange(index)
	self.currentExchange = self.dialog[index]

	AnswerPicker.setAnswers(self, self.currentExchange.answers)
end

function Dialogue:setOnFinished(func, args)
	self.onFinished = func
	self.args = args
end

function Dialogue:finish()
	self.isDone = true
	if self.onFinished then
		self.onFinished(self.args)
	end
end