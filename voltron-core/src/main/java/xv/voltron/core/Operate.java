package xv.voltron.core;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import xv.voltron.annotation.Table;
import xv.voltron.constant.ColumnType;
import xv.voltron.core.data.Column;

public abstract class Operate<T> {

	protected Class<T> clazz = null;
	
	protected String name = null;
	protected String tableName = null;
	protected String dataSource = null;
	
	protected Column[] columns = null;
	
	public Operate(Class<T> clazz) throws SQLException {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			throw new SQLException("Table Annotation Not Found");
		}

		this.clazz = clazz;
		
		Field[] fields = clazz.getDeclaredFields();
		
		ArrayList<Column> tmp = new ArrayList<Column>();
		for (int i = 0, len = fields.length; i < len; i++) {
			xv.voltron.annotation.Field f = 
					fields[i].getAnnotation(xv.voltron.annotation.Field.class);
			
			if (f != null) {
				String f_name = f.name();
				String f_field_name = f.fieldName();
				String f_def_val = f.defValue();
				ColumnType f_type = null;
				Class f_tmp = fields[i].getType();
				
				if (f_tmp.equals(String.class)) {
					f_type = ColumnType.STRING;
				}
				else if (f_tmp.equals(BigDecimal.class)) {
					f_type = ColumnType.BIGDECIMAL;
				}
				else if (f_tmp.equals(Integer.class)) {
					f_type = ColumnType.INTEGER;
				}
				else if (f_tmp.equals(Long.class)) {
					f_type = ColumnType.LONG;
				}
				else if (f_tmp.equals(java.util.Date.class)) {
					f_type = ColumnType.DATETIME;
				}
				else if (f_tmp.equals(Character.class)) {
					f_type = ColumnType.CHAR;
				}
				else if (f_tmp.equals(Float.class)) {
					f_type = ColumnType.FLOAT;
				}
				else if (f_tmp.equals(Double.class)) {
					f_type = ColumnType.DOUBLE;
				}
				else if (f_tmp.equals(java.sql.Date.class)) {
					f_type = ColumnType.DATE;
				}
				else if (f_tmp.equals(java.sql.Time.class)) {
					f_type = ColumnType.TIME;
				}
				else {
					tmp.clear();
					throw new SQLException("Not Support Type " 
								+ f_tmp.getName());
				}
				
				Column col = new Column(f_name, f_field_name, f_type, f_def_val);
				tmp.add(col);
			}
		}// for
		
		columns = tmp.toArray(new Column[tmp.size()]);
	}
	
	
	
	public abstract boolean add(T model) throws SQLException;
	
	
}
