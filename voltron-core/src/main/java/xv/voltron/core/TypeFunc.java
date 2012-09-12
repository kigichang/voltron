package xv.voltron.core;

import java.text.ParseException;

public interface TypeFunc {
	public boolean compatible(Class<?> clazz);
	public Object parseValue(String val) throws ParseException;
}
