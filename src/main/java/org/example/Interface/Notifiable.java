package org.example.Interface;

import org.example.Implementation.ParkingLot;

public interface Notifiable {
    void updateAvailableStatus(String parkingLotId);
}
