package xv.voltron.core.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import xv.voltron.core.DataManager;
import xv.voltron.core.Model;

public abstract class Operator<T extends Model> {

	protected boolean isPersistent = false;
	protected Schema schema = null;
	
	public Operator(Class<T> clazz, boolean persistent) throws SQLException {
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
