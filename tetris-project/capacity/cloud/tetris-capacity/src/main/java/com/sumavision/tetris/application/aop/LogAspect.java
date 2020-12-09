package com.sumavision.tetris.application.aop;/**
 * Created by Poemafar on 2020/12/4 14:52
 */

import com.sumavision.tetris.application.annotation.OprLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

/**
 * @ClassName: LogAspect
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/4 14:52
 */
@Aspect
@Component
@Order(200) //order小的先执行
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.sumavision.tetris.application.annotation.OprLog)")
    public void pointcut(){}

    /**
     * @MethodName: before
     * @Description: 前置通知
     * @param point 1
     * @Return: void
     * @Author: Poemafar
     * @Date: 2020/12/7 8:40
     **/
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Long beginTime = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();//操作标识

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        OprLog logAnnotation = method.getAnnotation(OprLog.class);

        String className = point.getTarget().getClass().getName();
        String methodName = method.getName();
        //方法参数
        Object[] args = point.getArgs();
        //方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        String params = "";
        if (args != null && paramNames != null) {
            for (int i = 0; i < args.length; i++) {
                params+="$" + paramNames[i] + ": " + args[i];
            }
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String requestPath = request.getServletPath();
        String callAddr = request.getRemoteAddr()+":"+request.getRemotePort();
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(logAnnotation.name()).append("]")
                .append("<").append(method.getName().toLowerCase(Locale.ENGLISH)).append(">")
                .append("(syn@").append(uuid).append(") ")
                .append("from ").append(callAddr)
                .append(",PATH: ").append(requestPath)
                .append(",CLASS: ").append(className+"."+methodName)
                .append(". ").append(params);
        LOG.info("{}",sb.toString());

        // 执行原代码
        Object data = null;
        try {
            data = point.proceed(args);
        } catch (Throwable throwable) {
            StringBuilder exs = new StringBuilder();
            exs.append("[").append(logAnnotation.name()).append("]")
                .append("<").append(method.getName().toLowerCase(Locale.ENGLISH)).append(">")
                .append("(exp@").append(uuid).append(")")
                .append(". #TOTAL: ").append(System.currentTimeMillis()-beginTime).append("ms");
            LOG.info("{}",exs.toString());
            throw throwable;
        }

        StringBuilder sbo = new StringBuilder();
        sbo.append("[").append(logAnnotation.name()).append("]")
                .append("<").append(method.getName().toLowerCase(Locale.ENGLISH)).append(">")
                .append("(ack@").append(uuid).append(")")
                .append(". #RESULT: "+ data)
                .append(". #TOTAL: ").append(System.currentTimeMillis()-beginTime).append("ms");
        LOG.info("{}",sbo.toString());

        return data;
    }

//    @AfterReturning(value = "pointcut()",returning = "result")
//    public void afterReturning(JoinPoint point,Object result){
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        Method method = signature.getMethod();
//        OprLog logAnnotation = method.getAnnotation(OprLog.class);
//        StringBuilder sb = new StringBuilder();
//        sb.append("[").append(logAnnotation.name()).append("]")
//                .append("<").append(method.getName().toLowerCase(Locale.ENGLISH)).append(">")
//                .append("(ack@").append(uuid).append(") ")
//                .append(". RESULT: "+ result);
//        LOG.info("{}",sb.toString());
//
//    }

//    @AfterThrowing(value = "pointcut()",throwing = "ex")
//    public void afterThrowing(JoinPoint point,Exception ex){
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        Method method = signature.getMethod();
//        OprLog logAnnotation = method.getAnnotation(OprLog.class);
//        StringBuilder sb = new StringBuilder();
//        sb.append("[").append(logAnnotation.name()).append("]")
//                .append("<").append(method.getName().toLowerCase(Locale.ENGLISH)).append(">")
//                .append("(ack@").append(uuid).append(") ")
//                .append(". ERROR: "+ ex);
//        LOG.info("{}",sb.toString());
//    }

}
