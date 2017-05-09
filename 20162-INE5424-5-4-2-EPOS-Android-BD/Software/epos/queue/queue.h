#ifndef QUEUE_EPOS
#define QUEUE_EPOS

#include <utility/queue.h>
#include "queue_interface.h"

namespace EAB {
    template<typename T>
    class QueueAbstraction : public QueueInterface<T> {
    public:
        typedef EPOS::Queue<T> Q;
        typedef typename Q::Element E;
    public:
        QueueAbstraction() {
            this->q = new Q();
        }
        void insert(T* element) {
            E *link = new E(element);
            this->q->insert(link);
        }
        T* remove() {
            E *link = this->q->remove();
            T* result = link->object();
            delete link;
            return result;
        }
        bool empty() {
            return this->q->empty();
        }
        int size() {
            return this->q->size();
        }
    private:
        Q *q;
    };
}

#endif
