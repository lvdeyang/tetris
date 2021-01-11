package com.sumavision.tetris.application.config;
/**
 * Created by Poemafar on 2020/9/25 16:26
 */

import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: SystemPO
 * @Description TODO 系统设置
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/25 16:26
 */
@Entity
@Table(name = "TETRIS_CAPACITY_SYSTEM")
public class SystemPO extends AbstractBasePO {


}
