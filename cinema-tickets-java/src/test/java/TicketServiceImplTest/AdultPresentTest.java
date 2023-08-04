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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdultPresentTest {
    TicketServiceImpl ticketService;

    private static Stream<Arguments> provideValidRatios() {
        return Stream.of(
                //1 ADULT for any number of CHILD and INFANT
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{
                                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1)
                        }}
                ),
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{
                                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
                        }}
                ),
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{
                                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1),
                                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
                        }}
                )
        );
    }

    private static Stream<Arguments> provideInValidRatios() {
        return Stream.of(
                //0 ADULT for any number of CHILD and INFANT
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1)
                        }}
                ),
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{
                                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
                        }}
                ),
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{
                                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1),
                                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
                        }}
                )
        );
    }

    @BeforeEach
    public void beforeEach() {
        ticketService = new TicketServiceImpl();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideInValidRatios")
    @DisplayName("NEGATIVE: No Adult with CHILD")
    public void givenNoAdultWithMinors_whenPurchaseTickets_thenInvalidPurchaseException(TicketTypeRequest... requests) {
        //Given A valid account ID, but a ticket type request is made by a Child/Infant without an adult.
        Long accountID = 1L;
        //When purchasing tickets
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(accountID, requests));
        //Then assert Invalid Purchase Exception is thrown
        assertEquals("Adult not present when attempting to purchase child/infant ticket", exception.getCause().getMessage());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideValidRatios")
    @DisplayName("POSITIVE: Adult Present with Minors")
    public void givenAdultWithMinors_whenPurchaseTickets_thenNoAssertionThrow(TicketTypeRequest... requests) {
        //Given a valid account ID with a valid ticket type request.
        Long accountID = 1L;
        //When purchasing tickets
        //Then no assertion is thrown
        assertDoesNotThrow(() -> ticketService.purchaseTickets(accountID, requests));
    }
}
