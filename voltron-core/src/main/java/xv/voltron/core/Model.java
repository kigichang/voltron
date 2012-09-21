package xv.voltron.core;

import java.sql.Connection;
import java.sql.SQLException;

public class Model {

	public boolean beforeUpdate(Connection conn) throws SQLException {
		return true;
	}
	
	public void afterUpdate(Connection conn) throws SQLException  {
		
	}
	
	public boolean beforeInsert(Connection conn) throws SQLException {
		return true;
	}
	
	public void afterInsert(Connection conn) throws SQLException {
		
	}
	
	public boolean beforeDelete(Connection conn) throws SQLException {
		return true;
	}
	
	public void afterDelete(Connection conn) throws SQLException {
		
	}
	
	public boolean beforeSelect(Connection conn) throws SQLException {
		return true;
	}
	
	public void afterSelect(Connection conn) throws SQLException {
		
	}
	
}
