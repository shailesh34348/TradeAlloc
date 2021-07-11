package com.app.trade.calc.engine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AllocationMath {
    private String account;
    private String stock;
    private String type;

    private double quantityHeld;
    private double TARGET_MARKET_VALUE;
    private double MAX_SHARES;
    private double ALL_IN_POSITION;
    private double SUGGESTED_FINAL_POSITION;
    private double SUGGESTED_TRADE_ALLOCATION;

    private long quantity;

    public AllocationMath(
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
        this.TARGET_MARKET_VALUE = targetMarketValue;
        this.MAX_SHARES = maxShares;
        this.ALL_IN_POSITION = allInPosition;
        this.SUGGESTED_FINAL_POSITION = suggestedFinalPosition;
        this.SUGGESTED_TRADE_ALLOCATION = suggestedTradeAllocation;
        this.quantity = quantity;
    }
}
