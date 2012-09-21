package xv.voltron.view;

import xv.voltron.core.Option;

public final class Html {

	protected static String extra(String[] extra) {
		if (extra == null || extra.length == 0) {
			return "";
		}
		else if (extra.length == 1) {
			return " " + extra[0];
		}
		else if (extra.length == 2) {
			return new StringBuffer(' ').append(extra[0])
					.append("=\"").append(extra[1]).append("\"").toString();
		}
		
		StringBuffer ret = new StringBuffer(64);
		for (int i = 0, len = extra.length; i < len; i++) {
			
			ret.append(' ').append(extra[i++]);
			if (i < len) {
				if (extra[i] != null && extra[i].length() > 0) {
					ret.append("=\"").append(extra[i]).append("\"");
				}
			}
			else {
				return ret.toString();
			}
		}
		return ret.toString();
	}
	
	protected static String input(String type, 
								  String name, 
								  String value, 
								  String... extra) {
		
		String tmp = extra(extra);
		StringBuffer input = new StringBuffer(128);
		input.append("<input type=\"").append(type).append("\"");
		if (name != null && name.length() != 0) {
			input.append(" name=\"").append(name).append("\"");
			input.append(" id=\"").append(name).append("\"");
		}
		
		if (value != null) {
			input.append(" value=\"").append(value).append("\"");
		}
		return input.append(tmp).append(" />").toString();
	}
	
	public static String text(String name, String value, String...extra) {
		return input("text", name, value, extra);
	}
	
	public static String password(String name, String value, String...extra) {
		return input("password", name, value, extra);
	}
	
	public static String file(String name, String...extra){
		return input("file", name, null, extra);
	}
	
	public static String button(String name, String...extra) {
		return input("button", name, null, extra);
	}
	
	public static String reset(String name, String...extra) {
		return input("reset", name, null, extra);
	}
	
	public static String submit(String name, String...extra) {
		return input("submit", name, null, extra);
	}
	
	protected static String option(String type, 
								   String name, 
								   Option[] options, 
								   Object selected, 
								   String seperator, 
								   String... extra) {
		
		String tmp = extra(extra);
		StringBuffer check = new StringBuffer(256);
		
		int flag = options.length - 1;
		int i = 0;
		for (Option op : options) {
			check.append("<label><input type=\"").append(type)
				.append("\" name=\"").append(name)
				.append("\" id=\"").append(name).append('_').append(op.toValue())
				.append("\"").append(op.sameAs(selected) ? " checked" : "")
				.append(tmp).append(" /></label>");
			
			if (i < flag) {
				check.append(seperator);
			}
		}
		
		return check.toString();
	}
	
	public static String checkbox(String name, 
								  Option[] options, 
								  Object selected, 
								  String seperator, 
								  String... extra) {
		
		return option("checkbox", name, options, selected, seperator, extra);
	}
	
	public static String radio(String name, 
							   Option[] options, 
							   Object selected, 
							   String seperator, 
							   String... extra) { 
		
		return option("radio", name, options, selected, seperator, extra);
	}
	
	public static String select (String name, 
								 Option[] options, 
								 Object selected, 
								 String...extra) {
		
		StringBuffer select = new StringBuffer(254);
		String tmp = extra(extra);
		
		select.append("<select name=\"").append(name)
			.append("\" id=\"").append(name).append("\"")
			.append(tmp).append('>');
		
		for (Option op : options) {
			select.append("<option value=\"")
				.append(op.toValue()).append("\"")
				.append(op.sameAs(selected) ? " selected" : "")
				.append('>')
				.append(op.toString()).append("</option>");
				
		}
		
		return select.append("</selected>").toString();
	}
	
	public static String area(String name, String content, String...extra) {
		StringBuffer area = new StringBuffer(128);
		String tmp = extra(extra);
		area.append("<textarea name=\"").append(name)
			.append("\" id=\"").append(name)
			.append("\"").append(tmp)
			.append('>');
		if (content != null) {
			area.append(content);
		}
		return area.append("</textarea>").toString();
	}
	
	
	public static String image(String name, String src, String...extra) {
		StringBuffer img = new StringBuffer(64);
		img.append("<img src=\"").append(name).append("\"");
		if (name != null && name.length() > 0) {
			img.append(" name=\"").append(name).append("\"");
			img.append(" id=\"").append(name).append("\"");
		}
		return img.append(extra(extra)).toString();
	}
	
	public static String link(String name, 
							  String href,
							  /*String target,*/
							  String content, 
							  String...extra) {
		
		StringBuffer anchor = new StringBuffer(128);
		
		anchor.append("<a");
		if (name != null && name.length() > 0) {
			anchor.append(" name=\"").append(name).append("\"");
			anchor.append(" id=\"").append(name).append("\"");
		}
		
		if (href != null && href.length() > 0) {
			anchor.append(" href=\"").append(href).append("\"");
		}
		
		/*if (target != null && target.length() > 0) {
			anchor.append(" target=\"").append(target).append("\"");
		}*/
		
		anchor.append(extra(extra)).append('>');
		if (content != null) {
			anchor.append(content);
		}
		return anchor.append("</a>").toString();
	}
	
	public static String error(String id, String content, String...extra) {
		StringBuffer error = new StringBuffer();
		error.append("<span id=\"").append(id).append("\"");
		error.append(extra(extra));
		error.append('>');
		if (content != null) {
			error.append(content);
		}
		return error.append("</span>").toString();
	}

}
