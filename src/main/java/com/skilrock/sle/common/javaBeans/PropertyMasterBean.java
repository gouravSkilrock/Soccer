package com.skilrock.sle.common.javaBeans;

import java.io.Serializable;

public class PropertyMasterBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String propertyCode;
	private String propertyDevName;
	private String propertyDisplayName;
	private String status;
	private String editable;
	private String value;
	private String valueType;
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public String getPropertyDevName() {
		return propertyDevName;
	}

	public void setPropertyDevName(String propertyDevName) {
		this.propertyDevName = propertyDevName;
	}

	public String getPropertyDisplayName() {
		return propertyDisplayName;
	}

	public void setPropertyDisplayName(String propertyDisplayName) {
		this.propertyDisplayName = propertyDisplayName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "PropertyMasterBean [id=" + id + ", propertyCode="
				+ propertyCode + ", propertyDevName=" + propertyDevName
				+ ", propertyDisplayName=" + propertyDisplayName + ", status="
				+ status + ", editable=" + editable + ", value=" + value
				+ ", valueType=" + valueType + ", description=" + description
				+ "]";
	}

}
