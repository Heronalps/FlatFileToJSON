package com.expicient.integration.schema;

public class FlatFileSchema {
	String fieldName;
	int startPosition;
	int length;

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the startPosition
	 */
	public int getStartPosition() {
		return startPosition;
	}

	/**
	 * @param startPosition
	 *            the startPosition to set
	 */
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	public FlatFileSchema() {
		fieldName = "";
		startPosition = 0;
		length = 0;
	}

	public FlatFileSchema(String fieldName, int start, int len) {
		this.fieldName = fieldName;
		this.startPosition = start;
		this.length = len;
	}

}
