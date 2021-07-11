package com.app.trade.calc.engine.service;

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
import com.opencsv.bean.CsvToBean;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class AbstractTestMethod {

    @Autowired
    TradeRepo tradeRepo;
    @Autowired
    HoldingRepo holdingRepo;
    @Autowired
    TargetRepo targetRepo;
    @Autowired
    CapitalRepo capitalRepo;

    private CsvToBean csvToBean = new CsvToBean();

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

    public List<Trade> convertTradeCsvToEntity() {
        CSVReader csvReader = this.getCsvReader("src/test/resources/trade.csv");
        return csvToBean.parse(CsvMappingUtil.getTradeCSVMapping(), csvReader);
    }

    public List<Capital> convertCapitalCsvToEntity() {
        CSVReader csvReader = this.getCsvReader("src/test/resources/capital.csv");
        return csvToBean.parse(CsvMappingUtil.getCapitalCSVMapping(), csvReader);
    }

    public List<Target> convertTargetCsvToEntity() {
        CSVReader csvReader = this.getCsvReader("src/test/resources/target.csv");
        return csvToBean.parse(CsvMappingUtil.getTargetCSVMapping(), csvReader);
    }

    public List<Holding> convertHoldingCsvToEntity() {
        CSVReader csvReader = this.getCsvReader("src/test/resources/holding.csv");
        return csvToBean.parse(CsvMappingUtil.getHoldingCSVMapping(), csvReader);
    }

    public void loadDataInDB() {
        List<Trade> tradeList = convertTradeCsvToEntity();
        List<Capital> capitalList = convertCapitalCsvToEntity();
        List<Target> targetList = convertTargetCsvToEntity();
        List<Holding> holdingList = convertHoldingCsvToEntity();

        tradeRepo.saveAll(tradeList);
        capitalRepo.saveAll(capitalList);
        targetRepo.saveAll(targetList);
        holdingRepo.saveAll(holdingList);
    }
}
