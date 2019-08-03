#!/bin/bash

protoc --go_out=plugins=grpc:. proto/*.proto
cp -r proto product-service
cp -r proto discount-service
cp -r proto user-service