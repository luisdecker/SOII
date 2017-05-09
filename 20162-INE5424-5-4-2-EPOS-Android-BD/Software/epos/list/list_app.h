#ifndef LIST_APP
#define LIST_APP

#include "list_interface.h"
#ifdef SIMULATE
    #include "simulator.h"
#else
    #include "list.h"
#endif

template<typename T>
class ListApp: public ListInterface<T> {
    public:
        ListApp() {
            this->m = new EAB::ListAbstraction<T>();
        }
        ~ListApp() {
            delete this->m;
        }
        void insert(T element) {
            this->m->insert(element);
        }
        void remove(T element) {
            return this->m->remove(element);
        }
        bool find(T element) {
            return this->m->find(element);
        }
        T search(T element) {
            return this->m->search(element);
        }
    private:
        EAB::ListAbstraction<T> *m;
};

#endif
