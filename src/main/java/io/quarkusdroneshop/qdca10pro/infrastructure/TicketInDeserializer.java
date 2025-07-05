package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.TicketIn;

public class TicketInDeserializer extends ObjectMapperDeserializer<TicketIn> {

    public TicketInDeserializer() {
        super(TicketIn.class);
    }
}
