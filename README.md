# TM88-II
This library was made to communicate with an TM88-II printer via an arduino that acts as a serial to parallel adapter.

The program for the arduino can be found under "firmware". It simply takes whatever byte gets send to it, puts it onto the data lines and pulses the strobe line 