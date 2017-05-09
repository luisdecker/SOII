#ifndef QUEUE_SIMULATOR
#define QUEUE_SIMULATOR

#include <queue>
#include "queue_interface.h"

namespace EAB {
  template<typename T>
  class QueueAbstraction: public QueueInterface<T> {
  public:
    QueueAbstraction() {
        this->m = new std::queue<T*>();
    }
    void insert(T* element) {
        this->m->push(element);
    }
    T* remove() {
        T* front = this->m->front();
        this->m->pop();
        return front;
    }
    bool empty() {
        return this->m->empty();
    }
    int size() {
        return this->m->size();
    }
  protected:
    std::queue<T*> *m;
  };
}

#endif
