FROM openjdk:11-jdk-slim as build
WORKDIR /workdir/weather-routes

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN ./gradlew clean build
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM openjdk:11-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=/workdir/weather-routes/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /weather-routes/lib
COPY --from=build ${DEPENDENCY}/META-INF /weather-routes/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /weather-routes
ENTRYPOINT ["java","-cp", "weather-routes:weather-routes/lib/*", "nosql2h20.weather.routes.Application"]
