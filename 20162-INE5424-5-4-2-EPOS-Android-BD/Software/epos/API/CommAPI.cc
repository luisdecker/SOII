#ifndef COMMAPI_CC
#define COMMAPI_CC
#include "CommAPI.h"
#include "function.h"
#include <iostream>
using std::endl;
using std::cout;
CommAPI::CommAPI(Communication *comm){
    this->comm = comm;
}

CommAPI::~CommAPI(){}

void CommAPI::setReadHandler(int (*fptr)(), int address){
    EAB::Function<fR> readHandler(address, fptr);
    EAB::Function<fR> rH = listRead.search(readHandler);
    if (readHandler == rH) {
        this->listRead.remove(rH);
    }
    this->listRead.insert(readHandler);
    // return;
}

bool CommAPI::setWriteHandler(bool (*fptr)(int), int address){
    EAB::Function<fW> writeHandler(address, fptr);
    EAB::Function<fW> wH = listWrite.search(writeHandler);
    if (writeHandler == wH) {
        this->listWrite.remove(wH);
    }
    this->listWrite.insert(writeHandler);
    return true;
}
void CommAPI::run(){
    while(true) {
        Message* msg = comm->receive();
        int address = msg->fromHex(msg->getAddress());
        int function = Message::fromHex(msg->getFunctionCode());
        Message *ack = new Message();
        ack->setAddress(address);
        ack->setFunctionCode(function);
        // cout << "Data section: '" << msg->getData() << "'" << endl;
        if (function == READ_HOLDING_REGISTERS) {
            int regAddress = Message::fromHex(msg->getDataSlice(0, 4));
            int count = Message::fromHex(msg->getDataSlice(4, 8));
            // cout << "Data section: '" << msg->getData() << "'" << endl;
            cout << "Lendo " << count << " (" << msg->getDataSlice(4, 8) << ") registradores a partir de " << regAddress << "(" << msg->getDataSlice(0, 4) << ") " << endl;
            if (count == 1) {
                EAB::Function<fR> f(regAddress);
                EAB::Function<fR> r = listRead.search(f);
                int (*func)() = r.getFunction();
                if (r.getAddress() != regAddress || func == 0) {
                    ack->setFunctionCode(function+128);
                    ack->setDataSlice(3, 0, 2);
                } else {
                    int result = func();
                    ack->setDataSlice(2, 0, 2);
                    ack->setDataSlice(result, 2, 6);
                }
            } else {
                ack->setFunctionCode(function+128);
                ack->setDataSlice(3, 0, 2);
            }
        } else if (function == WRITE_SINGLE_REGISTER) {
            int regAddress = Message::fromHex(msg->getDataSlice(0, 4));

            // cout << "Escrevendo registrador " << regAddress << endl;
            EAB::Function<fW> f(regAddress);
            EAB::Function<fW> w = listWrite.search(f);
            bool (*func)(int) = w.getFunction();
            bool result = false;
            if (w.getAddress() == regAddress && func != 0) {
                // There's a function to process the request! \o/
                int param = Message::fromHex(msg->getDataSlice(4, 8));
                result = func(param);
            }
            if (!result) {
                ack->setFunctionCode(function+128);
                ack->setDataSlice(3, 0, 2);
            } else {
                ack->setDataSlice(msg->getDataSlice(0, 8), 0, 8);
            }
        } else {
            ack->setFunctionCode(function+128);
            ack->setDataSlice(1, 0, 2);
        }
        comm->ack(ack);
    }
}
#endif
