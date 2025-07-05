package io.quarkusdroneshop.kitchen.domain;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.kitchen.domain.valueobjects.TicketIn;
import io.quarkusdroneshop.kitchen.domain.valueobjects.TicketUp;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class KitchenTest {

    static final Logger logger = Logger.getLogger(KitchenTest.class.getName());

    @Inject
    Kitchen kitchen;

    @Test
    public void testOrderCakepop() throws ExecutionException, InterruptedException {

        logger.info("Test that a Cakepop is ready instantly");

        TicketIn orderIn = new TicketIn(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Item.CAKEPOP, "Minnie", Instant.now());

        TicketUp ticketUp = kitchen.make(orderIn);
        assertEquals(orderIn.getItem(), ticketUp.getItem());
        assertEquals(orderIn.getOrderId(), ticketUp.getOrderId());
        assertEquals(orderIn.getName(), ticketUp.getName());
    }
}
