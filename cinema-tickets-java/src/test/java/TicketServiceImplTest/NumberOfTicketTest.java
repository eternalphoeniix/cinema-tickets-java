package TicketServiceImplTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;

public class NumberOfTicketTest {
    TicketServiceImpl ticketService = new TicketServiceImpl();

    @Test
    @DisplayName("Tickets requested greater than maximum")
    void givenTicketsRequestedGreaterThanMax_whenPurchaseTickets_thenInvalidPurchaseException() {
        Assertions.fail();
    }

    @Test
    @DisplayName("Tickets requested equal to limit")
    void givenTicketsRequestedEqualToMaximum_whenPurchaseTickets_thenSuccess() {
        Assertions.fail();
    }

    @Test
    @DisplayName("Tickets requested under limit")
    void givenTicketsRequestedUnderMaximum_whenPurchaseTickets_thenSuccess() {
        Assertions.fail();
    }
}
