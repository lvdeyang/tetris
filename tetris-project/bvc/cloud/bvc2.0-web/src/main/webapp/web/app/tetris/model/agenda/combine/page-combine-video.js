/**
 * Created by lvdeyang on 2020/7/2.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/agenda/combine/page-combine-video.html',
    'jquery',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'jquery.layout.auto',
    'css!' + window.APPPATH + 'tetris/model/agenda/combine/page-combine-video.css'
], function(tpl, $, config, ajax, context, commons, Vue){

    var htmlTemplates = [
        '<button class="button-edit el-button el-tooltip item el-button--text"><i class="el-icon-edit"></i></button>',
        '<button class="button-delete el-button el-tooltip item el-button--text"><i class="el-icon-delete"></i></button>',
        '<span class="text-area"></span>'
    ];

    var pageId = 'page-combine-video';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                agendaId: p.agendaId,
                agendaName: p.agendaName,
                roles:[],
                bundles:[],
                table:{
                    rows:[]
                },
                dialog: {
                    createCombineVideo:{
                        visible:false,
                        loading:false,
                        name:''
                    },
                    editCombineVideo:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    editPosition:{
                        visible:false,
                        loading:false,
                        column:0,
                        row:0,
                        id:''
                    },
                    editSrc:{
                        visible:false,
                        $cell:'',
                        oneToOneRoles:[],
                        totalRoles:[],
                        radio:'',
                        src:[],
                        switch:false,
                        //POLLING[|STATIC]
                        pictureType:'STATIC',
                        pollingTime:5,
                        currentTab:'role',
                        props:{
                            label:'roleName',
                            children:'channels'
                        }
                    }
                }
            },
            computed:{
                dialogEditSrcSwitch:function(val){
                    var self = this;
                    return self.dialog.editSrc.switch;
                }
            },
            watch:{
                dialogEditSrcSwitch:function(val){
                    var self = this;
                    if(val){
                        self.dialog.editSrc.pictureType = 'POLLING';
                    }else{
                        self.dialog.editSrc.pictureType = 'STATIC';
                    }
                }
            },
            methods:{
                rowKey:function(row){
                    return 'combine-video-' + row.uuid;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createCombineVideo.visible = true;
                },
                handleCreateCombineVideoClose:function(){
                    var self = this;
                    self.dialog.createCombineVideo.name = '';
                    self.dialog.createCombineVideo.loading = false;
                    self.dialog.createCombineVideo.visible = false;
                },
                handleCreateCombineVideoSubmit:function(){
                    var self = this;
                    self.dialog.createCombineVideo.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/combine/video/add', {
                        businessId:self.agendaId,
                        businessType:'AGENDA',
                        name:self.dialog.createCombineVideo.name,
                    }, function(data, status){
                        self.dialog.createCombineVideo.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateCombineVideoClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleDelete:function(){

                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editCombineVideo.id = row.id;
                    self.dialog.editCombineVideo.name = row.name;
                    self.dialog.editCombineVideo.visible = true;
                },
                handleEditCombineVideoClose:function(){
                    var self = this;
                    self.dialog.editCombineVideo.id = '';
                    self.dialog.editCombineVideo.name = '';
                    self.dialog.editCombineVideo.loading = false;
                    self.dialog.editCombineVideo.visible = false;
                },
                handleEditCombineVideoSubmit:function(){
                    var self = this;
                    self.dialog.editCombineVideo.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/combine/video/edit/name', {
                        id:self.dialog.editCombineVideo.id,
                        name:self.dialog.editCombineVideo.name
                    }, function(data, status){
                        self.dialog.editCombineVideo.loading = false;
                        if(status!==200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                            }
                        }
                        self.handleEditCombineVideoClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditPosition:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editPosition.id = row.id;
                    var websiteDraw = row.websiteDraw || {basic:{column:4, row:4}, cellspan:[]};
                    if(typeof websiteDraw === 'string') websiteDraw = $.parseJSON(websiteDraw);
                    self.dialog.editPosition.column = websiteDraw.basic.column;
                    self.dialog.editPosition.row = websiteDraw.basic.row;
                    self.dialog.editPosition.visible = true;
                    self.$nextTick(function(){
                        self.handleRefreshPosition(websiteDraw.cellspan);
                    });
                },
                handleRefreshPosition:function(cellspan){
                    var self = this;
                    var $screenWrapper = $(self.$refs.editPosition.$el).find('.screen-wrapper');
                    $screenWrapper['layout-auto']('create', {
                        cell:{
                            column:self.dialog.editPosition.column,
                            row:self.dialog.editPosition.row,
                            style:'custom',
                            html:htmlTemplates.join('')
                        },
                        cellspan:cellspan,
                        theme:'dark',
                        name:'combine-video-'+self.dialog.editPosition.id,
                        editable:true
                    });
                    ajax.post('/tetris/bvc/model/agenda/combine/video/load/positions', {
                        combineVideoId:self.dialog.editPosition.id
                    }, function(data){
                        if(!data || data.length<=0) return;
                        var cells = $screenWrapper['layout-auto']('getCells');
                        for(var i=0; i<cells.length; i++){
                            var cell = cells[i];
                            var position = null;
                            for(var j=0; j<data.length; j++){
                                if(data[j].serialnum == cell.serialNum){
                                    position = data[j];
                                    break;
                                }
                            }
                            if(!position) continue;
                            var srcs = position.srcs;
                            if(srcs && srcs.length>0){
                                var dataCache = {
                                    pictureType:position.pictureType,
                                    src:[]
                                };
                                if(dataCache.pictureType === 'POLLING'){
                                    dataCache.pollingTime = parseInt(position.pollingTime);
                                }
                                for(var k=0; k<srcs.length; k++){
                                    var src = srcs[k];
                                    if(src.combineVideoSrcType === 'ROLE_CHANNEL'){
                                        var targetChannel = null;
                                        for(var m=0; m<self.roles.length; m++){
                                            for(var n=0; n<self.roles[m].channels.length; n++){
                                                if(self.roles[m].channels[n].id == src.srcId){
                                                    targetChannel = self.roles[m].channels[n];
                                                    break;
                                                }
                                            }
                                            if(targetChannel) break;
                                        }
                                        targetChannel.type = 'ROLE_CHANNEL';
                                        dataCache.src.push(targetChannel);
                                    }
                                }
                                cell.$cell['layout-auto']('setData', dataCache);
                                var message = '';
                                if(dataCache.pictureType === 'POLLING'){
                                    message = '轮询（'+ dataCache.src.length +'）';
                                }else{
                                    message = dataCache.src[0].roleName + '-' + dataCache.src[0].name;
                                }
                                cell.$cell.find('.text-area').text(message);
                            }
                        }
                    });
                },
                handleEditPositionClose:function(){
                    var self = this;
                    self.dialog.editPosition.id = '';
                    self.dialog.editPosition.column = 0;
                    self.dialog.editPosition.row = 0;
                    self.dialog.editPosition.visible = false;
                },
                handleEditPositionSubmit:function(){
                    var self = this;
                    var $screenWrapper = $(self.$refs.editPosition.$el).find('.screen-wrapper');
                    var tpl = $screenWrapper['layout-auto']('generateTpl');
                    var websiteDraw = {
                        basic:tpl.basic,
                        cellspan:tpl.cellspan
                    };
                    var positions = tpl.layout;
                    self.dialog.editPosition.loading = true;
                    ajax.post('/tetris/bvc/model/agenda/combine/video/edit/position', $.toJSON({
                        id:self.dialog.editPosition.id,
                        websiteDraw:websiteDraw,
                        positions:positions
                    }), function(data, status){
                        self.dialog.editPosition.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditPositionClose();
                    }, 'application/json; charset=UTF-8', ajax.TOTAL_CATCH_CODE);
                },
                //这个地方不能放在watch里，直接改值不会调到这个事件
                switchChange:function(){
                    var self = this;
                    self.dialog.editSrc.src.splice(0, self.dialog.editSrc.src.length);
                },
                handleEditSrc:function($cell){
                    var self = this;
                    self.dialog.editSrc.visible = true;
                    self.dialog.editSrc.$cell = $cell;
                    self.dialog.editSrc.oneToOneRoles.splice(0, self.dialog.editSrc.oneToOneRoles.length);
                    self.dialog.editSrc.totalRoles.splice(0, self.dialog.editSrc.totalRoles.length);
                    self.dialog.editSrc.src.splice(0, self.dialog.editSrc.src.length);
                    for(var i=0; i<self.roles.length; i++){
                        if(self.roles[i].roleUserMappingType === 'ONE_TO_ONE'){
                            self.dialog.editSrc.oneToOneRoles.push($.extend(true, {}, self.roles[i]));
                        }
                        self.dialog.editSrc.totalRoles.push($.extend(true, {}, self.roles[i]));
                    }
                    var data = $cell['layout-auto']('getData');
                    if(!data) return;
                    if(data.pictureType === 'STATIC'){
                        self.dialog.editSrc.radio = data.src[0].uuid;
                        self.dialog.editSrc.switch = false;
                        self.dialog.editSrc.pollingTime = 5;
                        for(var i=0; i<self.dialog.editSrc.oneToOneRoles.length; i++){
                            var end = false;
                            for(var j=0; j<self.dialog.editSrc.oneToOneRoles[i].channels.length; j++){
                                if(self.dialog.editSrc.oneToOneRoles[i].channels[j].id == data.src[0].id){
                                    self.dialog.editSrc.src.push(self.dialog.editSrc.oneToOneRoles[i].channels[j]);
                                    end = true;
                                    break;
                                }
                            }
                            if(end) break;
                        }
                    }else if(data.pictureType === 'POLLING'){
                        self.dialog.editSrc.radio = '';
                        self.dialog.editSrc.switch = true;
                        self.dialog.editSrc.pollingTime = data.pollingTime;
                        for(var i=0; i<data.src.length; i++){
                            for(var j=0; j<self.dialog.editSrc.totalRoles.length; j++){
                                var end = false;
                                for(var k=0; k<self.dialog.editSrc.totalRoles[j].channels.length; k++){
                                    if(self.dialog.editSrc.totalRoles[j].channels[k].id == data.src[i].id){
                                        self.dialog.editSrc.totalRoles[j].channels[k].checked = true;
                                        self.dialog.editSrc.src.push(self.dialog.editSrc.totalRoles[j].channels[k]);
                                        end = true;
                                        break;
                                    }
                                }
                                if(end) break;
                            }
                        }
                    }
                    self.dialog.editSrc.pictureType = data.pictureType;
                },
                handleEditSrcClose:function(){
                    var self = this;
                    self.dialog.editSrc.$cell = '';
                    self.dialog.editSrc.radio = '';
                    for(var i=0; i<self.dialog.editSrc.src.length; i++){
                        self.dialog.editSrc.src[i].checked = false;
                    }
                    self.dialog.editSrc.switch = false;
                    self.dialog.editSrc.pictureType = 'STATIC';
                    self.dialog.editSrc.pollingTime = 5;
                    self.dialog.editSrc.currentTab = 'role';
                    self.dialog.editSrc.visible = false;
                },
                handleEditSrcCurrentChange:function(uuid){
                    var self = this;
                    self.dialog.editSrc.src.splice(0, self.dialog.editSrc.src.length);
                    for(var i=0; i<self.dialog.editSrc.oneToOneRoles.length; i++){
                        for(var j=0; j<self.dialog.editSrc.oneToOneRoles[i].channels.length; j++){
                            if(self.dialog.editSrc.oneToOneRoles[i].channels[j].uuid === uuid){
                                self.dialog.editSrc.src.push(self.dialog.editSrc.oneToOneRoles[i].channels[j]);
                                break;
                            }
                        }
                    }
                },
                handleEditSrcChange:function(data){
                    var self = this;
                    var exist = false;
                    for(var i=0; i<self.dialog.editSrc.src.length; i++){
                        if(self.dialog.editSrc.src[i] === data){
                            self.dialog.editSrc.src.splice(i, 1);
                            exist = true;
                            break;
                        }
                    }
                    if(!exist){
                        self.dialog.editSrc.src.push(data);
                        Vue.set(data, 'checked', true);
                    }else{
                        Vue.set(data, 'checked', false);
                    }
                },
                handleRemoveSelectedSrc:function(scope){
                    var self = this;
                    var row = scope.row;
                    for(var i=0; i<self.dialog.editSrc.src.length; i++){
                        if(self.dialog.editSrc.src[i] === row){
                            self.dialog.editSrc.src.splice(i, 1);
                            Vue.set(row, 'checked', false);
                            break;
                        }
                    }
                },
                handleUpperSelectedSrc:function(scope){
                    var self = this;
                    var row = scope.row;
                    for(var i=0; i<self.dialog.editSrc.src.length; i++){
                        if(self.dialog.editSrc.src[i] === row){
                            self.dialog.editSrc.src.splice(i, 1);
                            self.dialog.editSrc.src.splice(i-1, 0, row);
                            break;
                        }
                    }
                },
                handleDownSelectedSrc:function(scope){
                    var self = this;
                    var row = scope.row;
                    for(var i=0; i<self.dialog.editSrc.src.length; i++){
                        if(self.dialog.editSrc.src[i] === row){
                            self.dialog.editSrc.src.splice(i, 1);
                            self.dialog.editSrc.src.splice(i+1, 0, row);
                            break;
                        }
                    }
                },
                handleEditSrcSubmit:function(){
                    var self = this;
                    if(self.dialog.editSrc.src.length <= 0){
                        self.$message({
                            type:'error',
                            message:'您没有选择任何通道'
                        });
                        return;
                    }
                    var $cell = self.dialog.editSrc.$cell;
                    var data = {
                        pictureType:self.dialog.editSrc.pictureType,
                        src:[]
                    };
                    var message = '';
                    if(data.pictureType === 'POLLING'){
                        data.pollingTime = self.dialog.editSrc.pollingTime;
                        message = '轮询（'+ self.dialog.editSrc.src.length +'）';
                    }else{
                        message = self.dialog.editSrc.src[0].roleName + '-' + self.dialog.editSrc.src[0].name;
                    }
                    for(var i=0; i<self.dialog.editSrc.src.length; i++){
                        self.dialog.editSrc.src[i].type = 'ROLE_CHANNEL';
                        data.src.push(self.dialog.editSrc.src[i]);
                    }
                    $cell.find('.text-area').text(message);
                    $cell['layout-auto']('setData', data);
                    self.handleEditSrcClose();
                },
                handleDeleteSrc:function($cell){
                    $cell.find('.text-area').text('');
                    $cell['layout-auto']('clearData');
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该合屏，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/agenda/combine/video/delete', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id == row.id){
                                            self.table.rows.splice(i, 1);
                                        }
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                load:function(){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/tetris/bvc/model/agenda/combine/video/load', {
                        businessId:self.agendaId,
                        businessType:'AGENDA'
                    }, function(data, status){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                queryRoles:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/role/agenda/permission/load/with/channels', {
                        agendaId:self.agendaId,
                        channelType:'VIDEO_ENCODE'
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.roles.push(data[i]);
                            }
                        }
                    });
                },
                queryBundles:function(){

                }
            },
            created:function(){
                var self = this;
                self.load();
                self.queryRoles();
                self.$nextTick(function(){
                    $(self.$el).on('click.cell-edit', '.button-edit', function(){
                        var $cell = $(this).closest('td.cell');
                        self.handleEditSrc($cell);
                    });
                    $(self.$el).on('click.cell-delete', '.button-delete', function(){
                        var $cell = $(this).closest('td.cell');
                        self.handleDeleteSrc($cell);
                    });
                });
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:agendaId/:agendaName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});