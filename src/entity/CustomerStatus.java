package entity;

//represents the current state of customer
public enum CustomerStatus {
	WAITING_IN_QUEUE,  // waiting to be seated
    SEATED,            // seated but has not ordered
    ORDERED,           // order placed, food not yet served
    SERVED,            // food served
    READY_TO_PAY,      // finished eating for one full turn, waiting for payment
    PAID,              // payment completed
    ABANDONED          // abandonment due to zero satisfaction
}
