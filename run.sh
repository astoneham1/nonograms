#!/bin/bash

javac -cp lib/javax.json-1.0.jar *.java
java -cp lib/javax.json-1.0.jar:. App