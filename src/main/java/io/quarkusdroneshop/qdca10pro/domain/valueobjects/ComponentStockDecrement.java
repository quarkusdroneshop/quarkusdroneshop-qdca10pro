package io.quarkusdroneshop.qdca10pro.domain.valueobjects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.qdca10pro.domain.Item;

import java.util.StringJoiner;

/**
 * 注文消費による在庫減算後の絶対数量を component-stock-decrement トピックへ publish する
 * メッセージ。drone-component-stock データプロダクトが component_stock_quantity
 * (dataproduct-component-stock-quantity, upsert-kafka) へこの値をそのまま反映することで、
 * 補充時だけでなく消費時にも在庫サービス (quarkusdroneshop-inventory) 側の
 * inStockQuantity が実際の消費に追従するようにする。
 */
@RegisterForReflection
public class ComponentStockDecrement {

    private final Item item;

    private final long quantity;

    public ComponentStockDecrement(Item item, long quantity) {
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
        return new StringJoiner(", ", ComponentStockDecrement.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("quantity=" + quantity)
                .toString();
    }
}
