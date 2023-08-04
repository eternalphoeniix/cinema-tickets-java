package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */
public class TicketTypeRequest {
    //final ensures object is now immutable.
    private final int noOfTickets;
    private final Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    @Override
    public String toString() {
        return String.format("TYPE: %s , TICKETS: %s", type, noOfTickets);
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT(20, 1),
        CHILD(10, 1),
        INFANT(0, 0);
        private final int seatsPerTicket;
        private final int ticketPrice;

        Type(int ticketPrice, int seatsRequired) {
            this.ticketPrice = ticketPrice;
            this.seatsPerTicket = seatsRequired;
        }

        public int getCost() {
            return ticketPrice;
        }

        public int getSeatsPerTicket() {
            return seatsPerTicket;
        }
    }
}
