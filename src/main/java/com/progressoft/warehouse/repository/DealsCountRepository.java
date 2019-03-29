package com.progressoft.warehouse.repository;

import com.progressoft.warehouse.entity.DealsCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by EYAD on 3/8/2019.
 */
@Repository
public interface DealsCountRepository extends JpaRepository<DealsCount, Long> {

}
