package com.progressoft.warehouse.repository;

import com.progressoft.warehouse.bean.CsvDealRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by EYAD on 3/8/2019.
 */
@Repository
@Slf4j
public class BatchRepository {

    @Autowired
    @Qualifier("WarehouseDataSource")
    private DataSource dataSource;

    public void saveBatch(List<CsvDealRecord> dealRecordList) {

        int partitionSize = 1000;
        List<List<CsvDealRecord>> partitions = new ArrayList<>();
        for (int i = 0; i < dealRecordList.size(); i += partitionSize) {
            partitions.add(dealRecordList.subList(i,
                    Math.min(i + partitionSize, dealRecordList.size())));
        }

        for (List<CsvDealRecord> partition : partitions)
        {
            Map<String, String> batchQueries = dealsQueryBuilder(partition);
            decisionMaker(batchQueries);

        }

    }

    public void decisionMaker(Map<String, String> batchQueries) {
        if (batchQueries.get("valid") != null) {
            batchThreadCreator(batchQueries.get("valid"));
        }
        if (batchQueries.get("invalid") != null) {
            batchThreadCreator(batchQueries.get("invalid"));
        }

    }


    public Map<String, String> dealsQueryBuilder(List<CsvDealRecord> dealRecordList) {
        Map<String, String> batchQueries = new HashMap<>();
        AtomicInteger validFlag = new AtomicInteger(0);
        AtomicInteger invalidFlag = new AtomicInteger(0);

        StringBuilder validSql = new StringBuilder();
        StringBuilder invalidSql = new StringBuilder();
        validSql.append("INSERT INTO DEALS_VALID_RECORDS (id, from_currency,to_currency,time_stamp, amount, file_id) VALUES ");
        invalidSql.append("INSERT INTO DEALS_INVALID_RECORDS (id, from_currency,to_currency,time_stamp, amount,violation, file_id) VALUES ");

        AtomicInteger index = new AtomicInteger();
        dealRecordList.forEach(d -> {
                switch (d.getViolationsList().size()) {
                    case 0:

                        validSql.append("(NULL,'").append(d.getFromCurrency()).append("', '")
                                .append(d.getToCurrency()).append("', '").append(d.getTimeStamp()).append("', ").append(d.getAmount()).append(", ").append(d.getFileId()).append("),");
                        validFlag.set(1);
                        break;

                    default:
                        invalidSql.append("(NULL,'").append(d.getFromCurrency()).append("', '")
                                .append(d.getToCurrency()).append("', '").append(d.getTimeStamp()).append("', ").append(d.getAmount()).append(", '").append(d.getViolationsList()).append("',").append(d.getFileId()).append("),");
                        invalidFlag.set(1);
                        break;

                }

        });
        validSql.delete(validSql.length() - 1, validSql.length());
        invalidSql.delete(invalidSql.length() - 1, invalidSql.length());

        if (validFlag.get() != 0) {
            batchQueries.put("valid", validSql.toString());
        } else {
            batchQueries.put("valid", null);
        }

        if (validFlag.get() != 0) {
            batchQueries.put("invalid", invalidSql.toString());
        } else {
            batchQueries.put("invalid", null);
        }

        return batchQueries;

    }

    public void batchThreadCreator(String query) {
        Runnable runnable = () -> {

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.batchUpdate(query);
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    public void updateDealsCount(Map<String, Long> currencyCount) {
        String query = buildUpdateCountQuery(currencyCount);
        if (query != null) {
            log.info("update query " + query);
            batchThreadCreator(query);
        }
    }

    public void insertDealsCount(Map<String, Long> currencyCount) {
        String query = buildInsertCountQuery(currencyCount);
        if (query != null) {
            log.info("insert  query " + query);
            batchThreadCreator(query);
        }
    }


    public String buildUpdateCountQuery(Map<String, Long> currencyCount) {

        if (currencyCount.size() > 0) {
            StringBuilder query = new StringBuilder();
            query.append("UPDATE  DEALS_COUNT SET deals_count = ( case");
            currencyCount.entrySet().forEach(d -> query.append(" WHEN from_currency='").append(d.getKey()).append("' THEN ").append(d.getValue()));
            query.append(" END)");
            return query.toString();
        } else return null;

    }

    public String buildInsertCountQuery(Map<String, Long> currencyCount) {

        if (currencyCount.size() > 0) {
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO DEALS_COUNT (from_currency, deals_count) values");
            currencyCount.entrySet().forEach(d -> query.append("( '").append(d.getKey()).append("',").append(d.getValue()).append("),"));
            query.delete(query.length() - 1, query.length());
            return query.toString();
        } else {
            return null;
        }
    }

}
