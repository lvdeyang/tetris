package com.sumavision.tetris.business.common.controller.feign;/**
 * Created by Poemafar on 2021/2/1 11:46
 */

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.annotation.OprLog;
import com.sumavision.tetris.business.common.service.SourceService;
import com.sumavision.tetris.business.common.vo.RefreshSourceDTO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TaskController
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/1 11:46
 */
@RestController
@RequestMapping(value = "/feign/source/")
public class SourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SourceController.class);

    @Autowired
    SourceService sourceService;

    @OprLog
    @JsonBody
    @RequestMapping(value = "/refresh")
    public Object refreshSource(String source)throws Exception{
        RefreshSourceDTO refreshSourceDTO = JSONObject.parseObject(source, RefreshSourceDTO.class);
        return sourceService.refreshSource(refreshSourceDTO);
    }


}
