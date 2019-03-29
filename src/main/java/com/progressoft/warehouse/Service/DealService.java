package com.progressoft.warehouse.Service;

import com.progressoft.warehouse.bean.CsvDealRecord;
import com.progressoft.warehouse.entity.Violation;
import com.progressoft.warehouse.enums.CurrencyCode;
import com.progressoft.warehouse.repository.BatchRepository;
import com.progressoft.warehouse.utility.CsvParserUtility;
import com.progressoft.warehouse.utility.DateUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by EYAD on 3/6/2019.
 */
@Service
@Data
@Slf4j
public class DealService {

    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private DealsCountService dealsCountService;

    public List<CsvDealRecord> importRecords(InputStreamReader inputStreamReader, long fileIndex) {
        List<CsvDealRecord> dealRecords = CsvParserUtility.parseDealsRecords(inputStreamReader, "\n");

        dealRecords.stream().forEach(d -> {
            List<Violation> violationList = validateRecord(d);
            d.setFileId(fileIndex);
            try {
                d.setTimeStamp(DateUtility.stringToDate(d.getTimeStamp()));
            } catch (Exception e) {
            }
            if (violationList.size() > 0) {
                d.setViolationsList(violationList);
            }
        });


        saveRecordsToDatabase(dealRecords);
        dealsCountService.aggregateDealsCount(dealRecords);
        return dealRecords;

    }

    public List<Violation> getViolationRecords(List<CsvDealRecord> dealRecords) {
        return  dealRecords.parallelStream()
                .flatMap(a -> a.getViolationsList().stream()).collect(Collectors.toList());
    }

    public long getDeslsWithoutViolations(List<CsvDealRecord> dealRecords) {
        return dealRecords.size() - dealRecords.parallelStream().filter( d -> d.getViolationsList().size() > 0 ).count();
    }

    public List<Violation> validateRecord(CsvDealRecord record) {
        List<Violation> violationList = new ArrayList<>();
        for (Field field : record.getClass().getDeclaredFields()
                ) {
            field.setAccessible(true);
            try {
                if (field.get(record) == null || field.equals("") ) {
                    violationList.add(new Violation(record.getFileId(), field.getName(), "Empty property value"));
                } else  if ( field.get(record).equals(" ")) {
                    violationList.add(new Violation(record.getFileId(), field.getName(), "Property value is space"));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (record.getToCurrency() != null && (record.getToCurrency().equals("") || record.getToCurrency().equals(" ") || CurrencyCode.checkCodeExists(record.getToCurrency()).equals(Boolean.FALSE))) {
            violationList.add(new Violation(record.getFileId(), "To Currency Code", "Property value is invalid"));
        }

        if (record.getFromCurrency() != null && (record.getFromCurrency().equals("") || record.getFromCurrency().equals(" ") || CurrencyCode.checkCodeExists(record.getFromCurrency()).equals(Boolean.FALSE))) {
            violationList.add(new Violation(record.getFileId(), "From Currency Code", "Property value is invalid"));
        }

        try {
            if (record.getAmount() != null && (!record.getAmount().equals("") && !record.getAmount().equals(" ") && Double.parseDouble(record.getAmount()) < 0)) {
                violationList.add(new Violation(record.getFileId(), "Amount", "Property value is invalid(Negative)"));
            }
        } catch (NumberFormatException e) {
            violationList.add(new Violation(record.getFileId(), "Amount", "Property value is invalid"));
            record.setAmount("-1");
            log.info(e.getMessage());
        }

        return violationList;
    }

    public void saveRecordsToDatabase(List<CsvDealRecord> dealRecords) {

       batchRepository.saveBatch(dealRecords);
    }


}
