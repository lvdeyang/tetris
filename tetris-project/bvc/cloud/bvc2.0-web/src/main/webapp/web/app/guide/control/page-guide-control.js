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
                            typeOptions:['5G背包','直播流']
                    	},
                    	setOut:{
                    		visible: false,
                            loading: false,

							video:{
								codingObject: '',
								profile: 'main',
								fps: 25,
								bitrate: 1500,
								resolution: '',
								maxBitrate: 1500,
							},

							audio:{
								codingFormat:'',
								sampleFmt:'s16',
								bitrate:'128',
								codingType:'',
							},

							out:{
								outputProtocol: '',
								outputAddress: '',
								rateCtrl: 'VBR',
								bitrate: 8000000
							}

                            /*url:'',
                            typeOptions:['UDP','HLS']*/
                    	},
						setDevice:{
							visible: false,
                            loading: false,
							deviceData:[]
						}

                    },
					sources:{
						list:[]
					}
                }
            },
            computed:{
                
            },
            methods:{
				handleSelPgm: function () {
					var self = this;
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
				},
				handleSetSourceClose:function(){
					var self = this;
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
						profile:self.dialog.setOut.video.profile,
						fps:self.dialog.setOut.video.fps,
						bitrate:self.dialog.setOut.video.bitrate,
						resolution:self.dialog.setOut.video.resolution,
						maxBitrate:self.dialog.setOut.video.maxBitrate,
					};

					var questDataAudio = {
						id: 3,
						codingFormat: self.dialog.setOut.audio.codingFormat,
						sampleFmt: self.dialog.setOut.audio.sampleFmt,
						bitrate:self.dialog.setOut.audio.bitrate,
						codingType:self.dialog.setOut.audio.codingType,
					};

					var questDataOut = {
						id: 3,
						outputProtocal: self.dialog.setOut.out.outputProtocol,
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
				handleSetDeviceClick:function(){
					var self = this;
					self.dialog.setSource.visible = true;
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
						if(data[i].sourceTypeName == '5G背包'){
							self.dialog.setDevice.deviceData.push(data[i]);
						}
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