package xv.voltron.core.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import xv.voltron.annotation.Table;
import xv.voltron.constant.Const;
import xv.voltron.constant.DataType;
import xv.voltron.core.Convention;
import xv.voltron.core.Model;

public final class Schema {
	
	private static Hashtable<Class<? extends Model>, Schema> schemas = 
			new Hashtable<Class<? extends Model>, Schema>();
	
	public static Schema getSchema(Class<? extends Model> clazz) 
			throws SQLException {
		
		Schema s = null;
		if ((s = schemas.get(clazz)) == null) {
			s = new Schema(clazz);
			schemas.put(clazz, s);
		}
		return s;
	}
	
	
	protected String name = null;
	protected String tableName = null;
	protected String dataSource = null;
	
	/**
	 * column, alias pair
	 */
	//protected HashMap<String, String> alias = null;
	
	/**
	 * alias, column pair
	 */
	protected HashMap<String, Column> columns = null;
	
	
	
	private Schema(Class<? extends Model> clazz) throws SQLException {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			throw new SQLException("Table Annotation Not Found");
		}
		
		this.name = clazz.getSimpleName();
		this.tableName = "".equals(table.tableName()) ?
				Convention.toUnderlineName(this.name) : table.tableName();
		
		this.dataSource = "".equals(table.dataSource()) ?
				Const.DATA_DEFAULT : table.dataSource();
		
		//ArrayList<Field> fields = new ArrayList<Field>();
		columns = new HashMap<String, Column>();
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			if (c.equals(Model.class)) {
				break;
			}
			Field[] tmp = c.getDeclaredFields();
			
			for (Field field : tmp) {
				//fields.add(f);
				xv.voltron.annotation.Field f =
						field.getAnnotation(xv.voltron.annotation.Field.class);
				
				if (f != null) {
					DataType f_type = DataType.valueOf(field.getType());
					try {
						Column col = new Column(
										clazz, 
										Convention.capitalize(field.getName()),
										f_type,
										f);
						
						columns.put(col.displayLabel, col);
						
					} catch (NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						columns.clear();
						columns = null;
						//alias.clear();
						//alias = null;
						throw new SQLException(e);
					}
				}
			}
			
		}
	}
}
