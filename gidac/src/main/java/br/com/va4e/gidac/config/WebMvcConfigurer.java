package br.com.va4e.gidac.config;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;


@Configuration
@EnableWebMvc
@ComponentScan
public class WebMvcConfigurer extends WebMvcConfigurerAdapter implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	    public WebMvcConfigurer() {
	        super();
	    }

	    /* ******************************************************************* */
	    /*  GENERAL CONFIGURATION ARTIFACTS                                    */
	    /*  Static Resources, i18n Messages, Formatters (Conversion Service)   */
	    /* ******************************************************************* */

	    @Override
	    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
	        super.addResourceHandlers(registry);
	        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
	        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
	        registry.addResourceHandler("/js/**").addResourceLocations("/js/")
	        .resourceChain(true)
	        .addResolver(new WebJarsResourceResolver());

	    }

	    @Bean
	    ReloadableResourceBundleMessageSource messageSource() {
	        ReloadableResourceBundleMessageSource bundleMessageSource = new ReloadableResourceBundleMessageSource();
	        bundleMessageSource.setBasename("classpath:i18n/messages");
	        bundleMessageSource.setCacheSeconds(1800);
	        bundleMessageSource.setDefaultEncoding("UTF-8");
	        return bundleMessageSource;
	    }


	    /* **************************************************************** */
	    /*  THYMELEAF-SPECIFIC ARTIFACTS                                    */
	    /*  TemplateResolver <- TemplateEngine <- ViewResolver              */
	    /* **************************************************************** */

	    @Bean
	    public SpringResourceTemplateResolver templateResolver(){

	        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
	        
	       // templateResolver.setApplicationContext(this.applicationContext);
	        
	        templateResolver.setPrefix("/WEB-INF/templates/");
	        templateResolver.setSuffix(".html");
	        //templateResolver.setTemplateMode(TemplateMode.HTML);
	        // Template cache is true by default. Set to false if you want
	        // templates to be automatically updated when modified.
	        //templateResolver.setCacheable(true);
	        return templateResolver;
	    }

	    @Bean
	    public SpringTemplateEngine templateEngine(){
	        // SpringTemplateEngine automatically applies SpringStandardDialect and
	        // enables Spring's own MessageSource message resolution mechanisms.
	        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	        templateEngine.setTemplateResolver(templateResolver());
	        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
	        // speed up execution in most scenarios, but might be incompatible
	        // with specific cases when expressions in one template are reused
	        // across different data types, so this flag is "false" by default
	        // for safer backwards compatibility.
	       // templateEngine.setEnableSpringELCompiler(true);
	        
	        return templateEngine;
	    }

	    @Bean
	    public ThymeleafViewResolver viewResolver(){
	        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	        viewResolver.setTemplateEngine(templateEngine());
	        return viewResolver;
	    }

	    @Bean
	    public LocaleResolver localeResolver(){
	        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
	        sessionLocaleResolver.setDefaultLocale(Locale.US);
	        return sessionLocaleResolver;
	    }

	    @Bean
	    LocaleChangeInterceptor localeChangeInterceptor(){
	        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	        localeChangeInterceptor.setParamName("lang");
	        return localeChangeInterceptor;
	    }

	    @Override
	    public void addInterceptors(InterceptorRegistry interceptorRegistry){
	        interceptorRegistry.addInterceptor(localeChangeInterceptor());
	    }

		@Override
		public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext)
				throws BeansException {
			this.applicationContext = applicationContext;
			
		}
	

}




   





