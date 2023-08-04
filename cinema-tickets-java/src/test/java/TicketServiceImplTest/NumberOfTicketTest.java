package TicketServiceImplTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberOfTicketTest {
    TicketServiceImpl ticketService;

    @BeforeEach
    public void beforeEach() {
        ticketService = new TicketServiceImpl();
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {-1, 0, 21, 30})
    @DisplayName("NEGATIVE: Solo Type Invalid Number of Tickets")
    public void givenSoloTypeTicketsRequestedIsInvalid_whenPurchaseTickets_thenInvalidPurchaseException(int tickets) {
        //Given a valid account ID, with a single type of TicketTypeRequests with invalid number of tickets
        Long accountID = 1L;
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, tickets);
        //When purchasing tickets
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(accountID, request)
        );
        //Then assert Invalid Purchase Exception is thrown
        assertEquals(InvalidPurchaseException.class, exception.getCause().getClass());
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {-1, 0, 7})
    @DisplayName("NEGATIVE: Multi Type Invalid Number of Tickets")
    public void givenMultiTypeTicketsRequestedIsInvalid_whenPurchaseTickets_thenInvalidPurchaseException(int tickets) {
        //Given a valid account ID, with a multiple types of TicketTypeRequests with invalid number of tickets
        Long accountID = 1L;
        TicketTypeRequest[] requests = new TicketTypeRequest[]{
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, tickets),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, tickets),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, tickets)
        };
        //When purchasing tickets
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(accountID, requests
                )
        );
        //Then assert Invalid Purchase Exception is thrown
        assertEquals(InvalidPurchaseException.class, exception.getCause().getClass());
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {1, 10, 20})
    @DisplayName("POSITIVE: Solo Type valid Number of Tickets")
    public void givenSoloTypeTicketsRequestedIsValid_whenPurchaseTickets_thenSuccess(int tickets) {
        //Given a valid account ID, with a single type of TicketTypeRequests with valid number of tickets
        Long accountID = 1L;
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, tickets);
        //When purchasing tickets
        //Then assert no assertion thrown
        assertDoesNotThrow(() -> ticketService.purchaseTickets(accountID, request));
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {1, 5})
    @DisplayName("POSITIVE: Multi Type valid Number of Tickets")
    public void givenMultiTypeTicketsRequestedIsValid_whenPurchaseTickets_thenSuccess(int tickets) {
        //Given a valid account ID, with a multiple types of TicketTypeRequests with a valid number of tickets
        Long accountID = 1L;
        TicketTypeRequest[] requests = new TicketTypeRequest[]{
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, tickets),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, tickets),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, tickets)
        };
        //When purchasing tickets
        //Then assert no assertion thrown
        assertDoesNotThrow(() -> ticketService.purchaseTickets(accountID, requests));
    }
}
