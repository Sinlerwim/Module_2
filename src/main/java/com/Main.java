package com;

import com.model.*;
import com.service.Informer;
import com.service.PersonService;
import com.service.ShopService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;


public class Main {
    private static final ShopService SHOP_SERVICE = new ShopService();
    private static final PersonService PERSON_SERVICE = new PersonService();
    private static final int INVOICES_NUMBER = 15;
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Input retail limit:");
            Invoice.setRetailLimit(new BigDecimal(reader.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Customer customer;
        Invoice invoice;
        for (int i=0; i<INVOICES_NUMBER; i++) {
            customer = PERSON_SERVICE.getRandomCustomer();
            invoice = SHOP_SERVICE.generateRandomInvoice(customer);
            SHOP_SERVICE.saveInvoice(invoice);
        }
        Informer informer = new Informer(SHOP_SERVICE.getAllInvoices());
        informer.printProductsAmount();
        informer.printTheCheapestOrder();
        informer.printPriceOfAllOrders();
        informer.printNumberOfRetailOrders();
        informer.printOneTypeApplianceInvoices();
        informer.printFirstThreeInvoices();
        informer.printLowAgeCustomerInvoices();
        informer.printSortedInvoices();
    }
}
