package com.sumavision.tetris.cs.channel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ChannelQuery {
	@Autowired
	private ChannelDAO channelDao;

	public List<ChannelPO> findAll(int currentPage, int pageSize) {
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<ChannelPO> channels = channelDao.findAll(page);
		return channels.getContent();
	}

	public ChannelPO findByChannelId(Long channelId) {
		return channelDao.findOne(channelId);
	}
}
