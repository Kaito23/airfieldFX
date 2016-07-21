# airfieldFX
Self-contained Software Deployment Tool

airfieldFX is a fork from Adam Bien's airfield.
It is a self-contained auto-update utility written in Java 8. 
I just added some functions to the frontend like a JavaFX progressbar and the
option to skip the updateprocess.

Your clients can automatically distribute new versions to all connected Java clients.

At the very first start airfield will clone the remote repository, all subsequent launches will update the app. You only have to push your app to whatever GIT repo you like:

## Usage:
##Usage:
```
First you need to create a "airfield.properties" next to the .jar containing the path to the git repo:
git=https://github.com/Kaito23/airfieldFX

then you start your airfieldFX normally by:
java -jar airfield.jar 
```

## Delivery
You can deliver a zip file containing a jre, airfieldFX.jar and the airfield.properties and a release version of your java program.
The user updates automaticly to the newest version. If he has no internet connection the update process skips and the released version will start.

### Information
airfieldFX uses JavaFX Dialogs so you need JDK 8u40 or later!
[Read more at code.markery.ch](http://code.makery.ch/blog/javafx-dialogs-official/ "code.makery.ch")

## start parameter
- generateKeys [path] --> generates a new keypair to the given path or if no path is given next to the jar file
- sign [path_to_ppk] --> signs the files with the generated private key, if no path is given the key is searched next to the jar
- skipUpdate --> skips the update procedure
- updateonly --> updates the app but does not start it

## TODO
- ~~a securecheck with signing of the files in the git repo~~ DONE
- (self commit?)

## Contribute
### Build
To build airfieldFX you need my small signing library
[Latest release](https://github.com/Kaito23/panda-signer/releases/latest "Here you find the latest release of the signing library").  
Or you can add the imported project in eclipse to the airfieldFX project.

## Overview image
![Here should be an overview image. Sadly it seems like something went wrong](airfieldFX_User.png "Overview image")

