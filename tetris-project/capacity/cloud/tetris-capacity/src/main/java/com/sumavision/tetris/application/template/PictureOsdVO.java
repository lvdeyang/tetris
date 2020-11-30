package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/11/5 16:58
 */

/**
 * @ClassName: PictureOsdVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/5 16:58
 */
public class PictureOsdVO {
    private Integer x ;

    private Integer y ;

    private Integer width;

    private Integer height;

    private Boolean auto_scale ;

    private Integer transparent;

    private String path;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getAuto_scale() {
        return auto_scale;
    }

    public void setAuto_scale(Boolean auto_scale) {
        this.auto_scale = auto_scale;
    }

    public Integer getTransparent() {
        return transparent;
    }

    public void setTransparent(Integer transparent) {
        this.transparent = transparent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
