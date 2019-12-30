package com.sumavision.tetris.sts.device.group;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.sts.common.CommonController;
import com.sumavision.tetris.sts.device.DeviceDaoService;
import com.sumavision.tetris.sts.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * Created by Lost on 2017/2/24.
 */
@RestController
@RequestMapping(value = "/deviceGroup")
public class DeviceGroupController extends CommonController {
    @Autowired
    DeviceGroupService deviceGroupService;

    @Autowired
    DeviceDaoService deviceDaoService;


    
    @Autowired
    DeviceService deviceService;
    @Autowired
    private DeviceGroupDao deviceGroupDao;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DeviceGroupVO>> listDeviceGroups() {
        List<DeviceGroupVO> deviceGroupVOs = deviceDaoService.findDeviceGroup();
        if(deviceGroupVOs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(deviceGroupVOs, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> saveDeviceGroup(@Valid @RequestBody DeviceGroupPO deviceGroup , HttpServletRequest request) throws BaseException {
        deviceGroupService.save(deviceGroup);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public ResponseEntity<String> deleteDeviceGroup(@RequestParam Long[] groupIds , @RequestParam Long[] sdmDeviceIds ,
                                                    @RequestParam(required = false) Long[] deviceIds , HttpServletRequest request) {

        return new ResponseEntity<>(HttpStatus.OK);
    }



    @RequestMapping(value = "/task" , method = RequestMethod.GET)
    public ResponseEntity<Map> getDeviceGroup( HttpServletRequest request) {
        return new ResponseEntity<>(deviceDaoService.findDeviceGroupAndNetGroups() , HttpStatus.OK);
    }

    @RequestMapping(value = "/getDeviceNode" , method = RequestMethod.GET)
    public ResponseEntity<Map> getDeviceNodeInDeviceGroup(@RequestParam Integer taskType,HttpServletRequest request) {
//        if(taskType.equals(CommonConstants.ENCAPSULATE)){
//            return new ResponseEntity<>(deviceDaoService.findMainDeviceNodeByGroup(CommonConstants.FunUnitType.ENCAPSULATE) , HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>(deviceDaoService.findMainDeviceNodeByGroup(CommonConstants.FunUnitType.TRANS) , HttpStatus.OK);
//        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/updateAutoBackupFlag" , method = RequestMethod.POST)
    public ResponseEntity<String> updateAutoBackupFlag(@RequestParam Long id,@RequestParam Boolean flag,HttpServletRequest request) {
        try {
            DeviceGroupPO deviceGroupPO = deviceGroupDao.findOne(id);
            deviceGroupPO.setAutoBackupFlag(flag);
            deviceGroupDao.save(deviceGroupPO);
        } catch (Exception e) {
            e.printStackTrace();
//            return new ResponseEntity<>(errorMsg(request , e) , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
