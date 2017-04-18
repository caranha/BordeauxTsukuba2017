Animation = {}
Animation.__index = Animation

setmetatable(Animation, {
    -- To get a constructor "Java like"
	__call = function (cls, ...)
        local self = setmetatable({}, cls)
        self:__init(...)
        return self
        end,
    }
)

Animation.__animations = {}
Animation.__count = 0

function Animation:__init(reference, property, firstValue, lastValue, duration)
    self.reference = reference
    self.property = property
    self.firstValue = firstValue
    self.lastValue = lastValue
    self.current = 0
    self.duration = duration
    self.done = false
    Animation.__count = Animation.__count + 1
    Animation.__animations[Animation.__count] = self
end

function Animation:update(dt)
    if not self.done then
      self.current = self.current + dt
      if self.current > self.duration then
          self.current = self.duration
          self.done = true
      end
      self.reference[self.property] = (self.current / self.duration) * (self.lastValue - self.firstValue) + self.firstValue    
    end
end

function updateAnimations(dt)
    for i=1, #Animation.__animations do
        Animation.__animations[i]:update(dt)
    end
end