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
                int totalSeatsToAllocate = calculateSeatsRequired(ticketTypeRequests);
                reserveSeats(accountId, totalSeatsToAllocate);
                int totalAmountToPay = calculateCost(ticketTypeRequests);
                submitPurchase(accountId, totalAmountToPay);
            }
        } catch (Exception e) {
            throw new InvalidPurchaseException("Issue with Purchasing Tickets. ", e);
        }
    }

    private void submitPurchase(Long accountId, int totalAmountToPay) {
        LOGGER.debug("Making Payment of £{}", totalAmountToPay);
        TicketPaymentServiceImpl ticketServiceImpl = new TicketPaymentServiceImpl();
        ticketServiceImpl.makePayment(accountId, totalAmountToPay);
    }

    private void reserveSeats(Long accountId, int totalSeatsToAllocate) {
        LOGGER.debug("Reserving {} Seats", totalSeatsToAllocate);
        SeatReservationServiceImpl seatReservationServiceImpl = new SeatReservationServiceImpl();
        seatReservationServiceImpl.reserveSeat(accountId, totalSeatsToAllocate);
    }

    private boolean isRequestValid(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        LOGGER.debug("Checking Ticket Purchase request is valid");
        boolean isValid =
                isAccountIdValid(accountId)
                        && isTicketCountWithinLimit(ticketTypeRequests)
                        && isAdultToMinorValid(ticketTypeRequests)
                        && isAdultLapAvailableForInfant(ticketTypeRequests);
        if (isValid) {
            return true;
        }
        else {
            throw new InvalidPurchaseException("Ticket Purchase Request is invalid");
        }
    }

    private boolean isAccountIdValid(Long accountId) {
        LOGGER.debug("Checking Account ID is valid: " + accountId);
        if (accountId >= MINACCOUNTID) {
            LOGGER.trace("Account ID valid");
            return true;
        }
        else {
            throw new InvalidPurchaseException("Account ID Invalid");
        }
    }

    private boolean isTicketCountWithinLimit(TicketTypeRequest... requests) {
        LOGGER.debug("Checking number of tickets does not exceed maximum");
        try {
            int numberOfTickets = 0;
            for (TicketTypeRequest request : requests) {
                if (request.getNoOfTickets() <= MAXREQUESTEDTICKETS && request.getNoOfTickets() >= MINREQUESTEDTICKETS) {
                    numberOfTickets += request.getNoOfTickets();
                }
                else {
                    throw new InvalidPurchaseException("Invalid Number of Tickets requested");
                }
            }
            if (numberOfTickets <= MAXREQUESTEDTICKETS) {
                LOGGER.trace(
                        "Total Number of tickets {} is within the maximum limit of {}", numberOfTickets, MAXREQUESTEDTICKETS
                );
                return true;
            }
            else {
                throw new InvalidPurchaseException("Invalid Number of Tickets requested");
            }
        } catch (Exception e) {
            throw e;
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
            if (numberOfInfants == 0) {
                LOGGER.trace("There are no infants");
                return true;
            }
            else if (numberOfAdults >= numberOfInfants) {
                LOGGER.trace(
                        "There is at least one adult per infant. Adults: {}, infants: {}", numberOfAdults, numberOfInfants
                );
                return true;
            }
            else {
                throw new InvalidPurchaseException("There are more infants than adults");
            }
        } catch (Exception e) {
            throw e;
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
                LOGGER.trace("Adult is required as children or infants are present");
                if (isAdultPresent) {
                    LOGGER.trace("Adult is Present as children or infants are present");
                    return true;
                }
                else {
                    throw new InvalidPurchaseException("Adult not present when attempting to purchase child/infant ticket");
                }
            }
            else {
                LOGGER.trace("No Adult is required as there are no children or infants  present");
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private int calculateCost(TicketTypeRequest... requests) {
        LOGGER.debug("Calculating Total Cost");
        try {
            int total = 0;
            for (TicketTypeRequest request : requests
            ) {
                int numberOfTickets = request.getNoOfTickets();
                TicketTypeRequest.Type type = request.getTicketType();
                int costPerTicket = request.getTicketType().getCost();
                LOGGER.trace("Tickets: {}, Type: {}, Price: {}", numberOfTickets, type, costPerTicket);
                total += numberOfTickets * costPerTicket;
            }
            return total;
        } catch (Exception e) {
            throw new InvalidPurchaseException("Unable to Calculate Cost", e);
        }
    }

    private int calculateSeatsRequired(TicketTypeRequest... requests) {
        LOGGER.debug("Calculating total seats");
        try {
            int total = 0;
            for (TicketTypeRequest request : requests
            ) {
                int numberOfTickets = request.getNoOfTickets();
                int seatsPerTicket = request.getTicketType().getSeatsPerTicket();
                LOGGER.trace("Number of Tickets: {}, SeatsPerTicket: {}", numberOfTickets, seatsPerTicket);
                total += numberOfTickets * seatsPerTicket;
            }
            return total;
        } catch (Exception e) {
            throw new InvalidPurchaseException("Unable to Calculate Number of Seats", e);
        }
    }
}
