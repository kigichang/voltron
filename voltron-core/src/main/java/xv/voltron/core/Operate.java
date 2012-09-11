package xv.voltron.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import xv.voltron.annotation.Table;
import xv.voltron.constant.ColumnType;
import xv.voltron.constant.Const;
import xv.voltron.core.data.Column;

public abstract class Operate<T> {

	protected static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
	protected static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	
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
		
		String t_name = table.name();
		String t_table_name = table.tableName();
		String t_data_source = table.dataSource();
		name = "".equals(t_name) ? clazz.getSimpleName() : t_name;
		tableName = "".equals(t_table_name) ? 
				Convention.toUnderlineName(name) :
				t_table_name;
		
		dataSource = "".equals(t_data_source) ? 
				Const.DATA_DEFAULT :
				t_data_source;
		
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
				
				if ("".equals(f_name)) {
					f_name = fields[i].getName();
				}
				
				if ("".equals(f_field_name)) {
					f_field_name = Convention.toUnderlineName(f_name);
				}
				
				
				ColumnType f_type = null;
				Class<?> f_tmp = fields[i].getType();
				
				for (ColumnType ct : ColumnType.values()) {
					if (ct.compatiable(f_tmp)) {
						f_type = ct;
						break;
					}
				}
				
				if (f_type == null) {
					tmp.clear();
					throw new SQLException("Not Support Type " 
								+ f_tmp.getName());
				}
				/*if (f_tmp.equals(String.class)) {
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
					f_type = ColumnType.DATE;
				}
				else if (f_tmp.equals(java.sql.Timestamp.class)) {
					f_type = ColumnType.TIMESTAMP;
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
				}*/
				
				Column col = new Column(f_name, 
										f_field_name, 
										f_type, 
										f_def_val, 
										f.isPrimary(), 
										f.isAutoIncrement());
				
				tmp.add(col);
			}
		}// for
		
		columns = tmp.toArray(new Column[tmp.size()]);
	}
	
	protected void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	protected void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	protected void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	protected Object getValue(Class<? extends Object> clazz, T obj, String func) 
			throws IllegalAccessException, 
				   IllegalArgumentException, 
				   InvocationTargetException, 
				   NoSuchMethodException, SecurityException {
		
		return clazz.getMethod(func).invoke(obj);
	}
	
	protected void setValue(Class<? extends Object> clazz, 
							T obj, 
							String func, 
							Class<? extends Object> valType, 
							Object val) 
		throws IllegalAccessException, 
			   IllegalArgumentException, 
			   InvocationTargetException, 
			   NoSuchMethodException, 
			   SecurityException {
		
		clazz.getMethod(func, valType).invoke(obj, val);
	}
	
	public abstract int add(T model) throws SQLException;
	public abstract int update(T model) throws SQLException;
	public abstract T[] find(String condition, Object... values) throws SQLException;
	
}
