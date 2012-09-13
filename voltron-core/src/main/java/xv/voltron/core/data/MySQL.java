package xv.voltron.core.data;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import xv.voltron.constant.ColumnType;
import xv.voltron.core.DataManager;

public class MySQL<T> extends Operate<T> {

	public MySQL(Class<T> clazz) throws SQLException {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int add(T model) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer column = 
				new StringBuffer("INSERT INTO ")
				.append(tableName).append("(");
		
		StringBuffer values = 
				new StringBuffer(" VALUES (");
		
		ArrayList<Object> val = new ArrayList<Object>();
		ArrayList<ColumnType> type = new ArrayList<ColumnType>();
		ArrayList<Column> gen_keys = new ArrayList<Column>();
		
		int i = 0, len = columns
		boolean has_auto_increment = false;

		try {
			for (; i < len; i++) {
				if (!columns[i].isAutoIncrement) {
					Object val_tmp = columns[i].getValue(model);
					if (val_tmp != null) {
						column.append(columns[i].fieldName).append(',');
						values.append("?,");
						val.add(val_tmp);
						type.add(columns[i].type);
						
					}
				}
				else {
					gen_keys.add(columns[i]);
					has_auto_increment = true;
				}
			}
		}
		catch (IllegalAccessException
				| IllegalArgumentException 
				| InvocationTargetException
				| NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			val.clear();
			type.clear();
			val = null;
			type = null;
			column = null;
			values = null;
			new SQLException("Invoke Error " + columns[i].getter + " " + e);
		} 
		
		column.setCharAt(column.length() - 1, ')');
		values.setCharAt(column.length() - 1, ')');
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int ret = -1;
		try {
			conn = DataManager.getConnection(dataSource);
			stmt = conn.prepareStatement(column.append(values).toString(),
									 has_auto_increment ?
									 Statement.RETURN_GENERATED_KEYS :
									 Statement.NO_GENERATED_KEYS);
		
			for (i = 0, len = val.size(); i < len; i++) {
				stmt.setObject(i+1, val.get(i), type.get(i).toSQLType());
			}
			ret = stmt.executeUpdate();
			if (has_auto_increment) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					for (int k = 0, size = gen_keys.size(); k < size; k++) {
						Column col = gen_keys.get(k);
						Object v = rs.getObject(col.fieldName, col.type.toClass());
						setValue(clazz, model, col.setter, col.type.toClass(), v);
					}
				}
				else {
					throw new SQLException("Generated Key Error");
				}
			}
			
		} catch (IllegalAccessException
				| IllegalArgumentException 
				| InvocationTargetException
				| NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			val.clear();
			type.clear();
			val = null;
			type = null;
			column = null;
			values = null;
			new SQLException(e);
		}
		finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(conn);
		}
		
		
		val.clear();
		type.clear();
		val = null;
		type = null;
		column = null;
		values = null;
		
		return ret;
	}

	@Override
	public int update(T model) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer column = new StringBuffer("UPDATE ")
			.append(tableName).append(" SET ");
		
		StringBuffer where = new StringBuffer(" WHERE ");
		boolean findPrimary = false;
		int i = 0, len = columns.length;
		ArrayList<Object> val = new ArrayList<Object>();
		ArrayList<ColumnType> type = new ArrayList<ColumnType>();
		
		try {
			for (; i < len; i++) {
				Object val_tmp = getValue(clazz, model, columns[i].getter);
				if (val_tmp != null) {
					if (columns[i].isPrimary) {
						findPrimary = true;
						where.append(columns[i].fieldName)
							.append("=?,");
					}
					else {
						column.append(columns[i].fieldName)
							.append("=?,");
					}
					val.add(val_tmp);
					type.add(columns[i].type);
				}
			}
		}
		catch (IllegalAccessException
				| IllegalArgumentException 
				| InvocationTargetException
				| NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			val.clear();
			type.clear();
			val = null;
			type = null;
			column = null;
			new SQLException("Invoke Error " + columns[i].getter + " " + e);
		}
		
		if (!findPrimary) {
			val.clear();
			type.clear();
			val = null;
			type = null;
			column = null;
			new SQLException("No Primary Value Find, DO Nothing for SECURITY");
		}
		
		column.setLength(column.length() - 1);
		column.append(where.substring(0, where.length() - 1));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		int ret = -1;
		try {
			conn = DataManager.getConnection(dataSource);
			stmt = conn.prepareStatement(column.toString());
		
			for (i = 0, len = val.size(); i < len; i++) {
				stmt.setObject(i+1, val.get(i), type.get(i).toSQLType());
			}
			ret = stmt.executeUpdate();
			
		} catch (IllegalArgumentException | SecurityException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			val.clear();
			type.clear();
			val = null;
			type = null;
			column = null;
			new SQLException(e);
		}
		finally {
			closeStatement(stmt);
			closeConnection(conn);
		}
		
		val.clear();
		type.clear();
		val = null;
		type = null;
		column = null;
		
		return ret;
	}

	@Override
	public T[] find(String condition, Object... values) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer select = new StringBuffer("SELECT ");
		int i = 0, len = columns.length;
		for (; i < len; i++) {
			select.append(columns[i].fieldName)
				.append(" AS ")
				.append(columns[i].name)
				.append(',');
		}
		
		select.setCharAt(select.length() - 1, ' ');
		select.append(" FROM ").append(tableName);
		select.append(" WHERE ").append(condition);
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DataManager.getConnection(dataSource);
			stmt = conn.prepareStatement(select.toString());
			
			for (i = 0, len = values.length / 2; i < len; i++) {
				ColumnType ct = (ColumnType)values[i << 1];
				String val = (String)values[(i << 1) + 1];
				stmt.setObject(i + 1, ct.parseValue(val), ct.toSQLType());
			}
			
			rs = stmt.executeQuery();
			ArrayList<T> tmp = new ArrayList<T>();
			while(rs.next()) {
				
			}
			
		}
		catch (ParseException e) {
			throw new SQLException(e);
		}
		finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(conn);
		}
		
	}
	

}
