package com.app.trade.calc.engine.service;

import com.app.trade.calc.engine.domain.Allocation;
import com.app.trade.calc.engine.model.Capital;
import com.app.trade.calc.engine.repository.CapitalRepo;
import com.app.trade.calc.engine.model.Holding;
import com.app.trade.calc.engine.model.Target;
import com.app.trade.calc.engine.model.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class CsvConversionTest extends AbstractTestMethod {

    @Autowired
    CapitalRepo capitalRepo;

    @Autowired
    CaptialService captialService;

    @Autowired
    AllocationService tradeAllocatorService;

    @Test
    public void testTradeCSVConversion() {

        List<Trade> trades = this.convertTradeCsvToEntity();
        // print details of Bean object
        for (Trade trade : trades) {
            System.out.println("Stock: " +trade.getStock()+ " Type: " +trade.getType() + " Quantity: " + trade.getQuantity() + " Price: " + trade.getPrice());
        }
    }

    @Test
    public void testCapitalCSVConversion() {

        List<Capital> capitalList = this.convertCapitalCsvToEntity();
        // print details of Bean object
        for (Capital capital : capitalList) {
            System.out.println("Account: " + capital.getAccount() + " Captial: " + capital.getCapital());
        }
    }

    @Test
    public void testTargetCSVConversion() {

        List<Target> targetList = this.convertTargetCsvToEntity();
        // print details of Bean object
        for (Target target : targetList) {
            System.out.println("Stock: " +target.getStock() + " Account: " +target.getCapital().getAccount() + " percent: " +target.getPercent());
        }
    }

    @Test
    public void testHoldingCSVConversion() {
        List<Holding> holdingList = this.convertHoldingCsvToEntity();
        for (Holding holding : holdingList) {
            System.out.println("Account: " +holding.getAccount() + " Stock: " +holding.getStock() + " Quantity: " +holding.getQuantity() + " Price: " +holding.getPrice() + " Market Value: " +holding.getMarketValue());
        }
    }

    @Test
    public void testRelationship() {
        this.loadDataInDB();

        List<Capital> capitalList = this.capitalRepo.findAll();
        System.out.println(capitalList.size());


        List<Allocation> allocationList = this.tradeAllocatorService.calcTradeAllocation();
        System.out.println(allocationList.size());
    }

    @Test
    public void testFind() {
        this.loadDataInDB();
        List<Capital> capitalList = this.captialService.findAllCapitalByStock("GOOGLE");
        //List<Capital> capitalList = this.tradeAllocatorService.findByHoldingListStock("GOOGLE");
        System.out.println(capitalList.size());
    }
}
