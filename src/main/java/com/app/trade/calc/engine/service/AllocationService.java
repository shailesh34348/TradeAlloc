package com.app.trade.calc.engine.service;

import com.app.trade.calc.engine.domain.Allocation;
import com.app.trade.calc.engine.domain.AllocationMath;
import com.app.trade.calc.engine.model.Capital;
import com.app.trade.calc.engine.model.Holding;
import com.app.trade.calc.engine.model.Trade;
import com.app.trade.calc.engine.repository.TradeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AllocationService {

    private CaptialService captialService;
    private HoldingService holdingService;
    private TradeRepo tradeRepo;

    @Autowired
    public AllocationService(
            CaptialService captialService,
            HoldingService holdingService,
            TradeRepo tradeRepo) {
        this.captialService = captialService;
        this.holdingService = holdingService;
        this.tradeRepo = tradeRepo;
    }

    private AllocationMath calcAllocationMath(
            Double totalHoldingQuantityByStock,
            Double totalTargetMarketValue,
            Trade trade,
            Holding holding,
            Capital capital) {
        log.info("Inside method: calcAllocationMath in class: AllocationService -- Start");

        log.info(String.format("Allocation Math in progress for Account: %s and Stock: %s",
                capital.getAccount(),
                trade.getStock()));

        double targetMarketValue = capital.getTargetMarketValue();
        double maxShares = targetMarketValue / holding.getPrice();
        double allInPosition = totalHoldingQuantityByStock;

        //
        if (trade.getType().equalsIgnoreCase("BUY")) {
            allInPosition += trade.getQuantity();
        } else {
            allInPosition -= trade.getQuantity();
        }

        double suggestedFinalPosition = (allInPosition * targetMarketValue) / totalTargetMarketValue;
        double suggestedTradeAllocation = suggestedFinalPosition - holding.getQuantity();
        long allocationQuantity = Math.round(suggestedTradeAllocation);

        AllocationMath allocationMath = new AllocationMath(
                capital.getAccount(),
                holding.getStock(),
                trade.getType(),
                holding.getQuantity(),
                targetMarketValue,
                maxShares,
                allInPosition,
                suggestedFinalPosition,
                suggestedTradeAllocation,
                allocationQuantity);

        log.info("Inside method: calcAllocationMath in class: AllocationService -- End");
        return allocationMath;
    }

    private List<Allocation> createAllocationData(List<AllocationMath> allocationMathList) {

        log.info("Inside method: createAllocationData in class: AllocationService -- Start");

        Map<String, List<Allocation>> stockToAllocationMap = new HashMap<>();

        //create map where key is stock and value is list of allocationMath data then iterate over it
        Map<String, List<AllocationMath>> stockToAllocationMathMap = allocationMathList.stream().collect(Collectors.groupingBy(AllocationMath::getStock));
        stockToAllocationMathMap.forEach((s, allocationMaths) -> {
            boolean hasErrorCondition = false;
            boolean existingAllocationQuantityUpdated = false;

            //sorting list to start with smallest
            allocationMaths.sort(Comparator.comparing(AllocationMath::getSUGGESTED_TRADE_ALLOCATION));

            for (AllocationMath allocationMath : allocationMaths) {
                long quantity;

                //check error condition if true then set quantity as 0 and hasErrorCondition flag has true
                if (allocationMath.getSUGGESTED_FINAL_POSITION() < 0
                        || allocationMath.getSUGGESTED_FINAL_POSITION() > allocationMath.getMAX_SHARES()
                        || (allocationMath.getType().equalsIgnoreCase("BUY") && allocationMath.getSUGGESTED_FINAL_POSITION() < allocationMath.getQuantityHeld())
                        || (allocationMath.getType().equalsIgnoreCase("SELL") && allocationMath.getSUGGESTED_FINAL_POSITION() > allocationMath.getQuantityHeld())) {

                    log.info("Error condition encountered for Account: {}, Stock: {}, Trade Type: {}, " +
                                    "SUGGESTED_FINAL_POSITION: {}, MAX_SHARES: {} and Quantity Held: {}",
                            allocationMath.getAccount(),
                            allocationMath.getStock(),
                            allocationMath.getType(),
                            allocationMath.getSUGGESTED_FINAL_POSITION(),
                            allocationMath.getMAX_SHARES(),
                            allocationMath.getQuantityHeld());

                    quantity = 0;
                    hasErrorCondition = true;
                } else {
                    log.info(String.format("Rounding SUGGESTED_TRADE_ALLOCATION value %s for Account: %s and Stock: %s",
                            allocationMath.getSUGGESTED_TRADE_ALLOCATION(),
                            allocationMath.getAccount(),
                            allocationMath.getStock()));
                    quantity = Math.round(allocationMath.getSUGGESTED_TRADE_ALLOCATION());
                }

                Allocation allocation;
                if (hasErrorCondition) {
                    allocation = new Allocation(allocationMath.getAccount(), allocationMath.getStock(), 0);
                    if (!CollectionUtils.isEmpty(stockToAllocationMap.get(allocationMath.getStock())) && !existingAllocationQuantityUpdated) {
                        //this means there are allocation data added before -- update quantity for all of them to be zero
                        stockToAllocationMap.get(allocationMath.getStock()).forEach(x -> x.setQuantity(0));
                        existingAllocationQuantityUpdated = true;

                    }
                } else {
                    allocation = new Allocation(allocationMath.getAccount(), allocationMath.getStock(), quantity);
                }

                stockToAllocationMap.putIfAbsent(allocationMath.getStock(), new ArrayList<>());
                stockToAllocationMap.get(allocationMath.getStock()).add(allocation);
            }
            ;
        });

        log.info("Inside method: createAllocationData in class: AllocationService -- End");

        return stockToAllocationMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<AllocationMath> createAllocationMathData(List<Trade> tradeList) {
        log.info("Inside method: createAllocationMathData in class: AllocationService -- Start");

        List<AllocationMath> allocationMathList = new ArrayList<>();
        for (Trade trade : tradeList) {

            //findAllCapitalByStock method will return all capital data and it will filter holdings and target.
            //Only one holding and target data should be available for given account and stock
            List<Capital> capitalListByStock = this.captialService.findAllCapitalByStock(trade.getStock());

            //For given stock get all the holdings and do sum
            Double totalHoldingQuantityByStock = this.holdingService.getTotalQuantity(trade.getStock());

            //For a given stock get total Target market value in Capital entity
            Double totalTargetMarketValue = capitalListByStock.stream().map(Capital::getTargetMarketValue).mapToDouble(Double::doubleValue).sum();

            for (Capital capital : capitalListByStock) {
                AllocationMath allocationMath
                        = this.calcAllocationMath(
                        totalHoldingQuantityByStock,
                        totalTargetMarketValue,
                        trade,
                        capital.getHoldingList().get(0),
                        capital);
                allocationMathList.add(allocationMath);
            }
        }

        log.info("Inside method: createAllocationMathData in class: AllocationService -- End");
        return allocationMathList;
    }

    public List<Allocation> calcTradeAllocation() {

        log.info("Inside method: calcTradeAllocation in class: AllocationService -- Start");

        List<Trade> tradeList = this.tradeRepo.findAll();
        if (CollectionUtils.isEmpty(tradeList)) {
            log.info("No Trade Data found!");
            return new ArrayList<>();
        }

        List<AllocationMath> allocationMathList = createAllocationMathData(tradeList);
        List<Allocation> allocationList = this.createAllocationData(allocationMathList);

        log.info("Inside method: calcTradeAllocation in class: AllocationService -- End");
        return allocationList;
    }
}
