#ifndef SLEEP_APP_H
#define SLEEP_APP_H

#ifdef SIMULATE
#include <time.h>
void delay(unsigned int d) {
    struct timespec tim;
    tim.tv_sec = d/1000000;
    tim.tv_nsec = (d%1000000)*1000;
    int result = -1;
    while (result == -1) {
        struct timespec tim2;
        result = nanosleep(&tim, &tim2);
        tim = tim2;
    }
}
#else
#include <alarm.h>
void delay(unsigned int d) {
    EPOS::Delay x(d);
}
#endif

#endif
