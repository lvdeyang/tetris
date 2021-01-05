package com.sumavision.tetris.device;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.business.common.enumeration.BackType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Lost on 2017/2/13.
 */
@RestController
@RequestMapping(value = "/device")
public class DeviceController {
    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceDao deviceDao;

    /**
     * 添加设备
     * @param groupId
     * @param name
     * @param deviceIp
     * @param backType
     * @param devicePort
     * @param request
     * @return
     */
    @JsonBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object saveDevice(@RequestParam Long groupId, @RequestParam String name,
                             @RequestParam String deviceIp, @RequestParam BackType backType,
                             @RequestParam Integer devicePort, HttpServletRequest request) throws Exception {
        // 查重,,,可能要改成bundleId
        deviceService.deviceCheck(name, deviceIp);
        deviceService.saveDevice(groupId, backType, name, deviceIp, devicePort);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object deleteDevice(@RequestParam Long id) {
        deviceService.deleteDevice(id);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "refreshNet", method = RequestMethod.POST)
    public Object refreshNet(@RequestParam Long id) throws Exception {
        deviceService.refreshNetcard(id);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public Object configDevice(@RequestParam Long id, @RequestParam String nets,
                               HttpServletRequest request) {
        ResOptVO resOptVO = new ResOptVO();
        try {
            JSONArray netArray = JSONArray.parseArray(nets);
            resOptVO = deviceService.configDevice(id, netArray);
        } catch (BaseException e) {
            resOptVO.setBeSuccess(false);
            resOptVO.setReason(e.getMessage());
        } catch (Exception e) {
            resOptVO.setBeSuccess(false);
            resOptVO.setReason("System Error. "+e.getMessage());
            e.printStackTrace();
        }
        return resOptVO;
    }

    @JsonBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Object editDevice(@RequestParam Long id, @RequestParam Long groupId, @RequestParam String name, @RequestParam String backType,
                             HttpServletRequest request) throws Exception {
        deviceService.editDevice(id, groupId, name, backType);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public Object resetDevice(@RequestParam Long id, HttpServletRequest request) throws Exception {
        deviceService.resetDevice(id);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/switch", method = RequestMethod.POST)
    public Object switchDevice(@RequestParam Long tgtDevId, @RequestParam Long srcDevId, HttpServletRequest request) throws Exception {
        deviceService.switchDeviceByManual(srcDevId,tgtDevId);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    public Object syncDevice(@RequestParam Long id, HttpServletRequest request) throws Exception {
        deviceService.syncDevice(id);
        return null;
    }

}
