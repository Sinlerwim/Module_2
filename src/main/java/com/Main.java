package com;

import com.model.*;
import com.service.PersonService;
import com.service.ShopService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;


public class Main {
    private static final ShopService SHOP_SERVICE = new ShopService();
    private static final PersonService PERSON_SERVICE = new PersonService();
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Input retail limit:");
            Invoice.setRetailLimit(new BigDecimal(reader.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Customer customer;
        Invoice invoice;
        for (int i=0; i<15; i++) {
            customer = PERSON_SERVICE.getRandomCustomer();
            invoice = SHOP_SERVICE.generateRandomInvoice(customer);
            SHOP_SERVICE.saveInvoice(invoice);
        }
        List<Invoice> allInvoices = SHOP_SERVICE.getAllInvoices();
        System.out.println("Numbers of sold products:");
        System.out.println("Telephones - " + allInvoices.stream()
                .map(Invoice::getOrderList)
                .flatMap(List::stream)
                .filter(o -> o.getClass() == Telephone.class)
                .count());

        System.out.println("Television - " + allInvoices.stream()
                .map(Invoice::getOrderList)
                .flatMap(List::stream)
                .filter(o -> o.getClass() == Television.class)
                .count());

        System.out.println("The cheapest order:");
        Invoice theCheapestInvoice = allInvoices.stream()
                .min(Comparator.comparing(Invoice::getPriceOfOrder))
                .get();

        System.out.println("Price of order - " + theCheapestInvoice.getPriceOfOrder());
        System.out.println("Customer - " + theCheapestInvoice.getCustomer());
        System.out.println("Price of all orders - " + allInvoices.stream()
                .map(Invoice::getPriceOfOrder)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        System.out.println("Number of retail orders - " + allInvoices.stream()
                .filter(o -> o.getType() == InvoiceType.RETAIL)
                .count());

        System.out.println("Invoice with only one type of appliance:");
        allInvoices.stream()
                .filter(i -> {
                    List<Appliance> appliances = i.getOrderList();
                    Appliance generalAppliance = appliances.get(0);
                    for(Appliance appliance : appliances) {
                        if(generalAppliance.getClass() != appliance.getClass())
                            return false;
                    }
                    return true;
                })
                .forEach(System.out::println);
        System.out.println("First 3 orders:");
        allInvoices.stream()
                .sorted(Comparator.comparing(Invoice::getCreated))
                .limit(3)
                .forEach(System.out::println);
        System.out.println("Orders with low age customer:");
        allInvoices.stream()
                .filter(c-> c.getCustomer().getAge() < 18)
                .forEach(c-> {
                    c.setType(InvoiceType.LOW_AGE);           // Я так понял по заданию, что нужно изменять именно здесь
                    System.out.println(c);                    // по этому не вносил в конструктор
                });
        System.out.println("Sorted orders:");
        System.out.println(SHOP_SERVICE.getSortedInvoices());
    }
}
