#ifndef MUTEX_APP
#define MUTEX_APP

#include "mutex_interface.h"
#ifdef SIMULATE
    #include "simulator.h"
#else
    #include "mutex.h"
#endif

class MutexApp: public MutexInterface {
    public:
        MutexApp() {
          this->m = new EAB::MutexAbstraction();
        }
        void lock() {
          this->m->lock();
        }
        void unlock() {
          this->m->unlock();
        }
    private:
        EAB::MutexAbstraction* m;
};

#endif
