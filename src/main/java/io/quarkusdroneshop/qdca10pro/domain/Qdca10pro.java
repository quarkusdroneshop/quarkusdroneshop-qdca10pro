package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkusdroneshop.qdca10pro.domain.exceptions.EightySixException;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderUp;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.Qdca10proResult;
import io.quarkusdroneshop.qdca10pro.domain.EightySixEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

@ApplicationScoped
public class Qdca10pro {

    static final Logger logger = LoggerFactory.getLogger(Qdca10pro.class.getName());

    @Inject
    Inventory inventory;

    public Qdca10proResult make(final OrderIn ticketIn){

        logger.debug("making: {}", ticketIn.getItem());

        int delay = calculateDelay(ticketIn);
        
        try {
            OrderUp orderUp = prepare(ticketIn, delay);
            return new Qdca10proResult(orderUp);
        } catch (EightySixException e) {
            return new Qdca10proResult(new EightySixEvent(ticketIn.getItem()));
        }
    }

    /*
    Delay for the specified time and then return the completed TicketUp
    @throws RuntimeException for 86'd items
 */
    private OrderUp prepare(final OrderIn ticketIn, int seconds) {

        String madeBy;
        
        // decrement the item in inventory
        try {

            inventory.decrementItem(ticketIn.getItem());
            logger.debug("inventory decremented 1 {}", ticketIn.getItem());
        } catch (EightySixException e) {

            logger.debug(ticketIn.getItem() + " is 86'd");
            throw new EightySixException(ticketIn.getItem());
        }

        try {
            madeBy = InetAddress.getLocalHost().getHostName() + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        } catch (IOException e) {
            logger.debug("unable to get hostname");
            madeBy = "unknown";
        }

        // model the qdca10's time making the drink
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // return the completed drink
        return new OrderUp(
                ticketIn.getOrderId(),
                ticketIn.getLineItemId(),
                ticketIn.getItem(),
                ticketIn.getName(),
                madeBy);
    }

    private int calculateDelay(OrderIn ticketIn) {
        switch (ticketIn.getItem()) {
            case QDC_A105_Pro01:
                return 5;
            case QDC_A105_Pro02:
                return 3;
            case QDC_A105_Pro03:
                return 5;
            case QDC_A105_Pro04:
                return 7;
            default:
                return 10;
        }
    }
}
