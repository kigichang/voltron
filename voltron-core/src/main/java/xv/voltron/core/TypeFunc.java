package xv.voltron.core;

import java.text.ParseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import xv.voltron.core.data.DataValue;

public interface TypeFunc {
	public boolean compatible(Class<?> clazz);
	public Object parseValue(String val) throws ParseException;
	public Object getResult(ResultSet rs, String label) throws SQLException;
	public Object getResult(ResultSet rs, int index) throws SQLException;
	public void setParam(PreparedStatement statement, int index, Object val) throws SQLException;
	public Class<?> toClass();
	public DataValue toValue(String value);
}
