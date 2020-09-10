package com.suma.venus.resource.bo;

//记录权限状态变化信息的BO

public class PrivilegeStatusBO {
	
	public static final String OPR_ADD = "add";
	public static final String OPR_EDIT = "edit";
	public static final String OPR_REMOVE = "remove";

	//泛指资源的ID
	private String code;
	
	//之前是否具有读（录制）权限
	private boolean prevCanRead = false;
	
	//之前是否具有写（点播）权限
	private boolean prevCanWrite = false;
	
	//之前是否具有云台权限
	private boolean prevCanCloud = false;
	
	//之前是否具有呼叫权限
	private boolean prevCanHJ = false;
	
	//之前是否具有指挥权限
	private boolean prevCanZK = false;
	
	//之前是否具有会议权限
	private boolean prevCanHY = false;
	
	//现在是否有录制权限
	private boolean nowCanRead = false;
	
	//现在是否有点播权限
	private boolean nowCanWrite = false;
	
	//现在是否有点播权限
	private boolean nowCanCloud = false;
	
	//现在是或否有呼叫权限
	private boolean nowCanHJ = false;
	
	//现在是否具有指挥权限
	private boolean nowCanZK = false;
	
	//现在是否具有会议权限
	private boolean nowCanHY = false;
	
	public PrivilegeStatusBO() {}
	
	public PrivilegeStatusBO(String code) {
		this.code = code;
	}
	
	public String getDevOprType(){
		if(!prevCanRead && !prevCanWrite){
			//之前的权限为空，则为add
			return OPR_ADD;
		}else if(!nowCanRead && !nowCanWrite && !nowCanCloud){
			return OPR_REMOVE;
		} else {
			return OPR_EDIT;
		}
	}
	
	public String getUserOprType(){
		if(!prevCanRead && !prevCanWrite && !prevCanCloud && !prevCanHJ && !prevCanZK && !prevCanHY){
			//之前的权限为空，则为add
			return OPR_ADD;
		}else if(!nowCanRead && !nowCanWrite && !nowCanCloud && !nowCanHJ && !nowCanZK && !nowCanHY){
			return OPR_REMOVE;
		} else {
			return OPR_EDIT;
		}
	}
	
	/**用户权限16位，后8位为保留位，前八位从左到右分别代表点播、呼叫、ZH、会议、录制、回 放、下载、控制，每一位中1代表具备该权限，0代表不具备该权限**/
	public String getUserAuthCode(){
		StringBuilder sBuilder = new StringBuilder();
		if(nowCanWrite){//点播置位
			sBuilder.append("1");
		}else{
			sBuilder.append("0");
		}
		if(nowCanHJ){//呼叫置位
			sBuilder.append("1");
		}else {
			sBuilder.append("0");
		}
		if(nowCanZK){//指挥置位
			sBuilder.append("1");
		}else {
			sBuilder.append("0");
		}
		if(nowCanHY){//指挥置位
			sBuilder.append("1");
		}else {
			sBuilder.append("0");
		}
//		if(nowCanWrite){//ZH、会议置位
//		}else{
//			sBuilder.append("00");
//		}
		if(nowCanRead){//录制置位
			sBuilder.append("1");
		}else{
			sBuilder.append("0");
		}
		if(nowCanCloud){//云台置位
			sBuilder.append("1");
		}else{
			sBuilder.append("0");
		}
		sBuilder.append("1100000000");
		
		return sBuilder.toString();
	}
	
	/**设备权限16位，后10位为保留位，前六位从左到右分别代表点播、录制、回放、下载、控制、 画面调节，每一位中1代表具备该权限，0代表不具备该权限**/
	public String getDevAuthCode(){
		StringBuilder sBuilder = new StringBuilder();
		if(nowCanWrite){//点播置位
			sBuilder.append("1");
		}else{
			sBuilder.append("0");
		}
		if(nowCanRead){//录制置位
			sBuilder.append("1");
		}else{
			sBuilder.append("0");
		}
		if(nowCanRead){//云台置位
			sBuilder.append("1");
		}else{
			sBuilder.append("0");
		}
		sBuilder.append("1110000000000");
		
		return sBuilder.toString();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isPrevCanRead() {
		return prevCanRead;
	}

	public void setPrevCanRead(boolean prevCanRead) {
		this.prevCanRead = prevCanRead;
	}

	public boolean isPrevCanWrite() {
		return prevCanWrite;
	}

	public void setPrevCanWrite(boolean prevCanWrite) {
		this.prevCanWrite = prevCanWrite;
	}

	public boolean isPrevCanCloud() {
		return prevCanCloud;
	}

	public void setPrevCanCloud(boolean prevCanCloud) {
		this.prevCanCloud = prevCanCloud;
	}

	public boolean isPrevCanHJ() {
		return prevCanHJ;
	}

	public void setPrevCanHJ(boolean prevCanHJ) {
		this.prevCanHJ = prevCanHJ;
	}

	public boolean isNowCanRead() {
		return nowCanRead;
	}

	public void setNowCanRead(boolean nowCanRead) {
		this.nowCanRead = nowCanRead;
	}

	public boolean isNowCanWrite() {
		return nowCanWrite;
	}

	public void setNowCanWrite(boolean nowCanWrite) {
		this.nowCanWrite = nowCanWrite;
	}
	
	public boolean isNowCanCloud() {
		return nowCanCloud;
	}

	public void setNowCanCloud(boolean nowCanCloud) {
		this.nowCanCloud = nowCanCloud;
	}

	public boolean isNowCanHJ() {
		return nowCanHJ;
	}

	public void setNowCanHJ(boolean nowCanHJ) {
		this.nowCanHJ = nowCanHJ;
	}

	public boolean isPrevCanZK() {
		return prevCanZK;
	}

	public void setPrevCanZK(boolean prevCanZK) {
		this.prevCanZK = prevCanZK;
	}

	public boolean isNowCanZK() {
		return nowCanZK;
	}

	public void setNowCanZK(boolean nowCanZK) {
		this.nowCanZK = nowCanZK;
	}

	public boolean isPrevCanHY() {
		return prevCanHY;
	}

	public void setPrevCanHY(boolean prevCanHY) {
		this.prevCanHY = prevCanHY;
	}

	public boolean isNowCanHY() {
		return nowCanHY;
	}

	public void setNowCanHY(boolean nowCanHY) {
		this.nowCanHY = nowCanHY;
	}
	
}
