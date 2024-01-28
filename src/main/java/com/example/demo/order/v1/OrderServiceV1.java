package com.example.demo.order.v1;

import com.example.demo.order.OrderService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
//@Service
public class OrderServiceV1 implements OrderService {

    private final MeterRegistry meterRegistry;
    private AtomicInteger stock = new AtomicInteger(100);


    public OrderServiceV1(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();
        Counter counter = Counter.builder("my.order")
                .tag("class", this.getClass().getName())
                .tag("method", "order")
                .description("order")
                .register(meterRegistry);
        counter.increment();
    }

    @Override
    public void cancel() {
        log.info("주문");
        stock.incrementAndGet();
        Counter counter = Counter.builder("my.order")
                .tag("class", this.getClass().getName())
                .tag("method", "cancel")
                .description("order cancel")
                .register(meterRegistry);
        counter.increment();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
