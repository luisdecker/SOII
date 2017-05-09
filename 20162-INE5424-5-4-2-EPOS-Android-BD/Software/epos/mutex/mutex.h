#ifndef MUTEX_H
#define MUTEX_H

#include <mutex.h>
#include "mutex_interface.h"

namespace EAB {
  class MutexAbstraction : public MutexInterface {
  public:
    MutexAbstraction() {
        this->m = new EPOS::Mutex();
    }
    void lock() {
        this->m->lock();
    }
    void unlock() {
        this->m->unlock();
    }
  private:
    EPOS::Mutex *m;
};
}

#endif
