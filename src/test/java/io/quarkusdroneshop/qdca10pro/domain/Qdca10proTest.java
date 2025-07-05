package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.TicketIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.TicketUp;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class Qdca10proTest {

    static final Logger logger = Logger.getLogger(Qdca10proTest.class.getName());

    @Inject
    qdca10pro QDCA10Pro;

    @Test
    public void testOrderCakepop() throws ExecutionException, InterruptedException {

        logger.info("Test that a Cakepop is ready instantly");

        TicketIn orderIn = new TicketIn(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Item.QDC_A105_Pro01, "Minnie", Instant.now());

        TicketUp ticketUp = QDCA10Pro.make(orderIn);
        assertEquals(orderIn.getItem(), ticketUp.getItem());
        assertEquals(orderIn.getOrderId(), ticketUp.getOrderId());
        assertEquals(orderIn.getName(), ticketUp.getName());
    }
}
