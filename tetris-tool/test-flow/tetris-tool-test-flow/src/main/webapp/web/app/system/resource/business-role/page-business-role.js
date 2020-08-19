define([
    'text!' + window.APPPATH + 'system/resource/business-role/page-business-role.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-business-role';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/system/business/role/query/code', null, function(data){
            //数据转换
            var specialOptions = [];
            var specialArr = data.special;
            if(specialArr && specialArr.length>0){
                for(var i=0; i<specialArr.length; i++){
                    specialOptions.push({
                        label:specialArr[i],
                        value:specialArr[i]
                    });
                }
            }

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_businessRole = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    header:commons.getHeader(1),
                    side:{
                        active:'0-2'
                    },
                    role:{
                        buttonCreate:'新建角色',
                        buttonRemove:'删除角色',
                        columns:[{
                            label:'角色名称',
                            prop:'name',
                            type:'simple'
                        },{
                            label:'角色属性',
                            prop:'special',
                            type:'select',
                            options:specialOptions,
                            width:'240'
                        }],
                        load:'/system/business/role/load',
                        save:'/system/business/role/save',
                        update:'/system/business/role/update',
                        remove:'/system/business/role/remove',
                        removebatch:'/system/business/role/remove/all',
                        pk:'id'
                    }
                }
            });
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