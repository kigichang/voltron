package xv.voltron.core;

import org.apache.commons.lang3.StringUtils;

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
	
	
	public static String toJavaName(String name) {
		String tmp = StringUtils.capitalize(name);
		if (tmp.indexOf('_') > 0) {
			char[] t = tmp.toCharArray();
			StringBuffer ret = new StringBuffer();
			ret.append(t[0]);
			for (int i = 1, len = t.length; i < len; i++) {
				if (t[i] == '_') {
					continue;
				}
				if (t[i - 1] == '_') {
					ret.append(toUpperCase(t[i]));
				}
				else {
					ret.append(t[i]);
				}
			}
			return ret.toString();
		}
		
		return tmp;
	}
	
	public static String toUnderlineName(String name) {
		String tmp = StringUtils.uncapitalize(name);
		if (tmp.equals(name)) {
			return tmp;
		}
		
		char[] t = tmp.toCharArray();
		StringBuffer ret = new StringBuffer();
		ret.append(t[0]);
		for (int i = 1, len = t.length; i < len; i++) {
			if (isUpperCase(t[i])) {
				ret.append('_');
				ret.append(toLowerCase(t[i]));
			}
			else {
				ret.append(t[i]);
			}
		}
		
		return ret.toString();
	}
	
}
