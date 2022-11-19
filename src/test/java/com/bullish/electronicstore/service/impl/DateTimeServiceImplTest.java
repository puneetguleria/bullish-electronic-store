package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.service.DateTimeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeServiceImplTest {

    private DateTimeService dateTimeService = new DateTimeServiceImpl();

    @Test
    @DisplayName("Should generate current date")
    void shouldGenerateCurrentDate() {
        var dateTime = dateTimeService.now();
        LocalDateTime now = LocalDateTime.now();
        assertTrue(now.equals(dateTime) || now.isAfter(dateTime));
        assertTrue(now.minusSeconds(1).isBefore(dateTime));
    }

}