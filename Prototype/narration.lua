Narration = {
	texts = {}
}
Narration.__index = Narration

setmetatable(Narration, {
  	-- To get a constructor "Java like"
	__call = function (cls, ...)
		local self = setmetatable({}, cls)
		self:__init(...)
		return self
	end,
})

function Narration:__init(filename)

	self.lines = {}

	for line in io.lines(filename) do
		self.lines[#self.lines + 1] = line
	end


	self.isDone = false
	self.isStarted = false
	self.currentLine = 0

	if #self.lines == 0 then
		self.isDone = true
	end
end

function Narration:nextLine()
	self.isStarted = true
	if self.isDone then return end

	self.currentLine = self.currentLine + 1

	if self.currentLine > #self.lines then
		self.isDone = true
		if self.onFinished then
			self.onFinished(self.args)
		end
		return
	end
	

end

function Narration:printLine()
	if self.isStarted and not self.isDone then

		local str = self.lines[self.currentLine] .. ' [enter]' 

		local text = love.graphics.newText(love.graphics.getFont(), str)
		
		love.graphics.setColor(255,255,255, 150)
		love.graphics.rectangle(
			"fill", 
			(love.graphics.getWidth() - text:getWidth())/2 - 10, 
			0, 
			text:getWidth() + 20, 
			math.ceil(text:getWidth() / love.graphics.getWidth()) * text:getHeight() + 20)
		
		love.graphics.setColor(0,0,0)
		love.graphics.printf(
			str, 
			0 , 10 , 
			love.graphics.getWidth(), 
			'center')

	
	end
end

function Narration:setOnFinished(func, args)
	self.onFinished = func
	self.args = args
end
