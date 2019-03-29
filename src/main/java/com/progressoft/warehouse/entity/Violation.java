package com.progressoft.warehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Created by EYAD on 3/6/2019.
 */


@AllArgsConstructor
@Data
public class Violation {

    private long fileIndex;
    private String property;
    private String description;


}
