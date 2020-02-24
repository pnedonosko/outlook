package org.exoplatform.outlook.app.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;
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
    // TODO no such path in the WAR: /WEB-INF/views/react/build why we need it?
    registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/views/react/build/static/");
    registry.addResourceHandler("/*.js").addResourceLocations("/WEB-INF/views/react/build/");
    registry.addResourceHandler("/*.json").addResourceLocations("/WEB-INF/views/react/build/");
    registry.addResourceHandler("/*.ico").addResourceLocations("/WEB-INF/views/react/build/");
    registry.addResourceHandler("/**").addResourceLocations("/"); // TODO /app ?
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new ExoContainerRequestLifeCycleInterceptor()).addPathPatterns("/app/*");
    registry.addInterceptor(new ExoContainerRequestLifeCycleInterceptor()).addPathPatterns("/v1/*"); // v1 doesn't handled by this app at all
    registry.addInterceptor(new ExoContainerRequestLifeCycleInterceptor()).addPathPatterns("/v2/*");
  }

  @Bean // TODO Why we need this as a container managed bean? Where it used?
  public ViewResolver htmlViewResolver() {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
    resolver.setContentType("text/html");
    resolver.setCharacterEncoding("UTF-8"); // TODO encoding suaully pointed by the HTML doc itself, why we need it here?
    resolver.setViewNames(new String[] { "*.html" });
    return resolver;
  }

  private ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.addDialect(new LayoutDialect(new GroupingStrategy())); // TODO why use deprecated?
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

  @Bean // TODO Why we need this as a container managed bean? Where it used?
  public ViewResolver javascriptViewResolver() {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine(javascriptTemplateResolver()));
    resolver.setContentType("application/javascript");
    resolver.setCharacterEncoding("UTF-8");
    resolver.setViewNames(new String[] { "*.js" });
    return resolver;
  }

  private ITemplateResolver javascriptTemplateResolver() {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setApplicationContext(applicationContext);
    // TODO Why we need JS resolver on this path where v1 scripts also live?
    resolver.setPrefix("/WEB-INF/js/");
    resolver.setCacheable(false);
    resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
    return resolver;
  }
}
