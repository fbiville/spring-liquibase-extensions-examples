package com.github.lateralthoughts.configuration;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.*;

import com.github.lateralthoughts.liquibase.SpringLiquibaseChecker;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan(basePackages = "com.github.lateralthoughts")
@Import(EmbeddedServletContainerAutoConfiguration.class)
@EnableAutoConfiguration
public class Application {

    @Value("${url}")
    String databaseUrl;

    @Value("${username}")
    String databaseUsername;

    @Value("${password}")
    String databasePassword;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("datasource.properties"));
        return configurer;
    }

    @Bean
    @Lazy(false)
    public SpringLiquibaseChecker springLiquibaseChecker() {
        SpringLiquibaseChecker liquibaseChecker = new SpringLiquibaseChecker();
        liquibaseChecker.setDataSource(dataSource());
        liquibaseChecker.setIgnoringClasspathPrefix(true);
        liquibaseChecker.setChangeLog("classpath:META-INF/master.xml");
        return liquibaseChecker;
    }

    @Bean
    public DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(databaseUrl);
        dataSource.setUser(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
