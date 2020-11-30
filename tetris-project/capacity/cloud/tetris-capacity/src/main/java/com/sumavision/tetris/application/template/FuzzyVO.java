package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/11/5 17:05
 */

/**
 * @ClassName: FuzzyVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/5 17:05
 */
public class FuzzyVO {
    private String position;

    private String zone;

    /** 模糊效果 fuzzy/mosaic */
    private String fuzzy_effect;

    private Integer mosaic_radius;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getFuzzy_effect() {
        return fuzzy_effect;
    }

    public void setFuzzy_effect(String fuzzy_effect) {
        this.fuzzy_effect = fuzzy_effect;
    }

    public Integer getMosaic_radius() {
        return mosaic_radius;
    }

    public void setMosaic_radius(Integer mosaic_radius) {
        this.mosaic_radius = mosaic_radius;
    }
}
