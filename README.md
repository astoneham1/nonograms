# NONOGRAMS GAME
##### GROUP 05

To compile, run the following bash command:

`./run.sh`

You may need to allow for execute permissions, do this by running:
`chmod +x run.sh`

This should open a puzzle selector for the puzzles in the Jsons/ directory
Select a puzzle, then either load a previously saved grid or make a new one, and begin playing!

To compile and run the testing classes:
`javac -d ./out -cp lib/javax.json-1.0.jar src/*.java`
`java -cp lib/javax.json-1.0.jar:out TestChecker`
`java -cp lib/javax.json-1.0.jar:out TestMain`
`java -cp lib/javax.json-1.0.jar:out TestModel`
`java -cp lib/javax.json-1.0.jar:out TestParser`