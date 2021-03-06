/**
 * WebSocket
 */
package com.xiongyingqi.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author 瑛琪
 * @version 2013-8-7 下午12:09:13
 */
public class EntityHelper {
    // com.kingray.hibernate.domain.KrUser@d0b4b2f[userId=<null>,userName=aaa,userPassword=bbb,
    // a={b, c}]

    private static String reflectToString2(Object object) {
	if (object == null) {
	    return null;
	}
	Class clazz = object.getClass();
	Class superClazz = clazz.getSuperclass();

	StringBuilder builder = new StringBuilder(clazz.getName());
	builder.append("@");
	builder.append(object.hashCode());
	builder.append("[");
	Field[] fields = clazz.getDeclaredFields();
	Field[] superFields = superClazz.getDeclaredFields();

	int length = fields.length;
	int countLength = length;
	for (int i = 0; i < length; i++) {
	    Field field = fields[i];
	    if (Modifier.isStatic(field.getModifiers())) {
		countLength--;
		continue;
	    }
	    String fieldName = field.getName();
	    String methodGet = "get" + fieldName.substring(0, 1).toUpperCase()
		    + fieldName.substring(1, fieldName.length());
	    try {
		Method method = clazz.getDeclaredMethod(methodGet);
		try {
		    Object value = method.invoke(object);
		    builder.append(fieldName);
		    builder.append("=");
		    if (value == null) {
			builder.append("<null>");
		    } else {
			if (value.getClass().isArray()) {
			    int arrayLength = Array.getLength(value);
			    builder.append("{");
			    for (int j = 0; j < arrayLength; j++) {
				Object object2 = Array.get(value, j);
				builder.append(object2.toString());
				if (j < arrayLength - 1) {
				    builder.append(", ");
				}
			    }
			    builder.append("}");
			} else {
			    builder.append(value.toString());
			}
		    }
		    if (i < countLength - 1) {
			builder.append(", ");
		    }
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	    } catch (SecurityException e) {
	    } catch (NoSuchMethodException e) {
	    }
	}

	int superLength = superFields.length;
	countLength = superLength;
	for (int i = 0; i < superLength; i++) {
	    Field field = superFields[i];
	    if (Modifier.isStatic(field.getModifiers())) {
		countLength--;
		continue;
	    }
	    if (countLength > 0) {
		builder.append(", ");
	    }
	    String fieldName = field.getName();
	    String methodGet = "get" + fieldName.substring(0, 1).toUpperCase()
		    + fieldName.substring(1, fieldName.length());
	    try {
		Method method = superClazz.getDeclaredMethod(methodGet);
		try {
		    Object value = method.invoke(object);
		    builder.append(fieldName);
		    builder.append("=");
		    if (value == null) {
			builder.append("<null>");
		    } else {
			if (value.getClass().isArray()) {
			    int arraysuperLength = Array.getLength(value);
			    builder.append("{");
			    for (int j = 0; j < arraysuperLength; j++) {
				Object object2 = Array.get(value, j);
				builder.append(object2.toString());
				if (j < arraysuperLength - 1) {
				    builder.append(", ");
				}
			    }
			    builder.append("}");
			} else {
			    builder.append(value.toString());
			}
		    }
		    if (i < countLength - 1) {
			builder.append(", ");
		    }
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	    } catch (SecurityException e) {
	    } catch (NoSuchMethodException e) {
	    }
	}

	builder.append("]");
	return builder.toString();
    }

    /**
     * 获取类的所有超类 <br>
     * 2013-9-15 下午8:16:21
     * 
     * @return
     */
    public static Collection<Class> getAllSuperClassesOfClass(Class clazz) {
	Collection<Class> classes = new LinkedHashSet<Class>();
	classes.add(clazz);
	Class superClass = clazz.getSuperclass();
	while (superClass != null) {
	    classes.add(superClass);
	    superClass = superClass.getSuperclass();
	}
	return classes;
    }

    /**
     * 使用反射机制自动ToString <br>
     * 2013-8-28 下午3:16:40
     * 
     * @param object
     * @return
     */
    public static String reflectToString(Object object) {
	if (object == null) {
	    return null;
	}
	Class clazz = object.getClass();

	StringBuilder builder = new StringBuilder(clazz.getName());
	builder.append("@");
	builder.append(Integer.toHexString(object.hashCode()));
	builder.append("[");

	Set<Method> methods = new LinkedHashSet<Method>();

	Collection<Class> classes = getAllSuperClassesOfClass(clazz);
	for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
	    Class claxx = (Class) iterator.next();
	    Method[] clazzMethods = claxx.getDeclaredMethods();
	    for (int i = 0; i < clazzMethods.length; i++) {
		Method method = clazzMethods[i];
		methods.add(method);
	    }
	}

	// for (int i = 0; i < methods.length; i++) {
	// Method method = methods[i];
	for (Iterator iterator = methods.iterator(); iterator.hasNext();) {
	    Method method = (Method) iterator.next();
	    String methodName = method.getName();
	    if (methodName.startsWith("get")
		    && !Modifier.isStatic(method.getModifiers())
		    && Modifier.isPublic(method.getModifiers())) {
		try {
		    Object value = method.invoke(object);
		    String propertyName = methodName.substring(3, 4)
			    .toLowerCase() + methodName.substring(4);
		    builder.append(propertyName);
		    builder.append("=");

		    if (value == null) {
			builder.append("<null>");
		    } else {
			if (value.getClass().isArray()) {
			    int arraysuperLength = Array.getLength(value);
			    builder.append("{");
			    for (int j = 0; j < arraysuperLength; j++) {
				Object object2 = Array.get(value, j);
				builder.append(object2.toString());
				if (j < arraysuperLength - 1) {
				    builder.append(", ");
				}
			    }
			    builder.append("}");
			} else {
			    builder.append(value.toString());
			}
		    }
		    builder.append(", ");
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	    }
	}

	if (builder.toString().contains(", ")) {
	    builder.replace(builder.length() - 2, builder.length(), "");
	}

	builder.append("]");
	return builder.toString();
    }

    /**
     * 简单的ToString方法，只会查找简单类型的数据 <br>
     * 2013-8-14 下午3:42:01
     * 
     * @param object
     * @return
     */
    public static String simpleReflectToString(Object object) {
	if (object == null) {
	    return null;
	}
	Class clazz = object.getClass();
	Class superClazz = clazz.getSuperclass();

	StringBuilder builder = new StringBuilder(clazz.getName());
	builder.append("@");
	builder.append(object.hashCode());
	builder.append("[");

	Method[] methods = clazz.getDeclaredMethods();
	for (int i = 0; i < methods.length; i++) {
	    Method method = methods[i];
	    if (!isSimpleType(method.getReturnType())) {
		continue;
	    }
	    String methodName = method.getName();
	    if (methodName.startsWith("get")
		    && !Modifier.isStatic(method.getModifiers())
		    && Modifier.isPublic(method.getModifiers())) {
		try {
		    Object value = method.invoke(object);

		    String propertyName = methodName.substring(3, 4)
			    .toLowerCase() + methodName.substring(4);
		    builder.append(propertyName);
		    builder.append("=");

		    if (value == null) {
			builder.append("<null>");
		    } else {
			if (value.getClass().isArray()) {
			    int arraysuperLength = Array.getLength(value);
			    builder.append("{");
			    for (int j = 0; j < arraysuperLength; j++) {
				Object object2 = Array.get(value, j);
				builder.append(object2.toString());
				if (j < arraysuperLength - 1) {
				    builder.append(", ");
				}
			    }
			    builder.append("}");
			} else {
			    builder.append(value.toString());
			}
		    }
		    builder.append(", ");
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	    }
	}
	if (builder.toString().contains(", ")) {
	    builder.replace(builder.length() - 2, builder.length(), "");
	}

	builder.append("]");
	return builder.toString();
    }

    /**
     * 简单的ToString方法，只会查找简单类型的数据 <br>
     * 2013-8-14 下午3:42:01
     * 
     * @param object
     * @return
     */
    private static String simpleReflectToString2(Object object) {
	if (object == null) {
	    return null;
	}
	Class clazz = object.getClass();
	Class superClazz = clazz.getSuperclass();

	StringBuilder builder = new StringBuilder(clazz.getName());
	builder.append("@");
	builder.append(object.hashCode());
	builder.append("[");
	Field[] fields = clazz.getDeclaredFields();
	Field[] superFields = superClazz.getDeclaredFields();

	int length = fields.length;
	int countLength = length;
	for (int i = 0; i < length; i++) {
	    Field field = fields[i];
	    if (Modifier.isStatic(field.getModifiers()) || !isSimpleType(field)) {
		countLength--;
		continue;
	    }
	    String fieldName = field.getName();
	    String methodGet = "get" + fieldName.substring(0, 1).toUpperCase()
		    + fieldName.substring(1, fieldName.length());
	    try {
		Method method = clazz.getDeclaredMethod(methodGet);
		try {
		    Object value = method.invoke(object);
		    builder.append(fieldName);
		    builder.append("=");
		    if (value == null) {
			builder.append("<null>");
		    } else {
			if (value.getClass().isArray()) {
			    int arrayLength = Array.getLength(value);
			    builder.append("{");
			    for (int j = 0; j < arrayLength; j++) {
				Object object2 = Array.get(value, j);
				builder.append(object2.toString());
				if (j < countLength - 1) {
				    builder.append(", ");
				}
			    }
			    builder.append("}");
			} else {
			    builder.append(value.toString());
			}
		    }
		    builder.append(", ");
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	    } catch (SecurityException e) {
	    } catch (NoSuchMethodException e) {
	    }
	}
	if (builder.toString().contains(", ")) {
	    builder.replace(builder.length() - 2, builder.length(), "");
	}

	int superLength = superFields.length;
	countLength = superLength;
	for (int i = 0; i < superLength; i++) {
	    Field field = superFields[i];
	    if (Modifier.isStatic(field.getModifiers()) || !isSimpleType(field)) {
		countLength--;
		continue;
	    }
	    if (countLength > 0) {
		builder.append(", ");
	    }
	    String fieldName = field.getName();
	    String methodGet = "get" + fieldName.substring(0, 1).toUpperCase()
		    + fieldName.substring(1, fieldName.length());
	    try {
		Method method = superClazz.getDeclaredMethod(methodGet);
		try {
		    Object value = method.invoke(object);
		    builder.append(fieldName);
		    builder.append("=");
		    if (value == null) {
			builder.append("<null>");
		    } else {
			if (value.getClass().isArray()) {
			    int arraysuperLength = Array.getLength(value);
			    builder.append("{");
			    for (int j = 0; j < arraysuperLength; j++) {
				Object object2 = Array.get(value, j);
				builder.append(object2.toString());
				if (j < arraysuperLength - 1) {
				    builder.append(", ");
				}
			    }
			    builder.append("}");
			} else {
			    builder.append(value.toString());
			}
		    }
		    if (i < countLength - 1) {
			builder.append(", ");
		    }
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	    } catch (SecurityException e) {
	    } catch (NoSuchMethodException e) {
	    }
	}

	builder.append("]");
	return builder.toString();

    }

    public static boolean isSimpleType(Field field) {
	if (field == null) {
	    return false;
	}
	return isSimpleType(field.getType());
    }

    public static boolean isSimpleType(Class<?> clazz) {
	if (clazz == null) {
	    return false;
	}
	// System.out.println(field.getType().getName());

	if (clazz.equals(Boolean.class)) {
	    return true;
	} else if (clazz.equals(Float.class)) {
	    return true;
	} else if (clazz.equals(Long.class)) {
	    return true;
	} else if (clazz.equals(Double.class)) {
	    return true;
	} else if (clazz.equals(Byte.class)) {
	    return true;
	} else if (clazz.equals(Integer.class)) {
	    return true;
	} else if (clazz.equals(Short.class)) {
	    return true;
	} else if (clazz.equals(Character.class)) {
	    return true;
	} else if (clazz.equals(BigInteger.class)) {
	    return true;
	} else if (clazz.equals(BigDecimal.class)) {
	    return true;
	} else if (clazz.equals(String.class)) {
	    return true;
	} else if (clazz.equals(boolean.class)) {
	    return true;
	} else if (clazz.equals(float.class)) {
	    return true;
	} else if (clazz.equals(long.class)) {
	    return true;
	} else if (clazz.equals(double.class)) {
	    return true;
	} else if (clazz.equals(byte.class)) {
	    return true;
	} else if (clazz.equals(int.class)) {
	    return true;
	} else if (clazz.equals(short.class)) {
	    return true;
	} else if (clazz.equals(char.class)) {
	    return true;
	}
	return false;
    }

    /**
     * 计算对象的hashCode <br>
     * 2013-8-14 下午3:43:44
     * 
     * @param object
     * @return
     */
    public static int hashCode(Object object) {
	if (object == null) {
	    return 0;
	}

	int hashCode = 17;
	Class clazz = object.getClass();
	Field[] fields = clazz.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
	    Field field = fields[i];
	    if (Modifier.isStatic(field.getModifiers())) {
		continue;
	    }
	    String fieldName = field.getName();
	    String methodGet = "get" + fieldName.substring(0, 1).toUpperCase()
		    + fieldName.substring(1, fieldName.length());
	    try {
		Method method = clazz.getDeclaredMethod(methodGet);
		try {
		    Object value = method.invoke(object); // 拥有get方法的参数
		    hashCode = hashCode * 31
			    + (value == null ? 0 : value.hashCode());
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	    } catch (Exception e) {
	    }
	}

	Class<?> superClazz = clazz.getSuperclass();
	Field[] superFields = superClazz.getDeclaredFields();
	for (int i = 0; i < superFields.length; i++) {
	    Field field = superFields[i];
	    if (Modifier.isStatic(field.getModifiers())) {
		continue;
	    }
	    String fieldName = field.getName();
	    String methodGet = "get" + fieldName.substring(0, 1).toUpperCase()
		    + fieldName.substring(1, fieldName.length());
	    try {
		Method method = superClazz.getDeclaredMethod(methodGet);
		try {
		    Object value = method.invoke(object); // 拥有get方法的参数
		    hashCode = hashCode * 31
			    + (value == null ? 0 : value.hashCode());
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	    } catch (Exception e) {
	    }
	}

	return hashCode;
    }

    /**
     * 比较两个对象 <br>
     * 2013-8-14 下午3:44:00
     * 
     * @param origin
     * @param target
     * @return
     */
    public static boolean equals(Object origin, Object target) {
	boolean isEquals = false;
	if (origin == null) {
	    if (target == null) {
		isEquals = true;
	    } else {
		isEquals = false;
	    }
	} else {
	    if (target == null) {
		isEquals = false;
	    } else { // 两者都不为空
		if (origin.getClass() == target.getClass()) {
		    if (origin.hashCode() == target.hashCode()) {
			isEquals = true;
		    } else {
			isEquals = false;
		    }
		} else {
		    isEquals = false;
		}
	    }
	}
	return isEquals;
    }

    /**
     * 打印对象， <br>
     * 2013-8-26 下午2:45:02
     * 
     * @param object
     */
    public static void printDetail(Object object) {
	StackTraceElement[] stackTraceElements = StackTraceHelper
		.getStackTrace();
	StackTraceElement stackTraceElement = stackTraceElements[2]; // 调用本类的对象类型堆栈
	StringBuilder builder = new StringBuilder();
	builder.append(" ------------------------------------------------------------ ");
	builder.append(StringHelper.line());
	builder.append(StackTraceHelper
		.buildStackTrace(new StackTraceElement[] { stackTraceElement }));
	builder.append("    ");
	if (object == null) {
	    builder.append("<null>");
	} else {
	    builder.append(object.getClass().getSimpleName());
	    builder.append(" =============== ");
	    builder.append(reflectToString(object));
	}
	builder.append(StringHelper.line());
	builder.append(" ------------------------------------------------------------ ");
	System.out.println(builder.toString());
    }

    /**
     * 打印对象， <br>
     * 2013-8-26 下午2:45:02
     * 
     * @param object
     */
    public static void print(Object object) {
	StackTraceElement[] stackTraceElements = StackTraceHelper
		.getStackTrace();
	StackTraceElement stackTraceElement = stackTraceElements[2]; // 调用本类的对象类型堆栈
	StringBuilder builder = new StringBuilder();
	builder.append(" ------------------------------------------------------------ ");
	builder.append(StringHelper.line());
	builder.append(StackTraceHelper
		.buildStackTrace(new StackTraceElement[] { stackTraceElement }));
	builder.append("    ");
	if (object == null) {
	    builder.append("<null>");
	} else {
	    builder.append(object.getClass().getSimpleName());
	    builder.append(" =============== ");
	    builder.append(object.toString());
	}
	builder.append(StringHelper.line());
	builder.append(" ------------------------------------------------------------ ");
	System.out.println(builder.toString());
    }

    /**
     * <br>
     * 2013-8-8 上午11:36:26
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	return equals(this, obj);
    }

    /**
     * <br>
     * 2013-8-8 上午11:16:04
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	return hashCode(this);
    }

    /**
     * <br>
     * 2013-8-7 下午12:21:49
     * 
     * @return
     */
    private String reflectToString() {
	return reflectToString(this);
    }

    /**
     * <br>
     * 2013-8-7 下午12:21:05
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return this.reflectToString();
    }

    static class A {
	int a;
	int b;
	int c;
	String d;

	/**
	 * int
	 * 
	 * @return the a
	 */
	public int getA() {
	    return this.a;
	}

	/**
	 * int
	 * 
	 * @param a
	 *            the a to set
	 */
	public void setA(int a) {
	    this.a = a;
	}

	/**
	 * int
	 * 
	 * @return the b
	 */
	public int getB() {
	    return this.b;
	}

	/**
	 * int
	 * 
	 * @param b
	 *            the b to set
	 */
	public void setB(int b) {
	    this.b = b;
	}

	/**
	 * int
	 * 
	 * @return the c
	 */
	public int getC() {
	    return this.c;
	}

	/**
	 * int
	 * 
	 * @param c
	 *            the c to set
	 */
	public void setC(int c) {
	    this.c = c;
	}

	/**
	 * String
	 * 
	 * @return the d
	 */
	public String getD() {
	    return this.d;
	}

	/**
	 * String
	 * 
	 * @param d
	 *            the d to set
	 */
	public void setD(String d) {
	    this.d = d;
	}

    }

    public static void main(String[] args) {
	A a = new A();
	a.setA(124);
	a.setB(2);
	a.setD("asfasf");
	printDetail(a);
    }

}
