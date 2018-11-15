# Imgur Uploader Service

## Prerequisites

- You have Maven installed.
- You have Docker installed.
- Port 8080 is free for Tomcat.

## Running

- Clone this repository.
- There is a file named 'start.sh' in the root folder. Make sure you have permission to run the script.
- Just run './start.sh'
    - The script will build the project and the docker image with the name 'imguruploader-web:latest'.
    - It will also remove existing images with above name if any.
    - And, it will run the docker container in detched mode.

Now, you should have the application running on localhost:8080