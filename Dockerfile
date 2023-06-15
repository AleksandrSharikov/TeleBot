FROM openjdk:19-oracle
ARG JAR_FILE=target/*.jar
LABEL authors="Aleksandr.N.Sharikov@gmail.com"
RUN mkdir /opt/bot
COPY ${JAR_FILE} /opt/bot/bot_app.jar
ENTRYPOINT ["java","-jar","/opt/bot/bot_app.jar"]