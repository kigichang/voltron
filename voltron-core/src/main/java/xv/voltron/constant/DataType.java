package xv.voltron.constant;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;

import xv.voltron.core.TypeFunc;
import xv.voltron.exception.UnsupportedDataTypeException;
import xv.voltron.util.DateUtils;

public enum DataType implements TypeFunc {
	
	STRING(String.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
			if (val == null) {
				throw new ParseException("Value is Null", 0);
			}
			return val;
		}

		@Override
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getString(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setString(index, (String)val);
		}
	},
	
	BIGDECIMAL(BigDecimal.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
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
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getBigDecimal(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setBigDecimal(index, (BigDecimal)val);
		}
		
	},
	
	INTEGER(Integer.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
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
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getInt(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setInt(index, (Integer)val);
		}
		
	},
	
	TIMESTAMP(Timestamp.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			return DateUtils.parseTimestamp(val);
		}

		@Override
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getTimestamp(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setTimestamp(index, (Timestamp)val);
		}
		
	},
	
	LONG(Long.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
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
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getLong(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setLong(index, (Long)val);
		}
		
	},
	
	FLOAT(Float.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
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
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getFloat(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setFloat(index, (Float)val);
		}
		
	},
	
	DOUBLE(Double.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
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
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getDouble(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setDouble(index, (Double)val);
		}
		
	},
	
	DATE(Date.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			return new java.sql.Date(DateUtils.parseDate(val).getTime());
		}

		@Override
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getDate(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setDate(index, (Date)val);
		}
		
	},
	
	TIME(Time.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
			if (val == null || val.length() == 0) {
				throw new ParseException("Value is Null or Empty", 0);
			}
			return DateUtils.parseTime(val);
		}

		@Override
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getTime(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setTime(index, (Time)val);
		}
		
	},
	CHAR(Character.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
			if (val != null && val.length() > 0) {
				return val.charAt(0);
			}
			else {
				throw new ParseException("Value is Null or Empty", 0);
			}
		}

		@Override
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			String tmp = rs.getString(label);
			if (tmp == null) {
				return null;
			}
			if (tmp.length() == 0) {
				return 0x00;
			}
			return tmp.charAt(0);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setString(index, val.toString());
		}
		
	},
	BOOLEAN(Boolean.class) {

		@Override
		public Object parseValue(String val) throws ParseException {
			// TODO Auto-generated method stub
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
			case "-1":
			default:
				throw new ParseException("Can Not Verify Value to Boolean", 0);
			}
		}

		@Override
		public Object getResult(ResultSet rs, String label) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getBoolean(label);
		}

		@Override
		public void setParam(PreparedStatement statement, int index, Object val)
				throws SQLException {
			// TODO Auto-generated method stub
			statement.setBoolean(index, (Boolean)val);
		}
		
	};
	
	
	private Class<?> clazz = null;
	
	private DataType(Class<?> clazz) {
		this.clazz = clazz;
	}

	public boolean compatible(Class<?> clazz) {
		return this.clazz.equals(clazz);
	}
	
	public static DataType valueOf(Class<?> type) {
		for (DataType dt : DataType.values()) {
			if (dt.compatible(type)) {
				return dt;
			}
		}
		throw new UnsupportedDataTypeException(
				type.getName() + " Not Supported");
	}
	
	public Class<?> toClass() {
		return this.clazz;
	}
}
