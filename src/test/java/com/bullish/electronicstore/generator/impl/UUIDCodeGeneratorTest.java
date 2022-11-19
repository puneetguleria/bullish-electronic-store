package com.bullish.electronicstore.generator.impl;

import com.bullish.electronicstore.generator.CodeGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UUIDCodeGeneratorTest {
    private CodeGenerator codeGenerator = new UUIDCodeGenerator();

    @Test
    @DisplayName("Should generate random UUID everytime")
    void shouldGenerateRandomUUIDEverytime() {
        var attempt1 = codeGenerator.generate();
        var attempt2 = codeGenerator.generate();
        var attempt3 = codeGenerator.generate();

        assertAll(
                "Check all are unique",
                () -> assertNotEquals(attempt1, attempt2),
                () -> assertNotEquals(attempt2, attempt3),
                () -> assertNotEquals(attempt3, attempt1)
        );
    }

}