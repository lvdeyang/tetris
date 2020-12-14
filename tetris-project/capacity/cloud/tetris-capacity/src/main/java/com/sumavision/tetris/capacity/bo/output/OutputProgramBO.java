package com.sumavision.tetris.capacity.bo.output;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.MissionBO;
import com.sumavision.tetris.application.template.MediaVO;
import com.sumavision.tetris.application.template.ProgramVO;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 输出节目参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月1日 下午4:52:28
 */
public class OutputProgramBO {

	private Integer program_number;
	
	private String name;
	
	private String provider;
	
	private String character_set;
	
	private Integer pmt_pid;
	
	private Integer pcr_pid;
	
	private List<OutputMediaBO> media_array;

	public Integer getProgram_number() {
		return program_number;
	}

	public OutputProgramBO setProgram_number(Integer program_number) {
		this.program_number = program_number;
		return this;
	}

	public String getName() {
		return name;
	}

	public OutputProgramBO setName(String name) {
		this.name = name;
		return this;
	}

	public String getProvider() {
		return provider;
	}

	public OutputProgramBO setProvider(String provider) {
		this.provider = provider;
		return this;
	}

	public String getCharacter_set() {
		return character_set;
	}

	public OutputProgramBO setCharacter_set(String character_set) {
		this.character_set = character_set;
		return this;
	}

	public Integer getPmt_pid() {
		return pmt_pid;
	}

	public OutputProgramBO setPmt_pid(Integer pmt_pid) {
		this.pmt_pid = pmt_pid;
		return this;
	}

	public Integer getPcr_pid() {
		return pcr_pid;
	}

	public OutputProgramBO setPcr_pid(Integer pcr_pid) {
		this.pcr_pid = pcr_pid;
		return this;
	}

	public List<OutputMediaBO> getMedia_array() {
		return media_array;
	}

	public OutputProgramBO setMedia_array(List<OutputMediaBO> media_array) {
		this.media_array = media_array;
		return this;
	}

	public OutputProgramBO() {
	}

	public OutputProgramBO(ProgramVO programVO) {
		this.program_number = programVO.getProgram_number();
		this.name = programVO.getProgram_name();
		this.provider = programVO.getProvider();
		this.character_set = programVO.getCharacter_set();
		this.pmt_pid = programVO.getPmt_pid();
		this.pcr_pid = programVO.getPcr_pid();
	}


	public OutputProgramBO setOutputMedias(MissionBO missionBO, JSONArray tmplMedias){
		List<OutputMediaBO> outputMediaBOS = new ArrayList<>();
		if (tmplMedias==null||tmplMedias.isEmpty()){
			AtomicReference<Integer> outPid = new AtomicReference<>(512);
			missionBO.getTask_array().forEach(t->{
				Integer pid = null;
				if (t.getEs_source()!=null){
					pid = t.getEs_source().getElement_pid();
				}else if (t.getRaw_source()!=null){
					pid = t.getRaw_source().getElement_pid();
				}
				String outMediaType = "none";
				if (t.getType().equals("passby")){
					outMediaType = missionBO.getMediaTypeMap().get(pid);
				}else{
					outMediaType = t.getType();
				}
				String finalOutMediaType = outMediaType;
				t.getEncode_array().stream().forEach(e->{
					outputMediaBOS.add(new OutputMediaBO()
							.setTask_id(t.getId())
							.setEncode_id(e.getEncode_id())
							.setPid(outPid.getAndSet(outPid.get() + 1))
							.setType(finalOutMediaType)
					);
				});
			});
		}else {
			for (int i = 0; i < tmplMedias.size(); i++) {
				JSONObject mediaObj = tmplMedias.getJSONObject(i);
				String encodeId = missionBO.getOutEncodeMap().get(mediaObj.getInteger("index"));
				OutputMediaBO outputMediaBO = new OutputMediaBO().setPid(mediaObj.getInteger("pid")).setEncode_id(encodeId);
				for (int j = 0; j < missionBO.getTask_array().size(); j++) {
					TaskBO taskBO = missionBO.getTask_array().get(j);
					Integer pid = null;
					String outMediaType = "none";
					if (taskBO.getType().equals("passby")){
						if (taskBO.getEs_source()!=null){
							pid = taskBO.getEs_source().getElement_pid();
						}else if (taskBO.getRaw_source()!=null){
							pid = taskBO.getRaw_source().getElement_pid();
						}
						outMediaType = missionBO.getMediaTypeMap().get(pid);
					}else{
						outMediaType = taskBO.getType();
					}
					for (int k = 0; k < taskBO.getEncode_array().size(); k++) {
						EncodeBO encodeBO = taskBO.getEncode_array().get(k);
						if (encodeBO.getEncode_id().equals(encodeId)) {
							outputMediaBO.setTask_id(taskBO.getId()).setType(outMediaType);
							break;
						}
					}
				}
				outputMediaBOS.add(outputMediaBO);
			}
		}
		this.setMedia_array(outputMediaBOS);
		return this;
	}
}
