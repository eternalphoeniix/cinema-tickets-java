package uk.gov.dwp.uc.pairtest.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;

public class InvalidPurchaseException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(TicketServiceImpl.class.getName());

    public InvalidPurchaseException() {
        super("Invalid Purchase");
        logger.error("Invalid Purchase");
    }

    public InvalidPurchaseException(String message) {
        super(message);
        logger.error(message);
    }

    public InvalidPurchaseException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message + cause.getMessage());
    }
}
