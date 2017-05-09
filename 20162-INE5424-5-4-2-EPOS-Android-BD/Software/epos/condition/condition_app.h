#ifndef CONDITION_APP
#define CONDITION_APP

#include "condition_interface.h"
#ifdef SIMULATE
    #include "simulator.h"
#else
    #include "condition.h"
#endif

class ConditionApp: public ConditionInterface {
    public:
        ConditionApp() {
            this->m = new EAB::ConditionAbstraction();
        }
        ~ConditionApp() {
            delete this->m;
        }
        void wait() {
            this->m->wait();
        }
        void notify() {
            this->m->notify();
        }
        void notify_all() {
            this->m->notify_all();
        }
    private:
        EAB::ConditionAbstraction* m;
};

#endif
