package TicketServiceImplTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberOfTicketTest {
    TicketServiceImpl ticketService;

    @BeforeEach
    public void beforeEach() {
        ticketService = new TicketServiceImpl();
    }

    @ParameterizedTest(name = "{index} Invalid Number of Tickets Requested: {0}")
    @ValueSource(ints = {-1, 0, 21, 30})
    @DisplayName("NEGATIVE: Solo Type Invalid Number of Tickets")
    public void givenSoloTypeTicketsRequestedIsInvalid_whenPurchaseTickets_thenInvalidPurchaseException(int tickets) {
        assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, tickets)
                )
        );
    }

    @ParameterizedTest(name = "{index} Invalid Number of Tickets Requested: {0}")
    @ValueSource(ints = {-1, 0, 7})
    @DisplayName("NEGATIVE: Multi Type Invalid Number of Tickets")
    public void givenMultiTypeTicketsRequestedIsInvalid_whenPurchaseTickets_thenInvalidPurchaseException(int tickets) {
        assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(1L,
                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, tickets),
                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, tickets),
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, tickets)
                )
        );
    }

    @ParameterizedTest(name = "{index} Valid Number of Tickets Requested: {0}")
    @ValueSource(ints = {1, 10, 20})
    @DisplayName("POSITIVE: Solo Type valid Number of Tickets")
    public void givenSoloTypeTicketsRequestedIsValid_whenPurchaseTickets_thenSuccess(int tickets) {
        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, tickets));
    }

    @ParameterizedTest(name = "{index} Valid Number of Tickets Requested: {0}")
    @ValueSource(ints = {1, 5})
    @DisplayName("POSITIVE: Multi Type valid Number of Tickets")
    public void givenMultiTypeTicketsRequestedIsValid_whenPurchaseTickets_thenSuccess(int tickets) {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, tickets),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, tickets),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, tickets)
        );
    }
}
