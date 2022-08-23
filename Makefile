all:
	javac instruction/*.java components/*.java *.java 
	jar cvfe runtime.jar Runtime instruction/*.class components/*.class *.class
	rm -rf instruction/*.class components/*.class *.class