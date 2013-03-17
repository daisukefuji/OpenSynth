// envelope_test.cpp
// Author: Allen Porter <allen@thebends.org>

#include <assert.h>
#include <iostream>
#include "synth/envelope.h"
#include "synth/test_util.h"

namespace {

static void TestFlat() {
  synth::Envelope env;
  env.NoteOn();
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, env.GetValue());
  }
  env.NoteOff();
  assert(0.0 == env.GetValue());
}

static void TestZero() {
  synth::Envelope env;
  env.set_attack(0);
  env.set_decay(0);
  env.set_sustain(0);
  env.set_release(0);
  env.NoteOn();
  for (int i = 0; i < 10; ++i) { 
    ASSERT_DOUBLE_EQ(0.0, env.GetValue());
  }
  env.NoteOff();
  ASSERT_DOUBLE_EQ(0.0, env.GetValue());
}

static void TestCurve() {
  synth::Envelope env;
  env.set_attack(4);
  env.set_decay(4);
  env.set_sustain(0.45);
  env.set_release(8);
  env.NoteOn();
  // Attack
  ASSERT_DOUBLE_EQ(0.25, env.GetValue());
  ASSERT_DOUBLE_EQ(0.5, env.GetValue());
  ASSERT_DOUBLE_EQ(0.75, env.GetValue());
  ASSERT_DOUBLE_EQ(1.0, env.GetValue());
  // Decay
  ASSERT_DOUBLE_EQ(0.8625, env.GetValue());
  ASSERT_DOUBLE_EQ(0.725, env.GetValue());
  ASSERT_DOUBLE_EQ(0.5875, env.GetValue());
  // Sustain
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.45, env.GetValue());
  }
  env.NoteOff();
  ASSERT_DOUBLE_EQ(0.39375, env.GetValue());
  ASSERT_DOUBLE_EQ(0.3375, env.GetValue());
  ASSERT_DOUBLE_EQ(0.28125, env.GetValue());
  ASSERT_DOUBLE_EQ(0.225, env.GetValue());
  ASSERT_DOUBLE_EQ(0.16875, env.GetValue());
  ASSERT_DOUBLE_EQ(0.1125, env.GetValue());
  ASSERT_DOUBLE_EQ(0.05625, env.GetValue());
  // Done
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, env.GetValue());
  }
}

static void TestAttackRelease() {
  synth::Envelope env;
  env.set_attack(4);
  env.set_decay(0);
  env.set_sustain(0.99);  // ignored since we never reach it
  env.set_release(3);
  env.NoteOn();
  // Attack
  ASSERT_DOUBLE_EQ(0.25, env.GetValue());
  ASSERT_DOUBLE_EQ(0.5, env.GetValue());
  ASSERT_DOUBLE_EQ(0.75, env.GetValue());
  env.NoteOff();
  // Released before we finished attacking.  Release from the current value
  ASSERT_DOUBLE_EQ(0.5, env.GetValue());
  ASSERT_DOUBLE_EQ(0.25, env.GetValue());
  ASSERT_DOUBLE_EQ(0.0, env.GetValue());
}

static void TestDecay() {
  synth::Envelope env;
  env.set_attack(0);
  env.set_decay(5);
  env.set_sustain(0.0);
  env.set_release(8);
  env.NoteOn();
  ASSERT_DOUBLE_EQ(0.8, env.GetValue());
  ASSERT_DOUBLE_EQ(0.6, env.GetValue());
  ASSERT_DOUBLE_EQ(0.4, env.GetValue());
  ASSERT_DOUBLE_EQ(0.2, env.GetValue());
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, env.GetValue());
  }
  env.NoteOff();
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, env.GetValue());
  }
}

static void TestDecayRelease() {
  synth::Envelope env;
  env.set_attack(4);
  env.set_decay(4);
  env.set_sustain(0.5);  // ignored since we never reach it
  env.set_release(8);
  env.NoteOn();
  // Attack
  ASSERT_DOUBLE_EQ(0.25, env.GetValue());
  ASSERT_DOUBLE_EQ(0.5, env.GetValue());
  ASSERT_DOUBLE_EQ(0.75, env.GetValue());
  ASSERT_DOUBLE_EQ(1.0, env.GetValue());
  // Decay
  ASSERT_DOUBLE_EQ(0.875, env.GetValue());
  ASSERT_DOUBLE_EQ(0.75, env.GetValue());
  env.NoteOff();
  // Released before we finished decaying.  Release starts from the decay point.
  ASSERT_DOUBLE_EQ(0.65625, env.GetValue());
  ASSERT_DOUBLE_EQ(0.5625, env.GetValue());
  ASSERT_DOUBLE_EQ(0.46875, env.GetValue());
  ASSERT_DOUBLE_EQ(0.375, env.GetValue());
  ASSERT_DOUBLE_EQ(0.28125, env.GetValue());
  ASSERT_DOUBLE_EQ(0.1875, env.GetValue());
  ASSERT_DOUBLE_EQ(0.09375, env.GetValue());
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, env.GetValue());
  }
}

}  // namespace

int main(int argc, char* argv[]) {
  TestFlat();
  TestZero();
  TestCurve();
  TestAttackRelease();
  TestDecay();
  TestDecayRelease();
  std::cout << "PASS" << std::endl;
  return 0;
}
