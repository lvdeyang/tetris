package com.suma.venus.resource.vo;


import org.springframework.beans.BeanUtils;

import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;

public class WorkNodeVO{
    
    private Long id;
    
    private String name;

	private String ip;
	
	private Integer port;
	
	private String type;
	
	private String onlineStatus;
	
	private String nodeUid;
	
	private String url;
	
	private String netUrl;
	
	private String monitorUrl;
	
	/**录制资源下载端口**/
	private Integer downloadPort;

	public static WorkNodeVO fromPO(WorkNodePO po){
	    WorkNodeVO vo = new WorkNodeVO();
	    BeanUtils.copyProperties(po, vo, "type","onlineStatus");
	    vo.setType(po.getType().toString());
	    if(null != po.getOnlineStatus()){
	        vo.setOnlineStatus(po.getOnlineStatus().toString());
	    }
	    return vo;
	}
	
	public WorkNodePO toPO() throws Exception{
	    WorkNodePO po = new WorkNodePO();
	    BeanUtils.copyProperties(this, po, "type","onlineStatus");
	    po.setType(NodeType.fromString(this.getType()));
	    return po;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getNodeUid() {
		return nodeUid;
	}

	public void setNodeUid(String nodeUid) {
		this.nodeUid = nodeUid;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getDownloadPort() {
		return downloadPort;
	}

	public void setDownloadPort(Integer downloadPort) {
		this.downloadPort = downloadPort;
	}

	public String getNetUrl() {
		return netUrl;
	}

	public void setNetUrl(String netUrl) {
		this.netUrl = netUrl;
	}

	public String getMonitorUrl() {
		return monitorUrl;
	}

	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}
	
}
