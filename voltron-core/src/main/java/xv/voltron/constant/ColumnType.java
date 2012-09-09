package xv.voltron.constant;

import java.sql.Types;

public enum ColumnType {
	STRING		(Types.VARCHAR),
	INTEGER		(Types.INTEGER),
	LONG		(Types.BIGINT),
	FLOAT		(Types.FLOAT),
	DOUBLE		(Types.DOUBLE),
	BIGDECIMAL	(Types.DECIMAL),
	DATE		(Types.DATE),
	TIME		(Types.TIME),
	DATETIME	(Types.TIMESTAMP),
	CHAR		(Types.CHAR);
	
	
	private int value = Types.NULL;
	
	private ColumnType(int val) {
		this.value = val;
	}
	
	public int value() {
		return this.value;
	}
	
}
