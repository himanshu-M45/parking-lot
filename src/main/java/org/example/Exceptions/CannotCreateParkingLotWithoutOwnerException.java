package org.example.Exceptions;

public class CannotCreateParkingLotWithoutOwnerException extends RuntimeException {
    public CannotCreateParkingLotWithoutOwnerException(String message) {
        super(message);
    }
}
