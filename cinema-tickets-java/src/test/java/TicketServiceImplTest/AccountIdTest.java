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

public class AccountIdTest {
    TicketServiceImpl ticketService;

    @BeforeEach
    public void beforeEach() {
        ticketService = new TicketServiceImpl();
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(longs = {-1L, 0L})
    @DisplayName("NEGATIVE: Invalid Account Numbers")
    public void givenAccountIdLessThanOne_whenPurchaseTickets_thenInvalidPurchaseException(Long accountID) {
        //Given an invalid account ID with a valid ticket type request.
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        //When purchasing tickets
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(accountID, request));
        //Then assert Invalid Purchase Exception is thrown
        assertEquals(InvalidPurchaseException.class, exception.getCause().getClass());
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(longs = {1L, 100L, Long.MAX_VALUE})
    @DisplayName("POSITIVE: Valid Account Numbers")
    public void givenAccountIdGreaterThanZero_whenPurchaseTickets_thenSuccess(Long accountID) {
        //Given a valid account ID with a valid ticket type request.
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        //When purchasing tickets
        //Then no assertion is thrown
        assertDoesNotThrow(() -> ticketService.purchaseTickets(accountID, request));
    }
}
