package xv.voltron.core;

import java.math.BigDecimal;

public final class Convention {
	
	public static boolean isUpperCase(char c) {
		return ('A' <= c && c <= 'Z');
	}
	
	public static boolean isLowerCase(char c) {
		return ('a' <= c && c <= 'z');
	}
	
	
	public static char toUpperCase(char c) {
		if (isLowerCase(c)) {
			return (char)((int)c & 0x5f);
		}
		return c;
	}
	
	public static char toLowerCase(char c) {
		if (isUpperCase(c)) {
			return (char)((int)c | 0x20);
		}
		return c;
	}
	
	public static String capitalize(String src) {
		int len = 0;
		if (src == null || (len = src.length()) == 0) {
			return src;
		}
		return new StringBuffer(len)
			.append(toUpperCase(src.charAt(0)))
			.append(src.substring(1)).toString();
	}
	
	public static String uncapitalize(String src) {
		int len = 0;
		if (src == null || (len = src.length()) == 0) {
			return src;
		}
		
		return new StringBuffer(len)
			.append(toLowerCase(src.charAt(0)))
			.append(src.substring(1)).toString();
	}
	
	public static String toJavaName(String name) {
		String tmp = capitalize(name);
		
		if (tmp.indexOf('_') > 0) {
			StringBuffer ret = new StringBuffer();
			ret.append(tmp.charAt(0));
			for (int i = 1, len = tmp.length(); i < len; i++) {
				if (tmp.charAt(i) == '_') {
					continue;
				}
				if (tmp.charAt(i - 1) == '_') {
					ret.append(toUpperCase(tmp.charAt(i)));
				}
				else {
					ret.append(tmp.charAt(i));
				}
			}
			return ret.toString();
		}
		
		return tmp;
	}
	
	public static String toUnderlineName(String name) {
		String tmp = uncapitalize(name);
		if (tmp.equals(name)) {
			return tmp;
		}
		
		StringBuffer ret = new StringBuffer();
		ret.append(tmp.charAt(0));
		for (int i = 1, len = tmp.length(); i < len; i++) {
			if (isUpperCase(tmp.charAt(i))) {
				ret.append('_');
				ret.append(toLowerCase(tmp.charAt(i)));
			}
			else {
				ret.append(tmp.charAt(i));
			}
		}
		
		return ret.toString();
	}
	
	public static boolean is(int val) {
		return val == 1;
	}
	
	public static boolean is(long val) {
		return val == 1;
	}
	
	public static boolean is(BigDecimal val) {
		return (val != null && val.intValue() == 1);
	}
}
