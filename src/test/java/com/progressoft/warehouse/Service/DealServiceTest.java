package com.progressoft.warehouse.Service;

import com.progressoft.warehouse.WarehouseApplication;
import com.progressoft.warehouse.bean.CsvDealRecord;
import com.progressoft.warehouse.entity.Violation;
import com.progressoft.warehouse.repository.BatchRepository;
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
import utility.FileUtility;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore(value = {"javax.management.*", "javax.net.ssl.*", "javax.crypto.*","java.sql.*"})
@ContextConfiguration(classes = {WarehouseApplication.class})
public class DealServiceTest {

    @InjectMocks
    private DealService dealService;

    @Mock
    private BatchRepository batchRepository;
    @Mock
    private FileService fileService;
    @Mock
    private DealsCountService dealsCountService;


    private ArgumentCaptor<List<CsvDealRecord>>  dealsRecordArguments = new ArgumentCaptor<>();

    @Before
    public void setUp() {
        // i didn't fill any thing in the setup method for test, as i have chosen to create the test files instead in light it was more feasible and more close to the real test
    }

    @Test
    public void importDeals_invalidAmount() {
        FileInputStream fileInputStream = FileUtility.getFileInputStreamByName("unitTest","invalid_amount_cases.csv");
        List<CsvDealRecord> dealRecordList = dealService.importRecords(new InputStreamReader(fileInputStream),1);
        Assert.assertEquals(5, dealRecordList.size());
        Assert.assertEquals(1, dealRecordList.get(0).getViolationsList().size());
        Assert.assertEquals("Property value is invalid(Negative)", dealRecordList.get(0).getViolationsList().get(0).getDescription());
        Assert.assertEquals("Amount",dealRecordList.get(0).getViolationsList().get(0).getProperty());

        Assert.assertEquals(1, dealRecordList.get(1).getViolationsList().size());
        Assert.assertEquals("Property value is invalid", dealRecordList.get(1).getViolationsList().get(0).getDescription());
        Assert.assertEquals("Amount",dealRecordList.get(0).getViolationsList().get(0).getProperty());

        Assert.assertEquals(0, dealRecordList.get(2).getViolationsList().size());

        Assert.assertEquals(1, dealRecordList.get(3).getViolationsList().size());
        Assert.assertEquals("Empty property value", dealRecordList.get(3).getViolationsList().get(0).getDescription());
        Assert.assertEquals("amount",dealRecordList.get(3).getViolationsList().get(0).getProperty());


    }

    @Test
    public void importDeals_invalidToCurrency() {
        FileInputStream fileInputStream = FileUtility.getFileInputStreamByName("unitTest","invalid_to_currency_cases.csv");
        List<CsvDealRecord> dealRecordList = dealService.importRecords(new InputStreamReader(fileInputStream),1);
        Assert.assertEquals(4, dealRecordList.size());
        Assert.assertEquals(2, dealRecordList.get(0).getViolationsList().size());
        Assert.assertEquals("Property value is invalid", dealRecordList.get(0).getViolationsList().get(0).getDescription());
        Assert.assertEquals("To Currency Code",dealRecordList.get(0).getViolationsList().get(0).getProperty());
        Assert.assertEquals("Property value is invalid", dealRecordList.get(1).getViolationsList().get(0).getDescription());
        Assert.assertEquals("Amount",dealRecordList.get(0).getViolationsList().get(1).getProperty());

        Assert.assertEquals(1, dealRecordList.get(1).getViolationsList().size());
        Assert.assertEquals("Property value is invalid", dealRecordList.get(1).getViolationsList().get(0).getDescription());
        Assert.assertEquals("To Currency Code",dealRecordList.get(1).getViolationsList().get(0).getProperty());

        Assert.assertEquals(0, dealRecordList.get(2).getViolationsList().size());

        Assert.assertEquals(1, dealRecordList.get(3).getViolationsList().size());
        Assert.assertEquals("Empty property value", dealRecordList.get(3).getViolationsList().get(0).getDescription());
        Assert.assertEquals("toCurrency", dealRecordList.get(3).getViolationsList().get(0).getProperty());

    }


    @Test
    public void importDeals_invalidFromCurrency() {
        FileInputStream fileInputStream = FileUtility.getFileInputStreamByName("unitTest","invalid_from_currency_cases.csv");
        List<CsvDealRecord> dealRecordList = dealService.importRecords(new InputStreamReader(fileInputStream),1);
        Assert.assertEquals(4, dealRecordList.size());
        Assert.assertEquals(2, dealRecordList.get(0).getViolationsList().size());
        Assert.assertEquals("Property value is invalid", dealRecordList.get(1).getViolationsList().get(0).getDescription());
        Assert.assertEquals("From Currency Code",dealRecordList.get(0).getViolationsList().get(0).getProperty());
        Assert.assertEquals("Property value is invalid", dealRecordList.get(1).getViolationsList().get(0).getDescription());
        Assert.assertEquals("From Currency Code",dealRecordList.get(0).getViolationsList().get(0).getProperty());
        Assert.assertEquals(1, dealRecordList.get(1).getViolationsList().size());
        Assert.assertEquals("Property value is invalid", dealRecordList.get(1).getViolationsList().get(0).getDescription());
        Assert.assertEquals("From Currency Code",dealRecordList.get(0).getViolationsList().get(0).getProperty());
        Assert.assertEquals(0, dealRecordList.get(2).getViolationsList().size());
        Assert.assertEquals(1, dealRecordList.get(3).getViolationsList().size());
        Assert.assertEquals("Empty property value", dealRecordList.get(3).getViolationsList().get(0).getDescription());
        Assert.assertEquals("fromCurrency",dealRecordList.get(3).getViolationsList().get(0).getProperty());

    }


    @Test
    public void getViolationRecord() {

        FileInputStream fileInputStream = FileUtility.getFileInputStreamByName("unitTest","invalid_from_currency_cases.csv");
        List<CsvDealRecord> dealRecordList = dealService.importRecords(new InputStreamReader(fileInputStream),1);
        List<Violation> violationList = dealService.getViolationRecords(dealRecordList);
        Assert.assertEquals("From Currency Code", violationList.get(0).getProperty());
        Assert.assertEquals("Property value is invalid", violationList.get(0).getDescription());
        Assert.assertEquals(0, violationList.get(0).getFileIndex());
        Assert.assertEquals("Amount", violationList.get(1).getProperty());
        Assert.assertEquals("Property value is invalid(Negative)", violationList.get(1).getDescription());
        Assert.assertEquals(0, violationList.get(1).getFileIndex());

    }

    @Test
    public void saveRecordsToDatabase() {
        FileInputStream fileInputStream = FileUtility.getFileInputStreamByName("unitTest","invalid_from_currency_cases.csv");
        List<CsvDealRecord> dealRecordList = dealService.importRecords(new InputStreamReader(fileInputStream),1);
        dealService.saveRecordsToDatabase(dealRecordList);

        verify(batchRepository, times(2))
                .saveBatch(dealsRecordArguments.capture());

        Assert.assertEquals(dealRecordList, (dealsRecordArguments.getValue()));

    }


}