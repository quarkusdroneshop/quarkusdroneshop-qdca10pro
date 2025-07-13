package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkusdroneshop.qdca10pro.domain.Item;
import io.quarkusdroneshop.qdca10pro.domain.qdca10pro;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.qdca10pro.testing.KafkaTestProfile;
import io.quarkusdroneshop.qdca10pro.testing.KafkaTestResource;
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

    @ConfigProperty(name = "mp.messaging.incoming.qdca10pro-in.topic")
    protected String Qdca10pro_in;

    @InjectSpy
    qdca10pro Qdca10pro;

    @Inject
    @Any
    InMemoryConnector connector;

    InMemorySource<OrderIn> ordersIn;

    @Test
    public void testOrderIn() {

        LOGGER.debug("testOrderIn");

        OrderIn ticketIn = new OrderIn(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            Item.QDC_A105_Pro01,
            "Uhura",
            Instant.now()
        );
        ordersIn = connector.source("qdca10pro-in");
        ordersIn.send(ticketIn);
        await().atLeast(6, TimeUnit.SECONDS);
        verify(Qdca10pro, times(1)).make(any(OrderIn.class));
    }
}
