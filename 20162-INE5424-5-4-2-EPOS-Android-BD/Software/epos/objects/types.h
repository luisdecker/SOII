#ifndef TYPES_H
#define TYPES_H

class ValueInterface {
  public:
    const char* getEncoded();
}

class GetValue : public ValueInterface {

}

class ComboValue: public ValueInterface {

}

class DoubleValue: public ValueInterface {

}

class BooleanValue: public ValueInterface {

}

#endif
