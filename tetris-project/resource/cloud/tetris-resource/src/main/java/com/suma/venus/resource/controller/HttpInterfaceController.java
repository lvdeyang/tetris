package com.suma.venus.resource.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.AccessToken;
import com.suma.venus.resource.base.bo.BatchLockBundleParam;
import com.suma.venus.resource.base.bo.BatchLockBundleRespBody;
import com.suma.venus.resource.base.bo.BatchLockBundleRespParam;
import com.suma.venus.resource.base.bo.BundleCertifyResponseBody;
import com.suma.venus.resource.base.bo.BundleInfo;
import com.suma.venus.resource.base.bo.BundleOnlineRespParam;
import com.suma.venus.resource.base.bo.BusinessLayerBody;
import com.suma.venus.resource.base.bo.CreateResourceRespParam;
import com.suma.venus.resource.base.bo.DeleteResourceRespParam;
import com.suma.venus.resource.base.bo.GetTokenResultBO;
import com.suma.venus.resource.base.bo.LockBundleParam;
import com.suma.venus.resource.base.bo.LockBundleRespParam;
import com.suma.venus.resource.base.bo.LockChannelRespParam;
import com.suma.venus.resource.base.bo.ReleaseChannelRespParam;
import com.suma.venus.resource.base.bo.ReleaseChannelResponseBody;
import com.suma.venus.resource.base.bo.ResultBO;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.SetUserAuthBO;
import com.suma.venus.resource.base.bo.TokenResultBO;
import com.suma.venus.resource.base.bo.UnbindResouceBO;
import com.suma.venus.resource.base.bo.UnbindUserPrivilegeBO;
import com.suma.venus.resource.base.bo.UserAndResourceIdBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.bo.CreateBundleRequest;
import com.suma.venus.resource.bo.DeleteBundleRequest;
import com.suma.venus.resource.bo.DeleteBundleResponse;
import com.suma.venus.resource.bo.PrivilegeStatusBO;
import com.suma.venus.resource.bo.mq.BundleOfflineRequest;
import com.suma.venus.resource.bo.mq.BundleOfflineResponse;
import com.suma.venus.resource.bo.mq.BundleOnlineRequest;
import com.suma.venus.resource.bo.mq.BundleOnlineResp;
import com.suma.venus.resource.bo.mq.CreateResourceRequest;
import com.suma.venus.resource.bo.mq.DeleteResourceRequest;
import com.suma.venus.resource.bo.mq.DeviceBundleCertifyRequest;
import com.suma.venus.resource.bo.mq.DeviceBundleCertifyResp;
import com.suma.venus.resource.bo.mq.GetBundleInfoRequest;
import com.suma.venus.resource.bo.mq.LayerHeartBeatRequest;
import com.suma.venus.resource.bo.mq.LayerHeartBeatResponse;
import com.suma.venus.resource.bo.mq.LockChannelRequest;
import com.suma.venus.resource.bo.mq.ReleaseChannelRequest;
import com.suma.venus.resource.bo.mq.UserBundleCertifyRequest;
import com.suma.venus.resource.bo.mq.UserBundleCertifyResp;
import com.suma.venus.resource.constant.ErrorCode;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.BundleLoginBlackListDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.LockBundleParamDao;
import com.suma.venus.resource.dao.LockChannelParamDao;
import com.suma.venus.resource.dao.LockScreenParamDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.dao.SerInfoDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.dao.VirtualResourceDao;
import com.suma.venus.resource.externalinterface.InterfaceToResource;
import com.suma.venus.resource.feign.TokenFeign;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.lianwang.auth.AuthNotifyXml;
import com.suma.venus.resource.lianwang.auth.DevAuthXml;
import com.suma.venus.resource.lianwang.auth.UserAuthXml;
import com.suma.venus.resource.lianwang.status.DeviceStatusXML;
import com.suma.venus.resource.lianwang.status.NotifyRouteLinkXml;
import com.suma.venus.resource.lianwang.status.NotifyUserDeviceXML;
import com.suma.venus.resource.lianwang.status.StatusXMLUtil;
import com.suma.venus.resource.lianwang.status.UserStatusXML;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.VirtualResourcePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.LianwangPassbyService;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.suma.venus.resource.service.UserQueryService;
import com.suma.venus.resource.service.WorkNodeService;
import com.suma.venus.resource.task.BundleHeartBeatService;
import com.suma.venus.resource.util.XMLBeanUtils;
import com.suma.venus.resource.vo.WsVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

/**
 * Http对外接口
 * 
 * @author lxw
 *
 */
@Controller
@RequestMapping("/api")
public class HttpInterfaceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpInterfaceController.class);

	@Autowired
	private InterfaceToResource interfaceToResource;

	@Autowired
	private UserQueryFeign userFeign;

	@Autowired
	private UserQueryService userQueryService;

	@Autowired
	private TokenFeign tokenFeign;

	@Autowired
	private BundleService bundleService;

	@Autowired
	private ChannelSchemeService channelSchemeService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ExtraInfoService extraInfoService;

	@Autowired
	private WorkNodeService workNodeService;

	@Autowired
	private BundleLoginBlackListDao bundleLoginBlackListDao;

	@Autowired
	private ChannelSchemeDao channelSchemeDao;

	@Autowired
	private LockChannelParamDao lockChannelParamDao;

	@Autowired
	private ScreenSchemeDao screenSchemeDao;

	@Autowired
	private LockBundleParamDao lockBundleParamDao;

	@Autowired
	private LockScreenParamDao lockScreenParamDao;

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private VirtualResourceDao virtualResourceDao;

	@Autowired
	private StatusXMLUtil statusXMLUtil;

	@Autowired
	private SerNodeDao serNodeDao;

	@Autowired
	private SerInfoDao serInfoDao;

	@Autowired
	private BundleHeartBeatService bundleHeartBeatService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private LianwangPassbyService lianwangPassbyService;

	// 业务使用方式：vod|meeting
	@Value("${businessMode:vod}")
	private String businessMode;

	private final String TOKEN_DEFAULT_APP = "Basic d2ViQXBwOndlYkFwcA==";

	private final String DEFAULT_BUSSINESS_LAYER_ID = "suma-bvc-business";

	// 记录分段notify消息的map
	private static ConcurrentHashMap<String, Map<Integer, String>> notifyXmlFragMap = new ConcurrentHashMap<>();

	@RequestMapping(method = RequestMethod.POST, value = "/lockChannel", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public LockChannelRespParam lockChannel(@RequestBody LockChannelRequest lockChannelRequest) {
		LOGGER.info("Lock Channel Request : " + JSONObject.toJSONString(lockChannelRequest));
		LockChannelRespParam resp = null;
		try {
			resp = interfaceToResource.lockChannel(lockChannelRequest.getLock_channel_request());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			// 失败响应
			resp = new LockChannelRespParam();
			if (String.valueOf(ErrorCode.CHANNEL_BUSY).equals(e.getMessage())) {
				resp.setLock_channel_response(new ReleaseChannelResponseBody(
						com.suma.venus.resource.base.bo.ResponseBody.FAIL, ErrorCode.CHANNEL_BUSY));
			} else {
				resp.setLock_channel_response(
						new ReleaseChannelResponseBody(com.suma.venus.resource.base.bo.ResponseBody.FAIL));
			}
		}

		LOGGER.info("Lock Channel Response : " + JSONObject.toJSONString(resp));
		return resp;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/releaseChannel", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ReleaseChannelRespParam releaseChannel(@RequestBody ReleaseChannelRequest releaseChannelRequest) {
		LOGGER.info("Release Channel Request : " + JSONObject.toJSONString(releaseChannelRequest));
		ReleaseChannelRespParam resp = null;
		try {
			resp = interfaceToResource.releaseChannel(releaseChannelRequest.getRelease_channel_request());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			// 失败响应
			resp = new ReleaseChannelRespParam();
			resp.setRelease_channel_response(
					new ReleaseChannelResponseBody(com.suma.venus.resource.base.bo.ResponseBody.FAIL));
		}

		LOGGER.info("Release Channel Response : " + JSONObject.toJSONString(resp));
		return resp;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/lockBundle", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public LockBundleRespParam lockBundle(@RequestBody LockBundleParam lockParam) {
		LOGGER.info("lock_bundle_request : " + JSONObject.toJSONString(lockParam));
		LockBundleRespParam resp = null;
		try {
			resp = interfaceToResource.lockBundle(lockParam);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			resp = new LockBundleRespParam();
			resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
		}

		LOGGER.info("lock_bundle_response : " + JSONObject.toJSONString(resp));
		return resp;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/batchLockBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	@Deprecated
	public BatchLockBundleRespParam batchLockBundle(@RequestBody BatchLockBundleParam batchLockBundleParam) {
		LOGGER.info("batch_lock_bundle_request : " + JSONObject.toJSONString(batchLockBundleParam));
		long startTime = System.currentTimeMillis();

		BatchLockBundleRespParam resp = new BatchLockBundleRespParam();

		// 批量锁定，MustLockAll=true 表示必须所有bundle锁定成功，才是成功，否则全部回滚，返回失败
		// MustLockAll=false 表示
		if (batchLockBundleParam.isMustLockAll()) {
			try {

				resp = interfaceToResource.batchlockBundle(batchLockBundleParam);

			} catch (Exception e) {
				e.printStackTrace();
				resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);

			}

		} else {
			LockBundleRespParam lockBundleRespParam = null;
			List<BatchLockBundleRespBody> batchLockBundleRespBodyList = new ArrayList<BatchLockBundleRespBody>();
			int successCnt = 0;

			for (LockBundleParam lockBundleParam : batchLockBundleParam.getBundles()) {

				try {
					lockBundleRespParam = interfaceToResource.lockBundle(lockBundleParam);

					BatchLockBundleRespBody lockBundleRespBody = new BatchLockBundleRespBody();
					lockBundleRespBody.setBundleId(lockBundleParam.getBundleId());
					lockBundleRespBody.setOperateResult(true);
					lockBundleRespBody.setOperate_count(lockBundleRespParam.getOperate_count());
					lockBundleRespBody.setOperate_index(lockBundleRespParam.getOperate_index());
					batchLockBundleRespBodyList.add(lockBundleRespBody);
					successCnt++;
				} catch (Exception e) {
					LOGGER.error(e.toString());
					BatchLockBundleRespBody lockBundleRespBody = new BatchLockBundleRespBody();
					lockBundleRespBody.setBundleId(lockBundleParam.getBundleId());
					lockBundleRespBody.setOperateResult(false);
					batchLockBundleRespBodyList.add(lockBundleRespBody);
				}
			}

			resp.setSuccessCnt(successCnt);
			resp.setOperateBundlesResult(batchLockBundleRespBodyList);
			if (successCnt == 0) {
				resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
			} else {
				resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
			}

		}

		LOGGER.info("lock_bundle_response : " + JSONObject.toJSONString(resp));
		LOGGER.info("lock_bundle cost time : " + (System.currentTimeMillis() - startTime));

		return resp;
	}

	// TODO
	@RequestMapping(method = RequestMethod.POST, value = "/batchLockBundleNew", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public BatchLockBundleRespParam batchLockBundleNew(@RequestBody BatchLockBundleParam batchLockBundleParam) {
		LOGGER.info("batch_lock_bundle_request : " + JSONObject.toJSONString(batchLockBundleParam));
		long startTime = System.currentTimeMillis();

		BatchLockBundleRespParam resp = null;

		// 批量锁定，MustLockAll=true 表示必须所有bundle锁定成功，才是成功，否则全部回滚，返回失败
		// MustLockAll=false 表示部分锁定也可

		try {
			resp = interfaceToResource.batchlockBundleNew(batchLockBundleParam);

		} catch (Exception e) {
			e.printStackTrace();
			resp = new BatchLockBundleRespParam();
			resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
		}

		LOGGER.info("lock_bundle_response new : " + JSONObject.toJSONString(resp));
		LOGGER.info("lock_bundle new cost time : " + (System.currentTimeMillis() - startTime));
		return resp;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/releaseBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public LockBundleRespParam releaseBundle(@RequestBody LockBundleParam releaseParam) {
		LOGGER.info("release_bundle_request : " + JSONObject.toJSONString(releaseParam));
		LockBundleRespParam resp = null;
		try {
			resp = interfaceToResource.releaseBundle(releaseParam, businessMode);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			resp = new LockBundleRespParam();
			resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
		}

		LOGGER.info("release_bundle_response : " + JSONObject.toJSONString(resp));
		return resp;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/batchReleaseBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	@Deprecated
	public BatchLockBundleRespParam batchReleaseBundle(@RequestBody BatchLockBundleParam batchReleaseParam) {
		LOGGER.info("release_bundle_request : " + JSONObject.toJSONString(batchReleaseParam));
		long startTime = System.currentTimeMillis();

		BatchLockBundleRespParam resp = new BatchLockBundleRespParam();

		if (batchReleaseParam.isMustLockAll()) {

			try {
				resp = interfaceToResource.batchReleaseBundle(batchReleaseParam, businessMode);
			} catch (Exception e) {
				e.printStackTrace();
				resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
			}

		} else {

			List<BatchLockBundleRespBody> batchReleaseBundleRespBodyList = new ArrayList<BatchLockBundleRespBody>();

			int successCnt = 0;

			LockBundleRespParam releaseBundleRespParam = null;

			for (LockBundleParam releaseBundleParam : batchReleaseParam.getBundles()) {
				try {
					releaseBundleRespParam = interfaceToResource.releaseBundle(releaseBundleParam, businessMode);

					BatchLockBundleRespBody releaseBundleRespBody = new BatchLockBundleRespBody();
					releaseBundleRespBody.setBundleId(releaseBundleParam.getBundleId());
					releaseBundleRespBody.setOperateResult(true);
					releaseBundleRespBody.setOperate_count(releaseBundleRespParam.getOperate_count());
					releaseBundleRespBody.setOperate_index(releaseBundleRespParam.getOperate_index());
					batchReleaseBundleRespBodyList.add(releaseBundleRespBody);
					successCnt++;

				} catch (Exception e) {
					LOGGER.error(e.toString());
					BatchLockBundleRespBody releaseBundleRespBody = new BatchLockBundleRespBody();
					releaseBundleRespBody.setBundleId(releaseBundleParam.getBundleId());
					releaseBundleRespBody.setOperateResult(false);
					batchReleaseBundleRespBodyList.add(releaseBundleRespBody);
				}
			}

			resp.setSuccessCnt(successCnt);
			resp.setOperateBundlesResult(batchReleaseBundleRespBodyList);
			if (successCnt == 0) {
				resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
			} else {
				resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
			}
		}

		LOGGER.info("release_bundle_response : " + JSONObject.toJSONString(resp));
		LOGGER.info("release_bundle cost time : " + (System.currentTimeMillis() - startTime));

		return resp;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/batchReleaseBundleNew", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public BatchLockBundleRespParam batchReleaseBundleNew(@RequestBody BatchLockBundleParam batchReleaseParam) {
		LOGGER.info("release_bundle_request : " + JSONObject.toJSONString(batchReleaseParam));
		long startTime = System.currentTimeMillis();

		BatchLockBundleRespParam resp = null;

		try {

			resp = interfaceToResource.batchReleaseBundleNew(batchReleaseParam, businessMode);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resp = new BatchLockBundleRespParam();
			resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
		}

		LOGGER.info("release_bundle_response new : " + JSONObject.toJSONString(resp));
		LOGGER.info("release_bundle new cost time : " + (System.currentTimeMillis() - startTime));

		return resp;
	}

	// TODO:不知道哪里用到了
	@RequestMapping(method = RequestMethod.POST, value = "/createResource", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public CreateResourceRespParam createResource(@RequestBody CreateResourceRequest createResourceRequest) {
		CreateResourceRespParam resp = null;
		try {
			resp = interfaceToResource.createResource(createResourceRequest.getCreate_resource_request());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			// 失败响应
			resp = new CreateResourceRespParam();
			resp.setCreate_resource_response(null);
		}
		return resp;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteResource", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public DeleteResourceRespParam deleteResource(@RequestBody DeleteResourceRequest deleteResourceRequest) {
		DeleteResourceRespParam resp = null;
		try {
			resp = interfaceToResource.deleteResource(deleteResourceRequest.getDelete_resource_request());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			// 失败响应
			resp = new DeleteResourceRespParam();
			if (String.valueOf(ErrorCode.WITHOUT_PERMISSION).equals(e.getMessage())) {
				resp.setDelete_resource_response(createFailRespBody(ErrorCode.WITHOUT_PERMISSION));
			} else {
				resp.setDelete_resource_response(null);
			}
		}
		return resp;
	}

	/** 对外接口：创建bundle */
	@RequestMapping(method = RequestMethod.POST, value = "/createBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> createBundle(@RequestBody CreateBundleRequest createBundleRequest) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			data.put("create_bundle_response", interfaceToResource.createBundle(createBundleRequest));
		} catch (Exception e) {
			LOGGER.error("Fail to create bundle by request : " + JSONObject.toJSONString(createBundleRequest), e);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("result", com.suma.venus.resource.base.bo.ResponseBody.FAIL);
			data.put("create_bundle_response", result);
		}
		return data;
	}

	/** 对外接口：删除bundle */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public DeleteBundleResponse deleteBundle(@RequestBody DeleteBundleRequest request) {
		DeleteBundleResponse response = new DeleteBundleResponse();
		try {
			String bundleId = request.getDelete_bundle_request().getBundle_id();
			BundlePO bundle = bundleService.findByBundleId(bundleId);

			if (null != bundle.getUsername() && !bundle.getUsername().isEmpty()) {
				try {
					ResultBO deleteVirtualUserResult = userFeign.delVirtualUser(bundle.getUsername());
					if (!deleteVirtualUserResult.isResult()) {
						LOGGER.error("Communication success but Fail to delete user of bundle; username = "
								+ bundle.getUsername());
					}
				} catch (Exception e) {
					LOGGER.error(
							"Communication Error : Fail to delete user of bundle; username = " + bundle.getUsername(),
							e);
					response.setDelete_bundle_response(new com.suma.venus.resource.base.bo.ResponseBody(
							com.suma.venus.resource.base.bo.ResponseBody.FAIL));
					return response;
				}

			}

			bundleService.delete(bundle);

			// 删除extraInfo
			extraInfoService.deleteByBundleId(bundleId);

			// 删除配置能力
			channelSchemeService.deleteByBundleId(bundleId);
			lockChannelParamDao.deleteByBundleId(bundleId);

			// 删除屏配置信息
			screenSchemeDao.deleteByBundleId(bundleId);
			lockScreenParamDao.deleteByBundleId(bundleId);

			lockBundleParamDao.deleteByBundleId(bundleId);

			// 删除设备账号对应的黑名单
			bundleLoginBlackListDao.deleteByLoginId(bundle.getCurrentLoginId());

			response.setDelete_bundle_response(new com.suma.venus.resource.base.bo.ResponseBody(
					com.suma.venus.resource.base.bo.ResponseBody.SUCCESS));
		} catch (Exception e) {
			LOGGER.error("Fail to delete bundle by request : " + JSONObject.toJSONString(request));
			response.setDelete_bundle_response(new com.suma.venus.resource.base.bo.ResponseBody(
					com.suma.venus.resource.base.bo.ResponseBody.FAIL));
		}

		return response;
	}

	/** 为用户设置直播/点播资源权限 **/
	@Deprecated
	@RequestMapping(method = RequestMethod.POST, value = "/setUserAuthByUsernames", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResultBO setUserAuthByUsernames(@RequestBody SetUserAuthBO request) {
		LOGGER.info("Set User Auth by usernames request : " + JSONObject.toJSONString(request));
		ResultBO result = new ResultBO();
		if (null == request || (null == request.getCid() && null == request.getPid())) {
			result.setErrMsg("请求参数无效");
			return result;
		}

		try {
			if (null == request.getUsernames() || request.getUsernames().isEmpty()) {
				/** 删除该直播/点播资源权限 */
				String resourceId = null;
				if (null != request.getCid()) {
					VirtualResourcePO resource = virtualResourceDao.findTopByAttrNameAndAttrValue("cid",
							request.getCid());
					if (null == resource) {
						result.setErrMsg("不存在该直播资源");
						return result;
					}
					resourceId = resource.getResourceId();
				} else {
					VirtualResourcePO resource = virtualResourceDao.findTopByAttrNameAndAttrValue("pid",
							request.getPid());
					if (null == resource) {
						result.setErrMsg("不存在该点播资源");
						return result;
					}
					resourceId = resource.getResourceId();
				}
				ResultBO delAuthResult = userFeign.delbindPrivilege(resourceId);
				if (null == delAuthResult || !delAuthResult.isResult()) {
					result.setErrMsg("删除资源权限失败");
					return result;
				}
				result.setResult(true);
			} else {
				/** 为用户设置直播/点播资源权限 */
				String resourceId = null;
				if (null != request.getCid()) {
					VirtualResourcePO resource = virtualResourceDao.findTopByAttrNameAndAttrValue("cid",
							request.getCid());
					if (null == resource) {
						result.setErrMsg("不存在该直播资源");
						return result;
					}
					resourceId = resource.getResourceId();
				} else {
					VirtualResourcePO resource = virtualResourceDao.findTopByAttrNameAndAttrValue("pid",
							request.getPid());
					if (null == resource) {
						result.setErrMsg("不存在该点播资源");
						return result;
					}
					resourceId = resource.getResourceId();
				}

				for (String username : request.getUsernames()) {
//					Map<String, UserBO> userMap = userFeign.queryUserInfo(username);
					UserBO userBO = userQueryService.queryUserByUserName(username);

//					if (null == userMap || userMap.isEmpty() || null == userMap.get("user")) {
					if (userBO == null) {
						result.setErrMsg("用户名" + username + "不存在");
						return result;
					}
//					Long userId = userMap.get("user").getId();
					Long userId = userBO.getId();
					UserAndResourceIdBO userAndResourceId = new UserAndResourceIdBO();
					userAndResourceId.setUserId(userId);
					List<String> resourceCodes = new ArrayList<String>();
					resourceCodes.add(resourceId);
					userAndResourceId.setResourceCodes(resourceCodes);
					ResultBO bindResult = userFeign.bindUserPrivilege(userAndResourceId);
					if (null == bindResult || !bindResult.isResult()) {
						result.setErrMsg("用户 " + username + " 设置权限失败");
						return result;
					}
				}
				result.setResult(true);
			}
		} catch (Exception e) {
			LOGGER.error("Fail to set user auth by usernames", e);
			result.setErrMsg("未知异常");
		}

		LOGGER.info("Set User Auth by usernames response : " + JSONObject.toJSONString(result));
		return result;
	}

	/**
	 * 删除直播频道
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteLiveChannel", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResultBO deleteLiveChannel(@RequestBody SetUserAuthBO request) {
		LOGGER.info("Delete live channel request : " + JSONObject.toJSONString(request));
		ResultBO result = new ResultBO();
		if (null == request || null == request.getCid()) {
			result.setErrMsg("请求参数无效");
			return result;
		}

		try {
			List<VirtualResourcePO> resources = virtualResourceDao.findByAttrNameAndAttrValue("cid", request.getCid());
			for (VirtualResourcePO virtualResource : resources) {
				virtualResourceDao.deleteByResourceId(virtualResource.getResourceId());
			}
			result.setResult(true);
		} catch (Exception e) {
			LOGGER.error("Fail to delete live channel", e);
			result.setErrMsg("未知异常");
		}

		return result;
	}

	// TODO：重写（监听用户事件重写）
	/** 添加用户接口，资源层创建对应的10几个个播放器 **/
	@RequestMapping(method = RequestMethod.POST, value = "/createUserBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResultBO createUserBundle(@RequestParam(value = "username") String username,
			@RequestParam(value = "userNo") String userNo, @RequestParam(value = "password") String password,
			@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "accessLayerId", required = false) String accessLayerId) {
		LOGGER.info("Create User Bundle Request with username:" + username + " and password:" + password
				+ " and roleId:" + roleId);
		ResultBO result = new ResultBO();
		try {
			List<BundlePO> bundlePOs = new ArrayList<BundlePO>();
			List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
			List<String> bundleIds = new ArrayList<String>();
			// 创建17个播放器资源
			for (int i = 1; i <= 17; i++) {
				BundlePO bundlePO = new BundlePO();
				bundlePO.setBundleName(username + "_" + i);
				bundlePO.setUsername(userNo + "_" + i);
				bundlePO.setOnlinePassword(password);
				bundlePO.setBundleId(BundlePO.createBundleId());
				bundlePO.setDeviceModel("jv210");
				bundlePO.setBundleType("VenusTerminal");
				bundlePO.setBundleAlias("播放器");
				bundlePO.setBundleNum(userNo + "_" + i);
				if (!StringUtils.isEmpty(accessLayerId)) {
					bundlePO.setAccessNodeUid(accessLayerId);
				}
				// 默认上线
				bundlePO.setOnlineStatus(ONLINE_STATUS.ONLINE);

				bundlePOs.add(bundlePO);
				bundleIds.add(bundlePO.getBundleId());

				// 配置两路解码通道(音频解码和视频解码各一路)
				channelSchemePOs.addAll(channelSchemeService.createAudioAndVideoDecodeChannel(bundlePO));

				// 把第17个播放器的bundleId返回用户
				if (i == 17) {
					result.setInfo(bundlePO.getBundleId());
				}
			}

			// 绑定用户和资源的对应权限
			RoleAndResourceIdBO roleAndResourceIdBO = new RoleAndResourceIdBO();
			roleAndResourceIdBO.setRoleId(roleId);
			roleAndResourceIdBO.setResourceCodes(bundleIds);
			ResultBO bindResult = userFeign.bindRolePrivilege(roleAndResourceIdBO);
			if (null == bindResult || !bindResult.isResult()) {
				result.setResult(false);
				result.setErrMsg("通信错误");
				return result;
			}

			// 保存数据库
			bundleDao.save(bundlePOs);
			channelSchemeDao.save(channelSchemePOs);

			result.setResult(true);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			result.setResult(false);
			result.setErrMsg("资源层内部错误");
		}

		LOGGER.info("Create User Bundle Response: " + result.isResult());
		return result;
	}

	/** 删除用户接口，资源层删除对应的十几个播放器 **/
	@RequestMapping(method = RequestMethod.POST, value = "/deleteUserBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResultBO deleteUserBundle(@RequestParam(value = "userNo") String userNo) {
		ResultBO result = new ResultBO();
		try {
			for (int i = 1; i <= 17; i++) {
				BundlePO bundlePO = bundleService.findByUsername(userNo + "_" + i);
				if (null == bundlePO) {
					continue;
				}

				// 删除bundle锁定参数
				lockBundleParamDao.deleteByBundleId(bundlePO.getBundleId());
				// 删除通道配置信息
				lockChannelParamDao.deleteByBundleId(bundlePO.getBundleId());
				channelSchemeService.deleteByBundleId(bundlePO.getBundleId());
				// 删除extraInfo
				extraInfoService.deleteByBundleId(bundlePO.getBundleId());
				// 删除bundle
				bundleDao.delete(bundlePO);

				// 删除屏配置信息
				// screenSchemeDao.deleteByBundleId(bundlePO.getBundleId());
				// lockScreenParamDao.deleteByBundleId(bundlePO.getBundleId());
				// 删除设备账号对应的黑名单
				// bundleLoginBlackListDao.deleteByLoginId(bundlePO.getCurrentLoginId());
			}

			result.setResult(true);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			result.setResult(false);
			result.setErrMsg("资源层内部错误");
		}

		LOGGER.info("Delete User Bundle of userNo --> " + userNo + " ; response: " + result.isResult());
		return result;
	}

	/** 批量重置bundle */
	@RequestMapping(method = RequestMethod.POST, value = "/batchClearBundles", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> batchClearBundles(@RequestBody List<String> bundleIdList) {
		LOGGER.info("batch clear Bundles Request : " + JSONObject.toJSONString(bundleIdList));

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			// 事务控制一下
			resourceService.batchClearBundles(bundleIdList);

		} catch (Exception e) {
			LOGGER.error("Fail to clear batch bundles : ", e);
			result.put("errMsg", "操作失败");
		}

		return result;
	}

	/** 处理bundle_online_request消息 */
	@RequestMapping(method = RequestMethod.POST, value = "/thirdpart/bundleOnline", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public BundleOnlineResp bundleOnline(@RequestBody BundleOnlineRequest request) {
		LOGGER.info("Bundle online request : " + JSONObject.toJSONString(request));

		String bundleId = request.getBundle_online_request().getBundle_id();
		BundleOnlineResp resp = new BundleOnlineResp();
		BundleOnlineRespParam respBody = new BundleOnlineRespParam();
		respBody.setBundle_id(bundleId);
		resp.setBundle_online_response(respBody);
		try {
			BundlePO po = bundleService.findByBundleId(bundleId);
			if (null == po) {
				respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
				return resp;
			}

			boolean status_change = (ONLINE_STATUS.ONLINE != po.getOnlineStatus()) ? true : false;

			if (SOURCE_TYPE.SYSTEM == po.getSourceType()) {
				po.setOnlineStatus(ONLINE_STATUS.ONLINE);
			}
			po.setAccessNodeUid(request.getBundle_online_request().getLayer_id());
			bundleService.save(po);

			if (status_change && po.getSourceType() != SOURCE_TYPE.EXTERNAL && po.getSyncStatus() == SYNC_STATUS.SYNC) {
				// 发送状态改变notify增量消息
				String connectCenterLayerID = resourceRemoteService.queryLocalLayerId();
				List<BundlePO> bundles = new ArrayList<BundlePO>();
				bundles.add(po);
				statusXMLUtil.sendResourcesXmlMessage(null, bundles, null, connectCenterLayerID, 1500);
			}

			// 恢复离线告警
			// AlarmOprlogClientService.getInstance().recoverAlarm(AlarmCodeConstant.BUNDLE_OFFLINE_CODE,
			// bundleId, null, new Date());

			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
		}

		return resp;
	}

	/** 处理bundle_offline_notify */
	@RequestMapping(method = RequestMethod.POST, value = "/thirdpart/bundleOffline", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public BundleOfflineResponse bundleOffline(@RequestBody BundleOfflineRequest request) {
		String bundleId = request.getBundle_offline_request().getBundle_id();
		BundleOfflineResponse resp = new BundleOfflineResponse();
		BundleOnlineRespParam respBody = new BundleOnlineRespParam();
		respBody.setBundle_id(bundleId);
		resp.setBundle_offline_response(respBody);
		try {
			BundlePO po = bundleService.findByBundleId(bundleId);

			if (null != po && po.getOnlineStatus() != ONLINE_STATUS.OFFLINE) {
				if (SOURCE_TYPE.SYSTEM == po.getSourceType()) {
					po.setOnlineStatus(ONLINE_STATUS.OFFLINE);
				}
				bundleService.save(po);

				if (po.getSourceType() != SOURCE_TYPE.EXTERNAL && po.getSyncStatus() == SYNC_STATUS.SYNC) {
					// 发送状态改变notify增量消息
					String connectCenterLayerID = resourceRemoteService.queryLocalLayerId();
					List<BundlePO> bundles = new ArrayList<BundlePO>();
					bundles.add(po);
					statusXMLUtil.sendResourcesXmlMessage(null, bundles, null, connectCenterLayerID, 1500);
				}
			}

			// 发送告警消息
			// AlarmOprlogClientService.getInstance().triggerAlarm(AlarmCodeConstant.BUNDLE_OFFLINE_CODE,
			// bundleId, null, new Date());

			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
		}
		return resp;
	}

	/** 处理设备特征bundle认证请求 */
	@RequestMapping(method = RequestMethod.POST, value = "/thirdpart/deviceBundleCertify", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public DeviceBundleCertifyResp deviceBundleCertify(@RequestBody DeviceBundleCertifyRequest request) {
		LOGGER.info("Device Bundle Certify Request : " + JSONObject.toJSONString(request));
		String username = request.getDevice_bundle_certify_request().getUsername();
		String password = request.getDevice_bundle_certify_request().getPassword();
		String password_type = request.getDevice_bundle_certify_request().getPassword_type();
		String certify_id = request.getDevice_bundle_certify_request().getSerial_num();
		DeviceBundleCertifyResp resp = new DeviceBundleCertifyResp();
		BundleCertifyResponseBody respBody = new BundleCertifyResponseBody();
		respBody.setUsername(username);
		resp.setDevice_bundle_certify_response(respBody);
		try {
			// 判断certify_id是否有效；在黑名单中则无效
			if (null != certify_id && null != bundleLoginBlackListDao.findByLoginId(certify_id)) {
				LOGGER.error("Forbidden login by certify_id : " + certify_id);
				respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
				respBody.setError_message("该设备登陆凭证已被加入黑名单");
				return resp;
			}

			BundlePO bundle = bundleDao.findByUsername(username);
			if (null == bundle) {
				LOGGER.error("Fail to check certify device bundle , 账号对应的设备不存在 ：" + username);
				respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
				respBody.setError_message("账号对应的设备不存在");
			}

			// 校验用户名密码
			if (!SOURCE_TYPE.EXTERNAL.equals(bundle.getSourceType())) {//ldap的设备不进行用户微服务鉴权
			//if (!"播放器".equals(bundle.getBundleAlias()) && !SOURCE_TYPE.EXTERNAL.equals(bundle.getSourceType())) {// 播放器bundle和ldap的设备不进行用户微服务鉴权
				boolean check = bundleService.checkBundleAndPassword(bundle, password);
				if (!check) {
					LOGGER.error("Fail to check username and password : 设备账号名或密码错误");
					respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
					respBody.setError_message("用户名或密码错误");
					return resp;
				}
				respBody.setUserId(bundle.getId());
			}

			/**
			 * //查询对应bundleId Map<String, String> bundleIdMap =
			 * userFeign.queryResourceId(username); if(null == bundleIdMap || null ==
			 * bundleIdMap.get("resourceId")){ LOGGER.error("Fail to get bundle of username
			 * :" + username);
			 * respBody.setResult(com.suma.venus.resource.bo.ResponseBody.FAIL);
			 * respBody.setError_message("该用户没有bundle资源"); return resp; }
			 * 
			 * //更新bundle上当前登陆的设备账号标识ID String bundleId = bundleIdMap.get("resourceId");
			 * BundlePO bundle = bundleService.findByBundleId(bundleId);
			 **/
			bundle.setCurrentLoginId(certify_id);
			bundleService.save(bundle);

			respBody.setBundle_id(bundle.getBundleId());
			List<BusinessLayerBody> businessLayers = new ArrayList<BusinessLayerBody>();
			// TODO 当前是填充默认约定值
			businessLayers.add(new BusinessLayerBody(DEFAULT_BUSSINESS_LAYER_ID, "bvc"));
			respBody.setBusiness_layers(businessLayers);
			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
			respBody.setError_message("系统错误");
		}
		LOGGER.info("Device Bundle Certify Response : " + JSONObject.toJSONString(resp));
		return resp;
	}
	
	/** 获取用户下唯一机顶盒 */
	@RequestMapping(method = RequestMethod.POST, value = "/tvos/access/getUserBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public UserBundleCertifyResp getUserBundle(@RequestBody UserBundleCertifyRequest request) throws Exception{
		
		UserBO userBO = userQueryService.current();
		
		String username = request.getUser_bundle_certify_request().getUsername();
		String password = request.getUser_bundle_certify_request().getPassword();
		String bundleModel = request.getUser_bundle_certify_request().getBundle_model();
		String bundleType = request.getUser_bundle_certify_request().getBundle_type();
		String certify_id = request.getUser_bundle_certify_request().getSerial_num();
		UserBundleCertifyResp resp = new UserBundleCertifyResp();
		BundleCertifyResponseBody respBody = new BundleCertifyResponseBody();
		respBody.setUsername(username);
		resp.setUser_bundle_certify_response(respBody);
		
		BundlePO bundle = bundleDao.findByUserIdAndDeviceModel(userBO.getId(), "tvos");
		
		// 更新bundle上当前登陆的设备账号标识ID
		bundle.setCurrentLoginId(certify_id);
		
		String bundleId = bundle.getBundleId();

		String ca_num = request.getUser_bundle_certify_request().getCa_num();
		saveUserBundleExtraInfo(bundleId, "ca_num", ca_num);
		saveUserBundleExtraInfo(bundleId, "serial_num", certify_id);

		respBody.setBundle_id(bundleId);
		/** bundle_extra_info */
		List<ExtraInfoPO> extraInfos = extraInfoService.findByBundleId(bundleId);
		JSONObject bundleExtraInfoJson = new JSONObject();
		if (!extraInfos.isEmpty()) {
			for (ExtraInfoPO extraInfoPO : extraInfos) {
				bundleExtraInfoJson.put(extraInfoPO.getName(), extraInfoPO.getValue());
			}
		}
		//自动选择tvos接入层
		WorkNodePO choseWorkNode = null;
		if(bundle.getAccessNodeUid() != null){
			choseWorkNode = workNodeService.findByNodeUid(bundle.getAccessNodeUid());
		}else{
			List<WorkNodePO> tvosLayers = workNodeService.findByType(NodeType.ACCESS_TVOS);
			choseWorkNode = workNodeService.choseWorkNode(tvosLayers);
		}
		
		if(choseWorkNode != null){
			bundleExtraInfoJson.put("access_ip", choseWorkNode.getIp());
			bundleExtraInfoJson.put("access_port", choseWorkNode.getPort());
			bundle.setAccessNodeUid(choseWorkNode.getNodeUid());
		}else{
			bundleExtraInfoJson.put("access_ip", "");
			bundleExtraInfoJson.put("access_port", "");
		}
		
		bundleService.save(bundle);
		
		respBody.setBundle_extra_info(bundleExtraInfoJson.toJSONString());
		//respBody.setUserId(bundle.getId());
		respBody.setUserId(userBO.getUser().getId());
		// respBody.setUser_extra_info(checkResult.getExtraInfo());
		respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		
		return resp;
	}

	/** 处理用户特征bundle认证请求 */
	@RequestMapping(method = RequestMethod.POST, value = "/access/userBundleCertify", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public UserBundleCertifyResp userBundleCertify(@RequestBody UserBundleCertifyRequest request) {
		LOGGER.info("User Bundle Certify Request : " + JSONObject.toJSONString(request));
		String username = request.getUser_bundle_certify_request().getUsername();
		String password = request.getUser_bundle_certify_request().getPassword();
		String bundleModel = request.getUser_bundle_certify_request().getBundle_model();
		String bundleType = request.getUser_bundle_certify_request().getBundle_type();
		String certify_id = request.getUser_bundle_certify_request().getSerial_num();
		UserBundleCertifyResp resp = new UserBundleCertifyResp();
		BundleCertifyResponseBody respBody = new BundleCertifyResponseBody();
		respBody.setUsername(username);
		resp.setUser_bundle_certify_response(respBody);
		try {
			// 判断certify_id是否有效；在黑名单中则无效
			if (null != certify_id && null != bundleLoginBlackListDao.findByLoginId(certify_id)) {
				LOGGER.error("Forbidden login by certify_id : " + certify_id);
				respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
				respBody.setError_message("该设备登陆凭证已被加入黑名单");
				return resp;
			}

			// 查询对应bundleId
			// Map<String, String> bundleIdMap =
			// userFeign.queryResourceId(username);
			// if(null == bundleIdMap || null == bundleIdMap.get("resourceId")){
			// LOGGER.error("Fail to get bundle of username :" + username);
			// respBody.setResult(com.suma.venus.resource.bo.ResponseBody.FAIL);
			// respBody.setError_message("该用户没有bundle资源");
			// return resp;
			// }
			// String bundleId = bundleIdMap.get("resourceId");
			// BundlePO bundle = bundleService.findByBundleId(bundleId);

			BundlePO bundle = bundleService.findByUsername(username);
			// 校验用户名密码
			boolean check = bundleService.checkBundleAndPassword(bundle, password);
			if (!check) {
				LOGGER.error("Fail to check username and password : 设备账号名或密码错误");
				respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
				respBody.setError_message("用户名或密码错误");
				return resp;
			}

			String bundleId = bundle.getBundleId();
			/** 更新bundle信息 */
			if (!bundleModel.equalsIgnoreCase(bundle.getDeviceModel())) {
				bundle.setBundleType(bundleType);
				bundle.setDeviceModel(bundleModel);
				bundleService.save(bundle);

				// 更新bundle能力配置
				channelSchemeDao.deleteByBundleId(bundleId);
				lockChannelParamDao.deleteByBundleId(bundleId);

				// 删除屏配置信息
				screenSchemeDao.deleteByBundleId(bundleId);
				lockScreenParamDao.deleteByBundleId(bundleId);

				lockBundleParamDao.deleteByBundleId(bundleId);

				bundleService.configDefaultAbility(bundle);
			}

			// 更新bundle上当前登陆的设备账号标识ID
			bundle.setCurrentLoginId(certify_id);
			bundleService.save(bundle);

			String ca_num = request.getUser_bundle_certify_request().getCa_num();
			saveUserBundleExtraInfo(bundleId, "ca_num", ca_num);
			saveUserBundleExtraInfo(bundleId, "serial_num", certify_id);

			respBody.setBundle_id(bundleId);
			/** bundle_extra_info */
			List<ExtraInfoPO> extraInfos = extraInfoService.findByBundleId(bundleId);
			if (!extraInfos.isEmpty()) {
				JSONObject bundleExtraInfoJson = new JSONObject();
				for (ExtraInfoPO extraInfoPO : extraInfos) {
					bundleExtraInfoJson.put(extraInfoPO.getName(), extraInfoPO.getValue());
				}
				respBody.setBundle_extra_info(bundleExtraInfoJson.toJSONString());
			}

			List<BusinessLayerBody> businessLayers = new ArrayList<BusinessLayerBody>();
			// TODO 当前是填充默认约定值
			businessLayers.add(new BusinessLayerBody(DEFAULT_BUSSINESS_LAYER_ID, "bvc"));
			respBody.setBusiness_layers(businessLayers);
			respBody.setUserId(bundle.getId());
			// respBody.setUser_extra_info(checkResult.getExtraInfo());
			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
			respBody.setError_message("系统错误");
		}
		LOGGER.info("User Bundle Certify Response : " + JSONObject.toJSONString(resp));
		return resp;
	}

	private void saveUserBundleExtraInfo(String bundleId, String key, String value) {
		if (null != value) {
			ExtraInfoPO ca_num_extra_info = extraInfoService.findByBundleIdAndName(bundleId, key);
			if (null == ca_num_extra_info) {
				ca_num_extra_info = new ExtraInfoPO();
				ca_num_extra_info.setName(key);
				ca_num_extra_info.setBundleId(bundleId);
				ca_num_extra_info.setValue(value);
				extraInfoService.save(ca_num_extra_info);
			} else if (!value.equals(ca_num_extra_info.getValue())) {
				ca_num_extra_info.setValue(value);
				extraInfoService.save(ca_num_extra_info);
			}
		}
	}

	/** 处理收到的获取bundle整体信息的请求消息 */
	@RequestMapping(method = RequestMethod.POST, value = "/thirdpart/getBundleInfo", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> getBundleInfo(@RequestBody GetBundleInfoRequest request) {
		LOGGER.info("Get Bundle Info Request : " + JSONObject.toJSONString(request));

		Map<String, Object> result = new HashMap<String, Object>();

		BundleInfo bundleInfo = resourceService.getBundleInfo(request.getGet_bundle_info_request().getBundle_id());
		if (null != bundleInfo) {
			result.put("get_bundle_info_response", bundleInfo);
		} else {
			result.put("get_bundle_info_response", new BundleInfo());
		}

		// LOGGER.info("Get Bundle Info response : " +
		// JSONObject.toJSONString(result));
		return result;
	}

	/** 处理收到的获取bundle整体信息的请求消息 */
	@RequestMapping(method = RequestMethod.POST, value = "/access/getBatchBundleInfos", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> getBatchBundleInfos(@RequestBody List<String> bundleIdList) {
		LOGGER.info("Get batch BundleInfos Request : " + JSONObject.toJSONString(bundleIdList));

		Map<String, Object> result = new HashMap<String, Object>();

		List<BundleInfo> bundleInfoList = resourceService.getBatchBundleInfos(bundleIdList);

		result.put("get_bundle_info_response", bundleInfoList);

		LOGGER.info("Get Bundle Info response : " + JSONObject.toJSONString(result));
		return result;
	}

	/** 接入层心跳 */
	@RequestMapping(method = RequestMethod.POST, value = "/thirdpart/layerHeartbeat", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public LayerHeartBeatResponse layerHeartBeat(@RequestBody LayerHeartBeatRequest request) {
		// LOGGER.info("Layer heartbeat Request : " +
		// JSONObject.toJSONString(request));

		LayerHeartBeatResponse resp = new LayerHeartBeatResponse();
		com.suma.venus.resource.base.bo.ResponseBody respBody = new com.suma.venus.resource.base.bo.ResponseBody();
		resp.setLayer_heartbeat_response(respBody);
		try {
			String layerId = request.getLayer_heartbeat_request().getLayer_id();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			WorkNodePO layerNode = workNodeService.findByNodeUid(layerId);
			if (null != layerNode) {
				layerNode.setOnlineStatus(ONLINE_STATUS.ONLINE);
				layerNode.setLastHeartBeatTime(sdf.format(new Date()));
				workNodeService.save(layerNode);

				respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			respBody.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
		}

		return resp;
	}

	/**
	 * 通过用户名和密码请求获取token
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/auth/access_token", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public GetTokenResultBO loginAndGetToken(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password) {
		LOGGER.info("User login request--> username is " + username + " and password is " + password);
		GetTokenResultBO result = new GetTokenResultBO();
		try {
			AccessToken accessToken = tokenFeign.getToken(TOKEN_DEFAULT_APP, username, password, "password");
			if (null == accessToken) {
				LOGGER.error("Fail to login and get access_token by user " + username);
				result.setStatus(TokenResultBO.CODE_PASSWORD_WRONG);
				result.setErrorMessage("密码错误");
				return result;
			}

			LOGGER.info("User " + username + " get access_token : " + JSONObject.toJSONString(accessToken));
			result.setStatus(TokenResultBO.CODE_SUCCESS);
			result.setAccess_token(accessToken);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			result.setStatus(TokenResultBO.CODE_PASSWORD_WRONG);
			result.setErrorMessage("密码错误");
		}

		return result;
	}

	/**
	 * 退出登录并清除token
	 * 
	 * @param access_token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/auth/logout", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public TokenResultBO logoutAndClearToken(@RequestParam(value = "access_token") String access_token) {
		LOGGER.info("User logout with token : " + access_token);
		TokenResultBO result = new TokenResultBO();
		try {
			ResultBO feignResult = tokenFeign.clearToken(TOKEN_DEFAULT_APP, access_token);
			if (!feignResult.isResult()) {
				LOGGER.error("Fail to cleart access_token : " + access_token);
				result.setStatus(TokenResultBO.CODE_TOKEN_INVALID);
				result.setErrorMessage("token无效");
				return result;
			}
			LOGGER.info("Logout success : " + access_token);
			result.setStatus(TokenResultBO.CODE_SUCCESS);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			result.setStatus(TokenResultBO.CODE_TOKEN_INVALID);
			result.setErrorMessage("token无效");
		}

		return result;
	}

	/***
	 * 根据token判断当前用户是否具有点播或直播资源的权限
	 * 
	 * @param access_token
	 * @param token_type
	 * @param pid          点播ID
	 * @param cid          直播ID
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/auth/assert", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public TokenResultBO checkAssertPrivilege(@RequestParam(value = "access_token") String access_token,
			@RequestParam(value = "token_type") String token_type,
			@RequestParam(value = "pid", required = false) String pid,
			@RequestParam(value = "cid", required = false) String cid) {
		TokenResultBO result = new TokenResultBO();
		try {
			VirtualResourcePO virtualResource = null;
			if (null != pid) {
				virtualResource = virtualResourceDao.findTopByAttrNameAndAttrValue("pid", pid);
			} else if (null != cid) {
				virtualResource = virtualResourceDao.findTopByAttrNameAndAttrValue("cid", cid);
			} else {
				LOGGER.error("Assert info is empty!");
				result.setStatus(TokenResultBO.CODE_NO_AUTH);
				result.setErrorMessage("无权限");
				return result;
			}

			if (null == virtualResource) {
				LOGGER.info("User with token:" + access_token + "has no auth of pid=" + pid + " or cid=" + cid
						+ ": this assert data not exist");
				result.setStatus(TokenResultBO.CODE_NO_AUTH);
				result.setErrorMessage("无权限");
				return result;
			}

			Map<String, UserBO> userMap = userFeign.getUserByToken(token_type + " " + access_token);
			if (null == userMap || userMap.isEmpty()) {
				LOGGER.info("User with token:" + access_token + " invalid");
				result.setStatus(TokenResultBO.CODE_TOKEN_INVALID);
				result.setErrorMessage("token无效");
				return result;
			}
			UserBO user = userMap.get("user");

			if (checkPrivilege(virtualResource.getResourceId(), user.getId())) {
				result.setStatus(TokenResultBO.CODE_SUCCESS);
				LOGGER.info("Check auth success of token:" + access_token + "and pid=" + pid + " or cid=" + cid);
			} else {
				result.setStatus(TokenResultBO.CODE_NO_AUTH);
				result.setErrorMessage("无权限");
				LOGGER.info("User with token:" + access_token + "has no auth of pid=" + pid + " or cid=" + cid
						+ ": no auth");
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			result.setStatus(TokenResultBO.CODE_NO_AUTH);
			result.setErrorMessage("无权限");
		}

		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/notifyUserAndDevice", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public JSONObject notifyUserAndDevice(@RequestBody JSONObject requestJson) {
		LOGGER.info("notifyUserAndDevice request : " + requestJson.toJSONString());

		JSONObject jsonResult = new JSONObject();
		try {
			// 获取联网发来的notify消息
			String contentType = requestJson.getString("content-type");
			String xml = requestJson.getString("resources");
			if (null == xml || xml.isEmpty()) {
				jsonResult.put("code", 200);
				jsonResult.put("msg", "ok");
				return jsonResult;
			}
			
			String connectCenterLayerID = resourceRemoteService.queryLocalLayerId();
			if (contentType.startsWith("application/command+xml+packet_")) {// 如果是分片包
				// 提取分片信息
				// seq表示消息内容序列号，len表示消息内容总长度，cur表示当前包号，max表示分包总数
				String fraginfo = contentType.split("_")[1];
				String seq = fraginfo.split("-")[0];
				// int len = Integer.parseInt(fraginfo.split("-")[1]);
				int cur = Integer.parseInt(fraginfo.split("-")[2]);
				int max = Integer.parseInt(fraginfo.split("-")[3]);
				if (!notifyXmlFragMap.containsKey(seq)) {
					notifyXmlFragMap.put(seq, new HashMap<Integer, String>());
				}
				notifyXmlFragMap.get(seq).put(cur, xml);
				if (notifyXmlFragMap.get(seq).size() == max) {// 收集齐所有的分包
					// 组装出来整体包
					StringBuilder sBuilder = new StringBuilder();
					for (int i = 1; i <= max; i++) {
						sBuilder.append(notifyXmlFragMap.get(seq).get(i));
					}
					// 得到完整的xml内容
					xml = sBuilder.toString();
					notifyXmlFragMap.remove(seq);
				} else { // 没收完最后一包不做处理，直接返回OK
					jsonResult.put("code", 200);
					jsonResult.put("msg", "ok");
					return jsonResult;
				}
			}

			try {
				NotifyUserDeviceXML xmlBean = XMLBeanUtils.xmlToBean(xml, NotifyUserDeviceXML.class);
				if ("syncinfo".equalsIgnoreCase(xmlBean.getCommandname())) {
					List<DeviceStatusXML> devlist = xmlBean.getDevlist();
					List<BundlePO> toUpdateBundles = new ArrayList<BundlePO>();
					// 更新设备在线状态
					if (null != devlist) {
						for (DeviceStatusXML dev : devlist) {
							BundlePO bundle = bundleDao.findByUsername(dev.getDevid());
							if (null != bundle) {
								ONLINE_STATUS status = dev.getStatus() == 1 ? ONLINE_STATUS.ONLINE
										: ONLINE_STATUS.OFFLINE;
								if (status != bundle.getOnlineStatus()) {
									bundle.setOnlineStatus(status);
									toUpdateBundles.add(bundle);
								}
							}
						}
					}
					if (!toUpdateBundles.isEmpty()) {
						bundleDao.save(toUpdateBundles);
					}

					// 更新对应用户的在线状态
					List<UserStatusXML> userlist = xmlBean.getUserlist();
					List<UserBO> userBOs = new ArrayList<UserBO>();
					if (null != userlist) {
						for (UserStatusXML userXml : userlist) {
							userBOs.add(statusXMLUtil.toUserBO(userXml));
						}
					}
					if (!userBOs.isEmpty()) {
						// 通知用户服务更新对应的用户在线状态
						JSONObject usersJson = new JSONObject();
						usersJson.put("users", userBOs);
						userFeign.notifyUserStatus(usersJson);
					}

					// 通过消息服务向联网发送本系统的用户设备状态信息
					List<BundlePO> localDevs = bundleDao.findSyncedLocalDevs();
					List<UserBO> localUserBOs = null;
					try {
						Map<String, List<UserBO>> userBOMap = userFeign.queryLocalUserStatus();
						if (null != userBOMap) {
							localUserBOs = userBOMap.get("users");
						}
					} catch (Exception e) {
						LOGGER.error("", e);
					}

					List<SerInfoPO> serInfoPOs = serInfoDao.findBySourceType(SOURCE_TYPE.SYSTEM);
					statusXMLUtil.sendAllResourcesXmlMessage(serInfoPOs, localDevs, localUserBOs, connectCenterLayerID,
							1500);
				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}

			try {
				NotifyRouteLinkXml xmlBean = XMLBeanUtils.xmlToBean(xml, NotifyRouteLinkXml.class);
				if ("syncroutelink".equalsIgnoreCase(xmlBean.getCommandname())) {
					// 发送路由信息
					// List<SerInfoPO> serInfoPOs = serInfoDao.findBySerType(5);
					List<SerNodePO> serNodePOs = serNodeDao.findAll();
					if (!serNodePOs.isEmpty()) {
						statusXMLUtil.sendResourcesXmlMessage(serNodePOs, connectCenterLayerID);
					}
				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}

			jsonResult.put("code", 200);
			jsonResult.put("msg", "ok");
		} catch (Exception e) {
			LOGGER.error("", e);
			jsonResult.put("code", 200);
			jsonResult.put("msg", "fail");
		}

		LOGGER.info("notifyUserAndDevice Response : " + jsonResult.toJSONString());
		return jsonResult;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/authnotify", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public JSONObject authnotify(@RequestBody JSONObject requestJson) {
		LOGGER.info("authnotify request : " + requestJson.toJSONString());

		JSONObject jsonResult = new JSONObject();
		try {
			// 获取联网发来的notify消息
			String xml = requestJson.getString("resources").toLowerCase();
			if (null == xml || xml.isEmpty()) {
				jsonResult.put("code", 200);
				jsonResult.put("msg", "ok");
				return jsonResult;
			}
			AuthNotifyXml authNotifyXml = XMLBeanUtils.xmlToBean(xml, AuthNotifyXml.class);
			String userNo = authNotifyXml.getUserid();
			// UserBO userBO = userFeign.queryUserInfoByUserNo(userNo).get("user");
			UserBO userBO = userQueryService.queryUserByUserNo(userNo);
			if (PrivilegeStatusBO.OPR_ADD.equals(authNotifyXml.getOperation())) {
				bindUserPrivilegeFromXml(authNotifyXml, userBO.getId());
			} else if (PrivilegeStatusBO.OPR_REMOVE.equals(authNotifyXml.getOperation())) {
				unbindUserPrivilegeFromXml(authNotifyXml, userBO.getId());
			} else if (PrivilegeStatusBO.OPR_EDIT.equals(authNotifyXml.getOperation())) {
				// TODO 修改第一步清除旧权限，第二部绑定新权限
				unbindUserPrivilegeFromXml(authNotifyXml, userBO.getId());
				bindUserPrivilegeFromXml(authNotifyXml, userBO.getId());
			}

			jsonResult.put("code", 200);
			jsonResult.put("msg", "ok");
		} catch (Exception e) {
			LOGGER.error("", e);
			jsonResult.put("code", 200);
			jsonResult.put("msg", "fail");
		}

		LOGGER.info("authnotify Response : " + jsonResult.toJSONString());
		return jsonResult;
	}

	private void bindUserPrivilegeFromXml(AuthNotifyXml authNotifyXml, Long userId) {
		UserAndResourceIdBO userAndResourceIdBO = new UserAndResourceIdBO();
		userAndResourceIdBO.setUserId(userId);
		for (DevAuthXml devAuthXml : authNotifyXml.getDevlist()) {
			String devId = devAuthXml.getDevid();
			BundlePO bundle = bundleDao.findByUsername(devId);
			if (null == bundle) {
				continue;
			}
			if ("1".equals(devAuthXml.getAuth().substring(0, 1))) {
				userAndResourceIdBO.getResourceCodes().add(bundle.getBundleId() + "-w");
			}
			if ("1".equals(devAuthXml.getAuth().substring(1, 2))) {
				userAndResourceIdBO.getResourceCodes().add(bundle.getBundleId() + "-r");
			}
		}
		for (UserAuthXml userAuthXml : authNotifyXml.getUserlist()) {
			String authUserNo = userAuthXml.getUserid();
			// UserBO authUserBO =
			// userFeign.queryUserInfoByUserNo(authUserNo).get("user");
			if ("1".equals(userAuthXml.getAuth().substring(0, 1))) {
				userAndResourceIdBO.getResourceCodes().add(authUserNo + "-w");
			}
			if ("1".equals(userAuthXml.getAuth().subSequence(1, 2))) {
				userAndResourceIdBO.getResourceCodes().add(authUserNo + "-hj");
			}
			if ("1".equals(userAuthXml.getAuth().subSequence(4, 5))) {
				userAndResourceIdBO.getResourceCodes().add(authUserNo + "-r");
			}
		}
		// 绑定权限
		userFeign.bindUserPrivilege(userAndResourceIdBO);
	}

	private void unbindUserPrivilegeFromXml(AuthNotifyXml authNotifyXml, Long userId) {
		UnbindUserPrivilegeBO unbindUserPrivilegeBO = new UnbindUserPrivilegeBO();
		unbindUserPrivilegeBO.setUserId(userId);
		for (DevAuthXml devAuthXml : authNotifyXml.getDevlist()) {
			String devId = devAuthXml.getDevid();
			BundlePO bundle = bundleDao.findByUsername(devId);
			if (null == bundle) {
				continue;
			}
			unbindUserPrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(bundle.getBundleId() + "-w", false));
			unbindUserPrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(bundle.getBundleId() + "-r", false));
		}
		for (UserAuthXml userAuthXml : authNotifyXml.getUserlist()) {
			String authUserNo = userAuthXml.getUserid();
			unbindUserPrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(authUserNo + "-w", false));
			unbindUserPrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(authUserNo + "-r", false));
			unbindUserPrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(authUserNo + "-hj", false));
		}
		userFeign.unbindUserPrivilege(unbindUserPrivilegeBO);
	}

	/** 接收bundle直接发过来的心跳 **/
	// TODO
	@RequestMapping(method = RequestMethod.GET, value = "/thirdpart/bundleHeartBeat")
	@ResponseBody
	public void receiveBundleHeartBeat(@RequestParam(value = "bundle_ip") String bundle_ip) {
		LOGGER.info("receive bundle heartBeat, bundle_ip=" + bundle_ip);

		String regexString = ".*(\\d{3}(\\.\\d{1,3}){3}).*";
		String IPString = bundle_ip.replaceAll(regexString, "$1");
		LOGGER.info("receive bundle heartBeat, IPString=" + IPString);

		bundleHeartBeatService.addBundleStatus(IPString, System.currentTimeMillis());
	}

	/** 为ldap用户创建一个编码器 */
	@RequestMapping(value = "/createEncoder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> createEncoder(@RequestBody JSONObject coderJson) {
		LOGGER.info("Create encoder of ldapUser : " + coderJson.toJSONString());
		Map<String, String> data = new HashMap<String, String>();
		try {
			String username = coderJson.getString("username");
			BundlePO existedBundle = bundleDao.findByUsername(username);
			if (null != existedBundle) {// 编码器器已存在，返回成功
				data.put("encoderId", existedBundle.getBundleId());
				data.put("msg", "ok");
				return data;
			}
			BundlePO bundle = new BundlePO();
			bundle.setBundleAlias("编码器");
			bundle.setDeviceModel("jv210");
			bundle.setBundleType("VenusTerminal");
			bundle.setBundleId(BundlePO.createBundleId());
			bundle.setUsername(username);
			bundle.setBundleName(username);
			bundle.setOnlinePassword(coderJson.getString("password"));
			bundle.setSourceType(SOURCE_TYPE.EXTERNAL);
			bundle.setSyncStatus(SYNC_STATUS.SYNC);
			// 配置编码通道
			// 配置两路编码通道(音频编码和视频编码各一路)
			channelSchemeDao.save(channelSchemeService.createAudioAndVideoEncodeChannel(bundle));
			bundleDao.save(bundle);

			// 给管理员默认角色绑定该设备权限
			// Map<String, Object> adminRoleResult =
			// userFeign.queryRoleByName("管理员默认角色");
			// RoleBO defaultAdminRole =
			// JSONObject.parseObject(JSONObject.toJSONString(adminRoleResult.get("role")),
			// RoleBO.class);
			// RoleAndResourceIdBO roleAndResourceIdBO = new
			// RoleAndResourceIdBO();
			// roleAndResourceIdBO.setRoleId(defaultAdminRole.getId());
			// roleAndResourceIdBO.setResourceCodes(new ArrayList<String>());
			// roleAndResourceIdBO.getResourceCodes().add(bundle.getBundleId() +
			// "-r");
			// roleAndResourceIdBO.getResourceCodes().add(bundle.getBundleId() +
			// "-w");
			// userFeign.bindRolePrivilege(roleAndResourceIdBO);

			data.put("msg", "ok");
			data.put("encoderId", bundle.getBundleId());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put("msg", "fail");
		}

		LOGGER.info("Create encoder of ldapUser return encoderId : " + data.get("encoderId"));
		return data;
	}

	/** 为ldap用户创建一个解码器，并给admin默认角色绑定该解码器权限 */
	@RequestMapping(value = "/createDecoder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> createDecoder(@RequestBody JSONObject coderJson) {
		LOGGER.info("Create decoder of ldapUser : " + coderJson.toJSONString());
		Map<String, String> data = new HashMap<String, String>();
		try {
			String username = coderJson.getString("username");
			BundlePO existedBundle = bundleDao.findByUsername(username);
			if (null != existedBundle) {// 解码器已存在，返回成功
				data.put("decoderId", existedBundle.getBundleId());
				data.put("msg", "ok");
				return data;
			}
			BundlePO bundle = new BundlePO();
			bundle.setBundleAlias("解码器");
			bundle.setDeviceModel("jv210");
			bundle.setBundleType("VenusTerminal");
			bundle.setBundleId(BundlePO.createBundleId());
			bundle.setUsername(username);
			bundle.setBundleName(username);
			bundle.setOnlinePassword(coderJson.getString("password"));
			bundle.setSourceType(SOURCE_TYPE.EXTERNAL);
			bundle.setSyncStatus(SYNC_STATUS.SYNC);
			// 配置解码通道
			// 配置两路解码通道(音频解码和视频解码各一路)
			channelSchemeDao.save(channelSchemeService.createAudioAndVideoDecodeChannel(bundle));
			bundleDao.save(bundle);

			// 给管理员默认角色绑定该设备权限
			// Map<String, Object> adminRoleResult =
			// userFeign.queryRoleByName("管理员默认角色");
			// RoleBO defaultAdminRole =
			// JSONObject.parseObject(JSONObject.toJSONString(adminRoleResult.get("role")),
			// RoleBO.class);
			// RoleAndResourceIdBO roleAndResourceIdBO = new
			// RoleAndResourceIdBO();
			// roleAndResourceIdBO.setRoleId(defaultAdminRole.getId());
			// roleAndResourceIdBO.setResourceCodes(new ArrayList<String>());
			// roleAndResourceIdBO.getResourceCodes().add(bundle.getBundleId() +
			// "-r");
			// roleAndResourceIdBO.getResourceCodes().add(bundle.getBundleId() +
			// "-w");
			// userFeign.bindRolePrivilege(roleAndResourceIdBO);

			data.put("msg", "ok");
			data.put("decoderId", bundle.getBundleId());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put("msg", "fail");
		}

		LOGGER.info("Create decoder of ldapUser return decoderId : " + data.get("decoderId"));
		return data;
	}

	@RequestMapping(value = "/userStatus", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userStatus(@RequestBody JSONObject userJson) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			UserBO userBO = JSONObject.parseObject(JSONObject.toJSONString(userJson.get("user")), UserBO.class);
			if (null != userBO) {
				// 通过消息队列发送用户状态改变的notify消息
				String connectCenterLayerID = resourceRemoteService.queryLocalLayerId();
				List<UserBO> localUserBOs = new ArrayList<UserBO>();
				localUserBOs.add(userBO);
				statusXMLUtil.sendResourcesXmlMessage(null, null, localUserBOs, connectCenterLayerID, 1500);
			}

			data.put("msg", "ok");
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put("msg", "fail");
		}

		return data;
	}

	/** 添加ldap用户接口，资源层创建对应的10几个个播放器 **/
	@RequestMapping(method = RequestMethod.POST, value = "/createLdapUserBundle", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResultBO createLdapUserBundle(@RequestParam(value = "username") String username,
			@RequestParam(value = "userNo") String userNo, @RequestParam(value = "password") String password,
			@RequestParam(value = "roleId") Long roleId) {
		LOGGER.info("Create LdapUser Bundle Request with username:" + username + " and password:" + password
				+ " and roleId:" + roleId);
		ResultBO result = new ResultBO();
		try {
			List<BundlePO> bundlePOs = new ArrayList<BundlePO>();
			List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
			List<String> bundleIds = new ArrayList<String>();
			String connectCenterLayerID = resourceRemoteService.queryLocalLayerId();
			// 创建17个播放器资源
			for (int i = 1; i <= 17; i++) {
				BundlePO bundlePO = new BundlePO();
				bundlePO.setBundleName(username + "_" + i);
				bundlePO.setUsername(userNo + "_" + i);
				bundlePO.setOnlinePassword(password);
				bundlePO.setBundleId(BundlePO.createBundleId());
				bundlePO.setDeviceModel("jv210");
				bundlePO.setBundleType("VenusTerminal");
				bundlePO.setBundleAlias("播放器");
				bundlePO.setBundleNum(userNo + "_" + i);
				bundlePO.setSourceType(SOURCE_TYPE.EXTERNAL);
				bundlePO.setSyncStatus(SYNC_STATUS.SYNC);
				// 接入layerID设置为联网接入ID
				bundlePO.setAccessNodeUid(connectCenterLayerID);
				// 默认上线
				bundlePO.setOnlineStatus(ONLINE_STATUS.ONLINE);

				bundlePOs.add(bundlePO);
				bundleIds.add(bundlePO.getBundleId());

				// 配置两路解码通道(音频解码和视频解码各一路)
				channelSchemePOs.addAll(channelSchemeService.createAudioAndVideoDecodeChannel(bundlePO));

				// 把第17个播放器的bundleId返回用户
				if (i == 17) {
					result.setInfo(bundlePO.getBundleId());
				}
			}

			// 绑定用户虚拟角色和资源的对应权限
			RoleAndResourceIdBO roleAndResourceIdBO = new RoleAndResourceIdBO();
			roleAndResourceIdBO.setRoleId(roleId);
			roleAndResourceIdBO.setResourceCodes(bundleIds);
			ResultBO bindResult = userFeign.bindRolePrivilege(roleAndResourceIdBO);
			if (null == bindResult || !bindResult.isResult()) {
				result.setResult(false);
				result.setErrMsg("通信错误");
				return result;
			}

			// 保存数据库
			bundleDao.save(bundlePOs);
			channelSchemeDao.save(channelSchemePOs);

			result.setResult(true);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			result.setResult(false);
			result.setErrMsg("资源层内部错误");
		}

		LOGGER.info("Create LdapUser Bundle Response: " + result.isResult());
		return result;
	}

	private boolean checkPrivilege(String resourceId, Long userId) throws Exception {
		UserAndResourceIdBO userIdAndResourceIds = new UserAndResourceIdBO();
		userIdAndResourceIds.setUserId(userId);
		userIdAndResourceIds.setResourceCodes(new ArrayList<String>());
		userIdAndResourceIds.getResourceCodes().add(resourceId);
//		UserPrivilegeBO privResult = userFeign.hasPrivilege(userIdAndResourceIds);
//		if (null != privResult && null != privResult.getPrivilegePermission() && !privResult.getPrivilegePermission().isEmpty()
//				&& privResult.getPrivilegePermission().get(0).isHasPrivilege()) {
//			return true;
//		}
//		return false;
		return userQueryService.hasPrivilege(userIdAndResourceIds);
	}

	private com.suma.venus.resource.base.bo.ResponseBody createFailRespBody(Integer errorCode) {
		return new com.suma.venus.resource.base.bo.ResponseBody(com.suma.venus.resource.base.bo.ResponseBody.FAIL,
				errorCode);
	}
	
	/**
	 * 存联网passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 上午9:26:13
	 * @param String uuid 业务uuid
	 * @param String layerId 联网layerId
	 * @param String type 业务类型
	 * @param String protocol passby
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/passby")
	public Object addPassby(
			String uuid, 
			String layerId, 
			String type, 
			String protocol,
			HttpServletRequest request) throws Exception{
		
		lianwangPassbyService.save(uuid, layerId, type, protocol);
		return null;
	}
	
	/**
	 * 存联网passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 上午9:26:13
	 * @param String uuid 业务uuid
	 * @param String layerId 联网layerId
	 * @param String type 业务类型
	 * @param String protocol passby
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/cover/passby")
	public Object addAndCoverPassby(
			String uuid, 
			String layerId, 
			String type, 
			String protocol,
			HttpServletRequest request) throws Exception{
		
		lianwangPassbyService.saveAndCover(uuid, layerId, type, protocol);
		return null;
	}
	
	/**
	 * 删除联网passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 上午9:48:59
	 * @param String uuid 业务uuid
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/passby")
	public Object removePassby(
			String uuid, 
			HttpServletRequest request) throws Exception{
		
		lianwangPassbyService.delete(uuid);
		return null;
	}
	
	/**
	 * 查询接入层下的ws设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 上午9:10:42
	 * @param String layerId 接入层id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/thirdpart/query/ws/bundles")
	public Object queryWsBundles(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		String layerId = requestWrapper.getString("layerId");
		
		List<BundlePO> bundles = bundleDao.findByDeviceModelAndAccessNodeUid("ws", layerId);
		List<WsVO> wsVOs = new ArrayList<WsVO>();
		for(BundlePO bundle: bundles){
			
			//1.软终端 2.编码设备 3.解码设备
			Integer codecType = channelSchemeService.getCoderDeviceType(bundle.getBundleId());
			WsVO ws = new WsVO();
			ws.setDeviceIp(bundle.getDeviceIp());
			if(codecType == 1){
				ws.setType("zhihui");
			}else if(codecType == 2){
				ws.setType("4U");
			}else if(codecType == 3){
				ws.setType("4U");
			}
			ws.setUsername(bundle.getUsername());
			ws.setPassword(bundle.getOnlinePassword());
			wsVOs.add(ws);
		}
		
		return wsVOs;
	}
	
}
