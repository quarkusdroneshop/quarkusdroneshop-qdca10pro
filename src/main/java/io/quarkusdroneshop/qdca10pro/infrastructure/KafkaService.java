package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.qdca10pro.domain.Qdca10pro;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderUp;

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
public class KafkaService {

    Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    Qdca10pro qdca10pro;

    @Inject
    @Channel("orders-up")
    Emitter<OrderUp> orderUpEmitter;

    @Inject
    @Channel("eighty-six")
    Emitter<String> eightySixEmitter;

    @Incoming("orders-in")
    public CompletableFuture<Void> onOrderIn(final OrderIn orderIn) {

        logger.debug("OrderTicket received: {}", orderIn);

        return CompletableFuture
            .supplyAsync(new Qdca10proTask(qdca10pro, orderIn))
            .thenAccept(result -> {
                if (result.isEightySixed()) {
                    logger.debug("Item is eighty-sixed, sending to topic: {}", orderIn.getItem());
                    eightySixEmitter.send(orderIn.getItem().toString())
                        .whenComplete((res, ex) -> {
                            if (ex != null) {
                                logger.error("Failed to send to eighty-six topic", ex);
                            } else {
                                logger.debug("Sent to eighty-six topic successfully");
                            }
                        });
                } else {
                    OrderUp orderUp = result.getOrderUp();
                    logger.debug("OrderUp: {}", orderUp);
                    orderUpEmitter.send(orderUp)
                        .whenComplete((res, ex) -> {
                            if (ex != null) {
                                logger.error("Failed to send OrderUp to Kafka", ex);
                            } else {
                                logger.debug("OrderUp sent successfully to Kafka");
                            }
                        });
                }
            });
    }
}