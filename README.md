# TUmine kotlin part - developer accessible classes - DEPRECATED #

# USE [the rewritten TUmine library in Java](https://github.com/konstantintuev/TUmine-JavaLibraryDevAccessible). #

## What is this? ##

These are all the classes that the developer integrating the miner might need.

## Where are the rest of the classes? ##

You can purchase the whole source code (kotlin + c++) on my [website](https://android-miner.tuev-co.eu/#pricing).

## How can I use these files? ##

Only use them for educational purposes

## Can I contribute? ##

**YES**, they biggest contributors will get the low fee version of the miner for FREE.

## How to include the miner in my app? ##

There different versions + I have made a self-hosted repository

Here are all the versions (add the attached snippet to the app/build.gradle):
* This version of the TUmine Monero .aar library doesn't contain native miner. The needed file will be downloaded once.<br>
    ```gradle
    implementation 'tuev.co:tumine:6.0-nonative'
    ```

* This version of the TUmine Monero .aar library contains native miner with SSL support. -> THE MINER HAS BIGGER SIZE (contains openssl)<br>
    ```gradle
    implementation 'tuev.co:tumine:6.0-ssl'
    ```
* This version of the TUmine Monero .aar library contains native miner WITHOUT SSL support.<br>
    ```gradle
    implementation 'tuev.co:tumine:6.0-basic'
    ```

Also add my maven repository to the root build.gradle of your project.
```gradle
allprojects {
    repositories {
        ...
        maven {
            url 'https://maven.tuev-co.eu/repository/internal'
        }
    }
}
```

The files from my maven repository have the same sources + documentation + '.aar'.

## License ##

This is the license for downloading and using the binaries and this source code: [Binaries LICENSE](License_binaries.pdf)<br/>
 -> TL;DR: Without a purchase of the whole source code of the miner no permission is given to anyone to edit, modify or redistribute the native and kotlin binaries.

  The files in this repo can only be used for contribution to this project and for educational purposes.<br/><br/>
This is the license for buying the WHOLE source code: [Source Code LICENSE](License.pdf)
