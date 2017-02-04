# BlinkLED
This project uses your Android device to control the LED on Androidthings running on Raspbery Pi 3. This is a **very** simple project and

very rough around the edges. It is meant for prototyping a "virtual button" running on the cloud(aka cloud button). 

## How it works

There are 2 modules for this project.

1. Things app
2. Companion app

Companion app is a very simple app running on your Android phone/tablet with a button to control the LED wired to your Things app. 

The companion uses Firebase as a middleware to interface with the Things app.

## Video
https://www.youtube.com/watch?v=Z-TDwr_Ba4o

## Note:
The Things app is controlling `Pin 12(BCM 18)` on the Raspberry Pi 3. Adjust accordingly for your board. You will also need to 

add `google-services.json` from your Firebase project to `$PROJECT_ROOT/app/` and `$PROJECT_ROOT/ledswitch/`.


## Read more
[Click here](http://www.droidagency.com/android-things-virtual-button-3715) to read more about this project.
