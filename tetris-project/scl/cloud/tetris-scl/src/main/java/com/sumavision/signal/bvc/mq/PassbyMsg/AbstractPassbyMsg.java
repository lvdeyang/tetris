package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/2 16:57
 */

import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: AbstractPassbyMsg
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 16:57
 */
public abstract class AbstractPassbyMsg {

    public  abstract void exec(PassbyBO passby) throws Exception;
}
