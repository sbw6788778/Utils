package com.utils.ExcelUtils;

import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    @Test
    public void readExcel(){
        try{
            Workbook workbook=WorkbookFactory.create(new File("E://二期库存.xlsx"));
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i= 0;i<numberOfSheets;i++){
                Sheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int lastRowNum = sheet.getLastRowNum();
                 for(int j=0;j<lastRowNum;j++){
                     Row row = sheet.getRow(j);
                     if(row==null){
                         log.info("行数：{}为空",j);
                         continue;
                     }
                     for(int n=0;n<row.getLastCellNum();n++){
                         Cell cell = row.getCell(n);
                         if (cell==null){
                             log.info("行数：{}cell：{}为空",j,n);
                             continue;
                         }
                         cell.setCellType(Cell.CELL_TYPE_STRING);
                         String stringCellValue = cell.getStringCellValue();
                          /*
                     todo
                      */
                     }
                 }
            }
        }catch (Exception e){
            log.error("读excel文件错误",e);
        }
    }

    public void writeExcel(){

    }
}
