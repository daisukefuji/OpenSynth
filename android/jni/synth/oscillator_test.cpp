// oscillator_test.cpp
// Author: Allen Porter <allen@thebends.org>

#include <assert.h>
#include <iostream>
#include "synth/oscillator.h"
#include "synth/parameter.h"
#include "synth/test_util.h"

namespace {

static void TestSine() {
  synth::FixedParameter freq(1.0);  // one cycle per second
  synth::Oscillator osc;
  osc.set_sample_rate(4);
  osc.set_wave_type(synth::Oscillator::SINE);
  osc.set_frequency(&freq);

  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-1.0, osc.GetValue());
  }
}

static void TestSquare() {
  synth::FixedParameter freq(1.0);  // one cycle per second
  synth::Oscillator osc;
  osc.set_sample_rate(7);
  osc.set_wave_type(synth::Oscillator::SQUARE);
  osc.set_frequency(&freq);


  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-1.0, osc.GetValue());
  }
}

static void TestSawtooth() {
  synth::FixedParameter freq(1.0);  // one cycle per second
  synth::Oscillator osc;
  osc.set_sample_rate(8);
  osc.set_wave_type(synth::Oscillator::SAWTOOTH);
  osc.set_frequency(&freq);

  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(-1, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.75, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.5, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.25, osc.GetValue());
    ASSERT_DOUBLE_EQ(0, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.25, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.5, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.75, osc.GetValue());
  }
}

static void TestSawtooth2() {
  synth::FixedParameter freq(2.0);  // two cycles per second
  synth::Oscillator osc;
  osc.set_sample_rate(8);
  osc.set_wave_type(synth::Oscillator::SAWTOOTH);
  osc.set_frequency(&freq);

  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(-1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.5, osc.GetValue());
    ASSERT_DOUBLE_EQ(0, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.5, osc.GetValue());
  }
}

static void TestReverseSawtooth() {
  synth::FixedParameter freq(1.0);  // one cycle per second
  synth::Oscillator osc;
  osc.set_sample_rate(8);
  osc.set_wave_type(synth::Oscillator::REVERSE_SAWTOOTH);
  osc.set_frequency(&freq);

  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.75, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.5, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.25, osc.GetValue());
    ASSERT_DOUBLE_EQ(0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.25, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.5, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.75, osc.GetValue());
  }
}

static void TestTriangle() {
  synth::FixedParameter freq(1.0);  // one cycle per second
  synth::Oscillator osc;
  osc.set_sample_rate(8);
  osc.set_wave_type(synth::Oscillator::TRIANGLE);
  osc.set_frequency(&freq);

  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.5, osc.GetValue());
    ASSERT_DOUBLE_EQ(0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.5, osc.GetValue());
    ASSERT_DOUBLE_EQ(-1, osc.GetValue());
    ASSERT_DOUBLE_EQ(-0.5, osc.GetValue());
    ASSERT_DOUBLE_EQ(0, osc.GetValue());
    ASSERT_DOUBLE_EQ(0.5, osc.GetValue());
  }
}

static void TestTriangle2() {
  synth::FixedParameter freq(2.0);  // two cycles per second
  synth::Oscillator osc;
  osc.set_sample_rate(8);
  osc.set_wave_type(synth::Oscillator::TRIANGLE);
  osc.set_frequency(&freq);

  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, osc.GetValue());
    ASSERT_DOUBLE_EQ(0, osc.GetValue());
    ASSERT_DOUBLE_EQ(-1, osc.GetValue());
    ASSERT_DOUBLE_EQ(0, osc.GetValue());
  }
}

}  // namespace

int main(int argc, char* argv[]) {
  TestSine();
  TestSquare();
  TestTriangle();
  TestTriangle2();
  TestSawtooth();
  TestSawtooth2();
  TestReverseSawtooth();
  std::cout << "PASS" << std::endl;
  return 0;
}
