# airfieldFX
Self-contained Software Deployment Tool

airfieldFX is a fork from Adam Bien's airfield.
It is a self-contained auto-update utility written in Java 8. 
I just added some functions to the frontend like a JavaFX progressbar and the
option to skip the updateprocess.

Your clients can automatically distribute new versions to all connected Java clients.

At the very first start airfield will clone the remote repository, all subsequent launches will update the app. You only have to push your app to whatever GIT repo you like:

##Usage:
```
First you need to create a "airfield.properties" next to the .jar containing the path to the git repo:
git=https://github.com/Kaito23/airfieldFX

then you start your airfieldFX normally by:
java -jar airfield.jar 
```

# TODO
- a securecheck with signing of the files in the git repo

## Contribute
### Build
To build airfieldFX you need my small signing library
[Latest release](https://github.com/Kaito23/panda-signer/releases/latest "Here you find the latest release of the signing library").  
Or you can add the imported project in eclipse to the airfieldFX project.
