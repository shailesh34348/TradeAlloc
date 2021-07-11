package com.app.trade.calc.engine.service;

import com.app.trade.calc.engine.domain.Allocation;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AllocationServiceTest extends AbstractTestMethod {

    @Autowired
    private AllocationService allocationService;

    @Before("init")
    public void init() {
        this.loadDataInDB();
    }

    @Test
    public void test_calcTradeAllocationSuccess() {
        List<Allocation> allocationList = this.allocationService.calcTradeAllocation();
        System.out.println(allocationList.size());
    }
}
