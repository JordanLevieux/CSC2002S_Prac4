BIN=./bin
SRC=./src

#General build rule
.SUFFIXES: .java .class

${BIN}/%.class: ${SRC}/%.java
	javac $< -cp ${BIN} -d ${BIN}
	
${BIN}/WordApp.class: ${BIN}/WordPanel.class

${BIN}/WordApp.class: ${BIN}/Score.class

${BIN}/WordPanel.class: ${BIN}/WordRecord.class

${BIN}/WordRecord.class: ${BIN}/WordDictionary.class



clean:
	rm -f ${BIN}/*.class
	
run:
	java -cp ./bin WordApp 0 4 "bin/example_dict.txt"
