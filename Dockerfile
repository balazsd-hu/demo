# 1. Alapként egy hivatalos, könnyített Java 17-es környezetet használunk
FROM eclipse-temurin:17-jre-alpine

# 2. Beállítunk egy munkakönyvtárat a konténeren belül
WORKDIR /app

# 3. Átmásoljuk az előbb legyártott JAR fájlt a konténer belsejébe, app.jar néven
COPY target/*.jar app.jar

# 4. Megmondjuk a Dockernek, hogy indításkor futtassa el a Java programunkat
ENTRYPOINT ["java", "-jar", "app.jar"]