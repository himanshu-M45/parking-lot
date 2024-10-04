package org.example.Role;

import org.example.Interface.Notifiable;

import java.util.HashMap;
import java.util.Map;

public class Policeman implements Notifiable {
    private Map<String, Boolean> parkingLotStatus = new HashMap<>();

    @Override
    public void updateAvailableStatus(String parkingLotId, boolean status) {
        parkingLotStatus.put(parkingLotId, status);
    }
}
