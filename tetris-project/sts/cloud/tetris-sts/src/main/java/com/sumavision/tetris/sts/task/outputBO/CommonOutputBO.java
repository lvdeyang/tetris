package com.sumavision.tetris.sts.task.outputBO;

import java.io.Serializable;

import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;



public abstract class CommonOutputBO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7283431312319117339L;

	private String provider;

	private Integer programNum;

	/**
	 * 发布类型的推送方式，1是推送，0是本地发布
	 */
	private Integer dstType = 1;

	public abstract OutputCommon generateOutputCommon(OutputPO outputPO, ProgramPO programPO, OutputMediaEncodeMessage outputMediaEncodeMessage);

	public abstract Boolean isSameWithCfg(CommonOutputBO outputBO);
	
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Integer getProgramNum() {
		return programNum;
	}

	public void setProgramNum(Integer programNum) {
		this.programNum = programNum;
	}

	public String getBitrateS(String bitrateS) {
		StringBuilder sb = new StringBuilder();
		String[] str = bitrateS.split(",");
		for (int i = 0; i < str.length; i++) {
			if (sb.length() != 0)
				sb.append(",");
			if (!str[i].equals(""))
				sb.append(Integer.parseInt(str[i]) * 1000);
		}
		return sb.toString();
	}

	public Integer getDstType() {
		return dstType;
	}

	public void setDstType(Integer dstType) {
		this.dstType = dstType;
	}

	public static void main(String[] arg) {

	}
}
