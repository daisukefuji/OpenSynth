// glide_test.cpp
// Author: Allen Porter <allen@thebends.org>

#include <assert.h>
#include <iostream>
#include "synth/lag_processor.h"
#include "synth/test_util.h"

namespace {

static void TestFlat() {
  synth::MutableParameter param(1.0);
  synth::LagProcessor glide(&param);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, glide.GetValue());
  }
  // Rate has no effect when nothing changes
  glide.set_samples(10);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, glide.GetValue());
  }
}

static void TestUpDown() {
  synth::MutableParameter param(0.0);
  synth::LagProcessor glide(&param);
  glide.set_samples_up(4);
  glide.set_samples_down(10);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, glide.GetValue());
  }
  // Walk up over 5 samples
  param.set_value(2.0);

  ASSERT_DOUBLE_EQ(0.25, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.5, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.75, glide.GetValue());
  ASSERT_DOUBLE_EQ(1.0, glide.GetValue());
  ASSERT_DOUBLE_EQ(1.25, glide.GetValue());
  ASSERT_DOUBLE_EQ(1.5, glide.GetValue());
  ASSERT_DOUBLE_EQ(1.75, glide.GetValue());
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(2.0, glide.GetValue());
  }
  param.set_value(1.0);
  glide.set_samples_down(0);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, glide.GetValue());
  }
  // Walk down over 10 samples
  param.set_value(0.0);
  glide.set_samples_down(10);
  ASSERT_DOUBLE_EQ(0.9, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.8, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.7, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.6, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.5, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.4, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.3, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.2, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.1, glide.GetValue());
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, glide.GetValue());
  }
}

static void TestInterrupt() {
  synth::MutableParameter param(0.0);
  synth::LagProcessor glide(&param);
  glide.set_samples_up(5);
  glide.set_samples_down(4);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, glide.GetValue());
  }
  // Walk up over 5 samples, but interrupt in the middle.
  param.set_value(1.0);
  ASSERT_DOUBLE_EQ(0.2, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.4, glide.GetValue());
  param.set_value(0.0);
  ASSERT_DOUBLE_EQ(0.3, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.2, glide.GetValue());
  ASSERT_DOUBLE_EQ(0.1, glide.GetValue());
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, glide.GetValue());
  }
}

static void TestImmediate() {
  synth::MutableParameter param(0.0);
  synth::LagProcessor glide(&param);
  glide.set_samples(0);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, glide.GetValue());
  }
  param.set_value(1.0);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, glide.GetValue());
  }
}

static void TestReset() {
  synth::MutableParameter param(0.0);
  synth::LagProcessor glide(&param);
  glide.set_samples(2);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(0.0, glide.GetValue());
  }
  // No glide happens after a reset
  glide.reset();
  param.set_value(1.0);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(1.0, glide.GetValue());
  }
  param.set_value(2.0);
  ASSERT_DOUBLE_EQ(1.5, glide.GetValue());
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(2.0, glide.GetValue());
  }
  // No glide happens after a reset
  glide.reset();
  param.set_value(3.0);
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(3.0, glide.GetValue());
  }
  param.set_value(2.0);
  ASSERT_DOUBLE_EQ(2.5, glide.GetValue());
  for (int i = 0; i < 10; ++i) {
    ASSERT_DOUBLE_EQ(2.0, glide.GetValue());
  }
}

}  // namespace

int main(int argc, char* argv[]) {
  TestFlat();
  TestUpDown();
  TestInterrupt();
  TestImmediate();
  TestReset();
  std::cout << "PASS" << std::endl;
  return 0;
}
