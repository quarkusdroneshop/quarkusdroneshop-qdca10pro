package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.qdca10pro.domain.qdca10pro;
import io.quarkusdroneshop.qdca10pro.domain.exceptions.EightySixException;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.TicketIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.TicketUp;
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
    qdca10pro QDCA10Pro;

    @Inject
    @Channel("orders-up")
    Emitter<TicketUp> orderUpEmitter;

    @Inject
    @Channel("eighty-six-out")
    Emitter<String> eightySixEmitter;

    @Incoming("qdca10pro-in")
    public CompletableFuture handleOrderIn(final TicketIn ticketIn) {

        logger.debug("TicketIn received: {}", ticketIn);

        return CompletableFuture.supplyAsync(() -> {
            return QDCA10Pro.make(ticketIn);
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
