package com.progressoft.warehouse.Service;

import com.progressoft.warehouse.bean.CsvDealRecord;
import com.progressoft.warehouse.entity.DealsCount;
import com.progressoft.warehouse.repository.BatchRepository;
import com.progressoft.warehouse.repository.DealsCountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EYAD on 3/8/2019.
 */

@Service
@Slf4j
public class DealsCountService {

    @Autowired
    private DealsCountRepository dealsCountRepository;

    @Autowired
    private BatchRepository batchRepository;

    public void aggregateDealsCount(List<CsvDealRecord> fileDealRecords) {

        Map<String, Long> fileDealsCount = new HashMap<>();
        fileDealRecords.forEach(d -> {
                    if (fileDealsCount.get(d.getFromCurrency()) != null) {
                        fileDealsCount.put(d.getFromCurrency(), fileDealsCount.get(d.getFromCurrency()) + 1);
                    } else {
                        fileDealsCount.put(d.getFromCurrency(), 1L);
                    }

                }
        );
        addDealCount(fileDealsCount);
    }


    public void addDealCount(Map<String, Long> fileDealsCount) {
        List<DealsCount> dealsCountList = dealsCountRepository.findAll();
        Map<String, Long> currentDealsCount = new HashMap<>();
        Map<String, Long> insertDealsCount = new HashMap<>();

        dealsCountList.forEach(d -> currentDealsCount.put(d.getFromCurrency(), d.getCount()));

        fileDealsCount.entrySet().forEach(d -> {
            if (currentDealsCount.get(d.getKey()) != null && currentDealsCount.size() > 0) {
                currentDealsCount.put(d.getKey(), currentDealsCount.get(d.getKey()) + d.getValue());
            } else {
                insertDealsCount.put(d.getKey(), d.getValue());
            }
        });

        batchRepository.updateDealsCount(currentDealsCount);
        batchRepository.insertDealsCount(insertDealsCount);
    }


}
