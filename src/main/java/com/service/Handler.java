package com.service;

import com.model.*;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
public class Handler {
    private List<Invoice> handlingInvoices;

    public Handler(List<Invoice> handlingInvoices) {
        this.handlingInvoices = handlingInvoices;
    }

    public int getTelevisionAmount() {
        return (int) handlingInvoices.stream()
                .map(Invoice::getOrderList)
                .flatMap(List::stream)
                .filter(o -> o.getClass() == Television.class)
                .count();
    }

    public int getTelephonesAmount() {
        return (int) handlingInvoices.stream()
                .map(Invoice::getOrderList)
                .flatMap(List::stream)
                .filter(o -> o.getClass() == Telephone.class)
                .count();
    }

    public Optional<Invoice> getTheCheapestInvoice() {
        return handlingInvoices.stream()
                .min(Comparator.comparing(Invoice::getPriceOfOrder));
    }

    public BigDecimal getPriceOfAllOrders() {
        return handlingInvoices.stream()
                .map(Invoice::getPriceOfOrder)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getNumberOfRetailOrders() {
        return (int) handlingInvoices.stream()
                .filter(o -> o.getType() == InvoiceType.RETAIL)
                .count();
    }

    public List<Invoice> getListOfOneTypeAppliance() {
        return handlingInvoices.stream()
                .filter(i -> {
                    List<Appliance> appliances = i.getOrderList();
                    Appliance generalAppliance = appliances.get(0);
                    for(Appliance appliance : appliances) {
                        if(generalAppliance.getClass() != appliance.getClass())
                            return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public List<Invoice> getFirstThreeInvoices() {
        return handlingInvoices.stream()
                .sorted(Comparator.comparing(Invoice::getCreated))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Invoice> getLowAgeCustomerInvoices() {
        return handlingInvoices.stream()
                .filter(c-> c.getCustomer().getAge() < 18)
                .peek(c-> c.setType(InvoiceType.LOW_AGE))
                .toList();
    }

    public List<Invoice> getSortedInvoices() {
        return handlingInvoices.stream()
                .sorted(Comparator.comparing(Invoice::getCustomer, Comparator.comparingInt(Customer::getAge).reversed())
                .thenComparingInt(i-> i.getOrderList().size())
                .thenComparing(Invoice::getPriceOfOrder, BigDecimal::compareTo))
                .toList();
    }
}
