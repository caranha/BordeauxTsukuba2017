Answer = {}
for i=1, 2 do
  TextButton:new("answer" .. i)
end
Answer.__buttons = { answer1, answer2 }

for i, button in ipairs(Answer.__buttons) do    
    button.size = { 
    love.graphics.getWidth() * (1 / #Answer.__buttons),
    love.graphics.getHeight() * 0.2
  }
  button.position = {
    0  + (i - 1) * love.graphics.getWidth() * (1 / #Answer.__buttons), love.graphics.getHeight() * 0.8
  }
  button.__revealPosition = { button.position[1], button.position[2] }
  button.__hiddenPosition = { button.position[1], button.position[2] + button.size[2] }
  button.position = { button.__hiddenPosition[1], button.__hiddenPosition[2] }
  button.mouseButtonLUp:connect(function()
      hideAnswers()
  end)
  button.borderColor = { 255, 255, 255 }
end


function revealAnswers()
  for i, button in ipairs(Answer.__buttons) do
      Animation(button.position, 2, button.position[2], button.__revealPosition[2], 0.5)
  end
end

function hideAnswers()
  for i, button in ipairs(Answer.__buttons) do
      Animation(button.position, 2, button.position[2], button.__hiddenPosition[2], 0.5)
  end
end

function setAnswers(answer1, answer2)
  Answer.__buttons[1].text = answer1
  Answer.__buttons[2].text = answer2
end
