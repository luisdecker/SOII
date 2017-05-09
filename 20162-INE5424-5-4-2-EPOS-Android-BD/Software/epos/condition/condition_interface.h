#ifndef CONDITION_INTERFACE
#define CONDITION_INTERFACE

class ConditionInterface {
public:
  virtual void wait() = 0;
  virtual void notify() = 0;
  virtual void notify_all() = 0;
};

#endif
