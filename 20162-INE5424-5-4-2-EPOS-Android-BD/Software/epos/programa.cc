#define SIMULATE
#include "communication/communication.cc"
#ifdef SIMULATE
#include <thread>
#include <iostream>
using std::endl;
using std::cout;
#else
#include <thread.h>
#include <utility/ostream.h>
using EPOS::endl;
EPOS::OStream cout;
#endif
int send(Communication *comm) {

    Message *m = new Message();
    m->setAddress(247);
    m->setFunctionCode(READ_HOLDING_REGISTERS);
    m->setDataSlice(19, 0, 2);
    m->setDataSlice(137, 2, 4);
    m->setDataSlice(0, 4, 6);
    m->setDataSlice(10, 6, 8);
    cout << comm->send(m) << endl;
    return 0;
}
int thread1(Communication *comm) {
    comm->run();
    return 0;
}
int thread2(Communication *comm) {
    comm->runGC();
    return 0;
}
int main(int argc, char** argv) {
    USBManagerApp* manager = new USBManagerApp();
    #ifdef SIMULATE
    manager->setPort("/dev/ttyUSB0");
    #else
    manager->setPort(0);
    #endif
    Serialization *serialization = new Serialization();
    Communication *comm = new Communication(0, manager, serialization);

    #ifdef SIMULATE
    std::thread *t1 = new std::thread(thread1, comm);
    std::thread *t2 = new std::thread(thread2, comm);
    std::thread *t3 = new std::thread(send, comm);
    #else
    EPOS::Thread *t1 = new EPOS::Thread(&thread1, comm);
    EPOS::Thread *t2 = new EPOS::Thread(&thread2, comm);
    EPOS::Thread *t3 = new EPOS::Thread(&send, comm);
    #endif

    t1->join();
    t2->join();
    t3->join();
    /* const char* test = abstraction->read();
    std::cout << "Read: " << test << std::endl;
    std::string result("Hello, ");
    result.append(test);
    abstraction->write(result.c_str()); */
    // delete manager;
}
