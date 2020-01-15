define([
    'text!' + window.APPPATH + 'component/bvc2-dialog-upload-live/bvc2-dialog-upload-live.html',
    'restfull',
    'vue',
    'element-ui',
], function(tpl, ajax, Vue){
	
	//组件名称
	var bvc2DialogUploadLive = "bvc2-dialog-upload-live";
	
	Vue.component(bvc2DialogUploadLive, {
		template:tpl,
		data:function(){
			return {
				
				style:{
					uploadSubmit:{
                        text:"发布",
                        isLoading:false
                    }
                },
                
                dialogFormVisible:false,
				form: {
			          name:'',
			          description:''
			        },
                formLabelWidth:'120px',
                
			}
		},
		
		methods:{
			
//			onSubmit:function(){
//			   console.log('uploadSubmit');  
//			},
			
			uploadSubmit:function(){
				var upload_dialog_instance = this;
//				event.preventDefault();
//				
				//let formData = new FormData();
				var formData = document.getElementById("formInfo");

				var name = formData[0].value;
				var description = formData[1].value;
				
				var _url = '/device/group/control/upload/live/';
				
				var jsonData = {"name":name, 
						        "description":description};
				
				upload_dialog_instance.style.uploadSubmit.isLoading = true;
				upload_dialog_instance.style.uploadSubmit.text = "";
				
				ajax.post(_url, jsonData, function(data){
					upload_dialog_instance.$message({
						type:'success',
						message:'发布成功'
					});
					upload_dialog_instance.style.uploadSubmit.isloading = false;
					upload_dialog_instance.style.uploadSubmit.text = "发布";
					upload_dialog_instance.dialogFormVisible = false;
				})
				
				this.form.name = "";
				this.form.description = "";
				
				
			}
		
		}
		
	});
	
});