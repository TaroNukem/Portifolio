FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# 1) Baixa as dependências numa camada separada (fica em cache enquanto o pom.xml não mudar)
COPY pom.xml .
RUN mvn -B dependency:go-offline \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.http.retryHandler.requestSentEnabled=true

# 2) Compila o código
COPY src ./src
RUN mvn -B clean package -DskipTests \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.http.retryHandler.requestSentEnabled=true

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]