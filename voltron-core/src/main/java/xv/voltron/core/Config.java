package xv.voltron.core;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;;

public final class Config {

	private static Config instance = null;
	private static Configuration config = null;
	
	private static boolean debug = false;
	private static String encoding = null;
	
	private Config(String file) throws ConfigurationException {
		config = new PropertiesConfiguration(new File(file));
		debug = config.getBoolean("app.debug", false);
		encoding = config.getString("app.encoding", "UTF-8");
	}

	public static synchronized Config getInstance(String file) 
			throws ConfigurationException {
		if (instance == null) {
			instance = new Config(file);
		}
		return instance;
	}
	
	public static synchronized Config getInstance(ServletContext context) 
			throws ConfigurationException {
		if (instance != null) {
			return instance;
		}
		
		String file = new StringBuffer(context.getRealPath("/"))
			.append(File.separatorChar)
			.append("WEB-INF")
			.append(File.separatorChar)
			.append("conf")
			.append(File.separatorChar)
			.append("system.properties").toString();
		
		return getInstance(file);	
	}
	
	public static void addProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		config.addProperty(arg0, arg1);
	}

	public static void clear() {
		// TODO Auto-generated method stub
		config.clear();
	}

	public static void clearProperty(String arg0) {
		// TODO Auto-generated method stub
		config.clearProperty(arg0);
	}

	public static boolean containsKey(String arg0) {
		// TODO Auto-generated method stub
		return config.containsKey(arg0);
	}

	public static BigDecimal getBigDecimal(String arg0) {
		// TODO Auto-generated method stub
		return config.getBigDecimal(arg0);
	}

	public static BigDecimal getBigDecimal(String arg0, BigDecimal arg1) {
		// TODO Auto-generated method stub
		return config.getBigDecimal(arg0, arg1);
	}

	public static BigInteger getBigInteger(String arg0) {
		// TODO Auto-generated method stub
		return config.getBigInteger(arg0);
	}

	public static BigInteger getBigInteger(String arg0, BigInteger arg1) {
		// TODO Auto-generated method stub
		return config.getBigInteger(arg0, arg1);
	}

	public static boolean getBoolean(String arg0) {
		// TODO Auto-generated method stub
		return config.getBoolean(arg0);
	}

	public static boolean getBoolean(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return config.getBoolean(arg0, arg1);
	}

	public static Boolean getBoolean(String arg0, Boolean arg1) {
		// TODO Auto-generated method stub
		return config.getBoolean(arg0, arg1);
	}

	public static byte getByte(String arg0) {
		// TODO Auto-generated method stub
		return config.getByte(arg0);
	}

	public static byte getByte(String arg0, byte arg1) {
		// TODO Auto-generated method stub
		return config.getByte(arg0, arg1);
	}

	public static Byte getByte(String arg0, Byte arg1) {
		// TODO Auto-generated method stub
		return config.getByte(arg0, arg1);
	}

	public static double getDouble(String arg0) {
		// TODO Auto-generated method stub
		return config.getDouble(arg0);
	}

	public static double getDouble(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return config.getDouble(arg0, arg1);
	}

	public static Double getDouble(String arg0, Double arg1) {
		// TODO Auto-generated method stub
		return config.getDouble(arg0, arg1);
	}

	public static float getFloat(String arg0) {
		// TODO Auto-generated method stub
		return config.getFloat(arg0);
	}

	public static float getFloat(String arg0, float arg1) {
		// TODO Auto-generated method stub
		return config.getFloat(arg0, arg1);
	}

	public static Float getFloat(String arg0, Float arg1) {
		// TODO Auto-generated method stub
		return config.getFloat(arg0, arg1);
	}

	public static int getInt(String arg0) {
		// TODO Auto-generated method stub
		return config.getInt(arg0);
	}

	public static int getInt(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return config.getInt(arg0, arg1);
	}

	public static Integer getInteger(String arg0, Integer arg1) {
		// TODO Auto-generated method stub
		return config.getInteger(arg0, arg1);
	}

	public static Iterator<String> getKeys() {
		// TODO Auto-generated method stub
		return config.getKeys();
	}

	public static Iterator<String> getKeys(String arg0) {
		// TODO Auto-generated method stub
		return config.getKeys(arg0);
	}

	public static List<Object> getList(String arg0) {
		// TODO Auto-generated method stub
		return config.getList(arg0);
	}

	public static List<Object> getList(String arg0, List<Object> arg1) {
		// TODO Auto-generated method stub
		return config.getList(arg0, arg1);
	}

	public static long getLong(String arg0) {
		// TODO Auto-generated method stub
		return config.getLong(arg0);
	}

	public static long getLong(String arg0, long arg1) {
		// TODO Auto-generated method stub
		return config.getLong(arg0, arg1);
	}

	public static Long getLong(String arg0, Long arg1) {
		// TODO Auto-generated method stub
		return config.getLong(arg0, arg1);
	}

	public static Properties getProperties(String arg0) {
		// TODO Auto-generated method stub
		return config.getProperties(arg0);
	}

	public static Object getProperty(String arg0) {
		// TODO Auto-generated method stub
		return config.getProperty(arg0);
	}

	public static short getShort(String arg0) {
		// TODO Auto-generated method stub
		return config.getShort(arg0);
	}

	public static short getShort(String arg0, short arg1) {
		// TODO Auto-generated method stub
		return config.getShort(arg0, arg1);
	}

	public static Short getShort(String arg0, Short arg1) {
		// TODO Auto-generated method stub
		return config.getShort(arg0, arg1);
	}

	public static String getString(String arg0) {
		// TODO Auto-generated method stub
		return config.getString(arg0);
	}

	public static String getString(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return config.getString(arg0, arg1);
	}
	
	public static String get(String arg0) {
		return config.getString(arg0);
	}
	
	public static String get(String arg0, String arg1) {
		return config.getString(arg0, arg1);
	}

	public static String[] getStringArray(String arg0) {
		// TODO Auto-generated method stub
		return config.getStringArray(arg0);
	}

	public static boolean isEmpty() {
		// TODO Auto-generated method stub
		return config.isEmpty();
	}

	public static void setProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		config.setProperty(arg0, arg1);
	}

	public static Configuration subset(String arg0) {
		// TODO Auto-generated method stub
		return config.subset(arg0);
	}

	public static boolean debug() {
		return Config.debug;
	}
	
	public static String encoding() {
		return Config.encoding;
	}
	
	
}
