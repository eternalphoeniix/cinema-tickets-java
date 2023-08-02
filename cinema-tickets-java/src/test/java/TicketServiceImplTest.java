import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketServiceImplTest {
    TicketServiceImpl ticketService = new TicketServiceImpl();

    @Test
    @DisplayName("Negative boundary test MakePayment. Account ID is less than 1 throw InvalidPurchaseException")
    void givenAccountIdLessThanOne_whenMakePayment_thenInvalidPurchaseException() {
        assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(0L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)));
    }

    @Test
    @DisplayName("Boundary test MakePayment. When Account ID is greater than 0, success")
    void givenAccountIdGreaterThanZero_whenMakePayment_thenSuccess() {
        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
    }
}
