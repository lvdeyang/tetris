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
                    dialog: {
                    	setSource:{
                    		visible: false,
                            loading: false,
                            name:'',
                            type:'',
                            device:{},
                            url:'',
                            typeOptions:['Stream','Device']
                    	},
                    	setOut:{
                    		visible: false,
                            loading: false,
                            type:'',
                            url:'',
                            typeOptions:['UDP','HLS']
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
            	},
            	handleSetOut:function(){
            		var self=this;
            		self.dialog.setOut.visible=true;
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