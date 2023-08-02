package TicketServiceImplTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;

public class AdultToMinorRatioTest {
    TicketServiceImpl ticketService = new TicketServiceImpl();

    @Test
    @DisplayName("More infants than adults")
    void givenMoreInfantsThanAdults_whenPurchaseTickets_thenInvalidPurchaseException() {
        Assertions.fail();
    }

    @Test
    @DisplayName("Number of infants equal to adults")
    void givenInfantsEqualToAdults_whenPurchaseTickets_thenSuccess() {
        Assertions.fail();
    }

    @Test
    @DisplayName("Number of Adults greater than infants")
    void givenAdultsGreaterThanInfants_whenPurchaseTickets_thenSuccess() {
        Assertions.fail();
    }

    @Test
    @DisplayName("Number of children less than adults")
    void givenChildrenLessThanAdults_whenPurchaseTickets_thenInvalidPurchaseException() {
        Assertions.fail();
    }

    @Test
    @DisplayName("Children with 1 adult")
    void givenChildrenWithOneAdult_whenPurchaseTickets_thenSuccess() {
        Assertions.fail();
    }
}
