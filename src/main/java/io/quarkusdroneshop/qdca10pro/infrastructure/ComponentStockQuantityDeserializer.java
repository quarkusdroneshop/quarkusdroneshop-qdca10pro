package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;
import io.quarkusdroneshop.qdca10pro.domain.Item;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.ComponentStockUpdate;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * drone-component-stock データプロダクトが公開する dataproduct-component-stock-quantity
 * (upsert-kafka, Avro) を ComponentStockUpdate に変換する。この Item enum に存在しない
 * item や変換に失敗したレコードは null を返し、KafkaService 側で無視される。
 */
public class ComponentStockQuantityDeserializer implements Deserializer<ComponentStockUpdate> {

    private static final Logger logger = LoggerFactory.getLogger(ComponentStockQuantityDeserializer.class);

    private final AvroKafkaDeserializer<GenericRecord> avroDeserializer = new AvroKafkaDeserializer<>();

    @Override
    public void configure(java.util.Map<String, ?> configs, boolean isKey) {
        avroDeserializer.configure(configs, isKey);
    }

    @Override
    public ComponentStockUpdate deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        GenericRecord record = avroDeserializer.deserialize(topic, new RecordHeaders(), data);
        if (record == null) {
            return null;
        }

        try {
            Item item = Item.valueOf(record.get("item").toString());
            long quantity = (Long) record.get("quantity");
            return new ComponentStockUpdate(item, quantity);
        } catch (IllegalArgumentException e) {
            logger.debug("Unknown item in component-stock-quantity record, skipping: {}", record);
            return null;
        } catch (Exception e) {
            logger.warn("Failed to convert component-stock-quantity record: {}", record, e);
            return null;
        }
    }

    @Override
    public void close() {
        avroDeserializer.close();
    }
}
