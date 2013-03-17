ECHO = echo
INSTALL_DIR = /usr/local
CC = g++
CFLAGS = -Wall -Werror -I../ -L. -g \
         -I/System/Library/Frameworks/JavaVM.framework/Headers
.PHONY: all clean test

SRC = oscillator.cpp envelope.cpp controller.cpp modulation.cpp filter.cpp \
      parameter.cpp lag_processor.cpp key_stack.cpp arpeggio.cpp synth-jni.cpp
OBJ = $(SRC:.cpp=.o)

all: $(SUBDIRS) libsynth.a

.cpp.o:
	$(CC) -c $(CFLAGS) $<

libsynth.a: $(OBJ)
	$(AR) rc $@ $(OBJ)

oscillator_test: libsynth.a oscillator_test.o
	$(CC) -o $@ $(CFLAGS) -lsynth oscillator_test.o

envelope_test: libsynth.a envelope_test.o
	$(CC) -o $@ $(CFLAGS) -lsynth envelope_test.o

modulation_test: libsynth.a modulation_test.o
	$(CC) -o $@ $(CFLAGS) -lsynth modulation_test.o

lag_processor_test: libsynth.a lag_processor_test.o
	$(CC) -o $@ $(CFLAGS) -lsynth lag_processor_test.o

key_stack_test: libsynth.a key_stack_test.o
	$(CC) -o $@ $(CFLAGS) -lsynth key_stack_test.o

arpeggio_test: libsynth.a arpeggio_test.o
	$(CC) -o $@ $(CFLAGS) -lsynth arpeggio_test.o

controller_test: libsynth.a controller_test.o
	$(CC) -o $@ $(CFLAGS) -lsynth controller_test.o

test: oscillator_test envelope_test modulation_test controller_test \
      lag_processor_test key_stack_test arpeggio_test
	./envelope_test
	./lag_processor_test
	./oscillator_test
	./modulation_test
	./key_stack_test
	./arpeggio_test
	./controller_test

install: libsynth.a
	cp libsynth.a /usr/local/lib/
	mkdir -p /usr/local/include/synth/
	cp -rp *.h /usr/local/include/synth/

clean:
	-$(RM) *.o *.a __* *_test
