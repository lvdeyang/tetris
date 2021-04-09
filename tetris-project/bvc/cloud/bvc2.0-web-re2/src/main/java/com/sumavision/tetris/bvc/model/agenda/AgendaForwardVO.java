package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.bvc.model.layout.LayoutVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaForwardVO extends AbstractBaseVO<AgendaForwardVO, AgendaForwardPO>{

	private String name;
	
	private Long agendaId;
	
	private Long layoutId;
	
	private String layoutSelectionType;
	
	private String layoutSelectionTypeName;
	
	private String audioType;

	private String audioTypeName;
	
	private Integer volume;
	
	private LayoutVO layout;
	
	private List<AgendaForwardSourceVO> sources;
	
	private List<AgendaForwardDestinationVO> destinations;
	
	private List<LayoutScopeVO> layouts;
	
	private List<CustomAudioVO> audios;

	public String getName() {
		return name;
	}

	public AgendaForwardVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public AgendaForwardVO setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
		return this;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public AgendaForwardVO setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
		return this;
	}

	public String getLayoutSelectionType() {
		return layoutSelectionType;
	}

	public AgendaForwardVO setLayoutSelectionType(String layoutSelectionType) {
		this.layoutSelectionType = layoutSelectionType;
		return this;
	}

	public String getLayoutSelectionTypeName() {
		return layoutSelectionTypeName;
	}

	public AgendaForwardVO setLayoutSelectionTypeName(String layoutSelectionTypeName) {
		this.layoutSelectionTypeName = layoutSelectionTypeName;
		return this;
	}

	public String getAudioType() {
		return audioType;
	}

	public AgendaForwardVO setAudioType(String audioType) {
		this.audioType = audioType;
		return this;
	}

	public String getAudioTypeName() {
		return audioTypeName;
	}

	public AgendaForwardVO setAudioTypeName(String audioTypeName) {
		this.audioTypeName = audioTypeName;
		return this;
	}

	public Integer getVolume() {
		return volume;
	}

	public AgendaForwardVO setVolume(Integer volume) {
		this.volume = volume;
		return this;
	}

	public LayoutVO getLayout() {
		return layout;
	}

	public AgendaForwardVO setLayout(LayoutVO layout) {
		this.layout = layout;
		return this;
	}

	public List<AgendaForwardSourceVO> getSources() {
		return sources;
	}

	public AgendaForwardVO setSources(List<AgendaForwardSourceVO> sources) {
		this.sources = sources;
		return this;
	}

	public List<AgendaForwardDestinationVO> getDestinations() {
		return destinations;
	}

	public AgendaForwardVO setDestinations(List<AgendaForwardDestinationVO> destinations) {
		this.destinations = destinations;
		return this;
	}

	public List<LayoutScopeVO> getLayouts() {
		return layouts;
	}

	public AgendaForwardVO setLayouts(List<LayoutScopeVO> layouts) {
		this.layouts = layouts;
		return this;
	}

	public List<CustomAudioVO> getAudios() {
		return audios;
	}

	public AgendaForwardVO setAudios(List<CustomAudioVO> audios) {
		this.audios = audios;
		return this;
	}

	@Override
	public AgendaForwardVO set(AgendaForwardPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setAgendaId(entity.getAgendaId())
			.setLayoutId(entity.getLayoutId())
			.setLayoutSelectionType(entity.getLayoutSelectionType()==null?null:entity.getLayoutSelectionType().toString())
			.setLayoutSelectionTypeName(entity.getLayoutSelectionType()==null?null:entity.getLayoutSelectionType().getName())
			.setAudioType(entity.getAudioType()==null?null:entity.getAudioType().toString())
			.setAudioTypeName(entity.getAudioType()==null?null:entity.getAudioType().getName())
			.setVolume(entity.getVolume())
			.setSources(new ArrayList<AgendaForwardSourceVO>())
			.setDestinations(new ArrayList<AgendaForwardDestinationVO>())
			.setLayouts(new ArrayList<LayoutScopeVO>())
			.setAudios(new ArrayList<CustomAudioVO>());
		return this;
	}
	
}
