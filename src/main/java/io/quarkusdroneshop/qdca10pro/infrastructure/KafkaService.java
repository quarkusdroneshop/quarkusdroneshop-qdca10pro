package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.qdca10pro.domain.Qdca10pro;
import io.quarkusdroneshop.qdca10pro.domain.exceptions.EightySixException;
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

    final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    Qdca10pro Qdca10pro;

    @Inject
    @Channel("orders-up")
    Emitter<OrderUp> orderUpEmitter;

    @Inject
    @Channel("eighty-six-out")
    Emitter<String> eightySixEmitter;

    @Incoming("qdca10pro-in")
    
    public CompletableFuture<Void> onOrderInPro(final OrderIn orderIn) {
    
        logger.debug("OrderTicket (pro) received: {}", orderIn);
    
        return CompletableFuture
            .supplyAsync(new Qdca10proTask(Qdca10pro, orderIn))
            .thenAccept(result -> {
    
                if (result.isEightySixed()) {
                    eightySixEmitter.send(orderIn.getItem().toString());
                } else {
                    logger.debug("OrderUp (pro): {}", result.getOrderUp());
                    orderUpEmitter.send(result.getOrderUp());
                }
            });
    }
}
