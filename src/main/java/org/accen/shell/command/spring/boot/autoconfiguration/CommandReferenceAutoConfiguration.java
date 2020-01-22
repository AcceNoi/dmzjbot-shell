package org.accen.shell.command.spring.boot.autoconfiguration;

import java.util.List;

import org.accen.shell.command.annotation.CommandReference;
import org.accen.shell.command.spring.CommandReferenceScannerConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

@Configuration
public class CommandReferenceAutoConfiguration {

	public static class AutoConfiguredCommandScannerRegistrar implements BeanFactoryAware,ImportBeanDefinitionRegistrar{
		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
			//扫描@CommandReference
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CommandReferenceScannerConfiguration.class);
			builder.addPropertyValue("annotationClass", CommandReference.class);
			builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
			registry.registerBeanDefinition(CommandReferenceScannerConfiguration.class.getName(),builder.getBeanDefinition());
		}

		private BeanFactory beanFactory;
		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			this.beanFactory = beanFactory;
		}
	}
	

}
