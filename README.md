
# Using the pre-built version

The built version is only 232KB (mostly jnativehooks) so it is included in a dist/ folder.
It can be run directly with:

    java -cp dist/KeystrokeOSD-2.0-SNAPSHOT-jar-with-dependencies.jar com.heeere.osd.keystrokeosd.MagicOSD 

# Building and testing

Building should work out of the box with maven:

    mvn install

The live key viewer can then be run with:

    java -cp target/KeystrokeOSD-2.0-SNAPSHOT-jar-with-dependencies.jar com.heeere.osd.keystrokeosd.MagicOSD

Use your window manager or Ctrl+C it to close it.



# OLD: How the "local" maven repository was done

Following https://devcenter.heroku.com/articles/local-maven-dependencies

    unzip ~/dl/JNativeHook-1.1.4.zip
    mkdir jnativehook-local
    mvn deploy:deploy-file -Durl=file://$(pwd)/jnativehook-local/ -Dfile=./JNativeHook/jar/JNativeHook.jar -DgroupId=org.jnativehook -DartifactId=JNativeHook -Dpackaging=jar -Dversion=1.1.4


