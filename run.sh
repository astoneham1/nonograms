#!/bin/bash

javac -d ./out -cp lib/javax.json-1.0.jar src/*.java
java -cp lib/javax.json-1.0.jar:out App