package com.progressoft.warehouse.repository;

import com.progressoft.warehouse.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by EYAD on 3/6/2019.
 */
@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO FILE_NAME (file_name) VALUES ( :fileName )", nativeQuery = true)
    void addFileName(@Param("fileName") String fileName);

    @Query(value = "SELECT DISTINCT(file_name),* FROM FILE_NAME ", nativeQuery = true)
    FileRecord getFileName(@Param("fileIndex") String fileName);

    @Query(value = " SELECT * FROM FILE_NAME WHERE file_name = :fileName LIMIT 1", nativeQuery = true)
    FileRecord getFileRecordByFileName(@Param("fileName") String fileName);

    @Query(value = "SELECT MAX(id) FROM FILE_NAME", nativeQuery = true)
    Long getMaxFileId();

}
