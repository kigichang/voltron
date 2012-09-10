package xv.voltron.core.data;

import org.apache.commons.lang.StringUtils;

import xv.voltron.constant.ColumnType;

public final class Column {
	protected String name = null;
	protected String fieldName = null;
	protected ColumnType type = null;
	protected String defValue = null;
	protected boolean isPrimary = false;
	protected boolean isAutoIncrement = false;
	protected String settor = null;
	protected String gettor = null;
	
	public Column(String name, 
				  String fieldName, 
				  ColumnType type, 
				  String defValue, 
				  boolean isPrimary, 
				  boolean isAutoIncrement) {
		
		this.name = name;
		this.fieldName = fieldName;
		this.type = type;
		this.defValue = defValue;
		this.isPrimary = isPrimary;
		this.isAutoIncrement = isAutoIncrement;
		
	
		
	}
	
	public Column(String name, String fieldName, ColumnType type, String defValue) {
		this(name, fieldName, type, defValue, false, false);
	}
	
	
}
