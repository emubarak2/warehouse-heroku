package com.progressoft.warehouse.utility;

import com.progressoft.warehouse.bean.CsvDealRecord;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;

import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by EYAD on 3/7/2019.
 */
@Slf4j
public class CsvParserUtility {

    public static List<CsvDealRecord> parseDealsRecords(InputStreamReader inputStreamReader, String lineSeparator) {

        BeanListProcessor<CsvDealRecord> rowProcessor = new BeanListProcessor<>(CsvDealRecord.class);
        CsvParserSettings settings = new CsvParserSettings();
        settings.setRowProcessor(rowProcessor);
        settings.getFormat().setLineSeparator(lineSeparator);
        CsvParser csvParser = new CsvParser(settings);
        csvParser.parse(inputStreamReader);
        List<CsvDealRecord> csvDealRecords = rowProcessor.getBeans();
        return csvDealRecords;
    }

}
