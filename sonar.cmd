
set JAVA_HOME=%JAVA_11_HOME%

mvn verify sonar:sonar %SONAR_OPTS_FUSION_ALPHA% -Dsonar.branch.name=dev1 -P sonar
