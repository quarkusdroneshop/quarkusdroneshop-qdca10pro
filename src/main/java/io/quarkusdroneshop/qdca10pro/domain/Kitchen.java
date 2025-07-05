package io.quarkusdroneshop.kitchen.domain;

import io.quarkusdroneshop.kitchen.domain.exceptions.EightySixException;
import io.quarkusdroneshop.kitchen.domain.valueobjects.TicketIn;
import io.quarkusdroneshop.kitchen.domain.valueobjects.TicketUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;

@ApplicationScoped
public class Kitchen {

    static final Logger logger = LoggerFactory.getLogger(Kitchen.class.getName());

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
            case CROISSANT:
                delay = 5;
                break;
            case CAKEPOP:
                delay = 3;
                break;
            case CROISSANT_CHOCOLATE:
                delay = 5;
                break;
            case MUFFIN:
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
//        // give the kitchen time to make the item
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
