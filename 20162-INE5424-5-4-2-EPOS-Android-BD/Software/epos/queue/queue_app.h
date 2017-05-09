#ifndef QUEUE_APP
#define QUEUE_APP

#include "queue_interface.h"
#ifdef SIMULATE
    #include "simulator.h"
#else
    #include "queue.h"
#endif

template<typename T>
class QueueApp: public QueueInterface<T> {
    public:
        QueueApp() {
            this->m = new EAB::QueueAbstraction<T>();
        }
        void insert(T* element) {
            this->m->insert(element);
        }
        T* remove() {
            return this->m->remove();
        }
        bool empty() {
            return this->m->empty();
        }
        int size() {
            return this->m->size();
        }
    private:
        EAB::QueueAbstraction<T>* m;
};

#endif
