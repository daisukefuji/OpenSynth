// test_util.h
// Author: Allen Porter <allen@thebends.org>

#include <math.h>

#ifndef __TEST_UTIL_H__
#define __TEST_UTIL_H__

// HACK
#define ASSERT_DOUBLE_EQ(x, y) (assert(fabs(x - y) < 0.001))

#endif  // __TEST_UTIL_H__

