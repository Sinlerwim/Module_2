package com.service;

import com.model.Customer;

import java.util.Random;

public class PersonService {
    private static final int EMAIL_LENGTH = 8;

    public Customer getRandomCustomer() {
        final Random RANDOM = new Random();
        StringBuilder email = new StringBuilder();
        for (int i = 0; i < EMAIL_LENGTH; i++) {
            email.append((char) (RANDOM.nextInt(26) + 'a'));
        }
        email.append("@gmail.com");
        return new Customer(email.toString(), RANDOM.nextInt(12, 90));
    }
}
