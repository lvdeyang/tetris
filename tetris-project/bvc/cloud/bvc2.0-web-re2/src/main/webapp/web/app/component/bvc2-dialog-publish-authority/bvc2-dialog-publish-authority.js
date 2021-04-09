define([
    'text!' + window.APPPATH + 'component/bvc2-dialog-publish-authority/bvc2-dialog-publish-authority.html',
    'restfull',
    'vue',
    'element-ui'
], function(tpl, ajax, Vue){
	
	//组件名称
	var bvc2DialogPublishAuthority = "bvc2-dialog-publish-authority";
	
	Vue.component(bvc2DialogPublishAuthority, {
		props:['members','values','group'],
		template:tpl,
		data:function(){
			return {
				style:{
					saveAuthority:{
                        text:"发布",
                        isLoading:false
                    }
                },
                
                dialogFormVisible:false,
				form: {
			          name:''
			        },
                formLabelWidth:'120px'
                
			}
		},
		
		methods:{
			saveAuthority:function(){
				var dialog_instance = this;
				
				var _url = '/device/group/control/record/publishStream/' + dialog_instance.group.id;
				
				dialog_instance.style.saveAuthority.isLoading = true;
				dialog_instance.style.saveAuthority.text = "";
				
				ajax.post(_url, {
					rtmpUrl: dialog_instance.form.name
				}, function(data, status){
					if(status === 200) {
						dialog_instance.$message({
							type: 'success',
							message: '成功发布'
						});
						dialog_instance.dialogFormVisible = false;
					}
					dialog_instance.style.saveAuthority.isLoading = false;
					dialog_instance.style.saveAuthority.text = "发布";
				}, null, [403, 500]);
			}
		
		}
		
	});
	
});