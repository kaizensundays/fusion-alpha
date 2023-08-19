@echo off

set IGNITE_UPDATE_NOTIFIER=false

set IGNITE_WORK_DIR=%cd%\ignite\node0

echo "IGNITE_WORK_DIR=%IGNITE_WORK_DIR%"


start "Node" java -Xmx256m ^
	-Dlogging.config=log4j2-0.xml ^
	-Dlog4j.shutdownHookEnabled=false ^
	-Dserver.port=7701 ^
        -Dspring.profiles.active=dev ^
	-Djava.net.preferIPv4Stack=true ^
	-Dreactor.netty.ioWorkerCount=4 ^
	-Dreactor.schedulers.defaultPoolSize=4 ^
        -Dcluster.votes=2 ^
        -Dcluster.quorum=2 ^
	-jar service.jar
