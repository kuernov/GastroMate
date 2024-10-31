# Użyj oficjalnej wersji JDK jako obrazu bazowego
FROM openjdk:17-jdk-slim

# Skopiuj plik JAR z katalogu `build/libs` (po zbudowaniu przez Gradle) do kontenera
COPY build/libs/gastromate-0.0.1-SNAPSHOT.jar /app.jar

# Ustaw punkt wejścia dla aplikacji, aby uruchamiała plik JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
