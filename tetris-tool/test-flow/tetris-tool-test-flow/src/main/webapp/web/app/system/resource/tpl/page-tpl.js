define([
    'text!' + window.APPPATH + 'system/resource/tpl/page-tpl.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-tpl';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_tpl = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                header:commons.getHeader(1),
                side:{
                    active:'0-6'
                },
                table:{
                    buttonCreate:'新建模板',
                    buttonRemove:'删除模板',
                    columns:[{
                        label:'模板名称',
                        prop:'name',
                        type:'simple'
                    },{
                        label:'业务角色',
                        prop:'roles',
                        type:'entity',
                        entity:{
                            value:'id',
                            label:'name',
                            table:{
                                pk:'id',
                                label:'name',
                                title:'选择角色',
                                load:'/system/business/role/query/all',
                                width:'30%',
                                columns:[{
                                    label:'角色名称',
                                    prop:'name'
                                }]
                            }
                        },
                        width:400,
                    },{
                        label:'布局方案',
                        prop:'layouts',
                        type:'entity',
                        entity:{
                            label:'name',
                            value:'id',
                            table:{
                                pk:'id',
                                label:'name',
                                title:'选择布局方案',
                                load:'/system/screen/layout/query/all',
                                width:'30%',
                                columns:[{
                                    label:'布局名称',
                                    prop:'name'
                                }]
                            }
                        },
                        width:400,
                    },{
                        label:'录制方案',
                        prop:'records',
                        type:'entity',
                        entity:{
                            label:'name',
                            value:'id',
                            table:{
                                pk:'id',
                                label:'name',
                                title:'选择录制方案',
                                load:'/system/record/scheme/query/all',
                                columns:[{
                                    label:'录制名称',
                                    prop:'name'
                                },{
                                    label:'录制角色',
                                    prop:'roleName'
                                }]
                            }
                        },
                        width:400,
                    }],
                    load:'/system/tpl/load',
                    save:'/system/tpl/save',
                    update:'/system/tpl/update',
                    remove:'/system/tpl/remove',
                    removebatch:'/system/tpl/remove/all',
                    pk:'id'
                }
            },
            methods:{
                beforeSave:function(row, done){
                    done({
                        name:row.name,
                        roles:row.roles? $.toJSON(row.roles):$.toJSON([]),
                        layouts:row.layouts? $.toJSON(row.layouts): $.toJSON([]),
                        records:row.records? $.toJSON(row.records): $.toJSON([])
                    });
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