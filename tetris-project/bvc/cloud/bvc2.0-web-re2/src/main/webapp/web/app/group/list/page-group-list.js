define([
    'text!' + window.APPPATH + 'group/list/page-group-list.html',
    'restfull',
    'config',
    'commons',
    'jquery',
    'vue',
    'extral',
    'element-ui',
    'bvc2-header',
    'bvc2-tree-source-list',
    'bvc2-system-table-base',
    'bvc2-tree-meeting-member',
    'bvc2-dialog-edit-role',
    'bvc2-dialog-set-group-template',
    'bvc2-dialog-quick-group'
], function(tpl, ajax, config, commons, $, Vue){

    var pageId = 'page-group-list';

    //清空树
    var changeToEmptyMemberTree = function(instance){
        instance.sourcelist = "bvc2-tree-meeting-member";
        instance.sourcelist = "";
    };

    //刷新成成员编辑器
    var changeToMemberTreeEditor = function(instance, row, done){
        if(row){
            //查询成员
            ajax.get('/device/group/query/bundle/' + row.id, null, function(data){
                instance.sourcelist = "bvc2-tree-source-list";
                instance.checked.splice(0,instance.checked.length);
                var bundles = data.bundles;
                for(var j=0;j<bundles.length;j++){
                    instance.checked.push(bundles[j].key);
                }
                if(typeof done === 'function') done();
            });
        }else{
            instance.sourcelist = "bvc2-tree-source-list";
            instance.checked.splice(0,instance.checked.length);
            if(typeof done === 'function') done();
        }
    };

    //刷新成设备组成员
    var changeToMemberTree = function(instance, row, done){
        ajax.get('/device/group/control/query/tree/' + row.id, null, function(data){
            instance.sourcelist = "bvc2-tree-meeting-member";
            instance.group = data.group;
            instance.members.splice(0,instance.members.length);
            var membersTree = data.membersTree;
            for(var i=0;i<membersTree.length;i++){
                instance.members.push(membersTree[i]);
            }
            if(typeof done === 'function') done();
        });
    };

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

            var forwardModeOptions = [];
            var forwardModeArr = data.forwardMode;
            if(forwardModeArr && forwardModeArr.length>0){
                for(var i=0; i<forwardModeArr.length; i++){
                    forwardModeOptions.push({
                        label: forwardModeArr[i],
                        value: forwardModeArr[i]
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

            //看会权限模板
            var authtplOptions = [];
            var authtplArr = data.authtpl;
            if(authtplArr && authtplArr.length>0){
                for(var i=0; i<authtplArr.length; i++){
                    authtplOptions.push({
                        label:authtplArr[i].name,
                        value:authtplArr[i].id
                    });
                }
            }

            //地区分类先注掉
            //var regionOptions = [];
            //var regionArr = data.region;
            //if(regionArr && regionArr.length>0){
            //	for(var i=0; i<regionArr.length; i++){
            //		regionOptions.push({
            //			label:regionArr[i].content,
            //			value:regionArr[i].content
            //		})
            //	}
            //    regionOptions.push({
            //        label:"默认",
            //        value:"默认"
            //    });
            //}

            //会议类型（二级栏目）
            var programOptions = []; 
            var programArr = data.program;
            if(programArr && programArr.length>0){ 
            	for(var i=0; i<programArr.length; i++){ 
            		programOptions.push({
            			label:programArr[i].content, 
            			value:programArr[i].boId
            		});
            	}
            }

            //用于联动
            var sourceProgramOptions = $.extend(true, [], programArr);

            //直播栏目（去掉）
            var categoryLiveOptions = [];
            var categoryLiveArr = data.categoryLives;
            if(categoryLiveArr && categoryLiveArr.length>0){
                for(var i=0; i<categoryLiveArr.length; i++){
                    categoryLiveOptions.push({
                        label:categoryLiveArr[i].content,
                        value:categoryLiveArr[i].liveBoId
                    })
                }
            }

            //存储区域
            var storageLocationOptions = [];
            var storageLocationsArr = data.storageLocations;
            if(storageLocationsArr && storageLocationsArr.length>0){
                for(var i=0; i<storageLocationsArr.length; i++){
                    storageLocationOptions.push({
                        label:storageLocationsArr[i].content,
                        value:storageLocationsArr[i].code
                    })
                }
            }

            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_groupList = window.v_groupList = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    version: data.version,
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-group-list",
                    header:commons.getHeader(0),
                    sourcelist:'',
                    checked:[],
                    members:[],
                    roles:[],
                    roleMembers:[],
                    systemtpls:[],
                    avtpls:[],
                    authtpls:[],
                    createrow:'',
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
                            width:'160'

                        },{
                            label:'传输模式',
                            prop:'transmissionMode',
                            type:'select',
                            options:transmissionModeOptions,
                            width:'160'
                        },{
                            label:'转发模式',
                            prop:'forwardMode',
                            type:'select',
                            options:forwardModeOptions,
                            width:'160'
                        },
                        //{
                        //    label:'组织',
                        //    prop:'regions',
                        //    type:'entity',
                        //    entity:{
                        //        value:'boId',
                        //        label:'content',
                        //        table:{
                        //            pk:'boId',
                        //            label:'content',
                        //            title:'选择地区',
                        //            load:'/system/dictionary/query/region',
                        //            width:'30%',
                        //            columns:[{
                        //                label:'地区名称',
                        //                prop:'content'
                        //            }]
                        //        }
                        //    },
                        //    change:function(row, column, prop, instance){
                        //        var _row = row, _column = column, _prop = prop;
                        //        if(_row.type == '会议室' && prop.length > 1){
                        //            instance.$message({
                        //                message: _row.type + '不支持选多个地区，请修改！',
                        //                type:'warning'
                        //            });
                        //            return;
                        //        }
                        //        instance.dialog.entityEditorVisible = false;
                        //        var boIds = [];
                        //        if(prop && prop.length > 0){
                        //            for(var i = 0; i < prop.length; i++){
                        //                boIds.push(prop[i].boId);
                        //            }
                        //        }
                        //
                        //        return;
                        //        instance.columns[4].options.splice(0, instance.columns[4].options.length);
                        //        if(instance.columns[4].sourceProgramOptions && instance.columns[4].sourceProgramOptions.length > 0){
                        //            for(var i = 0; i < instance.columns[4].sourceProgramOptions.length; i++){
                        //                var programOption = instance.columns[4].sourceProgramOptions[i];
                        //                if(boIds.indexOf(programOption.parentRegionId) != -1){
                        //                    instance.columns[4].options.push({
                        //                        label:programOption.content,
                        //                        value:programOption.boId
                        //                    });
                        //                }
                        //            }
                        //        }
                        //
                        //    },
                        //    width:'200'
                        //},
                        //    {
                        //    label:'直播栏目',//可能去掉，改为自动选择
                        //    prop:'dicCategoryLiveContent',
                        //    hidden:'dicCategoryLiveId',
                        //    type:'select',
                        //    options:categoryLiveOptions,
                        //    //sourceProgramOptions:sourceProgramOptions,
                        //    width:'150'
                        //},
                        //    {
                        //	label:'会议类型',//二级栏目
                        //    prop:'dicProgramContent',
                        //    hidden:'dicProgramId',
                        //    type:'select',
                        //    options:programOptions,
                        //    sourceProgramOptions:sourceProgramOptions,
                        //    width:'160'
                        //},
                        //    {
                        //    label:'存储位置',//给CDN下发区域码
                        //    prop:'dicStorageLocationContent',
                        //    hidden:'dicStorageLocationCode',
                        //    type:'select',
                        //    options:storageLocationOptions,
                        //    width:'150'
                        //},
                            {
                            label:'状态',
                            prop:'statusContent',
                            //type:'simple',
                            //editable:false,
                            width:'120'
                        }
                        ],
                        options:[{
                            label:'修改会议参数',
                            icon:'el-icon-document',
                            iconStyle:'font-size:14px;',
                            click:'edit-group-param'
                        },{
                            label:'设备组控制',
                            icon:'icon-signin',
                            iconStyle:'font-size:16px;',
                            click:'enter-group'
                        },{
                            label:'刷新设备信息',
                            icon:'el-icon-download',
                            iconStyle:'font-size:14px;',
                            click:'refresh-group'
                        },{
                            label:'一键重置',
                            icon:'icon-exclamation',
                            iconStyle:'font-size:14px;',
                            click:'reset-group'
                        }
                        ],
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

                    /****************
                     * 表格与树联动
                     ****************/

                    //行点击
                    rowClick:function(row, e){
                        var instance = this;
                        if($(e.target).is('input')) return;
                        //切换成员树
                        changeToMemberTree(instance, row);
                    },

                    //设置设备组模板
                    rowCreateBefore:function(data, done, e){
                    	var instance = this;   	
                    	instance.systemtpls = tplOptions;
                    	instance.avtpls = avtplOptions;
                        instance.authtpls = authtplOptions;
                    	if(instance.$refs.setGroupTpls.tplIds.avtplId != null || instance.$refs.setGroupTpls.tplIds.systemTplId != null){
                    		instance.$refs.setGroupTpls.tplIds.avtplId = null;
                    		instance.$refs.setGroupTpls.tplIds.systemTplId = null;
                            instance.$refs.setGroupTpls.tplIds.authtplId = null;
                    	}
                    	instance.$refs.setGroupTpls.setTemplateVisible = true;
                    	instance.createrow = done;
                    },
                    
                    //新建设备组
                    rowCreate:function(){
                        var instance = this;
                        //切换到成员编辑器
                        changeToMemberTreeEditor(instance);
                    },

                    //新建保存数据转换
                    rowAddSaveBefore:function(data, done, e){
                        var instance = this,
                            checkedSource,
                            newData,
                        	tpls;

                            _columns = instance.table.columns;

                        //TODO:初始化状态为停止（先写这里）
                        data.statusContent = '已停止';

                        //空校验
                        for(var i=0;i<_columns.length;i++){
                            if(_columns[i].type){
                                if(data[_columns[i].prop] == null || (data[_columns[i].prop] == '' && _columns[i].label != '存储位置xxx')){
                                    instance.$message({
                                        message: _columns[i].label + '不能为空！',
                                        type: 'warning'
                                    });

                                    return;
                                }
                            }
                        }

                        checkedSource = instance.$refs.$sourceList.getCheckedNodes();
                        for(var i=checkedSource.length-1;i>=0;i--){
                            if(checkedSource[i].type != 'BUNDLE'){
                                checkedSource.splice(i,1);
                            }
                        }
                        
                        //获取会议模板和参数模板信息
                        tpls = instance.$refs.setGroupTpls.getTpls();
                        var transData = $.extend(true, {}, data);
                        transData.regions = transData.regions ? $.toJSON(transData.regions) : $.toJSON([]);
                        
                        //构造新数据
                        newData = $.extend(true, {sourceList: $.toJSON(checkedSource)}, transData, tpls);
                        //newData = Object.assign({}, data, {sourceList: $.toJSON(checkedSource)});
                        done(newData);
                    },

                    //新建保存数据之后
                    rowAddSaveAfter:function(row){
                        var instance = this;
                        //切换到成员树
                        changeToMemberTree(instance, row);
                    },

                    //新建数据取消保存
                    rowAddCancel:function(row, done, e){
                        var instance = this;
                        //切换到空成员树
                        changeToEmptyMemberTree(instance);
                        done();
                    },

                    //行编辑
                    rowEdit:function(row, done, e){
                        var instance = this;
                        if(row.status === 'START'){
                            instance.$message({
                                message: '当前设备组正在开会，请停会后编辑！',
                                type: 'warning'
                            });
                            return;
                        }
                        //切换到成员编辑器
                        changeToMemberTreeEditor(instance, row, done);
                    },

                    //编辑保存数据转换
                    rowEditSaveBefore:function(data, done, e){
                        var instance = this,
                            checkedSource,
                            newData,
                            _columns = instance.table.columns;

                        //空校验
                        for(var i=0;i<_columns.length;i++){
                            if(_columns[i].type){
                                if(data[_columns[i].prop] == null || (data[_columns[i].prop] == '' && _columns[i].label != '存储位置xxx')){
                                    instance.$message({
                                        message: _columns[i].label + '不能为空！',
                                        type: 'warning'
                                    });

                                    return;
                                }
                            }
                        }

                        //地区多选校验
                        if(data.type == '会议室' && data.regions && data.regions.length>1){
                            instance.$message({
                                message: data.type + '不支持选多个地区，请修改！',
                                type: 'warning'
                            });
                            return;
                        }

                        checkedSource = instance.$refs.$sourceList.getCheckedNodes();
                        for(var i=checkedSource.length-1;i>=0;i--){
                            if(checkedSource[i].type != 'BUNDLE'){
                                checkedSource.splice(i,1);
                            }
                        }

                        var transData = $.extend(true, {}, data);
                        transData.regions = transData.regions ? $.toJSON(transData.regions) : $.toJSON([]);

                        //构造新数据
                        newData = $.extend(true, {sourceList: $.toJSON(checkedSource)}, transData);
                        //newData = Object.assign({}, data, {sourceList: $.toJSON(checkedSource)});
                        done(newData);
                    },

                    //编辑保存数据之后
                    rowEditSaveAfter:function(row){
                        var instance = this;
                        //切换到成员树
                        changeToMemberTree(instance, row);
                    },

                    //编辑取消
                    rowEditCancel:function(row, done, e){
                        var instance = this;

                        //切换到成员树
                        changeToMemberTree(instance, row, done);
                    },

                    //删除行后
                    rowRemoveAfter:function(row){
                        var instance = this;
                        if(row.__isCurrent) changeToEmptyMemberTree(instance);
                    },

                    /********************
                     * 功能按钮事件
                     ********************/

                    //编辑权限
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

                    //保存权限
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
                    },

                    //控制设备组
                    enterGroup:function(row, e){
                        if(row.type==='监控室'){
                            window.location.hash = '#/page-group-preview/' + row.id;
                        }else{
                            window.location.hash = '#/page-group-control/' + row.id;
                            //window.location.hash = '#/page-custom-agenda/' + row.id + '/' + row.name;
                            //window.location.href = window.BASEPATH + 'tetris/index/' + window.TOKEN + '#/page-custom-agenda/' + row.id + '/' + row.name;
                        }
                    },

                    //刷新设备组成员
                    refreshGroup:function(row, e){
                        var instance = this;

                        instance.$confirm('确定要刷新设备组成员么？', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            confirmButtonLoading: false,
                            type: 'warning',
                            beforeClose: function (action, instance, d) {
                                if (action === 'confirm') {
                                    instance.confirmButtonLoading = true;
                                    instance.confirmButtonText = '执行中...';
                                    ajax.post('/device/group/control/refresh/' + row.id, null, function(data, status){
                                        instance.confirmButtonLoading = false;
                                        instance.confirmButtonText = '确定';
                                        d();
                                        if(status === 200){
                                            instance.$message({
                                                type:'success',
                                                message:'刷新成功！'
                                            });
                                        }
                                    }, null, [403, 404, 409]);
                                }else if(action === 'cancel'){
                                    d();
                                    instance.$message({
                                        type:'info',
                                        message:'操作取消！'
                                    });
                                }
                            }
                        });
                    },
                    editGroupParam:function(row, e){
                        var instance = this;
                        if(row.status === 'START'){
                            instance.$message({
                                message: '当前设备组正在开会，请停会后修改！',
                                type: 'warning'
                            });
                            return;
                        }
                        window.location.hash = '#/page-group-param-avtpl/' + row.id;
                    },
                    roleUpdate:function(row, e){
                        var instance = this;
                        instance.$confirm('确定要同步设备组角色么？', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            confirmButtonLoading: false,
                            type: 'warning',
                            beforeClose: function (action, instance, d) {
                                if (action === 'confirm') {
                                    instance.confirmButtonLoading = true;
                                    instance.confirmButtonText = '执行中...';
                                    ajax.post('/device/group/control/role/update/' + row.id, null, function(data, status){
                                        instance.confirmButtonLoading = false;
                                        instance.confirmButtonText = '确定';
                                        d();
                                        if(status === 200){
                                            instance.$message({
                                                type:'success',
                                                message:'同步成功！'
                                            });
                                        }
                                    }, null, [403, 404, 409]);
                                }else if(action === 'cancel'){
                                    d();
                                    instance.$message({
                                        type:'info',
                                        message:'操作取消！'
                                    });
                                }
                            }
                        });
                    },
                    quickGroup: function(){
                        var self=this;
                        self.$refs.$quickGroup.init();
                    },
                    quickGroupSave: function(groupName, chairmanId, members, success){
                        var self = this;
                        ajax.post("/device/group/quick/group/save",{
                            name: groupName,
                            chairman: chairmanId,
                            sourceList: $.toJSON(members)
                        }, function(data){

                            self.$refs.$groupList.rows.push(data);

                            success();

                            self.$message({
                                message: '创建成功！',
                                type: 'success'
                            });
                        });
                    },
                    resetGroup:function(row, e){
                        var instance = this;
                        instance.$confirm('一键重置会使设备组中的设备全部解锁，确定要一键重置设备组么？', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            confirmButtonLoading: false,
                            type: 'warning',
                            beforeClose: function (action, instance, d) {
                                if (action === 'confirm') {
                                    instance.confirmButtonLoading = true;
                                    instance.confirmButtonText = '执行中...';
                                    ajax.post('/device/group/reset/' + row.id, null, function(data, status){
                                        instance.confirmButtonLoading = false;
                                        instance.confirmButtonText = '确定';
                                        d();
                                        if(status === 200){
                                            instance.$message({
                                                type:'success',
                                                message:'重置成功！'
                                            });
                                        }
                                    }, null, [403, 404, 409]);
                                }else if(action === 'cancel'){
                                    d();
                                    instance.$message({
                                        type:'info',
                                        message:'操作取消！'
                                    });
                                }
                            }
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