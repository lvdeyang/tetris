package com.suma.venus.resource.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * 资源层数据源详细配置
 * @author lxw
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef="resourceEntityManagerFactory",//实体管理引用
						transactionManagerRef="resourceTransactionManager",//事务管理引用
						basePackages ={"com.suma.venus.resource.dao"})//扫描包
public class ResourceDataSourceConfig {
	
	@Autowired
	@Qualifier("resourceDataSource")
	private DataSource resourceDataSource;
	
	@Autowired
	private JpaProperties jpaProperties;
	
    //配置EntityManager工厂实体
    @Bean(name = "resourceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryResource(EntityManagerFactoryBuilder builder) {
    	Map<String, String> properties = getVendorProperties(resourceDataSource);
		//properties.put("hibernate.autoReconnect", "true");
    	return builder
                .dataSource(resourceDataSource)
                .properties(properties)
                .packages("com.suma.venus.resource") //设置应用creditDataSource的基础包名
                .persistenceUnit("resourcePersistenceUnit")
                .build();
    }
    
    //资源层数据源
    @Bean(name = "resourceDataSource")//装配该方法返回值为userDataSource管理bean
    @Qualifier("resourceDataSource")//spring装配bean唯一标识
    @ConfigurationProperties(prefix="spring.datasource.resource")//application.yml文件内配置数据源的前缀
    public DataSource resourceDataSource(){
    	return DruidDataSourceBuilder.create().build();
    }
	
    //配置EntityManager实体
    @Bean(name = "resourceEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryResource(builder).getObject().createEntityManager();
    }
    
    //配置事务
    @Bean(name = "resourceTransactionManager")
    public PlatformTransactionManager resourceTransactionManager(EntityManagerFactoryBuilder builder) {
    	JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactoryResource(builder).getObject());
    	transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }
    
    //获取jpa配置信息
    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
}
