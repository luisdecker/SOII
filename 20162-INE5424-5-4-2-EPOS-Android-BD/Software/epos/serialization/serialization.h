#ifndef SERIALIZATION_H
#define SERIALIZATION_H

#include "message.h"

class Serialization {
public:
  const char* serialize(Message *request);
  Message* deserialize(const char* message);
protected:
  static int lrc(char *message);
};

#endif
