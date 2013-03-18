// arpeggio.cpp
// Allen Porter <allen@thebends.org>

#include "arpeggio.h"
#include <assert.h>
#include <stdlib.h>
#include "key_stack.h"

namespace synth {

static const int kNotesPerOctave = 12;

Arpeggio::Arpeggio(KeyStack* keys) : keys_(keys),
                                     sample_(0),
                                     samples_per_note_(1),
                                     octaves_(1),
                                     note_(-1),
                                     moving_up_(true),
                                     step_(UP)
                                     { }

Arpeggio::~Arpeggio() { }

void Arpeggio::set_octaves(int count) {
  assert(count >= 1);
  octaves_ = count;
}

void Arpeggio::set_step(Step step) {
  step_ = step;
}

void Arpeggio::set_samples_per_note(long samples_per_note) {
  samples_per_note_ = samples_per_note;
}

float Arpeggio::GetValue() {
  int note = GetNote();
  if (note > 0) {
    return KeyToFrequency(GetNote());
  }
  return 0.0f;
}

int Arpeggio::GetNote() {
  int size = keys_->size();
  if (size <= 0) {
    return 0;
  }
  int max = octaves_ * size;
  if (sample_ == 0) {
    if (step_ == UP) {
      note_ = (note_ + 1) % max;
    } else if (step_ == DOWN) {
      note_--;
      if (note_ < 0) {
        note_ = max - 1;
      }
    } else if (step_ == UP_DOWN) {
      if (moving_up_) {
        note_++;
        if (note_ >= max - 1) {
          note_ = max - 1;
          moving_up_ = false;
        }
      } else {
        note_--;
         if (note_ <= 0) {
          moving_up_ = true;
          note_ = 0;
        }
     }
    } else {
      note_ = random() % max;
    }
  }
  sample_ = (sample_ + 1) % samples_per_note_;
  int octave = note_ / size;
  return octave * kNotesPerOctave + keys_->GetNote(note_ % size);
}
  
void Arpeggio::reset() {
  sample_ = 0;
}

}  // namespace synth
