Configure\
`mvn azure-webapp:config`

Deploy\
`mvn clean compile package azure-webapp:deploy`

Docker\
build the image\
`docker build -t subsurfer/backend-kacper-erik-pedro .`
push the image\
`docker push subsurfer/backend-kacper-erik-pedro`
