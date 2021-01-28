package com.sumavision.tetris.device.group;

import com.sumavision.tetris.business.common.enumeration.BackupStrategy;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lost on 2017/2/24.
 */
@Service
public class DeviceGroupService {
    @Autowired
    DeviceGroupDao deviceGroupDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    DeviceService deviceService;


    /**
     * @MethodName: editDeviceGroup
     * @Description: 修改设备分组
     * @param id 1
     * @param name 2
     * @param backupStrategy 3
     * @Return: void
     * @Author: Poemafar
     * @Date: 2020/12/23 8:45
     **/
    public void editDeviceGroup(Long id, String name,String backupStrategy) throws BaseException {
        DeviceGroupPO temp = deviceGroupDao.findById(id);
        temp.setUpdateTime(new Date());
        if (!name.equals(temp.getName())){
            saveCheck(name);
            temp.setName(name);
        }
        temp.setBackupStrategy(BackupStrategy.valueOf(backupStrategy));
        deviceGroupDao.save(temp);
    }

    public void changeAutoBackup(Long id,Boolean checked){
        deviceGroupDao.updateAutoBackupFlagById(id,checked);
    }

    /**
     * 添加设备分组
     * @param name
     * @param backupStrategy
     * @throws BaseException
     */
    public void save(String name,String backupStrategy) throws BaseException {
        saveCheck(name);
        DeviceGroupPO temp = new DeviceGroupPO();
        temp.setUpdateTime(new Date());
        temp.setName(name);
        temp.setBackupStrategy(BackupStrategy.valueOf(backupStrategy));
        temp.setAutoBackupFlag(true);
        deviceGroupDao.save(temp);
    }

    private void saveCheck(String name) throws BaseException {
        if (name.isEmpty()){
            throw new BaseException(StatusCode.FORBIDDEN,"分组名不能为空");
        }
        DeviceGroupPO group = deviceGroupDao.findTopByName(name);
        if (null != group) {
            throw new BaseException(StatusCode.FORBIDDEN, "不允许同名");
        }
    }


    public DeviceGroupVO getDeviceGroup(Long id) {
        DeviceGroupPO deviceGroupPO = deviceGroupDao.findById(id);
        List<DevicePO> devicePOS = deviceDao.findByDeviceGroupId(deviceGroupPO.getId());
        DeviceGroupVO deviceGroupVO = new DeviceGroupVO(deviceGroupPO,devicePOS);
        return deviceGroupVO;
    }


    public List<DeviceGroupVO> getDeviceGroups() {
        List<DeviceGroupVO> deviceGroupVOS = new ArrayList<>();
        List<DeviceGroupPO> deviceGroupPOS = deviceGroupDao.findAll();
        deviceGroupPOS.stream().forEach(dg->{
            List<DevicePO> devicePOS = deviceDao.findByDeviceGroupId(dg.getId());
            DeviceGroupVO deviceGroupVO = new DeviceGroupVO(dg,devicePOS);
            deviceGroupVOS.add(deviceGroupVO);
        });
        return deviceGroupVOS;
    }

    public void delete(String name) throws Exception {
        DeviceGroupPO deviceGroupPO = deviceGroupDao.findTopByName(name);
        if (deviceGroupPO==null){
            return;
        }
        if (Boolean.TRUE.equals(deviceGroupPO.getBeDefault())){
            throw new BaseException(StatusCode.FORBIDDEN,"不能删除默认分组");
        }
        delete(deviceGroupPO);
    }

    public void delete(Long id) {
        DeviceGroupPO deviceGroupPO = deviceGroupDao.findById(id);
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
