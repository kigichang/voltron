package xv.voltron.constant;

import java.math.BigDecimal;
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
}
