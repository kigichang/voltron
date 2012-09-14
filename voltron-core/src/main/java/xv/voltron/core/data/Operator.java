package xv.voltron.core.data;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import xv.voltron.core.DataManager;
import xv.voltron.core.Model;

public abstract class Operator<T extends Model> {

	protected Class<T> clazz = null;
	protected boolean isPersistent = false;
	protected Schema schema = null;
	
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
	
	protected T toModel(ResultSet rs, ResultSetMetaData meta) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		T model = clazz.newInstance();
		 for (int i = 0; i < meta.getColumnCount(); i++) {
			 String label = meta.getColumnLabel(i);
			 Column col = schema.columns.get(label);
			 
			 if (col != null) {
				 col.setValue(model, col.type.getResult(rs, label));
			 }
			 
		 }
		 return model;
	}
	
	public abstract int add(T model) throws SQLException;
	public abstract int update(T model) throws SQLException;
	public abstract T[] find(String condition, DataValue... values) throws SQLException;
	public abstract T[] find(String[] fields, String condition, DataValue... values) throws SQLException;
}
