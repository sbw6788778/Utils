package com.utils.ExcelUtils;

/**
 * Created by lichenglong on 2016/8/19.
 */
public abstract class FieldConverter {
	/**
	 * @param context 上下文对象, 该对象是执行doImport时传递进来的
	 * @param colIndex 当前需要转换的值所在位置. 从0开始. fieldNames[colIndex] 是列名. values[colIndex] 是待转换的值
	 * @param fieldNames 字段名称
	 * @param values Excel中的文本内容
	 * @return 返回转换后的对象. 验证或转换失败时应该抛出异常. Exception对象的message作为错误消息返回给用户
	 */
	public abstract Object convert(Object context, int colIndex, String fieldNames[], String[] values) throws Exception;
}
