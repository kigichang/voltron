package xv.voltron.core.data;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import xv.voltron.constant.Const;
import xv.voltron.constant.DataType;
import xv.voltron.core.DataManager;
import xv.voltron.core.Model;

public class MySQL<T extends Model> extends Operator<T> {

	public MySQL(Class<T> clazz, boolean persistent) throws SQLException {
		super(clazz, persistent);
	}
	
	public MySQL(Class<T> clazz) throws SQLException {
		super(clazz);

	}
	
	@Override
	public int add(T model) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer column = 
				new StringBuffer("INSERT INTO ")
				.append(schema.tableName).append("(");
		
		StringBuffer values = 
				new StringBuffer(" VALUES (");
		
		ArrayList<Object> val = new ArrayList<Object>();
		ArrayList<DataType> type = new ArrayList<DataType>();
		ArrayList<Column> gen_keys = new ArrayList<Column>();
		
		
		boolean has_auto_increment = false;

		try {
			for (Column col : schema.columns.values()) {
				if (col.isExpression) {
					continue;
				}
				
				if (!col.isAutoIncrement) {
					Object val_tmp = col.getValue(model);
					if (val_tmp == null) {
						val_tmp = col.defaultValue();
					}
					
					if (val_tmp != null) {
						column.append(col.fieldName).append(',');
						values.append("?,");
						val.add(val_tmp);
						type.add(col.type);
					}
				}
				else {
					gen_keys.add(col);
					has_auto_increment = true;
				}
			}
		}
		catch (IllegalAccessException
				| IllegalArgumentException 
				| InvocationTargetException
				| SecurityException
				| ParseException e) {
			// TODO Auto-generated catch block
			val.clear();
			type.clear();
			val = null;
			type = null;
			column = null;
			values = null;
			new SQLException(e);
		}
		
		column.setCharAt(column.length() - 1, ')');
		values.setCharAt(column.length() - 1, ')');
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int ret = -1;
		try {
			conn = getConnection();
			if(!model.beforeInsert(conn)) {
				return -1;
			}
			
			stmt = conn.prepareStatement(column.append(values).toString(),
									 has_auto_increment ?
									 Statement.RETURN_GENERATED_KEYS :
									 Statement.NO_GENERATED_KEYS);
		
			for (int i = 0, len = val.size(); i < len; i++) {
				type.get(i).setParam(stmt, i + 1, val.get(i));
			}
			
			ret = stmt.executeUpdate();
			if (has_auto_increment) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					for (Column col : gen_keys) {
						Object v = col.type.getResult(rs, col.fieldName);
						col.setValue(model, v);
					}
				}
				else {
					throw new SQLException("Generated Key Error");
				}
			}
			model.afterInsert(conn);
			
		} catch (IllegalAccessException
				| IllegalArgumentException 
				| InvocationTargetException
				| SecurityException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ret = -1;
			new SQLException(e);
		}
		finally {
			val.clear();
			type.clear();
			val = null;
			type = null;
			column = null;
			values = null;
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(conn);
		}
		
		return ret;
	}

	@Override
	public int update(T model) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer column = new StringBuffer("UPDATE ")
								.append(schema.tableName)
								.append(" SET ");
		
		ArrayList<DataType> col_type = new ArrayList<DataType>();
		ArrayList<Object> col_val = new ArrayList<Object>();
		
		StringBuffer where = new StringBuffer(" WHERE ");
		ArrayList<DataType> where_type = new ArrayList<DataType>();
		ArrayList<Object> where_val = new ArrayList<Object>();
		
		boolean has_primary = false;
		
		try {
			for(Column col : schema.columns.values()) {
				if (col.isExpression) {
					continue;
				}
			
				Object val_tmp = col.getValue(model);
				if (val_tmp != null) {
					if (col.isPrimary) {
						has_primary = true;
						where.append(col.fieldName).append("=? AND");
						where_type.add(col.type);
						where_val.add(val_tmp);
					}
					else {
						column.append(col.fieldName).append("=?,");
						col_type.add(col.type);
						col_val.add(val_tmp);
					}
				}
				else {
					if (Const.FIELD_UPDATED.equals(col.fieldName)) {
						val_tmp = col.defaultValue();
						if (val_tmp != null) {
							column.append(col.fieldName).append("=?,");
							col_type.add(col.type);
							col_val.add(val_tmp);
						}
					}
				}
			
			}// for
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ParseException e) {
			// TODO Auto-generated catch block
			col_val.clear();
			where_val.clear();
			col_val = null;
			where_val = null;
			column = null;
			where = null;
			throw new SQLException(e);
		}
		
		if (!has_primary) {
			col_val.clear();
			where_val.clear();
			col_val = null;
			where_val = null;
			column = null;
			where = null;
			throw new SQLException("Condition for Primary Not Found. Stop for Security Issue");
		}
		
		column.setLength(column.length() - 1);
		where.setLength(where.length() - 3);
		
		Connection conn = null;
		PreparedStatement stmt = null;
		int ret = -1;
		
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(column.append(where).toString());
			
			int seq = 1;
			for (int i = 0, len = col_val.size(); i < len; i++) {
				col_type.get(i).setParam(stmt, seq++, col_val.get(i));
			}
			
			for (int i = 0, len = where_val.size(); i < len; i++) {
				where_type.get(i).setParam(stmt, seq++, where_val.get(i));
			}
			
			ret = stmt.executeUpdate();
		}
		finally {
			col_val.clear();
			where_val.clear();
			col_val = null;
			where_val = null;
			column = null;
			where = null;
			closeStatement(stmt);
			closeConnection(conn);
		}
		return ret;
	}

	@Override
	public T[] find(String condition, Object... values) throws SQLException {
		// TODO Auto-generated method stub
		return null;		
	}
	

}
