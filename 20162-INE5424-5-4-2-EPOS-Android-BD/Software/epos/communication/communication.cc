#ifndef COMMUNICATION_CC
#define COMMUNICATION_CC

#include "communication.h"
#include "../mutex/mutex_app.h"
#include "../queue/queue_app.h"
#include "../sleep/sleep_app.h"
#include "../condition/condition_app.h"
#include "../chronometer/chronometer_app.h"
#include "../list/list_app.h"
#include "../serialization/message.cc"
#include "../serialization/serialization.cc"
#ifdef SIMULATE
    #include <string.h>
#else
    #include <utility/string.h>
#endif

Communication::Communication(int address, USBManagerApp *usbmanager, Serialization *serialization) {
    this->usbmanager = usbmanager;
    this->serialization = serialization;
    this->address = address;
    this->mutexQueue = new MutexApp();
    this->mutexSend = new MutexApp();
    this->sent = new ListApp<int>();
    this->receiveSendQueue = new QueueApp<Message>();
    this->receiveQueue = new QueueApp<Message>();
    this->notifierSend = new ConditionApp();
    this->notifier = new ConditionApp();
}

Message* Communication::send(Message *message, int retries) {
    Message *ack = 0;
    int msgAddress = Message::fromHex(message->getAddress());
    int msgFunctionCode = Message::fromHex(message->getFunctionCode());
    // Register interest in messages from the specified address
    this->mutexSend->lock();
    this->sent->insert(msgAddress);
    this->mutexSend->unlock();
    while (!ack && retries > 0) {
        // Two or more send() may run concurrently, so we need to guarantee
        // that each message is sent correctly..
        this->mutexSend->lock();
        const char *toSend = this->serialization->serialize(message);
        this->usbmanager->write(toSend);
        this->mutexSend->unlock();
        delete toSend;
        ChronometerApp *chron = new ChronometerApp();
        chron->start();
        while (!ack) {
            this->notifierSend->wait();
            chron->lap(); // Register actual difference
            if (chron->read() > 2000000) {
                // Timeout! Break the loop!
                break;
            }
            this->mutexSend->lock();
            int count = this->receiveSendQueue->size();
            // Get the size so we can iter over all the elements in the queue
            while (!ack && count > 0) {
                ack = this->receiveSendQueue->remove();
                if (
                    msgAddress != Message::fromHex(ack->getAddress()) ||
                    (
                        msgFunctionCode != Message::fromHex(ack->getFunctionCode()) &&
                        (msgFunctionCode+128) != Message::fromHex(ack->getFunctionCode()) // IF it's an excception
                    )
                ) {
                    this->receiveSendQueue->insert(ack);
                    ack = 0;
                }
                count--;
            }
            this->mutexSend->unlock();
        }
        chron->stop();
        delete chron;
        retries--;
    }
    this->mutexSend->lock();
    this->sent->remove(msgAddress);
    this->mutexSend->unlock();
    return ack;
}

bool Communication::ack(Message* message) {
    int msgAddress = Message::fromHex(message->getAddress());
    if (msgAddress != this->address) {
        // ack should be called only with address being the same of this
        // communicator device..
        return false;
    }
    this->mutexSend->lock();
    const char *toSend = this->serialization->serialize(message);
    this->usbmanager->write(toSend);
    this->mutexSend->unlock();
    return true;
}

Message* Communication::receive() {
    Message* result = 0;
    while(!result) {
        this->notifier->wait();
        this->mutexQueue->lock();
        if (this->receiveQueue->empty()) {
            this->mutexQueue->unlock();
            continue;
        }
        result = this->receiveQueue->remove();
        this->mutexQueue->unlock();
    }
    return result;
}

#include <iostream>
using std::endl;
using std::cout;
void Communication::run() {
    // This method will just read the USBManagerApp interface
    while (true) {
        const char *line = this->usbmanager->read();
        Message *msg = this->serialization->deserialize(line);
        cout << msg << endl;
        delete[] line;
        if (!msg) {
            continue;
        }
        this->mutexSend->lock();
        int msgAddress = Message::fromHex(msg->getAddress());
        if (msgAddress == this->address) {
            this->mutexQueue->lock();
            this->receiveQueue->insert(msg);
            this->notifier->notify();
            this->mutexQueue->unlock();
        } else if (this->sent->find(msgAddress)) {
            // There are interest in messages of this address!
            // Save it into the queue for the treatment of the message
            this->receiveSendQueue->insert(msg);
            this->notifierSend->notify_all();
        }
        this->mutexSend->unlock();
    }
}

void Communication::runGC() {
    while (true) {
        // Sleeps for 2 seconds and trigger..
        delay(2000000);
        this->mutexSend->lock();
        int l = this->receiveSendQueue->size();
        for (int c=0; c<l; c++) {
            Message *msg = this->receiveSendQueue->remove();
            // Get the address from that message
            int msgAddress = Message::fromHex(msg->getAddress());
            if(this->sent->find(msgAddress)) {
                // There are interest in messages from this address..
                // So we re-insert it into the queue again
                this->receiveSendQueue->insert(msg);
            } else {
                // There are no interest in messages from this address...
                // So it's safe to delete it from the queue
                delete msg;
            }
        }
        if (this->receiveSendQueue->empty()) {
            // Only notify thread if the receiveSendQueue is empty..
            this->notifierSend->notify_all();
        }
        this->mutexSend->unlock();
    }
}

#endif
