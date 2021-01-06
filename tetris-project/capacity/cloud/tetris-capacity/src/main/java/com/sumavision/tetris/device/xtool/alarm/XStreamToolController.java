package com.sumavision.tetris.device.xtool.alarm;/**
 * Created by Poemafar on 2020/12/29 10:10
 */

import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;
import com.sumavision.tetris.application.alarm.service.AlarmService;
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DevicePO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName: XStreamToolController
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/29 10:10
 */
@RestController
@RequestMapping(value = "/tetris-capacity/xtool/")
public class XStreamToolController {

    private static Logger LOGGER = LoggerFactory.getLogger(XStreamToolController.class);

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    AlarmService alarmService;

    @Autowired
    AlarmFeignClientService alarmFeignClientService;



    @RequestMapping("/netcardNotice")
    @ResponseBody
    public ResponseEntity netcardNotice(HttpServletRequest request){
        try {
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(
                    (ServletInputStream)request.getInputStream(),"utf-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            request.getInputStream().close();
            br.close();
            String deviceIp = request.getRemoteAddr();
            LOGGER.info("receive netcard alarm :{},ip:{}",sb.toString(),deviceIp);
            DevicePO devicePO = deviceDao.findByDeviceIp(deviceIp);
            if(devicePO == null){
                LOGGER.info("netcard alarm device not found!");
                return new ResponseEntity(HttpStatus.OK);
            }
//            //yzx 小工具上线的时候修改ip用
//            else{
//                JSONObject ipInfo = new JSONObject();
//                JSONArray netArray = new JSONArray();
//                NetCardHttpUnit netCardHttpUnit = SpringBeanFactory.getBean(NetCardHttpUnit.class);
//                NetCardInfoDao netCardInfoDao = SpringBeanFactory.getBean(NetCardInfoDao.class);
//                List<NetCardInfoPO> outNets = netCardInfoDao.findByOutputNetGroupIdIsNotNullAndDeviceId(devicePO.getId());
//                for (NetCardInfoPO outNet : outNets) {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("name",outNet.getName());
//                    jsonObject.put("mac",outNet.getMac());
//                    jsonObject.put("ipaddr",outNet.getIpv4());
//                    jsonObject.put("netmask",outNet.getIpv4Mask());
//                    netArray.add(jsonObject);
//                }
//                if (!netArray.isEmpty()) {
//                    ipInfo.put("netcard", netArray);
//                    try {
//                        netCardHttpUnit.setNetCard(devicePO, XToolMsgType.SET_IP_INFO, ipInfo);
//                    } catch (BaseException e) {
//                        logger.error("change deviceNode ip error, deviceNode-id:{}", devicePO.getId());
//                    }
//                }
//            }
            alarmService.netCardNoticeHandleFromXtool(sb.toString(), devicePO.getId());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Encode Error",e);
        } catch (IOException e) {
            LOGGER.error("IO Error",e);
        } catch (Exception e){
            LOGGER.error("System Error",e);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
