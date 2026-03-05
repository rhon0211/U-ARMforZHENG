FROM amazoncorretto:15.0.2-alpine AS builder
WORKDIR /build
RUN apk add --no-cache curl tar bash && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz | tar xzf - -C /usr/share && \
    mv /usr/share/apache-maven-3.8.8 /usr/share/maven && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:15.0.2-alpine
WORKDIR /opt/talent-management-system-api
COPY --from=builder /build/target/*.jar /opt/talent-management-system-api/talentMananageSystem-api.jar
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "talentMananageSystem-api.jar", "--server.port=8092"]