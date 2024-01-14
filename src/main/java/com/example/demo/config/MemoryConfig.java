package com.example.demo.config;

import com.memory.MemoryController;
import com.memory.MemoryFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Conditional(MemoryCondition.class)
@ConditionalOnProperty(name = "memory", havingValue = "on")
@Configuration(proxyBeanMethods = false)
public class MemoryConfig {

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    private MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }
}
