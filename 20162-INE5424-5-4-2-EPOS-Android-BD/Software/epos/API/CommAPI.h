#ifndef COMMAPI_H
#define COMMAPI_H
#include "../communication/communication.cc"
#include "function.h"
class CommAPI {
  typedef int (*fR)();
  typedef bool (*fW)(int);

  public:
      CommAPI(Communication *comm);
      ~CommAPI();
      void setReadHandler(int (*fptr)(), int address);
      bool setWriteHandler(bool (*fptr)(int), int address);
      void run();

  private:
      Communication* comm;
      int valueWrite;
      ListApp<EAB::Function<fR> > listRead;
      ListApp<EAB::Function<fW> > listWrite;
};
#endif
