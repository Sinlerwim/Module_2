package com.service;

import com.model.Invoice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
class ShopServiceTest {

    private ShopService target;
    Invoice mockedInvoice;

    @BeforeEach
    void setUp() {
        target = new ShopService();
        mockedInvoice= Mockito.mock(Invoice.class);
    }

    @AfterEach
    void tearDown() {
        target.clearInvoices();
    }

    @Test
    void saveInvoice() {
        target.saveInvoice(mockedInvoice);
        Assertions.assertEquals(1, target.getAllInvoices().size());
        Assertions.assertEquals(mockedInvoice, target.getAllInvoices().get(0));
    }

    @Test
    void getAllInvoices() {
        for (int i = 0; i < 5; i++)
            target.saveInvoice(mockedInvoice);
        List<Invoice> actual = target.getAllInvoices();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(5, actual.size());
    }
}