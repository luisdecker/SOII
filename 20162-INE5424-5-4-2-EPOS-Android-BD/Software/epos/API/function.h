#ifndef API_FUNCTION_H
#define API_FUNCTION_H
namespace EAB {
  template<typename T>
  class Function {
  protected:
      int address;
      T func;
  public:
      Function(int address, T func) {
          this->address = address;
          this->func = func;
      }

      Function(int address) {
          this->address = address;
      }

      int getAddress() const {
          return this->address;
      }

      T getFunction() {
          return this->func;
      }

      inline bool operator==(const Function& lhs){
          return this->getAddress() == lhs.getAddress();
      }
      inline bool operator!=(const Function& rhs){
          return this->getAddress() != rhs.getAddress();
      }
  };
}
#endif
