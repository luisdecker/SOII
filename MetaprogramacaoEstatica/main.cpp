#include <iostream>

typedef long long int longao;

template<longao x, longao y>
struct Multiplicar {
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
    template<longao n>
    struct Mult<n, 0> {
        enum {
            valor = 0
        };
    };
    enum {
        a = (x < 0 ? -x : x),
        b = (y < 0 ? -y : y),
        alce = Mult<a, b>::valor,
        valor = ((x < 0) xor (y < 0) ? -alce : alce)
    };
};

int main() {

    std::cout << Multiplicar<-3, -2>::valor << std::endl;

    std::cout << "MINHA MATRICULA È 12100759";

    return 0;
}