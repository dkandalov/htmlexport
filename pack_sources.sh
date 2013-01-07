#!/bin/bash

rm htmlexport_src.zip
rm -Rf htmlexport_src
mkdir htmlexport_src

cp -R META-INF htmlexport_src
cp -R src htmlexport_src
cp -R test htmlexport_src
cp * htmlexport_src

find htmlexport_src -name .svn | xargs rm -Rf
rm htmlexport_src/htmlexport.jar
rm htmlexport_src/notes.txt

zip -r htmlexport_src htmlexport_src
rm -Rf htmlexport_src