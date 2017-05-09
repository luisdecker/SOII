#ifndef USBMANAGER
#define USBMANAGER

#include "usbmanager_interface.h"
#include <uart.h>
#include <utility/string.h>
namespace EAB {
class USBManager: public USBManagerInterface {
    public:
        USBManager() {}
        ~USBManager() {
            delete uart;
        }
        USBManager(const char *port) {
            this->setPort(port);
        }
        USBManager(const int port) {
            this->setPort(port);
        }
        USBManager(EPOS::PC_UART *uart) {
            this->setPort(uart);
        }

        const char* read() {
          int length = 8;
          int count = 0;
          char* result = new char[length];
          memset(result, '\0', length);
          char lastchar = '\0';
          while (lastchar != '\n') {
            // Stop just when '\n' is received, as getline
            lastchar = this->uart->get();
            count++;
            if (count >= length) {
              length *= 2;
              char *tmp = new char[length];
              memset(tmp, '\0', length);
              strcpy(tmp, result);
              delete[] result;
              result = tmp;
            }
            char *a = new char[2];
            memset(a, '\0', 2);
            a[0] = lastchar;
            strcat(result, a);
            delete[] a;
          }
          return result;
        }


        void write(const char* command) {
          int l = strlen(command);
          for(int c=0; c<l; c++) {
            this->uart->put(command[c]);
          }
        }

        void setPort(const char* port) {
          setPort(atoi(port));
        }

        void setPort(const int port) {
          EPOS::PC_UART *uart = new EPOS::PC_UART(port);
          setPort(uart);
        }

        void setPort(EPOS::PC_UART* uart) {
            this->uart = uart;
        }
    protected:
        EPOS::PC_UART* uart;
};
}

#endif
