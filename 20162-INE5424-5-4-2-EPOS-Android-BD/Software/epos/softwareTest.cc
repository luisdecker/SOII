#define SIMULATE
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
#include "communication/communication.cc"
#include "API/CommAPI.cc"
// #include "programa.cc"

int temperature;
int poweredOn;
int humidity;
int speed;
int timer;
int function;

bool writeTemperature(int param) {
    temperature = param;
    cout << "Wrote param(" << param << ") in device!" << endl;
    return true;
}

int readTemperature() {
    cout << "Read temperature " << temperature << " from device!" << endl;
    return temperature;
}


bool writePoweredOn(int param) {
    poweredOn = param;
    cout << "Wrote param(" << param << ") in device!" << endl;
    return true;
}

int readPoweredOn() {
    cout << "Read powered on state " << poweredOn << " from device!" << endl;
    return poweredOn;
}


bool writeHumidity(int param) {
    humidity = param;
    cout << "Wrote param(" << param << ") in device!" << endl;
    return true;
}

int readHumidity() {
    cout << "Read humidity " << humidity << " from device!" << endl;
    return humidity;
}


bool writeSpeed(int param) {
    speed = param;
    cout << "Wrote param(" << param << ") in device!" << endl;
    return true;
}

int readSpeed() {
    cout << "Read speed " << speed << " from device!" << endl;
    return speed;
}


bool writeTimer(int param) {
    timer = param;
    cout << "Wrote param(" << param << ") in device!" << endl;
    return true;
}

int readTimer() {
    cout << "Read timer " << timer << " from device!" << endl;
    return timer;
}


bool writeFunction(int param) {
    function = param;
    cout << "Wrote param(" << param << ") in device!" << endl;
    return true;
}

int readFunction() {
    cout << "Read function " << function << " from device!" << endl;
    return function;
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

    manager->setPort("/dev/pts/2");

    Serialization *serialization = new Serialization();
    Communication *comm = new Communication(161, manager, serialization);
    CommAPI *api = new CommAPI(comm);

    //Writing on device;
    int temperatureAddress = 3;
    api->setWriteHandler(&writeTemperature, temperatureAddress);

    //Reading from device;
    api->setReadHandler(&readTemperature, temperatureAddress);


    int poweredOnAddress = 2;
    api->setWriteHandler(&writePoweredOn, poweredOnAddress);

    //Reading from device;
    api->setReadHandler(&readPoweredOn, poweredOnAddress);


    int humidityAddress = 6;
    api->setWriteHandler(&writeHumidity, humidityAddress);

    //Reading from device;
    api->setReadHandler(&readHumidity, humidityAddress);


    int speedAddress = 7;
    api->setWriteHandler(&writeSpeed, speedAddress);

    //Reading from device;
    api->setReadHandler(&readSpeed, speedAddress);


    int timerAddress = 8;
    api->setWriteHandler(&writeTimer, timerAddress);

    //Reading from device;
    api->setReadHandler(&readTimer, timerAddress);


    int functionAddress = 9;
    api->setWriteHandler(&writeTimer, functionAddress);

    //Reading from device;
    api->setReadHandler(&readTimer, functionAddress);


    api->setReadHandler(&readTemperature, 10);

    #ifdef SIMULATE
    std::thread *t1 = new std::thread(thread1, comm);
    std::thread *t2 = new std::thread(thread2, comm);
    #else
    EPOS::Thread *t1 = new EPOS::Thread(&thread1, comm);
    EPOS::Thread *t2 = new EPOS::Thread(&thread2, comm);
    #endif
    // cout << "Read: " << values << endl;
    /* const char* test = abstraction->read();
    std::cout << "Read: " << test << std::endl;
    std::string result("Hello, ");
    result.append(test);
    abstraction->write(result.c_str()); */
    // delete manager;
    api->run();
    t1->join();
    t2->join();
};
