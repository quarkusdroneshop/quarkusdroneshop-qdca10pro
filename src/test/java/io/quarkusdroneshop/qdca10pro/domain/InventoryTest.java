package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.qdca10pro.domain.exceptions.EightySixException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.inject.Inject;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryTest {

    @Inject
    Inventory inventory;

    @Test @Order(1)
    public void testStockIsPopulated() {
        Map<Item, Integer> inStock = inventory.getStock();
        assertNotNull(inStock);
        assertFalse(inStock.isEmpty());
    }

    @Test @Order(2)
    public void testGetItemCount() {
        Integer count = inventory.getItemCount(Item.QDC_A105_Pro01);
        assertNotNull(count);
        assertTrue(count >= 0);
    }

    @Test @Order(3)
    public void testDecrementItem_success() throws EightySixException {
        Integer before = inventory.getItemCount(Item.QDC_A105_Pro02);
        assertNotNull(before);
        assertTrue(before > 0);
        inventory.decrementItem(Item.QDC_A105_Pro02);
        Integer after = inventory.getItemCount(Item.QDC_A105_Pro02);
        assertEquals(before - 1, after);
    }

    @Test @Order(4)
    public void testDecrementItem_throwsEightySixException() {
        Integer count = inventory.getItemCount(Item.QDC_A105_Pro04);
        if (count == null) count = 0;
        for (int i = 0; i < count; i++) {
            try { inventory.decrementItem(Item.QDC_A105_Pro04); } catch (EightySixException ignored) {}
        }
        assertThrows(EightySixException.class, () -> inventory.decrementItem(Item.QDC_A105_Pro04));
    }
}
