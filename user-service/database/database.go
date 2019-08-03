package database

import (
	"context"
	"errors"
	"fmt"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"hashlab-challenge/user-service/util"
	"log"
)

type key string

const (
	hostKey     = key("hostKey")
	usernameKey = key("usernameKey")
	passwordKey = key("passwordKey")
	databaseKey = key("databaseKey")
)

func ConfigDB(ctx context.Context) (*mongo.Database, error) {
	uri := fmt.Sprintf(`mongodb://%s:%s@%s/%s?authMechanism=SCRAM-SHA-1`,
		ctx.Value(usernameKey).(string),
		ctx.Value(passwordKey).(string),
		ctx.Value(hostKey).(string),
		ctx.Value(databaseKey).(string),
	)

	client, err := mongo.NewClient(options.Client().ApplyURI(uri))
	if err != nil {
		return nil, fmt.Errorf("todo: couldn't connect to mongo: %v", err)
	}
	err = client.Connect(ctx)
	if err != nil {
		return nil, fmt.Errorf("todo: mongo client couldn't connect with background context: %v", err)
	}
	todoDB := client.Database("hashlab")
	return todoDB, nil
}

func SetupDB() (*mongo.Database, error) {
	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()
	ctx = context.WithValue(ctx, hostKey, util.GetEnv("HASHLAB_MONGODB_HOST", "localhost:27017"))
	ctx = context.WithValue(ctx, usernameKey, util.GetEnv("HASHLAB_MONGODB_USERNAME", "hashlab"))
	ctx = context.WithValue(ctx, passwordKey, util.GetEnv("HASHLAB_MONGODB_PASSWORD", "hashlab"))
	ctx = context.WithValue(ctx, databaseKey, util.GetEnv("HASHLAB_MONGODB_DATABASE", "admin"))
	db, err := ConfigDB(ctx)
	if err != nil {
		log.Printf("todo: database configuration failed: %v", err)
		return nil, errors.New("client is disconnected")
	}

	return db, nil
}