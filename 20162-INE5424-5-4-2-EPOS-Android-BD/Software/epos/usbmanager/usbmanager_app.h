#ifndef USBMANAGER_APP
#define USBMANAGER_APP

#include "usbmanager_interface.h"
#ifdef SIMULATE
    #include "simulator.h"
#else
    #include "usbmanager.h"
#endif

class USBManagerApp: public EAB::USBManager {};

#endif
