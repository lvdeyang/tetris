
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-ui 上午10:09:56  
* All right reserved.  
*  
*/

package com.sumavision.bvc.PO;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;

/**
 * @desc: bvc-monitor-ui,业务的实体类
 * @author: kpchen
 * @createTime: 2018年6月5日 上午10:09:56
 * @history:
 * @version: v1.0
 */
@Data
@Entity
@Table(name = "PreviewData")
public class PreviewPO implements Serializable {
	@Id
	@Column(name = "t_id")
	@GeneratedValue
	private Long id; // 预览Id
	private String userName; // 预览者
	private String callnumber;// 识别名
	private String resourceId;// 资源id
	private String role; // 角色
	private String outputUrl;
	private String location;
	private String splitNumer;// 分屏个数
}
