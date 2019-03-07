package com.utils.ExcelUtils.model;

import java.util.List;

/**
 * Created by lichenglong on 2016/8/19.
 */
public class ImportResult<T> {
	public static class ErrorInfo {
		private String[] keyFields;
		private String message;

		public String[] getKeyFields() {
			return keyFields;
		}

		public void setKeyFields(String[] keyFields) {
			this.keyFields = keyFields;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	private List<T> rows;
	private List<ErrorInfo> errors;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public List<ErrorInfo> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorInfo> errors) {
		this.errors = errors;
	}

}
