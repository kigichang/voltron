package xv.voltron.core.data;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import xv.voltron.constant.ColumnType;
import xv.voltron.core.DataManager;
import xv.voltron.core.Operate;

public class MySQL<T> extends Operate<T> {

	public MySQL(Class<T> clazz) throws SQLException {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean add(T model) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer column = 
				new StringBuffer("INSERT INTO ")
				.append(tableName).append("(");
		
		StringBuffer values = 
				new StringBuffer(" VALUES (");
		
		ArrayList<Object> val = new ArrayList<Object>();
		ArrayList<ColumnType> type = new ArrayList<ColumnType>();
		ArrayList<Column> gen_keys = new ArrayList<Column>();
		
		int i = 0, len = columns.length;
		boolean has_auto_increment = false;
		Class<? extends Object> clazz = model.getClass();
		try {
			for (; i < len; i++) {
				if (!columns[i].isAutoIncrement) {
					column.append(columns[i].fieldName).append(',');
					values.append("?,");
					val.add(clazz.getMethod(columns[i].getter)
								.invoke(model, Operate.EMPTY_OBJECT_ARRAY));
					type.add(columns[i].type);
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
		boolean ret = false;
		try {
			conn = DataManager.getConnection(dataSource);
			stmt = conn.prepareStatement(column.append(values).toString(),
									 has_auto_increment ?
									 Statement.RETURN_GENERATED_KEYS :
									 Statement.NO_GENERATED_KEYS);
		
			for (i = 0, len = val.size(); i < len; i++) {
				stmt.setObject(i+1, val.get(i), type.get(i).value());
			}
			ret = stmt.executeUpdate() > 0;
			if (has_auto_increment) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					for (int k = 0, size = gen_keys.size(); k < size; k++) {
						Column col = gen_keys.get(k);
						Object v = rs.getObject(col.fieldName, col.type.toClass());
						clazz.getMethod(col.setter, col.type.toClass())
							.invoke(model, v);
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

}
