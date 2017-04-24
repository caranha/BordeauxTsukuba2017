AnswerPicker = {}
AnswerPicker.buttons = {}

function AnswerPicker.setAnswers(dialog, answers)
  AnswerPicker.buttons = {}

  if answers then
    for i, answer in pairs(answers) do
      local flags = { 
      x = (i - 1) * love.graphics.getWidth() / #answers,
      y  = love.graphics.getHeight() * 0.8,
      width = love.graphics.getWidth() / #answers,
      height = love.graphics.getHeight() * 0.2,
      text = answer.text,
      args = answer.nextIndex,
      func = function(id)
          if answer.callback then
            answer.callback(player, currentScene)
            print(player.isLate)
          end
          if id then
            dialog:setCurrentExchange(id)
          else
            dialog:finish()
          end
        end
      }
      table.insert(AnswerPicker.buttons, Button(flags))
    end
  else

    function AnswerPicker.update()
      if love.keyboard.isScancodeDown('w')
        or love.keyboard.isScancodeDown('a')
        or love.keyboard.isScancodeDown('s')
        or love.keyboard.isScancodeDown('d') then
        dialog:finish()
      end
    end

  end
end

function AnswerPicker.update()
  for _, button in pairs(AnswerPicker.buttons) do
    button:update()
  end
end

function AnswerPicker:draw()
  for _, button in pairs(AnswerPicker.buttons) do
    button:draw()
  end
end
