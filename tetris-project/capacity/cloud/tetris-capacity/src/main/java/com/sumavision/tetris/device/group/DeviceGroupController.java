package com.sumavision.tetris.device.group;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//import com.sumavision.tetris.sts.device.ComplexService;

/**
 * Created by Lost on 2017/2/24.
 */
@RestController
@RequestMapping(value = "/deviceGroup")
public class DeviceGroupController {


    @Autowired
    DeviceGroupService deviceGroupService;



    @Autowired
    private DeviceGroupDao deviceGroupDao;

    

    /**
     * 获取设备分组，连带分组内的设备，及设备节点
     * @return
     */
    @JsonBody
    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public Object listDeviceGroups() {
        List<DeviceGroupVO> deviceGroupVOs = deviceGroupService.getDeviceGroups();
        return deviceGroupVOs;
    }

    @JsonBody
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public Object getDeviceGroupByName(@RequestParam Long id) {
        DeviceGroupVO deviceGroupVO = deviceGroupService.getDeviceGroup(id);
        return deviceGroupVO;
    }

    @JsonBody
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public Object deleteDeviceGroup(@RequestParam String name, HttpServletRequest request)throws Exception {
        deviceGroupService.delete(name);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/edit" , method = RequestMethod.POST)
    public Object editDeviceGroup(@RequestParam Long id, @RequestParam String name, @RequestParam String backupStrategy, HttpServletRequest request) throws Exception {
        deviceGroupService.editDeviceGroup(id,name,backupStrategy);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/changeAutoBackup" , method = RequestMethod.POST)
    public Object changeAutoBackup(@RequestParam Long id, @RequestParam Boolean flag, HttpServletRequest request) throws Exception {
        deviceGroupService.changeAutoBackup(id,flag);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object getDeviceTask(@RequestParam String name,
                                                @RequestParam String backupStrategy) throws Exception {
        deviceGroupService.save(name,backupStrategy);
        return null;
    }



    @RequestMapping(value = "/updateAutoBackupFlag" , method = RequestMethod.POST)
    public ResponseEntity<String> updateAutoBackupFlag(@RequestParam Long id,@RequestParam Boolean flag,HttpServletRequest request) {
        try {
            DeviceGroupPO deviceGroupPO = deviceGroupDao.findById(id);
            deviceGroupPO.setAutoBackupFlag(flag);
            deviceGroupDao.save(deviceGroupPO);
        } catch (Exception e) {
            e.printStackTrace();
//            return new ResponseEntity<>(errorMsg(request , e) , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
