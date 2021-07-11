package com.app.trade.calc.engine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Allocation {

    private String account;
    private String stock;
    private long quantity;

    public Allocation(
            String account,
            String stock,
            long quantity) {
        this.account = account;
        this.stock = stock;
        this.quantity = quantity;
    }
}
