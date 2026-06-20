package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.qdca10pro.domain.Inventory;
import io.quarkusdroneshop.qdca10pro.domain.Item;
import io.quarkusdroneshop.qdca10pro.domain.Qdca10pro;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.Qdca10proResult;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class Qdca10proTaskTest {

    @Inject
    Qdca10pro qdca10pro;

    @Inject
    Inventory inventory;

    @Test
    void testQdca10proTask_get_orderUp() {
        // Pro02 は 3s delay (最短)
        OrderIn orderIn = new OrderIn(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Item.QDC_A105_Pro02,
                "task-test-pro02");
        Qdca10proTask task = new Qdca10proTask(qdca10pro, orderIn);
        Qdca10proResult result = task.get();
        assertNotNull(result);
        assertFalse(result.isEightySixed());
        assertNotNull(result.getOrderUp());
    }

    @Test
    void testQdca10proTask_get_eightySix() {
        Integer count = inventory.getItemCount(Item.QDC_A105_Pro03);
        if (count == null) count = 0;
        for (int i = 0; i < count; i++) {
            try { inventory.decrementItem(Item.QDC_A105_Pro03); } catch (Exception ignored) {}
        }
        OrderIn orderIn = new OrderIn(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Item.QDC_A105_Pro03,
                "task-test-86");
        Qdca10proTask task = new Qdca10proTask(qdca10pro, orderIn);
        Qdca10proResult result = task.get();
        assertNotNull(result);
        assertTrue(result.isEightySixed());
    }
}
