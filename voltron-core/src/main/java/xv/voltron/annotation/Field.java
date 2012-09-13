package xv.voltron.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Field {
	String fieldName() default "";
	String expression() default "";
	String defValue() default "";
	boolean isPrimary() default false;
	boolean isAutoIncrement() default false;
}
