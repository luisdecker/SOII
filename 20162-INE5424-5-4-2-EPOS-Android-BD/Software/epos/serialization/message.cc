#ifndef MESSAGES_CC
#define MESSAGES_CC

#include "message.h"
#ifdef SIMULATE
  #include <string.h>
#else
  #include <utility/string.h>
#endif
const char* base = "0123456789ABCDEF";

Message::Message() {
  this->data_length = 0;
}
Message::~Message() {
  if(this->address) {
    delete[] this->address;
  }
  if(this->function_code) {
    delete[] this->function_code;
  }
  if(this->data_length) {
    delete[] this->data;
  }
}
const char* Message::getAddress() {
  return this->address;
}

const char* Message::getFunctionCode() {
  return this->function_code;
}


const char* Message::getData() {
  return this->data;
}

void Message::setFunctionCode(const char* function_code) {
  this->function_code = prefixPadding('0', function_code, 2);
}

void Message::setAddress(const char* address) {
  this->address = prefixPadding('0', address, 2);
}



void Message::setFunctionCode(int function_code) {
  setFunctionCode(toHex(function_code));
}

void Message::setAddress(int address) {
  setAddress(toHex(address));
}

const char* Message::getDataSlice(int from, int to) {
  return sliceChar(data, from, to);
}

const char* Message::sliceChar(const char* s, int from, int to) {
  int l = to-from;
  if (l < 1) {
    l = 0;
  }
  if (to-from < 1) {
    return 0;
  }
  l += 1;
  char *result = new char[l];
  memset(result, '\0', l);
  strncpy(result, &s[from], to-from);
  return result;
}

void Message::setDataSlice(const char *new_data, int from, int to) {
  if (to-from < 1) {
    delete[] new_data;
    return;
  }
  new_data = prefixPadding('0', new_data, to-from);
  if (data_length < to) {
    int to2 = to+1;
    char *r = new char[to2];
    memset(r, '\0', to2);
    if (data_length > 0) {
      strcpy(r, data);
      delete [] data;
    }
    this->data = r;
    this->data_length = to2;
  }
  char* result = new char[data_length+1];
  memset(result, '\0', data_length);
  strncpy(result, this->data, data_length);
  strncpy(&result[from], new_data, to-from);
  delete[] data;
  this->data = result;
  delete[] new_data;
}


void Message::setDataSlice(int new_data, int from, int to) {
  if (to-from < 1) {
    return;
  }
  setDataSlice(toHex(new_data), from, to);
}

int Message::hexIndex(char letter) {
  char *r = strchr((char*) base, letter);
  if(!r) {
    return -1;
  }
  return (r-base);
}


int Message::fromHex(const char *hex) {
  int result = 0;
  int l = strlen(hex);
  for(int i=0; i < l; i++) {
    int ind = hexIndex(hex[i]);
    if (ind == -1) {
      return -1;
    }
    result = (16*result) + ind;
  }
  return result;
};


const char* Message::toHex(int value) {
  const char* result = "";
  int i=1;
  while (value > 0) {
    int mod = value%16;
    value = value/16;
    const char* tmp = prependChar(base[mod], result, i);
    if (i > 1) {
        delete[] result;
    }
    result = tmp;
    i++;
  }
  if (i == 1) {
      result = prependChar(base[0], result, 1);
  }
  return result;
};


const char* Message::prependChar(char ch, const char *s, int i) {
  char *tmp = new char[i+1];
  memset(tmp, '\0', i+1);
  tmp[0] = ch;
  strcpy(&tmp[1], s);
  const char* result = tmp;
  return result;
}


const char* Message::prependChar(char ch, const char* s) {
  return prependChar(ch, s, strlen(s));
}

const char* Message::prefixPadding(char p, const char* s, int i) {
  char* result = (char*) new char[i+1];
  memset(result, '\0', i+1);
  int c = strlen(s);
  if (c >= i) {
    strcpy(result, &s[c-i]);
  } else {
    strcpy(&result[i-c], s);
  }
  i -= c;
  if (i >= 0) {
    memset(result, p, i);
  }
  delete[] s;
  return result;
}

#endif
