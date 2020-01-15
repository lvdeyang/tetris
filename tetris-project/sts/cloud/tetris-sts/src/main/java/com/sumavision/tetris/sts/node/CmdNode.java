package com.sumavision.tetris.sts.node;

import javax.xml.bind.annotation.XmlElement;

import org.springframework.transaction.annotation.Transactional;


/**
 * 命令节点抽象类，所有命令节点继承此对象
 * @author gaofeng
 * @date 2017年1月22日
 */
public abstract class CmdNode{
    
    
    
//    /**
//     * 命令执行失败是否需要数据库回滚的标志
//     * 需要数据库同步的命令：create-*
//     * 不需要数据库同步的命令：update-*,delete-*
//     */
//    public enum CmdRollBackType{
//    	ROLLBACK,NOROLLBACK
//    }
//    
//    private CmdRollBackType rollBackType;
	
	public abstract void updateDatabase();
	public abstract XmlElement getMsg();
}
