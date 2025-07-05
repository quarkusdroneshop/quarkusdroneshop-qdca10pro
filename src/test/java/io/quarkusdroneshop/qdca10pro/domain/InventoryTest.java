package io.quarkusdroneshop.kitchen.domain;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.kitchen.domain.exceptions.EightySixException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryTest {

    @Inject
    Inventory inventory;

    @Test @Order(1)
    public void testStockIsPopulated() {

        Map<Item, Integer> inStock = inventory.getStock();
        assertNotNull(inStock);
        inStock.forEach((k,v) -> {
            System.out.println(k + " " + v);
        });
    }

    @Test @Order(2)
    public void testEightySixQDCA105Pro01() {

        Integer itemCount = inventory.getItemCount(Item.QDC_A105_Pro01);
        for (int i = 0; i < itemCount; i++) {
            try {
                inventory.decrementItem(Item.QDC_A101);
            } catch (Exception e) {
                assertEquals(EightySixException.class, e.getClass());
                assertEquals(itemCount, Integer.valueOf(i));
            }
        }
    }
}
