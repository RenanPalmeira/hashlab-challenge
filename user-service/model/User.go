package model

import "time"

type User struct {
	Id          string `bson:"_id"`
	FirstName   string `bson:"first_name"`
	LastName    string `bson:"last_name"`
	DateOfBirth time.Time  `bson:"birth_date"`
}

