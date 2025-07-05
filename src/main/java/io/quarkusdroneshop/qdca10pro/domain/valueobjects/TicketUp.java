package io.quarkusdroneshop.qdca10pro.domain.valueobjects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.qdca10pro.domain.EventType;
import io.quarkusdroneshop.qdca10pro.domain.Item;
import io.quarkusdroneshop.qdca10pro.domain.LineItemEvent;

import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
public class TicketUp {

    final String orderId;

    final String lineItemId;

    final Item item;

    final String name;

    final Instant timestamp;

    final String madeBy;

    public TicketUp(final String orderId, final String lineItemId, final Item item, final String name, final String madeBy) {
        this.orderId = orderId;
        this.lineItemId = lineItemId;
        this.item = item;
        this.name = name;
        this.madeBy = madeBy;
        this.timestamp = Instant.now();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TicketUp.class.getSimpleName() + "[", "]")
                .add("orderId='" + orderId + "'")
                .add("lineItemId='" + lineItemId + "'")
                .add("item=" + item)
                .add("name='" + name + "'")
                .add("timestamp=" + timestamp)
                .add("madeBy='" + madeBy + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketUp ticketUp = (TicketUp) o;

        if (orderId != null ? !orderId.equals(ticketUp.orderId) : ticketUp.orderId != null) return false;
        if (lineItemId != null ? !lineItemId.equals(ticketUp.lineItemId) : ticketUp.lineItemId != null) return false;
        if (item != ticketUp.item) return false;
        if (name != null ? !name.equals(ticketUp.name) : ticketUp.name != null) return false;
        if (timestamp != null ? !timestamp.equals(ticketUp.timestamp) : ticketUp.timestamp != null) return false;
        return madeBy != null ? madeBy.equals(ticketUp.madeBy) : ticketUp.madeBy == null;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (lineItemId != null ? lineItemId.hashCode() : 0);
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (madeBy != null ? madeBy.hashCode() : 0);
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

    public String getMadeBy() {
        return madeBy;
    }
}
