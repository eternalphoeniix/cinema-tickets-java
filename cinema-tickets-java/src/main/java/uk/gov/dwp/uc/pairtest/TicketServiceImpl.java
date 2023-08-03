package uk.gov.dwp.uc.pairtest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
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
        LOGGER.debug("Processing Ticket(s)");
        try {
            if (isRequestValid(accountId, ticketTypeRequests)) {
                int totalAmountToPay = calculateCost(ticketTypeRequests);
                submitPurchase(accountId, totalAmountToPay);
                int totalSeatsToAllocate = 5;
                reserveSeats(accountId, totalSeatsToAllocate);
            }
            else {
                throw new InvalidPurchaseException();
            }
        } catch (Exception e) {
            throw new InvalidPurchaseException(e.getMessage(), e);
        }
    }

    private void submitPurchase(Long accountId, int totalAmountToPay) {
        LOGGER.debug(String.format("Making Payment of £%s", totalAmountToPay));
        TicketPaymentServiceImpl ticketServiceImpl = new TicketPaymentServiceImpl();
        ticketServiceImpl.makePayment(accountId, totalAmountToPay);
    }

    private void reserveSeats(Long accountId, int totalSeatsToAllocate) {
        LOGGER.debug(String.format("Reserving %s Seats", totalSeatsToAllocate));
        SeatReservationServiceImpl seatReservationServiceImpl = new SeatReservationServiceImpl();
        seatReservationServiceImpl.reserveSeat(accountId, totalSeatsToAllocate);
    }

    private boolean isRequestValid(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        LOGGER.debug("Checking Ticket Purchase request is valid");
        //could return this for dot chaining.
        return isAccountIdValid(accountId)
                && isTicketCountWithinLimit(ticketTypeRequests)
                && isAdultLapAvailableForInfant(ticketTypeRequests)
                && isAdultToMinorValid(ticketTypeRequests);
    }

    private boolean isAccountIdValid(Long accountId) {
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
                if (request.getNoOfTickets() <= MAXREQUESTEDTICKETS && request.getNoOfTickets() >= MINREQUESTEDTICKETS) {
                    numberOfTickets += request.getNoOfTickets();
                }
                else {
                    throw new InvalidPurchaseException(
                            String.format("Individual TicketTypeRequest has an invalid number of tickets: %s, %s", request, numberOfTickets)
                    );
                }
            }
            if (numberOfTickets <= MAXREQUESTEDTICKETS && numberOfTickets >= MINREQUESTEDTICKETS) {
                logMessage = String.format("Number of tickets %s is within the maximum limit of %s", numberOfTickets, MAXREQUESTEDTICKETS);
                LOGGER.debug(logMessage);
                return true;
            }
            else {
                throw new InvalidPurchaseException(
                        String.format("Number of tickets %s is outside the minimum limit of %s or maximum limit of %s", numberOfTickets, MINREQUESTEDTICKETS, MAXREQUESTEDTICKETS)
                );
            }
        } catch (Exception e) {
            String message = "Ticket Type Request is invalid";
            throw new InvalidPurchaseException(message);
        }
    }

    private boolean isAdultLapAvailableForInfant(TicketTypeRequest... requests) {
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
                        throw new InvalidPurchaseException(
                                String.format("Invalid Ticket Type Used: %s", request.getTicketType())
                        );
                    }
                }
            }
            if (numberOfAdults >= numberOfInfants) {
                LOGGER.debug(
                        String.format("There is at least one adult per infant. Adults: %s, infants: %s", numberOfAdults, numberOfInfants)
                );
                return true;
            }
            else {
                LOGGER.debug(
                        String.format("There are fewer adults than infants. Adults: %s, infants: %s", numberOfAdults, numberOfInfants)
                );
                return false;
            }
        } catch (Exception e) {
            throw new InvalidPurchaseException("Unable to validate adult available for infant", e);
        }
    }

    private boolean isAdultToMinorValid(TicketTypeRequest... requests) {
        LOGGER.debug("Checking there is an adult available if there are any infants or children");
        try {
            boolean isAdultRequired = false;
            boolean isAdultPresent = false;
            for (TicketTypeRequest request : requests
            ) {
                switch (request.getTicketType()) {
                    case ADULT -> isAdultPresent = true;
                    case INFANT, CHILD -> isAdultRequired = true;
                    default -> {
                        throw new InvalidPurchaseException(
                                String.format("Invalid Ticket Type Used: %s", request.getTicketType())
                        );
                    }
                }
            }
            if (isAdultRequired) {
                LOGGER.debug("Adult is required as children or infants are present");
                if (isAdultPresent) {
                    LOGGER.debug("Adult is Present as children or infants are present");
                    return true;
                }
                else {
                    throw new InvalidPurchaseException("Adult not present. Is required in presence of children or infants");
                }
            }
            else {
                LOGGER.debug("No Adult is required as there are no children or infants  present");
                return true;
            }
        } catch (Exception e) {
            throw new InvalidPurchaseException("Unable to validate if adult is required", e);
        }
    }

    private int calculateCost(TicketTypeRequest... requests) {
        LOGGER.debug("Calculating Total Cost");
        try {
            int total = 0;
            for (TicketTypeRequest request : requests
            ) {
                total += request.getNoOfTickets() * request.getTicketType().getCost();
            }
            return total;
        } catch (Exception e) {
            throw new InvalidPurchaseException("Unable to Calculate Cost", e);
        }
    }
}
