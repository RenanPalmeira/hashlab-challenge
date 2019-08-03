package logic

import (
	"testing"
	"time"
)

func TestApplyDiscountInBlackFriday(t *testing.T) {

	blackFriday := time.Date(2019, 11, 25, 0, 15, 30, 0, time.UTC)

	discount := ApplyDiscount(blackFriday, time.Now())
	if discount != 10 {
		t.Errorf("Discount was incorrect, got: %f, want: %d.", discount, 10)
	}
}

func TestApplyDiscountIsUserBirthDay(t *testing.T) {
	discount := ApplyDiscount(time.Now(), time.Now())
	if discount != 5 {
		t.Errorf("Discount was incorrect, got: %f, want: %d.", discount, 5)
	}
}


func TestApplyDiscountIsUserBirthDayAndBlackFriday(t *testing.T) {

	blackFriday := time.Date(2019, 11, 25, 0, 15, 30, 0, time.UTC)

	discount := ApplyDiscount(blackFriday, blackFriday)
	if discount != 10 {
		t.Errorf("Discount was incorrect, got: %f, want: %d.", discount, 10)
	}
}
