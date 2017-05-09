#ifndef SERVICE_H
#define SERVICE_H

#include "category.h"

class Service {
public:
  /** Getters */
  int getId();
  int getCategoryID();
  const char* getName();
  const char* getModbusService();
  const char* getModbusRegister();

  /** Setters */
  void setCategory(Category* category);
  void setCategory(int id);
  void setName(const char* name);
  void setModbusService(const char* id);
  void setModbusRegister(const char* id);

protected:
    int id;
    int category_id;
    const char *name;
    const char *modbus_service;
    const char *modbus_register;
}
#endif
