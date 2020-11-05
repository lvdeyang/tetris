package com.sumavision.signal.bvc.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.signal.bvc.entity.dao.InternetAccessDAO;
import com.sumavision.signal.bvc.entity.dao.PortMappingDAO;
import com.sumavision.signal.bvc.entity.dao.RepeaterDAO;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.dao.TerminalBindRepeaterDAO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.entity.po.TerminalBindRepeaterPO;
import com.sumavision.signal.bvc.http.HttpAsyncClient;
import com.sumavision.signal.bvc.http.HttpClient;
import com.sumavision.signal.bvc.terminal.TerminalParam;

@Service
@Transactional(rollbackFor = Exception.class)
public class RepeaterService {

	@Autowired
	private RepeaterDAO repeaterDao;
	
	@Autowired
	private InternetAccessDAO internetAccessDao;
	
	@Autowired
	private TerminalBindRepeaterDAO terminalBindRepeaterDao;
	
	@Autowired
	private PortMappingDAO portMappingDao;
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private TaskExecuteService taskExecuteService;
	
	/**
	 * 删除流转发器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午3:59:44
	 * @param Long id 流转发器id
	 * @throws Exception 
	 */
	public void removeRepeater(Long id) throws Exception{
		
		RepeaterPO repeater = repeaterDao.findOne(id);
		List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findByRepeaterId(id);
		
		List<String> bundleIds = new ArrayList<String>();
		for(TerminalBindRepeaterPO bind: binds){
			bundleIds.add(bind.getBundleId());
		}
		
		List<TaskPO> tasks = taskDao.findByIp(repeater.getIp());
		
		if(tasks != null && tasks.size() > 0 && repeater != null){
			taskExecuteService.resetDevice(repeater);
		}
		
		taskDao.deleteByIp(repeater.getIp());
		portMappingDao.deleteBySrcBundleIdInOrDstBundleIdIn(bundleIds, bundleIds);
		terminalBindRepeaterDao.deleteByRepeaterId(id);
		internetAccessDao.deleteByRepeaterId(id);
		repeaterDao.delete(id);
		
	}
	
	/**
	 * 删除网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午10:02:11
	 * @param Long id
	 */
	public void removeAccess(Long id){
		
		portMappingDao.deleteBySrcAccessIdOrDstAccessId(id, id);
		terminalBindRepeaterDao.deleteByAccessId(id);
		internetAccessDao.delete(id);
		
	}
	
	/**
	 * 网口控制<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月10日 下午3:59:08
	 * @param repeater
	 * @param open
	 * @throws Exception
	 */
	public void gbeControl(RepeaterPO repeater, boolean open) throws Exception{
		
		//编码设置请求
    	String commons = HttpClient.get("http://" + repeater.getAddress() + TerminalParam.GET_S100_COMMON_SUFFIX);
    	String[][] commonsParam = TerminalParam.html2Data(commons, TerminalParam.S100CommonParam);
    	
    	if(open){
    		commonsParam[0][7] = "1";
    		commonsParam[1][7] = "1";
    		commonsParam[2][7] = "1";
    		commonsParam[3][7] = "1";
    		commonsParam[4][7] = "1";
    		commonsParam[5][7] = "1";
    		commonsParam[6][7] = "1";
    		commonsParam[7][7] = "1";
    	}else{
    		commonsParam[0][7] = "0";
    		commonsParam[1][7] = "0";
    		commonsParam[2][7] = "0";
    		commonsParam[3][7] = "0";
    		commonsParam[4][7] = "0";
    		commonsParam[5][7] = "0";
    		commonsParam[6][7] = "0";
    		commonsParam[7][7] = "0";
		}
    	
    	List<BasicNameValuePair> commonsBody = new ArrayList<BasicNameValuePair>();
    	BasicNameValuePair commonPair1 = new BasicNameValuePair("type", "500");
    	BasicNameValuePair commonPair2 = new BasicNameValuePair("cmd", "2");
    	BasicNameValuePair commonPair3 = new BasicNameValuePair("language", "1");
		BasicNameValuePair commonPair4 = new BasicNameValuePair("setString", TerminalParam.array2Data(commonsParam));
		commonsBody.add(commonPair1);
		commonsBody.add(commonPair2);
		commonsBody.add(commonPair3);
		commonsBody.add(commonPair4);
		HttpAsyncClient.getInstance().formPost("http://" + repeater.getAddress() + TerminalParam.POST_S100_COMMON_SUFFIX, null, commonsBody, null);
		
    	
	}
	
}
