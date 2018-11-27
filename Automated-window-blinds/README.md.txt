# Window blind automation DIY project

### Current status: proof-of-concept (waiting to be continued)

Arduino Nano and ESP8266 based multiple window automation system.
Webserver-code is for the "master"-nano, that will distribute the requests to
respective nanos (or this functionality will be centralized to some other 
home-server eg. RPi)

Includes currently:
 - Basic Webserver code used in the proof of concept testing
 - Some kind of PCB-board layout that supports LDR, Potentiometer input and servo output + ESP8266-plugin
 - 3D-printable adapter between servo and D-shaft