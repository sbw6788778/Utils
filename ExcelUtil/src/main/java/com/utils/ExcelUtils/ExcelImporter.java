package com.utils.ExcelUtils;


import com.utils.ExcelUtils.model.FieldRule;
import com.utils.ExcelUtils.model.ImportResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lichenglong on 2016/8/19.
 */
public class ExcelImporter {

    private static final Logger logger = LoggerFactory.getLogger(ExcelImporter.class);

    private String uniqueKey;

    private String version;

    private List<FieldRule> rules;

    /**
     * 创建新的Importer对象.
     *
     * @param uniqueKey Excel模板的唯一识别名称. Excel文档的第一行第一列应该为唯一识别名称
     * @param version   模板的版本. Excel文档的第一行第二列应该为唯一识别名称
     * @param rules     字段规则. 规则中包含字段名称, Bean属性名称, 数据校验转换器(负责校验和转换)
     */
    public ExcelImporter(String uniqueKey, String version, List<FieldRule> rules) {
        this.uniqueKey = uniqueKey;
        this.version = version;
        this.rules = rules;
    }

    /**
     * 从Excel文件导入数据, 返回数据. 支持多线程同时调用
     */
    public ImportResult<String> doImport(File file, Object context) throws Exception {
        return doImport(file, String.class, context, true);
    }

    public ImportResult<String> doImport(InputStream is, Object context) throws Exception {
        return doImport(is, String.class, context, true);
    }

    public ImportResult<String> doImport(byte[] buf, Object context) throws Exception {
        return doImport(buf, String.class, context, true);
    }

    public <T> ImportResult<T> doImport(File file, Class<T> clazz, Object context, boolean generateExcelResult) throws Exception {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return doImport(is, clazz, context, generateExcelResult);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 从Excel文件导入数据, 返回数据. 支持多线程同时调用
     *
     * @param is                  输入流
     * @param clazz               Excel一行值的bean
     * @param context             此context将会传递给FieldValidator, 有助于validator实现者获取上下文环境
     * @param generateExcelResult Excel处理结果导出开关
     * @return Excel导出后的结果
     * @throws Exception
     */
    public <T> ImportResult<T> doImport(InputStream is, Class<T> clazz, Object context, boolean generateExcelResult) throws Exception {
        // 使用WorkbookFactory根据excel的版本类型生成相应workbook
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);
        int rowNumber = sheet.getLastRowNum();
        int colNumber = sheet.getRow(1).getPhysicalNumberOfCells();

        // Excel中的字段数组
        String[] fieldNames = new String[colNumber];
        // ruleIndex和colIndex的对应关系
        Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
        // 返回结果
        ImportResult<T> result = new ImportResult<T>();
        ArrayList<T> rows = new ArrayList<T>();
        ArrayList<ImportResult.ErrorInfo> errors = new ArrayList<ImportResult.ErrorInfo>();

        // 模板校验
        if (!templateCheck(uniqueKey, version, sheet)) {
            rows.add(null);

            ImportResult.ErrorInfo errorInfo = new ImportResult.ErrorInfo();
            errorInfo.setMessage("模板使用错误，请使用uniqueKey为" + uniqueKey
                    + "，version为" + version + "的模板。");
            errorInfo.setKeyFields(null);
            errors.add(errorInfo);

            result.setErrors(errors);
            result.setRows(rows);
            return result;
        }

        // 获取Excel字段数组
        for (int colIndex = 0; colIndex < colNumber; colIndex++) {
            fieldNames[colIndex] = getCellValue(sheet.getRow(1).getCell(colIndex));
        }

        // 将colIndex和ruleIndex进行绑定
        for (int colIndex = 0; colIndex < colNumber; colIndex++) {
            for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
                if (fieldNames[colIndex].equals(rules.get(ruleIndex).getFieldName())) {
                    indexMap.put(ruleIndex, colIndex);
                }
            }
        }

        // 从第6行开始读取数据
        for (int rowIndex = 5; rowIndex <= rowNumber; rowIndex++) {
            // 与Excel字段对应的bean
            T bean = clazz.newInstance();

            Row currentRow = sheet.getRow(rowIndex);
            // 处理空行
            boolean isNullRow = hasNullRow(currentRow, colNumber);
            if (true == isNullRow) {
                continue;
            }
            // 存储excel一行数据的数组
            String[] rowValues = new String[colNumber];
            // 经过规则转换处理后的值
            Object value = null;
            // 存储错误信息
            StringBuilder errorMsg = new StringBuilder();

            for (int colIndex = 0; colIndex < colNumber; colIndex++) {
                rowValues[colIndex] = getCellValue(currentRow.getCell(colIndex));
            }

            for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
                Integer colIndex = indexMap.get(ruleIndex);
                // 处理excel中不存在字段的转换规则
                if (null == colIndex) {
                    continue;
                }
                FieldConverter converter = rules.get(ruleIndex).getConverter();
                if (null != converter) {
                    try {
                        value = converter.convert(context, colIndex,
                                fieldNames, rowValues);
                    } catch (Exception e) {
                        logger.error(e.toString());
                        errorMsg.append(e.getMessage()).append('\n');
                        continue;
                    }
                } else {
                    value = rowValues[colIndex];
                    if (null == value || value.toString().trim().equals("")) {
                        String msg = "存在未输入的数据";
                        logger.error(msg);
                        errorMsg.append(msg).append('\n');
                        continue;
                    }
                }

                BeanUtils.setProperty(bean, rules.get(ruleIndex).getFieldBeanName(), value);

            }

            if (errorMsg.length() == 0) {// 转换过程中没有错误
                rows.add(bean);
                errors.add(null);
            } else {// 转换过程中有错误
                rows.add(null);
                ImportResult.ErrorInfo errorInfo = new ImportResult.ErrorInfo();
                errorInfo.setMessage(errorMsg.toString());
                errorInfo.setKeyFields(rowValues);
                errors.add(errorInfo);
            }
            result.setRows(rows);
            result.setErrors(errors);
        }

        return result;
    }

    public <T> ImportResult<T> doImport(byte[] buf, Class<T> clazz, Object context, boolean generateExcelResult) throws Exception {
        InputStream is = new ByteArrayInputStream(buf);
        return doImport(is, clazz, context, generateExcelResult);
    }

    /**
     * 将单元格里面的数据转换成String类型，并去掉两头的空格
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return org.apache.commons.lang.StringUtils.trim(cell.getStringCellValue());
    }

    /**
     * 校验模板是否正确
     *
     * @param uniqueKey Excel的唯一主键
     * @param version   Excel的版本号
     * @param sheet
     * @return 模板是否正确
     */
    private boolean templateCheck(String uniqueKey, String version, Sheet sheet) {
        if (null == uniqueKey || null == version || null == sheet) {
            return false;
        }
        if (uniqueKey.equals(getCellValue(sheet.getRow(0).getCell(0)))
                && version.equals(getCellValue(sheet.getRow(0).getCell(1)))) {
            return true;
        }
        return false;
    }

    /**
     * 判断excel表格中的空行问题
     *
     * @param currentRow excel当前行
     * @return
     */
    private boolean hasNullRow(Row currentRow, int colNumber) {
        if (null == currentRow) {
            return true;
        }
        for (int i = 0; i < colNumber; i++) {
            if (!(getCellValue(currentRow.getCell(i)).equals(""))) {
                return false;
            }
        }
        return true;
    }
}
