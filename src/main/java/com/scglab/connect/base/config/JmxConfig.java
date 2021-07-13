package com.scglab.connect.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;

@Configuration
public class JmxConfig {

	@Bean
	public AnnotationMBeanExporter mbeanExporter() {
		final AnnotationMBeanExporter exporter = new AnnotationMBeanExporter();
		exporter.setRegistrationPolicy(RegistrationPolicy.FAIL_ON_EXISTING);
		exporter.setAutodetectMode(MBeanExporter.AUTODETECT_ASSEMBLER);
		return exporter;
	}

}
