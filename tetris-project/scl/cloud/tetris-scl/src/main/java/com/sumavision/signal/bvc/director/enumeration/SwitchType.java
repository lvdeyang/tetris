package com.sumavision.signal.bvc.director.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum SwitchType {
    CUT("直接切换"),
    FRAME("按帧切换"),
    TRANSCODE("转码切换");

    private String name;

    private SwitchType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @Title:
     * @param name 名称
     * @throws Exception
     * @return SwitchType
     */
    public static final SwitchType fromName(String name) throws Exception{
        SwitchType[] values = SwitchType.values();
        for(SwitchType value:values){
            if(value.getName().equals(name)){
                return value;
            }
        }
        throw new ErrorTypeException("name", name);
    }
}
