package com.progressoft.warehouse.exception;

/**
 * Created by EYAD on 3/10/2019.
 */
public class FileExitsException extends  Exception{

    public  FileExitsException() { super ("File is already imported."); }
}
