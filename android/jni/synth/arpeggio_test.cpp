// arpeggio_test.cpp
// Author: Allen Porter <allen@thebends.org>

#include <assert.h>
#include <iostream>
#include "synth/arpeggio.h"
#include "synth/key_stack.h"
#include "synth/test_util.h"

namespace {

static void TestNoKeysDown() {
  synth::KeyStack keys;
  synth::Arpeggio arpeggio(&keys);
  for (int i = 0; i < 10; ++i) {
    assert(arpeggio.GetNote() == 0);
  }
}

static void TestOneKeyDown() {
  synth::KeyStack keys;
  synth::Arpeggio arpeggio(&keys);
  keys.NoteOn(16);
  for (int i = 0; i < 10; ++i) {
    assert(arpeggio.GetNote() == 16);
  }
}

static void TestUp() {
  synth::KeyStack keys;
  keys.NoteOn(16);
  keys.NoteOn(17);
  keys.NoteOn(18);

  synth::Arpeggio arpeggio(&keys);
  for (int i = 0; i < 10; ++i) {
    assert(arpeggio.GetNote() == 16);
    assert(arpeggio.GetNote() == 17);
    assert(arpeggio.GetNote() == 18);
  }
}

static void TestDown() {
  synth::KeyStack keys;
  keys.NoteOn(16);
  keys.NoteOn(17);
  keys.NoteOn(18);

  synth::Arpeggio arpeggio(&keys);
  arpeggio.set_step(synth::Arpeggio::DOWN);
  for (int i = 0; i < 10; ++i) {
    assert(arpeggio.GetNote() == 18);
    assert(arpeggio.GetNote() == 17);
    assert(arpeggio.GetNote() == 16);
  }
}

static void TestUpDown() {
  synth::KeyStack keys;
  keys.NoteOn(16);
  keys.NoteOn(17);
  keys.NoteOn(18);

  synth::Arpeggio arpeggio(&keys);
  arpeggio.set_step(synth::Arpeggio::UP_DOWN);
  for (int i = 0; i < 10; ++i) {
    assert(arpeggio.GetNote() == 16);
    assert(arpeggio.GetNote() == 17);
    assert(arpeggio.GetNote() == 18);
    assert(arpeggio.GetNote() == 17);
  }
}

static void TestOctaves() {
  synth::KeyStack keys;
  keys.NoteOn(16);
  keys.NoteOn(17);

  synth::Arpeggio arpeggio(&keys);
  arpeggio.set_octaves(2);
  arpeggio.set_step(synth::Arpeggio::UP);
  for (int i = 0; i < 10; ++i) {
    assert(arpeggio.GetNote() == 16);
    assert(arpeggio.GetNote() == 17);
    assert(arpeggio.GetNote() == 16 + 12);
    assert(arpeggio.GetNote() == 17 + 12);
  }
}

static void TestSamplesPerNote() {
  synth::KeyStack keys;
  keys.NoteOn(16);
  keys.NoteOn(17);

  synth::Arpeggio arpeggio(&keys);
  arpeggio.set_samples_per_note(3);
  arpeggio.set_step(synth::Arpeggio::UP);
  for (int i = 0; i < 10; ++i) {
    assert(arpeggio.GetNote() == 16);
    assert(arpeggio.GetNote() == 16);
    assert(arpeggio.GetNote() == 16);
    assert(arpeggio.GetNote() == 17);
    assert(arpeggio.GetNote() == 17);
    assert(arpeggio.GetNote() == 17);
  }
}
}  // namespace

int main(int argc, char* argv[]) {
  TestNoKeysDown();
  TestOneKeyDown();
  TestUp();
  TestDown();
  TestUpDown();
  TestOctaves();
  TestSamplesPerNote();
  std::cout << "PASS" << std::endl;
  return 0;
}
