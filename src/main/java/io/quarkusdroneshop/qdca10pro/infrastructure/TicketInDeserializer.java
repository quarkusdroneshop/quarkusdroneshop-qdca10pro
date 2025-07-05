package io.quarkusdroneshop.kitchen.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkusdroneshop.kitchen.domain.valueobjects.TicketIn;

public class TicketInDeserializer extends ObjectMapperDeserializer<TicketIn> {

    public TicketInDeserializer() {
        super(TicketIn.class);
    }
}
