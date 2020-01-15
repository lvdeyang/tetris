package com.sumavision.tetris.sts.common;

/**
 * Created by Poemafar on 2019/12/16 14:08
 */
public class CommonConstants {

    public enum NetGroupType {
        INPUT , OUTPUT
    }
    
    /**
     * 任务类型：发布
     */
    public static final Integer ENCAPSULATE = 0;
    /**
     * 任务类型：转码
     */
    public static final Integer TRANS = 1;
    public static final Integer NEWABILITY = 2;

    public static final Integer INPUT_UNICAST = 0;
    public static final Integer INPUT_MUTICAST = 1;

    public static final int OFFLINE= 0;
    public static final int ONLINE= 1;

    public static final int BACK = 0;
    public static final int MAIN = 1;

    public static final int UNSYN = 0;
    public static final int SYNCHRO = 1;
    
    /**
     * 设备校验
     */
    public static final int DECODE_FLAG = 0;
    public static final int ENCODE_FLAG = 1;
    
    public static final String INPUT_TYPE_STREAM = "stream";
    public static final String INPUT_TYPE_PASSBY = "passby";
    public static final String INPUT_TYPE_CARD = "card";
    
    //目前mpeg2的输入源，video里的编码格式是mpeg2-video，所以目前的预处理计算处有问题，暂时不改
    public static final String ENCODE_TYPE_MPEG2 = "mpeg2";
    public static final String ENCODE_TYPE_H264 = "h264";
    public static final String ENCODE_TYPE_H265 = "h265";
    public static final String ENCODE_TYPE_AVS = "avs";
    public static final String ENCODE_TYPE_NON_AVS = "non-avs";
    
    /**
     * refine
     */
    public static final String REFINE_TYPE_FAST = "fast";
    public static final String REFINE_TYPE_MIDDLE = "middle";
    public static final String REFINE_TYPE_SLOW = "slow";
    
    /**
     * deinterlace-mode  节目参数里的
     */
    public static final String DEINTERLACE_TYPE_FAST = "fast";
    public static final String DEINTERLACE_TYPE_MIDDLE = "middle";
    public static final String DEINTERLACE_TYPE_SLOW = "slow";
    
    public static final String DECODE_MODE_CPU = "cpu";
    public static final String DECODE_MODE_GPU = "gpu";
    public static final String DECODE_MODE_MSDK = "msdk";
    public static final String DECODE_MODE_CUDA = "cuda";
    public static final String DECODE_MODE_AUTO = "auto";
    
    public static final String FFMPEG_PATH = "/opt/sumavision/ffmpeg-3.4.1-64bit-static/ffmpeg"; 
    
    
//    public enum HardwareType{
//    	SDM,SINGLE_SERVER
//    }
    public enum FilterMode{
        INCLUDE,EXCLUDE
    }

	public enum BackType{
        DEFAULT , MAIN , BACK
    }
    /**
     * 关键词分隔符，目前包含空格与下划线
     */
    public static final String WORD_SPLIT = "[ _]";

    /**
     * node：SDM设备内节点
     * server：单节点服务器
     */
    public enum DeviceType{
        SDM2 ,SDM3, SERVER
    }

    public enum FunUnitType{
        TRANS , ENCAPSULATE, STREAM_MEDIA, MATRIX
    }

	public enum FunUnitStatus{
		NONE , DEFAULT , NORMAL ,/*NEED_LIGHTSYNC,*/NEED_SYNC , OFF_LINE
	}

    public enum GroupType {
        GROUP , SDM , SDM_GROUP , TRANSMIT
    }

    public enum LockStatus {
    	LOCKED , UNLOCKED
    }
    
    public enum NetCardType {
        DEFAULT , TRANSMIT , DATA
    }

    public enum BondType {
        NONE , MASTER , SLAVE
    }

    public enum TemplateType {
        AUDIO , VIDEO
    }
    
    public enum ResolutionType{
    	SD_720_576 , HD_1280_720 , HD_1920_1080
    }


    public enum ProtoType {
        ALL,
        TSUDP,
        TSRTP,
        HTTPTS,

        RTSPRTP,
        RTMPFLV,
        HTTPFLV,
        HLS,
        TSUDPPASSBY,
        HDS,
        MSS,
        TSSRT,
        TSSRTPASSBY,

        ASF,
        DASH,
        RTMPDYNAMIC,
        SDI,
        ASI,
        HTTPTSPASSBY,
        TSRTPPASSBY,
        RTPES,
        PROGRESSHTTP,
        CALLER,
        LISTENER;

        public static ProtoType getProtoType(String type) {
            switch (type) {
                case "TS-UDP":
                    return TSUDP;
                case "TS-RTP":
                	return TSRTP;
                case "HTTP-FLV":
                case "http4flv":
                	return HTTPFLV;
                case "TS-HTTP":
                case "HTTP-TS"://yzx add HTTP-TS类型应该被解析为HTTPTS，源的类型有TS-HTTP
                case "httptsapp":
                	return HTTPTS;
                case "DASH":
                case "dash":
                	return DASH;
                case "HDS-FLV":
                case "hds":
                	return HDS;
//                case "HDS":
//                	return HDS;
                case "HLS":
                case "hls":
                	return HLS;
                case "HTTP-SSM":
                case "smoothstream":
                	return MSS;
                case "RTSP":
                case "rtsp":
                	return RTSPRTP;
                case "RTMP-FLV":
                case "naturalrtmp":
                	return RTMPFLV;
                case "TS-UDP-PASSBY":
                	return TSUDPPASSBY;
                case "TS-HTTP-PASSBY":
                	return HTTPTSPASSBY;
                case "TS-SRT":
                    return TSSRT;
 				case "TS-SRT-PASSBY":
                	return TSSRTPASSBY;				case "listener":
                    return LISTENER;
                case "caller":
                    return CALLER;                default:
                    return TSUDP;
            }
        }
        /**
         * 生成对应类型的标签字符串
         * @param protoType
         * @return
         */
        public static String getStringTypeByProtoType(ProtoType protoType){
        	switch (protoType) {
            case ASI:
                return "ASI_SEND_10K744";
    		case TSUDP:
    			return "TS-UDP";
    		case TSUDPPASSBY:
    			return "TS-UDP-PASSBY";
    		case TSSRT:
    			return "TS-SRT";
    		case TSSRTPASSBY:
    			return "TS-SRT-PASSBY";
    		case DASH:
    			return "DASH";
    		case HDS:
    			//return "HDS-FLV";
    			return "HDS";
    		case HLS:
    			return "HLS";
    		case MSS:
    			return "HTTP-SSM";
			case HTTPTSPASSBY:
    			return "TS-HTTP-PASSBY";
    		case HTTPFLV:
    			return "HTTP-FLV";
    		case HTTPTS:
    			return "TS-HTTP";
    		case TSRTP:
    			return "TS-RTP";
    		case RTSPRTP:
    			return "RTSP";
    		case RTMPFLV:
    			return "RTMP-FLV";
    		default:
    			
    			break;
    		}
        	return null;
        }

        public String toApplication() {
            switch (this) {
    		case DASH:
    			return "dash";
    		case HDS:
    			return "hds";
    		case HLS:
    			return "hls";
    		case MSS:
    			return "smoothstream";
    		case HTTPFLV:
    			return "http4flv";
    		case HTTPTS:
    			return "httptsapp";
    		case RTSPRTP:
    			return "rtsp";
    		case RTMPFLV:
    			return "naturalrtmp";
    		default:
    			return "hls";
            }
        }
        
        
    }
    
	public enum NodeStatus{
		NORMAL, ERROR, DELETING
	}
	
	public enum DeviceMonitorTime{
		ONE_HOUR,SIX_HOUR,ONE_DAY,ONE_WEEK
	}
}
