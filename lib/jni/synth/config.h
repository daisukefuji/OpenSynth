// config.h
//

#ifndef __CONFIG_H__
#define __CONFIG_H__

#include <SLES/OpenSLES.h>

#define CONFIG_CH CONFIG_CH_MONO
#define CONFIG_CH_MONO 1
#define CONFIG_CH_STEREO 2

#define CONFIG_SAMPLINGRATE SL_SAMPLINGRATE_44_1

namespace synth {
class Config {
 public:
    static const float kSampleRate = 44100.0f;
};
}  // namespace synth

#endif  // __CONFIG_H__
