# GOGS BOT

## MYSQL DOCKER
```
docker run -d --name telebotdb --net=docknet \
   -p 3316:3306 \
   -e MYSQL_ROOT_PASSWORD=dodol123 \
   -e MYSQL_DATABASE=telebot \
   -e MYSQL_USER=telebot \
   -e MYSQL_PASSWORD=dodol123 \
   mysql
```

## DOCKER
```
docker volume create --name maven-data

docker rm -f telebot

docker run -d --name telebot --net=docknet \
   -v maven-data:/root/.m2/repository/ \
   -e GIT=https://github.com/senomas/WebhookTelegramBot.git \
   -e PRJ=WebhookTelegramBot \
   -p 10.37.1.2:8082:8080 \
   senomas/maven-springboot

docker logs -f telebot
```

## TEST POST
```
curl -v -X POST -H "Content-Type: application/json" -d @payload.json 10.37.1.2:8082/api/v1.0/webhook/gogs/foo/

curl -v -X POST -H "Content-Type: application/json" -d @payload2.json 10.37.1.2:8082/api/v1.0/webhook/transmission/tv/

curl -v -X POST -H "Content-Type: application/json" -d @payload2.json 10.37.1.2:8082/api/v1.0/webhook/transmission/mov/
```

## HOOK
http://webhook:8080/api/v1.0/webhook/gogs/foo/
