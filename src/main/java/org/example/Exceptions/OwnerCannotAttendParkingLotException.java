package org.example.Exceptions;

public class OwnerCannotAttendParkingLotException extends RuntimeException {
    public OwnerCannotAttendParkingLotException(String message) {
        super(message);
    }
}
