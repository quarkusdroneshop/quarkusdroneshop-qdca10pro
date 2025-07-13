package io.quarkusdroneshop.qdca10pro.infrastructure;

import io.quarkusdroneshop.qdca10pro.domain.qdca10pro;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.qdca10pro.domain.valueobjects.Qdca10proResult;

import java.util.function.Supplier;

public class Qdca10proTask implements Supplier<Qdca10proResult> {
    private final qdca10pro qdca10pro;
    private final OrderIn orderIn;

    public Qdca10proTask(qdca10pro qdca10pro, OrderIn orderIn) {
        this.qdca10pro = qdca10pro;
        this.orderIn = orderIn;
    }

    @Override
    public Qdca10proResult get() {
        return qdca10pro.make(orderIn);
    }
}