package com.app.trade.calc.engine.service;

import com.app.trade.calc.engine.domain.Allocation;
import com.app.trade.calc.engine.model.Capital;
import com.app.trade.calc.engine.model.Holding;
import com.app.trade.calc.engine.model.Target;
import com.app.trade.calc.engine.model.Trade;
import com.app.trade.calc.engine.repository.CapitalRepo;
import com.app.trade.calc.engine.repository.HoldingRepo;
import com.app.trade.calc.engine.repository.TargetRepo;
import com.app.trade.calc.engine.repository.TradeRepo;
import com.app.trade.calc.engine.util.CsvMappingUtil;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DataLoadService {

    private TradeRepo tradeRepo;
    private HoldingRepo holdingRepo;
    private TargetRepo targetRepo;
    private CapitalRepo capitalRepo;
    private CsvToBean csvToBean = new CsvToBean();

    @Autowired
    public DataLoadService(TradeRepo tradeRepo, HoldingRepo holdingRepo, TargetRepo targetRepo, CapitalRepo capitalRepo) {
        this.tradeRepo = tradeRepo;
        this.holdingRepo = holdingRepo;
        this.targetRepo = targetRepo;
        this.capitalRepo = capitalRepo;
    }

    private CSVReader getCsvReader(String filePath) {
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(filePath));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return csvReader;
    }

    public List<Trade> convertTradeCsvToEntity(String tradeFilePath) {
        CSVReader csvReader = this.getCsvReader(tradeFilePath);
        return csvToBean.parse(CsvMappingUtil.getTradeCSVMapping(), csvReader);
    }

    public List<Capital> convertCapitalCsvToEntity(String capitalFilePath) {
        CSVReader csvReader = this.getCsvReader(capitalFilePath);
        return csvToBean.parse(CsvMappingUtil.getCapitalCSVMapping(), csvReader);
    }

    public List<Target> convertTargetCsvToEntity(String targetFilePath) {
        CSVReader csvReader = this.getCsvReader(targetFilePath);
        return csvToBean.parse(CsvMappingUtil.getTargetCSVMapping(), csvReader);
    }

    public List<Holding> convertHoldingCsvToEntity(String holdingFilePath) {
        CSVReader csvReader = this.getCsvReader(holdingFilePath);
        return csvToBean.parse(CsvMappingUtil.getHoldingCSVMapping(), csvReader);
    }

    public void loadDataInDB(
            String tradeFilePath,
            String capitalFilePath,
            String holdingFilePath,
            String targetFilePath) {

        List<Trade> tradeList = convertTradeCsvToEntity(tradeFilePath);
        log.info("trade list side from csv file:  {}", tradeList.size());

        List<Capital> capitalList = convertCapitalCsvToEntity(capitalFilePath);
        log.info("capital list side from csv file:  {}", capitalList.size());

        List<Target> targetList = convertTargetCsvToEntity(targetFilePath);
        log.info("target list side from csv file:  {}", targetList.size());

        List<Holding> holdingList = convertHoldingCsvToEntity(holdingFilePath);
        log.info("holding list side from csv file:  {}", holdingList.size());

        tradeRepo.saveAll(tradeList);
        capitalRepo.saveAll(capitalList);
        targetRepo.saveAll(targetList);
        holdingRepo.saveAll(holdingList);
    }

    public void writeCsv(String allocationFilePath, List<Allocation> allocationList) {

        try (FileWriter writer = new FileWriter(allocationFilePath)) {
            ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
            mappingStrategy.setType(Allocation.class);

            String[] columns = {"account", "stock", "quantity"};
            mappingStrategy.setColumnMapping(columns);

            StatefulBeanToCsv beanWriter = new StatefulBeanToCsvBuilder(writer)
                    .withMappingStrategy(mappingStrategy)
                    .build();

            beanWriter.write(allocationList);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
