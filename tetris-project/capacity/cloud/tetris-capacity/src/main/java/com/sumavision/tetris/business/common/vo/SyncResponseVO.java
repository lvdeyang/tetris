package com.sumavision.tetris.business.common.vo;/**
 * Created by Poemafar on 2020/11/26 9:56
 */

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SyncResponseVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/26 9:56
 */
public class SyncResponseVO {

    /**
     * 缺失的任务
     */
    public List<String> lessJobIds=new ArrayList<>();

    /**
     * 多余的任务
     */
    public List<String> moreJobIds=new ArrayList<>();

    public List<String> getLessJobIds() {
        return lessJobIds;
    }

    public void setLessJobIds(List<String> lessJobIds) {
        this.lessJobIds = lessJobIds;
    }

    public List<String> getMoreJobIds() {
        return moreJobIds;
    }

    public void setMoreJobIds(List<String> moreJobIds) {
        this.moreJobIds = moreJobIds;
    }
}
