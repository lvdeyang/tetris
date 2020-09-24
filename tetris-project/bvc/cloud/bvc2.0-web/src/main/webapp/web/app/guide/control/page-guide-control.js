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
					guides:{
						list: []
					},

					//tabIndex: this.guides.list.length,

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
							bundleName: '',
							set: ''
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
								bitrate: 8000000,
								switchingMode: ''
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
					output:{
						out:[]
					},
					rules: {
						sourceName: [
							{ required: true, message: "请输入源名称", trigger: 'blur' }
							//{ min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
							//{ validator: validatePass, trigger: 'blur'}
						]
					},
                }
            },
            computed:{
                
            },
            methods:{
				handleSelPgm: function () {
					var self = this;
				},
				addTab: function(tabIndex){
					var listLength = this.guides.list.length +1;
					ajax.post('/tetris/guide/control/guide/po/add', {taskName: ("导播任务"+listLength)}, function(data, status){
						self.guides.list.push({
							id:data.id,
							taskName:data.taskName
						})
					}, null, ajax.NO_ERROR_CATCH_CODE);
					self.editableTabsValue = data.id;
				},
				removeTab: function(targetName) {
					let tabs = this.guides.list;
					let activeName = this.editableTabsValue;
					if (activeName == targetName) {
						tabs.forEach((tab, index) => {
							if (tab.id == targetName) {
								let nextTab = tabs[index + 1] || tabs[index - 1];
								if (nextTab) {
									activeName = nextTab.id;
								}
							}
						});
					}

					this.editableTabsValue = activeName;
					this.guides.list = tabs.filter(tab => tab.id != targetName);
					ajax.post('/tetris/guide/control/guide/po/delete', {id: targetName}, function (data, status) {

					}, null, ajax.NO_ERROR_CATCH_CODE);
				},
				handleClick:function(tab, event){
					console.log(this.editableTabsValue)
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
					self.dialog.setSource.sourceName = x.sourceName;
					self.dialog.setSource.bundleName = x.sourceName;
            	},
				handleSetSourceCommit:function(){
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
							if(data.id === self.sources.list[i].id){
								self.sources.list.splice(i,1,data);
								break;
							}
						}
					}, null, ajax.NO_ERROR_CATCH_CODE);
				},
				handleSetSourceClose:function(){
					var self = this;
					self.$refs['setSource'].resetFields();
					self.dialog.setSource.visible=false;
				},
            	handleSetOut:function(){
					var self = this;
					ajax.post('/tetris/guide/control/output/setting/po/query', {taskNumber: 2}, function(data, status){
						//console.log(data);
						for(var i = 0; i < data.length; i++){
							self.output.out.push(data[i]);
							self.dialog.setOut.out.outputProtocol = data[i].outputProtocolName;
							self.dialog.setOut.out.outputAddress = data[i].outputAddress;
							self.dialog.setOut.out.rateCtrl = data[i].rateCtrl;
							self.dialog.setOut.out.bitrate = data[i].bitrate;
							self.dialog.setOut.out.switchingMode = data[i].switchingModeName
						}
					}, null, ajax.NO_ERROR_CATCH_CODE);

					ajax.post('/tetris/guide/control/output/setting/po/queryVideo', {taskNumber: 2}, function(data, status){
						console.log(data);
						self.dialog.setOut.video.codingObject = data.codingObjectName;
						self.dialog.setOut.video.fps = data.fps;
						self.dialog.setOut.video.bitrate = data.bitrate;
						self.dialog.setOut.video.resolution = data.resolutionName;
						self.dialog.setOut.video.ratio = data.ratioName;
						self.dialog.setOut.video.rcMode = data.rcModeName;
						self.dialog.setOut.video.maxBitrate = data.maxBitrate;
					}, null, ajax.NO_ERROR_CATCH_CODE);

					ajax.post('/tetris/guide/control/output/setting/po/queryAudio', {taskNumber: 2},function(data, status){
						self.dialog.setOut.audio.codingFormat = data.codingFormatName;
						self.dialog.setOut.audio.channelLayout = data.channelLayoutName;
						self.dialog.setOut.audio.bitrate = data.bitrate;
						self.dialog.setOut.audio.sampleRate = data.sampleRate;
						self.dialog.setOut.audio.codingType = data.codingTypeName;
					}, null, ajax.NO_ERROR_CATCH_CODE);

            		self.dialog.setOut.visible=true;
					self.dialog.setOut.type="";
					self.dialog.setOut.url="";
            	},
				handleSetOutCommit:function(){
					var self = this;
					self.dialog.setOut.visible=false;
					var questDataVideo = {
						id: 2,
						codingObject: self.dialog.setOut.video.codingObject,
						fps:self.dialog.setOut.video.fps,
						bitrate:self.dialog.setOut.video.bitrate,
						resolution:self.dialog.setOut.video.resolution,
						ratio:self.dialog.setOut.video.ratio,
						rcMode:self.dialog.setOut.video.rcMode,
						maxBitrate:self.dialog.setOut.video.maxBitrate,
					};

					var questDataAudio = {
						id: 2,
						codingFormat:self.dialog.setOut.audio.codingFormat,
						channelLayout:self.dialog.setOut.audio.channelLayout,
						bitrate:self.dialog.setOut.audio.bitrate,
						sampleRate:self.dialog.setOut.audio.sampleRate,
						codingType:self.dialog.setOut.audio.codingType,
					};

					var questDataOut = {
						id: 2,
						outputProtocol: self.dialog.setOut.out.outputProtocol,
						outputAddress: self.dialog.setOut.out.outputAddress,
						rateCtrl: self.dialog.setOut.out.rateCtrl,
						bitrate: self.dialog.setOut.out.bitrate,
						switchingMode: dialog.setOut.out.switchingMode
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
				getBackcolor:function(source){
					var self=this;
					if(source.current===true){
						return "background:#cc0033"
					}else{
						return "background:#CCC"
					}
				},
				switchSource:function(){
					var self = this;
					var questData = {
						id: self.curPgm
					};
					ajax.post('/tetris/guide/control/source/po/cut', questData, function (data, status) {
						for(var i = 0; i < self.sources.list.length; i++){
							if(self.sources.list[i].id === data.id){
								self.sources.list[i].current = true;
							}else{
								self.sources.list[i].current = false;
							}
						}
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

				ajax.post('/tetris/guide/control/source/po/query', {id: 2}, function(data, status){
					//console.log(data);
					for(var i = 0; i < data.length; i++){
						self.sources.list.push(data[i]);
					}
				})

				ajax.post('/tetris/guide/control/guide/po/query', null, function(data, status){
					for(var i = 0; i < data.length; i++){
						self.guides.list.push(data[i]);
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