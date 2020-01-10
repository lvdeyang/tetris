package com.suma.application.ldap.contants;

public interface LdapContants {

	//objectclass类型
	public static final String OBJECT_DEPARTMENT = "departInfo";
	public static final String OBJECT_USER = "userInfo";
	public static final String OBJECT_EQUIP = "equipInfo";
	
	//组织根节点隶属关系
	public static final String DEPARTROOTRELATION = "1001";
	public static final String SUPER = "10001";
	//ldap系统设备类型
	public static final String EQUIP_ENCODE = "2";
	public static final String EQUIP_DECODE = "3";
	
    public final static String DEFAULT_NODE_UUID = "f9ec9048025840e4a427ab8dc8475652";
    public final static String DEFAULT_FACT_UUID = "SUMA";
}
