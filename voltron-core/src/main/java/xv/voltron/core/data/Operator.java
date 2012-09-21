package xv.voltron.core.data;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import xv.voltron.constant.Const;
import xv.voltron.constant.DataType;
import xv.voltron.core.DataManager;
import xv.voltron.core.Model;

public class Operator<T extends Model> {

	protected Class<T> clazz = null;
	protected boolean isPersistent = false;
	protected Schema schema = null;
	protected HashMap<String, String> errors = new HashMap<String, String>();
	
	public Operator(Class<T> clazz, boolean persistent) throws SQLException {
		this.clazz = clazz;
		schema = Schema.getSchema(clazz);
		this.isPersistent = persistent;
	}
	
	public Operator(Class<T> clazz) throws SQLException {
		this(clazz, false);
	}
	
	protected Connection getConnection() throws SQLException {
		return isPersistent ?
				DataManager.getPersistent(schema.dataSource):
				DataManager.getConnection(schema.dataSource);
	}
	
	protected void closeConnection(Connection conn) {
		if (!isPersistent) {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
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
	
	public T toModel(HttpServletRequest req) throws SQLException {
		T model = null;
		errors.clear();
		try {
			model = clazz.newInstance();
			for (String display_label : schema.columns.keySet()) {
				Column col = schema.columns.get(display_label);
				if (col.isExpression) {
					continue;
				}
				
				String value = req.getParameter(display_label);
				if (value != null) {
					col.setValue(model, col.parseValue(value));
				}
			}
		} catch (InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException 
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			model = null;
			throw new SQLException(e);
		}
		catch(ParseException e) {
			model = null;
			throw new SQLException(e);
		}
		return model;
	}
	
	public T toModel(Map<String, String> data) throws SQLException {
		T model = null;
		
		try {
			model = clazz.newInstance();
			for(Column column : schema.columns.values()) {
				if (!column.isExpression) {
					String value = data.get(column.fieldLabel);
					if (value != null) {
						column.setter.invoke(model, column.parseValue(value));
					}
				}
			}
			
		} catch (InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException 
				| InvocationTargetException 
				| ParseException e) {
			// TODO Auto-generated catch block
			model = null;
			throw new SQLException(e);
		}
		
		return model;
	}
	
	public int add(T model) throws SQLException {
		StringBuffer insert = new StringBuffer("INSERT INTO ")
			.append(schema.tableName).append('(');
		
		StringBuffer values = new StringBuffer(" VALUES(");
		
		int size = schema.columns.size();
		Object[] val = new Object[size];
		DataType[] type = new DataType[size];
		Column[] gen_keys = new Column[1];
		boolean has_auto_increment = false;
		int val_size = 0, gen_size = 0;
		try {
			for (Column column : schema.columns.values()) {
				if (column.isExpression) {
					continue;
				}
			
				if (!column.isAutoIncrement) {
					Object val_tmp = column.getValue(model);
					if (val_tmp == null) {
						val_tmp = column.defaultValue();
					}
					
					if (val_tmp != null) {
						insert.append(column.fieldName).append(',');
						values.append("?,");
						val[val_size] = val_tmp;
						type[val_size] = column.type;
						val_size++;
					}
				}
				else {
					gen_keys[0] = column;
					has_auto_increment = true;
					gen_size++;
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ParseException e) {
			// TODO Auto-generated catch block
			val = null;
			type = null;
			gen_keys = null;
			insert = null;
			values = null;
			throw new SQLException(e);
		}
		
		if (gen_size > 1) {
			val = null;
			type = null;
			gen_keys = null;
			insert = null;
			values = null;
			throw new SQLException(
					"More than One AutoIncrement Fields Not Supperted");
		}
		
		insert.setCharAt(insert.length() - 1, ')');
		values.setCharAt(values.length() - 1, ')');
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int ret = -1;
		
		try {
			conn = getConnection();
		
			if (!model.beforeInsert(conn)) {
				return -1;
			}
		
			stmt = conn.prepareStatement(
					insert.append(values).toString(),
					has_auto_increment ?
							Statement.RETURN_GENERATED_KEYS : 
							Statement.NO_GENERATED_KEYS);
		
			for (int i = 0; i < val_size; i++) {
				type[i].setParam(stmt, i + 1, val[i]);
			}
		
			ret = stmt.executeUpdate();
		
			if (has_auto_increment) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					for (int i = 0; i < gen_size; i++) {
						Column col = gen_keys[i];
						Object v = col.type.getResult(rs, i + 1);
						col.setValue(model, v);
					}
				}
				else {
					throw new SQLException("Generated Key Error");
				}
			}
		
			model.afterInsert(conn);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			ret = -1;
			throw new SQLException(e);
		}
		finally {
			val = null;
			type = null;
			gen_keys = null;
			insert = null;
			values = null;
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(conn);
		}
		return ret;
	}
	
	public int add(HashMap<String, String> data) throws SQLException {
		return add(toModel(data));
	}
	
	public T add(HashMap<String, String> data, int threshold) 
			throws SQLException {
		
		T model = toModel(data);
		return add(model) >= threshold ? model : null;
	}
	
	public int add(HttpServletRequest req) throws SQLException {
		return add(toModel(req));
	}
	
	public T add(HttpServletRequest req, int threshold) throws SQLException {
		T model = toModel(req);
		return add(model) >= threshold ? model : null;
	}
	
	public int update(T model) throws SQLException {
		StringBuffer update = new StringBuffer("UPDATE ")
			.append(schema.tableName).append(" SET ");
		
		int size = schema.columns.size();
		
		DataType[] update_type = new DataType[size];
		Object[] update_val = new Object[size];
		int update_size = 0;
		
		StringBuffer where = new StringBuffer(" WHERE ");
		DataType[] where_type = new DataType[size];
		Object[] where_val = new Object[size];
		int where_size = 0;
		
		boolean has_primary = false;
		try {
			for (Column column : schema.columns.values()) {
				if (column.isExpression) {
					continue;
				}
				
				Object val_tmp = column.getValue(model);
				
				if (val_tmp != null) {
					if (!column.isPrimary) {
						update.append(column.fieldName).append("=?,");
						update_type[update_size] = column.type;
						update_val[update_size] = val_tmp;
						update_size++;
					}
					else {
						has_primary = true;
						where.append(column.fieldName).append("=? AND");
						where_type[where_size] = column.type;
						where_val[where_size] = val_tmp;
						where_size++;
					}
				}
				else {
					if (Const.FIELD_UPDATED.equals(column.fieldName)) {
						val_tmp = column.defaultValue();
						if (val_tmp != null) {
							column.setValue(model, val_tmp);
							update.append(column.fieldName).append("=?,");
							update_type[update_size] = column.type;
							update_val[update_size] = val_tmp;
							update_size++;
						}
					}
				}
				
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ParseException e) {
			// TODO Auto-generated catch block
			update = null;
			where = null;
			update_type = null;
			update_val = null;
			where_type = null;
			where_val = null;
			throw new SQLException(e);
		}
		
		if (!has_primary) {
			update = null;
			where = null;
			update_type = null;
			update_val = null;
			where_type = null;
			where_val = null;
			throw new SQLException(
					"Condition for Primary Not Found. Stop for Security Issue");
		}
		
		update.setCharAt(update.length() - 1, ' ');
		where.setLength(where.length() - 3);
		
		Connection conn = null;
		PreparedStatement stmt = null;
		int ret = -1;
		
		try {
			conn = getConnection();
			if (!model.beforeUpdate(conn)) {
				return -1;
			}
			stmt = conn.prepareStatement(update.append(where).toString());
			int seq = 1;
			
			for (int i = 0; i < update_size; i++) {
				update_type[i].setParam(stmt, seq++, update_val[i]);
			}
			
			for (int i = 0; i < where_size; i++) {
				where_type[i].setParam(stmt, seq++, where_val[i]);
			}
			
			ret = stmt.executeUpdate();
			model.afterUpdate(conn);
		}
		finally {
			update = null;
			where = null;
			update_type = null;
			update_val = null;
			where_type = null;
			where_val = null;
			closeStatement(stmt);
			closeConnection(conn);
		}
		
		return ret;
	}
	
	public int update(HashMap<String, String> data) throws SQLException {
		return update(toModel(data));
	}
	
	public T update(HashMap<String, String> data, int threshold) 
			throws SQLException {
		
		T model = toModel(data);
		return update(data) >= threshold ? model : null;
	}
	
	public int update(HttpServletRequest req) throws SQLException {
		return update(toModel(req));
	}
	
	public T update(HttpServletRequest req, int threshold) throws SQLException {
		T model = toModel(req);
		return update(model) >= threshold ? model : null;
	}
	
	public T[] find(String[] fields, String condition, DataValue... values) 
			throws SQLException {
		
		return null;
	}
	/*
	public abstract int add(T model) throws SQLException;
	public abstract int update(T model) throws SQLException;
	public abstract T[] find(String condition, DataValue... values) throws SQLException;
	public abstract T[] find(String[] fields, String condition, DataValue... values) throws SQLException;
	*/
}
