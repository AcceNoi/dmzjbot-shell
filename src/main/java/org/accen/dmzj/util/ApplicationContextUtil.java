package org.accen.dmzj.util;

import java.util.Map;

import org.springframework.context.ApplicationContext;

public class ApplicationContextUtil {
	private static ApplicationContext context;
	public static void setContext(ApplicationContext context) {
		ApplicationContextUtil.context = context;
	}
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}
	public static <T> T getBean(String beanName,Class<T> clazz) {
		return context.getBean(clazz);
	}
	public static <T> Map<String,T> getBeans(Class<T> clazz){
		return context.getBeansOfType(clazz);
	}
	public static ApplicationContext getContext() {
		return context;
	}
}
