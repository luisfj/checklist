FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /home/projects/checklist
VOLUME /home/projects/checklist
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install
CMD [ "tail", "-f", "/dev/null" ]
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#FROM eclipse-temurin:17-jdk-alpine
#VOLUME /home/projects/previsaotempo
#ARG DEPENDENCY=/home/projects/previsaotempo/target/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-cp","app:app/lib/*","dev.luisjohann.previsaotempo.PrevisaotempoApplication"]
# ["java","-jar","./target/app.jar"]