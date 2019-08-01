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

type server struct {}

func (s *server) GetDiscount(ctx context.Context, in *pb.DiscountRequest) (*pb.DiscountResponse, error) {
	log.Printf("Received: %v", in)


	// Set up a connection to the server.
	conn, err := grpc.Dial(userService, grpc.WithInsecure())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()
	discountServiceClient := pb.NewUserServiceClient(conn)

	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()

	r, err := discountServiceClient.GetUser(ctx, &pb.UserRequest{UserId: in.UserId})
	if err != nil {
		log.Fatalf("could not greet: %v", err)
	}
	log.Printf("Greeting: %s", r.User.DateOfBirth)

	return &pb.DiscountResponse{Discount: &pb.Discount{ValueInCents: 1}}, nil
}

func main() {

	lis, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	s := grpc.NewServer()
	pb.RegisterDiscountServiceServer(s, &server{})
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
