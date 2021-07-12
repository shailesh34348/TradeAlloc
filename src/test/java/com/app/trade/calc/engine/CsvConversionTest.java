package com.app.trade.calc.engine;

import com.app.trade.calc.engine.model.Capital;
import com.app.trade.calc.engine.model.Holding;
import com.app.trade.calc.engine.model.Target;
import com.app.trade.calc.engine.model.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class CsvConversionTest extends AbstractTestMethod {

    @Test
    public void testTradeCSVConversion() {

        List<Trade> trades = this.convertTradeCsvToEntity();
        for (Trade trade : trades) {
            System.out.println("Stock: " +trade.getStock()+ " Type: " +trade.getType() + " Quantity: " + trade.getQuantity() + " Price: " + trade.getPrice());
        }
    }

    @Test
    public void testCapitalCSVConversion() {

        List<Capital> capitalList = this.convertCapitalCsvToEntity();
        for (Capital capital : capitalList) {
            System.out.println("Account: " + capital.getAccount() + " Capital: " + capital.getCapital());
        }
    }

    @Test
    public void testTargetCSVConversion() {

        List<Target> targetList = this.convertTargetCsvToEntity();
        for (Target target : targetList) {
            System.out.println("Stock: " +target.getStock() + " percent: " +target.getPercent());
        }
    }

    @Test
    public void testHoldingCSVConversion() {
        List<Holding> holdingList = this.convertHoldingCsvToEntity();
        for (Holding holding : holdingList) {
            System.out.println("Account: " +holding.getAccount() + " Stock: " +holding.getStock() + " Quantity: " +holding.getQuantity() + " Price: " +holding.getPrice() + " Market Value: " +holding.getMarketValue());
        }
    }
}