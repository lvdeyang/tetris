package com.suma.venus.resource.constant;
/**
 * venus参数常量定义类
 * @author lxw
 *
 */
public class VenusParamConstant {
	
	public static final String PARAM_JSON_KEY_TYPE = "type";
	public static final String PARAM_JSON_KEY_CONSTRAINT = "constraint";
	public static final String PARAM_JSON_KEY_CHANNELS = "channels";
	public static final String PARAM_JSON_KEY_ELEMTYPE = "elem_type";
	public static final String PARAM_JSON_KEY_ELEMCONSTRAINT = "elem_constraint";
	public static final String PARAM_JSON_KEY_CHANNELVERSION = "channel_version";
	public static final String PARAM_JSON_KEY_CHANNELID = "channel_id";
	public static final String PARAM_JSON_KEY_CHANNELNAME = "channel_name";
	public static final String PARAM_JSON_KEY_CHANNELTYPE = "channel_type";
	public static final String PARAM_JSON_KEY_BASETYPE = "base_type";
	public static final String PARAM_JSON_KEY_EXTERNTYPE = "extern_type";
	public static final String PARAM_JSON_KEY_MAXCHANNELCNT = "max_channel_cnt";
	public static final String PARAM_JSON_KEY_CHANNELPARAM = "channel_param";
	public static final String PARAM_JSON_KEY_VALUES = "values";
	public static final String PARAM_JSON_KEY_CONSTTYPE = "const_type";
	public static final String PARAM_JSON_KEY_CONSTVALUE = "const_value";
	public static final String PARAM_JSON_KEY_MAXVALUE = "max_value";
	public static final String PARAM_JSON_KEY_MINVALUE = "min_value";
	public static final String PARAM_JSON_KEY_STEP = "step";
	public static final String PARAM_JSON_KEY_MAXLENGTH = "max_length";
	public static final String PARAM_JSON_KEY_MINLENGTH = "min_length";
	public static final String PARAM_JSON_KEY_DYNAMICCONSTRAINT = "dynamic_constraint";
	public static final String PARAM_JSON_KEY_ENUMCONSTRAINT = "enum_constraint";
	public static final String PARAM_JSON_KEY_WIDTH = "width";
	public static final String PARAM_JSON_KEY_HEIGHT = "height";
	
	/**
	 * 模板参数作用域类型
	 * @author lxw
	 */
	public enum ParamScope{
		LOCAL_BASIC,
		LOCAL_EXTERN,
		GLOBAL_BASIC,
		GLOBAL_EXTERN
	}
	
	/**
	 * 模板参数类型
	 * @author lxw
	 */
	public enum ParamType{
		NUM("num"),
		STRING("string"),
		ENUM("enum"),
		CONSTANT("constant"),
		RSL("rsl"),
		FPS("fps"),
		CONTAINER("container"),
		ARRAY("array");
		
		private String name;

		private ParamType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
		public static ParamType fromName(String name){
			if(null == name){
				return null;
			}
			for (ParamType paramType : ParamType.values()) {
				if(name.equals(paramType.getName())){
					return paramType;
				}
			}
			return null;
		}
	}
	

	public static final int MIXER_MAX_AUDIO_SRC_CNT = 256;
	
	public static final int MIXER_MAX_VIDEO_SRC_CNT = 16;
	
}
