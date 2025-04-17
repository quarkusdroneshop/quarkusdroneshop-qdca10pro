package io.quarkuscoffeeshop.kitchen.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkuscoffeeshop.kitchen.domain.Kitchen;
import io.quarkuscoffeeshop.kitchen.domain.exceptions.EightySixException;
import io.quarkuscoffeeshop.kitchen.domain.valueobjects.TicketIn;
import io.quarkuscoffeeshop.kitchen.domain.valueobjects.TicketUp;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
@RegisterForReflection
public class KafkaResource {

    final Logger logger = LoggerFactory.getLogger(KafkaResource.class);

    @Inject
    Kitchen kitchen;

    @Inject
    @Channel("orders-up")
    Emitter<TicketUp> orderUpEmitter;

    @Inject
    @Channel("eighty-six-out")
    Emitter<String> eightySixEmitter;

    @Incoming("kitchen-in")
    public CompletableFuture handleOrderIn(final TicketIn ticketIn) {

        logger.debug("TicketIn received: {}", ticketIn);

        return CompletableFuture.supplyAsync(() -> {
            return kitchen.make(ticketIn);
        }).thenApply(orderUp -> {
            logger.debug("OrderUp: {}", orderUp);
            orderUpEmitter.send(orderUp);
            return null;
        }).exceptionally(exception -> {
            logger.debug("EightySixException: {}", exception.getMessage());
            ((EightySixException) exception).getItems().forEach(item -> {
                eightySixEmitter.send(item.toString());
            });
            return null;
        });
    }
}
