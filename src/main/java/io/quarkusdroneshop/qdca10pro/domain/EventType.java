package io.quarkusdroneshop.kitchen.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum EventType {
    BEVERAGE_ORDER_IN, BEVERAGE_ORDER_UP, EIGHTY_SIX, KITCHEN_ORDER_IN, KITCHEN_ORDER_UP, ORDER_PLACED, RESTOCK, NEW_ORDER
}
