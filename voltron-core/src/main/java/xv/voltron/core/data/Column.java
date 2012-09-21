package xv.voltron.core.data;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;

import xv.voltron.annotation.Field;
import xv.voltron.constant.Const;
import xv.voltron.constant.DataType;
import xv.voltron.core.Convention;

public final class Column {
	protected String name = null;
	protected String fieldName = null;
	protected DataType type = null;
	protected String defValue = null;
	protected String expression = null;
	protected boolean isPrimary = false;
	protected boolean isAutoIncrement = false;
	protected boolean isExpression = false;
	protected Method setter = null;
	protected Method getter = null;
	
	protected String columnLabel = null;
	protected String fieldLabel = null;
	protected String displayLabel = null;
	
	public Column(Class<?> clazz, String name, DataType type, Field field) 
			throws NoSuchMethodException, SecurityException {
		
		//this.clazz = clazz;
		String tmp = clazz.getSimpleName();
		this.name = name;
		this.type = type;
		
		this.fieldName = "".equals(field.fieldName()) ?
				Convention.toUnderlineName(name) :
				field.fieldName();
				
		this.defValue = field.defValue();
		this.expression = field.expression();
		this.isPrimary = field.isPrimary();
		this.isAutoIncrement = field.isAutoIncrement();
		this.isExpression = !"".equals(this.expression);
		
		setter = clazz.getMethod("set" + name, type.toClass());
		getter = clazz.getMethod(
					(type.compatible(Boolean.class) ? "is" : "get") + name);
		
		/* for db display label */
		displayLabel = 
				new StringBuffer(tmp).append('_').append(name).toString();
		
		/* for db column name */
		columnLabel = 
				new StringBuffer(tmp).append('.').append(fieldName).toString();
		
		/* for class field name */
		fieldLabel = 
				new StringBuffer(tmp).append('.').append(name).toString();
		
	}
	
	public Object getValue(Object model) 
			throws IllegalAccessException, 
					IllegalArgumentException, 
					InvocationTargetException {
		
		return getter.invoke(model);
	}
	
	public void setValue(Object model, Object val) 
			throws IllegalAccessException, 
					IllegalArgumentException, 
					InvocationTargetException {
		
		setter.invoke(model, val);
	}
	
	public Object defaultValue() throws ParseException {
		if ("".equals(defValue)) {
			throw new ParseException("Can Not Parse Null", 0);
		}
		else if (Const.DEFAULT_TIME_VALUE.equals(defValue)) {
			switch(type) {
			case TIMESTAMP:
				return new Timestamp(System.currentTimeMillis());
			case DATE:
				return new java.sql.Date(System.currentTimeMillis());
			case TIME:
				return new java.sql.Time(System.currentTimeMillis());
			default:
				return null;
			}
		}
		else {
			return type.parseValue(defValue);
		}
		
	}

	public Object parseValue(String value) throws ParseException {
		return type.parseValue(value);
	}
}
