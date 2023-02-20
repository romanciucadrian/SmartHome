docker rm -f smarthome
docker rmi smarthome:latest
docker build -t smarthome:latest .
docker run --name smarthome -p 8081:8081 -d smarthome:latest
