package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public abstract class Appliance {
    protected final String id;
    protected String series;
    protected String model;
    protected BigDecimal price;

    public Appliance(String series, String model, BigDecimal price) {
        this.id = UUID.randomUUID().toString();
        this.series = series;
        this.model = model;
        this.price = price;
    }
}

