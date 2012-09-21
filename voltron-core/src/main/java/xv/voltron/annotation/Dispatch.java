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

import xv.voltron.constant.RequestScope;
import xv.voltron.constant.ArgumentPolicy;

/**
 * Action method description
 * @author kigi
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Dispatch {
	/**
	 * method scope (GET, POST, or BOTH)
	 */
	RequestScope scope() default RequestScope.BOTH;
	
	/**
	 * parameter length must be the same or not
	 * STRICT: every parameter must have value and not null
	 * DEFAULT_NULL: every parameter may not have a value, it can be null.
	 */
	ArgumentPolicy policy() default ArgumentPolicy.STRICT;
	
	/**
	 * It's useful when policy is DEFAULT_NULL.
	 * How many parameters must have value.
	 */
	int strictLength() default 0;
}
