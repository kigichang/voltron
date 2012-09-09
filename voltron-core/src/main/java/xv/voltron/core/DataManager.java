package xv.voltron.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;

import xv.voltron.constant.Const;

public final class DataManager {
	
	private static DataManager instance = null;
	
	protected static HashMap<String, DataSource> sources = null;
	
	protected static HashMap<String, Connection> persistents = null;
	protected static String[] names = null;
	private DataManager() throws NamingException {
		InitialContext ctx = new InitialContext();
		sources = new HashMap<String, DataSource>();
		NamingEnumeration<NameClassPair> e = ctx.list(Const.DATA_PREFIX);
		boolean found = false;
		ArrayList<String> tmp = new ArrayList<String>();
		while(e.hasMoreElements()) {
			String name = e.nextElement().getName();
			if (Const.DATA_DEFAULT.equals(name)) {
				found = true;
				DataSource ds = 
						(DataSource)ctx.lookup(Const.DATA_PREFIX + "/" + name);
				
				sources.put(name, ds);
				tmp.add(name);
			}
		}
		if (!found) {
			sources.clear();
			sources = null;
			tmp.clear();
			tmp = null;
			throw new NamingException("Default DataSource Not Found");
		}
		names = tmp.toArray(new String[tmp.size()]);
		persistents = new HashMap<String, Connection>();
	}
	
	public static synchronized DataManager getInstance() 
			throws NamingException {
		
		if (instance == null) {
			instance = new DataManager();
		}
		
		return instance;
	}
	
	public static Connection getConnection(String source) throws SQLException {
		DataSource ds = sources.get(source);
		if (ds == null) {
			throw new SQLException("Can Not Find DataSource :" + source);
		}
		
		Connection conn = ds.getConnection();
		if (conn == null) {
			throw new SQLException("Can Not Get a Connection from :" + source);
		}
		
		return conn;
	}
	
	public static Connection getConnection() throws SQLException {
		return getConnection(Const.DATA_DEFAULT);
	}
	
	public static Connection getPersistent(String source) 
			throws SQLException {
		
		long id = Thread.currentThread().getId();
		String key = source + id;
		Connection conn = persistents.get(key);
		if (conn != null) {
			return conn;
		}
		
		conn = getConnection(source);
		
		persistents.put(key, conn);
		return conn;
	}
	
	public static Connection getPersistent() throws SQLException {
		return getPersistent(Const.DATA_DEFAULT);
	}
	
	
	protected static Connection getPersistentConnection(String source) {
		long id = Thread.currentThread().getId();
		String key = source + id;
		return persistents.get(key);
	}
	
	public static void startTransaction(String source) throws SQLException {
		Connection conn = getPersistentConnection(source);
		if (conn == null) {
			throw new SQLException(
					"Connection Not Found with Source: " + source);
		}
		
		conn.setAutoCommit(false);
	}
	
	public static void startTransaction() throws SQLException {
		startTransaction(Const.DATA_DEFAULT);
	}
	
	public static void commit(String source) throws SQLException {
		Connection conn = getPersistentConnection(source);
		if (conn == null) {
			throw new SQLException(
					"Connection Not Found with Source: " + source);
		}
		
		conn.commit();
	}
	
	public static void commit() throws SQLException {
		commit(Const.DATA_DEFAULT);
	}
	
	public static void rollback(String source) throws SQLException {
		Connection conn = getPersistentConnection(source);
		if (conn == null) {
			throw new SQLException(
					"Connection Not Found with Source: " + source);
		}
		
		conn.rollback();
	}
	
	public static void rollback() throws SQLException {
		rollback(Const.DATA_DEFAULT);
	}
	
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.getAutoCommit()) {
					conn.commit();
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	public static void closeWithException(Connection conn) throws SQLException {
		if (conn != null) {
			if (!conn.getAutoCommit()) {
				conn.commit();
			}
			conn.close();
		}
	}
	
	
	public static void closePersistent(String source) {
		long id = Thread.currentThread().getId();
		String key = source + id;
		close(persistents.remove(key));
	}
	
	public static void closePersistent() {
		closePersistent(Const.DATA_DEFAULT);
	}
	
	public static void freeAllPersisten() {
		for (int i = 0, len = names.length; 
			 i < len; 
			 closePersistent(names[i++]));
	}
	
}
