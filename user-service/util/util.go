package util

import (
	"os"
)

// Utility functions

// Get environment variables with fallback
func GetEnv(key, fallback string) string {
	value := os.Getenv(key)
	if len(value) == 0 {
		return fallback
	}
	return value
}