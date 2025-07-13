package io.quarkusdroneshop.qdca10pro.domain.valueobjects;

import io.quarkusdroneshop.qdca10pro.domain.EightySixEvent;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderUp;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.Qdca10proResult;

public class Qdca10proResult {

    private OrderUp orderUp;

    private EightySixEvent eightySixEvent;

    private boolean isEightySixed;

    public Qdca10proResult(OrderUp orderUp) {
        this.orderUp = orderUp;
        this.isEightySixed = false;
    }

    public Qdca10proResult(EightySixEvent eightySixEvent) {
        this.eightySixEvent = eightySixEvent;
        this.isEightySixed = true;
    }

    public EightySixEvent getEightySixEvent() {
        return eightySixEvent;
    }

    public void setEightySixEvent(EightySixEvent eightySixEvent) {
        this.eightySixEvent = eightySixEvent;
    }

    public OrderUp getOrderUp() {
        return orderUp;
    }

    public void setOrderUp(OrderUp orderUp) {
        this.orderUp = orderUp;
    }

    public boolean isEightySixed() {
        return isEightySixed;
    }

    public void setEightySixed(boolean eightySixed) {
        isEightySixed = eightySixed;
    }
}