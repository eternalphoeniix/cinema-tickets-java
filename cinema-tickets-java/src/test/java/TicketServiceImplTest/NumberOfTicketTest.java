package TicketServiceImplTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberOfTicketTest {
    TicketTypeRequest[] numberOfRequests;
    TicketServiceImpl ticketService;

    @BeforeEach
    public void beforeEach() {
        ticketService = new TicketServiceImpl();
        numberOfRequests = new TicketTypeRequest[]{};
    }

    @ParameterizedTest(name = "{index} Invalid Number of Tickets Requested: {0}")
    @ValueSource(ints = {0, 21, 30})
    @DisplayName("Invalid Number of Tickets")
    public void givenTicketsRequestedIsInvalid_whenPurchaseTickets_thenInvalidPurchaseException(int ticketsToGenerate) {
        generateTicketTypeRequests(ticketsToGenerate);
        assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(1L, numberOfRequests));
    }

    @ParameterizedTest(name = "{index} Valid Number of Tickets Requested: {0}")
    @ValueSource(ints = {1, 10, 20})
    @DisplayName("Valid Number of Tickets")
    public void givenTicketsRequestedEqualToMaximum_whenPurchaseTickets_thenSuccess(int ticketsToGenerate) {
        generateTicketTypeRequests(ticketsToGenerate);
        ticketService.purchaseTickets(1L, numberOfRequests);
    }

    private void generateTicketTypeRequests(int total) {
        numberOfRequests = new TicketTypeRequest[total];
        Arrays.fill(numberOfRequests, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
    }
}
