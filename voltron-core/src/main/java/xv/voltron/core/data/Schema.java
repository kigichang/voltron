package xv.voltron.core.data;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;

import xv.voltron.annotation.Table;
import xv.voltron.constant.Const;
import xv.voltron.constant.DataType;
import xv.voltron.core.Convention;
import xv.voltron.core.Model;

public class Schema {
	
	protected static Hashtable<Class<?>, Schema> schemas = 
			new Hashtable<Class<?>, Schema>();
	
	public static Schema getSchema(Class<? extends Model> clazz) 
			throws SQLException {
		
		Schema s = null;
		if ((s = schemas.get(clazz)) == null) {
			s = new Schema(clazz);
			Schema old = schemas.put(clazz, s);
			old = null;
		}
		return s;
	}
	
	protected String name = null;
	protected String tableName = null;
	protected String dataSource = null;
	protected HashMap<String, Column> columns = null;
	
	private Schema(Class<? extends Model> clazz) throws SQLException {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			throw new SQLException("Table Annotation Not Found");
		}
		
		this.name = clazz.getSimpleName();
		this.tableName = "".equals(table.tableName()) ?
				Convention.uncapitalize(this.name) : table.tableName();
		
		this.dataSource = "".equals(table.dataSource()) ?
				Const.DATA_DEFAULT : table.dataSource();
		
		
		Field[] fields = clazz.getDeclaredFields();
		columns = new HashMap<String, Column>();
		for (Field field : fields) {
			xv.voltron.annotation.Field f =
					field.getAnnotation(xv.voltron.annotation.Field.class);
			
			if (f != null) {
				DataType f_type = DataType.valueOf(field.getType());
				try {
					Column col = 
							new Column(clazz, 
									   Convention.capitalize(field.getName()), 
									   f_type, 
									   f);
					
					columns.put(col.fieldName, col);
					
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					columns.clear();
					columns = null;
					throw new SQLException(e);
				}
			}
		}
	}
}
