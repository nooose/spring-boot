package com.example.demo.order.v2;

import com.example.demo.order.OrderService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class OrderServiceV2 implements OrderService {

    private AtomicInteger stock = new AtomicInteger(100);

    @Counted("my.order")
    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();
    }

    @Counted("my.order")
    @Override
    public void cancel() {
        log.info("주문");
        stock.incrementAndGet();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
