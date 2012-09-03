package xv.voltron.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xv.voltron.constant.RequestScope;
import xv.voltron.constant.ArgumentPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Dispatch {
	RequestScope scope() default RequestScope.BOTH;
	ArgumentPolicy policy() default ArgumentPolicy.STRICT;
	int strictLength() default 0;
}
