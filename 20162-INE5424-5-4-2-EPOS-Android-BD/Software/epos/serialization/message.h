#ifndef MESSAGE_H
#define MESSAGE_H

extern const char* base;

enum MessageType {
  READ_COIL_STATUS = 0x01,
  READ_INPUT_STATUS = 0x02,
  READ_HOLDING_REGISTERS = 0x03,
  READ_INPUT_REGISTERS = 0x04,

  WRITE_SINGLE_COIL = 0x05,
  WRITE_SINGLE_REGISTER = 0x06,
  WRITE_MULTIPLE_COILS = 0x15,
  WRITE_MULTIPLE_REGISTERS = 0x16
};

class Message {
public:
  Message();
  ~Message();

  /** Getters */
  const char* getAddress();
  const char* getFunctionCode();
  const char* getData();

  const char* getDataSlice(int from, int to);

  /** Setters */
  void setAddress(const char* address);
  void setFunctionCode(const char* function_code);
  void setDataSlice(const char *new_data, int from, int to);
  void setDataSlice(int new_data, int from, int to);

  void setAddress(int address);
  void setFunctionCode(int function_code);

  /** Utilities Functions */

  /* Hex conversion */
  static const char* toHex(int value);
  static int fromHex(const char* hex);

  /* String manipulation */
  static const char* sliceChar(const char* s, int from, int to);
  static const char* prefixPadding(char p, const char* s, int i);
protected:
  const char *address;
  const char *function_code;
  const char *data;
  int data_length;
  static int hexIndex(char letter);
  static const char* prependChar(char ch, const char* s, int i);
  static const char* prependChar(char ch, const char* s);
};

#endif
