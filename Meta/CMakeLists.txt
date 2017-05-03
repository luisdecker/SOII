cmake_minimum_required(VERSION 3.7)
project(Meta)

set(CMAKE_CXX_STANDARD 11)
set(HIGH_RECURSION "-ftemplate-depth=10000")
set(SOURCE_FILES main.cpp)
add_definitions(${HIGH_RECURSION})
add_executable(Meta ${SOURCE_FILES})