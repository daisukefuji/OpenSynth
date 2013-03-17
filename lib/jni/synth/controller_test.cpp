// controller_test.cpp
// Author: Allen Porter <allen@thebends.org>

#include <assert.h>
#include <iostream>
#include "synth/controller.h"
#include "synth/envelope.h"
#include "synth/modulation.h"
#include "synth/oscillator.h"
#include "synth/parameter.h"
#include "synth/test_util.h"

namespace {

static void TestController() {
  synth::Controller controller;
  controller.set_osc1_level(0.5);
  controller.set_osc1_wave_type(synth::Oscillator::TRIANGLE);
  controller.set_sample_rate(16);

  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, controller.GetSample());
  }

  controller.NoteOnFrequency(2);  // 2 cycles per second

  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.5, controller.GetSample());
    ASSERT_DOUBLE_EQ(0.25, controller.GetSample());
    ASSERT_DOUBLE_EQ(0, controller.GetSample());
    ASSERT_DOUBLE_EQ(-0.25, controller.GetSample());
    ASSERT_DOUBLE_EQ(-0.5, controller.GetSample());
    ASSERT_DOUBLE_EQ(-0.25, controller.GetSample());
    ASSERT_DOUBLE_EQ(0, controller.GetSample());
    ASSERT_DOUBLE_EQ(0.25, controller.GetSample());
  }

  // Silence
  controller.NoteOff();
  for (int i = 0; i < 10; i++) {
    ASSERT_DOUBLE_EQ(0.0, controller.GetSample());
  }
}

}  // namespace

int main(int argc, char* argv[]) {
  TestController();
  std::cout << "PASS" << std::endl;
  return 0;
}
