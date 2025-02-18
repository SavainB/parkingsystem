package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    final int STILLFREE = 30; // temp avant que ça devienne payant
    private TicketDAO ticketDAO = new TicketDAO();

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        long duration = outHour - inHour;

        // Convertir la durée en heures arrondies à l'entier supérieur
        int durationMinutes = (int) Math.ceil(duration / (60.0 * 1000.0));


        double tauxClient;
        if (ticket.isClient()) {
            System.out.println("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
            tauxClient = Fare.REDUCTION;
        } else {
            tauxClient = 1;
        }
        if (durationMinutes < STILLFREE) {
            ticket.setPrice(Fare.RATE_UNDER_THIRTY);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    double carPrice = (durationMinutes * (Fare.CAR_RATE_PER_HOUR / 60)) * tauxClient;
                    ticket.setPrice(Math.round(carPrice * 100.0) / 100.0);
                    break;
                }
                case BIKE: {
                    double bikePrice = (durationMinutes * (Fare.BIKE_RATE_PER_HOUR / 60)) * tauxClient;
                    ticket.setPrice(Math.round(bikePrice * 100.0) / 100.0);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }

        }
    }

}