#ifndef SIMULATOR
#define SIMULATOR

#include <iostream>
#include <fstream>

#include <string>
#include <string.h>
#include <stdio.h>

#include "usbmanager_interface.h"

namespace EAB {
    class USBManager : public USBManagerInterface {
    public:
        USBManager() {}
        ~USBManager() {}
        USBManager(const char *port) {
            this->setPort(port);
        }
        USBManager(const int port) {
            this->setPort(port);
        }

        const char* read() {
            std::cout << "void OS::Simulator::read('" << port << "')" << std::endl;

            std::ifstream usb (this->port);
            int length = 8;
            int count = 0;
            char* result = new char[length];
            memset(result, '\0', length);
            char lastchar = '\0';
            while (lastchar != '\n') {
              // Stop just when '\n' is received, as getline
              lastchar = usb.get();
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
            std::cout << "void OS::Simulator::write('" << port << "', '" << command << "')" << std::endl;
            std::ofstream usb (this->port);
            if (usb.is_open())
            {
                usb << command << "\n";
                usb.close();
            }
        }

        void setPort(const char* port) {
            this->port = port;
        }

        void setPort(const int portId) {
            char* port = new char[22];
            char* n = new char[10];
            sprintf(n, "%d", portId);
            strcat(port, "/dev/ttyUSB");
            strcat(port, n);
            delete[] n;
            setPort(port);
        }
    protected:
        const char* port;
    };
}

#endif
