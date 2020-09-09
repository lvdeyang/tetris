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
                            name:'',
                            type:'',
                            device:{},
                            url:'',
							index:0,
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
                    }
                }
            },
            computed:{
                
            },
            methods:{
            	handleSetSource:function(index){
            		var self=this;
            		self.dialog.setSource.visible=true;
					self.dialog.setSource.index=index;
					self.dialog.setSource.type="";
					self.dialog.setSource.url="";
					self.dialog.setSource.previewOut="";
					self.dialog.setSource.device={};
            	},
				handleSetSourceCommit:function(){
					var self = this;
					self.dialog.setSource.visible=false;
					var questData = {
						type: self.dialog.setSource.type,
						url:self.dialog.setSource.url,
						deviceId:self.dialog.setSource.device.id,
						name:self.dialog.setSource.device.name,
						index:self.dialog.setSource.index,
						previewOut:self.dialog.setSource.previewOut
					};
					ajax.post('/guide/control/source/set', questData, function (data, status) {
						
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
					
				},
				stopGuide:function(){
					
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
						index: self.curPgm
					};
					ajax.post('/guide/control/source/switch', questData, function (data, status) {
						
					}, null, ajax.NO_ERROR_CATCH_CODE);
				}
               
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