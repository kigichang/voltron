package xv.voltron.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import xv.voltron.constant.ArgumentPolicy;

public class ActionMethod {

	protected Method invokeMethod = null;
	protected Type[] types = null;
	protected ArgumentPolicy strict = ArgumentPolicy.STRICT;
	protected int parameterLength = 0;
	protected int strictLength = 0;
	
	public ActionMethod(Method method, ArgumentPolicy strict, int strictLength) {
		this.invokeMethod = method;
		this.types = invokeMethod.getParameterTypes();
		this.strict = strict;
		this.parameterLength = types.length - 2;
		this.strictLength = strictLength;
	}
	
}
