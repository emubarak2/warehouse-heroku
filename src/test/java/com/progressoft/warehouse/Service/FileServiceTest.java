package com.progressoft.warehouse.Service;

import com.progressoft.warehouse.WarehouseApplication;
import com.progressoft.warehouse.entity.FileRecord;
import com.progressoft.warehouse.exception.FileExitsException;
import com.progressoft.warehouse.repository.FileRecordRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore(value = {"javax.management.*", "javax.net.ssl.*", "javax.crypto.*", "java.sql.*"})
@ContextConfiguration(classes = {WarehouseApplication.class})
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRecordRepository fileRecordRepository;

    private ArgumentCaptor<String> fileName = ArgumentCaptor.forClass(String.class);

    @Before
    public void setUp() {
        // i didn't fill any thing in the setup method for test, as i have chosen to create the test files instead in light it was more feasible and more close to the real test
    }

    @Test(expected = FileExitsException.class)
    public void getNextFileId_FileAlreadyExits() throws FileExitsException {
        when(fileRecordRepository.getFileRecordByFileName(
                any(String.class))).thenReturn(new FileRecord(200L, "AnyFileName"));

        fileService.getNextFileId("FileName");
        Mockito.verify(fileRecordRepository, times(0)).addFileName("FileName");
        Mockito.verify(fileRecordRepository, times(0)).getMaxFileId();

    }

    @Test
    public void getNextFileId_FileDoesntExits() throws FileExitsException {
        when(fileRecordRepository.getFileRecordByFileName(
                any(String.class))).thenReturn(null);

        fileService.getNextFileId("FileName");
        Mockito.verify(fileRecordRepository, times(1)).addFileName("FileName");
        Mockito.verify(fileRecordRepository, times(1)).getMaxFileId();
        verify(fileRecordRepository, times(1))
                .addFileName(fileName.capture());

        Assert.assertEquals("FileName", fileName.getValue());

    }

}