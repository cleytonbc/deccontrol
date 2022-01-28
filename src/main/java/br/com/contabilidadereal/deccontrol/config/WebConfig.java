package br.com.contabilidadereal.deccontrol.config;


import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import br.com.contabilidadereal.deccontrol.controller.converter.DeclaracaoConverter;
import br.com.contabilidadereal.deccontrol.controller.converter.EmpresaConverter;
import nz.net.ultraq.thymeleaf.LayoutDialect;


@Configuration
@ComponentScan(basePackages = "br.com.contabilidadereal.deccontrol.controller")
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		engine.addDialect(new LayoutDialect());
		engine.addDialect(new Java8TimeDialect());
		engine.addDialect(new SpringSecurityDialect());
		return engine;
	}

	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}
	
	@Bean
	public FormattingConversionService mvcConversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		conversionService.addConverter(new EmpresaConverter());
		conversionService.addConverter(new DeclaracaoConverter());
	
		
		NumberStyleFormatter bigDecimalFormatter = new NumberStyleFormatter("#,##0.00"); // Define o padrão de formatação numérico
		conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter); // Para os números BigDecimal, define o formato bigDecimalFormatter 
		
		NumberStyleFormatter integerFormatter = new NumberStyleFormatter("#,##0");
		conversionService.addFormatterForFieldType(Integer.class, integerFormatter);
		
		return conversionService;
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}
	
	//necessário para converter json chamando outra entidade lazy
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {

	    MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

	    ObjectMapper objectMapper = new ObjectMapper();
	    Hibernate5Module hibernate5Module = new Hibernate5Module();
	    hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
	    hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false);

	    objectMapper.registerModule(hibernate5Module);
	    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY); //optional

	    messageConverter.setObjectMapper(objectMapper);

	    return messageConverter;
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter< ? >> converters) {
	    converters.add(mappingJackson2HttpMessageConverter());

	    super.configureMessageConverters(converters);
	}
    

}