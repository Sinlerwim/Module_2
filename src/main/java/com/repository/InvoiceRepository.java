package com.repository;

import com.model.Customer;
import com.model.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InvoiceRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceRepository.class);

    private final List<Invoice> invoices;


    public InvoiceRepository() {
        invoices = new LinkedList<>();
    }

    public boolean save(Invoice invoice) {
        invoices.add(invoice);
        LOGGER.info("[{}][Items:{} Total price:{} Type:{}]", invoice.getCustomer(), invoice.getOrderList(),
                invoice.getPriceOfOrder(), invoice.getType());
        return true;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void clear() {
        invoices.clear();
    }
}
