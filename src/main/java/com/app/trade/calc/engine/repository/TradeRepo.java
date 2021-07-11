package com.app.trade.calc.engine.repository;

import com.app.trade.calc.engine.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepo extends JpaRepository<Trade, Long> {
}
