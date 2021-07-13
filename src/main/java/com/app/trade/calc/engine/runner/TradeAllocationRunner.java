package com.app.trade.calc.engine.runner;

import com.app.trade.calc.engine.domain.Allocation;
import com.app.trade.calc.engine.service.AllocationService;
import com.app.trade.calc.engine.service.DataLoadService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Getter @Setter
public class TradeAllocationRunner implements CommandLineRunner {

    private DataLoadService dataLoadService;
    private AllocationService allocationService;
    private ConfigurableApplicationContext context;

    @Autowired
    public TradeAllocationRunner(DataLoadService dataLoadService, AllocationService allocationService, ConfigurableApplicationContext context) {
        this.dataLoadService = dataLoadService;
        this.allocationService = allocationService;
        this.context = context;
    }

    @Value("${tradeFilePath}")
    private String tradeFilePath;

    @Value("${capitalFilePath}")
    private String capitalFilePath;

    @Value("${holdingFilePath}")
    private String holdingFilePath;

    @Value("${targetFilePath}")
    private String targetFilePath;

    @Value("${allocationFilePath}")
    private String allocationFilePath;

    @Override
    public void run(String... args)  {
        log.info("App started with " + args.length + " parameters.");
        log.info("Trade File Path: {}, Capital File Path: {}, " +
                        "Holding File Path: {}, Target File Path {}," +
                        "Allocations File Path: {}",
                tradeFilePath,
                capitalFilePath,
                holdingFilePath,
                targetFilePath,
                allocationFilePath);

        //Load data from csv file to H2 DB
        this.dataLoadService.loadDataInDB(
                tradeFilePath,
                capitalFilePath,
                holdingFilePath,
                targetFilePath);

        //calculate trade allocations
        List<Allocation> allocationList = this.allocationService.calcTradeAllocation();

        //write output in allocations.csv
        this.dataLoadService.writeCsv(allocationFilePath, allocationList);

        SpringApplication.exit(context);
    }
}
