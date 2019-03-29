package com.progressoft.warehouse.entity.Interface;

/**
 * Created by EYAD on 3/6/2019.
 */

public interface DealRecord {

    String ID = "id";

    String RECORD_ID = "record_id";

    String FROM_CURRENCY = "from_currency";

    String TO_CURRENCY = "to_currency";

    String TIME_STAMP = "time_stamp";

    String FILE_ID = "file_Id";

    String AMOUNT = "Amount";

    long id = 0;
    String toCurrency = "";
    String fromCurrency = "";
    String timeStamp = "";
    long fileId = 0;
    String amount = "";
    String violations = "";

    long getId();

    String getFromCurrency();

    String getToCurrency();

    String getTimeStamp();

    long getFileId();

    String getAmount();

    String getViolations();

}
