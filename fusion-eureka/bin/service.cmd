
start "Eureka" java -Xmx256m ^
	-Dlogging.config=log4j2.xml ^
	-Dlog4j.shutdownHookEnabled=false ^
	-Dserver.port=7777 ^
	-jar service.jar
