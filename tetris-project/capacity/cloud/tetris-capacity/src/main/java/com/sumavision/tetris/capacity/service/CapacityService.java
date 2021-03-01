package com.sumavision.tetris.capacity.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sumavision.tetris.business.common.TransformModule;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.request.*;
import com.sumavision.tetris.capacity.bo.response.*;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.constant.UrlConstant;
import com.sumavision.tetris.capacity.enumeration.InputResponseEnum;
import com.sumavision.tetris.capacity.enumeration.OutputResponseEnum;
import com.sumavision.tetris.capacity.enumeration.TaskResponseEnum;
import com.sumavision.tetris.capacity.exception.HttpTimeoutException;
import com.sumavision.tetris.capacity.exception.InputResponseErrorException;
import com.sumavision.tetris.capacity.exception.OutputResponseErrorException;
import com.sumavision.tetris.capacity.exception.TaskResponseErrorException;
import com.sumavision.tetris.capacity.util.http.HttpUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CapacityService {

    private static final Logger LOG = LoggerFactory.getLogger(CapacityService.class);

    @Autowired
    private CapacityProps capacityProps;

    /**
     * 获取输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:41:19
     *
     * @return GetInputsResponse
     */
    public GetInputsResponse getInputs(TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        return getInputs(msg_id, transformModule);
    }

    /**
     * 获取输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月4日 上午11:26:59
     *
     * @param msg_id 消息id
     * @return GetInputsRespBO 输入信息
     */
    private GetInputsResponse getInputs(String msg_id, TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[get-inputs] request, url: {}", url);
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[get-inputs] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());
        GetInputsResponse response = JSONObject.parseObject(res.toJSONString(), GetInputsResponse.class);

        return response;

    }

    /**
     * 创建all,加msg_id<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月28日 下午2:39:30
     *
     * @param AllRequest all
     * @return AllResponse
     */
    public AllResponse createAllAddMsgId(AllRequest all, TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        all.setMsg_id(msg_id);

        return createAll(all, transformModule);
    }

    /**
     * 创建all<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月28日 下午2:39:30
     *
     * @return AllResponse
     */
    private AllResponse createAll(AllRequest all, TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION)
                .append("/")
                .append(UrlConstant.URL_COMBINE)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(all, SerializerFeature.DisableCircularReferenceDetect));
        LOG.info("[create-all] request, url: {},body: {}", url, request);
        JSONObject res = HttpUtil.httpPost(url, request);
        LOG.info("[create-all] response, result: {}", res);
        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        AllResponse response = JSONObject.parseObject(res.toJSONString(), AllResponse.class);

        return response;

    }

    public JSONObject sendCommandsByQueue(JSONObject request, TransformModule transformModule) throws Exception {
        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION)
                .append("/")
                .append(UrlConstant.URL_QUEUE)
                .toString();
        LOG.info("[command-queue] request, url: {},body: {}", url, request);
        JSONObject res = HttpUtil.httpPost(url, request);
        LOG.info("[command-queue] response, result: {}", res);
        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        if (!CommonUtil.findAllEqualByKey(res,"result_code","0") || !CommonUtil.findAllEqualByKey(res,"http_code","200")) {
            throw new BaseException(StatusCode.ERROR,"存在指令操作失败");
        }
        return res;
    }

    public void deleteAllIgnoreTransError(AllRequest all,TransformModule transformModule) {
        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        all.setMsg_id(msg_id);
        try {
            deleteAll(all,transformModule);
        } catch (Exception e) {
            LOG.info("force delete ignore error", e);
        }
    }

    /**
     * 删除all加msg_id<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月28日 下午2:47:06
     *
     * @param AllRequest all
     */
    public AllResponse deleteAllAddMsgId(AllRequest all,TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        all.setMsg_id(msg_id);

        return deleteAll(all,transformModule); //此处捕获异常
    }

    /**
     * 删除all<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月28日 下午3:20:44
     * @param AllRequest all
     */
    private AllResponse deleteAll(AllRequest all, TransformModule transformModule) throws Exception {
        String socketAddress = transformModule.getSocketAddress();
        String url = new StringBufferWrapper().append(socketAddress)
                .append(UrlConstant.URL_VERSION)
                .append("/")
                .append(UrlConstant.URL_COMBINE)
                .toString();
        JSONObject request = JSONObject.parseObject(JSON.toJSONString(all));

        LOG.info("[delete-all] request, url: {}, all: {}", url, request);

        JSONObject res = HttpUtil.httpDelete(url, request);
        if (res == null) throw new HttpTimeoutException(socketAddress);
        LOG.info("[delete-all] response, result: {}", res);
        AllResponse response = JSONObject.parseObject(res.toJSONString(), AllResponse.class);
        return response;
    }

    /**
     * 创建输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:43:40
     *
     * @param CreateInputsRequest input 不带msg_id的input
     * @return CreateInputsResponse
     */
    public CreateInputsResponse createInputsWithMsgId(CreateInputsRequest input,TransformModule transformModule) throws Exception {
        if (StringUtils.isBlank(input.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            input.setMsg_id(msg_id);
        }
        return createInputsToTransform(input, transformModule);

    }

    /**
     * 创建输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:43:40
     *
     * @param CreateInputsRequest input 不带msg_id的input
     * @return CreateInputsResponse
     */
    public CreateInputsResponse createInputs(CreateInputsRequest input,TransformModule transformModule) throws Exception {
        return createInputsToTransform(input,transformModule);
    }

    /**
     * 创建输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月4日 下午7:34:04
     *
     * @param CreateInputsRequest input 输入参数
     * @return CreateInputsResponse 创建输入返回
     */
    private CreateInputsResponse createInputsToTransform(CreateInputsRequest input,TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(input, SerializerFeature.DisableCircularReferenceDetect));
        LOG.info("[create-inputs] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPost(url, request);
        LOG.info("[create-inputs] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        CreateInputsResponse response = JSONObject.parseObject(res.toJSONString(), CreateInputsResponse.class);

        return response;

    }

    /**
     * 删除输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:46:11
     *
     * @param DeleteInputsRequest input 不带msg_id的input
     */
    public void deleteInputsAddMsgId(DeleteInputsRequest input, TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        input.setMsg_id(msg_id);

        deleteInputsToTransform(input,transformModule);

    }

    /**
     * 删除输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:46:11
     *
     * @param DeleteInputsRequest input 不带msg_id的input
     */
    public void deleteInputs(DeleteInputsRequest input,TransformModule transformModule) throws Exception {
        deleteInputsToTransform(input,transformModule);
    }


    /**
     * 删除输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 上午9:51:20
     *
     * @param DeleteInputsRequest input 删除输入请求
     */
    private void deleteInputsToTransform(DeleteInputsRequest input,TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(input));
        LOG.info("[delete-inputs] request, url: {}, body: {}", url, request);
        JSONObject result = HttpUtil.httpDelete(url, request);
        LOG.info("[delete-inputs] response, result: {}", result);
    }

    /**
     * 修改输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:49:16
     *
     * @param String           inputId
     * @param PutInputsRequest input 不带msg_id的input
     * @return PutInputResponse
     */
    public PutInputResponse modifyInputsAddMsgId(String inputId, PutInputsRequest input,TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        input.setMsg_id(msg_id);

        return modifyInputsToTransform(inputId, input,transformModule);

    }

    /**
     * 修改输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:49:16
     *
     * @param String           inputId
     * @param PutInputsRequest input 不带msg_id的input
     * @return PutInputResponse
     */
    public PutInputResponse modifyInputs(PutInputsRequest input,TransformModule transformModule) throws Exception {
        return modifyInputsToTransform(input.getInput().getId(), input,transformModule);
    }

    /**
     * 修改输入<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 上午10:37:27
     *
     * @param String           inputId 输入id
     * @param PutInputsRequest input 修改输入参数
     * @return PutInputResponse 修改输入返回参数
     */
    private PutInputResponse modifyInputsToTransform(String inputId, PutInputsRequest input,TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT)
                .append("/")
                .append(inputId).append("/")
                .append(UrlConstant.URL_INPUT_PARAM)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(input.getInput()));
        request.put("msg_id", input.getMsg_id());
//		JSONObject request = JSONObject.parseObject(JSON.toJSONString(input));
        LOG.info("[modify-inputs] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[modify-inputs] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        PutInputResponse response = JSONObject.parseObject(res.toJSONString(), PutInputResponse.class);
        if (response.getResult_code() != null && !response.getResult_code().equals(InputResponseEnum.SUCCESS.getCode())) {
            throw new InputResponseErrorException(response.getResult_msg());
        }
        return response;

    }


    public void modifyProgramParamToTransform(PutElementsRequest putElementsRequest,TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT)
                .append("/")
                .append(putElementsRequest.getInput_id()).append("/")
                .append(UrlConstant.URL_INPUT_PROGRAM).append("/")
                .append(putElementsRequest.getProgram_num()).append("/")
                .append(UrlConstant.URL_INPUT_PARAM)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(putElementsRequest));

        LOG.info("[modify-elements] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[modify-elements] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());


    }


    /**
     * 创建节目<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:51:37
     *
     * @param String                inputId
     * @param CreateProgramsRequest program 不带msg_id的program
     * @return CreateProgramResponse
     */
    public CreateProgramResponse createProgramAddMsgId(TransformModule transformModule, String inputId, CreateProgramsRequest program) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        program.setMsg_id(msg_id);

        return createProgramToTransform(transformModule, inputId, program);

    }

    /**
     * 创建节目<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:51:37
     *
     * @param String                inputId
     * @param CreateProgramsRequest program 不带msg_id的program
     * @return CreateProgramResponse
     */
    public CreateProgramResponse createProgram(TransformModule transformModule, CreateProgramsRequest program) throws Exception {
        return createProgramToTransform(transformModule, program.getInput_id(), program);
    }


    /**
     * 创建节目<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 上午11:17:34
     *
     * @param String                inputId 输入id
     * @param CreateProgramsRequest program 节目参数
     * @return CreateProgramResponse CreateProgramResponse 创建节目返回
     */
    private CreateProgramResponse createProgramToTransform(TransformModule transformModule, String inputId, CreateProgramsRequest program) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT).append("/")
                .append(inputId).append("/")
                .append(UrlConstant.URL_INPUT_PROGRAM)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(program, SerializerFeature.DisableCircularReferenceDetect));

        LOG.info("[create-program] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPost(url, request);
        LOG.info("[create-program] response, result: {}", res);
        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        CreateProgramResponse response = JSONObject.parseObject(res.toJSONString(), CreateProgramResponse.class);

        return response;

    }

    /**
     * 删除节目<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:54:29
     *
     * @param String               inputId
     * @param DeleteProgramRequest program 不带msg_id的program
     */
    public void deleteProgramAddMsgId(TransformModule transformModule, String inputId, DeleteProgramRequest program) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        program.setMsg_id(msg_id);

        deleteProgram(transformModule, inputId, program);

    }

    /**
     * 删除节目<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:54:29
     *
     * @param String               inputId
     * @param DeleteProgramRequest program 不带msg_id的program
     */
    public void deleteProgram(TransformModule transformModule, DeleteProgramRequest program) throws Exception {
        deleteProgram(transformModule, program.getInput_id(), program);

    }

    /**
     * 删除节目<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 上午11:34:35
     *
     * @param String               inputId 输入id
     * @param DeleteProgramRequest program 删除节目参数
     */
    private void deleteProgram(TransformModule transformModule, String inputId, DeleteProgramRequest program) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT).append("/")
                .append(inputId).append("/")
                .append(UrlConstant.URL_INPUT_PROGRAM)
                .toString();
        JSONObject request = JSONObject.parseObject(JSON.toJSONString(program));

        LOG.info("[delete-program] request, url: {}, body: {}", url, request);
        JSONObject result = HttpUtil.httpDelete(url, request);
        LOG.info("[delete-program] response, result: {}", result);

    }

    /**
     * 修改指定节目解码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:59:40
     *
     * @param String             inputId
     * @param String             programId
     * @param String             pid
     * @param PatchDecodeRequest decode
     * @return PutInputResponse
     */
    public PutInputResponse modifyProgramDecodeAddMsgId(
            TransformModule transformModule,
            String inputId,
            String programId,
            String pid,
            PatchDecodeRequest decode) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        decode.setMsg_id(msg_id);

        return modifyProgramDecode(transformModule, inputId, programId, pid, decode);

    }

    /**
     * 修改指定节目解码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:59:40
     *
     * @param String             inputId
     * @param String             programId
     * @param String             pid
     * @param PatchDecodeRequest decode
     * @return PutInputResponse
     */
    public PutInputResponse modifyProgramDecodeMode(TransformModule transformModule, PatchDecodeRequest decode) throws Exception {
        return modifyProgramDecode(transformModule, decode.getInput_id(), decode.getProgram_num(), decode.getPid(), decode);
    }

    /**
     * 修改指定节目解码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午1:59:40
     *
     * @param String             inputId
     * @param String             programId
     * @param String             pid
     * @param PatchDecodeRequest decode
     * @return PutInputResponse
     */
    private PutInputResponse modifyProgramDecode(
            TransformModule transformModule,
            String inputId,
            String programId,
            String pid,
            PatchDecodeRequest decode) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT).append("/")
                .append(inputId).append("/")
                .append(UrlConstant.URL_INPUT_PROGRAM).append("/")
                .append(programId).append("/")
                .append(UrlConstant.URL_INPUT_PROGRAM_ELEMENTS).append("/")
                .append(pid).append("/")
                .append(UrlConstant.URL_INPUT_PROGRAM_DECODE)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(decode));
        LOG.info("[modify-program] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[modify-program] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        PutInputResponse response = JSONObject.parseObject(res.toJSONString(), PutInputResponse.class);
        if (response.getResult_code() != null && !response.getResult_code().equals(InputResponseEnum.SUCCESS.getCode())) {
            throw new InputResponseErrorException(response.getResult_msg());
        }
        return response;

    }

    /**
     * 获取指定输入源信息<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午2:17:19
     *
     * @param String inputId
     * @return AnalysisResponse
     */
    public AnalysisResponse getAnalysis(String inputId, TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        return getAnalysis(inputId, msg_id, transformModule);

    }

    /**
     * 获取指定输入源信息<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 下午2:26:14
     *
     * @param String msg_id 消息id
     * @return AnalysisResponse 刷源返回
     */
    private AnalysisResponse getAnalysis(String inputId, String msg_id, TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT).append("/")
                .append(inputId).append("/")
                .append(UrlConstant.URL_INPUT_ANALYSIS)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[analysis-input] request, url: {}, body: {}", url, "");
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[analysis-input] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        AnalysisResponse response = JSONObject.parseObject(res.toJSONString(), AnalysisResponse.class);

        return response;

    }

    /**
     * 获取所有任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午2:18:39
     *
     * @return GetTaskResponse
     */
    public GetTaskResponse getTasks(TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        return getTasks(msg_id,transformModule);
    }

    /**
     * 获取所有任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 下午2:54:39
     *
     * @param String msg_id 消息id
     * @return TaskResponse 任务返回
     */
    private GetTaskResponse getTasks(String msg_id,TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[get-tasks] request, url: {}, body: {}", url, "");
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[get-tasks] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        GetTaskResponse response = JSONObject.parseObject(res.toJSONString(), GetTaskResponse.class);

        return response;

    }

    /**
     * 创建任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午2:21:59
     *
     * @param CreateTaskRequest task 不带msg_id的task
     * @return CreateTaskResponse
     */
    public CreateTaskResponse createTasksAddMsgId(CreateTaskRequest task, TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        task.setMsg_id(msg_id);

        System.out.println(JSONObject.toJSONString(task));

        return createTasks(transformModule, task);
    }

    /**
     * 创建任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午2:21:59
     *
     * @param CreateTaskRequest task 不带msg_id的task
     * @return CreateTaskResponse
     */
    public CreateTaskResponse createTasksWithMsgId(CreateTaskRequest task, TransformModule transformModule) throws Exception {
        if (StringUtils.isBlank(task.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            task.setMsg_id(msg_id);
        }
        return createTasks(transformModule, task);
    }

    /**
     * 创建任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 下午3:38:08
     *
     * @param CreateTaskRequest task 创建任务参数
     * @return CreateTaskResponse 创建任务返回
     */
    private CreateTaskResponse createTasks(TransformModule transformModule, CreateTaskRequest task) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(task, SerializerFeature.DisableCircularReferenceDetect));
        LOG.info("[create-tasks] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPost(url, request);
        LOG.info("[create-taks] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        CreateTaskResponse response = JSONObject.parseObject(res.toJSONString(), CreateTaskResponse.class);

        return response;

    }

    /**
     * 删除任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午2:53:36
     *
     * @param DeleteTasksRequest task 不带msg_id的task
     */
    public void deleteTasksAddMsgId(DeleteTasksRequest task, TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        task.setMsg_id(msg_id);

        deleteTasks(task, transformModule);
    }

    /**
     * 删除任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午2:53:36
     *
     * @param DeleteTasksRequest task 不带msg_id的task
     */
    public void deleteTasksWithMsgId(DeleteTasksRequest task, TransformModule  transformModule) throws Exception {

        if (StringUtils.isBlank(task.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            task.setMsg_id(msg_id);
        }

        deleteTasks(task, transformModule);
    }

    /**
     * 删除任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 下午3:56:56
     *
     * @param DeleteTasksRequest task 需要删除的任务
     */
    private void deleteTasks(DeleteTasksRequest task, TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK)
                .toString();
        JSONObject request = JSONObject.parseObject(JSON.toJSONString(task));

        LOG.info("[delete-tasks] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpDelete(url, request);
        LOG.info("[delete-tasks] response, result: {}", res);

    }

    /**
     * 添加任务编码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午3:25:31
     *
     * @param String               taskId
     * @param AddTaskEncodeRequest encode 不带msg_id的encode
     * @return TaskEncodeResponse
     */
    public TaskEncodeResponse addTaskEncodeWithMsgId(TransformModule transformModule, String taskId, AddTaskEncodeRequest encode) throws Exception {

        if (StringUtils.isBlank(encode.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            encode.setMsg_id(msg_id);
        }

        return addTaskEncode(transformModule, taskId, encode);
    }

    /**
     * 添加任务编码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午3:25:31
     *
     * @param String               taskId
     * @param AddTaskEncodeRequest encode 不带msg_id的encode
     * @return TaskEncodeResponse
     */
    public TaskEncodeResponse addTaskEncode(TransformModule transformModule, AddTaskEncodeRequest encode) throws Exception {
        return addTaskEncode(transformModule, encode.getTask_id(), encode);
    }

    /**
     * 添加任务编码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 下午4:28:22
     *
     * @param AddTaskEncodeRequest encode 添加任务编码参数
     * @return TaskEncodeResponse 添加任务编码返回
     */
    private TaskEncodeResponse addTaskEncode(TransformModule transformModule, String taskId, AddTaskEncodeRequest encode) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK).append("/")
                .append(taskId).append("/")
                .append(UrlConstant.URL_TASK_ENCODE)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(encode, SerializerFeature.DisableCircularReferenceDetect));
        LOG.info("[add-encode] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPost(url, request);
        LOG.info("[add-encode] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        TaskEncodeResponse response = JSONObject.parseObject(res.toJSONString(), TaskEncodeResponse.class);

        return response;

    }

    /**
     * 删除任务编码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午3:29:39
     *
     * @param String                   taskId
     * @param DeleteTaskEncodeResponse encode 不带msg_id的encode
     * @return TaskEncodeResponse
     */
    public TaskEncodeResponse deleteTaskEncodeWithMsgId(TransformModule transformModule, String taskId, DeleteTaskEncodeResponse encode) throws Exception {
        if (StringUtils.isBlank(encode.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            encode.setMsg_id(msg_id);
        }
        return deleteTaskEncode(transformModule, taskId, encode);
    }

    /**
     * 删除任务编码<br/>
     * <b>作者:</b>yzx<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午3:29:39
     *
     * @param String                   taskId
     * @param DeleteTaskEncodeResponse encode 不带msg_id的encode
     * @return TaskEncodeResponse
     */
    public TaskEncodeResponse deleteTaskEncode(TransformModule transformModule, DeleteTaskEncodeResponse encode) throws Exception {
        return deleteTaskEncode(transformModule, encode.getTask_id(), encode);
    }

    /**
     * 删除任务编码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 下午4:41:13
     *
     * @param DeleteTaskEncodeResponse encode 删除任务编码请求参数
     * @return TaskEncodeResponse 删除任务编码返回
     */
    private TaskEncodeResponse deleteTaskEncode(TransformModule transformModule, String taskId, DeleteTaskEncodeResponse encode) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK).append("/")
                .append(taskId).append("/")
                .append(UrlConstant.URL_TASK_ENCODE)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(encode));
        LOG.info("[delete-encode] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpDelete(url, request);
        LOG.info("[delete-encode] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        TaskEncodeResponse response = JSONObject.parseObject(res.toJSONString(), TaskEncodeResponse.class);

        return response;

    }

    /**
     * 修改任务编码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午3:41:14
     *
     * @param String               taskId
     * @param String               encodeId
     * @param PutTaskEncodeRequest encode 不带msg_id的encode
     * @return TaskEncodeResultResponse
     */
    public TaskEncodeResultResponse modifyTaskEncodeWithMsgId(TransformModule transformModule, String taskId, String encodeId, PutTaskEncodeRequest encode) throws Exception {

        if (StringUtils.isBlank(encode.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            encode.setMsg_id(msg_id);
        }

        return modifyTaskEncode(transformModule, taskId, encodeId, encode);
    }

    /**
     * 修改任务编码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午3:41:14
     *
     * @param PutTaskEncodeRequest encode
     * @return TaskEncodeResultResponse
     */
    public TaskEncodeResultResponse modifyTaskEncode(TransformModule transformModule, PutTaskEncodeRequest encode) throws Exception {
        return modifyTaskEncode(transformModule, encode.getTask_id(), encode.getEncode_param().getEncode_id(), encode);
    }

    /**
     * 修改任务编码<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月5日 下午6:04:13
     *
     * @param String               taskId 任务id
     * @param String               encodeId 编码id
     * @param PutTaskEncodeRequest encode 修改编码参数
     * @return TaskEncodeResultResponse 返回
     */
    private TaskEncodeResultResponse modifyTaskEncode(TransformModule transformModule, String taskId, String encodeId, PutTaskEncodeRequest encode) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK).append("/")
                .append(taskId).append("/")
                .append(UrlConstant.URL_TASK_ENCODE).append("/")
                .append(encodeId)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(encode));
        LOG.info("[modify-encode] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[modify-encode] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        TaskEncodeResultResponse response = JSONObject.parseObject(res.toJSONString(), TaskEncodeResultResponse.class);
        if (response.getResult_code() != null && !response.getResult_code().equals(TaskResponseEnum.SUCCESS.getCode())) {
            throw new TaskResponseErrorException(response.getResult_msg());
        }

        return response;

    }

    /**
     * 修改解码后处理<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午3:48:50
     *
     * @param String                      taskId
     * @param PutTaskDecodeProcessRequest decode
     * @return TaskResponse
     */
    public TaskBaseResponse modifyTaskDecodeProcessAddMsgId(TransformModule transformModule, String taskId, PutTaskDecodeProcessRequest decode) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        decode.setMsg_id(msg_id);

        return modifyTaskDecodeProcessToTransform(transformModule, taskId, decode);

    }

    /**
     * 修改解码后处理<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午3:48:50
     *
     * @param String                      taskId
     * @param PutTaskDecodeProcessRequest decode
     * @return TaskResponse
     */
    public TaskBaseResponse modifyTaskDecodeProcess(TransformModule transformModule, PutTaskDecodeProcessRequest decode) throws Exception {
        return modifyTaskDecodeProcessToTransform(transformModule, decode.getTask_id(), decode);
    }

    /**
     * 修改解码后处理<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 上午9:26:49
     *
     * @param String                      taskId 任务id
     * @param PutTaskDecodeProcessRequest decode 修改解码后处理参数
     * @return TaskResponse 返回
     */
    private TaskBaseResponse modifyTaskDecodeProcessToTransform(TransformModule transformModule, String taskId, PutTaskDecodeProcessRequest decode) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK).append("/")
                .append(taskId).append("/")
                .append(UrlConstant.URL_TASK_DECODE_PROCESS)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(decode));
        LOG.info("[modify-decode-process] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[modify-decode-process] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        TaskBaseResponse response = JSONObject.parseObject(res.toJSONString(), TaskBaseResponse.class);
        if (response.getResult_code() != null && !response.getResult_code().equals(InputResponseEnum.SUCCESS.getCode())) {
            throw new InputResponseErrorException(response.getResult_msg());
        }

        return response;

    }

    /**
     * 修改任务源<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午4:05:13
     *
     * @param String               taskId
     * @param PutTaskSourceRequest source 不带msg_id的source
     * @return TaskBaseResponse
     */
    public TaskBaseResponse modifyTaskSourceAddMsgId(TransformModule transformModule,String taskId, PutTaskSourceRequest source) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        source.setMsg_id(msg_id);

        return modifyTaskSourceToTransform(transformModule, taskId, source);

    }

    /**
     * 修改任务源<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午4:05:13
     *
     * @param String               taskId
     * @param PutTaskSourceRequest source 不带msg_id的source
     * @return TaskBaseResponse
     */
    public TaskBaseResponse modifyTaskSource(TransformModule transformModule, PutTaskSourceRequest source) throws Exception {
        return modifyTaskSourceToTransform(transformModule, source.getTask_id(), source);
    }

    /**
     * 修改任务源<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 上午10:01:30
     *
     * @param String               taskId 任务id
     * @param PutTaskSourceRequest source 修改任务源参数
     * @return TaskResponse 返回
     */
    private TaskBaseResponse modifyTaskSourceToTransform(TransformModule transformModule, String taskId, PutTaskSourceRequest source) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK).append("/")
                .append(taskId).append("/")
                .append(UrlConstant.URL_TASK_SOURCE)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(source));
        LOG.info("[modify-source] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[modify-source] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        TaskBaseResponse response = JSONObject.parseObject(res.toJSONString(), TaskBaseResponse.class);
        if (response.getResult_code() != null && !response.getResult_code().equals(TaskResponseEnum.SUCCESS.getCode())) {
            throw new TaskResponseErrorException(response.getResult_msg());
        }
        return response;

    }

    /**
     * 源节目切换<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午4:12:22
     *
     * @param String              taskId
     * @param PutRealIndexRequest realIndex 不带msg_id的realIndex
     * @return TaskRealIndexResponse
     */
    public TaskRealIndexResponse switchTaskSourceAddMsgId(TransformModule transformModule, String taskId, PutRealIndexRequest realIndex) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        realIndex.setMsg_id(msg_id);

        return switchTaskSource(transformModule, taskId, realIndex);

    }

    /**
     * 源节目切换<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午4:12:22
     *
     * @param String              taskId
     * @param PutRealIndexRequest realIndex 不带msg_id的realIndex
     * @return TaskRealIndexResponse
     */
    public TaskRealIndexResponse switchTaskSource(TransformModule transformModule, PutRealIndexRequest realIndex) throws Exception {
        return switchTaskSource(transformModule, realIndex.getTask_id(), realIndex);
    }

    /**
     * 源节目切换<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 上午10:32:05
     *
     * @param String              taskId 任务id
     * @param PutRealIndexRequest realIndex 待切换节目索引
     * @return TaskRealIndexResponse 返回
     */
    private TaskRealIndexResponse switchTaskSource(TransformModule transformModule, String taskId, PutRealIndexRequest realIndex) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_TASK).append("/")
                .append(taskId).append("/")
                .append(UrlConstant.URL_TASK_SOURCE_INDEX)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(realIndex));
        LOG.info("[switch-source] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[switch-source] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        TaskRealIndexResponse response = JSONObject.parseObject(res.toJSONString(), TaskRealIndexResponse.class);
        if (response.getResult_code() != null && !response.getResult_code().equals(InputResponseEnum.SUCCESS.getCode())) {
            throw new InputResponseErrorException(response.getResult_msg());
        }
        return response;

    }

    /**
     * 方法概述<br/>
     * <p>详细描述</p>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午4:28:27
     *
     * @return GetOutputsResponse
     */
    public GetOutputsResponse getOutputs(TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        return getOutputs(msg_id,transformModule);

    }

    /**
     * 获取所有输出返回<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 上午11:10:12
     *
     * @param String msg_id 消息id
     * @return GetOutputsResponse 返回
     */
    private GetOutputsResponse getOutputs(String msg_id,TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_OUTPUT)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[get-outputs] request, url: {}, body: {}", url, "");
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[get-outputs] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        GetOutputsResponse response = JSONObject.parseObject(res.toJSONString(), GetOutputsResponse.class);

        return response;

    }

    /**
     * 创建输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月12日 上午8:53:18
     *
     * @param CreateOutputsRequest output 不带msg_id的 输出
     * @return CreateOutputsResponse
     */
    public CreateOutputsResponse createOutputsWithMsgId(CreateOutputsRequest output,TransformModule transformModule) throws Exception {
        if (StringUtils.isBlank(output.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            output.setMsg_id(msg_id);
        }
        return createOutputs(output, transformModule);
    }

    /**
     * 创建输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 上午11:34:36
     *
     * @param CreateOutputsRequest output 创建输出参数
     * @return CreateOutputsResponse 返回
     */
    private CreateOutputsResponse createOutputs(CreateOutputsRequest output, TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_OUTPUT)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(output, SerializerFeature.DisableCircularReferenceDetect));
        LOG.info("[create-outputs] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPost(url, request);
        LOG.info("[create-outputs] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        CreateOutputsResponse response = JSONObject.parseObject(res.toJSONString(), CreateOutputsResponse.class);

        return response;

    }

    /**
     * 删除输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月12日 上午8:55:13
     *
     * @param DeleteOutputsRequest output 不带msg_id的output
     */
    public void deleteOutputsWithMsgId(DeleteOutputsRequest output,TransformModule transformModule) throws Exception {
        if (StringUtils.isBlank(output.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            output.setMsg_id(msg_id);
        }

        deleteOutputs(output, transformModule);
    }

    /**
     * 删除输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 上午11:50:09
     *
     * @param DeleteOutputsRequest output 输出
     */
    private void deleteOutputs(DeleteOutputsRequest output, TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_OUTPUT)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(output));

        LOG.info("[delete-outputs] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpDelete(url, request);
        LOG.info("[delete-outputs] response, result: {}", res);

    }

    /**
     * 获取指定输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月12日 上午8:57:31
     *
     * @param String id
     * @return GetOutputResponse
     */
    public GetOutputResponse getOutputById(String id,TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        return getOutputById(id, msg_id,transformModule);

    }

    /**
     * 获取指定输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 下午1:15:00
     *
     * @param String id 输出id
     * @param String msg_id 消息id
     * @return GetOutputResponse 返回
     */
    private GetOutputResponse getOutputById(String id, String msg_id,TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_OUTPUT)
                .append("/")
                .append(id)
                .append("?msg=")
                .append(msg_id)
                .toString();
        LOG.info("[get-output] request, url: {}, body: {}", url, "");
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[get-output] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        GetOutputResponse response = JSONObject.parseObject(res.toJSONString(), GetOutputResponse.class);

        return response;

    }

    /**
     * 修改指定输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月12日 上午9:00:07
     *
     * @param String           id
     * @param PutOutputRequest output 不带msg_id的output
     * @return ResultCodeResponse
     */
    public ResultCodeResponse modifyOutputByIdWithMsgId(TransformModule transformModule, String id, PutOutputRequest output) throws Exception {

        if (StringUtils.isBlank(output.getMsg_id())) {
            String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
            output.setMsg_id(msg_id);
        }

        return modifyOutputById(transformModule, id, output);
    }

    /**
     * 修改指定输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月12日 上午9:00:07
     *
     * @param String           id
     * @param PutOutputRequest output 不带msg_id的output
     * @return ResultCodeResponse
     */
    public ResultCodeResponse modifyOutputById(TransformModule transformModule, PutOutputRequest output) throws Exception {
        return modifyOutputById(transformModule, output.getOutput().getId(), output);
    }

    /**
     * 修改指定输出<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 下午1:51:31
     *
     * @param String           id 输出id
     * @param PutOutputRequest output 输出参数
     * @return ResultCodeResponse 返回
     */
    private ResultCodeResponse modifyOutputById(TransformModule transformModule, String id, PutOutputRequest output) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_OUTPUT).append("/")
                .append(id).append("/")
                .append(UrlConstant.URL_INPUT_PARAM)
                .toString();
        JSONObject request = JSONObject.parseObject(JSON.toJSONString(output));

        LOG.info("[modify-output] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[modify-output] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);
        if (response.getResult_code() != null && !response.getResult_code().equals(OutputResponseEnum.SUCCESS.getCode())) {
            throw new OutputResponseErrorException(response.getResult_msg());
        }
        return response;

    }

    /**
     * 获取所有<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月12日 上午9:02:11
     *
     * @return GetEntiretiesResponse
     */
    public GetEntiretiesResponse getEntireties(TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        return getEntireties(msg_id, transformModule);

    }

    /**
     * 获取所有<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 下午2:10:05
     *
     * @param String msg_id 消息id
     * @return GetEntiretiesResponse 返回
     */
    private GetEntiretiesResponse getEntireties(String msg_id, TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_ENTIRETY)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[get-entities] request, url: {}, body: {}", url, "");
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[get-entities] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        GetEntiretiesResponse response = JSONObject.parseObject(res.toJSONString(), GetEntiretiesResponse.class);

        return response;
    }

    /**
     * 清空所有<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月12日 上午9:03:40
     */
    public void removeAll(TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        removeAll(msg_id, transformModule);

    }

    /**
     * 清空所有<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月6日 下午2:19:02
     *
     * @param String msg_id 消息id
     */
    private void removeAll(String msg_id, TransformModule transformModule) throws Exception {

        System.out.println("removeAll");

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_ENTIRETY)
                .toString();

        JSONObject json = new JSONObject();
        json.put("msg_id", msg_id);
        LOG.info("[remove-all] request, url: {}, body: {}", url, "");
        JSONObject res = HttpUtil.httpDelete(url, json);
        LOG.info("[remove-all] response, result: {}", res);

    }

    /**
     * 获取授权<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年12月5日 下午2:39:50
     */
    @Deprecated
    public JSONObject getAuthorizationAddMsgId(TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        return getAuthorization(msg_id, transformModule);
    }

    /**
     * 获取授权<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年12月5日 下午2:27:54
     */
    @Deprecated
    private JSONObject getAuthorization(String msg_id, TransformModule transformModule) throws Exception {

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_AUTHORIZATION)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        JSONObject res = HttpUtil.httpGet(url);
        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());
        return res;
    }

    /**
     * 获取授权<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年12月5日 下午2:39:50
     */
    public JSONObject getLicenseAddMsgId(TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        return getLicense(msg_id, transformModule);
    }



    private JSONObject getLicense(String msg_id, TransformModule transformModule) throws HttpTimeoutException {
        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_LICENSE)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        JSONObject res = HttpUtil.httpGet(url);
        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());
        return res;
    }

    public void changeBackup(TransformModule transformModule, PutBackupModeRequest backup) throws Exception {
        ResultCodeResponse result = changeBackUp(backup.getInputId(), backup.getSelect_index(), backup.getMode(), transformModule);

        if (!result.getResult_code().equals(InputResponseEnum.SUCCESS.getCode())) {
            throw new BaseException(StatusCode.FORBIDDEN, result.getResult_msg());
        }
    }

    /**
     * 更改节目备份源<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年12月25日 上午11:48:39
     *
     * @param String inputId 输入id(back_up)
     * @param String index 索引
     * @param String ip 能力ip
     * @param Integer   port 能力端口
     * @return ResultCodeResponse
     */
    public ResultCodeResponse changeBackUp(String inputId, String index, String mode, TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT).append("/")
                .append(inputId).append("/")
                .append(UrlConstant.URL_TASK_SOURCE_INDEX)
                .toString();

        JSONObject post = new JSONObject();
        post.put("msg_id", msg_id);
        post.put("mode", mode);
        post.put("select_index", index);

        LOG.info("[change-backup] request, url: {}, body: {}", url, post);
        JSONObject res = HttpUtil.httpPut(url, post);
        LOG.info("[change-backup] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);
        if (response.getResult_code() != null && !response.getResult_code().equals(TaskResponseEnum.SUCCESS.getCode())) {
            throw new TaskResponseErrorException(response.getResult_msg());
        }
        return response;
    }

    /**
     * @param ip   1 转换模块IP
     * @param port 2 转换模块Port
     * @MethodName: getAlarmUrl
     * @Description: TODO 获取告警地址
     * @Return: java.lang.String
     * @Author: Poemafar
     * @Date: 2020/12/30 14:09
     **/
    public String getAlarmUrl(TransformModule transformModule) throws Exception {
        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_ALARM)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[get-alarm-url] request, url: {}", url);
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[get-alarm-url] response, result: {}", res);
        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());
        String alarm_url = res.getString("alarm_url");
        return alarm_url;
    }

    /**
     * @MethodName: putAlarmUrl
     * @Description: 设置告警请求地址
     * @param ip 转换IP
     * @param port 转换端口
     * @param alarmUrl 告警地址
     * @Return: com.sumavision.tetris.capacity.bo.request.ResultCodeResponse
     * @Author: Poemafar
     * @Date: 2021/2/7 17:02
     **/
    public ResultCodeResponse putAlarmUrl(TransformModule transformModule, String alarmUrl) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_ALARM)
                .toString();

        JSONObject put = new JSONObject();
        put.put("msg_id", msg_id);
        put.put("alarm_url", alarmUrl);
        put.put("max_count", 100);

        LOG.info("[set-alarm-url] request, url: {}, body: {}", url, put);
        JSONObject res = HttpUtil.httpPut(url, put);
        LOG.info("[set-alarm-url] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);

        return response;

    }

    /**
     * 设置心跳地址请求<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2020年1月14日 上午11:51:24
     *
     * @param String ip 能力ip
     * @param Long   port 能力端口
     * @param String heartbeatUrl 心跳地址
     * @return ResultCodeResponse
     */
    public ResultCodeResponse putHeartbeatUrl(TransformModule transformModule, String heartbeatUrl) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_HEARTBEAT)
                .toString();

        JSONObject put = new JSONObject();
        put.put("msg_id", msg_id);
        put.put("heartbeat_url", heartbeatUrl);
        put.put("span_second", 10);

        LOG.info("[set-heartbeat-url] request, url: {}, body: {}", url, put);
        JSONObject res = HttpUtil.httpPut(url, put);
        LOG.info("[set-heartbeat-url] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);

        return response;

    }

    /**
     * 追加排期请求<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2020年4月28日 下午1:27:17
     *
     * @param String             ip 转换模块ip
     * @param Long               port 转换模块端口
     * @param String             inputId schedule输入id
     * @param PutScheduleRequest scheduleRequest 追加排期参数
     * @return ResultCodeResponse
     */
    public ResultCodeResponse putSchedule(TransformModule transformModule, String inputId, PutScheduleRequest scheduleRequest) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        scheduleRequest.setMsg_id(msg_id);

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT).append("/")
                .append(inputId).append("/")
                .append(UrlConstant.URL_INPUT_SCHEDULE)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(scheduleRequest));
        LOG.info("[set-schedule] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[set-schedule] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);

        return response;

    }

    /**
     * 清空排期单
     *
     * @param ip
     * @param port
     * @param inputId
     * @param scheduleRequest
     * @throws Exception
     */
    public void clearSchedule(TransformModule transformModule, String inputId, DeleteScheduleRequest scheduleRequest) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
        scheduleRequest.setMsg_id(msg_id);

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT).append("/")
                .append(inputId).append("/")
                .append(UrlConstant.URL_INPUT_SCHEDULE)
                .toString();

        JSONObject request = JSONObject.parseObject(JSON.toJSONString(scheduleRequest));
        LOG.info("[clear-schedule] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpDelete(url, request);
        LOG.info("[clear-schedule] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

    }

    /**
     * 设置告警列表<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2020年5月29日 下午3:27:19
     *
     * @param String ip 转换模块ip
     * @param Long   port 转换模块端口
     * @param String alarmList 告警列表信息
     * @return ResultCodeResponse
     */
    public ResultCodeResponse putAlarmList(TransformModule transformModule, String alarmList) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        JSONObject request = JSONObject.parseObject(alarmList);
        request.put("msg_id", msg_id);

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_ALARMLIST)
                .toString();
        LOG.info("[set-alarm-list] request, url: {}, body: {}", url, request);
        JSONObject res = HttpUtil.httpPut(url, request);
        LOG.info("[set-alarm-list] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);

        return response;
    }


    /**
     * 获取所有任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年11月11日 下午2:18:39
     *
     * @return GetTaskResponse
     */
    public PlatformResponse getPlatforms(TransformModule transformModule) throws Exception {
        return getPlatformsToTransform(transformModule);
    }

    public PlatformResponse getPlatformsToTransform(TransformModule transformModule) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.GET_PLATFORM)
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[get-platform] request, url: {}", url);
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[get-platform] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());

        PlatformResponse response = JSONObject.parseObject(res.toJSONString(), PlatformResponse.class);
        return response;
    }

    public String startAnalysisStreamToTransform(TransformModule transformModule, String inputId) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT)
                .append("/")
                .append(inputId)
                .append("/tsana")
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[start-analysis-stream] request, url: {}", url);
        JSONObject res = HttpUtil.httpPost(url);
        LOG.info("[start-analysis-stream] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());
        return res.toJSONString();
    }

    public String deleteAnalysisStreamToTransform(TransformModule transformModule, String inputId) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT)
                .append("/")
                .append(inputId)
                .append("/tsana")
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[delete-analysis-stream] request, url: {}", url);
        JSONObject res = HttpUtil.httpDelete(url, new JSONObject());
        LOG.info("[delete-analysis-stream] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());
        return res.toJSONString();
    }

    public String getAnalysisStreamToTransform(TransformModule transformModule, String inputId) throws Exception {

        String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

        String url = new StringBufferWrapper().append(transformModule.getSocketAddress())
                .append(UrlConstant.URL_VERSION).append("/")
                .append(UrlConstant.URL_INPUT)
                .append("/")
                .append(inputId)
                .append("/tsana")
                .append("?msg_id=")
                .append(msg_id)
                .toString();
        LOG.info("[get-analysis-stream] request, url: {}", url);
        JSONObject res = HttpUtil.httpGet(url);
        LOG.info("[get-analysis-stream] response, result: {}", res);

        if (res == null) throw new HttpTimeoutException(transformModule.getSocketAddress());
        return res.toJSONString();
    }
}
