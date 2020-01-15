package com.sumavision.tetris.sts.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConstantUtil {

    @Value("${constant.ipType:ipv4}")
    private String ipType;

    @Value("${constant.transType:2030}")
    private String transType;
    
    @Value("${constant.sourceEncodeType:avs}")
    private String sourceEncodeType;
    
    @Value("${constant.databaseBackup:false}")
    private Boolean databaseBackup;
    
	@Value("${constant.version:v3.2.1_1}")
    private String version;
    
    @Value("${constant.pathRoot:/opt/sumavision/tomcat_sdm/webapps/web-app}")
    private String pathRoot;

    @Value("${constant.paramOne:8}")
    private Integer paramOne;

    @Value("${constant.offline.time:17}")
    private Integer offlineTime;
    
    @Value("${constant.validTime:3}")
    private String validTime;
    
    @Value("${constant.service.install.dir:/opt/sumavision}")
    private String serviceInstallDir;
    
    @Value("${constant.isCheckMysqlSync:false}")
    private Boolean isCheckMysqlSync;
    
    @Value("${constant.isMaster:true}")
    private Boolean isMaster;
    
    @Value("${constant.language:c}")
    private String language;
    
    @Value("${constant.fununit.bridgeFlag:false}")
    private Boolean bridgeFlag;

    @Value("${constant.shutdown:/opt/sumavision/tomcat_sdm/logs/shutdown.log}")
    private String shutdownFile;

    @Value("${constant.deploy:prod}")
    private String deployType;

    @Value("${constant.freeIp:10.10.43.24}")
    private String freeIp;

    @Value("${constant.reconnectTimes:10}")
    private Integer reconnectTimes;

    @Value("${constant.httpSecurityFlag:false}")
    private Boolean httpSecurityFlag;

    @Value("${constant.fileTypeCheck:false}")
    private Boolean fileTypeCheck;

    @Value("${constant.apiCheck:false}")
    private Boolean apiCheck;

    /**
     * 控制主备切换方式，0是旧的：稳定，1是：优化加速
     */
    @Value("${constant.switchMode:0}")
    private Integer switchMode;
    
    @Value("${constant.inputPORepeatCheck:false}")
    private Boolean inputPORepeatCheck;

    public Boolean getInputPORepeatCheck() {
		return inputPORepeatCheck;
	}

	public Boolean getBridgeFlag() {
		return bridgeFlag;
	}

	public String getLanguage() {
		return language;
	}

    public Boolean getIsMaster() {
		return isMaster;
	}

	public Boolean getIsCheckMysqlSync() {
		return isCheckMysqlSync;
	}

	public String getIpType(){
        return ipType;
    }

    public String getTransType() {
        return transType;
    }

    public String getSourceEncodeType() {
        return sourceEncodeType;
    }

	public Boolean getDatabaseBackup() {
		return databaseBackup;
	}

	public String getVersion() {
		return version;
	}

	public String getPathRoot() {
		return pathRoot;
	}

	public Integer getParamOne() {
		return paramOne;
	}

	public Integer getOfflineTime() {
		return offlineTime;
	}

	public String getValidTime() {
		return validTime;
	}

	public String getServiceInstallDir() {
		return serviceInstallDir;
	}

    public String getShutdownFile() {
        return shutdownFile;
    }

    public String getDeployType() {
        return deployType;
    }

    public String getFreeIp() {
        return freeIp;
    }

    public Integer getReconnectTimes() {
        return reconnectTimes;
    }

    public Boolean getHttpSecurityFlag() {
        return httpSecurityFlag;
    }

    public Boolean getFileTypeCheck(){ return fileTypeCheck;}

    public Boolean getApiCheck() {
        return apiCheck;
    }

    public Integer getSwitchMode() {
        return switchMode;
    }
}
