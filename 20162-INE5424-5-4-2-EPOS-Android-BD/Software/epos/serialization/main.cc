#define SIMULATE
#include "serialization.cc"
#include "message.cc"
#include <iostream>
#include "string.h"
using std::cout;
using std::endl;

bool isCharsEqual(const char* c1, const char* c2) {
  return strcmp(c1, c2) == 0;
};

int main(int argc, char **argv) {

  Serialization *s = new Serialization();
  Message *mymsg = s->deserialize(":A10302000AB0\r\n");
  cout << "Address: " << Message::fromHex(mymsg->getAddress()) << endl;
  cout << "Function Code: " << Message::fromHex(mymsg->getFunctionCode()) << endl;
  cout << "Data: " << mymsg->getData() << endl;
  bool run = true;
  for (int address=0; address < 255 && run; address++) {
      for (int i=0; i < 255 && run; i++) {
          for (int i2=0; i2 < 255 && run; i2++) {
              Message *m = new Message();
              m->setAddress(address);
              m->setFunctionCode(READ_HOLDING_REGISTERS);
              m->setDataSlice(i, 0, 4);
              m->setDataSlice(i2, 4, 8);
              const char *r = s->serialize(m);
              delete m;
              cout << "Codificado '" << r << "'" << endl;
              Message *m2 = s->deserialize(r);
              if (m == 0) {
                  cout << "Problema na codificacao" << endl;
                  run = false;
              } else {
                  delete m2;
              }
          }
      }
  }

  Message *m = new Message();
  m->setAddress(161);
  m->setFunctionCode(READ_HOLDING_REGISTERS);
  m->setDataSlice(3, 0, 4);
  m->setDataSlice(1, 4, 8);

  /** Define mensagem acima */
  const char *r = s->serialize(m); // Serializa...
  cout << "Mensagem codificada com '" << r << "'" << endl;
  cout << "Deserializando mensagem '" << r << "'" << endl;
  Message *m2 = s->deserialize(r); // Deserializa..
  if (m2) { // Verifica se é válida (caso o checksum não bata com a mensagem, o método deserialize retorna NULL)
    cout << "M2 é válido!" << endl;
    cout << isCharsEqual(m->getAddress(), m2->getAddress()) << endl;
    cout << isCharsEqual(m->getFunctionCode(), m2->getFunctionCode()) << endl;
    cout << isCharsEqual(m->getData(), m2->getData()) << endl;
  } else {
    cout << "M2 é inválido!" << endl;
  }
  char* r2 = new char[strlen(r)+1];
  memset(r2, '\0', strlen(r)+1);
  strcpy(r2, r);
  r2[1] = 'H'; // Modifica o segundo byte da mensagem só pra ZOAR...
  cout << "Deserializando mensagem '" << r2 << "'" << endl;
  Message *m3 = s->deserialize(r2);
  if (m3) {
    cout << "M3 é válido!" << endl;
    cout << isCharsEqual(m->getAddress(), m3->getAddress()) << endl;
    cout << isCharsEqual(m->getFunctionCode(), m3->getFunctionCode()) << endl;
    cout << isCharsEqual(m->getData(), m3->getData()) << endl;
  } else {
    cout << "M3 é inválido!" << endl;
  }
  delete[] r;
  delete[] r2;
  delete m;
  delete m2;
  delete m3;
  delete s;

}
