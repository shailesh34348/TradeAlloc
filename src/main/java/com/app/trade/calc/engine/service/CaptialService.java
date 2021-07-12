package com.app.trade.calc.engine.service;

import com.app.trade.calc.engine.model.Capital;
import com.app.trade.calc.engine.model.Holding;
import com.app.trade.calc.engine.model.Target;
import com.app.trade.calc.engine.repository.CapitalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CaptialService {
    private CapitalRepo capitalRepo;

    @Autowired
    public CaptialService(CapitalRepo capitalRepo) {
        this.capitalRepo = capitalRepo;
    }

    private Double calculateTargetMarketValue(float percent, Double capital) {
        return percent*capital*0.01;
    }

    public List<Capital> findAllCapitalByStock(String stock) {
        List<Capital> capitalList = this.capitalRepo.findAllCapitalByStockUsingCustomQuery(stock);
        for (Capital capital: capitalList) {

            Set<String> holdingStockSet = capital.getHoldingList().stream().map(Holding::getStock).collect(Collectors.toSet());
            if (holdingStockSet.size() >1) {
                throw new RuntimeException("Multiple holding entry for same stock and same account found");
            }

            List<Target> filteredTargetList = capital.getTargetList().stream().filter(x -> holdingStockSet.contains(x.getStock())).collect(Collectors.toList());
            capital.setTargetList(filteredTargetList);

            float percent = !CollectionUtils.isEmpty(filteredTargetList) ? filteredTargetList.get(0).getPercent() : 0;
            double capitalValue = !CollectionUtils.isEmpty(capital.getHoldingList()) ? capital.getHoldingList().get(0).getCapital().getCapital() : 0;

            Double targetMarketValue = this.calculateTargetMarketValue(percent, capitalValue);
            capital.setTargetMarketValue(targetMarketValue);
        }
        return capitalList;
    }


}
