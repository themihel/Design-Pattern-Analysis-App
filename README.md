# Design Pattern Analysis - App
Corresponding server: https://github.com/themihel/Design-Pattern-Analysis-Server

## Background
Background of this repository is a framework to give researchers the opportunity to have a look into closed source apps using their, mostly present, web app.
It is possible to run different tests, like turning specific design patterns on or off. Any change using the functionality of Javascript and CSS is possible.
Also you can track different groups to construct a study based on your need. Furthermore this platform can be used for any other field study using own content.

## Adaptions to be done
To get the app up and running for you study participants some adaptions need to be done. Mainly they're located in the MainActivity.
For this reason the constants of this class and their respective usage will be explained in the following:

`GROUP`: The group is used to get specific scripts and styles from the server. At current state the app needs to be build for every group separately.

`MODIFICATIONS_BASE_URL`: This URL specifies your server based on the other repo. The GROUP constant should be used to differentiate the groups from each other.

`MODIFICATION_VERSION_POSITION`: This integer specifies where the modification version in the script (delivered by the corresponding server) is located. If no code changes were made *before* the version constant in the JS file, no adaption is needed here. (Default: 12)

`TRACK_URL`: This URL is used to point at the server your data will be tracked. If you're using the provided server, `trackaction` is the way to go.

`USER_AGENT`: The user agent in the POST request to the tracking server may be adapted to your needs. (Default: Design-Pattern-Analysis-App)

`PLATFORM_URL`: This URL specifies the website which should be tracked. Also this is used to prevent opening any incoming links on the device from opening the original app.

`PLATFORM_PROTOCOL`: This is used to set the protocol of the tracked platform. In most cases 'https://' will be fine.

### Additional scripts or styles
If any additional scripts or styles are needed for any group and any version, you can consider putting the into the app to safe bandwidth. For example jQuery is included which makes it possible to easily access specific elements and modifying them.
But you can add anything you want here. Relevant code position: `MainActivity:87`.

## Any question?
Feel free to contact me.