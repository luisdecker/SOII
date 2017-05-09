#!/bin/sh
cd Software;
./gradlew build;
cd ..;
for PROJECT in SORepository SOServer; do
    cp -Rvf Software/$PROJECT/build/libs/$PROJECT.war vm/webapps/
done;


