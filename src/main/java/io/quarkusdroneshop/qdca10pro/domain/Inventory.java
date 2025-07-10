package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkusdroneshop.qdca10pro.domain.exceptions.EightySixException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@ApplicationScoped
public class Inventory {

    Logger logger = LoggerFactory.getLogger(Inventory.class.getName());

    static Map<Item, Integer> stock;

    public Inventory() {
        super();
    }

    /*
        QDC_A101 and DRONE_WITH_ROOM are simply tracked as QDC_A101
     */
    @PostConstruct
    private void createStock() {
        stock = new HashMap<>();
        Stream.of(Item.values()).forEach(v ->{
            stock.put(v, ThreadLocalRandom.current().nextInt(55,99));
        });
        stock.entrySet().stream().forEach(entrySet -> {
            logger.debug(entrySet.getKey() + " " + entrySet.getValue());
        });
    }

    public void decrementItem(Item item) throws EightySixException {
        if (item.equals(Item.QDC_A101) || item.equals(Item.QDC_A102)) {
        }else{
            Integer currentValue = stock.get(item);
            if(currentValue <= 0) throw new EightySixException(item);
            stock.replace(item, currentValue - 1);
        }
    }

    public Integer getItemCount(Item item) {
        return stock.get(item);
    }

    public Map<Item, Integer> getStock() {
        return stock;
    }
}
