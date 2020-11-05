package com.sumavision.signal.bvc.terminal;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 终端参数处理<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月23日 下午2:56:04
 */
public class TerminalParam {
	
	//分隔符
	private static final String SEPARATOR_LEVEL1_SPLIT = "<\\*1\\*>";
	private static final String SEPARATOR_LEVEL2_SPLIT = "<\\*2\\*>";
	private static final String SEPARATOR_LEVEL1 = "<*1*>";
	private static final String SEPARATOR_LEVEL2 = "<*2*>";
	
	//转码url
	public static final String CAPACITY_URL_SUFFIX = "/tetris-capacity/api/server/bvc";
	
	//5G背包 url
	public static final String FIVEG_URL_SUFFIX = "/goform/form_device";
	
	//jv220 url
	public static final String JV220_URL_SUFFIX = "/goform/form_data";
	
	//jv220 api
	public static final String JV220_API_SUFFIX = "/goform/bvc_api";
	
	//jv210心跳检测
	public static final String GET_HEART_BEAT_SUFFIX = "/goform/form1068?type=18&cmd=1";
	
	//获取s100网络-常规参数
	public static final String GET_S100_COMMON_SUFFIX = "/goform/formDevice?type=500&cmd=1&language=1&ran=0.8319381104618808";
	
	//获取jv210通话设置
	public static final String GET_JV210_CALLSETTING_SUFFIX = "/goform/form1068?type=10&cmd=1&_=1558683126560";
	
	//获取jv210解码设置
	public static final String GET_JV210_DECODEPARAM_SUFFIX = "/goform/form1068?type=17&cmd=1&_=1558768377573";
	
	//获取jv210编码设置
	public static final String GET_JV210_ENCODEPARAM_SUFFIX = "/goform/form1068?type=16&cmd=1&_=1558768378452";
	
	//设备s100网络-常规参数
	public static final String POST_S100_COMMON_SUFFIX = "/goform/formDevice";
	
    //设置jv210通话设置
	public static final String POST_JV210_CALLSETTING_SUFFIX = "/goform/form1068?type=10&cmd=2&funType=0";
	
	//设置jv210编码设置
	public static final String POST_JV210_ENCODEPARAM_SUFFIX = "/goform/form1068?type=16&cmd=2&funType=0";
	
	//设置jv210解码参数
	public static final String POST_JV210_DECODEPARAM_SUFFIX = "/goform/form1068?type=17&cmd=2&funType=0";
	
    //jv210通话参数
    public static String[][] TerminalCallingParam = {
    		{"3","1","1","0"},
    		{"0","0","13","2","3","3","2"},
    		{"2000","2000","0","0"},
    		{"0","0","1800","6865"},
    		{"0","0","0","0","0","0","0","0","0","10","24"},
    		{"3010","4010","4020"}};
    
    //jv210编码参数
	public static String[][] TerminalEncodeParam = {
			{"0","192.165.56.218","10038","192.165.56.218","10036","1","96","97"},
			{"0","192.165.56.218","10040","192.168.1.100","7000","0","96","8"},
			{"7515","3515","1920","1080","25","19","0","25"},
			{"3824","1757","1920","1080","25","12","0","25"},
			{"0","0","4","0","1","1","3"}};
	
	//jv210解码参数
	public static String[][] TerminalDecodeParam = {
			{"0","0","0","224.1.1.1","4010","3010","96","97"},
			{"0","0","0","224.1.1.1","4020","7000","96","8"},
			{"1","13"},
			{"0","2","1","0"}};
	
	//s100网络常规参数
	public static String[][] S100CommonParam = {
			{"","","","","","","","",""},
			{"","","","","","","","",""},
			{"","","","","","","","",""},
			{"","","","","","","","",""},
			{"","","","","","","","",""},
			{"","","","","","","","",""},
			{"","","","","","","","",""},
			{"","","","","","","","",""},
			{""},
			{""},
			{""}};

	//获取jv210参数转为后台存储数据
	public static String[][] data2Array(String data, String[][] template){
		String[][] array = template;
		if(data != "" && data != null){
			String paramLevel1[] = data.split(SEPARATOR_LEVEL1_SPLIT);
			for(int i=0; i<paramLevel1.length; i++){
				String paramLevel2[] = paramLevel1[i].split(SEPARATOR_LEVEL2_SPLIT);
				for(int j=0; j<paramLevel2.length; j++){
					array[i][j] = paramLevel2[j];
				}
			}
		
		}
		
		return array;
	}
	
	//htmlString转为String
	public static String[][] html2Data(String html, String[][] template){
		
		String data = html.replaceAll("<html>", "").replaceAll("</html>", "");
		
		return data2Array(data, template);
	}
	
	//数组数据转为协议数据
	public static String array2Data(String[][] array){
		StringBufferWrapper sBuffer = new StringBufferWrapper();
		for(int i=0; i<array.length; i++){
			for(int j=0; j<array[i].length; j++){
				if(j == array[i].length-1){
					sBuffer.append(array[i][j]);
				}else{
					sBuffer.append(array[i][j]).append(SEPARATOR_LEVEL2);
				}
			}
			if(i != array.length-1){
				sBuffer.append(SEPARATOR_LEVEL1);
			}
		}
		
		return sBuffer.toString();
	}
	
//	public static void main(String[] args) throws Exception{
//		
//		String data = HttpClient.get("http://192.165.56.155" + GET_JV210_CALLSETTING_SUFFIX);
//		
//		String hh = data.replaceAll("<html>", "").replaceAll("</html>", "");
//
//		System.out.println(hh);
//		
//		System.out.println(array2Data(TerminalCallingParam));
//		
//	}
}

