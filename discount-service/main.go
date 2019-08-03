package main

import (
	"context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"hashlab-challenge/discount-service/util"
	"log"
	"net"
	"time"

	"google.golang.org/grpc"
	"hashlab-challenge/discount-service/logic"
	pb "hashlab-challenge/proto"
)

var (
	port = ":" + util.GetEnv("HASHLAB_DISCOUNT_SERVICE_PORT", "50051")
	userService = util.GetEnv("HASHLAB_USER_SERVICE_URI", "localhost:50052")
)

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
type server struct {}

// apply body to proto.DiscountServiceServer.GetDiscount
func (s *server) GetDiscount(ctx context.Context, in *pb.DiscountRequest) (*pb.DiscountResponse, error) {
	log.Printf("Received: %v", in)

	// Set up a connection to the server.
	conn, err := grpc.Dial(userService, grpc.WithInsecure())

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

	userDateOfBirth := time.Unix(userResponse.User.DateOfBirth, 0).UTC()

	discount := logic.ApplyDiscount(today, userDateOfBirth)

	if discount == 0 {
		return nil, status.Error(codes.NotFound, "no discount to apply")
	}

	return &pb.DiscountResponse{Discount: &pb.Discount{Prc: discount}}, nil
}

func main() {

	log.Print("Creating your discount-service...")

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

