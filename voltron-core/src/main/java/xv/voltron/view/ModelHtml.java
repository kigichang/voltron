package xv.voltron.view;

public final class ModelHtml {

	private static String name(String model, String field) {
		return new StringBuffer(model).append('_').append(field).toString();
	}
	
	public static String text(String model, 
							  String field, 
							  String value, 
							  String...extra) {
		
		return Html.text(name(model, field), value, extra);
	}
	
	public static String password(String model, String field, String value, String...extra) {
		return Html.password(name(model, field), value, extra);
	}
	
	public static String file(String model, String field, String...extra) {
		return Html.file(name(model, field), extra);
	}
	
}
