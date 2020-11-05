package com.sumavision.bvc.config;

import java.sql.SQLException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * 主数据源详细配置
 * @author lxw
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef="baseEntityManagerFactory",//实体管理引用
						transactionManagerRef="baseTransactionManager",//事务管理引用
						basePackages ={"com.sumavision.bvc", "com.sumavision.tetris", "com.suma.venus"})//扫描包
public class BaseDataSourceConfig {
	
	@Autowired
	@Qualifier("baseDataSource")
	private DataSource baseDataSource;
	
	@Autowired
	private JpaProperties jpaProperties;
	
    //自身数据库
    @Primary
    @Bean(name = "baseDataSource")//装配该方法返回值为userDataSource管理bean
    @Qualifier("baseDataSource")//spring装配bean唯一标识
    @ConfigurationProperties(prefix="spring.datasource.base")//application.yml文件内配置数据源的前缀
    public DataSource baseDataSource(){
    	return DruidDataSourceBuilder.create().build();
    }
	
    //配置EntityManager工厂实体
	@Primary
    @Bean(name = "baseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean baseEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(baseDataSource)
                .properties(getVendorProperties(baseDataSource))
                .packages("com.sumavision.bvc", "com.sumavision.tetris", "com.suma.venus") //设置应用DataSource的基础包名
                .persistenceUnit("basePersistenceUnit")
                .build();
    }
	
    //配置EntityManager实体
    @Primary
    @Bean(name = "baseEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return baseEntityManagerFactory(builder).getObject().createEntityManager();
    }
    
    //配置事务
    @Primary
    @Bean(name = "baseTransactionManager")
    public PlatformTransactionManager baseTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(baseEntityManagerFactory(builder).getObject());
    }
    
    //获取jpa配置信息
    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
}
