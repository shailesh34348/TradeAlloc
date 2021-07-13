package com.app.trade.calc.engine.repository;

import com.app.trade.calc.engine.model.Capital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CapitalRepo extends JpaRepository<Capital, Long> {

    List<Capital> findDistinctByHoldingListStockAndTargetListStock(String holdingStock, String targetStock);
    List<Capital> findByHoldingListStock(String stock);

    @Query("select DISTINCT c from Capital c JOIN FETCH c.holdingList as h where  h.stock = :holdingStock ")
    List<Capital> findAllCapitalByStockUsingCustomQuery(String holdingStock);
}
