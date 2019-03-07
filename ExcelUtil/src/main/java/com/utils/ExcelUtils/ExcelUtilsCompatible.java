package com.utils.ExcelUtils;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by wangtingjs on 2019/2/22.
 */
public class ExcelUtilsCompatible {

    static final Long MAX_ROW = 17179869184L;
    static final int MAX_COL = 255;

    private static final String EXTENSION_XLS = "xls";
    private static final String EXTENSION_XLSX = "xlsx";

    /**
     * 解析excel，同时支持xlsx和xls
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbookFromFilePath(String filePath) throws Exception {
        if (StringUtils.isBlank(filePath)) {
            throw new Exception("filePath cannot be empty.");
        }
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(filePath);
            if (filePath.endsWith(EXTENSION_XLSX)) {
                workbook = new XSSFWorkbook(is);
            } else if (filePath.endsWith(EXTENSION_XLS)) {
                workbook = new HSSFWorkbook(is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception("the file does not exist:" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("transfer file to Workbook occur error:", e);
        } finally {
            if (null != is) {
                is.close();
            }
        }

        return workbook;
    }

    /**
     * 预检查excel
     *
     * @param filePath
     * @throws Exception
     * @author luweijs
     */
    public static void preCheckExcel(String filePath, long fileSizeLimit, long excelRowsLimit) throws Exception {

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("该路径的文件不存在" + filePath);
        }

        if (file.length() > fileSizeLimit) {
            throw new Exception("您上传的文件过大");
        }

        if (!(filePath.endsWith(EXTENSION_XLS) || filePath.endsWith(EXTENSION_XLSX))) {
            throw new FileFormatException("不存在该格式的excel");
        }

        Workbook workbook = null;
        //会抛出org.apache.poi.POIXMLException
        workbook = getWorkbookFromFilePath(filePath);

        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            throw new Exception("您上传excel文件为空");
        }

        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();

        if (lastRowIndex - firstRowIndex > excelRowsLimit) {
            throw new Exception("您上传的表格行数过多");
        }

        if (lastRowIndex - firstRowIndex <= 0) {
            throw new Exception("您上传的表格没有数据");
        }

    }

    /**
     * 写文件之前校验
     *
     * @param filePath
     * @throws FileNotFoundException
     * @throws FileFormatException
     * @author luweijs
     */
    private static File preWriteCheck(String filePath) throws FileNotFoundException, FileFormatException {

        File file = new File(filePath);

        if (!(filePath.endsWith(EXTENSION_XLS) || filePath.endsWith(EXTENSION_XLSX))) {
            throw new FileFormatException("不存在该格式的excel");
        }

        return file;
    }

    /***
     * @author luweijs xls:HSSFWorkbook xlsx:XSSFWorkbook
     * @param filePath
     * @return
     * @throws IOException
     */
    private static Workbook setWorkbook(String filePath) throws IOException {
        Workbook workbook = null;
        if (filePath.endsWith(EXTENSION_XLS)) {
            workbook = new HSSFWorkbook();
        } else if (filePath.endsWith(EXTENSION_XLSX)) {
            workbook = new XSSFWorkbook();
        }

        return workbook;
    }

    /**
     * 通过sheetNum获取Sheet
     *
     * @param workbook
     * @param sheetNum
     * @return
     * @throws Exception
     */
    public static Sheet getSheet(Workbook workbook, int sheetNum) throws Exception {

        if (null == workbook) {
            throw new Exception("workbook cannot be null.");
        }

        return workbook.getSheetAt(sheetNum);
    }

    /**
     * 通过sheetName获取Sheet
     *
     * @param workbook
     * @param sheetName
     * @return
     * @throws Exception
     */
    public static Sheet getSheet(Workbook workbook, String sheetName) throws Exception {

        if (null == workbook) {
            throw new Exception("workbook cannot be null.");
        }

        return workbook.getSheet(sheetName);
    }

    /**
     * 最大行和最大列校验
     *
     * @param col
     * @param row
     * @throws Exception
     */
    private static void basicRowAndColCheck(int col, int row) throws Exception {

        if (row > MAX_ROW) {
            throw new Exception("row number cannot be greater than " + MAX_ROW);
        }

        if (row < 0) {
            throw new Exception("row number cannot be less than 0");
        }

        if (col > MAX_COL) {
            throw new Exception("row number cannot be greater than " + MAX_COL);
        }

        if (col < 0) {
            throw new Exception("col number cannot be less than 0");
        }
    }

    /**
     * 通过行和列获取Cell值
     *
     * @param sheet
     * @param col
     * @param row
     * @return
     * @throws Exception
     */
    public static String getCellValue(Sheet sheet, int col, int row) throws Exception {

        basicRowAndColCheck(col, row);
        Row rowNow = sheet.getRow(row);
        if (null == rowNow) {
            throw new Exception("the cell dose not exist:row=" + row + ",col=" + col);
        }
        Cell cell = rowNow.getCell(col);
        if (null == cell) {
            throw new Exception("the cell dose not exist:row=" + row + ",col=" + col);
        }

        //判断cell类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    return sdf.format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getRichStringCellValue().getString();
        }
    }

    /**
     * 通过Cell获取当前excel格子的值
     *
     * @param cell
     * @param
     * @return
     * @author luweijs
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        cell.setCellType(Cell.CELL_TYPE_STRING);

        return cell.getStringCellValue();
    }

    /**
     * 读取路径参数，解析excel为List<String>
     *
     * @param filePath
     * @throws FileNotFoundException
     * @throws FileFormatException
     * @author luweijs
     */
    public static List<String> readExcelToString(String filePath) throws Exception {

        Workbook workbook = null;
        List<String> idStrList = new LinkedList<String>();

        try {
            workbook = getWorkbookFromFilePath(filePath);

            Sheet sheet = workbook.getSheetAt(0);

            int firstRowIndex = sheet.getFirstRowNum();
            int lastRowIndex = sheet.getLastRowNum();

            Row firstRow = sheet.getRow(firstRowIndex);
            int ColumnIndex = firstRow.getFirstCellNum();

            //从第二行开始获取商品id
            for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                if (null == currentRow) {
                    continue;
                }

                Cell currentCell = currentRow.getCell(ColumnIndex);

                if (null == currentCell) {
                    continue;
                }

                String currentCellValue = getCellValue(currentCell);

                if (currentCellValue == null || "".equals(currentCellValue.trim())) {
                    continue;
                }

                idStrList.add(currentCellValue);
            }

            return idStrList;

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     //     * @param filePath excel文件路径
     //     * @return 每一个cell的数据
     //     */
    public static List<String[]> readExcelToStringArray(String filePath) throws Exception {
        Workbook workbook = null;
        List<String[]> idStrList = Lists.newArrayList();
        try {
            workbook = getWorkbookFromFilePath(filePath);
            Sheet sheet = workbook.getSheetAt(0);
            int firstRowIndex = sheet.getFirstRowNum();
            int lastRowIndex = sheet.getLastRowNum();
            Row firstRow = sheet.getRow(firstRowIndex);
            int headBegin = firstRow.getFirstCellNum();
            int headEnd = firstRow.getLastCellNum();
            //从第二行开始获取商品id
            for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
                String[] rowContent = new String[headEnd];
                Row currentRow = sheet.getRow(rowIndex);
                if (null == currentRow) {
                    continue;
                }
                for (int i = headBegin; i < headEnd; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    if (null == currentCell) {
                        continue;
                    }
                    if (currentCell.getCellType() != Cell.CELL_TYPE_STRING) {
                        currentCell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    rowContent[i] = currentCell.getStringCellValue();
                }
                idStrList.add(rowContent);
            }
            return idStrList;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断当前行是否有值
     *
     * @param sheet
     * @param row
     * @return
     */
    public static boolean hasRowData(Sheet sheet, int row) {

        if (null == sheet) {
            return false;
        }
        if (row > MAX_ROW || row < 0) {
            return false;
        }

        for (int col = 0; col < MAX_COL; col++) {

            try {

                Cell cell = sheet.getRow(row).getCell(col);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                if (null != cell && !"".equals(cell.getRichStringCellValue().getString())) {
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

                return false;
            }
        }

        return false;
    }

    /**
     * 创建Workbook
     *
     * @return
     * @throws Exception
     */
    public static Workbook createWorkbook() throws Exception {

        return new SXSSFWorkbook();
    }

    /**
     * 创建Sheet
     *
     * @param workbook
     * @param sheetName
     * @param sheetNum
     * @return
     * @throws Exception
     */
    public static Sheet createSheet(Workbook workbook, String sheetName, int sheetNum) throws Exception {

        if (null == workbook) {
            throw new Exception("workbook cannot be null.");
        }
        workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetName);
        return workbook.getSheetAt(sheetNum);
    }

    /**
     * 新增Cell
     *
     * @param sheet
     * @param col
     * @param row
     * @param data
     * @throws Exception
     */
    public static void addCell(Sheet sheet, int col, int row, Object data) throws Exception {

        if (null == sheet) {
            throw new Exception("sheet cannot be null.");
        }

        if (null == data) {
            return;
        }
        basicRowAndColCheck(col, row);
        Row rowNow = sheet.getRow(row);
        if (null == rowNow) {
            rowNow = sheet.createRow(row);
        }
        Cell cell = rowNow.getCell(col);
        if (null == cell) {
            cell = rowNow.createCell(col);
        }

        if (data instanceof String) {
            cell.setCellValue((String) data);
        } else if (data instanceof Integer) {
            cell.setCellValue((Integer) data);
        } else if (data instanceof Long) {
            cell.setCellValue((Long) data);
        } else if (data instanceof Short) {
            cell.setCellValue((Short) data);
        } else if (data instanceof Double) {
            cell.setCellValue((Double) data);
        } else if (data instanceof Date) {
            cell.setCellValue((Date) data);
        } else {
            cell.setCellValue(data.toString());
        }
    }

    /**
     * 设置Cell的值
     *
     * @param cell
     * @param
     * @return
     */
    private static void setCellValue(Cell cell, String strInput) {

        if (cell == null || strInput == null) {
            return;
        }

        cell.setCellValue(strInput);

        return;
    }

    /**
     * 将workbook内容写入file中
     *
     * @param workbook
     * @param os       流文件
     * @return
     * @throws IOException
     */
    public static boolean writeToExcelByStream(Workbook workbook, OutputStream os) throws IOException {
        try {
            workbook.write(os);
            return true;
        } finally {
            if (os != null) {
                os.close();
                workbook.close();
            }
        }
    }

    /**
     * 将workbook内容写入file中
     *
     * @param workbook
     * @param filePath
     * @return
     * @throws IOException
     */
    public static boolean writeToExcelByPath(Workbook workbook, String filePath) throws IOException {
        String dirName = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1);
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            workbook.write(os);
            return true;
        } finally {
            if (os != null) {
                os.close();
                workbook.close();
            }
        }
    }

    /**
     * @param filePath        需要写的excel的路径
     * @param content         内容
     * @param firstRowContent 第一行的内容
     * @return 是否写入成功
     * @throws IOException 文件写入异常
     */
    public static void writeExcel(String filePath, List<String[]> content, String[] firstRowContent)
            throws Exception {
        String dirName = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1);
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        // 创建sxssf工作薄
        Workbook workbook = createWorkbook();
        // 创建excle表
        Sheet sheet = workbook.createSheet();
        int currentRowNums = 0;
        if (CollectionUtils.isEmpty(content)) {
            return;
        }
        // 填充第一行的内容
        writeRow(firstRowContent, sheet.createRow(currentRowNums++));
        //写其余行
        for (String[] rowContent : content) {
            writeRow(rowContent, sheet.createRow(currentRowNums++));
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            workbook.write(os);
        } catch (Exception e) {
            throw new Exception("write workbook error");
        } finally {
            if (os != null) {
                try {
                    os.close();
                    workbook.close();
                } catch (IOException e) {
                    throw new Exception("writeExcel close stream error");
                }
            }
        }
    }

    /**
     * excel中一行填充数据
     *
     * @param msgs
     * @param row
     */
    private static void writeRow(String[] msgs, Row row) {
        for (int i = 0; i < msgs.length; i++) {
            Cell cell = row.createCell(i);
            if (msgs[i] != null) {
                cell.setCellValue(msgs[i]);
            }
        }
    }

    /**
     * @param filePath
     * @throws IOException
     */
    public static void writeExcel(String filePath, String[][] strExeclContent, String[] strExcelFirstRowContent)
            throws IOException {
        // 检查路径
        File file = preWriteCheck(filePath);

        // 创建工作薄
        Workbook workbook = setWorkbook(filePath);

        // 创建excle表
        Sheet sheet = workbook.createSheet();

        int rowNums = 0;
        if (null != strExeclContent) {
            rowNums = strExeclContent.length;
        }

        int columnNums = strExcelFirstRowContent.length;

        // 填充第一行的内容
        Row row = sheet.createRow(0);
        for (int i = 0; i < columnNums; i++) {
            Cell cell = row.createCell(i);
            setCellValue(cell, strExcelFirstRowContent[i]);
        }

        for (int rowIndex = 0; rowIndex < rowNums; rowIndex++) {
            Row rows = sheet.createRow(rowIndex + 1);
            for (int columnIndex = 0; columnIndex < columnNums; columnIndex++) {
                Cell cell = rows.createCell(columnIndex);
                setCellValue(cell, strExeclContent[rowIndex][columnIndex]);
            }
        }

        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            workbook.write(os);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return;
    }

//    public static void main(String[] args) {
//        try {
//            //读取
//            long now = System.currentTimeMillis();
//            Workbook workbookRead = getWorkbookFromFilePath("F:\\MyDrivers\\学习文件\\天猫相关\\产商品发布重构\\excel\\product_id.xlsx");
//            Sheet sheetRead = getSheet(workbookRead, 0);
//            int row = 1;
//            System.out.println("haha");
//            while (hasRowData(sheetRead, row)) {
//                System.out.println(getCellValue(sheetRead, 0, row));
//                row++;
//            }
//            System.out.println("共耗时" + (System.currentTimeMillis() - now));
//
//            //写入
//            Workbook workbookWrite = createWorkbook();
//            Sheet sheetWrite = createSheet(workbookWrite, "order", 0);
//            // 写表头
//            addCell(sheetWrite, 0, 0, "TM-订单号");
//            addCell(sheetWrite, 1, 0, "TM-省");
//            addCell(sheetWrite, 2, 0, "TM-市");
//            addCell(sheetWrite, 3, 0, "TM-区/县");
//            addCell(sheetWrite, 4, 0, "TM-街道地址");
//            addCell(sheetWrite, 5, 0, "付款时间");
//            addCell(sheetWrite, 6, 0, "抓取时间");
//            addCell(sheetWrite, 7, 0, "店铺");
//            addCell(sheetWrite, 8, 0, "问题原因");
//            addCell(sheetWrite, 9, 0, "状态");
//            writeToExcel(workbookWrite,"F:\\MyDrivers\\学习文件\\天猫相关\\产商品发布重构\\excel\\product_idWrite.xlsx");
//            System.out.println("共耗时" + (System.currentTimeMillis() - now));
//        } catch (Exception e) {
//            System.out.println("又错了！" + e);
//        }
//    }
}
