package com.sumavision.bvc.vo.tree;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.minBy;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.pojo.FolderPO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//生成资源树
public class ResourceTreeGenerater {
	List<FolderPO> folders;
	List<BundleBody> bundles;
	ResourceTreeNodeVO rootNode;

	public ResourceTreeGenerater(List<FolderPO> folders, List<BundleBody> bundles) {
		this.folders = folders;
		this.bundles = bundles;
	}

	public Long findMaxGroupId() {
		Optional<FolderPO> collectFolder = folders.stream().collect(maxBy(comparing(FolderPO::getId)));
		return collectFolder.map(FolderPO::getId).orElse(0L);
		// return collectFolder.get() == null? 0L:collectFolder.get().getId();
	}

	public String loadTree() throws Exception {
		// 寻找根节点,根节点只有一个
		rootNode = findRoots();
		ResourceTreeNodeVO treeNode = recursiveTree(rootNode);
		JSONObject jsonobject = JSONObject.fromObject(treeNode);
		JSONArray jsonArray = JSONArray.fromObject(jsonobject);
		return jsonArray.toString();
	}

	private ResourceTreeNodeVO recursiveTree(ResourceTreeNodeVO node) {
		// 寻找根节点下的终端
		List<BundleBody> childrenBundles = bundles.stream().filter(b ->(b.getFolderId()!=null) && b.getFolderId().equals(node.getGroupId()))
				.collect(toList());
		// 寻找children分组节点
		List<FolderPO> childrenFolders = folders.stream().filter(f -> f.getParentId().equals(node.getGroupId()))
				.collect(toList());

		// 添加children节点
		for (FolderPO folder : childrenFolders) {
			ResourceTreeNodeVO treeNode = new ResourceTreeNodeVO();
			treeNode.setGroupId(folder.getId());
			treeNode.setName(folder.getName());
			treeNode.setBundle_Id("");
			treeNode.setIsFolder(true);
			treeNode.setParentId(folder.getParentId());
			ResourceTreeNodeVO childNode = recursiveTree(treeNode);
			node.getChildren().add(childNode);
		}
		// 添加资源节点
		for (BundleBody bundle : childrenBundles) {
			ResourceTreeNodeVO treeNode = new ResourceTreeNodeVO();
			treeNode.setName(bundle.getBundle_name());
			treeNode.setBundle_Id(bundle.getBundle_id());
			treeNode.setIsFolder(false);
			treeNode.setParentId(node.getGroupId());
			node.getChildren().add(treeNode);
		}
		return node;
	}

	// 寻找跟节点
	private ResourceTreeNodeVO findRoots() {
		// 返回跟节点
		Optional<FolderPO> collectFolder = folders.stream().collect(minBy(comparing(FolderPO::getId)));
		ResourceTreeNodeVO root = new ResourceTreeNodeVO();
		root.setGroupId(collectFolder.map(FolderPO::getId).orElse(0L));
		root.setIsFolder(true);
		root.setParentId(-1L);
		root.setName(collectFolder.map(FolderPO::getName).orElse("Unknown"));
		return root;
	}

}
