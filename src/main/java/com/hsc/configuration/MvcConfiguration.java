package com.hsc.configuration;


//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hsc.interceptor.LoggerInterceptor;

@Configuration
//@PropertySource("classpath:Resource.properties")
public class MvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor())
		.excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**");
	}
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("UTF-8"); // 파일 인코딩 설정
		multipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); // 파일당 업로드 크기 제한 (5MB)
		return multipartResolver;
	}
	

//	@Value("${resource.path}")
//	private String resourcePath;
//	
//	@Value("${upload.path}")
//	private String uploadPath;
//	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler(uploadPath)
//				.addResourceLocations(resourcePath);
//	}
//	
	
	
	

}
