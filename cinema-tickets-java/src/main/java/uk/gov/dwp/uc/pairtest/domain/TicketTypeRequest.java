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
        //number of seats - infant doesn't take up a seat.
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT(20),
        CHILD(10),
        INFANT(0);
        private final int ticketPrice;

        Type(int ticketPrice) {
            this.ticketPrice = ticketPrice;
        }

        public int getCost() {
            return ticketPrice;
        }
    }
}
