//全局配置
var gC = {
	// 树形结构
	tree : {
		"id" : "",
		"type" : "",
		"container" : "",
		"root" : "",
		"multiselect":"true"	//默认是true表示可多选。
	},
	// 消息框
	zkAlert : {
		"id" : "zk_Alert_default",
		"width" : "30",
		"title" : "提示消息",
		"left" : "500px",
		"top" : "250px",
		"content" : "这是一个弹框",
		"hasFooter":"true",
		"onClose":"",
		"commitBtn" : {
			"text" : "确定",
			"callback" : ""
		}
	},
	// 窗体弹出
	popWindow : {
		"id" : "zk_Window_default",
		"title" : "",
		"content" : "",
		"commitBtn" : {
			"text" : "",
			"callback" : ""
		},
		"onClose":null
	}
};