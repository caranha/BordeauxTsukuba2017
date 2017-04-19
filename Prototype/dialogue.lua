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

function Dialogue:__init(file, e)
	self.dialog = require(file)
	self.interlocutor = e

	self.currentExchange = self.dialog[1]

	self.isDone = false
	self.isStarted = false

	if not self.dialog or self.dialog == {} or not self.dialog then
		self.isDone = true
	end
end

function Dialogue:start()
	self.isStarted = true
	if self.isDone then return end

	AnswerPicker.setAnswers(self, self.currentExchange.answers)
end

function Dialogue:drawMessage()
	local text = self.interlocutor.name .. ' : ' .. self.currentExchange.text

	love.graphics.printf(
		text, 
		0 , 0 , 
		love.graphics.getWidth(), 
		'center'
	)

end

function Dialogue:setCurrentExchange(index)
	self.currentExchange = self.dialog[index]

	AnswerPicker.setAnswers(self, self.currentExchange.answers)
end
