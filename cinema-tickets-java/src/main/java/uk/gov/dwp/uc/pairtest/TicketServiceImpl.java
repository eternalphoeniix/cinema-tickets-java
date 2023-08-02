package uk.gov.dwp.uc.pairtest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    private static Logger logger = LogManager.getLogger(TicketServiceImpl.class.getName());
//        private static Logger logger = LogManager.getLogger("Application");
//    private static Logger logger = LogManager.getRootLogger();

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        //worth splitting into a separate method
        //need to do a try catch in case of string input
        if (accountId > 0) {
            logger.debug("Account ID greater than 0");
            logger.info("Account ID greater than 0");
            logger.trace("Account ID greater than 0");
        } else {
            logger.error("Account ID less than 0");
            throw new InvalidPurchaseException("Account ID Invalid: " + accountId);
        }
        //need to validate ticket request is valid - max of 20 tickets, 1 child per adult. Clarify infant not buying, still need a ticket.
        //to account for infants having a ticket but not taking up a seat?
        //send request to payment service
        //send request to seating service.
        //reject any invalid requests.
    }
}
