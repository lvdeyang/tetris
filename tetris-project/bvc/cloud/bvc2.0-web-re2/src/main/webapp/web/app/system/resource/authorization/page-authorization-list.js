
define([
    'text!' + window.APPPATH + 'system/resource/authorization/page-authorization-list.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base',
    'bvc2-dialog-authorization-list',
    'extral'
], function(tpl, ajax, config, commons){

    var pageId = 'page-authorization-list';

    var init = function(p){

        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menurouter: false,
                shortCutsRoutes:commons.data,
                active:"/page-authorization-list",
                header:commons.getHeader(1),
                side:{
                    active:'0-10'
                },
                table:{
                    buttonCreate:'新建权限',
                    buttonRemove:'删除权限',
                    columns:[{
                        label:'权限名称',
                        prop:'name',
                        type:'simple'
                    },{
                        label:'备注',
                        prop:'remark',
                        type:'simple'
                    }],
                    load:'/system/authorization/load',
                    save:'/system/authorization/save',
                    update:'/system/authorization/update',
                    remove:'/system/authorization/remove',
                    removebatch:'/system/authorization/remove/all',
                    options:[{
                        label:'成员选择',
                        icon:'el-icon-setting',
                        click:'edit-authorization'
                    }],
                    pk:'id',
                    currentRow:''
                },
                authorization:{
                    row:'',
                    query:'/system/authorization/query/members/',
                    save:'/system/authorization/save/members/'
                }
            },
            methods:{
                editAuthorization:function(row,e){
                    var instance = this;

                    instance.authorization.row = row;
                    instance.$refs.$systemAuthorization.refreshAllMember(row);
                    instance.$refs.$systemAuthorization.dialogVisible = true;
                }
            }

        });
    };

    var destroy = function(){

    };

    var authorization = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return authorization;

});