#ifndef LIST_EPOS
#define LIST_EPOS

#include <utility/list.h>
#include "list_interface.h"
#include <utility/ostream.h>
#include <utility/string.h>
using EPOS::endl;
EPOS::OStream eabcout;
namespace EAB {
    template<typename T>
    class ListAbstraction : public ListInterface<T> {
    public:
        typedef EPOS::List<T> Q;
        typedef typename Q::Element E;
    public:
        ListAbstraction() {
            this->l = new Q();
        }
        void insert(T element) {
            T *v = new T;
            // Copy element because element will only survive to the end of the
            // function
            memcpy(v, &element, 1);
            E *link = new E(v);
            this->l->insert(link);
        }
        void remove(T element) {
            E * e = this->l->head();
            for(; e && (*(e->object()) != element); e = e->next());
            E *link = this->l->remove(e);
            T *v = link->object();
            delete v;
            delete link;
        }
        bool find(T element) {
            E *e = this->l->head();

            for(; e && (*(e->object()) != element); e = e->next());
            return e != 0;
        }
        T search(T element) {
            E *e = this->l->head();

            for(; e && (*(e->object()) != element); e = e->next());
            return e;
        }
    private:
        Q *l;
    };
}

#endif
