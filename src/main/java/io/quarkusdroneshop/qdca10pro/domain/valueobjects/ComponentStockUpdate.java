package io.quarkusdroneshop.qdca10pro.domain.valueobjects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.qdca10pro.domain.Item;

import java.util.StringJoiner;

@RegisterForReflection
public class ComponentStockUpdate {

    private final Item item;

    private final long quantity;

    public ComponentStockUpdate(final Item item, final long quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ComponentStockUpdate.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("quantity=" + quantity)
                .toString();
    }
}
