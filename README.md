## Introduction

This project focuses on using low-energy beacons with an Android application. We will develop an app that sends a notification when a specific URL is detected for the first time by an authorized beacon. After the initial detection, the app will ignore subsequent signals from the same beacon.

If the app is open when the beacon is received for the first time, a new page will be opened with the corresponding web URL loaded.

## Libraries to be used

1. Alt Beacon library
2. MVVM related libraries
3. Room Library

### UI Screens

1. Splash screen: First screen with logo
2. Home screen: Displays the history of received beacons.
3. WebView screen: Shows the content of the received beacon data.

### ******************************Functionalities******************************

1. Normal Home Page which is dumb.
2. a background service will always be running and scanning for the beacons.
3. once a beacon is found and the app is in the background one notification will be triggered.
4. if the app is in the foreground then the webview screen will be opened.
5. or on click of notification, the Web-view screen will open.



tags to search
open activity with beacon signal
scan beacon
scan and open activity in background
