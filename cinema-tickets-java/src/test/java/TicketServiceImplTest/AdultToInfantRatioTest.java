package TicketServiceImplTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdultToInfantRatioTest {
    TicketTypeRequest[] numberOfRequests;
    TicketServiceImpl ticketService;

    private static Stream<Arguments> provideValidRatios() {
        return Stream.of(
                //adults >= infants
                Arguments.of(1, 1),
                Arguments.of(2, 1)
        );
    }

    private static Stream<Arguments> provideInValidRatios() {
        return Stream.of(
                //adults < infants
                Arguments.of(1, 2)
        );
    }

    @BeforeEach
    public void beforeEach() {
        ticketService = new TicketServiceImpl();
        numberOfRequests = new TicketTypeRequest[]{};
    }

    @ParameterizedTest
    @MethodSource("provideInValidRatios")
    @DisplayName("Invalid Parent to Infant Ratio")
    public void givenMoreInfantsThanAdults_whenPurchaseTickets_thenInvalidPurchaseException(int numberOfAdults, int numberOfInfants) {
        assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(1L,
                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, numberOfAdults),
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, numberOfInfants)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidRatios")
    @DisplayName("Valid Parent to Infant Ratio")
    public void givenInfantsEqualToAdults_whenPurchaseTickets_thenSuccess(int numberOfAdults, int numberOfInfants) {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, numberOfAdults),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, numberOfInfants)
        );
    }
}
