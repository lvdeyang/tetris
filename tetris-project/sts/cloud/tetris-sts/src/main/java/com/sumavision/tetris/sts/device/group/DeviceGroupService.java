package com.sumavision.tetris.sts.device.group;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.common.ErrorCodes;
import com.sumavision.tetris.sts.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lost on 2017/2/24.
 */
@Service
public class DeviceGroupService {
    @Autowired
    DeviceGroupDao deviceGroupDao;

    @Autowired
    DeviceService deviceService;


    public void save(DeviceGroupPO deviceGroupPO) throws BaseException {
        saveCheck(deviceGroupPO);
        if (deviceGroupPO.getId() != null) {
            DeviceGroupPO temp = deviceGroupDao.findOne(deviceGroupPO.getId());
            temp.setName(deviceGroupPO.getName());
            temp.setDataNetIds(deviceGroupPO.getDataNetIds());
            temp.setTransmitNetId(deviceGroupPO.getTransmitNetId());
            deviceGroupDao.save(temp);
        } else
            deviceGroupDao.save(deviceGroupPO);
    }

    private void saveCheck(DeviceGroupPO deviceGroupPO) throws BaseException {
        DeviceGroupPO group;
        if (deviceGroupPO.getId() != null) {
            group = deviceGroupDao.findByNameAndIdNot(deviceGroupPO.getName() , deviceGroupPO.getId());
        } else {
            group = deviceGroupDao.findTopByName(deviceGroupPO.getName());
        }
        if (null != group) {
            throw new BaseException(StatusCode.FORBIDDEN, ErrorCodes.NAME_CONFLICT);
        }
    }

    public List<DeviceGroupPO> findAll() {
        return deviceGroupDao.findAll();
    }

    public void delete(Long id) {
        DeviceGroupPO deviceGroupPO = deviceGroupDao.findOne(id);
        delete(deviceGroupPO);
    }

    public void delete(DeviceGroupPO deviceGroupPO) {

    	/**删除分组关联的设备数据**/
    	deviceService.delete(deviceGroupPO);
        
    	/**删除分组数据*/
    	deviceGroupDao.delete(deviceGroupPO);
    }

    public DeviceGroupDao getDeviceGroupDao() {
        return deviceGroupDao;
    }

}
