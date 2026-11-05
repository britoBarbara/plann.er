package br.com.barbara.planner.exception;

public class InvalidTripDatesException extends RuntimeException {
    public InvalidTripDatesException(String message) {
        super(message);
    }
}
