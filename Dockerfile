FROM gradle:8-jdk21-alpine AS build

COPY . /app
WORKDIR /app

RUN gradle clean installDist

FROM openjdk:21-alpine

RUN addgroup -S ktor && adduser -S ktor -G ktor

COPY --from=build /app/build/install/url-shortener /app

USER ktor

EXPOSE 8080

CMD ["/app/bin/url-shortener"]