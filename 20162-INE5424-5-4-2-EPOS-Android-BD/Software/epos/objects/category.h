#ifndef CATEGORY_INTERFACE
#define CATEGORY_INTERFACE

class Category {
  public:
      int getId();
      const char* getName();
      void setName(const char* name);
  protected:
    int id;
    const char* name;
}

#endif
