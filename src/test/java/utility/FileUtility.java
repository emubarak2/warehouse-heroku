package utility;

import lombok.extern.java.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by EYAD on 2/18/2019.
 */
@Log
public class FileUtility {

    public static List<File> getFiles() {
        List<File> transactionFiles;
        File folder = new File(System.getProperty("user.dir") + "/files");
        File[] listOfFiles = folder.listFiles();
        transactionFiles = Arrays.asList(listOfFiles);
        return transactionFiles;
    }


    public static FileInputStream getFileInputStreamByName(String folderName, String fileName)  {
        List<File> transactionFiles;
        File file = new File(System.getProperty("user.dir") + "/files/" + folderName +"/"+ fileName);
        try {
         return new FileInputStream(file); }
         catch (FileNotFoundException e) {log.info(e.getMessage());}
         return null;
    }


}
