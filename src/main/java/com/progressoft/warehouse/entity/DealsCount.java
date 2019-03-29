package com.progressoft.warehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by EYAD on 3/8/2019.
 */

@Entity
@Table(name = "DEALS_COUNT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealsCount {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "from_currency")
    private String fromCurrency;

    @Column(name = "deals_count")
    private long count;


}



