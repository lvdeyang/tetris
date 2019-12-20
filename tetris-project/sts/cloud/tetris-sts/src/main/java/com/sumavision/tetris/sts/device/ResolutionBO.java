package com.sumavision.tetris.sts.device;


public class ResolutionBO {

    private Integer width;

    private Integer height;

    public ResolutionBO(Integer width, Integer height){
        setWidth(width);
        setHeight(height);
        generate();
    }

    private void generate(){
        Integer num = width * height;
        if(num <= 720*576){
            width = 720;
            height = 576;
        }else if(num <= 1920*1080){
            width = 1920;
            height = 1080;
        }else{
            width = 3840;
            height = 2160;
        }
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
}
