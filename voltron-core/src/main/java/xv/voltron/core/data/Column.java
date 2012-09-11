package xv.voltron.core.data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import xv.voltron.constant.ColumnType;
import xv.voltron.core.Convention;

public final class Column {
	protected String name = null;
	protected String fieldName = null;
	protected ColumnType type = null;
	protected String defValue = null;
	protected boolean isPrimary = false;
	protected boolean isAutoIncrement = false;
	protected String setter = null;
	protected String getter = null;
	
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
		
		setter = "set" + name;
		getter = (ColumnType.BOOLEAN == type ? "is" : "get") + name;
		
	}
	
	public Column(String name, String fieldName, ColumnType type, String defValue) {
		this(name, fieldName, type, defValue, false, false);
	}
	
	public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
		type.setStatement(stmt, index, val);
	}
	
	public Object getResult(ResultSet rs) throws SQLException {
		return type.getResult(rs, fieldName);
	}
	
}
