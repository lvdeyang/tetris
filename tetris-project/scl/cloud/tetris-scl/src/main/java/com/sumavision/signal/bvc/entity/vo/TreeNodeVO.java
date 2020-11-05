package com.sumavision.signal.bvc.entity.vo;

import java.util.List;

import com.sumavision.signal.bvc.entity.enumeration.TreeNodeIcon;
import com.sumavision.signal.bvc.entity.enumeration.TreeNodeType;
import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
/**
 * 
* @ClassName: TreeNodeVO 
* @Description: 生成树结构
* @author wjw
* @date 2019年5月21日 上午11:24:41 
*
 */
public class TreeNodeVO {

	private String id;
	
	private String uuid;
	
	private String name;
	
	/** type@@id */
	private String key;
	
	private TreeNodeType type;
	
	private List<TreeNodeVO> children;
	
	/** jsonString 附加参数 */
	private String param;
	
	private String icon;

	public String getId() {
		return id;
	}

	public TreeNodeVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public TreeNodeVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public TreeNodeVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getKey() {
		return key;
	}

	public TreeNodeVO setKey(){
		this.setKey(this.generateKey());
		return this;
	}
	public TreeNodeVO setKey(String key) {
		this.key = key;
		return this;
	}

	public TreeNodeType getType() {
		return type;
	}

	public TreeNodeVO setType(TreeNodeType type) {
		this.type = type;
		return this;
	}

	public List<TreeNodeVO> getChildren() {
		return children;
	}

	public TreeNodeVO setChildren(List<TreeNodeVO> children) {
		this.children = children;
		return this;
	}

	public String getParam() {
		return param;
	}

	public TreeNodeVO setParam(String param) {
		this.param = param;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public TreeNodeVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}
	
	public String generateKey(){
		return new StringBufferWrapper().append(this.getType().toString())
										.append("@@")
										.append(this.getId())
										.toString();
	}

	/**
	 * 创建流转发器节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午5:23:41
	 * @param RepeaterPO repeater 流转发器信息
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(RepeaterPO repeater){
		this.setId(repeater.getId().toString())
			.setName(repeater.getName())
			.setParam(repeater.getIp())
			.setType(TreeNodeType.REPEATER)
			.setIcon(TreeNodeIcon.REPEATER.getName())
			.setKey(this.generateKey());
			
		return this;
	}
	
	/**
	 * 创建网口节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午5:23:41
	 * @param InternetAccessPO access 网口信息
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(InternetAccessPO access){
		this.setId(access.getId().toString())
			.setName(access.getAddress())
			.setParam(access.getAddress())
			.setType(TreeNodeType.INTERNET_ACCESS)
			.setIcon(TreeNodeIcon.INTERNET_ACCESS.getName())
			.setKey(this.generateKey());
			
		return this;
	}
}
