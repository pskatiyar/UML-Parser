# UML-Parser
Java source code to generate UML Class Diagram from .java files.

**Steps to run the umlparser.jar file:**
* 1. Install the Graphviz on the system.
* 2. Go to Terminal-> navigate to the directory where umlparser.jar resides-> type: java –jar umlparser.jar <source> <destination>
For example:
java -jar /Users/ABC/Desktop/umlparser.jar /Users/ABC/Desktop/JavaFiles /Users/ABC/Desktop/ClassDiag.png


**Steps to run the source code on Eclipse IDE:**
*1. Go to File-> Import-> Existing Projects into Workspace.
*2. Browse the Source code path. Source contains the required jars:
javaparser-core-2.2.3-SNAPSHOT.jar and plantuml.jar.
*3. Install the Graphviz on the system.
*4. Build the source code. Then go to Run-> Run Configurations-> click on Arguments tab.
*5. In Program arguments, enter the arguments, for example: "/Users/ABC/Desktop/JavaFiles" "/Users/ABC/Desktop/ClassDiag.png"
*6. Click on Apply and Run.
