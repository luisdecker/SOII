#ifndef PARAMETER_H
#define PARAMETER_H

#include "service.h"

enum ParameterType {

}
class Parameter {
public:
  /** Getters */
  int getId();
  ParameterType getType();
  const char* getName();

  /** Setters */
  void setName(const char* name);
  void setType(ParameterType type);


protected:
    int id;
    const char *name;
    
}
#endif
