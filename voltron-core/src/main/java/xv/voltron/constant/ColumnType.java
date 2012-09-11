package xv.voltron.constant;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
//import java.util.Date;

import xv.voltron.util.DateUtils;

public enum ColumnType {
	STRING		(Types.VARCHAR, String.class) {
		@Override
		public String parseValue(String val) throws ParseException {
			if (val == null) {
				throw new ParseException("Value is Null", 0);
			}
			return val;
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (String.class.equals(clazz));
		}
		
		@Override
		public String getResult(ResultSet rs, String label) throws SQLException {
			return rs.getString(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setString(index, (String)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException,ParseException {
			stmt.setString(index, val);
		}
	},
	
	INTEGER		(Types.INTEGER, Integer.class) {
		@Override
		public Integer parseValue(String val) throws ParseException {
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			
			try {
				return Integer.valueOf(val);
			}
			catch (NumberFormatException e) {
				throw new ParseException(e.toString(), 0);
			}
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (Integer.class.equals(clazz));
		}
		
		@Override
		public Integer getResult(ResultSet rs, String label) throws SQLException {
			return rs.getInt(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setInt(index, (Integer)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setInt(index, parseValue(val));
		}
	},
	
	TIMESTAMP	(Types.TIMESTAMP, java.sql.Timestamp.class) {
		@Override
		public Timestamp parseValue(String val) throws ParseException {
			return DateUtils.parseTimestamp(val);
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (java.sql.Timestamp.class.equals(clazz));
		}
		
		@Override
		public Timestamp getResult(ResultSet rs, String label) throws SQLException {
			return rs.getTimestamp(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setTimestamp(index, (Timestamp)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setTimestamp(index, parseValue(val));
		}
	},
	
	LONG		(Types.BIGINT, Long.class) {
		@Override
		public Long parseValue(String val) throws ParseException {
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			
			try {
				return Long.valueOf(val);
			}
			catch (NumberFormatException e) {
				throw new ParseException(e.toString(), 0);
			}
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (Long.class.equals(clazz));
		}
		
		@Override
		public Long getResult(ResultSet rs, String label) throws SQLException {
			return rs.getLong(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setLong(index, (Long)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setLong(index, parseValue(val));
		}
	},
	BIGDECIMAL	(Types.DECIMAL, BigDecimal.class) {
		@Override
		public BigDecimal parseValue(String val) throws ParseException {
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			
			try {
				return new BigDecimal(val);
			}
			catch (NumberFormatException e) {
				throw new ParseException(e.toString(), 0);
			}
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (BigDecimal.class.equals(clazz));
		}
		
		@Override
		public BigDecimal getResult(ResultSet rs, String label) throws SQLException {
			return rs.getBigDecimal(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setBigDecimal(index, (BigDecimal)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setBigDecimal(index, parseValue(val));
		}
	},
	
	FLOAT		(Types.FLOAT, Float.class) {
		@Override
		public Float parseValue(String val) throws ParseException {
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			
			try {
				return Float.valueOf(val);
			}
			catch (NumberFormatException e) {
				throw new ParseException(e.toString(), 0);
			}
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (Float.class.equals(clazz));
		}
		
		@Override
		public Float getResult(ResultSet rs, String label) throws SQLException {
			return rs.getFloat(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setFloat(index, (Float)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setFloat(index, parseValue(val));
		}
	},
	
	DOUBLE		(Types.DOUBLE, Double.class) {
		@Override
		public Double parseValue(String val) throws ParseException {
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			
			try {
				return Double.valueOf(val);
			}
			catch (NumberFormatException e) {
				throw new ParseException(e.toString(), 0);
			}
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (Double.class.equals(clazz));
		}
		
		@Override
		public Double getResult(ResultSet rs, String label) throws SQLException {
			return rs.getDouble(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setDouble(index, (Double)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setDouble(index, parseValue(val));
		}
	},
	
	/*CHAR		(Types.CHAR, Character.class) {
		@Override
		public Character parseValue(String val) throws ParseException {
			if (val != null && val.length() > 0) {
				return val.charAt(0);
			}
			else {
				throw new ParseException("Value is Null or Empty", 0);
			}
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (Character.class.equals(clazz));
		}
		
		@Override
		public Character getResult(ResultSet rs, String label) throws SQLException {
			return null;
		}
	},*/
	
	DATE		(Types.DATE, java.sql.Date.class) {
		@Override
		public java.sql.Date parseValue(String val) throws ParseException {
			return new java.sql.Date(DateUtils.parseDate(val).getTime());
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (java.sql.Date.class.equals(clazz));
		}
		
		@Override
		public java.sql.Date getResult(ResultSet rs, String label) throws SQLException {
			return rs.getDate(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setDate(index, (java.sql.Date)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setDate(index, parseValue(val));
		}
		
	},
	
	TIME		(Types.TIME, java.sql.Time.class) {
		@Override
		public Time parseValue(String val) throws ParseException {
			return DateUtils.parseTime(val);
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (java.sql.Time.class.equals(clazz));
		}
		
		@Override
		public java.sql.Time getResult(ResultSet rs, String label) throws SQLException {
			return rs.getTime(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setTime(index, (java.sql.Time)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setTime(index, parseValue(val));
		}
	},
	
	BOOLEAN		(Types.BOOLEAN, Boolean.class) {
		@Override
		public Boolean parseValue(String val) throws ParseException {
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			String tmp = val.trim().toLowerCase();
			switch(tmp) {
			case "yes":
			case "true":
			case "on":
			case "1":
				return Boolean.TRUE;
			case "no":
			case "false":
			case "off":
			case "0":
			default:
				throw new ParseException("Can Not Verify Value to Boolean", 0);
			}
		}
		
		@Override
		public boolean compatiable(Class<?> clazz) {
			return (Boolean.class.equals(clazz));
		}
		
		@Override
		public Boolean getResult(ResultSet rs, String label) throws SQLException {
			return rs.getBoolean(label);
		}
		
		@Override
		public void setStatementOrigin(PreparedStatement stmt, int index, Object val) throws SQLException {
			stmt.setBoolean(index, (Boolean)val);
		}
		
		@Override
		public void setStatement(PreparedStatement stmt, int index, String val) throws SQLException, ParseException {
			stmt.setBoolean(index, parseValue(val));
		}
	};
	
	
	private int sqlType = Types.NULL;
	private Class<?> clazz = null;
	
	private ColumnType(int sqlType, Class<?> clazz) {
		this.sqlType = sqlType;
		this.clazz = clazz;
	}
	
	
	public int toSQLType() {
		return this.sqlType;
	}
	
	public Class<?> toClass() {
		return this.clazz;
	}
	
	public Object parseValue(String val) throws ParseException {
		return null;
	}
	
	public boolean compatiable(Class<?> clazz) {
		return false;
	}
	
	public Object getResult(ResultSet rs, String label) throws SQLException {
		return null;
	}
	
	public void setStatement(PreparedStatement stmt, int index, String val) 
			throws SQLException, ParseException {
		
	}
	
	public void setStatementOrigin(PreparedStatement stmt, int index, Object val) 
			throws SQLException {
		
	}
}
