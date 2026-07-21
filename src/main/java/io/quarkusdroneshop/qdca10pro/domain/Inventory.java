package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkusdroneshop.qdca10pro.domain.exceptions.EightySixException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.EnumMap;
import java.util.Map;

@ApplicationScoped
public class Inventory {

    Logger logger = LoggerFactory.getLogger(Inventory.class.getName());

    // drone-component-stock データプロダクト (dataproduct-component-stock-quantity) から
    // 受信するまでは在庫 0 (=売り切れ) として扱う (ランダムな初期値を捏造しない)。
    private final Map<Item, Integer> stock = new EnumMap<>(Item.class);

    public synchronized void decrementItem(Item item) throws EightySixException {
        Integer currentValue = stock.get(item);
        if (currentValue == null || currentValue <= 0) throw new EightySixException(item);
        stock.put(item, currentValue - 1);
    }

    // component-stock-decrement で下流 (在庫サービス) へ実消費を反映できるよう、
    // 減算後の絶対数を呼び出し側 (KafkaService) へ返す。
    public synchronized Integer getItemCountOrNull(Item item) {
        return stock.get(item);
    }

    // drone-component-stock からの補充完了通知 (item 単位の絶対数, upsert) を
    // 受信するたびに呼ばれる。直近の値でそのまま置き換える。
    public synchronized void restockItem(Item item, int quantity) {
        stock.put(item, quantity);
    }

    public synchronized Integer getItemCount(Item item) {
        return stock.getOrDefault(item, 0);
    }

    public synchronized Map<Item, Integer> getStock() {
        return new EnumMap<>(stock);
    }
}
