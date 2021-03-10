package com.sumavision.tetris.business.common.controller.feign;/**
 * Created by Poemafar on 2021/3/1 15:58
 */

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.annotation.OprLog;
import com.sumavision.tetris.business.common.ResultCode;
import com.sumavision.tetris.business.common.ResultVO;
import com.sumavision.tetris.business.common.TransformModule;
import com.sumavision.tetris.business.common.vo.RefreshSourceDTO;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TransformModuleController
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/3/1 15:58
 */
@RestController
@RequestMapping(value = "/feign/transform/")
public class TransformModuleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformModuleController.class);


    @Autowired
    CapacityService capacityService;

    /**
     * 获取转换模块授权
     * @param transform
     * @return
     * @throws Exception
     */
    @OprLog
    @JsonBody
    @RequestMapping(value = "/license/get")
    public Object getLicense(String transform)throws Exception{
        TransformModule transformModule = JSONObject.parseObject(transform, TransformModule.class);
        JSONObject license = null;
        try {
            license = capacityService.getLicenseAddMsgId(transformModule);
        } catch (Exception e) {
            LOGGER.error("get license fail",e);
            return new ResultVO(ResultCode.FAIL,e.getMessage());
        }
        return new ResultVO(ResultCode.SUCCESS,license);
    }
}
