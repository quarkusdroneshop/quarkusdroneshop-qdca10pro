package io.quarkusdroneshop.qdca10pro.domain.valueobjects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.qdca10pro.domain.Item;

import java.time.Instant;
import java.util.StringJoiner;

@RegisterForReflection
public class OrderIn {

    final String orderId;

    final String lineItemId;

    final Item item;

    final String name;

    final Instant timestamp;

    public OrderIn(final String orderId, final String lineItemId, final Item item, final String name, final Instant timestamp) {
        this.orderId = orderId;
        this.lineItemId = lineItemId;
        this.item = item;
        this.name = name;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderIn.class.getSimpleName() + "[", "]")
                .add("orderId='" + orderId + "'")
                .add("lineItemId='" + lineItemId + "'")
                .add("item=" + item)
                .add("name='" + name + "'")
                .add("timestamp=" + timestamp)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderIn ticketIn = (OrderIn) o;

        if (orderId != null ? !orderId.equals(ticketIn.orderId) : ticketIn.orderId != null) return false;
        if (lineItemId != null ? !lineItemId.equals(ticketIn.lineItemId) : ticketIn.lineItemId != null) return false;
        if (item != ticketIn.item) return false;
        if (name != null ? !name.equals(ticketIn.name) : ticketIn.name != null) return false;
        return timestamp != null ? timestamp.equals(ticketIn.timestamp) : ticketIn.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (lineItemId != null ? lineItemId.hashCode() : 0);
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public Item getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
