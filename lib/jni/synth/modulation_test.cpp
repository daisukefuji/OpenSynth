// envelope_test.cpp
// Author: Allen Porter <allen@thebends.org>

#include <assert.h>
#include <iostream>
#include "synth/modulation.h"
#include "synth/oscillator.h"
#include "synth/test_util.h"

namespace {

static void TestNoLevel() {
  synth::FixedParameter osc(0.2);
  synth::FixedParameter level(0.0);
  synth::LFO mod;
  mod.set_oscillator(&osc);
  mod.set_level(&level);
  ASSERT_DOUBLE_EQ(1.0, mod.GetValue()); 
}

static void TestMaxLevel() {
  synth::MutableParameter osc(0.2);
  synth::FixedParameter level(1.0);

  synth::LFO mod;
  mod.set_level(&level);
  mod.set_oscillator(&osc);

  osc.set_value(-1);
  ASSERT_DOUBLE_EQ(0.0, mod.GetValue()); 
  osc.set_value(-0.5);
  ASSERT_DOUBLE_EQ(0.25, mod.GetValue()); 
  osc.set_value(0.0);
  ASSERT_DOUBLE_EQ(0.5, mod.GetValue()); 
  osc.set_value(0.5);
  ASSERT_DOUBLE_EQ(0.75, mod.GetValue()); 
  osc.set_value(1.0);
  ASSERT_DOUBLE_EQ(1.0, mod.GetValue()); 
}

static void TestMidLevel() {
  synth::MutableParameter osc(0.0);
  synth::FixedParameter level(0.2);

  synth::LFO mod;
  mod.set_level(&level);
  mod.set_oscillator(&osc);

  osc.set_value(-1);
  ASSERT_DOUBLE_EQ(0.8, mod.GetValue()); 
  osc.set_value(-0.5);
  ASSERT_DOUBLE_EQ(0.85, mod.GetValue()); 
  osc.set_value(0.0);
  ASSERT_DOUBLE_EQ(0.9, mod.GetValue()); 
  osc.set_value(0.5);
  ASSERT_DOUBLE_EQ(0.95, mod.GetValue()); 
  osc.set_value(1.0);
  ASSERT_DOUBLE_EQ(1.0, mod.GetValue()); 
}

}  // namespace

int main(int argc, char* argv[]) {
  TestNoLevel();
  TestMaxLevel();
  TestMidLevel();
  std::cout << "PASS" << std::endl;
  return 0;
}
