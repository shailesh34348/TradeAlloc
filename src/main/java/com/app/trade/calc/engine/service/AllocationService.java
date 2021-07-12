package com.app.trade.calc.engine.service;

import com.app.trade.calc.engine.domain.Allocation;
import com.app.trade.calc.engine.domain.AllocationMetric;
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

    private AllocationMetric calcUsingAllocationMath(
            Double totalHoldingQuantityByStock,
            Double totalTargetMarketValue,
            Trade trade,
            Holding holding,
            Capital capital) {
        log.info("Inside method: calcAllocationMath in class: AllocationService -- Start");

        log.info("Allocation Math in progress for Account: {} and Stock: {}",
                capital.getAccount(),
                trade.getStock());

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

        AllocationMetric allocationMetric = new AllocationMetric(
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
        return allocationMetric;
    }

    private List<Allocation> createAllocationData(List<AllocationMetric> allocationMetricList) {

        log.info("Inside method: createAllocationData in class: AllocationService -- Start");

        Map<String, List<Allocation>> stockToAllocationMap = new HashMap<>();

        //create map where key is stock and value is list of allocationMath data then iterate over it
        Map<String, List<AllocationMetric>> stockToAllocationMathMap = allocationMetricList.stream().collect(Collectors.groupingBy(AllocationMetric::getStock));
        stockToAllocationMathMap.forEach((stock, allocationMetrics) -> {
            boolean hasErrorCondition = false;
            boolean existingAllocationQuantityUpdated = false;
            long remainingQuantity = 0;

            //sorting list to start with smallest
            allocationMetrics.sort(Comparator.comparing(AllocationMetric::getSuggestedTradeAllocation));

            Iterator<AllocationMetric> allocationMetricIterator = allocationMetrics.iterator();
            while (allocationMetricIterator.hasNext()) {
                AllocationMetric allocationMetric = allocationMetricIterator.next();
                long quantity;

                //check error condition if true then set quantity as 0 and hasErrorCondition flag has true
                if (allocationMetric.getSuggestedFinalPosition() < 0
                        || allocationMetric.getSuggestedFinalPosition() > allocationMetric.getMaxShares()
                        || (allocationMetric.getType().equalsIgnoreCase("BUY") && allocationMetric.getSuggestedFinalPosition() < allocationMetric.getQuantityHeld())
                        || (allocationMetric.getType().equalsIgnoreCase("SELL") && allocationMetric.getSuggestedFinalPosition() > allocationMetric.getQuantityHeld())) {

                    log.info("Error condition encountered for Account: {}, Stock: {}, Trade Type: {}, " +
                                    "SUGGESTED_FINAL_POSITION: {}, MAX_SHARES: {} and Quantity Held: {}",
                            allocationMetric.getAccount(),
                            allocationMetric.getStock(),
                            allocationMetric.getType(),
                            allocationMetric.getSuggestedFinalPosition(),
                            allocationMetric.getMaxShares(),
                            allocationMetric.getQuantityHeld());

                    quantity = 0;
                    hasErrorCondition = true;
                } else {
                    log.info("Rounding SUGGESTED_TRADE_ALLOCATION value {} for Account: {} and Stock: {}",
                            allocationMetric.getSuggestedTradeAllocation(),
                            allocationMetric.getAccount(),
                            allocationMetric.getStock());

                    quantity = !allocationMetricIterator.hasNext() ? remainingQuantity : Math.round(allocationMetric.getSuggestedTradeAllocation());

                    if (remainingQuantity == 0) {
                        remainingQuantity = Math.round(allocationMetric.getMaxShares()) - quantity;
                    } else {
                        remainingQuantity -= quantity;
                    }

                }

                Allocation allocation;
                if (hasErrorCondition) {
                    allocation = new Allocation(allocationMetric.getAccount(), allocationMetric.getStock(), 0);
                    if (!CollectionUtils.isEmpty(stockToAllocationMap.get(allocationMetric.getStock())) && !existingAllocationQuantityUpdated) {
                        //this means there are allocation data added before -- update quantity for all of them to be zero
                        stockToAllocationMap.get(allocationMetric.getStock()).forEach(x -> x.setQuantity(0));
                        existingAllocationQuantityUpdated = true;

                    }
                } else {
                    allocation = new Allocation(allocationMetric.getAccount(), allocationMetric.getStock(), quantity);
                }

                stockToAllocationMap.putIfAbsent(allocationMetric.getStock(), new ArrayList<>());
                stockToAllocationMap.get(allocationMetric.getStock()).add(allocation);
            }
        });

        log.info("Inside method: createAllocationData in class: AllocationService -- End");

        return stockToAllocationMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<AllocationMetric> createAllocationMetric(List<Trade> tradeList) {
        log.info("Inside method: createAllocationMathData in class: AllocationService -- Start");

        List<AllocationMetric> allocationMetricList = new ArrayList<>();
        for (Trade trade : tradeList) {

            //findAllCapitalByStock method will return all capital data and it will filter holdings and target.
            //Only one holding and target data should be available for given account and stock
            List<Capital> capitalListByStock = this.captialService.findAllCapitalByStock(trade.getStock());

            //For given stock get all the holdings and do sum
            Double totalHoldingQuantityByStock = this.holdingService.getTotalQuantity(trade.getStock());

            //For a given stock get total Target market value in Capital entity
            Double totalTargetMarketValue = capitalListByStock.stream().map(Capital::getTargetMarketValue).mapToDouble(Double::doubleValue).sum();

            for (Capital capital : capitalListByStock) {
                AllocationMetric allocationMetric
                        = this.calcUsingAllocationMath(
                        totalHoldingQuantityByStock,
                        totalTargetMarketValue,
                        trade,
                        capital.getHoldingList().get(0),
                        capital);
                allocationMetricList.add(allocationMetric);
            }
        }

        log.info("Inside method: createAllocationMathData in class: AllocationService -- End");
        return allocationMetricList;
    }

    public List<Allocation> calcTradeAllocation() {

        log.info("Inside method: calcTradeAllocation in class: AllocationService -- Start");

        List<Trade> tradeList = this.tradeRepo.findAll();
        if (CollectionUtils.isEmpty(tradeList)) {
            log.info("No Trade Data found!");
            return new ArrayList<>();
        }

        List<AllocationMetric> allocationMetricList = createAllocationMetric(tradeList);
        List<Allocation> allocationList = this.createAllocationData(allocationMetricList);

        log.info("Inside method: calcTradeAllocation in class: AllocationService -- End");
        return allocationList;
    }
}
