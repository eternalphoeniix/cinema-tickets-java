package TicketServiceImplTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountIdTest {
    TicketServiceImpl ticketService;

    @BeforeEach
    public void beforeEach() {
        ticketService = new TicketServiceImpl();
    }

    @ParameterizedTest(name = "{index} Invalid Account Number Requested: {0}")
    @ValueSource(longs = {-1L, 0L})
    @DisplayName("Invalid Account Numbers")
    public void givenAccountIdLessThanOne_whenPurchaseTickets_thenInvalidPurchaseException(long accountID) {
        assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(accountID, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)));
    }

    @ParameterizedTest(name = "{index} Valid Account Number Requested: {0}")
    @ValueSource(longs = {1L, 100L, Long.MAX_VALUE})
    @DisplayName("Valid Account Numbers")
    public void givenAccountIdGreaterThanZero_whenPurchaseTickets_thenSuccess(long accountID) {
        ticketService.purchaseTickets(accountID, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
    }
}
