package xv.voltron.constant;

import java.math.BigDecimal;
import java.sql.Types;

public enum ColumnType {
	STRING		(Types.VARCHAR, String.class),
	INTEGER		(Types.INTEGER, Integer.class),
	LONG		(Types.BIGINT, Long.class),
	FLOAT		(Types.FLOAT, Float.class),
	DOUBLE		(Types.DOUBLE, Double.class),
	BIGDECIMAL	(Types.DECIMAL, BigDecimal.class),
	DATE		(Types.DATE, java.util.Date.class),
	TIME		(Types.TIME, java.sql.Time.class),
	TIMESTAMP	(Types.TIMESTAMP, java.sql.Timestamp.class),
	CHAR		(Types.CHAR, Character.class),
	BOOLEAN		(Types.BOOLEAN, Boolean.class);
	
	
	private int value = Types.NULL;
	private Class<?> clazz = null;
	
	private ColumnType(int val, Class<?> clazz) {
		this.value = val;
		this.clazz = clazz;
	}
	
	public int value() {
		return this.value;
	}
	
	public Class<?> toClass() {
		return this.clazz;
	}
	
}
