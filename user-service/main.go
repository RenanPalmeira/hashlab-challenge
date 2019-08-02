package main

import (
	"context"
	"log"
	"net"
	"time"

	"google.golang.org/grpc"
	pb "hashlab-challenge/proto"
)

const (
	port = ":50052"
)

// a type to implement proto.UserServiceServer
type server struct {}

// apply body to proto.UserServiceServer.GetUser
func (s *server) GetUser(ctx context.Context, in *pb.UserRequest) (*pb.UserResponse, error) {
	log.Printf("Received: %v", in.UserId)
	return &pb.UserResponse{User: &pb.User{
		Id:                   "1",
		FirstName:            "Renan",
		LastName:             "Palmentos",
		DateOfBirth:          time.Now().Unix(),
	}}, nil
}

func main() {

	// create a listen
	lis, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	// start server
	s := grpc.NewServer()
	pb.RegisterUserServiceServer(s, &server{})
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
