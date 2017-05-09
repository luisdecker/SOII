#ifndef MUTEX_SIMULATOR
#define MUTEX_SIMULATOR

#include <mutex>
#include "mutex_interface.h"

namespace EAB {
  class MutexAbstraction: public MutexInterface {
  public:
    MutexAbstraction() {
      this->m = new std::mutex();
    }
    void lock() {
      this->m->lock();
    }
    void unlock() {
      this->m->unlock();
    }
  protected:
    std::mutex *m;
  };
}

#endif
