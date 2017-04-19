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
		return
	end
	

end

function Narration:printLine()
	love.graphics.push()

    love.graphics.setFont(love.graphics.newFont(20))

	if self.isStarted and not self.isDone then
		love.graphics.printf(
			self.lines[self.currentLine] .. ' [enter]', 
			0 , 0 , 
			love.graphics.getWidth(), 
			'center')
	end

	love.graphics.pop()
end


