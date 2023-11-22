Configure\
`mvn azure-webapp:config`

Deploy\
`mvn clean compile package azure-webapp:deploy`

Docker\
build the image\
`docker build -t subsurfer/backend-kacper-erik-pedro .`
push the image\
`docker push subsurfer/backend-kacper-erik-pedro`
run locally with env variables\
`docker run --rm -e DB_CONNECTION_URL='xxx' -e DB_NAME='xxx' -e DB_KEY='xxx' -p 8080:8080 subsurfer/backend-kacper-erik-pedro`
