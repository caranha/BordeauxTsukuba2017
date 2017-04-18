function initAnswer(texts)
    for i=1,#texts do
      TextButton:new("TB" .. i)
    end
    local buttons = {TB1, TB2, TB3, TB4}
    
    for i, button in ipairs(buttons) do    
      button.size = { 
        love.graphics.getWidth() * 0.25,
        love.graphics.getHeight() * 0.2
      }
      button.position = {
        0  + (i - 1) * love.graphics.getWidth() * 0.25, love.graphics.getHeight() * 0.8
      }
      button.text = texts[i]
      button.borderColor = { 255, 255, 255 }
    end
end
