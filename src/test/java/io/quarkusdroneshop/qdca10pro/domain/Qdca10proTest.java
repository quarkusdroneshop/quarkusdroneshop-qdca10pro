package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.Qdca10proResult;
import org.junit.jupiter.api.*;

import jakarta.inject.Inject;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Qdca10pro.make() の全分岐 (calculateDelay + 86 パス) をカバーする
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Qdca10proTest {

    @Inject
    Qdca10pro qdca10pro;

    @Inject
    Inventory inventory;

    private OrderIn makeOrder(Item item) {
        return new OrderIn(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                item,
                "pro-test-" + item.name());
    }

    @Test @Order(1)
    public void testMake_QDC_A105_Pro01() {
        // delay = 5s
        Qdca10proResult result = qdca10pro.make(makeOrder(Item.QDC_A105_Pro01));
        assertFalse(result.isEightySixed());
        assertNotNull(result.getOrderUp());
        assertEquals(Item.QDC_A105_Pro01, result.getOrderUp().getItem());
    }

    @Test @Order(2)
    public void testMake_QDC_A105_Pro02() {
        // delay = 3s
        Qdca10proResult result = qdca10pro.make(makeOrder(Item.QDC_A105_Pro02));
        assertFalse(result.isEightySixed());
        assertNotNull(result.getOrderUp());
    }

    @Test @Order(3)
    public void testMake_QDC_A105_Pro03() {
        // delay = 5s
        Qdca10proResult result = qdca10pro.make(makeOrder(Item.QDC_A105_Pro03));
        assertFalse(result.isEightySixed());
        assertNotNull(result.getOrderUp());
    }

    @Test @Order(4)
    public void testMake_QDC_A105_Pro04() {
        // delay = 7s
        Qdca10proResult result = qdca10pro.make(makeOrder(Item.QDC_A105_Pro04));
        assertFalse(result.isEightySixed());
        assertNotNull(result.getOrderUp());
    }

    @Test @Order(5)
    public void testMake_eightySix_path() {
        // 在庫を枯渇させて 86 パスを確認 (sleepなし)
        Integer count = inventory.getItemCount(Item.QDC_A105_Pro01);
        if (count == null) count = 0;
        for (int i = 0; i < count; i++) {
            try { inventory.decrementItem(Item.QDC_A105_Pro01); } catch (Exception ignored) {}
        }
        Qdca10proResult result = qdca10pro.make(makeOrder(Item.QDC_A105_Pro01));
        assertTrue(result.isEightySixed());
        assertNotNull(result.getEightySixEvent());
        assertNull(result.getOrderUp());
    }
}
