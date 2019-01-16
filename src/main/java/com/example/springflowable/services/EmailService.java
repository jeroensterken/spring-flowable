package com.example.springflowable.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class EmailService {
    public Map<String, AtomicInteger> sends = new ConcurrentHashMap<>();

    public void sendWelcomeEmail (String customerId, String email) {
        log.info("** sending welcome email for "+customerId+ " to "+ email);

        sends.computeIfAbsent(email, e -> new AtomicInteger());
        sends.get(email).incrementAndGet();
    }
}
