define([
    'text!' + window.APPPATH + 'system/resource/record-scheme/page-record-scheme.html',
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

    var pageId = 'page-record-scheme';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/system/record/scheme/query/code', null, function(data){
            var roleArr = data.role;
            var roleOptions = [];
            if(roleArr && roleArr.length>0){
                for(var i=0; i<roleArr.length; i++){
                    roleOptions.push({
                        label:roleArr[i].name,
                        value:roleArr[i].id
                    });
                }
            }

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_recordScheme = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-record-scheme",
                    header:commons.getHeader(1),
                    side:{
                        active:'0-5'
                    },
                    table:{
                        buttonCreate:'新建录制',
                        buttonRemove:'删除录制',
                        columns:[{
                            label:'录制名称',
                            prop:'name',
                            type:'simple'
                        },{
                            label:'录制角色',
                            prop:'roleName',
                            hidden:'roleId',
                            type:'select',
                            options:roleOptions,
                            width:'240'
                        }],
                        load:'/system/record/scheme/load',
                        save:'/system/record/scheme/save',
                        update:'/system/record/scheme/update',
                        remove:'/system/record/scheme/remove',
                        removebatch:'/system/record/scheme/remove/all',
                        pk:'id'
                    }
                },
                methods:{
                    beforeSave:function(row, done){
                        var instance = this,
                            _columns = instance.table.columns;

                        //空校验
                        for(var i=0;i<_columns.length;i++){
                            if(_columns[i].type){
                                if(row[_columns[i].prop] == null || row[_columns[i].prop] == ''){
                                    instance.$message({
                                        message: _columns[i].label + '不能为空！',
                                        type: 'warning'
                                    });

                                    return;
                                }
                            }
                        }

                        done();
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