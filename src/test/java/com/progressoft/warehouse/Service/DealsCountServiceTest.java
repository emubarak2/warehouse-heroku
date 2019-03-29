package com.progressoft.warehouse.Service;

import com.progressoft.warehouse.WarehouseApplication;
import com.progressoft.warehouse.bean.CsvDealRecord;
import com.progressoft.warehouse.entity.DealsCount;
import com.progressoft.warehouse.repository.BatchRepository;
import com.progressoft.warehouse.repository.DealsCountRepository;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore(value = {" org.springframework.beans.factory.*,com.zaxxer.*,javax.*", "javax.net.ssl.*", "javax.crypto.*","java.sql.*,org.springframework.jmx.*"})
@ContextConfiguration(classes = {WarehouseApplication.class})
public class DealsCountServiceTest {


    @InjectMocks
    private DealsCountService dealsCountService;

    @Mock
    DealService dealService;

    @Mock
    private BatchRepository batchRepository;

    @Mock
    DealsCountRepository dealsCountRepository;


    private ArgumentCaptor<Map> insertMapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
    private ArgumentCaptor<Map> updateMapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
    private ArgumentCaptor<Map> mapToCheck =  ArgumentCaptor.forClass(Map.class);

    List<CsvDealRecord>csvDealRecordList = new ArrayList();
    Map<String,Long> currentCurrencyCount = new HashedMap();
    List<DealsCount> dealsCountList = new ArrayList<>();


    @Before
    public void setUp() {

        csvDealRecordList.add(new CsvDealRecord("AFAA", "AFA","7/28/2001  8:43:00 AM","-11"));
        csvDealRecordList.add(new CsvDealRecord("BBB", "ALL","7/28/2001  8:43:00 AM","1"));
        csvDealRecordList.add(new CsvDealRecord("DZD", "DZD","7/28/2001  8:43:00 AM","1"));
        csvDealRecordList.add(new CsvDealRecord("", "DZD","7/28/2001  8:43:00 AM","1"));
        csvDealRecordList.add(new CsvDealRecord("", "CCC","7/28/2001  8:43:00 AM","1"));
        csvDealRecordList.add(new CsvDealRecord("DZD", "DZD","7/28/2001  8:43:00 AM","1"));

        dealsCountList.add(new DealsCount(1L, "DZD", 4));
        dealsCountList.add(new DealsCount(1L, "BBB", 512));

        currentCurrencyCount.put("AFA",2L);
        currentCurrencyCount.put("BBB",82L);
        currentCurrencyCount.put("AFAA",1L);
        currentCurrencyCount.put("",2L);


    }

    @Test
    public void aggregateDealsCount() {
        when(dealService.importRecords(
                any(InputStreamReader.class), any(Long.class)))
                .thenReturn(csvDealRecordList);
        dealsCountService.aggregateDealsCount(csvDealRecordList);

        verify(batchRepository, times(1))
                .updateDealsCount(updateMapArgumentCaptor.capture());
        verify(batchRepository, times(1))
                .insertDealsCount(insertMapArgumentCaptor.capture());

        Assert.assertEquals("{=2, BBB=1, AFAA=1, DZD=2}", (insertMapArgumentCaptor.getValue().toString()));
        Assert.assertEquals("{}", (updateMapArgumentCaptor.getValue().toString()));

    }


    @Test
    public void addDealCount() {
        when(dealService.importRecords(
                any(InputStreamReader.class), any(Long.class)))
                .thenReturn(csvDealRecordList);

        when(dealsCountRepository.findAll())
                .thenReturn(dealsCountList);
        dealsCountService.addDealCount(currentCurrencyCount);


        verify(batchRepository, times(1))
                .updateDealsCount(updateMapArgumentCaptor.capture());
        verify(batchRepository, times(1))
                .insertDealsCount(insertMapArgumentCaptor.capture());

        Assert.assertEquals("{=2, AFAA=1, AFA=2}", (insertMapArgumentCaptor.getValue().toString()));
        Assert.assertEquals("{BBB=594, DZD=4}", (updateMapArgumentCaptor.getValue().toString()));

    }



}