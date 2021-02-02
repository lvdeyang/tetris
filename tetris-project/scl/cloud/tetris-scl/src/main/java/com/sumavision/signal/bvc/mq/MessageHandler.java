package com.sumavision.signal.bvc.mq;/**
 * Created by Poemafar on 2020/8/31 17:09
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.VenusMessageHead;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.util.RegisterStatus;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.capacity.TransformModuleService;
import com.sumavision.signal.bvc.capacity.bo.source.MediaSourceBO;
import com.sumavision.signal.bvc.common.enumeration.CommonConstants.*;
import com.sumavision.signal.bvc.common.enumeration.NodeType;
import com.sumavision.signal.bvc.config.CapacityProps;
import com.sumavision.signal.bvc.director.dao.DirectorTaskDAO;
import com.sumavision.signal.bvc.director.dao.DirectorTaskDstDAO;
import com.sumavision.signal.bvc.director.dao.DirectorTaskSrcDAO;
import com.sumavision.signal.bvc.director.po.DirectorTaskDstPO;
import com.sumavision.signal.bvc.director.po.DirectorTaskPO;
import com.sumavision.signal.bvc.director.po.DirectorTaskSrcPO;
import com.sumavision.signal.bvc.entity.dao.*;
import com.sumavision.signal.bvc.entity.enumeration.*;
import com.sumavision.signal.bvc.entity.po.*;
import com.sumavision.signal.bvc.entity.vo.TranscodeVO;
import com.sumavision.signal.bvc.fifthg.bo.http.AudioEncParam;
import com.sumavision.signal.bvc.fifthg.bo.http.SuiBusParam;
import com.sumavision.signal.bvc.http.HttpAsyncClient;
import com.sumavision.signal.bvc.http.HttpClient;
import com.sumavision.signal.bvc.mq.PassbyMsg.AbstractPassbyMsg;
import com.sumavision.signal.bvc.mq.PassbyMsg.PassbyMsgFactory;
import com.sumavision.signal.bvc.mq.bo.*;
import com.sumavision.signal.bvc.network.service.NetworkService;
import com.sumavision.signal.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.signal.bvc.service.*;
import com.sumavision.signal.bvc.terminal.JV220Param;
import com.sumavision.signal.bvc.terminal.OneOneFiveMTerminal;
import com.sumavision.signal.bvc.terminal.TerminalParam;
import com.sumavision.tetris.capacity.server.CapacityService;
import com.sumavision.tetris.capacity.server.PackageService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.util.*;

/**
 * @ClassName: HandleMessage
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/8/31 17:09
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private PortMappingDAO portMappingDao;

    @Autowired
    private TerminalBindRepeaterDAO terminalBindRepeaterDao;

    @Autowired
    private TaskDAO taskDao;

    @Autowired
    CapacityService capacityService;

    @Autowired
    private RepeaterDAO repeaterDao;

    @Autowired
    private InternetAccessDAO internetAccessDao;

    @Autowired
    private TaskExecuteService taskExecuteService;

    @Autowired
    private TerminalMappingService terminalMappingService;

    @Autowired
    private MQSendService mqSendService;

    @Autowired
    private QueryUtilService queryUtilService;

    @Autowired
    private JV220Param jv220Param;

    @Autowired
    private OneOneFiveMTerminal oneOneFiveMTerminal;

    @Autowired
    private ResourceBundleDAO resourceBundleDao;

    @Autowired
    private CapacityProps capacityProps;

    @Autowired
    private CapacityPermissionPortDAO capacityPermissionPortDao;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private TransformModuleService transformModuleService;

    @Value("${constant.package.output.type:rtmp}")
    private String packageOutputType;

    @Autowired
    private DirectorTaskDAO directorTaskDAO;
    @Autowired
    private DirectorTaskSrcDAO directorTaskSrcDAO;
    @Autowired
    private DirectorTaskDstDAO directorTaskDstDAO;

    /*
     * 根据接口协议的数据类型解析msg中的方法名，根据相应方法名注入上层解析接口进行处理
     */
    public void process(String msg) throws Exception {
        ResponseBO responseBo = JSONObject.parseObject(msg, ResponseBO.class);
        String msgType = responseBo.getMessage().getMessage_header().getMessage_type();
        if(VenusMessageHead.MsgType.request.toString().equalsIgnoreCase(msgType) || VenusMessageHead.MsgType.notification.toString().equalsIgnoreCase(msgType)
                || VenusMessageHead.MsgType.alert.toString().equalsIgnoreCase(msgType) || VenusMessageHead.MsgType.passby.toString().equalsIgnoreCase(msgType)){
            switch (responseBo.getMessage().getMessage_header().getMessage_name()) {
                case "open_bundle":
                    processOpenBundleMsg(responseBo);
//                    processPassByMsg(responseBo);
                    break;
                case "close_bundle":
                    processCloseBundleMsg(responseBo);
                    break;
                case "passby":
                    processPassByMsg(responseBo);
                    break;
                default:
                    break;
            }
        }else if(VenusMessageHead.MsgType.response.toString().equalsIgnoreCase(msgType)){
            switch (responseBo.getMessage().getMessage_header().getMessage_name()) {
                case "pullData":
                    break;
                default:
                    break;
            }
        }
    }

    /**处理open_bundle信息
     * @throws Exception */
    private void processOpenBundleMsg(ResponseBO responseBo) throws Exception{

        String openBundleRequestString = responseBo.getMessage().getMessage_body().getString("open_bundle_request");
        JSONObject openBundleRequest = JSON.parseObject(openBundleRequestString);
        BundleBO bundle = openBundleRequest.getObject("bundle", BundleBO.class);
        String bundleType = bundle.getBundle_type();
        String deviceModel = bundle.getDevice_model();
        if(deviceModel.equals("virtual")){
            //添加源
            JSONObject json=JSONObject.parseObject(bundle.getPass_by_str());
            List<DirectorTaskPO> directorTaskPOs=directorTaskDAO.findByBusinessId(json.getString("businessId"));
            DirectorTaskPO directorTaskPO=new DirectorTaskPO();
            if(directorTaskPOs==null||directorTaskPOs.isEmpty()){
                directorTaskPO.setBusinessId(json.getString("businessId"));
                directorTaskDAO.save(directorTaskPO);
            }else{
                directorTaskPO=directorTaskPOs.get(0);
            }
            for (ChannelBO channelBo:bundle.getChannels()) {

                if("VenusOut".equals(channelBo.getChannel_param().getBase_type())){
                    DirectorTaskSrcPO directorTaskSrcPO=new DirectorTaskSrcPO();
                    directorTaskSrcPO.setBundleId(bundle.getBundle_id());
                    directorTaskSrcPO.setTaskId(directorTaskPO.getId());
                    directorTaskSrcPO.setChannelId(channelBo.getChannel_id());
                    directorTaskSrcPO.setSortIndex(json.getInteger("index"));
                    directorTaskSrcDAO.save(directorTaskSrcPO);
                }else if("VenusIn".equals(channelBo.getChannel_param().getBase_type())){
                    DirectorTaskDstPO directorTaskDstPO=new DirectorTaskDstPO();
                    directorTaskDstPO.setTaskId(directorTaskPO.getId());
                    directorTaskDstPO.setBundleId(bundle.getBundle_id());
                    directorTaskDstPO.setChannelId(channelBo.getChannel_id());
                    directorTaskDstDAO.save(directorTaskDstPO);

                    if(directorTaskPOs==null||directorTaskPOs.isEmpty()){
                        //todo 添加备份源，添加转码任务，添加输出

                    }else{
                        //todo 单纯的给任务添加输出
                    }
                }
            }

        }else{
            String layerId = responseBo.getMessage().getMessage_header().getDestination_id();
            processOpenBundle5GMsg(bundle);
        }
    }

    /**处理colse_bundle信息
     * @throws Exception */
    private void processCloseBundleMsg(ResponseBO responseBo) throws Exception{

        String closeBundleRequestString = responseBo.getMessage().getMessage_body().getString("close_bundle_request");
        JSONObject openBundleRequest = JSON.parseObject(closeBundleRequestString);
        BundleBO bundle = openBundleRequest.getObject("bundle", BundleBO.class);

        String bundleType = bundle.getBundle_type();
        String deviceModel = bundle.getDevice_model();
        if(deviceModel.equals("virtual")){
            JSONObject json=JSONObject.parseObject(bundle.getPass_by_str());
            String businessId=json.getString("businessId");
            List<DirectorTaskPO> directorTaskPOs=directorTaskDAO.findByBusinessId(json.getString("businessId"));
            if(directorTaskPOs!=null&&directorTaskPOs.isEmpty()){
                Long taskId=directorTaskPOs.get(0).getId();
                for (ChannelBO channelBo:bundle.getChannels()) {

                    if("VenusOut".equals(channelBo.getChannel_param().getBase_type())){
                        List<DirectorTaskSrcPO> directorTaskSrcPOs=directorTaskSrcDAO.findByBundleIdAndTaskId(bundle.getBundle_id(),
                                taskId);
                        //todo 给任务下发删除备份源
                        directorTaskSrcDAO.deleteInBatch(directorTaskSrcPOs);
                    }else if("VenusIn".equals(channelBo.getChannel_param().getBase_type())){
                        List<DirectorTaskDstPO> directorTaskDstPOS=directorTaskDstDAO.findByBundleIdAndTaskId(bundle.getBundle_id(),
                                directorTaskPOs.get(0).getId());
                        //todo 给任务下发删除输出

                        //判断是否需要下发删除任务
                        List<DirectorTaskDstPO> existOuts=directorTaskDstDAO.findByTaskId(taskId);
                        if(existOuts==null||existOuts.isEmpty()){
                            //todo 下发删除任务

                            directorTaskDAO.deleteById(taskId);
                            //删除所有的源
                            directorTaskSrcDAO.deleteByTaskId(taskId);
                        }

                        directorTaskDstDAO.deleteInBatch(directorTaskDstPOS);
                    }
                }

            }
        }else{
            String layerId = responseBo.getMessage().getMessage_header().getDestination_id();
            processCloseBundle5GMsg(bundle);
        }

    }

    /**处理pass_by信息信息
     * @throws Exception */
    public void processPassByMsg(ResponseBO responseBo) throws Exception{

        String passbyString = responseBo.getMessage().getMessage_body().getString("pass_by");
        PassbyBO passby = JSON.parseObject(passbyString, PassbyBO.class);

        PassbyMsgFactory passbyMsgFactory = new PassbyMsgFactory();
        AbstractPassbyMsg passbyMsg = passbyMsgFactory.getPassbyMsg(passby.getType());
        passbyMsg.exec(passby);

    }





    /**处理open_bundle信息的5G背包类型信息
     * todo 待完善
     * @throws Exception */
    private void processOpenBundle5GMsg(BundleBO bundle) throws Exception{

        String bundleId = bundle.getBundle_id();

        List<String> bundleIds = new ArrayList<String>();
        bundleIds.add(bundleId);

        List<BundlePO> bundles = resourceBundleDao.findByBundleIds(bundleIds);
        if(bundles == null || bundles.size() <= 0){
            throw new BaseException(StatusCode.ERROR, "bundleId为：" + bundleId + "的设备不存在！");
        }

        BundlePO transformModule = transformModuleService.autoChosedTransformModule();
        if (transformModule == null){
            throw new BaseException(StatusCode.ERROR,"未找到接受收入转换模块");
        }

        BundlePO bundlePO = bundles.get(0);

        boolean isEncode = false;

        SuiBusParam suiBusParam = new SuiBusParam();
        String outInterface = "";
        List<AudioEncParam> audioEncParams = new ArrayList<>();
        for (int i = 0; i < bundle.getChannels().size(); i++) {
            ChannelBO channel = bundle.getChannels().get(i);
            String channelId = channel.getChannel_id();
            BaseParamBO baseParam = channel.getChannel_param().getBase_param();

            if(channelId.equals(ChannelType.VIDEOENCODE1.getType())){

                //视频码率
                Long bitrate = baseParam.getBitrate();
                //发送码率
                Long send_bitrate = Float.valueOf(bitrate * 2.0f).longValue() ;
                //编码分辨率
                String resoluton = baseParam.getResolution();
                //编码格式
                String codec = baseParam.getCodec();
                //帧率
                String fps = baseParam.getFps();
                String inInterface = baseParam.getInput_interface();
                String format_resolution = new StringBufferWrapper().append(resoluton)
                        .append("P")
                        .append(fps)
                        .toString();

                outInterface = baseParam.getOutput_interface();
                //视频编码参数设置
                suiBusParam.setOnoff(1);
                suiBusParam.setVidCapSel(OneOneFiveMParam.fromName(inInterface).getProtocal());
                suiBusParam.setVidEncRes(OneOneFiveMParam.fromName(format_resolution).getProtocal());
                suiBusParam.setVidEncBR(bitrate);
                suiBusParam.setVidSysBR(send_bitrate);
                suiBusParam.setVidEncStd(OneOneFiveMParam.fromName(codec).getProtocal());

                isEncode = true;

            }

            if(channelId.equals(ChannelType.AUDIOENCODE1.getType())){
                //音频编码
                String codec = baseParam.getCodec();
                //音频码率
                Long bitrate = baseParam.getBitrate();

                //为了保证enum的name唯一
                String format_bitrate = new StringBufferWrapper().append(bitrate)
                        .append("abr")
                        .toString();

                //音频编码参数设置
                AudioEncParam audEnc = new AudioEncParam();
                audEnc.setAudOnOff(1);
                audEnc.setAudRate(OneOneFiveMParam.fromName(format_bitrate).getProtocal());
                audEnc.setAudStd(OneOneFiveMParam.fromName(codec).getProtocal());
                audioEncParams.add(audEnc);

            }

        }

        //TODO:解码暂时设备不支持
        String outputUrl = "";
        if(isEncode){
            //协商端口
            CapacityPermissionPortPO permission  = capacityPermissionPortDao.findByBundleId(bundleId);
            if(permission == null){
                //协商端口
                permission = new CapacityPermissionPortPO();

                List<Long> ports = new ArrayList<Long>();
                List<CapacityPermissionPortPO> capacityPermissionPorts = capacityPermissionPortDao.findByCapacityIp(transformModule.getDeviceIp());
                for(CapacityPermissionPortPO capacityPermissionPort: capacityPermissionPorts){
                    if (capacityPermissionPort.getCapacityPort()!=null) {
                        ports.add(capacityPermissionPort.getCapacityPort());
                    }
                }

                Long newport = terminalMappingService.generatePort(ports);

                permission.setBundleId(bundleId);
                permission.setBundleIp(bundlePO.getDeviceIp());
                permission.setCapacityIp(transformModule.getDeviceIp());//推流给转换
                if ("rtmp".equals(packageOutputType)){
                    permission.setCapacityPort(1935L);//说明转换上这个端口被占了，描述网络端口占用，非进程通信端口
                    SourcePO sourcePO = new SourcePO();
                    sourcePO.setProtocolType(ProtocolType.RTMP);
                    sourcePO.setUrl("rtmp://" + transformModule.getDeviceIp() + ":1935/live/live");
                    permission.getSourcePOs().add(sourcePO);
                }else if ("srt".equals(packageOutputType)){ //背包只能出caller模式
                    permission.setCapacityPort(newport);
                    SourcePO sourcePO = new SourcePO();
                    sourcePO.setProtocolType(ProtocolType.SRT_TS);
                    sourcePO.setUrl("srt://" + transformModule.getDeviceIp() + ":" + newport);
                    sourcePO.setSrtMode("caller");
                    permission.getSourcePOs().add(sourcePO);
                }
                capacityPermissionPortDao.save(permission);
            }else{
                throw new BaseException(StatusCode.ERROR,"bundle has exist, id: "+bundleId);
            }

            //GBE设置参数
            suiBusParam.setAudEncPara(audioEncParams);

//            suiBusParam.setProtocol(0);
//            suiBusParam.setUdp_send_enable(1);
//            suiBusParam.setUdp_net_select(OneOneFiveMParam.fromName(outInterface).getProtocal());
//            suiBusParam.setUdp_send_ip(permission.getCapacityIp());
//            suiBusParam.setUdp_send_port(permission.getCapacityPort());
            if ("rtmp".equals(packageOutputType)) {
                suiBusParam.setProtocol(ProtocolType.getPackageType(ProtocolType.RTMP));
                suiBusParam.setRtmp_enable(1);
                suiBusParam.setRtmp_net_select(OneOneFiveMParam.fromName(outInterface).getProtocal());
                suiBusParam.setRtmp_serverIp(permission.getCapacityIp());
                suiBusParam.setRtmp_serverPort(1935);
                suiBusParam.setRtmp_appName("live");
                suiBusParam.setRtmp_streamName("live");//appname/streamname
            }else if ("srt".equals(packageOutputType)){ //srt只支持单播，caller模式
                suiBusParam.setProtocol(ProtocolType.getPackageType(ProtocolType.SRT_TS));
                suiBusParam.setSrt_send_enable(1);
                suiBusParam.setSrt_net_select(OneOneFiveMParam.fromName(outInterface).getProtocal());
                suiBusParam.setSrt_send_ip(permission.getCapacityIp());
                suiBusParam.setSrt_send_port(permission.getCapacityPort().intValue());
                suiBusParam.setSrt_encrypt(0);
                suiBusParam.setSrt_timeout(0);//延时设置0
            }
            JSONObject params = new JSONObject();
            params.put("prog1bus",suiBusParam);

            HttpClient.post("http://" + bundlePO.getDeviceIp() + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.setSuiBus(params).toJSONString());
        //刷源方法,判断能否刷成功,顺带记录节目信息
 // todo 改成不刷表  MediaSourceBO sourceBO = new MediaSourceBO(suiBusParam);
 //           transformModuleService.refreshSourceAndSaveSourceInfo(permission.getId().toString(), transformModule.getDeviceIp(), sourceBO);

        }

    }

    /**处理close_bundle信息的5G类型信息
     * @throws Exception */
    private void processCloseBundle5GMsg(BundleBO bundle) throws Exception{

        String bundleId = bundle.getBundle_id();

        CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(bundleId);

        if(permission != null){
            String deviceIp = permission.getBundleIp();

            //GBE设置参数
            SuiBusParam suiBusParam = new SuiBusParam();
            suiBusParam.setOnoff(0);
            suiBusParam.setUdp_send_enable(0);

            JSONObject params = new JSONObject();
            params.put("prog1bus",suiBusParam);

            HttpAsyncClient.getInstance().httpAsyncPost("http://" + deviceIp + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.setSuiBus(params).toJSONString(), null, null);
            //TODO:解码参数--设备暂时不支持
            //停止转码任务
            if(!StringUtils.isEmpty(permission.getTaskId())){
                packageService.deleteTask(permission.getTaskId());
            }

            capacityPermissionPortDao.delete(permission);
        }

    }
}
