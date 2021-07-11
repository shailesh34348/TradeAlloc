package com.app.trade.calc.engine.repository;

import com.app.trade.calc.engine.model.Target;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepo extends JpaRepository<Target, Long> {
}
