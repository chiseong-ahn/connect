# 프로젝트 설정

## 정적리소스 설정
- com.scglab.connect.base.websocket.WebMvcConfig.java
```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
}
```

## 인터셉터 정의
- com.scglab.connect.base.websocket.WebMvcConfig.java
```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(commonInterceptor)
            .excludePathPatterns("/webjars/**", "/static/**");
}
```

## CORS 설정
- com.scglab.connect.base.websocket.WebMvcConfig.java
```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
}
```

## Filter 적용
- com.scglab.connect.base.websocket.WebMvcConfig.java
```java
@Bean
public FilterRegistrationBean<XssEscapeServletFilter> getFilterRegistrationBean(){
    FilterRegistrationBean<XssEscapeServletFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new XssEscapeServletFilter());
    registrationBean.setOrder(1);
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
}
```


### Websocket 설정
- com.scglab.connect.base.websocket.WebsocketConfig
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(Constant.SOCKET_ENDPOINT) // 연결할 socket space.
        		.setAllowedOrigins("*")	  // socket CORS.
        		.withSockJS();			  // sockjs 사용.
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");	// 메세지를 받을 uri prefix
        registry.enableSimpleBroker("/sub");	// 메세지를 보낼 uri prefix
    }
}
```
[< 목록으로 돌아가기](manual.md)