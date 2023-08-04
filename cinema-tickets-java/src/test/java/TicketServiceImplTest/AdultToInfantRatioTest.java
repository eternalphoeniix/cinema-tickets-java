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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdultToInfantRatioTest {
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
    }

    @ParameterizedTest(name = "ADULT: {0}, INFANT: {1}")
    @MethodSource("provideInValidRatios")
    @DisplayName("NEGATIVE: Invalid Parent to Infant Ratio")
    public void givenMoreInfantsThanAdults_whenPurchaseTickets_thenInvalidPurchaseException(int numberOfAdults, int numberOfInfants) {
        //Given a valid account ID, and more infant Ticket Type requests than Adults.
        Long accountID = 1L;
        TicketTypeRequest[] requests = new TicketTypeRequest[]{
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, numberOfAdults),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, numberOfInfants)
        };
        //When purchasing tickets
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(accountID, requests));
        //Then assert Invalid Purchase Exception is thrown
        assertEquals(InvalidPurchaseException.class, exception.getCause().getClass());
    }

    @ParameterizedTest(name = "ADULT: {0}, INFANT: {1}")
    @MethodSource("provideValidRatios")
    @DisplayName("POSITIVE: Valid Parent to Infant Ratio")
    public void givenInfantsEqualToAdults_whenPurchaseTickets_thenSuccess(int numberOfAdults, int numberOfInfants) {
        //Given a valid account ID, and an equal number of infant Ticket Type requests to Adults.
        Long accountID = 1L;
        TicketTypeRequest[] requests = new TicketTypeRequest[]{
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, numberOfAdults),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, numberOfInfants)
        };
        //When purchasing tickets
        //Then no assertion is thrown
        ticketService.purchaseTickets(1L, requests);
    }
}
