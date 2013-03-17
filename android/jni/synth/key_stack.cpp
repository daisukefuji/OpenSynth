// key_stack.cpp
// Author: Allen Porter <allen@thebends.org>

#include "key_stack.h"
#include <math.h>
#include <assert.h>

using namespace std;

namespace synth {

KeyStack::KeyStack() : size_(0) { }

KeyStack::~KeyStack() { }

bool KeyStack::NoteOn(int note) {
  assert(size_ < kMaxSize);
  for (int i = 0; i < size_; ++i) {
    if (notes_[i] == note) {
      count_[i]++;
      return false;
    }
  }
  notes_[size_] = note;
  count_[size_] = 1;
  size_++;
  return true;
}

bool KeyStack::NoteOff(int note) {
  for (int i = 0; i < size_; ++i) {
    if (notes_[i] == note) {
      count_[i]--;
      if (count_[i] == 0) {
        // Remove this element from the stack -- copy all elements above
        for (int j = i; j < size_ - 1; ++j) {
          notes_[j] = notes_[j + 1];
          count_[j] = count_[j + 1];
        }
        size_--;
      }
      return true;
    }
  }
  // The note wasn't on the stack.  The multi-touch events on the iphone seem
  // to be flaky, so we don't worry if we were asked to remove something that
  // was not on the stack.  The controller also calls our clear() method when
  // no touch events are left as a fallback. 
  return false;
}
  
bool KeyStack::IsNoteInStack(int note) {
  for (int i = 0; i < size_; ++i) {
    if (notes_[i] == note) {
      return true;
    }
  }
  return false;
}

int KeyStack::size() {
  int count = 0;
  for (int i = 0; i < size_; ++i) {
    count += count_[i];
  }
  return count;
}

int KeyStack::GetCurrentNote() {
  if (size_ > 0) {
    return notes_[size_ - 1];
  }
  return 0;
}

int KeyStack::GetNote(int num) {
  if (num >= size_) {
    return 0;
  }
  return notes_[num];
}

static const int kMiddleAKey(49);
static const float kNotesPerOctave = 12.0f;
static const float kMiddleAFrequency = 440.0f;

float KeyToFrequency(int key) {
  return kMiddleAFrequency * powf(2, (key - kMiddleAKey) / kNotesPerOctave);
}

}  // namespace synth
