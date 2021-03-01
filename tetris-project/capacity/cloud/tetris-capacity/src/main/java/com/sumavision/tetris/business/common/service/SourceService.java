package com.sumavision.tetris.business.common.service;/**
 * Created by Poemafar on 2021/2/1 13:59
 */

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.business.common.ResultCode;
import com.sumavision.tetris.business.common.ResultVO;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.Util.IpV4Util;
import com.sumavision.tetris.business.common.Util.MediaUtil;
import com.sumavision.tetris.business.common.Util.NodeUtil;
import com.sumavision.tetris.business.common.bo.MediaSourceBO;
import com.sumavision.tetris.business.common.enumeration.FunUnitStatus;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.vo.RefreshSourceDTO;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.transcode.vo.AnalysisInputVO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.response.AnalysisResponse;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * @ClassName: SourceService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/1 13:59
 */
@Service
public class SourceService {

    @Autowired
    TranscodeTaskService transcodeTaskService;

    @Autowired
    NetCardInfoDao netCardInfoDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    NodeUtil nodeUtil;

    @Autowired
    CapacityProps capacityProps;

    public ResultVO refreshSource(RefreshSourceDTO refreshSourceDTO) throws Exception {
        if (refreshSourceDTO.getUrl() == null || refreshSourceDTO.getType() == null) {
            return new ResultVO(ResultCode.ILLEGAL_PARAMS).setDetail("source url and type must not null");
        }
        ProtocolType sourceProtocolType = ProtocolType.getProtocolType(refreshSourceDTO.getType());
        String deviceIp = refreshSourceDTO.getDeviceIp();
        if (deviceIp==null || deviceIp.isEmpty()){
            deviceIp = getDeviceForRefreshSource(refreshSourceDTO.getUrl(),sourceProtocolType, refreshSourceDTO.getSrtMode());
        }

        String localIp = refreshSourceDTO.getLocalIp();
        if (localIp == null || localIp.isEmpty()) {
            DevicePO device = deviceDao.findByDeviceIp(deviceIp);
            if (device!=null){
                List<NetCardInfoPO> nets = netCardInfoDao.findByDeviceId(device.getId());
                if (!CollectionUtils.isEmpty(nets) && nets.stream().anyMatch(n->n.getInputNetGroupId()!=null)) {
                    localIp = nets.stream().filter(n->n.getInputNetGroupId()!=null).findFirst().get().getIpv4();
                }else {
                    localIp = deviceIp;
                }
            }else {
                localIp = deviceIp;
            }
        }

        InputBO inputBO = new InputBO();
        inputBO.setId(UUID.randomUUID().toString());
        inputBO.setEncapsulateInfo(new MediaSourceBO(refreshSourceDTO,localIp));

        AnalysisInputVO analysisInputVO = new AnalysisInputVO();
        analysisInputVO.setDevice_ip(deviceIp);
        analysisInputVO.setInput(inputBO);
        AnalysisResponse response = transcodeTaskService.analysisInput(analysisInputVO);
        response.setDevice_ip(deviceIp);//刷源的设备
        if (response.getInput().getResult_code()!=null && 0!=response.getInput().getResult_code()) {
            return new ResultVO(ResultCode.FAIL).setDetail(JSON.toJSONString(response));
        }
        return new ResultVO(ResultCode.SUCCESS,response);
    }

    /**
     * @MethodName: getDeviceForRefreshSource
     * @Description: TODO
     * @param url 1 源地址
     * @param type 2 源封装类型
     * @param srtMode 3 可为null
     * @Return: java.lang.String
     * @Author: Poemafar
     * @Date: 2021/2/1 17:11
     **/
    public String getDeviceForRefreshSource(String url,ProtocolType type,String srtMode) throws BaseException {
        String deviceIp = null;
        boolean bePush = MediaUtil.bePushFromSourceType(type,srtMode);
        String dstIp = IpV4Util.getIpFromUrl(url);
        if (bePush && !CommonUtil.isMulticast(dstIp)){ //推流单播
            List<NetCardInfoPO> nets = netCardInfoDao.findByIpv4(dstIp);
            if (!CollectionUtils.isEmpty(nets)) {
                DevicePO device = deviceDao.findById(nets.get(0).getDeviceId());
                if (device != null) {
                    deviceIp = device.getDeviceIp();
                }else{
                    deviceIp = dstIp;
                }
            }else{
                deviceIp = dstIp;
            }
        }else{
            List<DevicePO> devices = deviceDao.findByFunUnitStatus(FunUnitStatus.NORMAL);
            if (!CollectionUtils.isEmpty(devices)) {
                deviceIp = devices.get(0).getDeviceIp();
            }else{
                deviceIp = capacityProps.getIp();
            }
        }
        return deviceIp;
    }


}
