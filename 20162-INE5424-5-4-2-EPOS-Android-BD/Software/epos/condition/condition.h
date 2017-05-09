#ifndef CONDITION_H
#define CONDITION_H

#include <condition.h>
#include "condition_interface.h"

namespace EAB {
  class ConditionAbstraction : public ConditionInterface {
  public:
    ConditionAbstraction() {
        this->c = new EPOS::Condition();
    }
    void wait() {
        this->c->wait();
    }
    void notify() {
        this->c->signal();
    }
    void notify_all() {
        this->c->broadcast();
    }
  private:
    EPOS::Condition *c;
};
}

#endif
