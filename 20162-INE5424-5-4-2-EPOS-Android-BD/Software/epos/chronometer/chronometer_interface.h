#ifndef CHRONOMETER_INTERFACE
#define CHRONOMETER_INTERFACE

class ChronometerInterface {
public:
  virtual void reset() = 0;
  virtual void start() = 0;
  virtual void lap() = 0;
  virtual void stop() = 0;
  virtual unsigned int read() = 0;
};

#endif
