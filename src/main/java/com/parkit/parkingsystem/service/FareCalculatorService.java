package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    final int ENCOREGRATUIT = 30; // temp avant que ça devienne payant
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration = outHour - inHour;
        System.out.println(duration);
        // Convertir la durée en heures arrondies à l'entier supérieur
        int durationMinutes = (int) Math.ceil(duration / (60.0 * 1000.0));
        int durationHours = (int) Math.ceil(duration / (60.0 * 60.0 * 1000.0));

        if (durationMinutes < ENCOREGRATUIT){
            switch (ticket.getParkingSpot().getParkingType()) {
                case BIKE:
                case CAR: {
                    ticket.setPrice(0);
                    break;
                }
                default: throw new IllegalArgumentException("Unknown Parking Type");
            }

        }else{
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    ticket.setPrice(durationHours * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationHours * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }
}