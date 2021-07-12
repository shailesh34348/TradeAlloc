package com.app.trade.calc.engine.service;

import com.app.trade.calc.engine.model.Holding;
import com.app.trade.calc.engine.repository.HoldingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HoldingService {
    private HoldingRepo holdingRepo;

    @Autowired
    public HoldingService(HoldingRepo holdingRepo) {
        this.holdingRepo = holdingRepo;
    }

    public Double getTotalQuantity(String stock) {
        List<Holding> holdingList = this.holdingRepo.findAllByStock(stock);
        return holdingList.stream().map(Holding::getQuantity).mapToDouble(Double::doubleValue).sum();
    }
}
