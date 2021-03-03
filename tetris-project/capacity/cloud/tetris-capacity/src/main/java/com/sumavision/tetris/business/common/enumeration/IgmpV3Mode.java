package com.sumavision.tetris.business.common.enumeration;/**
 * Created by Poemafar on 2021/3/3 13:47
 */

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

import java.util.Locale;

/**
 * @ClassName: IgmpV3Mode
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/3/3 13:47
 */
public enum  IgmpV3Mode {
    INCLUDE,EXCLUDE;

    public static IgmpV3Mode getIgmpV3Mode(String mode) throws BaseException {
        String modeEnum = mode.toUpperCase(Locale.ENGLISH);
        switch (modeEnum) {
            case "INCLUDE":
                return INCLUDE;
            case "EXCLUDE":
                return EXCLUDE;
        }
        throw new BaseException(StatusCode.FORBIDDEN,"not support igmp mode :"+mode);
    }
}
