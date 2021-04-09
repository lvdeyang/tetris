define([
    'text!' + window.APPPATH + 'jv210/layer/page-layer.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base',
    'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-layer';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

       
           
            
            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_layertpl = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-layer",
                    header:commons.getHeader(1),
                    side:{
                        active:'2-1'
                    },
                    layer:{
                        buttonCreate:'新建',
                        buttonRemove:'删除',
                        columns:[{
                            label:'layerId',
                            prop:'layerId',
                            type:'simple'
                        },{
                            label:'IP',
                            prop:'layerIp',
                            type:'simple'
                           
                        },{
                            label:'类型',
                            prop:'type',
                            type:'simple'
                           
                        },{
                            label:'版本',
                            prop:'version',
                            type:'simple'
                           
                        }],
                        load:'/net/layer/load/new',
                        save:'/net/layer/save',
                        update:'/net/layer/update',
                        remove:'/net/layer/remove',
                        removebatch:'/net/layer/remove/all',
                        options:[{
                            label:'设置',
                            icon:'el-icon-setting',
                            click:'edit-config'
                        },{
                            label:'查看状态',
                            icon:'icon-sitemap',
                            click:'get-status'
                        }],
                        pk:'id'
                    }
                },
                methods:{
                    editConfig:function(row){
                        window.location.hash = '#/page-jv210-config/' + row.layerIp;
                    },
                    getStatus:function(row){
                    	
                    	if(row.type=='ACCESS_MIXER'){
                    		window.location.hash = '#/page-mixer-status/' + row.layerIp;
                    	}else{
                    		window.location.hash = '#/page-jv210-status/' + row.layerIp+'/'+row.type;
                    	}
                    	
                    }
                }
            });


    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});