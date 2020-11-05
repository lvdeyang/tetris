package com.sumavision.bvc.startRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.service.FolderService;

@Component
public class treeInitRunner implements ApplicationRunner {

	@Autowired
	private FolderService folderService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (folderService.findAll().isEmpty()) {
			FolderPO folder = new FolderPO();
			folder.setName("根节点");
			folder.setParentId(-1L);
			folderService.save(folder);
		}
	}

}
