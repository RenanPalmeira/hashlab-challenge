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
	port = ":50051"
	userService = "localhost:50052"
)

// business logic to apply discount or not
// if is black friday return 10
// if not is black friday and is birth day of user return 5
// else return zero
func ApplyDiscount(date time.Time, userDateOfBirth int64) float32 {

	if date.Day() == 25 && date.Month() == 11 {
		return 10
	}

	if WithTimeAtStartOfDay(time.Unix(userDateOfBirth, 0)).Equal(today) {
		return 5
	}

	return 0
}

// Get user from user-service
func GetUserResponse(ctx context.Context, userId int32) (*pb.UserResponse, error) {
	// Set up a channel to connection
	conn, _ := CreateChannel(userService)

	// Connection to the server.
	discountServiceClient := pb.NewUserServiceClient(conn)

	//ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	//defer cancel()

	// Call getUser from user-service
	return discountServiceClient.GetUser(ctx, &pb.UserRequest{UserId: userId})
}

// a type to implement proto.DiscountServiceServer
type server struct {}

// apply body to proto.DiscountServiceServer.GetDiscount
func (s *server) GetDiscount(ctx context.Context, in *pb.DiscountRequest) (*pb.DiscountResponse, error) {
	log.Printf("Received: %v", in)

	userResponse, _ := GetUserResponse(ctx, in.UserId)

	today := WithTimeAtStartOfDay(time.Now())

	return &pb.DiscountResponse{Discount: &pb.Discount{Prc: ApplyDiscount(today, userResponse.User.DateOfBirth)}}, nil
}

func main() {

	// create a listen
	lis, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	// start server
	s := grpc.NewServer()
	pb.RegisterDiscountServiceServer(s, &server{})
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}

// Utility functions

// Erase time of a date, for example
// 2019-08-01 23:10 after pass to WithTimeAtStartOfDay 2019-08-01 00:00
// Is a utility function to compare dates
func WithTimeAtStartOfDay(t time.Time) time.Time {
	year, month, day := t.Date()
	return time.Date(year, month, day, 0, 0, 0, 0, t.Location())
}

// Utility to create a channel
func CreateChannel(target string) (*grpc.ClientConn, error) {
	conn, err := grpc.Dial(target, grpc.WithInsecure())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	return conn, err
}
