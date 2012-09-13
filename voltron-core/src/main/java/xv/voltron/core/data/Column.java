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
	protected Class<?> clazz = null;
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
	
	public Column(Class<?> clazz, String name, DataType type, Field field) throws NoSuchMethodException, SecurityException {
		this.clazz = clazz;
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
	}
	
	public Object getValue(Object model) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return getter.invoke(model);
	}
	
	public void setValue(Object model, Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setter.invoke(model, val);
	}
	
	public Object defaultValue() throws ParseException {
		if ("".equals(defValue)) {
			return null;
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
	
}
