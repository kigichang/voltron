package xv.voltron.core.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import xv.voltron.annotation.Table;
import xv.voltron.constant.ColumnType;
import xv.voltron.constant.Const;
import xv.voltron.constant.DataType;
import xv.voltron.core.Convention;
import xv.voltron.core.DataManager;
import xv.voltron.core.Model;

public abstract class Operate<T extends Model> {

	protected Class<T> clazz = null;
	
	protected String name = null;
	protected String tableName = null;
	protected String dataSource = null;
	protected boolean isPersistent = false;
	
	/**
	 * Data Field Name and Column pair
	 */
	protected HashMap<String, Column> columns = null;
	
	
	public Operate(Class<T> clazz, boolean persistent) throws SQLException {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			throw new SQLException("Table Annotation Not Found");
		}
		this.isPersistent = persistent;
		String t_name = table.name();
		String t_table_name = table.tableName();
		String t_data_source = table.dataSource();
		
		name = "".equals(t_name) ? clazz.getSimpleName() : t_name;
		
		tableName = "".equals(t_table_name) ?
						Convention.toUnderlineName(name) : t_table_name;
		
		dataSource = "".equals(t_data_source) ? 
						Const.DATA_DEFAULT : t_data_source;
		
		this.clazz = clazz;
		
		Field[] fields = clazz.getDeclaredFields();
		
		for (int i = 0, len = fields.length; i < len; i++) {
			xv.voltron.annotation.Field f = 
					fields[i].getAnnotation(xv.voltron.annotation.Field.class);
			
			if (f != null) {
				DataType f_type = DataType.valueOf(fields[i].getType());
				
				try {
					Column col = 
						new Column(clazz,
						  	       Convention.capitalize(fields[i].getName()),
						  	       f_type,
								   f);
					
					columns.put(col.fieldName, col);
					
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					columns.clear();
					throw new SQLException(e);
				}
			}
		}// for
	}
	
	public Operate(Class<T> clazz) throws SQLException {
		this(clazz, false);
	}
	
	protected Connection getConnection() throws SQLException {
		return isPersistent ?
				DataManager.getPersistent(dataSource):
				DataManager.getConnection(dataSource);
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
	
	public abstract int add(T model) throws SQLException;
	public abstract int update(T model) throws SQLException;
	public abstract T[] find(String condition, Object... values) throws SQLException;
	
}
