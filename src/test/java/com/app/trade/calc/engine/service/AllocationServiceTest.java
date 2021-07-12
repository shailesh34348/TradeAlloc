package com.app.trade.calc.engine.service;

import com.app.trade.calc.engine.AbstractTestMethod;
import com.app.trade.calc.engine.domain.Allocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AllocationServiceTest extends AbstractTestMethod {

    @Autowired
    private AllocationService allocationService;

    @Before
    public void init() {
        this.loadDataInDB();
    }

    @Test
    public void test_calcTradeAllocationSuccess() {
        List<Allocation> allocationList = this.allocationService.calcTradeAllocation();
        this.writeCsv(allocationList);
    }
}