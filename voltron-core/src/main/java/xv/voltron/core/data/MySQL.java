package xv.voltron.core.data;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang.reflect.FieldUtils;

import xv.voltron.core.Operate;

public class MySQL extends Operate {

	public MySQL(Class clazz) throws SQLException {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean add(Object model) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer column = 
				new StringBuffer("INSERT INTO ")
				.append(tableName).append(" (");
		
		StringBuffer values = 
				new StringBuffer(" VALUES (");
		
		ArrayList<Object> val = new ArrayList<Object>();
		int i = 0, len = columns.length;
		try {
			for (; i < len; i++) {
				if (!columns[i].isAutoIncrement) {
					column.append(columns[i].fieldName).append(',');
					values.append("?,");	
					val.add(FieldUtils.readDeclaredField(model, columns[i].name));
				
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new SQLException("Read Model Value Error " + columns[i].name);
		}
		column.setCharAt(column.length() - 1, ')');
		values.setCharAt(column.length() - 1, ')');
	
		return false;
	}

}
