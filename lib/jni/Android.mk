# Describes the sources for the NDK build system

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := synth

LOCAL_CFLAGS := -Werror -Wall 
LOCAL_C_INCLUDES := $(LOCAL_PATH)/synth

LOCAL_SRC_FILES := synth/oscillator.cpp \
                   synth/envelope.cpp \
                   synth/controller.cpp \
                   synth/modulation.cpp \
                   synth/filter.cpp \
                   synth/parameter.cpp \
                   synth/lag_processor.cpp \
                   synth/key_stack.cpp \
                   synth/arpeggio.cpp \
                   synth/synth-jni.cpp

LOCAL_SRC_FILES += synth/synth_engine.cpp \
                   synth/audio_engine.cpp

LOCAL_LDLIBS := -llog -lOpenSLES

include $(BUILD_SHARED_LIBRARY)
