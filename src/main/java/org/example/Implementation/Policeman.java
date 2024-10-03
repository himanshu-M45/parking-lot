package org.example.Implementation;

import org.example.Interface.Notifiable;

import java.util.HashMap;
import java.util.Map;

public class Policeman implements Notifiable {
    private final Map<String, Boolean> parkingLotStatus = new HashMap<>();

    @Override
    public void updateAvailableStatus(String parkingLotId) {
//        for (Map.Entry<String, Boolean> entry : parkingLotStatus.entrySet()) {
//            if (entry.getKey().equals(parkingLotId)) {
//                // Assuming you have a method to check if the parking lot is full
//                boolean isFull = checkIfParkingLotIsFull(parkingLotId);
//                parkingLotStatus.put(parkingLotId, isFull);
//            }
//        }
    }
}
