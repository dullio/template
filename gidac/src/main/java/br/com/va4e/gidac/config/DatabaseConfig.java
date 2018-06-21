package br.com.va4e.gidac.config;

import java.beans.PropertyVetoException;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@PropertySource("classpath:database-persistence.properties")
@EnableJpaRepositories(enableDefaultTransactions = true)
@EnableTransactionManagement
public class DatabaseConfig {

	@Autowired
	private Environment env;

	private Logger logger = Logger.getLogger(getClass().getName());

	@Bean
	public DataSource DataSource() {

		// create connection pool
		ComboPooledDataSource dataSource = new ComboPooledDataSource();

		// set the jdbc driver class

		try {
			dataSource.setDriverClass(env.getProperty("idac.datasource.driver-class-name"));
		} catch (PropertyVetoException exc) {
			throw new RuntimeException(exc);
		}

		// log the connection props
		// for sanity's sake, log this info
		// just to make sure we are REALLY reading data from properties file

		logger.info(">>>Database jdbc.url=" + env.getProperty("idac.datasource.url"));
		logger.info(">>>Database jdbc.user=" + env.getProperty("idac.datasource.username"));

		// set database connection props

		dataSource.setJdbcUrl(env.getProperty("idac.datasource.url"));
		dataSource.setUser(env.getProperty("idac.datasource.username"));
		dataSource.setPassword(env.getProperty("idac.datasource.password"));

		// set connection pool props

		dataSource.setInitialPoolSize(getIntProperty("idac.connection.pool.initialPoolSize"));

		dataSource.setMinPoolSize(getIntProperty("idac.connection.pool.minPoolSize"));

		dataSource.setMaxPoolSize(getIntProperty("idac.connection.pool.maxPoolSize"));

		dataSource.setMaxIdleTime(getIntProperty("idac.connection.pool.maxIdleTime"));

		return dataSource;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter(){
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(getBoolProperty("idac.jpa.show-sql"));
		adapter.setGenerateDdl(true);

		adapter.setDatabasePlatform(env.getProperty("idac.jpa.properties.hibernate.dialect"));

		return adapter;

	}

	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		factory.setPackagesToScan("br.com.va4e.idac");
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

	// need a helper method
	// read environment property and convert to int

	private int getIntProperty(String propName) {

		String propVal = env.getProperty(propName);

		// now convert to int
		int intPropVal = Integer.parseInt(propVal);

		return intPropVal;
	}
	
	private boolean getBoolProperty(String propName) {

		String propVal = env.getProperty(propName);
		
		if (propVal=="true") {
			return true;
		}else {
			return false;
		}

	}
}

