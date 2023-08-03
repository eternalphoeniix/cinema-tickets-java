package TicketServiceImplTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;

public class TicketCostTest {
    TicketServiceImpl ticketService = new TicketServiceImpl();

    @Test
    @DisplayName("Adult cost")
    void givenAdultTicket_whenPurchaseTickets_thenAssertCostEquals() {
        Assertions.fail();
    }

    @Test
    @DisplayName("Child and Adult cost")
    void givenAdultAndChild_whenPurchaseTickets_thenAssertCostEquals() {
        Assertions.fail();
    }

    @Test
    @DisplayName("Infant and Adult cost")
    void givenAdultAndInfant_whenPurchaseTickets_thenAssertCostEquals() {
        Assertions.fail();
    }
}
