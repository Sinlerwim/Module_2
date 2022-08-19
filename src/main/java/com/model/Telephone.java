package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Telephone extends Appliance {
    protected String screenType;
    public Telephone(String series, String model, String screenType, BigDecimal price) {
        super(series, model, price);
        this.screenType = screenType;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "id=" + id +
                ", series=" + series +
                ", model=" + model +
                ", screenType=" + screenType +
                ", price=" + price;
    }
}
