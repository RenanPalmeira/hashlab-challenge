FROM golang:1.12.7-buster

WORKDIR $GOPATH/src/hashlab-challenge

ENV PATH="/protoc/bin:${PATH}"

# install protobuf from source
RUN apt-get update && \
    apt-get -y install git unzip build-essential autoconf libtool

RUN mkdir /protoc; \
    cd /protoc; \
    wget "https://github.com/protocolbuffers/protobuf/releases/download/v3.9.0/protoc-3.9.0-linux-x86_64.zip"; \
    unzip protoc-3.9.0-linux-x86_64.zip

# Copy the source from the current directory to the Working Directory inside the container
COPY . $GOPATH/src/hashlab-challenge/user-service
COPY proto $GOPATH/src/hashlab-challenge/proto

## Install grpc
RUN go get google.golang.org/grpc
# Install protoc-gen-go
RUN go get github.com/golang/protobuf/protoc-gen-go
# Install mongo-driver
RUN go get go.mongodb.org/mongo-driver/mongo

## Compile proto files
RUN protoc --go_out=plugins=grpc:. proto/*.proto

# Build the Go app
RUN go build user-service/main.go

# Command to run the executable
CMD ["./main"]