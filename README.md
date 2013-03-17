This project is divided into several components:
* test - Tests for android package. Depends on JUnit.
* android - Android application project. Depends on Android.
* lib - Library for the synthesizer part.

This project is based on [mobilesynth][]. Thanks a lot Allen!

[mobilesynth]: https://code.google.com/p/mobilesynth/ "mobilesynth"


How to launch test activity
am start -a indigo_lab.intent.action.TEST -n com.indigo_lab.android.opensynth/com.indigo_lab.android.opensynth.tests.JavaImp
