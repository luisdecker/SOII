#ifndef COMMUNICATION_H
#define COMMUNICATION_H

#include "../serialization/serialization.h"
#include "../usbmanager/usbmanager_app.h"
#include "../mutex/mutex_app.h"
#include "../list/list_app.h"
#include "../queue/queue_app.h"
#include "../condition/condition_app.h"

class Communication {
  public:
    Communication(int address, USBManagerApp *usbmanager, Serialization *serialization);
    Message* send(Message *message, int retry=1);
    bool ack(Message *message);
    Message* receive();
    void run();
    void runGC();

  protected:
    int address;
    USBManagerApp *usbmanager;
    Serialization *serialization;
    MutexApp *mutexQueue;
    MutexApp *mutexSend;
    ListApp<int> *sent;
    QueueApp<Message> *receiveSendQueue;
    QueueApp<Message> *receiveQueue;
    ConditionApp *notifierSend;
    ConditionApp *notifier;
};

#endif
