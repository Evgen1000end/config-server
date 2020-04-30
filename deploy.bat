mvn clean package
docker build -t config-server:%1 .
docker image tag config-server:%1 evgen1000end/config-server:%1
docker image push evgen1000end/config-server:%1
