

Following https://devcenter.heroku.com/articles/local-maven-dependencies

    unzip ~/dl/JNativeHook-1.1.4.zip
    mkdir jnativehook-local
    mvn deploy:deploy-file -Durl=file://$(pwd)/jnativehook-local/ -Dfile=./JNativeHook/jar/JNativeHook.jar -DgroupId=org.jnativehook -DartifactId=JNativeHook -Dpackaging=jar -Dversion=1.1.4
    
    
