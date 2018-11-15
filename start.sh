#!/bin/bash

printf 'Building the project ....'
printf '\n-'
printf '\n-'
mvn clean install
printf '\n-'
printf '\n-'
docker rm --force imguruploader-web:latest
printf '\n-'
printf '\n-'
printf '\nBuilding docker image ....'
printf '\n-'
printf '\n-'
docker build -t imguruploader-web:latest .
printf '\n-'
printf '\n-'
printf '\nRunning docker image ....'
docker run  -d -p 8080:8080 imguruploader-web
printf '\n-'
printf '\n-'
printf '\nwaiting for application .'
until $(curl --output /dev/null --silent --head --fail http://localhost:8080/v1/images); do
    printf '.'
    sleep 5
done
printf '\nApplication started successfully!'
printf '\n-'
printf '\n-'
exit 0