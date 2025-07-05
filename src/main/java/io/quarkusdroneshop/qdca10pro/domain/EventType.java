package io.quarkusdroneshop.qdca10pro.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum EventType {
    BEVERAGE_ORDER_IN, BEVERAGE_ORDER_UP, EIGHTY_SIX, QDCA10Pro_ORDER_IN, QDCA10Pro_ORDER_UP, ORDER_PLACED, RESTOCK, NEW_ORDER
}
