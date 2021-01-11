package com.sumavision.tetris.application.preview;
/**
 * Created by Poemafar on 2020/9/25 16:26
 */

import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: StructurePO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/25 16:26
 */
@Entity
@Table(name = "TETRIS_CAPACITY_PREVIEW")
public class PreviewPO extends AbstractBasePO {

    private String transferTaskId;

    private String previewTaskId;

    private String inputId; //哪个输入做的预览

    public String getTransferTaskId() {
        return transferTaskId;
    }

    public PreviewPO setTransferTaskId(String transferTaskId) {
        this.transferTaskId = transferTaskId;
        return this;
    }

    public String getPreviewTaskId() {
        return previewTaskId;
    }

    public PreviewPO setPreviewTaskId(String previewTaskId) {
        this.previewTaskId = previewTaskId;
        return this;
    }

    public String getInputId() {
        return inputId;
    }

    public PreviewPO setInputId(String inputId) {
        this.inputId = inputId;
        return this;
    }
}
