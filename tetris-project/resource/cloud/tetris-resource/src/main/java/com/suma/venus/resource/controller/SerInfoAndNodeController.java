package com.suma.venus.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.application.ldap.node.LdapNodeDao;
import com.suma.application.ldap.node.LdapNodePo;
import com.suma.application.ldap.ser.LdapSerInfoDao;
import com.suma.application.ldap.ser.LdapSerInfoPo;
import com.suma.venus.resource.dao.SerInfoDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.SerInfoPO.SerInfoType;
import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.service.LdapService;
import com.suma.venus.resource.util.SerInfoAndNodeSyncLdapUtils;
import com.suma.venus.resource.vo.SerInfoVO;
import com.suma.venus.resource.vo.SerNodeVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping("serInfo")
public class SerInfoAndNodeController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerInfoAndNodeController.class);

	@Autowired
	private SerInfoDao serInfoDao;

	@Autowired
	private SerNodeDao serNodeDao;

	@Autowired
	private LdapNodeDao ldapNodeDao;

	@Autowired
	private LdapSerInfoDao ldapSerInfoDao;

	@Autowired
	private SerInfoAndNodeSyncLdapUtils serInfoAndNodeSyncLdapUtils;
	
	@Autowired
	private LdapService ldapService;

	@RequestMapping("querySerInfo")
	@ResponseBody
	public Map<String, Object> querySerInfo() {

		Map<String, Object> data = makeAjaxData();

		try {

			// TODO 待补充， 分页
			List<SerInfoPO> pos = serInfoDao.findAll();

			List<SerInfoVO> vos = SerInfoVO.transFromPOs(pos);

			data.put(ERRMSG, "");
			data.put("serInfoVOs", vos);
		} catch (Exception e) {
			data.put(ERRMSG, "内部错误");
			LOGGER.error("query serInfo error, e=" + e.getStackTrace());
		}

		return data;
	}

	@RequestMapping("querySerNode")
	@ResponseBody
	public Map<String, Object> querySerNode() {

		Map<String, Object> data = makeAjaxData();

		try {
			List<SerNodePO> pos = serNodeDao.findAll();

			List<SerNodeVO> vos = SerNodeVO.transFromPOs(pos);

			data.put(ERRMSG, "");
			data.put("serNodeVOs", vos);
		} catch (Exception e) {
			data.put(ERRMSG, "内部错误");
			LOGGER.error("query serNode error, e=" + e.getStackTrace());
		}
		return data;
	}

	@RequestMapping("queryFatherNodeOptions")
	@ResponseBody
	public Map<String, Object> queryFatherNodeOptions() {

		Map<String, Object> data = makeAjaxData();

		try {
			List<SerNodePO> pos = serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
			
			// 修改为查出所有 临时版本
			// List<SerNodePO> pos = serNodeDao.findAll();
			
			SerNodePO local = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
			String fatherNodes = local.getNodeFather();
			String relationNodes = local.getNodeRelations();
			List<SerNodePO> needRemovePos = new ArrayList<SerNodePO>();
			
			for(SerNodePO po: pos){
				String father = po.getNodeFather();
				if(father != null && !father.equals("NULL")){
					String[] fathers = father.split(",");
					for(String str: fathers){
						if(str.equals(local.getNodeUuid())){
							needRemovePos.add(po);
							break;
						}
					}
				}
			}
			
			if(fatherNodes != null && !fatherNodes.equals("NULL")){
				String[] nodes = fatherNodes.split(",");
				for(String node: nodes){
					for(SerNodePO po: pos){
						if(po.getNodeUuid().equals(node)){
							needRemovePos.add(po);
							break;
						}
					}
				}
			}
			if(relationNodes != null && !relationNodes.equals("NULL")){
				String[] nodes = relationNodes.split(",");
				for(String node: nodes){
					for(SerNodePO po: pos){
						if(po.getNodeUuid().equals(node)){
							needRemovePos.add(po);
							break;
						}
					}
				}
			}
			
			pos.removeAll(needRemovePos);
			
			List<SerNodeVO> vos = SerNodeVO.transFromPOs(pos);

			data.put(ERRMSG, "");
			data.put("serNodeVOs", vos);
		} catch (Exception e) {
			data.put(ERRMSG, "内部错误");
			LOGGER.error("query serNode error, e=" + e.getStackTrace());
		}
		return data;
	}

	@RequestMapping("/addSerInfo")
	@ResponseBody
	public Map<String, Object> addSerInfo(@ModelAttribute SerInfoVO serInfoVO) {

		Map<String, Object> data = makeAjaxData();

		// TODO 参数检查
		if (serInfoVO == null) {
			data.put(ERRMSG, "参数错误");
			return data;
		}
		
		if(serInfoVO.getSerType().equals(SerInfoType.APPLICATION.getNum())){
			SerInfoPO serInfo = serInfoDao.findBySourceTypeAndSerType(SOURCE_TYPE.SYSTEM, SerInfoType.APPLICATION.getNum());
			if(serInfo != null){
				data.put(ERRMSG, "应用服务单元已存在！");
				return data;
			}
		}

		try {
			SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
			
			SerInfoPO serInfoPO = new SerInfoPO();
			BeanUtils.copyProperties(serInfoVO, serInfoPO);

			serInfoPO.setSerUuid(BundlePO.createBundleId());
			serInfoPO.setSerNode(self.getNodeUuid());
			serInfoPO.setSerFactInfo(self.getNodeFactInfo());
			serInfoPO.setSourceType(SOURCE_TYPE.SYSTEM);
			serInfoPO.setSyncStatus(SYNC_STATUS.ASYNC);

			serInfoDao.save(serInfoPO);
			data.put(ERRMSG, "");
		} catch (Exception e) {
			data.put("errMsg", "内部错误");
		}

		return data;
	}

	@RequestMapping("/modifySerInfo")
	@ResponseBody
	public Map<String, Object> modifySerInfo(@ModelAttribute SerInfoVO serInfoVO) {

		Map<String, Object> data = makeAjaxData();

		// TODO 参数检查
		if (serInfoVO == null || StringUtils.isEmpty(serInfoVO.getSerUuid())) {
			data.put(ERRMSG, "参数错误");
			return data;
		}

		try {

			SerInfoPO serInfoPO = serInfoDao.findTopBySerUuid(serInfoVO.getSerUuid());
			if (serInfoPO == null) {
				data.put(ERRMSG, "参数错误");
				return data;
			}

			BeanUtils.copyProperties(serInfoVO, serInfoPO, "serUuid", "syncStatus", "sourceType", "serNode", "serFactInfo");
			serInfoPO.setSyncStatus(SYNC_STATUS.ASYNC);
			serInfoDao.save(serInfoPO);
			data.put(ERRMSG, "");
		} catch (Exception e) {
			data.put("errMsg", "内部错误");
		}

		return data;
	}

	@RequestMapping("/addSerNode")
	@ResponseBody
	public Map<String, Object> addSerNode(@ModelAttribute SerNodeVO serNodeVO) {
		Map<String, Object> data = makeAjaxData();

		if (serNodeVO == null) {
			data.put(ERRMSG, "参数错误");
			return data;
		}

		try {
			
			List<SerNodePO> serNodes = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
			
			if (serNodes != null && serNodes.size() > 0) {
				data.put(ERRMSG, "不能再新增");
				return data;
			}

			SerNodePO serNodePO = new SerNodePO();
			BeanUtils.copyProperties(serNodeVO, serNodePO);
//			serNodePO.setNodeUuid(LdapContants.DEFAULT_NODE_UUID);
			
			serNodePO.setNodeUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			
			serNodePO.setNodeFather("NULL");
			serNodePO.setNodeRelations("NULL");
			serNodePO.setSourceType(SOURCE_TYPE.SYSTEM);
			serNodePO.setSyncStatus(SYNC_STATUS.ASYNC);
			serNodeDao.save(serNodePO);

			data.put(ERRMSG, "");

		} catch (Exception e) {
			data.put(ERRMSG, "内部错误");
		}

		return data;

	}

	@RequestMapping("/modifySerNode")
	@ResponseBody
	public Map<String, Object> modifySerNode(@ModelAttribute SerNodeVO serNodeVO) {
		Map<String, Object> data = makeAjaxData();

		// TODO 参数检查
		if (serNodeVO == null || StringUtils.isEmpty(serNodeVO.getNodeUuid())) {
			data.put(ERRMSG, "参数错误");
			return data;
		}

		try {

			SerNodePO serNodePOTemp = serNodeDao.findTopByNodeUuid(serNodeVO.getNodeUuid());

			 if (serNodePOTemp == null) {
				data.put(ERRMSG, "参数错误");
				return data;
			 }

			serNodePOTemp.setNodePublisher(serNodeVO.getNodePublisher());
			serNodePOTemp.setNodeName(serNodeVO.getNodeName());
			serNodePOTemp.setNodeFather(serNodeVO.getNodeFather().equals("")? "NULL": serNodeVO.getNodeFather());
			serNodePOTemp.setNodeRelations(serNodeVO.getNodeRelations().equals("")? "NULL": serNodeVO.getNodeRelations());
			serNodePOTemp.setNodeFactInfo(serNodeVO.getNodeFactInfo());
			serNodePOTemp.setSyncStatus(SYNC_STATUS.ASYNC);
			serNodeDao.save(serNodePOTemp);

			data.put(ERRMSG, "");

		} catch (Exception e) {
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping("/delSerInfo")
	@ResponseBody
	public Map<String, Object> delSerInfo(@RequestParam(value = "serUuid") String serUuid) {

		Map<String, Object> data = makeAjaxData();

		if (StringUtils.isEmpty(serUuid)) {
			data.put(ERRMSG, "参数错误");
			return data;
		}

		try {

			SerInfoPO serInfoPO = serInfoDao.findTopBySerUuid(serUuid);

			try {
				if (serInfoPO.getSourceType().equals(SOURCE_TYPE.SYSTEM)) {
					List<LdapSerInfoPo> ldapSerInfoPoList = ldapSerInfoDao.getSerInfoByUuid(serUuid);
					if (!CollectionUtils.isEmpty(ldapSerInfoPoList)) {
						ldapSerInfoDao.remove(ldapSerInfoPoList.get(0));
					}
				}

			} catch (Exception e) {
				LOGGER.error("delete serinfo @ ldap error");
			}

			serInfoDao.delete(serInfoPO);

		} catch (Exception e) {
			data.put(ERRMSG, "内部错误");
		}

		return data;

	}

	@RequestMapping("/delSerNode")
	@ResponseBody
	public Map<String, Object> delSerNode(@RequestParam(value = "nodeUuid") String nodeUuid) {
		Map<String, Object> data = makeAjaxData();

		if (StringUtils.isEmpty(nodeUuid)) {
			data.put(ERRMSG, "参数错误");
			return data;
		}

		try {
			
			List<SerNodePO> childSerNodeList = serNodeDao.findByNodeFather(nodeUuid);
			
			// System.out.println(JSON.toJSON(childSerNodeList));
			
			if(!CollectionUtils.isEmpty(childSerNodeList)) {
				data.put(ERRMSG, "此节点还有子节点存在，请先清理子节点");
				return data;
			}
			
						
			SerNodePO serNodePO = serNodeDao.findTopByNodeUuid(nodeUuid);

			if (serNodePO.getSourceType().equals(SOURCE_TYPE.SYSTEM) && serNodePO.getSyncStatus().equals(SYNC_STATUS.SYNC)) {
				List<LdapNodePo> ldapNodePoList = ldapNodeDao.getNodeInfoByUuid(nodeUuid);

				if (!CollectionUtils.isEmpty(ldapNodePoList)) {
					ldapNodeDao.remove(ldapNodePoList.get(0));
				}
			}

			serNodeDao.delete(serNodePO);

		} catch (Exception e) {
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping("syncSerNodeFromLdap")
	@ResponseBody
	public Map<String, Object> syncSerNodeFromLdap() {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("successCnt", serInfoAndNodeSyncLdapUtils.handleSyncSerNodeFromLdap());
		data.put(ERRMSG, "");
		return data;
	}

	@RequestMapping("syncSerNodeToLdap")
	@ResponseBody
	public Map<String, Object> syncSerNodeToLdap() throws Exception{

		Map<String, Object> data = new HashMap<String, Object>();
		
		try{
			int successCnt = serInfoAndNodeSyncLdapUtils.handleSyncSerNodeToLdap();
			data.put("successCnt", successCnt);
			data.put(ERRMSG, "");
		}catch(Exception e){
			data.put(ERRMSG, e.getMessage());
			LOGGER.error(e.getMessage(), e);
			
		}
		
		return data;

	}

	@RequestMapping("/syncSerInfoFromLdap")
	@ResponseBody
	public Map<String, Object> syncSerInfoFromLdap() {

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("successCnt", serInfoAndNodeSyncLdapUtils.handleSyncSerInfoFromLdap());
		data.put(ERRMSG, "");
		return data;

	}

	@RequestMapping("/syncSerInfoToLdap")
	@ResponseBody
	public Map<String, Object> syncSerInfoToLdap() {

		Map<String, Object> data = makeAjaxData();

		data.put("successCnt", serInfoAndNodeSyncLdapUtils.handleSyncSerInfoToLdap());
		data.put(ERRMSG, "");
		return data;
	}

	@RequestMapping("/cleanUpSerInfo")
	@ResponseBody
	public Map<String, Object> cleanUpSerInfo() {
		Map<String, Object> data = makeAjaxData();
		data.put(ERRMSG, serInfoAndNodeSyncLdapUtils.handleCleanUpSerInfo());
		return data;

	}

	@RequestMapping("/cleanUpSerNode")
	@ResponseBody
	public Map<String, Object> cleanUpSerNode() {
		Map<String, Object> data = makeAjaxData();
		data.put(ERRMSG, serInfoAndNodeSyncLdapUtils.handleCleanUpSerNode());
		return data;
	}
	
	/**
	 * LDAP数据备份<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月22日 下午3:14:17
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/ldap/backup")
	public Object ldapBackUp(HttpServletRequest request) throws Exception{
	
		ldapService.ldapBackup();
		
		return null;
	}
	
	/**
	 * LDAP数据恢复<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月22日 下午3:14:40
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/ldap/resume")
	public Object ldapResume(HttpServletRequest request) throws Exception{
	
		ldapService.ldapResume();
		
		return null;
	}

}
