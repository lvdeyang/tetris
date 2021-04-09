package com.sumavision.tetris.bvc.listener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;

import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class HibernateUtil {

	//解决新线程懒加载报错问题
	public static boolean bindSessionForThread(EntityManagerFactory entityManagerFactory){
        boolean participate = false;  
        if (TransactionSynchronizationManager.hasResource(entityManagerFactory)) {  
        	participate = true;  
        } else {  
        	EntityManager entityManager = entityManagerFactory.createEntityManager();
        	entityManager.setFlushMode(FlushModeType.COMMIT);
        	EntityManagerHolder entityManagerHolder = new EntityManagerHolder(entityManager);
            TransactionSynchronizationManager.bindResource(entityManagerFactory, entityManagerHolder);  
        }
        return participate;
	}
	
	//关闭新线程中的session
	public static void closeSession(boolean participate, EntityManagerFactory entityManagerFactory){
		if (!participate) {  
			EntityManagerHolder entityManagerHolder = (EntityManagerHolder)TransactionSynchronizationManager.unbindResource(entityManagerFactory); 
			EntityManagerFactoryUtils.closeEntityManager(entityManagerHolder.getEntityManager());
	    }  
	}
}
