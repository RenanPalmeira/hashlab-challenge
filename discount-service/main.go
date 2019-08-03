package main

import (
	"context"
	"fmt"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"log"
	"net"
	"time"

	"github.com/crgimenes/goconfig"

	"google.golang.org/grpc"
	"hashlab-challenge/discount-service/logic"
	pb "hashlab-challenge/proto"
)

type HashlabConfiguration struct {
	DiscountServicePort string `cfg:"HASHLAB_DISCOUNT_SERVICE_PORT" cfgDefault:":50051" cfgRequired:"true"`
	UserServiceUri      string `cfg:"HASHLAB_USER_SERVICE_URI" cfgDefault:"localhost:50052" cfgRequired:"true"`
}

var config *HashlabConfiguration

// Get user from user-service
func GetUserResponse(conn *grpc.ClientConn, userId string) (*pb.UserResponse, error) {

	// Connection to the server.
	discountServiceClient := pb.NewUserServiceClient(conn)

	// set timeout
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()

	// Call getUser from user-service
	return discountServiceClient.GetUser(ctx, &pb.UserRequest{UserId: userId})
}

// a type to implement proto.DiscountServiceServer
type server struct{}

// apply body to proto.DiscountServiceServer.GetDiscount
func (s *server) GetDiscount(ctx context.Context, in *pb.DiscountRequest) (*pb.DiscountResponse, error) {
	log.Printf("Received: %v", in)

	// Set up a connection to the server.
	conn, err := grpc.Dial(config.UserServiceUri, grpc.WithInsecure())

	if err != nil {
		log.Printf("did not connect: %v", err)
		return nil, status.Error(codes.Internal, "did not connect")
	}
	defer conn.Close()

	// Call user response
	userResponse, err := GetUserResponse(conn, in.UserId)

	if err != nil {
		return nil, err
	}

	today := time.Now().UTC()

	// convert unix date of birth user to go time
	userDateOfBirth := time.Unix(userResponse.User.DateOfBirth, 0).UTC()

	// call business logic to apply discount or not
	discount := logic.ApplyDiscount(today, userDateOfBirth)

	if discount == 0 {
		return nil, status.Error(codes.NotFound, "no discount to apply")
	}

	return &pb.DiscountResponse{Discount: &pb.Discount{Prc: discount}}, nil
}

func main() {

	// get configurations from env or defaults
	config := HashlabConfiguration{}

	err := goconfig.Parse(&config)
	if err != nil {
		fmt.Println(err)
		return
	}

	log.Printf("Creating your discount-service on port %v ...", config.DiscountServicePort)

	// create a listen
	lis, err := net.Listen("tcp", config.DiscountServicePort)
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
