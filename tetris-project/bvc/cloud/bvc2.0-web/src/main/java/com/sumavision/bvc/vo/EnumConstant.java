package com.sumavision.bvc.vo;

public class EnumConstant {

	public enum DeviceType{
		sumaDevice("数码视讯设备"),
		zte_T502("中兴T502"),
		zte_T800("中兴T800"),
		polycom("polycom软终端"),
		openphone("openphone软终端"),
		jc_pad("军c pad"),
		car_3("III型车"),
		new_wireless("新研无线"),
		mwe860("MWE860无线单兵"),
		itl("ITL视频服务器"),
		sat("SAT视频服务器"),
		hksdk("海康SDK"),
		onvif_ipc("ONVIF-IPC"),
		dhsdk("大华SDK"),
		gb_28281("GB.28281终端"),
		hw_te50("华为TE50");
		
		private String typeName;
		
		private DeviceType(String typeName){
			this.typeName = typeName;
		}

		public String getTypeName() {
			return typeName;
		}	
		
		public static DeviceType queryDeviceTypeByName(String typeName){
			for(DeviceType deviceType:DeviceType.values()){
				if(deviceType.name().equals(typeName)){
					return deviceType;
				}
			}
			return null;
		}
	}
	
	public enum DeviceProtocol{
		SIP,
		ONVIF,
		H323,
		GB28281;
		
		public static DeviceProtocol queryProtocolByName(String protocolName){
			for(DeviceProtocol protocol:DeviceProtocol.values()){
				if(protocol.name().equals(protocolName)){
					return protocol;
				}
			}
			return null;
		}
	}
}
