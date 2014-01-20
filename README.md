
# Using the pre-built version

The built version is only 28KB so it is included in a dist/ folder.
It can be run directly with:

    java -cp jnativehook-local/org/jnativehook/JNativeHook/1.1.4/JNativeHook-1.1.4.jar:dist/KeystrokeOSD-latest.jar com.heeere.osd.keystrokeosd.MagicOSD

# Building and testing

Building should work out of the box with maven:

    mvn install

The live key viewer can be run with:

    java -cp jnativehook-local/org/jnativehook/JNativeHook/1.1.4/JNativeHook-1.1.4.jar:target/KeystrokeOSD-1.0-SNAPSHOT.jar com.heeere.osd.keystrokeosd.MagicOSD

Use your window manager or Ctrl+C it to close it.



# How the "local" maven repository was done

Following https://devcenter.heroku.com/articles/local-maven-dependencies

    unzip ~/dl/JNativeHook-1.1.4.zip
    mkdir jnativehook-local
    mvn deploy:deploy-file -Durl=file://$(pwd)/jnativehook-local/ -Dfile=./JNativeHook/jar/JNativeHook.jar -DgroupId=org.jnativehook -DartifactId=JNativeHook -Dpackaging=jar -Dversion=1.1.4


<style type="text/css">
    pre { border: 1px solid black; background: #AFA; padding: 1em; }
</style>
