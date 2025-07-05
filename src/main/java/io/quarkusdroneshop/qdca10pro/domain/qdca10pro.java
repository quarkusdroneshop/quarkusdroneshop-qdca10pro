package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkusdroneshop.qdca10pro.domain.exceptions.EightySixException;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.TicketIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.TicketUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;

@ApplicationScoped
public class qdca10pro {

    static final Logger logger = LoggerFactory.getLogger(qdca10pro.class.getName());

    @Inject
    Inventory inventory;

    private String madeBy = "undefined";

    @PostConstruct
    void setHostName() {
        try {
            madeBy = InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            logger.debug("unable to get hostname");
            madeBy = "unknown";
        }
    }

    public TicketUp make(final TicketIn ticketIn){

        logger.debug("making: {}", ticketIn.getItem());

        int delay;
        switch (ticketIn.getItem()) {
            case QDC_A105_Pro01:
                delay = 5;
                break;
            case QDC_A105_Pro02:
                delay = 3;
                break;
            case QDC_A105_Pro03:
                delay = 5;
                break;
            case QDC_A105_Pro04:
                delay = 7;
                break;
            default:
                delay = 10;
                break;
        };
        return prepare(ticketIn, delay);
    }

    /*
    Delay for the specified time and then return the completed TicketUp
    @throws RuntimeException for 86'd items
 */
    private TicketUp prepare(final TicketIn ticketIn, int seconds) {

        // decrement the item in inventory
        try {

            inventory.decrementItem(ticketIn.getItem());
            logger.debug("inventory decremented 1 {}", ticketIn.getItem());
        } catch (EightySixException e) {

            logger.debug(ticketIn.getItem() + " is 86'd");
            throw new EightySixException(ticketIn.getItem());
        }

        // model the qdca10's time making the drink
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // return the completed drink
        return new TicketUp(
                ticketIn.getOrderId(),
                ticketIn.getLineItemId(),
                ticketIn.getItem(),
                ticketIn.getName(),
                madeBy);
    }


//    public CompletableFuture<Event> make(final TicketIn ticketIn) {
//
//        logger.debug("orderIn: " + ticketIn.toString());
//        return CompletableFuture.supplyAsync(() -> {
//
//            switch(ticketIn.getItem()){
//                case CAKEPOP:
//                    return prepare(ticketIn, 5);
//                case CROISSANT:
//                    return prepare(ticketIn, 5);
//                case CROISSANT_CHOCOLATE:
//                    return prepare(ticketIn, 5);
//                case MUFFIN:
//                    return prepare(ticketIn, 7);
//                default:
//                    return prepare(ticketIn, 11);
//            }
//        });
//    }

//    private TicketUp prepare(final TicketIn ticketIn, int seconds) {
//
//        // decrement the item in inventory
//        try {
//            inventory.decrementItem(ticketIn.getItem());
//        } catch (EightySixException e) {
//            e.printStackTrace();
//            logger.debug(ticketIn.getItem() + " is 86'd");
//            return new EightySixEvent(ticketIn.getItem());
//        }
//
//        // give the QDCA10Pro time to make the item
//        try {
//            Thread.sleep(seconds * 1000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        return new TicketUp(
//                ticketIn.getOrderId(),
//                ticketIn.getLineItemId(),
//                ticketIn.getItem(),
//                ticketIn.getName(),
//                madeBy);
//    }
}
