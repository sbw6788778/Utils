package com.utils.ExcelUtils.model;

import com.utils.ExcelUtils.FieldConverter;

/**
 * Created by lichenglong on 2016/8/19.
 */
public class FieldRule {
	private String fieldName;
	
	private String fieldBeanName;
	
	private FieldConverter converter;
	
	/**
	 * @param fieldName 字段名称.
	 * @param fieldBeanName 转换为Bean对象时Bean的属性名称
	 * @param converter null 表示使用默认的转换器. 默认转换器不做任何校验和加工, 直接返回数据.
	 */
	public FieldRule(String fieldName, String fieldBeanName, FieldConverter converter) {
		this.fieldName = fieldName;
		this.fieldBeanName = fieldBeanName;
		this.converter = converter;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldBeanName() {
		return fieldBeanName;
	}

	public void setFieldBeanName(String fieldBeanName) {
		this.fieldBeanName = fieldBeanName;
	}

	public FieldConverter getConverter() {
		return converter;
	}

	public void setConverter(FieldConverter converter) {
		this.converter = converter;
	}
}
