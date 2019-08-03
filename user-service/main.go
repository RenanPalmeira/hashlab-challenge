package main

import (
	"context"
	"fmt"
	"github.com/crgimenes/goconfig"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	pb "hashlab-challenge/proto"
	. "hashlab-challenge/user-service/database"
	. "hashlab-challenge/user-service/model"
	"log"
	"net"
)

type HashlabConfiguration struct {
	HashlabUserServicePort string `cfg:"HASHLAB_USER_SERVICE_PORT" cfgDefault:":50052" cfgRequired:"true"`
}

// a type to implement proto.UserServiceServer
type server struct{}

// apply body to proto.UserServiceServer.GetUser
func (s *server) GetUser(ctx context.Context, in *pb.UserRequest) (*pb.UserResponse, error) {
	log.Printf("Received: %v", in.UserId)

	db, err := SetupDB()

	if err != nil {
		log.Print("database disconnect")
		return nil, status.Error(codes.Internal, "database disconnect")
	}

	// create a value into which the result can be decoded
	var u User

	err = db.Collection("user").FindOne(context.TODO(), bson.D{primitive.E{"_id", in.UserId}}).Decode(&u)

	if err != nil {
		log.Printf("id %v was not found", in.UserId)
		return nil, status.Error(codes.NotFound, "id was not found")
	}

	log.Printf("Found a single document: %+v\n", u)

	return &pb.UserResponse{User: &pb.User{
		Id:          u.Id,
		FirstName:   u.FirstName,
		LastName:    u.LastName,
		DateOfBirth: u.DateOfBirth.Unix(),
	}}, nil
}

func main() {

	// get configurations from env or defaults
	config := HashlabConfiguration{}

	err := goconfig.Parse(&config)
	if err != nil {
		fmt.Println(err)
		return
	}

	log.Printf("Creating your user-service on port %v ...", config.HashlabUserServicePort)

	// create a listen
	lis, err := net.Listen("tcp", config.HashlabUserServicePort)
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
