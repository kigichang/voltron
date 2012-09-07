package xv.voltron.core;

import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class DataManager {
	protected static Hashtable<String, DataSource> sources = 
			new Hashtable<String, DataSource>();
	
	protected static InitialContext context = null;
	
	
	
}
