package com.sumavision.tetris.capacity.bo.task;

import com.sumavision.tetris.application.template.TemplateUtil;
import com.sumavision.tetris.business.common.MissionBO;
import com.sumavision.tetris.business.common.enumeration.MediaType;
import com.sumavision.tetris.business.common.enumeration.TaskType;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.ProgramAudioBO;
import com.sumavision.tetris.capacity.bo.input.ProgramBO;
import com.sumavision.tetris.capacity.bo.input.ProgramVideoBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 下午3:59:07
 */
public class TaskBO {

	/** 任务id */
	private String id;
	
	/** 任务类型 video/audio/subtitle/passby */
	private String type;
	
	private TaskSourceBO es_source;
	
	private TaskSourceBO raw_source;
	
	private TaskSourceBO passby_source;
	
	private VideoMixSourceBO video_mix_source;
	
	private AudioMixSourceBO audio_mix_source;
	
	private List<DecodePreProcessingBO> decode_process_array;

	private List<EncodeBO> encode_array;

	public String getId() {
		return id;
	}

	public TaskBO setId(String id) {
		this.id = id;
		return this;
	}

	public String getType() {
		return type;
	}

	public TaskBO setType(String type) {
		this.type = type;
		return this;
	}

	public TaskSourceBO getEs_source() {
		return es_source;
	}

	public TaskBO setEs_source(TaskSourceBO es_source) {
		this.es_source = es_source;
		return this;
	}

	public TaskSourceBO getRaw_source() {
		return raw_source;
	}

	public TaskBO setRaw_source(TaskSourceBO raw_source) {
		this.raw_source = raw_source;
		return this;
	}

	public TaskSourceBO getPassby_source() {
		return passby_source;
	}

	public TaskBO setPassby_source(TaskSourceBO passby_source) {
		this.passby_source = passby_source;
		return this;
	}

	public VideoMixSourceBO getVideo_mix_source() {
		return video_mix_source;
	}

	public TaskBO setVideo_mix_source(VideoMixSourceBO video_mix_source) {
		this.video_mix_source = video_mix_source;
		return this;
	}

	public AudioMixSourceBO getAudio_mix_source() {
		return audio_mix_source;
	}

	public TaskBO setAudio_mix_source(AudioMixSourceBO audio_mix_source) {
		this.audio_mix_source = audio_mix_source;
		return this;
	}

	public List<DecodePreProcessingBO> getDecode_process_array() {
		return decode_process_array;
	}

	public TaskBO setDecode_process_array(List<DecodePreProcessingBO> decode_process_array) {
		this.decode_process_array = decode_process_array;
		return this;
	}

	public List<EncodeBO> getEncode_array() {
		return encode_array;
	}

	public TaskBO setEncode_array(List<EncodeBO> encode_array) {
		this.encode_array = encode_array;
		return this;
	}

	public TaskBO() {
	}

	public void setTaskSource(MissionBO missionBO,MediaType mediaType) throws BaseException {

		Integer taskProgNO = 0;
		Integer taskVideoNO = 0;
		Integer taskAudioNO = 0;

		InputBO taskInput = TemplateUtil.getInstance().getTaskInputBO(missionBO.getInputMap().values().stream().collect(Collectors.toList()));
		if ("audio".equals(this.getType())) {
			ProgramBO taskProg = taskInput.getProgram_array().get(taskProgNO);
			ProgramAudioBO taskAudio = taskProg.getAudio_array().get(taskAudioNO);
			this.setRaw_source(
					new TaskSourceBO(taskInput.getId(), taskProg.getProgram_number(), taskAudio.getPid()));
		}else if ("video".equals(this.getType())){
			ProgramBO taskProg = taskInput.getProgram_array().get(taskProgNO);
			if (taskProg.getVideo_array()==null || taskProg.getVideo_array().isEmpty()){
				throw new BaseException(StatusCode.FORBIDDEN,"video track not found: source no video");
			}
			ProgramVideoBO taskVideo = taskProg.getVideo_array().get(taskVideoNO);
			this.setRaw_source(
					new TaskSourceBO(taskInput.getId(), taskProg.getProgram_number(), taskVideo.getPid()));
		}else if ("passby".equals(this.getType())){
			if (TaskType.PASSBY.equals(missionBO.getTaskType())) {
				this.setPassby_source(
						new TaskSourceBO(taskInput.getId()));
			}else {
				ProgramBO taskProg = taskInput.getProgram_array().get(taskProgNO);
				if (MediaType.AUDIO.equals(mediaType)){
					this.setEs_source(
							new TaskSourceBO(taskInput.getId(),
									taskProg.getProgram_number(),
									taskProg.getAudio_array().get(taskAudioNO).getPid()));
				}else if (MediaType.VIDEO.equals(mediaType)){
					this.setEs_source(
							new TaskSourceBO(taskInput.getId(),
									taskProg.getProgram_number(),
									taskProg.getVideo_array().get(taskVideoNO).getPid()));
				}
			}
		}else if("subtitle".equals(this.getType())) {

		}else{
			throw new BaseException(StatusCode.ERROR,"not support media type: "+this.getType());
		}
	}
}
