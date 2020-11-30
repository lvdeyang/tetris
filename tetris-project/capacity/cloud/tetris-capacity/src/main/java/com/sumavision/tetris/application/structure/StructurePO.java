package com.sumavision.tetris.application.structure;/**
 * Created by Poemafar on 2020/9/25 16:26
 */

import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.*;

/**
 * @ClassName: StructurePO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/25 16:26
 */
@Entity
@Table(name = "TETRIS_CAPACITY_STRUCTURE")
public class StructurePO extends AbstractBasePO {

    enum StructureType{
        INPUT,      //输入
        ENCODE,     //编码
        PREPROCESS, //预处理
        OUTPUT      //输出模板
    }

    /**
     * 所属节点类型
     */
    private StructureType structureType;

    /**
     * 结构信息
     */
    private String body;




}
