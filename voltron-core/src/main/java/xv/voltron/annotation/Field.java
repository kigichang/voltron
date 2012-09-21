/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package xv.voltron.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DataBase column description
 * @author kigi
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
	/**
	 * Column name
	 */
	String fieldName() default "";
	
	/**
	 * It's not a physical column. 
	 * It's a database expression with other column.
	 */
	String expression() default "";
	
	/**
	 * Column default value
	 */
	String defValue() default "";
	
	/**
	 * Is the column primary or not
	 */
	boolean isPrimary() default false;
	
	/**
	 * Is the column auto increment or not
	 * In MySQL: auto_increment
	 * In MSSQL: Identity
	 * In Oracle: Sequence
	 */
	boolean isAutoIncrement() default false;
	
	/**
	 * Does the column allow null or not.
	 */
	boolean allowEmpty() default false; /* DB not null */
}
