package TicketServiceImplTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountIdTest {
    TicketServiceImpl ticketService = new TicketServiceImpl();

    @Test
    @DisplayName("Account ID less than minimum.")
    void givenAccountIdLessThanOne_whenPurchaseTickets_thenInvalidPurchaseException() {
        assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(0L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)));
    }

    @Test
    @DisplayName("Account ID Greater than minimum")
    void givenAccountIdGreaterThanZero_whenPurchaseTickets_thenSuccess() {
        Assertions.fail();
        //        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
    }
}
