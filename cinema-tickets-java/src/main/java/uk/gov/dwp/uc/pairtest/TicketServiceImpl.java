package uk.gov.dwp.uc.pairtest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    private static final Logger LOGGER = LogManager.getLogger(TicketServiceImpl.class.getName());
    private final int MAXREQUESTEDTICKETS = 20;
    private final int MINACCOUNTID = 1;
    private final int MINREQUESTEDTICKETS = 1;

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        LOGGER.debug("Purchasing Ticket(s)");
        try {
            if (isRequestValid(accountId, ticketTypeRequests)) {
            }
            else {
                throw new InvalidPurchaseException();
            }
        } catch (Exception e) {
            throw new InvalidPurchaseException(e.getMessage(), e);
        }
        //need to validate ticket request is valid - max of 20 tickets,
        // 1 child per adult. Clarify infant not buying, still need a ticket.
        //to account for infants having a ticket but not taking up a seat?
        //send request to payment service
        //send request to seating service.
        //reject any invalid requests.
    }

    private boolean isRequestValid(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        LOGGER.debug("Checking Ticket Purchase request is valid");
        //could return this for dot chaining.
        return isAccountIdValid(accountId)
                && isTicketCountWithinLimit(ticketTypeRequests)
                && isAdultAvailableForInfant(ticketTypeRequests);
    }

    private boolean isAccountIdValid(long accountId) {
        LOGGER.debug("Checking Account ID is valid: " + accountId);
//        try {
        if (accountId >= MINACCOUNTID) {
            LOGGER.debug("Account ID is valid");
            return true;
        }
        else {
            String message = String.format("Account ID %s is less than the system minimum: %s", accountId, MINACCOUNTID);
            throw new InvalidPurchaseException(message);
        }
    }

    private boolean isTicketCountWithinLimit(TicketTypeRequest... requests) {
        LOGGER.debug("Checking number of tickets does not exceed maximum");
        try {
            String logMessage;
            int numberOfTickets = 0;
            for (TicketTypeRequest request : requests) {
                numberOfTickets += request.getNoOfTickets();
            }
            if (numberOfTickets <= MAXREQUESTEDTICKETS && numberOfTickets >= MINREQUESTEDTICKETS) {
                logMessage = String.format("Number of tickets %s is within the maximum limit of %s", numberOfTickets, MAXREQUESTEDTICKETS);
                LOGGER.debug(logMessage);
                return true;
            }
            else {
                logMessage = String.format("Number of tickets %s is outside the minimum limit of %s or maximum limit of %s", numberOfTickets, MINREQUESTEDTICKETS, MAXREQUESTEDTICKETS);
                LOGGER.debug(logMessage);
                return false;
            }
        } catch (Exception e) {
            String message = "Ticket Type Request is invalid";
            throw new InvalidPurchaseException(message);
        }
    }

    private boolean isAdultAvailableForInfant(TicketTypeRequest... requests) {
        LOGGER.debug("Checking there is an adult available for each Infant");
        try {
            int numberOfAdults = 0;
            int numberOfInfants = 0;
            for (TicketTypeRequest request : requests
            ) {
                switch (request.getTicketType()) {
                    case ADULT -> numberOfAdults += request.getNoOfTickets();
                    case INFANT -> numberOfInfants += request.getNoOfTickets();
                    case CHILD -> {/*do nothing*/}
                    default -> {
                        String message = String.format("Invalid Ticket Type Used: %s", request.getTicketType());
                        throw new InvalidPurchaseException(message);
                    }
                }
            }
            return numberOfAdults >= numberOfInfants;
        } catch (Exception e) {
            String message = "Unable to validate adult available for infant";
            throw new InvalidPurchaseException(message, e);
        }
    }
}
