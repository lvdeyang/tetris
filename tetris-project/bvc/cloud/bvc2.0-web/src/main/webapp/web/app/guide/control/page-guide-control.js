define([
    'text!' + window.APPPATH + 'guide/control/page-guide-control.html',
    'restfull',
    'config',
    'context',
    'commons',
    'vue',
    'extral',
    'element-ui',
    'css!' + window.APPPATH + 'guide/control/page-guide-control.css'
], function(tpl, ajax, config, context, commons, Vue){

    var pageId = 'page-guide-control';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);


        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;
        new Vue({
            el:'#' + pageId + '-wrapper',
            data:function(){
				//var self = this;
				//var validatePass = function(rule, value, callback) {
				//	if (value === '') {
				//		callback(new Error('请输入密码'));
				//	} else {
				//		if (self.dialog.setSource.sourceName !== '') {
				//			self.$refs.ruleForm.validateField('name');
				//		}
				//		callback();
				//	}
				//};
                return {
					editableTabsValue: '2',
					editableTabs: [{
						title: '导播任务1',
						name: '1',
						content: 'Tab 1 content'
					}, {
						title: '导播任务2',
						name: '2',
						content: 'Tab 2 content'
					}],
					tabIndex: 2,

                	user:context.getProp('user'),
                	menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-group-list",
					curPgm:1,
                    dialog: {
                    	setSource:{
                    		visible: false,
                            loading: false,
							id: 0,
                            sourceName:'',
                            sourceType:'',
							source:'',
							previewOut:'',
                            typeOptions:['5G背包','直播流'],
							bundleName: ''
                    	},
                    	setOut:{
                    		visible: false,
                            loading: false,

							video:{
								codingObject: '',
								fps: '25',
								bitrate: 1500,
								resolution: '',
								ratio: '',
								rcMode: '',
								maxBitrate: 1500
							},

							audio:{
								codingFormat:'',
								channelLayout:'',
								bitrate:'128',
								sampleRate:'44.1',
								codingType:''
							},

							out:{
								outputProtocol: '',
								outputAddress: '',
								rateCtrl: 'VBR',
								bitrate: 8000000
							}
                    	},
						setDevice:{
							visible: false,
                            loading: false,
							deviceData:[]
						}

                    },
					sources:{
						list:[]
					},
					rules: {
						sourceName: [
							{ required: true, message: "请输入源名称", trigger: 'blur' }
							//{ min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
							//{ validator: validatePass, trigger: 'blur'}
						]
					}
                }
            },
            computed:{
                
            },
            methods:{
				handleSelPgm: function () {
					var self = this;
				},
				addTab: function(targetName) {
					var newTabName = ++this.tabIndex + '';
					this.editableTabs.push({
						title: '导播任务' + this.tabIndex,
						name: newTabName,
						content: 'New Tab content'
					});
					this.editableTabsValue = newTabName;
					newTabName = '导播任务' + newTabName;
					ajax.post('/tetris/guide/control/guide/po/add', newTabName, function (data, status) {

					}, null, ajax.NO_ERROR_CATCH_CODE);
				},
				removeTab: function(targetName) {
					var tabs = this.editableTabs;
					var activeName = this.editableTabsValue;
					if (activeName === targetName) {
						tabs.forEach((tab, index) => {
							if (tab.name === targetName) {
								var nextTab = tabs[index + 1] || tabs[index - 1];
								if (nextTab) {
									activeName = nextTab.name;
								}
							}
						});
					}

					this.editableTabsValue = activeName;
					this.editableTabs = tabs.filter(tab => tab.name !== targetName);
					this.tabIndex--;
				},
				handleSetDeviceClose:function(){
					var self = this;
					self.dialog.setDevice.visible = false;
				},
            	handleSetSource:function(sourceNumber){
            		var self = this;
					var x;
					for(var i = 0; i < self.sources.list.length; i++){
						if(self.sources.list[i].index == sourceNumber){
							x = self.sources.list[i];
							break;
						}
					}
            		self.dialog.setSource.visible = true;
					self.dialog.setSource.id = x.id;

					self.dialog.setSource.sourceType = x.sourceTypeName;
					self.dialog.setSource.source = x.source;
					self.dialog.setSource.previewOut = x.previewOut;
            	},
				handleSetSourceCommit:function(){
/*					console.log(this.$refs[formName])
					this.$refs[formName].validate((valid)=>{

						if (valid) {*/
							var self = this;
							self.dialog.setSource.visible=false;
							var questData = {
								id:self.dialog.setSource.id,
								sourceType: self.dialog.setSource.sourceType,
								source:self.dialog.setSource.source,
								sourceName:self.dialog.setSource.sourceName,
								previewOut:self.dialog.setSource.previewOut
							};
							ajax.post('/tetris/guide/control/source/po/edit', questData, function (data, status) {
								for(var i = 0; i < self.sources.list.length; i++){
									list[i].sourceType = self.dialog.setSource.sourceType;
									list[i].source = self.dialog.setSource.source;
									list[i].sourceName = self.dialog.setSource.sourceName;
									list[i].previewOut = self.dialog.setSource.previewOut;
								}
							}, null, ajax.NO_ERROR_CATCH_CODE);
						/*} else {
							console.log('error submit!!');
							return false;
						}
					});*/
				},
				handleSetSourceClose:function(){
					var self = this;
					self.$refs['setSource'].resetFields();
					self.dialog.setSource.visible=false;
				},
            	handleSetOut:function(){
            		var self=this;
            		self.dialog.setOut.visible=true;
					self.dialog.setOut.type="";
					self.dialog.setOut.url="";
            	},
				handleSetOutCommit:function(){
					var self = this;
					self.dialog.setOut.visible=false;
					var questDataVideo = {
						id: 3,
						codingObject: self.dialog.setOut.video.codingObject,
						fps:self.dialog.setOut.video.fps,
						bitrate:self.dialog.setOut.video.bitrate,
						resolution:self.dialog.setOut.video.resolution,
						ratio:self.dialog.setOut.video.ratio,
						rcMode:self.dialog.setOut.video.rcMode,
						maxBitrate:self.dialog.setOut.video.maxBitrate,
					};

					var questDataAudio = {
						id: 3,
						codingFormat:self.dialog.setOut.audio.codingFormat,
						channelLayout:self.dialog.setOut.audio.channelLayout,
						bitrate:self.dialog.setOut.audio.bitrate,
						sampleRate:self.dialog.setOut.audio.sampleRate,
						codingType:self.dialog.setOut.audio.codingType,
					};

					var questDataOut = {
						id: 3,
						outputProtocol: self.dialog.setOut.out.outputProtocol,
						outputAddress: self.dialog.setOut.out.outputAddress,
						rateCtrl: self.dialog.setOut.out.rateCtrl,
						bitrate: self.dialog.setOut.out.bitrate,
					}
					ajax.post('/tetris/guide/control/output/setting/po/editVideo', questDataVideo, function (data, status) {
						
					}, null, ajax.NO_ERROR_CATCH_CODE);

					ajax.post('/tetris/guide/control/output/setting/po/editAudio', questDataAudio, function (data, status) {

					}, null, ajax.NO_ERROR_CATCH_CODE);

					ajax.post('/tetris/guide/control/output/setting/po/edit', questDataOut, function (data, status) {

					}, null, ajax.NO_ERROR_CATCH_CODE);

				},
				handleSetOutClose:function(){
					var self=this;
            		self.dialog.setOut.visible=false;
				},
				handleDeviceSelect:function(){
					var self=this;
					self.dialog.setSource.visible=false;
					self.dialog.setDevice.visible=true;
					self.dialog.setDevice.deviceData.splice(0, self.dialog.setDevice.deviceData.length);
					ajax.post('/tetris/guide/control/source/po/queryDevice', null, function(data, status){
						for(var i = 0; i < data.length; i++){
							self.dialog.setDevice.deviceData.push(data[i]);
						}
					})
				},
				startGuide:function(){
					ajax.post('/tetris/guide/control/guide/po/start',{id: 1},function(data, status){

					})
				},
				stopGuide:function(){
					ajax.post('/tetris/guide/control/guide/po/stop',{id: 1},function(data, status){

					})
				},
				handleSelPgm:function(index){
					var self=this;
					self.curPgm=index;
				},
				getBackcolor:function(index){
					var self=this;
					if(self.curPgm==index){
						return "background:#cc0033"
					}else{
						return "background:#CCC"
					}
				},
				switchSource:function(){
					var self = this;
					var questData = {
						id: 1,
						index: self.curPgm
					};
					ajax.post('/tetris/guide/control/source/po/cut', questData, function (data, status) {
						
					}, null, ajax.NO_ERROR_CATCH_CODE);
				},
				handleSetDeviceClick:function(row){
					var self = this;
					self.dialog.setSource.source = row.bundleId;
					self.dialog.setSource.visible = true;
					self.dialog.setDevice.visible = false;
					self.dialog.setSource.bundleName = row.bundleName;
				}

               
            },
			created:function(){
				var self = this;
/*
				ajax.post('/tetris/guide/control/guide/po/query', null, function(data){

				});
*/

				ajax.post('/tetris/guide/control/source/po/query', {id: 2}, function(data, status){
					console.log(data);
					for(var i = 0; i < data.length; i++){
						self.sources.list.push(data[i]);
						/*if(data[i].sourceTypeName == '5G背包'){
							self.dialog.setDevice.deviceData.push(data[i]);
						}*/
					}
				})

			}

        });
    };

    var destroy = function(){

    };
	var guideList = {
		path:'/' + pageId,
		component:{
			template:'<div id="' + pageId + '" class="page-wrapper"></div>'
		},
		init:init,
		destroy:destroy
	};

	return guideList;
});