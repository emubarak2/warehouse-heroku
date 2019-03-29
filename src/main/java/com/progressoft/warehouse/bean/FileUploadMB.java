package com.progressoft.warehouse.bean;

import com.progressoft.warehouse.Service.DealService;
import com.progressoft.warehouse.Service.FileService;
import com.progressoft.warehouse.entity.Violation;
import com.progressoft.warehouse.exception.FileExitsException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.beans.Transient;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyadmubarak@amazon.com on 3/10/2019.
 */

@ManagedBean(name = "fileUploadMB")
@Named
@ViewScoped
@Data
@NoArgsConstructor
public class FileUploadMB extends FileUpload implements Serializable {
    public UploadedFile file;
    private List<CsvDealRecord> violationList = new ArrayList();
    private String fileName;
    private InputStream uploadedFileStream;
    private String report;

    @Autowired
    private FileService fileService;
    @Autowired
    private DealService dealService;
    private String stringReport;

    @Transient(value = false)
    public void fileUploadAction(FileUploadEvent event) throws IOException {
        event.getComponent().setTransient(false);
        UploadedFile uploadedFile = event.getFile();
        this.file = event.getFile();
        try {
            this.uploadedFileStream = uploadedFile.getInputstream();
            this.fileName = "Uploaded Sheet: " + uploadedFile.getFileName();
        } catch (IOException e) {

        }

    }

    @Override
    public boolean isTransient() {
        return false;
    }


    public void processFile() {
        List<CsvDealRecord> dealRecords = new ArrayList();
        String fileName;

        if (file != null) {
            try {
                long start2 = System.nanoTime();

                fileName = this.file.getFileName();
                long fileIndex = fileService.getNextFileId(fileName);
                dealRecords = dealService.importRecords(new InputStreamReader(file.getInputstream()), fileIndex);

                float timeConsumed = Precision.round((System.nanoTime() - start2) / 1000000000L, 6);
                this.stringReport = generateReport(dealRecords, timeConsumed);
            } catch (IOException | FileExitsException ex) {
                report = ex.getMessage();
                this.stringReport = ex.getMessage();
            }
        } else {
            stringReport = "Please a select a file before clicking on import.";
        }
    }

    public String generateReport(List<CsvDealRecord> dealRecords, float timeConsumed) {

        List<Violation> invalidRecords = dealService.getViolationRecords(dealRecords);
        long validRecords = dealService.getDeslsWithoutViolations(dealRecords);
        long violationRecords  = dealRecords.size() - validRecords;


        StringBuilder sb = new StringBuilder(
                "Your sheet has been imported successfully  '" + file.getFileName() +
                        "<br /> importing time was : " + timeConsumed + " seconds" +
                        "<br /> number of valid records was : " + validRecords +
                        "<br /> number of violations records was : " + violationRecords +
                        "<br /> number of violations was : " + invalidRecords.size());
        return sb.toString();

    }


}
