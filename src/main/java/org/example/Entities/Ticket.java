package org.example.Entities;

import java.util.Objects;
import java.util.UUID;

public class Ticket {
    private final String ticketId;

    public Ticket() {
        this.ticketId = UUID.randomUUID().toString();
    }

    public boolean validateTicket(Ticket ticket) {
        return Objects.equals(this.ticketId, ticket.ticketId);
    }
}
