package com.bullish.electronicstore.generator.impl;

import com.bullish.electronicstore.generator.CodeGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDCodeGenerator implements CodeGenerator<UUID> {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
