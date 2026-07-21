package io.quarkusdroneshop.qdca10pro.domain.valueobjects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.qdca10pro.domain.Item;

import java.util.StringJoiner;

/**
 * 欠品(eighty-six)発生時に eighty-six トピックへ publish するメッセージ。
 * 以前は品目名の文字列のみを送っていたため、どの注文・明細が欠品になったか
 * 下流(order-events-job.sql の ORDER_CANCELLED 生成)で特定できなかった。
 * orderId/lineItemId を含めることで、counter 側が該当明細を CANCELLED に
 * 更新できるようにする。
 */
@RegisterForReflection
public class EightySixMessage {

    private final String orderId;

    private final String lineItemId;

    private final Item item;

    public EightySixMessage(String orderId, String lineItemId, Item item) {
        this.orderId = orderId;
        this.lineItemId = lineItemId;
        this.item = item;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", EightySixMessage.class.getSimpleName() + "[", "]")
                .add("orderId='" + orderId + "'")
                .add("lineItemId='" + lineItemId + "'")
                .add("item=" + item)
                .toString();
    }
}
