package com.scglab.connect.base.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import com.scglab.connect.base.interceptor.CommonInterceptor;

import net.rakugakibox.util.YamlResourceBundle;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Autowired
	private CommonInterceptor commonInterceptor;
	
	/**
	 * 
	 * @Method Name : addResourceHandlers
	 * @작성일 : 2020. 9. 24.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : static(정적 리소스) 디렉토리 설정.
	 * @return
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}
	
	/**
	 * 
	 * @Method Name : addInterceptors
	 * @작성일 : 2020. 9. 24.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 인터셉터 등록 및 인터셉터를 제외시킬 Pattern 설
	 * @return
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonInterceptor)
		.excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**");
	}
	
	/**
	 * 
	 * @Method Name : addCorsMappings
	 * @작성일 : 2020. 9. 24.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : CORS 기본설정  
	 * @return
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	
	/**
	 * 
	 * @Method Name : localeResolver
	 * @작성일 : 2020. 9. 24.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 다국어 기본 설정 
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver() { 
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver(); 
		// 언어&국가정보가 없는 경우 미국으로 인식하도록 설정 
		localeResolver.setDefaultLocale(Locale.US); 
		return localeResolver; 
	}
	
	/**
	 * 
	 * @Method Name : messageSource
	 * @작성일 : 2020. 9. 24.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Resource(message) 내용이 변경될 경우 자동으로 reload 될 수 있도록 설
	 * @return
	 */
	@Bean 
	public ReloadableResourceBundleMessageSource messageSource(){ 
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource(); 
		messageSource.setBasename("classpath:/messages/message"); 
		messageSource.setDefaultEncoding("UTF-8"); 
		messageSource.setCacheSeconds(180); 
		
		// 제공하지 않는 언어로 요청이 들어왔을 때 MessageSource에서 사용할 기본 언어정보. 
		Locale.setDefault(Locale.KOREAN); 
		return messageSource;
	}
	
	/**
	 * 
	 * @Method Name : messageSource
	 * @작성일 : 2020. 9. 24.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : yml을 통해 다국어 message 를 서비스하기위해 설정 
	 * @param basename
	 * @param encoding
	 * @return
	 */
	@Bean("messageSource")
    public MessageSource messageSource(@Value("${spring.messages.basename}") String basename, @Value("${spring.messages.encoding}") String encoding){
        YamlMessageSource ms = new YamlMessageSource();
        ms.setBasename(basename);
        ms.setDefaultEncoding(encoding);
        ms.setAlwaysUseMessageFormat(true);
        ms.setUseCodeAsDefaultMessage(true);
        ms.setFallbackToSystemLocale(true);
        return ms;
    }
	
	/**
	 * 
	 * @Method Name : getFilterRegistrationBean
	 * @작성일 : 2020. 9. 24.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : XSS를 방어하기 위한 Filter 적용 - lucy-xss-servlet-filter-rule.xml 참조. 
	 * @return
	 */
	@Bean
    public FilterRegistrationBean<XssEscapeServletFilter> getFilterRegistrationBean(){
        FilterRegistrationBean<XssEscapeServletFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssEscapeServletFilter());
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}

/**
 * 
 * @FileName : WebMvcConfig.java
 * @Project : connect
 * @Date : 2020. 9. 24. 
 * @작성자 : anchiseong
 * @변경이력 :
 * @프로그램 설명 : yaml 또는 yml 파일로 메세지를 사용하기 위해 ResourceBudle override 함. 
 
 */
class YamlMessageSource extends ResourceBundleMessageSource {
    @Override
    protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
        return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
    }
}
