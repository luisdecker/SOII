#ifndef USBMANAGER_INTERFACE
#define USBMANAGER_INTERFACE

class USBManagerInterface {
    public:
        virtual const char* read() = 0;
        virtual void write(const char* command) = 0;
        virtual void setPort(const int port) = 0;
        virtual void setPort(const char* port) = 0;

};
#endif
