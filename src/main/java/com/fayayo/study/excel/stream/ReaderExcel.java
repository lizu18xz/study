package com.fayayo.study.excel.stream;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.openxml4j.util.ZipEntrySource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;

/**
 * @author dalizu on 2019/8/20.
 * @version v1.0
 * @desc
 */
public class ReaderExcel {

    public static void main(String[] args) {

        try (
                InputStream is = new FileInputStream(new File("F:/Excel/sss.xlsx"));
                Workbook workbook = StreamingReader.builder()
                        .rowCacheSize(100)
                        .bufferSize(4096)
                        .open(is)) {
            for (Sheet sheet : workbook){
                System.out.println(sheet.getSheetName());
                for (Row r : sheet) {
                    for (Cell c : r) {
                        System.out.println(c.getStringCellValue());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
