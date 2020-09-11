<a href="http://navigine.com"><img src="https://navigine.com/assets/web/images/logo.svg" align="right" height="60" width="180" hspace="10" vspace="5"></a>

# Android SDK

The following sections describe the contents of the Navigine Android SDK repository. The files in our public repository for Android are: 

- sources of the Navigine Demo Application for Android
- Navigine SDK for Android in form of a JAR file

## Useful Links

- Refer to the [Navigine official website](https://navigine.com/) for complete list of downloads, useful materials, information about the company, and so on.
- [Get started](http://client.navigine.com/login) with Navigine to get full access to Navigation services, SDKs, and applications.
- Refer to the Navigine [User Manual](http://docs.navigine.com/) for complete product usage guidelines.
- Find company contact information at the official website under <a href="https://navigine.com/contacts/">Contact</a> tab.

## Android Demo Application

Navigine Demo application for Android enables you to test indoor navigation as well as measure your target location's radiomap.
Source files as well as compiled application reside in the Navigine folder and nested folders.

To get the Navigine demo application for Android,

- Either [download the precompiled APK file](https://github.com/Navigine/Android-SDK/blob/master/NavigineDemo/NavigineDemo-debug.apk).
- Or compile the application yourself [using sources, available at GitHub](https://github.com/Navigine/Android-SDK/tree/master/NavigineDemo).

For complete guidelines on using the Demo, refer to the [corresponding sections in the Navigine User Manual](http://docs.navigine.com/ud_android_demo.html), or refer to the Help file incorporated into the application.

## Navigation SDK

Navigine SDK for Android applications enables you to develop your own indoor navigation apps using the well-developed methods, classes, and functions created by the Navigine team.
The SDK file resides in the NavigineSDK folder.

Refer to the [NavigineSDK](https://github.com/Navigine/Android-SDK/tree/master/NavigineSDK) for the SDK used for indoor navigation.
Find formal description of Navigine-SDK API including the list of classes and their public fields and methods at [Navigine SDK wiki](https://github.com/Navigine/Android-SDK/wiki).

### Using with Jitpack

#### Gradle

Add this in root *build.gradle* at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```
dependencies {
  ...
  implementation 'com.github.Navigine:Android-SDK:20191220'
}
```
