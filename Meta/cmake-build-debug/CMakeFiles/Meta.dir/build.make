# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.7

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /home/decker/build/clion/clion-2017.1.1/bin/cmake/bin/cmake

# The command to remove a file.
RM = /home/decker/build/clion/clion-2017.1.1/bin/cmake/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/decker/Documents/SOII/Meta

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/decker/Documents/SOII/Meta/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/Meta.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/Meta.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Meta.dir/flags.make

CMakeFiles/Meta.dir/main.cpp.o: CMakeFiles/Meta.dir/flags.make
CMakeFiles/Meta.dir/main.cpp.o: ../main.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/decker/Documents/SOII/Meta/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/Meta.dir/main.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/Meta.dir/main.cpp.o -c /home/decker/Documents/SOII/Meta/main.cpp

CMakeFiles/Meta.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Meta.dir/main.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/decker/Documents/SOII/Meta/main.cpp > CMakeFiles/Meta.dir/main.cpp.i

CMakeFiles/Meta.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Meta.dir/main.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/decker/Documents/SOII/Meta/main.cpp -o CMakeFiles/Meta.dir/main.cpp.s

CMakeFiles/Meta.dir/main.cpp.o.requires:

.PHONY : CMakeFiles/Meta.dir/main.cpp.o.requires

CMakeFiles/Meta.dir/main.cpp.o.provides: CMakeFiles/Meta.dir/main.cpp.o.requires
	$(MAKE) -f CMakeFiles/Meta.dir/build.make CMakeFiles/Meta.dir/main.cpp.o.provides.build
.PHONY : CMakeFiles/Meta.dir/main.cpp.o.provides

CMakeFiles/Meta.dir/main.cpp.o.provides.build: CMakeFiles/Meta.dir/main.cpp.o


# Object files for target Meta
Meta_OBJECTS = \
"CMakeFiles/Meta.dir/main.cpp.o"

# External object files for target Meta
Meta_EXTERNAL_OBJECTS =

Meta: CMakeFiles/Meta.dir/main.cpp.o
Meta: CMakeFiles/Meta.dir/build.make
Meta: CMakeFiles/Meta.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/decker/Documents/SOII/Meta/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable Meta"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Meta.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Meta.dir/build: Meta

.PHONY : CMakeFiles/Meta.dir/build

CMakeFiles/Meta.dir/requires: CMakeFiles/Meta.dir/main.cpp.o.requires

.PHONY : CMakeFiles/Meta.dir/requires

CMakeFiles/Meta.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/Meta.dir/cmake_clean.cmake
.PHONY : CMakeFiles/Meta.dir/clean

CMakeFiles/Meta.dir/depend:
	cd /home/decker/Documents/SOII/Meta/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/decker/Documents/SOII/Meta /home/decker/Documents/SOII/Meta /home/decker/Documents/SOII/Meta/cmake-build-debug /home/decker/Documents/SOII/Meta/cmake-build-debug /home/decker/Documents/SOII/Meta/cmake-build-debug/CMakeFiles/Meta.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/Meta.dir/depend

