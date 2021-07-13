package com.app.trade.calc.engine.util;

import com.app.trade.calc.engine.model.Capital;
import com.app.trade.calc.engine.model.Holding;
import com.app.trade.calc.engine.model.Target;
import com.app.trade.calc.engine.model.Trade;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import java.util.Map;

public class CsvMappingUtil {
    private static final Map<String, String> TRADE_CSV_MAP = Map.of("Stock", "stock", "Type", "type", "Quantity", "quantity", "Price", "price");
    private static final Map<String, String> CAPITAL_CSV_MAP = Map.of("Account", "account", "Capital", "capital");
    private static final Map<String, String> TARGET_CSV_MAP = Map.of("Stock", "stock","Account", "account", "target_percent", "percent");
    private static final Map<String, String> HOLDING_CSV_MAP = Map.of("Account", "account", "Stock", "Stock","Quantity", "quantity","Price", "price","Market Value", "marketValue");

    public static HeaderColumnNameTranslateMappingStrategy<Trade> getTradeCSVMapping() {
        HeaderColumnNameTranslateMappingStrategy<Trade> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
        strategy.setType(Trade.class);
        strategy.setColumnMapping(TRADE_CSV_MAP);
        return strategy;
    }

    public static HeaderColumnNameTranslateMappingStrategy<Capital> getCapitalCSVMapping() {
        HeaderColumnNameTranslateMappingStrategy<Capital> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
        strategy.setType(Capital.class);
        strategy.setColumnMapping(CAPITAL_CSV_MAP);
        return strategy;
    }

    public static HeaderColumnNameTranslateMappingStrategy<Target> getTargetCSVMapping() {
        HeaderColumnNameTranslateMappingStrategy<Target> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
        strategy.setType(Target.class);
        strategy.setColumnMapping(TARGET_CSV_MAP);
        return strategy;
    }

    public static HeaderColumnNameTranslateMappingStrategy<Holding> getHoldingCSVMapping() {
        HeaderColumnNameTranslateMappingStrategy<Holding> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
        strategy.setType(Holding.class);
        strategy.setColumnMapping(HOLDING_CSV_MAP);
        return strategy;
    }
}
