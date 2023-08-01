package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        //worth splitting into a separate method
        if ( accountId>0){

        } else {
            throw new InvalidPurchaseException();
        }

        //need to validate ticket request is valid - max of 20 tickets, 1 child per adult. Clarify infant not buying, still need a ticket.
        //to account for infants having a ticket but not taking up a seat?
        //send request to payment service
        //send request to seating service.
        //reject any invalid requests.
    }

}
