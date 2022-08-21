package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Television extends Appliance {
    protected Float diagonal;
    protected String country;
    protected String screenType;

    public Television(String series, float diagonal, String model, String screenType, String country, BigDecimal price) {
        super(series, model, price);
        this.diagonal = diagonal;
        this.country = country;
        this.screenType = screenType;
    }

    @Override
    public String toString() {
        return "Television{" +
                "id='" + id + '\'' +
                ", series='" + series + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", diagonal=" + diagonal +
                ", country='" + country + '\'' +
                ", screenType='" + screenType + '\'' +
                '}';
    }
}
