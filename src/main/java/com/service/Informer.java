package com.service;

import com.model.Invoice;

import java.util.List;

public class Informer {
    private Handler handler;
    
    public Informer(List<Invoice> invoices) {
        this.handler = new Handler(invoices);
    }

    public void printProductsAmount() {
        System.out.println("Numbers of sold products:");
        System.out.println("Telephones - " + handler.getTelephonesAmount());
        System.out.println("Television - " + handler.getTelevisionAmount());
    }

    public void printTheCheapestOrder() {
        System.out.println("The cheapest order:");
        handler.getTheCheapestInvoice()
                .ifPresent(i -> {
                    System.out.println("Price of order - " + i.getPriceOfOrder());
                    System.out.println("Customer - " + i.getCustomer());
                });
    }

    public void printPriceOfAllOrders() {
        System.out.println("Price of all orders - " + handler.getPriceOfAllOrders());
    }

    public void printNumberOfRetailOrders() {
        System.out.println("Number of retail orders - " + handler.getNumberOfRetailOrders());
    }

    public void printOneTypeApplianceInvoices() {
        System.out.println("Orders with only one type of appliance:\n" + handler.getListOfOneTypeAppliance());
    }

    public void printFirstThreeInvoices() {
        System.out.println("First three orders:\n" + handler.getFirstThreeInvoices());
    }

    public void printLowAgeCustomerInvoices() {
        System.out.println(handler.getLowAgeCustomerInvoices());
    }

    public void printSortedInvoices() {
        System.out.println("Sorted invoices:\n" + handler.getSortedInvoices());
    }
}
