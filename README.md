# Domotica Arduino for Android
Domotica Arduino for Android is written for all devices compatible with Android 1.6+ (minimum SDK 4).
It allows you to control some physical devices in your home using an Arduino with [DomoticaArduino](https://github.com/emanuele-palazzetti/DomoticaArduino) code.

The code in this repository can be imported as Eclipse Project with Android ADT plugin installed.

## Setup and configuration
It is possible to set some parameter before install the application in your device.

### Default Arduino IP
If your Arduino has a static IP, you can change the default value by edit the file <code>[App ROOT]/res/values/strings.xml</code> as follows:

    <string name="defaultArduinoIP">192.168.0.250</string>

The <code>192.168.0.250</code> IP, should be changed with your real Arduino IP setted in DomoticaArduino code.

### Type Mapping
In <code>Arduino.java</code> singleton class, there is the function <code>typeMapping(int type)</code> that you should change in order to map the type used in Android <code>ListView</code> with the one setted in DomoticaArduino code.

For example, if you add another variable in DomoticaArduino to match new kind of type, you should add a <code>case</code> in this function.

### Splashscreen
The first executed Activity is a Splashscreen with the logo of our [Linux User Group in Perugia](http://www.perugiagnulug.org/) (Italy). You can change splashscreen image by replacing the file <code>[App ROOT]/res/drawable/splashscreen.jpg</code> with your own.

## Usage
Run the application and insert your Arduino IP.
