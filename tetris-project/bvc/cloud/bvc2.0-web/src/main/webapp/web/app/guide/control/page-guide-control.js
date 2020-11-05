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
					editableTabsValue2: null,
					guides:{
						list: []
					},
					outputGroups:{
						list: []
					},
                	user:context.getProp('user'),
                	menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-group-list",
					curPgm:1,
					curPvm:1,
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
							isPreviewOut: false,
							sourceProtocol: ''
                    	},
                    	setOut:{
                    		visible: false,
                            loading: false,

/*							video:{
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
							}*/
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
					}
                }
            },
            computed:{
                
            },
            methods:{
				addTab2: function(){

				},
				removeTab2: function(){

				},
				handleClick2: function(){

				},
				newOut: function(){
					var self = this;
					/*var data = {
						outputProtocol: '',
						outputAddress: '',
						rateCtrl: '',
						bitrate: null,
					}*/
					ajax.post('/tetris/guide/control/output/setting/po/add', { groupId: self.outputGroups.list[0].id }, function(data){
						data.isCheck = false;
						self.output.out.push(data);
					})
				},
				deleteOut: function(){
					var self = this;
					/*for(var i = 0; i < self.output.out.length; i++){
						if(self.output.out[i].isCheck === true){
							ajax.post('/tetris/guide/control/output/setting/po/delete', { id: self.output.out[i].id }, function(data){
								self.output.out.splice(i, 1);
							});
						}
					}*/
					var arr = [];
					for(var i = 0; i < self.output.out.length; i++){
						if(self.output.out[i].isCheck === true){
							arr.push(self.output.out[i].id);
						}
					}

					ajax.post('/tetris/guide/control/output/setting/po/delete',{ids:$.toJSON(arr)}, function(data){
						/*for(var i = 0; i < self.output.out.length; i++) {
							if (self.output.out[i].isCheck === true) {
								self.output.out.splice(i, 1);
							}
						}*/
						for(var i = 0; i < arr.length; i++){
							for(var j = 0; j < self.output.out.length; j++){
								if(self.output.out[j].id === arr[i]){
									self.output.out.splice(j, 1);
									break;
								}
							}
						}
					})
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
					self.dialog.setSource.sourceProtocol = x.sourceProtocolName;
            	},
				handleSetSourceCommit:function(){
					var self = this;
					if(self.dialog.setSource.sourceType == null){
						this.$message('源类型不能为空');
						return;
					}
					if(self.dialog.setSource.sourceType === '直播流' && self.dialog.setSource.source == null){
						this.$message('源地址不能为空');
						return;
					}
					if(self.dialog.setSource.sourceProtocol == null){
						this.$message('源协议不能为空');
						return;
					}
					if(self.dialog.setSource.sourceName == null){
						this.$message('源名称不能为空');
						return;
					}
					if(self.dialog.setSource.isPreviewOut === true && self.dialog.setSource.previewOut == null){
						this.$message('预监输出不能为空');
						return;
					}
					var questData = {
						id:self.dialog.setSource.id,
						sourceType: self.dialog.setSource.sourceType,
						source:self.dialog.setSource.source,
						sourceName:self.dialog.setSource.sourceName,
						previewOut:self.dialog.setSource.previewOut,
						isPreviewOut:self.dialog.setSource.isPreviewOut,
						sourceProtocol:self.dialog.setSource.sourceProtocol
					};
					ajax.post('/tetris/guide/control/source/po/edit', questData, function (data, status) {
						for(var i = 0; i < self.sources.list.length; i++){
							if(data.id === self.sources.list[i].id){
								self.sources.list.splice(i,1,data);
								break;
							}
						}
						self.dialog.setSource.visible = false;
						self.dialog.setSource.sourceType = '';
						self.dialog.setSource.source = '';
						self.dialog.setSource.sourceName = '';
						self.dialog.setSource.previewOut = '';
						self.dialog.setSource.isPreviewOut = false;
						self.dialog.setSource.sourceProtocol = '';
					}, null, ajax.NO_ERROR_CATCH_CODE);
				},
				handleSetSourceClose:function(){
					var self = this;
					self.$refs['setSource'].resetFields();
					self.dialog.setSource.visible=false;
				},
            	handleSetOut:function(){
					var self = this;
					self.outputGroups.list.splice(0, self.outputGroups.list.length);
					ajax.post('/tetris/guide/control/output/group/po/query',{ guideId: self.editableTabsValue }, function(data, status){
						for(var i = 0; i < data.length; i++){
							self.outputGroups.list.push(data[i]);
						}
						self.editableTabsValue2 = data[0].id + '';

						self.output.out.splice(0, self.output.out.length);
						ajax.post('/tetris/guide/control/output/setting/po/query', { groupId: self.outputGroups.list[0].id }, function(data, status){
							//console.log(data);
							for(var i = 0; i < data.length; i++){
								data[i].isCheck = false;
								self.output.out.push(data[i]);
								/*self.dialog.setOut.out.id = data[i].id;
								 self.dialog.setOut.out.outputProtocol = data[i].outputProtocolName;
								 self.dialog.setOut.out.outputAddress = data[i].outputAddress;
								 self.dialog.setOut.out.rateCtrl = data[i].rateCtrl;
								 self.dialog.setOut.out.bitrate = data[i].bitrate;*/
							}
						}, null, ajax.NO_ERROR_CATCH_CODE);

					}, null, ajax.NO_ERROR_CATCH_CODE);
					/*ajax.post('/tetris/guide/control/output/setting/po/queryVideo', { groupId: 1 }, function(data, status){
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

					ajax.post('/tetris/guide/control/output/setting/po/queryAudio', { groupId: 1 },function(data, status){
						self.dialog.setOut.audio.id = data.id;
						self.dialog.setOut.audio.codingFormat = data.codingFormatName;
						self.dialog.setOut.audio.channelLayout = data.channelLayoutName;
						self.dialog.setOut.audio.bitrate = data.bitrate;
						self.dialog.setOut.audio.sampleRate = data.sampleRate;
						self.dialog.setOut.audio.codingType = data.codingTypeName;
					}, null, ajax.NO_ERROR_CATCH_CODE);*/

            		self.dialog.setOut.visible=true;
					self.dialog.setOut.type="";
					self.dialog.setOut.url="";
            	},
				handleSetOutCommit:function(){
					var self = this;
					/*var questDataVideo = {
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

					var questDataOut = {
						id: self.dialog.setOut.out.id,
						outputProtocol: self.dialog.setOut.out.outputProtocol,
						outputAddress: self.dialog.setOut.out.outputAddress,
						rateCtrl: self.dialog.setOut.out.rateCtrl,
						bitrate: self.dialog.setOut.out.bitrate,
						switchingMode: self.dialog.setOut.out.switchingMode
					}*/
					/*ajax.post('/tetris/guide/control/output/setting/po/editVideo', questDataVideo, function (data, status) {
						
					}, null, ajax.NO_ERROR_CATCH_CODE);

					ajax.post('/tetris/guide/control/output/setting/po/editAudio', questDataAudio, function (data, status) {

					}, null, ajax.NO_ERROR_CATCH_CODE);*/

					//console.log(questDataOut);
					/*for(var i = 0; i < self.output.out.length; i++){
						var questDataOut = {
							id: self.output.out[i].id,
							outputProtocol: self.output.out[i].outputProtocolName,
							outputAddress: self.output.out[i].outputAddress,
							rateCtrl: self.output.out[i].rateCtrl,
							bitrate: self.output.out[i].bitrate
						}
						ajax.post('/tetris/guide/control/output/setting/po/edit', questDataOut, function (data, status) {

						}, null, ajax.NO_ERROR_CATCH_CODE);
					}*/
					for(var i = 0; i < self.outputGroups.list.length; i++){
						if(self.outputGroups.list[i].switchingModeName == null){
							this.$message('切换方式不能为空');
							return;
						}
						if(self.outputGroups.list[i].switchingModeName === '转码' && self.outputGroups.list[i].transcodingTemplate == null){
							this.$message('转码模板不能为空');
							return;
						}
					}
					for(var i = 0; i < self.output.out.length; i++){
						var reudp = /^udp\:\/\/(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
						var rertp = /^rtp\:\/\/(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
						var rertmp = /^rtmp\:\/\/(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
						var resrt = /^srt\:\/\/(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;


						if(self.output.out[i].outputProtocolName == null){
							this.$message('输出协议不能为空');
							return;
						}
						if(self.output.out[i].outputAddress == null){
							this.$message('输出地址不能为空');
							return;
						}
						if((self.output.out[i].outputProtocolName === 'udp_ts') && (!(reudp.test(self.output.out[i].outputAddress)))){
							this.$message('输出地址格式错误！正确格式举例：udp://10.10.40.24:15024');
							return;
						}
						if((self.output.out[i].outputProtocolName === 'rtp_ts') && (!(rertp.test(self.output.out[i].outputAddress)))){
							this.$message('输出地址格式错误！正确格式举例：rtp://10.10.40.24:15024');
							return;
						}
						if((self.output.out[i].outputProtocolName === 'rtmp') && (!(rertmp.test(self.output.out[i].outputAddress)))){
							this.$message('输出地址格式错误!正确格式举例：rtmp://10.10.40.24:15024');
							return;
						}
						if((self.output.out[i].outputProtocolName === 'srt') && (!(resrt.test(self.output.out[i].outputAddress)))){
							this.$message('输出地址格式错误!正确格式举例：srt://10.10.40.24:15024');
							return;
						}
						if(self.output.out[i].rateCtrlName == null){
							this.$message('码率控制方式不能为空');
							return;
						}
						if(self.output.out[i].bitrate == null){
							this.$message('系统码率不能为空');
							return;
						}
						//console.log(self.output.out[i].bitrate);
						//console.log(typeof self.output.out[i].bitrate);
						if(parseInt(self.output.out[i].bitrate) === NaN){
							this.$message('系统码率必须为整数值');
							return;
						}
						if(!(parseInt(self.output.out[i].bitrate) == self.output.out[i].bitrate)){
							this.$message('系统码率必须为整数值');
							return;
						}
						/*if(!Number.isInteger(self.output.out[i].bitrate)){
							this.$message('系统码率必须为整数值');
							return;
						}*/
					}
					ajax.post('/tetris/guide/control/output/group/po/edit', { groups: $.toJSON(self.outputGroups.list) }, function(data, status){
						for(var i = 0; i < self.outputGroups.list.length; i++){
							self.outputGroups.list[i].switchingModeName = '';
							self.outputGroups.list[i].transcodingTemplate = '';
						}
						self.dialog.setOut.visible=false;
					}, null, ajax.NO_ERROR_CATCH_CODE);
					ajax.post('/tetris/guide/control/output/setting/po/edit',{ outputs: $.toJSON(self.output.out) },function(data, status){
						for(var i = 0; i < self.output.out.length; i++){
							self.output.out[i].outputProtocolName = '';
							self.output.out[i].outputAddress = '';
							self.output.out[i].rateCtrl = '';
							self.output.out[i].bitrate = '';
							self.output.out[i].outType = '';
						}
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
				handleSelPvm:function(index){
					var self=this;
					self.curPvm=index;
					
					var questData = {
						id: self.curPvm
					};
					ajax.post('/tetris/guide/control/source/po/pvmcut', questData, function (data, status) {
						for(var i = 0; i < self.sources.list.length; i++){
							if(self.sources.list[i].id === data.id){
								self.sources.list[i].pvmCurrent = true;
							}else{
								self.sources.list[i].pvmCurrent = false;
							}
						}
					}, null, ajax.NO_ERROR_CATCH_CODE);
				},
				getBackcolor:function(source){
					var self=this;
					if(source.current===true){
						return "background:#cc0033"
					}else{
						return "background:#CCC"
					}
				},
				getPvmBackcolor:function(source){
					var self=this;
					if(source.pvmCurrent===true){
						return "background:#339999"
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
					for(var i = 0; i < data.length; i++){
						self.guides.list.push(data[i]);
					}
					self.editableTabsValue = data[0].id + '';

					self.handleClick({ name:self.editableTabsValue });

					/*ajax.post('/tetris/guide/control/source/po/query', {id: self.editableTabsValue}, function(data, status){
						//console.log(data);
						for(var i = 0; i < data.length; i++){
							self.sources.list.push(data[i]);
						}
					})*/

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