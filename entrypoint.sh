#!/bin/bash

docker stop transaction-routine
./mvnw clean package
docker build -t transaction-routine:1.0.0 .
docker run -d --rm --name transaction-routine -p 8080:8080 transaction-routine:1.0.0
