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
							id:0,
                            sourceName:'',
                            sourceType:'',
							source:'',
							previewOut:'',
                            typeOptions:['直播流','5G背包']
                    	},
                    	setOut:{
                    		visible: false,
                            loading: false,
                            type:'',
                            url:'',
                            typeOptions:['UDP','HLS']
                    	},
						selDevice:{
							visible: false,
                            loading: false,
							deviceData:[],
							
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
				handleSelDeviceClose:function(){
					var self = this;
					self.dialog.setDevice.visible = false;
				},
            	handleSetSource:function(sourceNumber){
            		var self=this;
					var x;
					for(var i = 0; i < self.sources.list.length; i++){
						if(self.sources.list[i].index == sourceNumber){
							x = self.sources.list[i];
							break;
						}
					}
            		self.dialog.setSource.visible=true;
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
						sourceName:self.dialog.setSource.sourceName,
						sourceType: self.dialog.setSource.sourceType,
						source:self.dialog.setSource.source,
						previewOut:self.dialog.setSource.previewOut
					};
					ajax.post('/tetris/guide/control/source/po/edit', questData, function (data, status) {

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
					var questData = {
						type: self.dialog.setOut.type,
						url:self.dialog.setOut.url
					};
					ajax.post('/guide/control/output/set', questData, function (data, status) {
						
					}, null, ajax.NO_ERROR_CATCH_CODE);

				},
				handleSetOutClose:function(){
					var self=this;
            		self.dialog.setOut.visible=false;
				},
				handleDeviceSelect:function(){
					var self=this;
					self.dialog.setSource.visible=false;
					self.dialog.selDevice.visible=true;
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
				}

               
            },
			created:function(){
				var self = this;
/*
				ajax.post('/tetris/guide/control/guide/po/query', null, function(data){

				});
*/

				ajax.post('/tetris/guide/control/source/po/query', {id: 1}, function(data, status){
					console.log(data);
					for(var i = 0; i < data.length; i++){
						self.sources.list.push(data[i]);
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