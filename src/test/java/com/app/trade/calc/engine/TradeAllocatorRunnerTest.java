package com.app.trade.calc.engine;

import com.app.trade.calc.engine.runner.TradeAllocationRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Ignore
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeAllocatorRunnerTest {

    @Autowired
    private TradeAllocationRunner tradeAllocationRunner;

    @Test
    public void testMain() {
        System.out.println("Test");
        tradeAllocationRunner.run();
    }
}
