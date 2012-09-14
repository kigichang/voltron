package xv.voltron.core.data;

import java.text.ParseException;

import xv.voltron.constant.DataType;

public class DataValue {

	DataType type = null;
	String value = null;
	
	public DataValue(DataType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public Object getValue() throws ParseException {
		return type.parseValue(value);
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public DataType getType() {
		return type;
	}
}
