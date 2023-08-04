package TicketServiceImplTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TicketCostTest {
    TicketServiceImpl ticketService = new TicketServiceImpl();

    private static Stream<Arguments> provideValidInput() {
        return Stream.of(
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{
                                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)
                        }}
                ),
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{
                                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2)
                        }}
                ),
                Arguments.of(
                        new Object[]{new TicketTypeRequest[]{
                                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3),
                                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3)
                        }}
                )
        );
    }

    @BeforeEach
    public void beforeEach() {
        ticketService = new TicketServiceImpl();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideValidInput")
    @DisplayName("POSITIVE: Valid Input outputs correct cost")
    public void givenValidTicket_whenPurchaseTickets_thenNoAssertionThrow(TicketTypeRequest... requests) {
        //Given a valid account ID, valid ticket type requests
        Long accountID = 1L;
        //When purchasing tickets
        //Then assert no assertion thrown
        assertDoesNotThrow(() -> ticketService.purchaseTickets(accountID, requests));
    }
}
