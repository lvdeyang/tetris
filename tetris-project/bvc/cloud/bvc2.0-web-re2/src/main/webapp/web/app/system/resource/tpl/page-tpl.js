define([
    'text!' + window.APPPATH + 'system/resource/tpl/page-tpl.html',
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

    var pageId = 'page-tpl';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_tpl = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menurouter: false,
                shortCutsRoutes:commons.data,
                active:"/page-tpl",
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
                        label:'自动建立议程',
                        prop:'autoBuildAgendas',
                        type:'entity',
                        entity:{
                            label:'name',
                            value:'id',
                            table:{
                                pk:'id',
                                label:'name',
                                title:'选择自动建立的议程',
                                load:'/system/agenda/scheme/query/all',
                                columns:[{
                                    label:'议程名称',
                                    prop:'name'
                                }]
                            }
                        },
                        width:300,
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
                        width:200,
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
                        width:200,
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
                    var instance = this,
                        _columns = instance.table.columns;

                    //空校验
                    for(var i=0;i<_columns.length;i++){
                        if(_columns[i].type === 'simple'){
                            if(row[_columns[i].prop] == null || row[_columns[i].prop] == ''){
                                instance.$message({
                                    message: _columns[i].label + '不能为空！',
                                    type: 'warning'
                                });

                                return;
                            }
                        }
                    }

                    done({
                        name:row.name,
                        roles:row.roles? $.toJSON(row.roles):$.toJSON([]),
                        layouts:row.layouts? $.toJSON(row.layouts): $.toJSON([]),
                        records:row.records? $.toJSON(row.records): $.toJSON([]),
                        autoBuildAgendaIds:row.autoBuildAgendas? joinAutoBuildAgendaIds(row.autoBuildAgendas): ""
                    });
                }
            }
        });

    };
    
    var joinAutoBuildAgendaIds = function(agendas){
    	var idArray = [];
    	for(var i in agendas){
    		idArray.push(agendas[i].id);
    	}
    	idArray.sort();
    	var ids = idArray.join(",");
    	return ids;
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