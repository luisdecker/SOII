#ifndef LIST_INTERFACE
#define LIST_INTERFACE

template<typename T>
class ListInterface {
public:
  virtual void insert(T element) = 0;
  virtual void remove(T element) = 0;
  virtual bool find(T element) = 0;
  virtual T search(T element) = 0;

};

#endif
