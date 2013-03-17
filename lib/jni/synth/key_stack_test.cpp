// key_stack_test.cpp
// Author: Allen Porter <allen@thebends.org>

#include <assert.h>
#include <iostream>
#include "synth/key_stack.h"

namespace {

static void TestSimple() {
  synth::KeyStack stack;
  assert(0 == stack.GetCurrentNote());
  stack.NoteOn(1);
  assert(1 == stack.GetCurrentNote());
  assert(1 == stack.size());
  stack.NoteOff(1);
  assert(0 == stack.GetCurrentNote());
  assert(0 == stack.size());
}

static void TestMultiple() {
  synth::KeyStack stack;
  assert(0 == stack.GetCurrentNote());
  stack.NoteOn(1);
  assert(1 == stack.GetCurrentNote());
  assert(1 == stack.size());
  stack.NoteOn(2);
  assert(2 == stack.GetCurrentNote());
  assert(2 == stack.size());
  stack.NoteOff(1);
  assert(2 == stack.GetCurrentNote());
  assert(1 == stack.size());
  stack.NoteOn(3);
  assert(3 == stack.GetCurrentNote());
  assert(2 == stack.size());
  stack.NoteOff(3);
  assert(2 == stack.GetCurrentNote());
  assert(1 == stack.size());
  stack.NoteOff(2);
  assert(0 == stack.GetCurrentNote());
  assert(0 == stack.size());
  assert(0 == stack.GetNote(0));
}

static void TestDuplicates() {
  synth::KeyStack stack;
  stack.NoteOn(1);
  assert(1 == stack.GetCurrentNote());
  assert(1 == stack.size());
  stack.NoteOn(2);
  assert(2 == stack.GetCurrentNote());
  assert(2 == stack.size());
  stack.NoteOn(1);
  assert(2 == stack.GetCurrentNote());
  assert(3 == stack.size());
  stack.NoteOff(2);
  assert(1 == stack.GetCurrentNote());
  assert(2 == stack.size());
  stack.NoteOff(1);
  assert(1 == stack.GetCurrentNote());
  assert(1 == stack.size());
  stack.NoteOff(1);
  assert(0 == stack.GetCurrentNote());
  assert(0 == stack.size());
}

}  // namespace

int main(int argc, char* argv[]) {
  TestSimple();
  TestMultiple();
  TestDuplicates();
  std::cout << "PASS" << std::endl;
  return 0;
}
