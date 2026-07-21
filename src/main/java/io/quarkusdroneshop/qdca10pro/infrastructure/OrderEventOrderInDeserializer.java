package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;
import io.quarkusdroneshop.qdca10pro.domain.Item;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * dataproduct-order-events (Avro, order-events Flink job が発行) から
 * QDCA10pro 宛ての ORDER_PLACED イベントのみを OrderIn に変換する。
 * それ以外 (他 eventType、他 assemblyLine 宛ての明細) は null を返す。
 */
public class OrderEventOrderInDeserializer implements Deserializer<OrderIn> {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventOrderInDeserializer.class);

    private final AvroKafkaDeserializer<GenericRecord> avroDeserializer = new AvroKafkaDeserializer<>();

    @Override
    public void configure(java.util.Map<String, ?> configs, boolean isKey) {
        avroDeserializer.configure(configs, isKey);
    }

    @Override
    public OrderIn deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        GenericRecord record = avroDeserializer.deserialize(topic, new RecordHeaders(), data);
        if (record == null) {
            return null;
        }

        Object eventTypeObj = record.get("eventType");
        if (eventTypeObj == null || !"ORDER_PLACED".equals(eventTypeObj.toString())) {
            return null;
        }

        GenericRecord lineItem = (GenericRecord) record.get("lineItem");
        if (lineItem == null) {
            return null;
        }

        Object assemblyLineObj = lineItem.get("assemblyLine");
        if (assemblyLineObj == null || !"QDCA10PRO".equals(assemblyLineObj.toString())) {
            return null;
        }

        try {
            String orderId = record.get("orderId").toString();
            String lineItemId = lineItem.get("itemId").toString();
            Item item = Item.valueOf(lineItem.get("item").toString());
            String name = String.valueOf(lineItem.get("name"));

            return new OrderIn(orderId, lineItemId, item, name);
        } catch (Exception e) {
            logger.warn("Failed to convert OrderEvent to OrderIn: {}", record, e);
            return null;
        }
    }

    @Override
    public void close() {
        avroDeserializer.close();
    }
}
