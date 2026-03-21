set JAVA_HOME="%JAVA_21_HOME%"

set MAVEN_OPTS="-Xmx1g"

mvn clean install -P tests
