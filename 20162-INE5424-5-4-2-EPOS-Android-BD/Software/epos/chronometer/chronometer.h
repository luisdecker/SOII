#ifndef CHRONOMETER_H
#define CHRONOMETER_H

#include <chronometer.h>
#include "chronometer_interface.h"

namespace EAB {
  class ChronometerAbstraction : public ChronometerInterface {
  public:
    ChronometerAbstraction() {
        this->c = new EPOS::Chronometer();
    }
    ~ChronometerAbstraction() {
        delete this->c;
    }
    void reset() {
        this->c->reset();
    }
    void start() {
        this->c->start();
    }
    void lap() {
        this->c->lap();
    }
    void stop() {
        this->c->stop();
    }
    unsigned int read() {
        return this->c->read();
    }
  private:
    EPOS::Chronometer *c;
};
}

#endif
