package com.sumavision.tetris.application.annotation;/**
 * Created by Poemafar on 2020/12/4 14:45
 */

import java.lang.annotation.*;

/**
 * @ClassName: OprLog
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/4 14:45
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface OprLog {
    String value() default "";

    /**
     * 业务名
     * @return
     */
    String name() default "";
}
