package com.sumavision.tetris.capacity.template;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.template.feign.TemplateTaskVO;
import com.sumavision.tetris.application.template.feign.TemplateTaskService;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.common.service.TaskService;
import com.sumavision.tetris.business.director.service.DirectorTaskService;
import com.sumavision.tetris.business.push.service.ScheduleService;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.yjgb.service.TransformService;
import com.sumavision.tetris.capacity.TetrisCapacityApplicationTest;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.util.http.HttpUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Poemafar on 2020/7/28 11:55
 */
public class TemplateServiceTestCapacity extends TetrisCapacityApplicationTest {


    @Autowired
    TemplateService templateService;

    @Autowired
    DirectorTaskService directorTaskService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    TaskService taskService;

    @Autowired
    TranscodeTaskService transcodeTaskService;

    @Autowired
    TemplateTaskService templateTaskService;

    @Autowired
    TransformService transformService;

    @Autowired
    TaskOutputDAO taskOutputDAO;

    @Test
    public void testTransfer() throws Exception {

        String task = "{\n" +
                "    \"task_ip\":\"10.10.40.103\",\n" +
                "    \"template\":\"VIDEO_TRANS\",\n" +
                "    \"map_sources\":[\n" +
                "        {\n" +
                "            \"index\":1,\n" +
                "            \"type\":\"udp_ts\",\n" +
                "            \"startTime\":\"2020-12-11 9:46:00\",\n" +
                "            \"endTime\":\"2020-12-11 9:48:00\",\n" +
                "            \"url\":\"udp://@10.10.40.103:19000\",\n" +
                "            \"localIp\":\"10.10.40.103\"\n" +
                "        }\n" +
//                "        {\n" +
//                "            \"index\":3,\n" +
//                "            \"type\":\"schedule\",\n" +
//                "            \"mediaType\":\"video\",\n" +
//                "            \"prev\":1\n" +
//                "        }\n" +
                "    ],\n" +
                "    \"map_tasks\":[\n" +
                "        {\n" +
                "            \"index\":1,\n" +
                "            \"codec\":\"h264\",\n" +
                "            \"resolution\":\"720*576\",\n" +
                "            \"bitrate\":4000\n" +
                "        },\n" +
                "        {\n" +
                "            \"index\":2,\n" +
                "            \"codec\":\"audiopassby\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"map_outputs\":[\n" +
                "        {\n" +
                "            \"index\":1,\n" +
                "            \"type\":\"udp_ts\",\n" +
                "            \"url\":\"udp://10.10.40.24:19030\",\n" +
                "            \"localIp\":\"10.10.40.103\",\n" +
                "            \"rate_ctrl\":\"VBR\",\n" +
                "            \"bitrate\":8000\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        TemplateTaskVO taskVO = JSONObject.parseObject(task, TemplateTaskVO.class);
        templateTaskService.addTask(taskVO);
    }

    @Test
    public void analysisFile(){
        String result= null;
        try {
//            result = transformService.analysisInput("10.10.40.104","ftp://test:123@192.165.58.123:21/upload/tmp/北京数码视讯科技股份有限公司/a9b4c2c776814c5288840dac1a9166af/1/0.0.1606637360226/suma.ts");
            result = transformService.analysisInput("10.10.40.104","/home/Rolling_in_the_deep.mp4");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    @Test
    public void truncate() throws Exception {
        transcodeTaskService.deleteAll();
    }

    @Test
    public void deleteTask() throws Exception {
        TaskOutputPO output = taskService.delete("3ddcdbb1-72a2-45a0-b31b-cee5b8852605", BusinessType.TRANSCODE);
        if (output!=null){
            taskOutputDAO.delete(output);
        }
    }

    @Test
    public void lock(){
        Map<String,String> ipMap=new HashMap();
        ipMap.put("10.10.40.184","10.10.40.103");
        String inputStr = "{\"delay_ms\":0,\"id\":\"2902\",\"program_array\":[{\"audio_array\":[{\"backup_mode\":\"silence\",\"bitrate\":188094,\"cutoff_time\":3000,\"decode_mode\":\"cpu\",\"pid\":35,\"type\":\"mp2\"}],\"name\":\"HD Phx Chinese Channel\",\"normal_map\":{},\"pcr_pid\":36,\"pmt_pid\":37,\"program_number\":2,\"provider\":\"Phoenix Satellite Television Co\",\"video_array\":[{\"backup_mode\":\"none\",\"bitrate\":8723182,\"cutoff_time\":3000,\"decode_mode\":\"cpu\",\"fps\":\"50\",\"height\":1080,\"pattern_path\":\"\",\"pid\":36,\"type\":\"h264\",\"width\":1920}]}],\"udp_ts\":{\"local_ip\":\"10.10.40.184\",\"source_ip\":\"10.10.40.184\",\"source_port\":22222}}";
        JSONObject obj = JSONObject.parseObject(inputStr);
        CommonUtil.setValueByKeyFromJson(obj,"local_ip",ipMap);
        System.out.println(obj.toJSONString());
    }

    @Test
    public void batchDeletePushTask(){
        String taskIds = "[\"25517515-95ba-4152-9e6d-69f4096bbf07\"]";
        List<String> taskUuids = JSONArray.parseArray(taskIds, String.class);
        try {
            scheduleService.batchDeletePushTask(taskUuids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        taskOutputDAO.updateCapacityIpByIp("10.10.40.184","10.10.40.183");
    }

    @Test
    public void deleteInput() throws CommonException {
       // scheduleService.test();
        JSONObject obj = new JSONObject();
        obj.put("msg_id","123");
        JSONArray inputObjs = new JSONArray();
        JSONObject inputid = new JSONObject();
        inputid.put("id","123");
        inputObjs.add(inputid);
        obj.put("input_array",inputObjs);
        JSONObject res = HttpUtil.httpDelete("http://[fe80::224:68ff:fe07:7b71]:5656/v0.0/inputs", obj);
        System.out.println(res.toJSONString());
    }

    @Test
    public void testJson() throws  CommonException{
        String outputUrl = "http://10.10.40.103:1010/";

        String urlWithoutHead = outputUrl.split("://")[1];
        String pubName = urlWithoutHead.substring(urlWithoutHead.indexOf(":")).split("/",2)[1] ;
        System.out.println(pubName);
    }

}