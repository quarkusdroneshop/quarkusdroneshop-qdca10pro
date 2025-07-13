package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;

public class TicketInDeserializer extends ObjectMapperDeserializer<OrderIn> {

    public TicketInDeserializer() {
        super(OrderIn.class);
    }
}
