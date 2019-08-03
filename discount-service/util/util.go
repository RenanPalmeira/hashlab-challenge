package util

import (
	"os"
	"time"
)

// Utility functions

// Erase time of a date, for example
// 2019-08-01 23:10 after pass to WithTimeAtStartOfDay 2019-08-01 00:00
// Is a utility function to compare dates
func WithTimeAtStartOfDay(t time.Time) time.Time {
	year, month, day := t.Date()
	return time.Date(year, month, day, 0, 0, 0, 0, t.Location())
}

// Get environment variables with fallback
func GetEnv(key, fallback string) string {
	value := os.Getenv(key)
	if len(value) == 0 {
		return fallback
	}
	return value
}