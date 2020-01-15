define([
	'text!' + window.APPPATH + 'component/bvc2-dialog-set-group-template/bvc2-dialog-set-group-template.html',
	'restfull',
    'vue',
    'element-ui'
], function(tpl, ajax, Vue){
	
	//组件名称
	var bvc2DialogSetGroupTemplate = "bvc2-dialog-set-group-template";
	

	Vue.component(bvc2DialogSetGroupTemplate, {
		props:['systemtpls', 'avtpls', 'authtpls', 'createrow'],
		template:tpl,
		data:function(){
			return {
				style:{
					setTemplate:{
						text:"完成",
						isLoading:false
					}
				},
				
				setTemplateVisible:false,
				
				tplIds:{
					systemTplId:'',
					avtplId:'',
					authtplId:'',
				}
				
			}
		},

		methods:{
			setTemplate:function(){
				
				var dialog_instance = this;
				
				var systemTplId = this.tplIds.systemTplId;
				var avtplId = this.tplIds.avtplId;
				var authtplId = this.tplIds.authtplId;
				
				//设备组模板判空
				if(systemTplId == '' && avtplId == ''){
					dialog_instance.$message({
						message: '设备组模板不能为空',
						type: 'warning'
					})
					return;
				}else if(systemTplId == ''){
					dialog_instance.$message({
						message: '会议模板不能为空！',
						type: 'warning'
					})
					return;
				}else if(avtplId == ''){
					dialog_instance.$message({
						message: '参数方案不能为空！',
						type: 'warning'
					})
					return;
				}
				
				var data = [{"systemTplId": systemTplId}, {"avtplId": avtplId}, {"authtplId": authtplId}];
				
				dialog_instance.setTemplateVisible = false;
				
				dialog_instance.createrow(data);
				
			},
		
			getTpls:function(){
				
				var systemTplId = this.tplIds.systemTplId;
				var avtplId = this.tplIds.avtplId;
				var authtplId = this.tplIds.authtplId;
				var data = {"systemTplId": systemTplId, "avtplId": avtplId, "authtplId": authtplId};
				return data;
			
			},
			
		}
	})
	
})