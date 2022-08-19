package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
public class Invoice {
    private static BigDecimal RETAIL_LIMIT;
    List<Appliance> orderList;
    Customer customer;
    InvoiceType type;
    Calendar created;
    BigDecimal priceOfOrder;

    public Invoice(List<Appliance> orderList, Customer customer) {
        this.orderList = orderList;
        this.customer = customer;
        this.created = Calendar.getInstance();
        this.priceOfOrder = calculatePrice();
        if (priceOfOrder.compareTo(RETAIL_LIMIT) < 0) {
            this.type = InvoiceType.RETAIL;
        } else this.type = InvoiceType.WHOLESALE;
    }

    private BigDecimal calculatePrice() {
        return orderList.stream()
                .map(Appliance::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "orderList=" + orderList +
                ",\n customer=" + customer +
                ",\n type=" + type +
                ", created=" + created.getTime() +
                ", priceOfOrder=" + priceOfOrder +
                "}";
    }

    public static void setRetailLimit(BigDecimal retailLimit) {
        if (RETAIL_LIMIT == null)
            RETAIL_LIMIT = retailLimit;
    }
}
