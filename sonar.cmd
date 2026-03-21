
set JAVA_HOME="%JAVA_17_HOME%"

mvn verify sonar:sonar %SONAR_OPTS_IGNITE_2_LAB% -Dsonar.branch.name=dev -P sonar
