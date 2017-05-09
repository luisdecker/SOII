#ifndef SMART_OBJECT_H
#define SMART_OBJECT_H

#include "category.h"

class SmartObject {
public:
  /** Getters */
  int getId();
  int getCategoryID();
  const char* getServerURL();

  /** Setters */
  void setCategory(Category* category);
  void setCategory(int id);
  void setServerURL(const char* url);
  void setModbusID(const char* id);

protected:
    int id;
    int category_id;
    const char *url;
    const char *modbus_id;
}
#endif
