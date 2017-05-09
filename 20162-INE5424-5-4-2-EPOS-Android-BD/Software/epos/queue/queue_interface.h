#ifndef QUEUE_INTERFACE
#define QUEUE_INTERFACE

template<typename T>
class QueueInterface {
public:
  virtual void insert(T* element) = 0;
  virtual T* remove() = 0;
  virtual bool empty() = 0;
  virtual int size() = 0;
};

#endif
