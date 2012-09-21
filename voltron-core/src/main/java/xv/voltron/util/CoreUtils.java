package xv.voltron.util;

import java.util.ArrayList;
import java.util.List;

public final class CoreUtils {

	public static List<String>split(String str, char seperator) {
		List<String> ret = new ArrayList<String>();
		int len = 0, i = 0, start = 0;
		if (str != null && (len = str.length()) > 0) {
			while(i < len) {
				if (str.charAt(i) == seperator) {
					if (start > 0) {
						ret.add(str.substring(start, i));
					}
					start = ++i;
					continue;
				}
				i++;
			}
			if (start < len) {
				ret.add(str.substring(start, len));
			}
		}
		
		return ret;
	}
}
