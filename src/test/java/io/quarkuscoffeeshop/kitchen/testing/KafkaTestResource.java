package io.quarkuscoffeeshop.kitchen.testing;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashMap;
import java.util.Map;

public class KafkaTestResource implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();
        Map<String, String> props1 = InMemoryConnector.switchIncomingChannelsToInMemory("kitchen-in");
        Map<String, String> props2 = InMemoryConnector.switchOutgoingChannelsToInMemory("orders-up");
        Map<String, String> props3 = InMemoryConnector.switchOutgoingChannelsToInMemory("eighty-six-out");
        env.putAll(props1);
        env.putAll(props2);
        env.putAll(props3);
        env.put("KAFKA_BOOTSTRAP_URLS", "localhost:9092");
        return env;
    }

    @Override
    public void stop() {
        InMemoryConnector.clear();
    }
}
