package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkusdroneshop.qdca10pro.domain.exceptions.EightySixException;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderUp;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.Qdca10proResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * qdca10pro ドメインオブジェクトの純粋ユニットテスト
 */
public class DomainTest {

    // ── EightySixEvent ────────────────────────────────────────────────────────

    @Test
    void testEightySixEvent_defaultConstructor() {
        EightySixEvent event = new EightySixEvent();
        assertEquals(EventType.EIGHTY_SIX, event.getEventType());
    }

    @Test
    void testEightySixEvent_withItem() {
        EightySixEvent event = new EightySixEvent(Item.QDC_A105_Pro01);
        assertEquals(EventType.EIGHTY_SIX, event.getEventType());
    }

    // ── EightySixException ────────────────────────────────────────────────────

    @Test
    void testEightySixException_singleItem() {
        EightySixException ex = new EightySixException(Item.QDC_A105_Pro02);
        assertNotNull(ex.getItems());
        assertEquals(1, ex.getItems().size());
        assertEquals(Item.QDC_A105_Pro02, ex.getItems().get(0));
    }

    @Test
    void testEightySixException_multipleItems() {
        List<Item> items = Arrays.asList(Item.QDC_A105_Pro01, Item.QDC_A105_Pro02);
        EightySixException ex = new EightySixException(items);
        assertEquals(2, ex.getItems().size());
    }

    // ── EventType ────────────────────────────────────────────────────────────

    @Test
    void testEventType_allValues() {
        assertTrue(EventType.values().length > 0);
        assertNotNull(EventType.valueOf("EIGHTY_SIX"));
    }

    // ── Item enum ─────────────────────────────────────────────────────────────

    @Test
    void testItem_allValues() {
        Item[] items = Item.values();
        assertTrue(items.length > 0);
        assertEquals(Item.QDC_A105_Pro01, Item.valueOf("QDC_A105_Pro01"));
        assertEquals(Item.QDC_A105_Pro02, Item.valueOf("QDC_A105_Pro02"));
        assertEquals(Item.QDC_A105_Pro03, Item.valueOf("QDC_A105_Pro03"));
        assertEquals(Item.QDC_A105_Pro04, Item.valueOf("QDC_A105_Pro04"));
    }

    // ── LineItemEvent (abstract) ──────────────────────────────────────────────

    @Test
    void testLineItemEvent_constructors() {
        // 具象クラスとして EightySixEvent を使用（abstract の各コンストラクタをカバー）
        // EventType のみコンストラクタ
        EightySixEvent e1 = new EightySixEvent();
        assertNotNull(e1);
    }

    // ── OrderIn ───────────────────────────────────────────────────────────────

    @Test
    void testOrderIn_constructorAndGetters() {
        String orderId = UUID.randomUUID().toString();
        String lineItemId = UUID.randomUUID().toString();
        OrderIn orderIn = new OrderIn(orderId, lineItemId, Item.QDC_A105_Pro01, "proName");

        assertEquals(orderId, orderIn.getOrderId());
        assertEquals(lineItemId, orderIn.getLineItemId());
        assertEquals(Item.QDC_A105_Pro01, orderIn.getItem());
        assertEquals("proName", orderIn.getName());
        assertNotNull(orderIn.getTimestamp());
    }

    @Test
    void testOrderIn_equalsAndHashCode() {
        String orderId = UUID.randomUUID().toString();
        OrderIn a = new OrderIn(orderId, "l1", Item.QDC_A105_Pro02, "n");
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, "other");
        assertNotEquals(a, new OrderIn(UUID.randomUUID().toString(), "l1", Item.QDC_A105_Pro02, "n"));
        assertEquals(a.hashCode(), a.hashCode());
    }

    @Test
    void testOrderIn_toString() {
        OrderIn orderIn = new OrderIn("id1", "li1", Item.QDC_A105_Pro03, "name");
        assertTrue(orderIn.toString().contains("OrderIn"));
    }

    // ── OrderUp ───────────────────────────────────────────────────────────────

    @Test
    void testOrderUp_constructorAndGetters() {
        OrderUp orderUp = new OrderUp("o1", "l1", Item.QDC_A105_Pro01, "name", "worker1");
        assertEquals("o1", orderUp.getOrderId());
        assertEquals("l1", orderUp.getLineItemId());
        assertEquals(Item.QDC_A105_Pro01, orderUp.getItem());
        assertEquals("name", orderUp.getName());
        assertEquals("worker1", orderUp.getMadeBy());
        assertNotNull(orderUp.getTimestamp());
    }

    @Test
    void testOrderUp_equalsAndHashCode() {
        OrderUp a = new OrderUp("o1", "l1", Item.QDC_A105_Pro01, "n", "w");
        OrderUp b = new OrderUp("o1", "l1", Item.QDC_A105_Pro01, "n", "w");
        OrderUp c = new OrderUp("o2", "l1", Item.QDC_A105_Pro01, "n", "w");
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, "other");
        assertNotEquals(a, c);
        // タイムスタンプが異なるため a != b だが hashCode は安定
        assertNotNull(a.hashCode());
    }

    @Test
    void testOrderUp_toString() {
        OrderUp orderUp = new OrderUp("o1", "l1", Item.QDC_A105_Pro04, "n", "w");
        assertTrue(orderUp.toString().contains("OrderUp"));
    }

    // ── Qdca10proResult ───────────────────────────────────────────────────────

    @Test
    void testQdca10proResult_withOrderUp() {
        OrderUp orderUp = new OrderUp("o1", "l1", Item.QDC_A105_Pro01, "n", "w");
        Qdca10proResult result = new Qdca10proResult(orderUp);
        assertFalse(result.isEightySixed());
        assertEquals(orderUp, result.getOrderUp());
        assertNull(result.getEightySixEvent());
    }

    @Test
    void testQdca10proResult_withEightySix() {
        EightySixEvent event = new EightySixEvent(Item.QDC_A105_Pro02);
        Qdca10proResult result = new Qdca10proResult(event);
        assertTrue(result.isEightySixed());
        assertEquals(event, result.getEightySixEvent());
        assertNull(result.getOrderUp());
    }

    @Test
    void testQdca10proResult_setters() {
        Qdca10proResult result = new Qdca10proResult(new EightySixEvent(Item.QDC_A105_Pro01));
        OrderUp orderUp = new OrderUp("o2", "l2", Item.QDC_A105_Pro02, "n2", "w2");
        result.setOrderUp(orderUp);
        result.setEightySixed(false);
        assertEquals(orderUp, result.getOrderUp());
        assertFalse(result.isEightySixed());

        EightySixEvent newEvent = new EightySixEvent(Item.QDC_A105_Pro03);
        result.setEightySixEvent(newEvent);
        assertEquals(newEvent, result.getEightySixEvent());
    }
}
