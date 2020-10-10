package com.sumavision.tetris.business.common.po;/**
 * Created by Poemafar on 2020/9/25 16:26
 */

import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @ClassName: TemplatePO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/25 16:26
 */
@Entity
@Table(name = "TETRIS_CAPACITY_TEMPLATE")
public class TemplatePO extends AbstractBasePO {

    /**
     * 模板名
     */
    public String name;


    /**
     * 模板类型
     */
    public String type;

    /**
     * 模板内容
     */
    public String body;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(columnDefinition = "longtext")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
