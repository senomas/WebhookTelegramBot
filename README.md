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
   -v maven-data:/root/.m2/repository/
   -e GIT=https://github.com/senomas/WebhookTelegramBot.git \
   -e PRJ=WebhookTelegramBot \
   -p 10.37.1.2:8082:8080 \
   senomas/maven-springboot

docker logs -f telebot
```

## TEST POST
```
curl -v -X POST -H "Content-Type: application/json" -d @payload.json 10.37.1.2:8082/api/v1.0/webhook/gogs/demo/

curl -v -X POST -H "Content-Type: application/json" -d @payload2.json 10.37.1.2:8082/api/v1.0/webhook/transmission/torrent/
```

## TEST TRANSMISSION
```
curl -v -X POST -H "Content-Type: application/json" -d '{"TR_APP_VERSION": "${TR_APP_VERSION}", "TR_TIME_LOCALTIME": "${TR_TIME_LOCALTIME}", "TR_TORRENT_DIR": "$TR_TORRENT_DIR", "TR_TORRENT_HASH": "$TR_TORRENT_HASH", "TR_TORRENT_ID": "$TR_TORRENT_ID", "TR_TORRENT_NAME" : "$TR_TORRENT_NAME"}' 10.37.1.2:8082/api/v1.0/webhook/transmission/tv/
```

```
#!/bin/bash
/usr/bin/curl -v -X POST -H "Content-Type: application/json" -d '{"TR_APP_VERSION": "'"$TR_APP_VERSION"'", "TR_TIME_LOCALTIME": "'"$TR_TIME_LOCALTIME"'", "TR_TORRENT_DIR": "'"$TR_TORRENT_DIR"'", "TR_TORRENT_HASH": "'"$TR_TORRENT_HASH"'", "TR_TORRENT_ID": "'"$TR_TORRENT_ID"'", "TR_TORRENT_NAME" : "'"$TR_TORRENT_NAME"'"}' 10.37.1.2:8082/api/v1.0/webhook/dump/tv/
```

```
#!/bin/bash
/usr/bin/curl -v -X POST -H "Content-Type: application/json" -d '{"TR_APP_VERSION": "'"$TR_APP_VERSION"'", "TR_TIME_LOCALTIME": "'"$TR_TIME_LOCALTIME"'", "TR_TORRENT_DIR": "'"$TR_TORRENT_DIR"'", "TR_TORRENT_HASH": "'"$TR_TORRENT_HASH"'", "TR_TORRENT_ID": "'"$TR_TORRENT_ID"'", "TR_TORRENT_NAME" : "'"$TR_TORRENT_NAME"'"}' webhook:8080/api/v1.0/webhook/dump/tv/
```

## RUN ON SERVER
```
docker run -d --name webhookdb --net=docknet \
   -e MYSQL_ROOT_PASSWORD=dodol123 \
   -e MYSQL_DATABASE=gogs \
   -e MYSQL_USER=gogs \
   -e MYSQL_PASSWORD=dodol123 \
   mysql


docker rm -f webhook

docker run -d --name webhook --net=docknet \
   -e GIT=https://deploy:dodolduren123@code.senomas.com/seno/gogsbot.git \
   -e PRJ=gogsbot/WebhookTelegramBot \
   -p 10.37.1.2:8082:8080 \
   senomas/maven-springboot

docker logs -f webhook
```

## HOOK
http://webhook:8080/api/v1.0/webhook/gogs/foo/
