package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.service.DateTimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DateTimeServiceImpl implements DateTimeService {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
