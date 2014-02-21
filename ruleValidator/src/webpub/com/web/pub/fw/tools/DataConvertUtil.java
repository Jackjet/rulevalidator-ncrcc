package com.web.pub.fw.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DataConvertUtil
// extends TestCase
{

	private static Field[] getFields(Object object) {
		Class<?> clazz = object.getClass();
		return clazz.getFields();
	}

	private static Method[] getMethods(Object object) {
		Class<?> clazz = object.getClass();
		return clazz.getMethods();
	}

	private static boolean containsField(Object object, String fieldName) {
		if (null == object || null == fieldName) {
			return false;
		}

		for (Field field : getFields(object)) {
			String name = field.getName().toLowerCase();
			if (name.equals(fieldName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private static Object getValue(Object object, Field field) {
		if (null == object || null == field) {
			return null;
		}

		String fieldName = field.getName().toLowerCase();
		for (Method method : getMethods(object)) {
			String methodName = method.getName().toLowerCase();
			if (methodName.startsWith("get")) {
				methodName = methodName.substring(3);
			} else if (methodName.startsWith("is")) {
				methodName = methodName.substring(2);
			}
			if (methodName.equals(fieldName.toLowerCase())) {
				try {
					return method.invoke(object, new Object[0]);
				} catch (Exception e) {

				}
			}
		}
		return null;
	}

	private static void setValue(Object object, Field field, Object value) {
		if (null == object || null == value) {
			return;
		}

		String fieldName = field.getName().toLowerCase();
		for (Method method : getMethods(object)) {
			String methodName = method.getName().toLowerCase();
			if (methodName.startsWith("set")) {
				methodName = methodName.substring(3);
			}
			if (methodName.equals(fieldName.toLowerCase())) {
				try {
					method.invoke(object, value);
				} catch (Exception e) {

				}
			}
		}
	}

	public static void convert(Object objectFrom, Object objectTo) {
		Field[] fields = getFields(objectFrom);
		for (Field field : fields) {
			if (containsField(objectTo, field.getName())) {
				Object value = getValue(objectFrom, field);
				setValue(objectTo, field, value);
			}
		}
	}

	public static void main(String[] args) {
		DataConvertUtil util = new DataConvertUtil();
		Data1 data1 = util.new Data1();
		data1.setName("data1");
		Data2 data2 = util.new Data2();

		DataConvertUtil.convert(data1, data2);

	}

	class Data1 {
		private String name;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	class Data2 {
		private String name;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
