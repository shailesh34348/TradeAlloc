package com.app.trade.calc.engine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AllocationMetric {
    private String account;
    private String stock;
    private String type;

    private double quantityHeld;
    private double targetMarketValue;
    private double maxShares;
    private double allInPosition;
    private double suggestedFinalPosition;
    private double suggestedTradeAllocation;

    private long quantity;

    public AllocationMetric(
            String account,
            String stock,
            String type,
            double quantityHeld,
            double targetMarketValue,
            double maxShares,
            double allInPosition,
            double suggestedFinalPosition,
            double suggestedTradeAllocation,
            long quantity) {

        this.account = account;
        this.stock = stock;
        this.type = type;
        this.quantityHeld = quantityHeld;
        this.targetMarketValue = targetMarketValue;
        this.maxShares = maxShares;
        this.allInPosition = allInPosition;
        this.suggestedFinalPosition = suggestedFinalPosition;
        this.suggestedTradeAllocation = suggestedTradeAllocation;
        this.quantity = quantity;
    }
}
