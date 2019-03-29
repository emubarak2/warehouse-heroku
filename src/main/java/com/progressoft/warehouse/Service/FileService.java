package com.progressoft.warehouse.Service;

import com.progressoft.warehouse.exception.FileExitsException;
import com.progressoft.warehouse.repository.FileRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by EYAD on 3/6/2019.
 */
@Service
public class FileService {

    @Autowired
    FileRecordRepository fileRecordRepository;

    public synchronized long getNextFileId(String fileName) throws FileExitsException {
        if (fileRecordRepository.getFileRecordByFileName(fileName) != null) {
            throw new FileExitsException();
        } else {
            fileRecordRepository.addFileName(fileName);
            return fileRecordRepository.getMaxFileId();
        }
    }

}
