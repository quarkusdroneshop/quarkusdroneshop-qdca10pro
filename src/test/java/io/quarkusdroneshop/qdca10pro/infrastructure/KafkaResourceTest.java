package io.quarkusdroneshop.kitchen.infrastructure;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkusdroneshop.kitchen.domain.Item;
import io.quarkusdroneshop.kitchen.domain.Kitchen;
import io.quarkusdroneshop.kitchen.domain.valueobjects.TicketIn;
import io.quarkusdroneshop.kitchen.testing.KafkaTestProfile;
import io.quarkusdroneshop.kitchen.testing.KafkaTestResource;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class KafkaResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaResourceTest.class);

    @ConfigProperty(name = "mp.messaging.incoming.kitchen-in.topic")
    protected String KITCHEN_IN;

    @InjectSpy
    Kitchen kitchen;

    @Inject
    @Any
    InMemoryConnector connector;

    InMemorySource<TicketIn> ordersIn;

//    @BeforeEach
//    public void setUp() {
//        ordersIn = connector.source(ORDERS_IN);
//    }

    @Test
    public void testOrderIn() {

        LOGGER.debug("testOrderIn");

        TicketIn ticketIn = new TicketIn(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            Item.CAKEPOP,
            "Uhura",
            Instant.now()
        );
        ordersIn = connector.source("kitchen-in");
        ordersIn.send(ticketIn);
        await().atLeast(6, TimeUnit.SECONDS);
        verify(kitchen, times(1)).make(any(TicketIn.class));
    }
}
