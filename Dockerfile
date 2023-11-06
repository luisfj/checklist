FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /home/Development/Projects/previsaotempo/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /previsaotempo/app
ARG DEPENDENCY=/home/Development/Projects/previsaotempo/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","dev.luisjohann.previsaotempo.PrevisaotempoApplication"]