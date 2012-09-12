package xv.voltron.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import xv.voltron.constant.ArgumentPolicy;
import xv.voltron.constant.DataType;

public class ActionMethod {

	protected Method invokeMethod = null;
	protected DataType[] types = null;
	protected ArgumentPolicy strict = ArgumentPolicy.STRICT;
	protected int parameterLength = 0;
	protected int strictLength = 0;
	
	public ActionMethod(Method method, ArgumentPolicy strict, int strictLength) {
		this.invokeMethod = method;
		Class<?>[] tmp = invokeMethod.getParameterTypes();
		types = new DataType[tmp.length];
		for (int i = 0, len = tmp.length; i < len; i++) {
			types[i] = DataType.valueOf(tmp[i]);
		}
		this.strict = strict;
		this.parameterLength = types.length - 2;
		this.strictLength = strictLength;
	}
	
}
