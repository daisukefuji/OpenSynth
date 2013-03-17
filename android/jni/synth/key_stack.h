// key_stack.h
// Author: Allen Porter <allen@thebends.org>
//
// The key stack keeps track of notes pressed and released, determining when
// the attack phase should be entered or when the note should just change
// without starting a new note attack.

#ifndef __KEY_STACK_H__
#define __KEY_STACK_H__

namespace synth {

class KeyStack {
 public:
  // We can't push more notes on the stack than this
  static const int kMaxSize = 64;

  KeyStack();
  ~KeyStack();

  // Returns true if this was the first note pushed on to the key stack
  bool NoteOn(int note);

  // Returns true if this was the last note removed from the key stack
  bool NoteOff(int note);

  // Returns the current not, or 0 if no note is playing.
  int GetCurrentNote();

  // Return the note at the specified position in the stack.  num must be less
  // than size. 
  int GetNote(int num);
  
  bool IsNoteInStack(int note);

  int size();

  void clear() { size_ = 0; }

 private:
  int size_;
  int notes_[kMaxSize];
  // Number of times the note at the position was pressed
  int count_[kMaxSize];
};

float KeyToFrequency(int key);

}  // namespace synth

#endif  // __KEY_STACK_H__
