package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/2 16:56
 */

import com.sumavision.signal.bvc.common.SpringBeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PassbyMsgFactory
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 16:56
 */
public class PassbyMsgFactory {

    Map<String, AbstractPassbyMsg> map = new HashMap<>();

    public PassbyMsgFactory(){
        map.put("CreateTask", SpringBeanFactory.getBean(CreateTask.class)  );
        map.put("create_repeater_node",SpringBeanFactory.getBean(CreateRepeaterNode.class)  );
        map.put("remove_repeater_node", SpringBeanFactory.getBean(RemoveRepeaterNode.class));
        map.put("creatInputSource", SpringBeanFactory.getBean(CreateInputSource.class));
        map.put("deleteInputSource", SpringBeanFactory.getBean(DeleteInputSource.class));
        map.put("creatBackupSources",SpringBeanFactory.getBean(CreateBackupSource.class));
        map.put("deleteAllBackupSources", SpringBeanFactory.getBean(DeleteAllBackupSources.class));
        map.put("switchSource", SpringBeanFactory.getBean(SwitchSource.class));
    }

    public AbstractPassbyMsg getPassbyMsg(String msgType){
        AbstractPassbyMsg msgHandler = map.get(msgType);
        if(msgHandler != null) {
            return msgHandler;
        }
        throw new IllegalArgumentException("No such msg " + msgType.toUpperCase());

    }

}
