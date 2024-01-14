
start "Eureka" %JAVA_17_HOME%/bin/java -Xmx256m ^
	-Ddebug=false ^
	-Dlogging.config=log4j2.xml ^
	-Dlog4j.shutdownHookEnabled=false ^
	-Dserver.port=7777 ^
	--add-opens=java.base/sun.nio.ch=ALL-UNNAMED ^
	-jar service.jar
