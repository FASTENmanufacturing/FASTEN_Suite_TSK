package com.fasten.wp4.iot.kafka.config;
//package com.fasten.wp4.iot.kafka.config;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories( entityManagerFactoryRef = "h2EntityManagerFactory", 
//transactionManagerRef = "h2TransactionManager", 
//basePackages = { "com.fasten.wp4.iot.kafka.repository" })
//public class H2DbConfig {
//	
//	@Autowired
//    private Environment env;
//
//	@Bean(name = "h2DataSource")
//	public DataSource h2DataSource() {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//		dataSource.setDriverClassName(env.getProperty("h2.spring.datasource.driverClassName"));
//		dataSource.setUrl(env.getProperty("h2.spring.datasource.url"));
//		dataSource.setUsername(env.getProperty("h2.spring.datasource.username"));
//		dataSource.setPassword(env.getProperty("h2.spring.datasource.password"));
//
//		return dataSource;
//	}
//
//	@Bean(name = "h2EntityManagerFactory")
//	public LocalContainerEntityManagerFactoryBean h2EntityManagerFactory( EntityManagerFactoryBuilder builder, @Qualifier("h2DataSource") DataSource h2DataSource) {
//		return builder
//				.dataSource(h2DataSource)
//				.packages("com.fasten.wp4.iot.kafka.model")
//				.persistenceUnit("h2")
//				.build();
//	}
//	@Bean(name = "h2TransactionManager")
//	public PlatformTransactionManager h2TransactionManager(@Qualifier("h2EntityManagerFactory") EntityManagerFactory h2EntityManagerFactory) {
//		return new JpaTransactionManager(h2EntityManagerFactory);
//	}
//}