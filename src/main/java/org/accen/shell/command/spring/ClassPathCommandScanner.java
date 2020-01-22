package org.accen.shell.command.spring;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class ClassPathCommandScanner extends ClassPathBeanDefinitionScanner{

	public ClassPathCommandScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}
	private Class<? extends Annotation> annotationClass;
	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}
	public void registerFilters() {
		if (this.annotationClass != null) {
			addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
		}
	}
}
