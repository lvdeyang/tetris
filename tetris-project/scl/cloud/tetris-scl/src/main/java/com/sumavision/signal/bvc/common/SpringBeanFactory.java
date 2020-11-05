package com.sumavision.signal.bvc.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanFactory implements ApplicationContextAware{

    private static ApplicationContext ctx;
    
    public static ApplicationContext getCtx() {
        return ctx;
    }

    public static void setCtx(ApplicationContext ctx) {
        SpringBeanFactory.ctx = ctx;
    }

    public static <T>T getBean(Class<T> paramClass){
        return getCtx().getBean(paramClass);
    }
    
    public static <T>T getBean(Class<T> paramClass,Object agr1){
    	return getCtx().getBean(paramClass,agr1);
    }
    
    public static Object getBeanByName(String name){
        return getCtx().getBean(name);
    }
    
    public static <T>T getBean(Class<T> paramClass,Object agr1,Object arg2){
    	return getCtx().getBean(paramClass,agr1,arg2);
    }
    
    public static <T>T getBean(Class<T> paramClass,Object... args){
    	return getCtx().getBean(paramClass, args);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        // TODO Auto-generated method stub
        setCtx(ctx);
    }

}
