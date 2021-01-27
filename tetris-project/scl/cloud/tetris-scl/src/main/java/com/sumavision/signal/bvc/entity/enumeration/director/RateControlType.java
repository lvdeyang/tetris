package com.sumavision.signal.bvc.entity.enumeration.director;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum RateControlType {
    CBR("CBR"),
    VBR("VBR");

    private String name;

    private RateControlType(String name){
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
    public static final RateControlType fromName(String name) throws Exception{
        RateControlType[] values = RateControlType.values();
        for(RateControlType value:values){
            if(value.getName().equals(name)){
                return value;
            }
        }
        throw new ErrorTypeException("name", name);
    }
}
