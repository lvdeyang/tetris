package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/2 17:27
 */

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.capacity.TransformModuleService;
import com.sumavision.signal.bvc.common.CommonUtil;
import com.sumavision.signal.bvc.common.IpV4Util;
import com.sumavision.signal.bvc.common.enumeration.CommonConstants.ProtocolType;
import com.sumavision.signal.bvc.entity.dao.CapacityPermissionPortDAO;
import com.sumavision.signal.bvc.entity.dao.SourceDAO;
import com.sumavision.signal.bvc.entity.po.CapacityPermissionPortPO;
import com.sumavision.signal.bvc.entity.po.SourcePO;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: CreateInputSource
 * @Description 输入源建的虚拟bundle
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/2 17:27
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class CreateInputSource extends AbstractPassbyMsg {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInputSource.class);

    @Autowired
    SourceDAO sourceDAO;

    @Autowired
    TransformModuleService transformModuleService;

    @Autowired
    CapacityPermissionPortDAO capacityPermissionPortDao;

    @Override
    public void exec(PassbyBO passby) throws Exception {

        CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(passby.getBundle_id());
        if (permission == null){
            permission = new CapacityPermissionPortPO();
            String protocolTypeStr = passby.getPass_by_content().getString("type");
            ProtocolType protocolType = ProtocolType.getProtocolType(protocolTypeStr);
            String url = passby.getPass_by_content().getString("url");


            BundlePO transModuleInTask = null;
            //判断下输入如果是单播，就只能选特定设备了，不能负载均衡建任务
            Boolean beSingle = transformModuleService.beSpecificModuleToReceiveStream(url,protocolType);
            if (beSingle) {
                String sourceIp = CommonUtil.getIpFromUrl(url);
                transModuleInTask =transformModuleService.getSpecificTransformModule(sourceIp);
            }else{
                transModuleInTask =transformModuleService.autoChosedTransformModule();
            }

            if (transModuleInTask == null){
                throw new BaseException(StatusCode.ERROR,"未找到接受收入转换模块");
            }
            String bundleId = passby.getBundle_id();

            permission.setBundleId(bundleId);
            permission.setLayerId(passby.getLayer_id());
            permission.setCapacityIp(transModuleInTask.getDeviceIp());

            String srtMode = "caller";
            if (ProtocolType.SRT_TS.equals(protocolType)){
                srtMode = passby.getPass_by_content().getString("srt_mode");
            }

            SourcePO sourcePO = new SourcePO();
            sourcePO.setUrl(url);
            sourcePO.setProtocolType(protocolType);
            sourcePO.setSrtMode(srtMode);
            permission.getSourcePOs().add(sourcePO);
            if (beSingle) {
                permission.setCapacityPort(CommonUtil.getPortFromUrl(url).longValue());
            }
//            permissionPortPO.setCapacityPort();//todo url如果是推流需要记下端口，防止重复
            capacityPermissionPortDao.save(permission);
//todo 先不刷表           MediaSourceBO sourceBO = new MediaSourceBO().setProtocolType(protocolType).setUrl(url).setLocalIp(transModuleInTask.getDeviceIp());
//           transformModuleService.refreshSourceAndSaveSourceInfo(permission.getId().toString(), transModuleInTask.getDeviceIp(), sourceBO);
        }else{
            LOGGER.error("the bundle has exist, id:{}",passby.getBundle_id());
            throw new BaseException(StatusCode.FORBIDDEN,"bundle has exist, id: "+passby.getBundle_id());
        }


    }


}
