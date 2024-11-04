# Użyj oficjalnej wersji JDK jako obrazu bazowego
FROM openjdk:17-jdk-slim
WORKDIR /app

# Skopiuj plik JAR z katalogu `build/libs` (po zbudowaniu przez Gradle) do kontenera
COPY build/libs/gastromate-0.0.1-SNAPSHOT.jar /app.jar
ENV JAVA_TOOL_OPTIONS "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

# Ustaw punkt wejścia dla aplikacji, aby uruchamiała plik JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
