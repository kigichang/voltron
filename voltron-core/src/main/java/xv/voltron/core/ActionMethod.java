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
		this.parameterLength = tmp.length - 2;
		types = new DataType[parameterLength];
		for (int i = 0; i < parameterLength; i++) {
			types[i] = DataType.valueOf(tmp[i + 2]);
		}
		this.strict = strict;
		
		this.strictLength = strictLength;
	}
	
}
