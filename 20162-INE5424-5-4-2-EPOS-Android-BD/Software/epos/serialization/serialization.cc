#ifndef SERIALIZATION_CC
#define SERIALIZATION_CC

#include "serialization.h"
#include "message.h"
#ifdef SIMULATE
  #include <cstring>
#else
  #include <utility/string.h>
#endif
const char* Serialization::serialize(Message *message) {
  const char* data = message->getData();
  int ldata = strlen(data);
  char* tmp = new char[5+ldata];
  memset(tmp, '\0', 5+ldata);
  strcat(tmp, message->getAddress());
  strcat(tmp, message->getFunctionCode());
  strcat(tmp, data);
  int checksum = this->lrc(tmp);
  delete[] tmp;
  char *result = new char[10+ldata];
  memset(result, '\0', 10+ldata);
  strcat(result, ":");
  strcat(result, message->getAddress());
  strcat(result, message->getFunctionCode());
  strcat(result, data);
  const char *checksumHex = Message::prefixPadding('0', Message::toHex(checksum), 2);
  strcat(result, checksumHex);
  strcat(result, "\r\n");
  delete[] checksumHex;
  return result;
}

Message* Serialization::deserialize(const char *content) {
  Message* result = new Message();
  result->setAddress(Message::sliceChar(content, 1, 3));
  result->setFunctionCode(Message::sliceChar(content, 3, 5));
  const char *r = strrchr(content, '\r');
  if (!r) {
    delete result;
    result = 0;
    return result;
  }
  int t = r-content;
  result->setDataSlice(Message::sliceChar(content, 5, t-2), 0, t-7);
  const char* checksumContent = Message::sliceChar(content, t-2, t);
  int checksum = Message::fromHex(checksumContent);
  delete[] checksumContent;
  // Check cheksum..
  char *tmp = new char[4+(t-7)+1];
  memset(tmp, '\0', 4+(t-7)+1);
  strcat(tmp, result->getAddress());
  strcat(tmp, result->getFunctionCode());
  strcat(tmp, result->getData());
  int checksum2 = this->lrc(tmp);
  delete[] tmp;
  if (checksum != checksum2) {
    delete result;
    result = 0;
  }
  return result;
}

int Serialization::lrc(char *data)
{
  int nLRC = 0; // LRC char initialized
  int l = strlen(data);
  for (int i = 0; i < l; i += 2) {
    const char *s = Message::sliceChar(data, i, i+2);
    nLRC += Message::fromHex(s);
    delete[] s;
  }
  return nLRC & 0xff;
}

#endif
