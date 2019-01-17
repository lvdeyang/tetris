define([
    'text!' + window.APPPATH + 'group/list/page-group-list.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-tree-source-list',
    'bvc2-system-table-base',
    'bvc2-tree-meeting-member',
    'bvc2-dialog-edit-role'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-list';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/device/group/query/code', null, function(data){

            //转换数据格式
            var typeOptions = [];
            var typeArr = data.type;
            if(typeArr && typeArr.length>0){
                for(var i=0; i<typeArr.length; i++){
                    typeOptions.push({
                        label:typeArr[i],
                        value:typeArr[i]
                    });
                }
            }

            var transmissionModeOptions = [];
            var transmissionModeArr = data.transmissionMode;
            if(transmissionModeArr && transmissionModeArr.length>0){
                for(var i=0; i<transmissionModeArr.length; i++){
                    transmissionModeOptions.push({
                        label:transmissionModeArr[i],
                        value:transmissionModeArr[i]
                    });
                }
            }

            var avtplOptions = [];
            var avtplArr = data.avtpl;
            if(avtplArr && avtplArr.length>0){
                for(var i=0; i<avtplArr.length; i++){
                    avtplOptions.push({
                        label:avtplArr[i].name,
                        value:avtplArr[i].id
                    });
                }
            }

            var tplOptions = [];
            var tplArr = data.tpl;
            if(tplArr && tplArr.length>0){
                for(var i=0; i<tplArr.length; i++){
                    tplOptions.push({
                        label:tplArr[i].name,
                        value:tplArr[i].id
                    });
                }
            }

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_groupList = window.v_groupList = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    header:commons.getHeader(0),
                    sourcelist:'',
                    checked:[],
                    members:[],
                    roles:[],
                    roleMembers:[],
                    table:{
                        buttonCreate:'新建设备组',
                        buttonRemove:'删除设备组',
                        columns:[{
                            label:'组名称',
                            prop:'name',
                            type:'simple'
                        },{
                            label:'组类型',
                            prop:'type',
                            type:'select',
                            options:typeOptions,
                            width:'200'
                        },{
                            label:'传输模式',
                            prop:'transmissionMode',
                            type:'select',
                            options:transmissionModeOptions,
                            width:'200'
                        },{
                            label:'参数方案',
                            prop:'avtplName',
                            hidden:'avtplId',
                            type:'select',
                            options:avtplOptions,
                            width:'200'
                        },{
                            label:'会议模板',
                            prop:'systemTplName',
                            hidden:'systemTplId',
                            type:'select',
                            options:tplOptions,
                            width:'200'
                        }],
                        options:[{
                            label:'编辑角色',
                            icon:'icon-user',
                            iconStyle:'font-size:15px;',
                            click:'edit-role'
                        },{
                            label:'设备组控制',
                            icon:'icon-signin',
                            iconStyle:'font-size:16px;',
                            click:'enter-group'
                        }],
                        load:'/device/group/load/' ,
                        save:'/device/group/save/',
                        update:'/device/group/update',
                        remove:'/device/group/remove',
                        removebatch:'/device/group/remove/all',
                        pk:'id',
                        highlight:true
                    }
                },
                methods:{
                    editRole:function(row, e){
                        var instance = this;
                        console.log('编辑角色');
                        ajax.get('/device/group/query/role/' + row.id, null, function(data){
                            instance.roles.splice(0, instance.roles.length);
                            instance.roleMembers.splice(0, instance.roleMembers.length);

                            var _roles = data.roles,
                                _roleMembers = data.members;

                            for(var i=0;i<_roles.length;i++){
                                _roles[i].checked = [];
                                for(var k=0;k<_roles[i].members.length;k++){
                                    _roles[i].checked.push(_roles[i].members[k].id);
                                }
                                instance.roles.push(_roles[i]);
                            }

                            for(var j=0;j<_roleMembers.length;j++){
                                instance.roleMembers.push(_roleMembers[j]);
                            }

                            instance.$refs.$editRole.roleVisible = true;
                            instance.$refs.$editRole.value.splice(0, instance.$refs.$editRole.value.length);
                            instance.$refs.$editRole.deviceData.splice(0, instance.$refs.$editRole.deviceData.length);
                        });

                    },
                    enterGroup:function(row, e){
                        window.location.hash = '#/page-group-control/' + row.id;
                    },
                    changeComponentSourceList:function(){
                        var instance = this;
                        instance.checked.splice(0,instance.checked.length);
                        instance.sourcelist = "bvc2-tree-source-list";
                    },
                    changeComponentNull:function(){
                        this.sourcelist = "";
                    },
                    changeComponentMemberList:function(row, e){
                        var instance = this;

                        ajax.get('/device/group/control/query/tree/' + row.id, null, function(data){
                            var membersTree = data.membersTree;
                            instance.members.splice(0,instance.members.length);
                            for(var i=0;i<membersTree.length;i++){
                                instance.members.push(membersTree[i]);
                            }
                            instance.sourcelist = "bvc2-tree-meeting-member";
                        });
                    },
                    rowAddSaveBefore:function(data, done, e){
                        var instance = this,
                            checkedSource,
                            newData;

                        checkedSource = instance.$refs.$sourceList.getCheckedNodes();

                        for(var i=checkedSource.length-1;i>=0;i--){
                            if(checkedSource[i].type != 'BUNDLE'){
                                checkedSource.splice(i,1);
                            }
                        }

                        newData = Object.assign({}, data, {sourceList: $.toJSON(checkedSource)});

                        done(newData);
                    },
                    rowEditSaveBefore:function(data, done, e){
                        var instance = this,
                            checkedSource,
                            newData;

                        checkedSource = instance.$refs.$sourceList.getCheckedNodes();

                        for(var i=checkedSource.length-1;i>=0;i--){
                            if(checkedSource[i].type != 'BUNDLE'){
                                checkedSource.splice(i,1);
                            }
                        }

                        newData = Object.assign({}, data, {sourceList: $.toJSON(checkedSource)});

                        done(newData);
                    },
                    rowEdit:function(row, done, e){
                        var instance = this;
                        ajax.get('/device/group/query/bundle/' + row.id, null, function(data){
                            var bundles = data.bundles;
                            instance.checked.splice(0,instance.checked.length);
                            for(var j=0;j<bundles.length;j++){
                                instance.checked.push(bundles[j].key);
                            }
                            instance.sourcelist = "bvc2-tree-source-list";
                            done();
                        });
                    },
                    rowEditCancel:function(row, done, e){
                        var instance = this;
                        instance.sourcelist = "";
                        done();
                    },
                    saveRole:function(members){
                        var instance = this;
                        var _members = {members: $.toJSON(members)};

                        ajax.post('/device/group/update/role', _members, function(data){
                            instance.$refs.$editRole.roleVisible = false;
                            v_groupList.$message({
                                type:'success',
                                message:'保存角色成功！'
                            });
                        });
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