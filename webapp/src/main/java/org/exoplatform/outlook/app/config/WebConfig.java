package org.exoplatform.outlook.app.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingRespectLayoutTitleStrategy;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.app.config.service.OutlookServiceFactory;
import org.exoplatform.outlook.app.rest.interceptor.ExoContainerRequestLifeCycleInterceptor;
import org.exoplatform.outlook.app.rest.localization.CustomMessageSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.exoplatform.outlook.app")
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/app/**").addResourceLocations("/");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new ExoContainerRequestLifeCycleInterceptor()).addPathPatterns("/view/**");
    registry.addInterceptor(new ExoContainerRequestLifeCycleInterceptor()).addPathPatterns("/v2/**");
  }

  @Bean
  public ViewResolver htmlViewResolver() {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
    resolver.setContentType("text/html");
    resolver.setViewNames(new String[] { "*.html" });
    return resolver;
  }

  private ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.addDialect(new LayoutDialect(new GroupingRespectLayoutTitleStrategy()));
    engine.addDialect(new Java8TimeDialect());
    engine.setTemplateResolver(templateResolver);
    engine.setTemplateEngineMessageSource(messageSource());
    return engine;
  }

  private ITemplateResolver htmlTemplateResolver() {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setApplicationContext(applicationContext);
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setCacheable(false);
    resolver.setTemplateMode(TemplateMode.HTML);
    return resolver;
  }

  @Bean(name = "messageSource")
  @Description("Spring Message Resolver")
  public CustomMessageSource messageSource() {
    CustomMessageSource customMessageSource = new CustomMessageSource();
    return customMessageSource;
  }

  // Services config

  /*
  * We should try to use @Lazy annotation in order to initialize beans when we have an access to services (server started)
  * */

  /*@Bean(name = "outlookServiceFactory")
  public OutlookServiceFactory outlookServiceFactory() {
    OutlookServiceFactory factory = new OutlookServiceFactory();
    return factory;
  }

  @Bean
  public OutlookService outlookService() throws Exception {
    return outlookServiceFactory().getObject();
  }*/
}
