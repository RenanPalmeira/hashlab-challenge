package logic

import (
	"hashlab-challenge/discount-service/util"
	"time"
)

// business logic to apply discount or not
// if is black friday return 10
// if not is black friday and is birth day of user return 5
// else return zero
func ApplyDiscount(date time.Time, userDateOfBirth time.Time) float32 {

	// is black friday
	if date.Day() == 25 && date.Month() == 11 {
		return 10
	}

	// erase times of date and userDateOfBirth to compare just year-month-day
	if util.WithTimeAtStartOfDay(userDateOfBirth).Equal(util.WithTimeAtStartOfDay(date)) {
		return 5
	}

	// no discount
	return 0
}
