JavaMatePatch
=============

Simple patch java classes to control WM name


Aimed to resolve bug : java swing apps incorrectly working in full screen on mate desktop

global menu open and immediately close on mouse click

it happens because of java can't detect Window Manager name.

java asks name of root x window and receive nothing.

My patch just instrument one method in jdk class XNETProtocol - forces it to return custom Window Manager name instead of asking xwindow name

This patch use javassist to instrument class, so dont forget copy javassist lib from 
build to the same directory as JavaMatePatch-1.0.0-SNAPSHOT.jar

To run any application in mate just add

-javaagent:JavaMatePatch-1.0.0-SNAPSHOT.jar=Metacity

to java run string

parameter to  javaagent - is name of WM. 

Mate desktop uses Metacity.

For example :

soapui

add line to soapui.sh

JAVA_OPTS="$JAVA_OPTS -javaagent:/<path>/JavaMatePatch-1.0.0-SNAPSHOT.jar=Metacity"

oracle sqldeveloper

add line to jdk.conf

AddVMOption -javaagent:/<path>/JavaMatePatch-1.0.0-SNAPSHOT.jar=Metacity

