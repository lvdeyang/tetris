package com.sumavision.signal.bvc.entity.po;/**
 * Created by Poemafar on 2020/9/27 16:29
 */

import com.sumavision.signal.bvc.common.enumeration.CommonConstants.*;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SourcePO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/27 16:29
 */
@Entity
@Table(name = "TETRIS_SCL_SOURCE")
public class SourcePO extends AbstractBasePO {


    private ProtocolType protocolType;

    private String url;

    private String srtMode;

    private List<ProgramPO> programPOs = new ArrayList<ProgramPO>();

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSrtMode() {
        return srtMode;
    }

    public void setSrtMode(String srtMode) {
        this.srtMode = srtMode;
    }

    @OneToMany(fetch= FetchType.EAGER , cascade= CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="tetrisSclSourceId")
    public List<ProgramPO> getProgramPOs() {
        return programPOs;
    }

    public void setProgramPOs(List<ProgramPO> programPOs) {
        this.programPOs = programPOs;
    }

}
