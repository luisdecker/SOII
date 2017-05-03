#include <iostream>

typedef long long int longao;

template<longao n, longao m>
struct Mult {
    enum {
        valor = m + Mult<n - 1, m>::valor
    };
};

template<longao m>
struct Mult<1, m> {
    enum {
        valor = m
    };
};

template<longao m>
struct Mult<0, m> {
    enum {
        valor = 0
    };
};

int main() {

    std::cout << Mult<3, 2>::valor << std::endl;

    std::cout << "MINHA MATRICULA È 12100759";

    return 0;
}