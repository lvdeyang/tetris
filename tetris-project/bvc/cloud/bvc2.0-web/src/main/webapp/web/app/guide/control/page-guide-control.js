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
                return {
					editableTabsValue: null,
					guides:{
						list: []
					},
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
							isPreviewOut: false
                    	},
                    	setOut:{
                    		visible: false,
                            loading: false,

							video:{
								id: 0,
								codingObject: '',
								fps: '25',
								bitrate: 1500,
								resolution: '',
								ratio: '',
								rcMode: '',
								maxBitrate: 1500
							},

							audio:{
								id: 0,
								codingFormat:'',
								channelLayout:'',
								bitrate:'128',
								sampleRate:'44.1',
								codingType:''
							},

							out:{
								id: 0,
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
				newOut: function(){
					var self = this;
					var data = {
						outputProtocol: '',
						outputAddress: '',
						rateCtrl: '',
						bitrate: null,
					}
					self.output.out.push(data);
				},
				deleteOut: function(){
					var self = this;
					self.output.out.pop();
				},
				handleSelPgm: function () {
					var self = this;
				},
				addTab: function(){
					var self = this;
					var listLength = this.guides.list.length +1;
					ajax.post('/tetris/guide/control/guide/po/add', {taskName: ("导播任务"+listLength)}, function(data){
						self.guides.list.push(data);
						self.editableTabsValue = data.id+'';
						self.handleClick({name:data.id+''});
					});
				},
				removeTab: function(targetName) {
					var self = this;
					ajax.post('/tetris/guide/control/guide/po/delete', {id: targetName}, function (data) {
						for(var i = 0; i < self.guides.list.length; i++){
							if(self.guides.list[i].id == targetName){
								self.guides.list.splice(i, 1);
								break;
							}
						}
						if(self.editableTabsValue === targetName && self.guides.list.length > 0){
							self.editableTabsValue = self.guides.list[0].id+'';
							self.handleClick({name:self.guides.list[0].id+''});
						}
					});
				},
				handleClick:function(tab, event){
					//console.log(tab);
					var self = this;
					self.sources.list.splice(0, self.sources.list.length);
					self.output.out.splice(0, self.output.out.length);
					ajax.post('/tetris/guide/control/source/po/query', {id: tab.name}, function(data, status){
						for(var i = 0; i < data.length; i++){
							self.sources.list.push(data[i]);
						}
					})
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
					if(x.isPreviewOut != null){
						self.dialog.setSource.isPreviewOut = x.isPreviewOut;
					}
            	},
				handleSetSourceCommit:function(){
					var self = this;
					self.dialog.setSource.visible=false;
					var questData = {
						id:self.dialog.setSource.id,
						sourceType: self.dialog.setSource.sourceType,
						source:self.dialog.setSource.source,
						sourceName:self.dialog.setSource.sourceName,
						previewOut:self.dialog.setSource.previewOut,
						isPreviewOut:self.dialog.setSource.isPreviewOut
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
					ajax.post('/tetris/guide/control/output/setting/po/query', {taskNumber: self.editableTabsValue}, function(data, status){
						//console.log(data);
						for(var i = 0; i < data.length; i++){
							self.output.out.push(data[i]);
							self.dialog.setOut.out.id = data[i].id;
							self.dialog.setOut.out.outputProtocol = data[i].outputProtocolName;
							self.dialog.setOut.out.outputAddress = data[i].outputAddress;
							self.dialog.setOut.out.rateCtrl = data[i].rateCtrl;
							self.dialog.setOut.out.bitrate = data[i].bitrate;
							self.dialog.setOut.out.switchingMode = data[i].switchingModeName
						}
					}, null, ajax.NO_ERROR_CATCH_CODE);

					ajax.post('/tetris/guide/control/output/setting/po/queryVideo', {taskNumber: self.editableTabsValue}, function(data, status){
						console.log(data);
						self.dialog.setOut.video.id = data.id;
						self.dialog.setOut.video.codingObject = data.codingObjectName;
						self.dialog.setOut.video.fps = data.fps;
						self.dialog.setOut.video.bitrate = data.bitrate;
						self.dialog.setOut.video.resolution = data.resolutionName;
						self.dialog.setOut.video.ratio = data.ratioName;
						self.dialog.setOut.video.rcMode = data.rcModeName;
						self.dialog.setOut.video.maxBitrate = data.maxBitrate;
					}, null, ajax.NO_ERROR_CATCH_CODE);

					ajax.post('/tetris/guide/control/output/setting/po/queryAudio', {taskNumber: self.editableTabsValue},function(data, status){
						self.dialog.setOut.audio.id = data.id;
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
						id: self.dialog.setOut.video.id,
						codingObject: self.dialog.setOut.video.codingObject,
						fps:self.dialog.setOut.video.fps,
						bitrate:self.dialog.setOut.video.bitrate,
						resolution:self.dialog.setOut.video.resolution,
						ratio:self.dialog.setOut.video.ratio,
						rcMode:self.dialog.setOut.video.rcMode,
						maxBitrate:self.dialog.setOut.video.maxBitrate,
					};

					var questDataAudio = {
						id: self.dialog.setOut.audio.id,
						codingFormat:self.dialog.setOut.audio.codingFormat,
						channelLayout:self.dialog.setOut.audio.channelLayout,
						bitrate:self.dialog.setOut.audio.bitrate,
						sampleRate:self.dialog.setOut.audio.sampleRate,
						codingType:self.dialog.setOut.audio.codingType,
					};

/*					var questDataOut = {
						id: self.editableTabsValue,
						outputProtocol: self.dialog.setOut.out.outputProtocol,
						outputAddress: self.dialog.setOut.out.outputAddress,
						rateCtrl: self.dialog.setOut.out.rateCtrl,
						bitrate: self.dialog.setOut.out.bitrate,
						switchingMode: self.dialog.setOut.out.switchingMode
					}*/
					ajax.post('/tetris/guide/control/output/setting/po/editVideo', questDataVideo, function (data, status) {
						
					}, null, ajax.NO_ERROR_CATCH_CODE);

					ajax.post('/tetris/guide/control/output/setting/po/editAudio', questDataAudio, function (data, status) {

					}, null, ajax.NO_ERROR_CATCH_CODE);

					//console.log(questDataOut);
					for(var i = 0; i < self.output.out.length; i++){
						var questDataOut = {
							id: self.output.out[i].id,
							outputProtocol: self.output.out[i].outputProtocol,
							outputAddress: self.output.out[i].outputAddress,
							rateCtrl: self.output.out[i].rateCtrl,
							bitrate: self.output.out[i].bitrate,
							switchingMode: self.dialog.setOut.out.switchingMode
						}
						ajax.post('/tetris/guide/control/output/setting/po/edit', questDataOut, function (data, status) {

						}, null, ajax.NO_ERROR_CATCH_CODE);
					}
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
					var self = this;
					ajax.post('/tetris/guide/control/guide/po/start',{id: self.editableTabsValue},function(data, status){

					})
				},
				stopGuide:function(){
					var self = this;
					ajax.post('/tetris/guide/control/guide/po/stop',{id: self.editableTabsValue},function(data, status){

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

				ajax.post('/tetris/guide/control/guide/po/query', null, function(data, status){
					if(data && data.length > 0){
						for(var i = 0; i < data.length; i++){
							self.guides.list.push(data[i]);
						}
						self.editableTabsValue = self.guides.list[0].id + '';
						self.handleClick({name:self.guides.list[0].id+''});
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