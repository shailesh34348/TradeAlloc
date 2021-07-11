package com.app.trade.calc.engine.repository;

import com.app.trade.calc.engine.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoldingRepo extends JpaRepository<Holding, Long> {

    List<Holding> findAllByStock(String stock);
}
