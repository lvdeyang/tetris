/**
 * Created by sms on 2020/1/14.
 */
define([
    'text!' + window.APPPATH + 'front/media/push-live/page-media-push-live.html',
    'config',
    'context',
    'commons',
    'restfull',
    'json',
    'file',
    'uploader',
    'vue',
    'element-ui',
    'mi-frame',
    'mi-folder-dialog',
    'mi-task-view',
    'mi-upload-dialog',
    'mi-lightbox',
    'css!' + window.APPPATH + 'front/media/push-live/page-media-push-live.css'
], function(tpl, config, context, commons, ajax, $, File, Uploader, Vue){

    var pageId = 'page-media-push-live';

    var init = function(p){

        var folderId = p.folderId;

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups'),
                activeId:window.BASEPATH + 'index/media/pushLive/' + window.TOKEN,
                current:'',
                breadCrumb:[],
                typeList:{
                    audio: [],
                    video: []
                },
                table:{
                    tooltip:false,
                    rows:[],
                    page:{
                        current:1
                    }
                },
                dialog:{
                    createFolder:{
                        visible:false,
                        folderName:'',
                        loading:false
                    },
                    renameFolder:{
                        visible:false,
                        row:null,
                        folderName:'',
                        loading:false
                    },
                    addPushLive:{
                        visible:false,
                        name:'',
                        remark:'',
                        tags:'',
                        keyWords:'',
                        freq:'',
                        audioPid:'',
                        videoPid:'',
                        audioType:'',
                        videoType:'',
                        loading:false
                    },
                    editPushLive:{
                        visible:false,
                        id:'',
                        name:'',
                        remark:'',
                        tags:'',
                        keyWords:'',
                        freq:'',
                        audioPid:'',
                        videoPid:'',
                        audioType:'',
                        videoType:'',
                        loading:false
                    }
                },
                prepareUploadFileInfo:null
            },
            methods:{
                //鼠标移入
                mouseEnter:function(row, column, cell, event){
                    var self = this;
                    if(self.table.tooltip) return;
                    var rows = self.table.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            if(rows[i] === row){
                                Vue.set(rows[i], '__hover__', true);
                            }else{
                                Vue.set(rows[i], '__hover__', false);
                            }
                        }
                    }
                },
                //鼠标移出
                mouseLeave:function(row, column, cell, event){
                    var self = this;
                    if(self.table.tooltip) return;
                    Vue.set(row, '__hover__', false);
                },
                //添加图片媒资库文件夹
                addMediaPushLiveFolder:function(){
                    var self = this;
                    var folderName = self.dialog.createFolder.folderName;
                    if(!folderName){
                        self.$message({
                            message: '您还没有输入文件夹名称！',
                            type: 'error'
                        });
                        return;
                    }
                    self.dialog.createFolder.loading = true;
                    ajax.post('/folder/media/add/pushLive', {
                        parentFolderId:self.current.id,
                        folderName:folderName
                    }, function(data, status){
                        self.dialog.createFolder.loading = false;
                        if(status === 200){
                            prependFolderRow(self.table.rows, data);
                            self.dialog.createFolder.visible = false;
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //添加素材库文件夹对话框关闭初始化数据
                addMediaPushLiveFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.createFolder.folderName = '';
                },
                //预览文件
                handlePreview:function(scope){
                    var self = this;
                    var row = scope.row;
                    if(self.$refs.uploadDialog.isTxt(row.mimetype)){
                        ajax.post('/material/txt/' + row.id, null, function(data){
                            self.$refs.Lightbox.preview(data, 'txt', row);
                        });
                    }else{
                        ajax.post('/media/video/preview/uri/' + row.id, null, function(data){
                            var name = data.name;
                            var uri = data.uri;
                            if(self.$refs.uploadDialog.isImage(row.mimetype)){
                                self.$refs.Lightbox.preview(window.BASEPATH + uri, 'image');
                            }else if(self.$refs.uploadDialog.isAudio(row.mimetype)){
                                self.$refs.Lightbox.preview(window.BASEPATH + uri, 'audio');
                            }else if(self.$refs.uploadDialog.isVideo(row.mimetype)){
                                self.$refs.Lightbox.preview(window.BASEPATH + uri, 'video');
                            }
                        });
                    }
                },
                handleTxtSave:function(txt, row, done){
                    var self = this;
                    ajax.post('/material/save/txt/' + row.id, {
                        txt:txt
                    }, function(){
                        self.$message({
                            type:'success',
                            message:'保存成功!'
                        });
                        done();
                    });
                },
                //行删除按钮点击事件
                handleRowRemove:function(scope){
                    var self = this;
                    var row = scope.row;
                    h = self.$createElement;
                    if(row.type === 'FOLDER'){
                        self.$msgbox({
                            title:'危险操作',
                            message:h('div', null, [
                                h('div', {class:'el-message-box__status el-icon-warning'}, null),
                                h('div', {class:'el-message-box__message'}, [
                                    h('p', null, ['此操作将永久删除文件夹及所有内容，且不可恢复，是否继续?'])
                                ])
                            ]),
                            type:'wraning',
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            beforeClose:function(action, instance, done){
                                if(action === 'confirm'){
                                    self.removeMediaPushLiveFolder(scope, instance, done);
                                }else{
                                    done();
                                }
                            }
                        });
                    }else{
                        self.$msgbox({
                            title:'危险操作',
                            message:h('div', null, [
                                h('div', {class:'el-message-box__status el-icon-warning'}, null),
                                h('div', {class:'el-message-box__message'}, [
                                    h('p', null, ['此操作将永久删除文件，且不可恢复，是否继续?'])
                                ])
                            ]),
                            type:'wraning',
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            beforeClose:function(action, instance, done){
                                if(action === 'confirm'){
                                    self.removeMediaPushLive(scope, instance, done);
                                }else{
                                    done();
                                }
                            }
                        });
                    }
                },
                //删除视频媒资库文件夹
                removeMediaPushLiveFolder:function(scope, $confirm, done){
                    var self = this;
                    var row = scope.row;
                    if($confirm) $confirm.confirmButtonLoading = true;
                    ajax.post('/folder/media/remove/' + row.id, null, function(data, status, message){
                        if($confirm) $confirm.confirmButtonLoading = false;
                        if(typeof done === 'function') done();
                        if(status === 200){
                            var rows = self.table.rows;
                            for(var i=0; i<rows.length; i++){
                                if(rows[i].uuid === row.uuid){
                                    rows.splice(i, 1);
                                    break;
                                }
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //删除视频媒资
                removeMediaPushLive:function(scope, $confirm, done){
                    var self = this;
                    var row = scope.row;
                    if($confirm) $confirm.confirmButtonLoading = true;
                    ajax.post('/media/push/live/remove/' + row.id, null, function(data, status, message){
                        if($confirm) $confirm.confirmButtonLoading = false;
                        if(typeof done === 'function') done();
                        if(status === 200){
                            var rows = self.table.rows;
                            var deleted = data.deleted;
                            var processed = data.processed;
                            if(deleted && deleted.length>0) {
                                for (var i = 0; i < rows.length; i++) {
                                    if (rows[i].uuid === row.uuid) {
                                        rows.splice(i, 1);
                                        break;
                                    }
                                }
                            }
                            if(processed && processed.length>0){
                                var newEntity = processed[0];
                                for(var i=0; i<rows.length; i++){
                                    if(rows[i].uuid === row.uuid){
                                        newEntity.freq = rows[i].freq;
                                        newEntity.audioPid = rows[i].audioPid;
                                        newEntity.videoPid = rows[i].videoPid;
                                        rows.splice(i, 1, newEntity);
                                        break;
                                    }
                                }
                            }
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //行隐藏的功能按钮
                tableOptionsCommand:function(command, scope){
                    var self = this;
                    var row = scope.row;
                    if(command === '0'){
                        //重命名
                        if(row.type === 'FOLDER'){
                            self.dialog.renameFolder.row = row;
                            self.dialog.renameFolder.visible = true;
                        }
                    }else if(command === '1'){
                        //编辑
                        if(row.type === 'PUSH_LIVE'){
                            self.dialog.editPushLive.id = row.id;
                            self.dialog.editPushLive.name = row.name;
                            self.dialog.editPushLive.remark = row.remarks;
                            self.dialog.editPushLive.tags = row.tags.join('');
                            self.dialog.editPushLive.keyWords = row.keyWords.join('');
                            self.dialog.editPushLive.freq = row.freq;
                            self.dialog.editPushLive.audioPid = row.audioPid;
                            self.dialog.editPushLive.videoPid = row.videoPid;
                            self.dialog.editPushLive.audioType = row.audioType;
                            self.dialog.editPushLive.videoType = row.videoType;
                            self.dialog.editPushLive.visible = true;
                        }
                    }else if(command === '2'){
                        //移动
                        if(row.type === 'FOLDER'){
                            self.$refs.moveMediaPushLiveFolderDialog.open('/folder/permission/media/tree/with/except/pushLive', row.id);
                            self.$refs.moveMediaPushLiveFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.moveMediaPushLiveDialog.open('/folder/permission/media/tree/with/except/pushLive');
                            self.$refs.moveMediaPushLiveDialog.setBuffer(row);
                        }
                    }else if(command === '3'){
                        //复制
                        if(row.type === 'FOLDER'){
                            self.$refs.copyMediaPushLiveFolderDialog.open('/folder/permission/media/tree/pushLive');
                            self.$refs.copyMediaPushLiveFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.copyMediaPushLiveDialog.open('/folder/permission/media/tree/pushLive');
                            self.$refs.copyMediaPushLiveDialog.setBuffer(row);
                        }
                    }
                },
                //重命名素材库文件夹
                renameMediaPushLiveFolder:function(){
                    var self = this;
                    var folderName = self.dialog.renameFolder.folderName;
                    if(!folderName){
                        self.$message({
                            message: '文件夹名称不能为空！',
                            type: 'error'
                        });
                        return;
                    }
                    self.dialog.renameFolder.loading = true;
                    var row = self.dialog.renameFolder.row;
                    ajax.post('/folder/media/rename/' + row.id, {
                        folderName:folderName
                    }, function(data, status, message){
                        self.dialog.renameFolder.loading = false;
                        if(status === 200){
                            row.name = folderName;
                            self.dialog.renameFolder.visible = false;
                        }

                    }, null, ajax.NO_ERROR_CATCH_CODE)
                },
                //重命名素材库文件夹对话框关闭初始化数据
                renameMediaPushLiveFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.renameFolder.row = null;
                    self.dialog.renameFolder.folderName = '';
                },
                //文件夹移动
                moveMediaPushLiveFolder:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/folder/media/move', {
                        folderId:buffer.id,
                        targetId:folder.id
                    }, function(data, status){
                        endLoading();
                        if(status !== 200) return;
                        if(data){
                            var rows = self.table.rows;
                            for(var i=0; i<rows.length; i++){
                                if(rows[i].id === buffer.id){
                                    rows.splice(i, 1);
                                }
                            }
                            close();
                            self.$message({
                                message: '移动成功！',
                                type: 'success'
                            });
                        }
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //图片媒资移动
                moveMediaPushLive:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/media/push/live/move', {
                        mediaId:buffer.id,
                        targetId:folder.id
                    }, function(data, status){
                        endLoading();
                        if(status !== 200) return;
                        if(data){
                            var rows = self.table.rows;
                            for(var i=0; i<rows.length; i++){
                                if(rows[i].id === buffer.id){
                                    rows.splice(i, 1);
                                }
                            }
                        }
                        close();
                        self.$message({
                            message: '移动成功！',
                            type: 'success'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //文件夹复制
                copyMediaPushLiveFolder:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/folder/media/copy', {
                        folderId:buffer.id,
                        targetId:folder.id
                    }, function(data, status){
                        endLoading();
                        if(status !== 200) return;
                        var moved = data.moved;
                        var copied = data.copied;
                        if(!moved){
                            prependFolderRow(self.table.rows, copied);
                        }
                        close();
                        self.$message({
                            message:'复制成功！',
                            type:'success'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //素材文件复制
                copyMediaPushLive:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/media/push/live/copy', {
                        mediaId:buffer.id,
                        targetId:folder.id
                    }, function(data, status){
                        endLoading();
                        if(status !== 200) return;
                        var moved = data.moved;
                        var copied = data.copied;
                        if(!moved){
                            prependExceptFolderRow(self.table.rows, copied);
                        }
                        close();
                        self.$message({
                            message:'复制成功！',
                            type:'success'
                        });
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //添加媒资对话框关闭
                handleAddPushLiveClose:function(){
                    var self = this;
                    self.dialog.addPushLive.name = '';
                    self.dialog.addPushLive.remark = '';
                    self.dialog.addPushLive.tags = '';
                    self.dialog.addPushLive.keyWords = '';
                    self.dialog.addPushLive.freq = '';
                    self.dialog.addPushLive.audioPid = '';
                    self.dialog.addPushLive.videoPid = '';
                    self.dialog.addPushLive.audioType = '';
                    self.dialog.addPushLive.videoType = '';
                    self.dialog.addPushLive.visible = false;
                    self.dialog.addPushLive.loading = false;
                },
                //添加媒资对话框关闭
                handleEditPushLiveClose:function(){
                    var self = this;
                    self.dialog.editPushLive.id = '';
                    self.dialog.editPushLive.name = '';
                    self.dialog.editPushLive.remark = '';
                    self.dialog.editPushLive.tags = '';
                    self.dialog.editPushLive.keyWords = '';
                    self.dialog.editPushLive.freq = '';
                    self.dialog.editPushLive.audioPid = '';
                    self.dialog.editPushLive.videoPid = '';
                    self.dialog.editPushLive.audioType = '';
                    self.dialog.editPushLive.videoType = '';
                    self.dialog.editPushLive.visible = false;
                    self.dialog.editPushLive.loading = false;
                },
                //添加音频流媒资任务
                addMediaPushLiveTask:function(){
                    var self = this;
                    if (!self.dialog.addPushLive.name) {
                        self.$message({
                            message: '您还没有为资源命名',
                            type: 'warning'
                        });
                        return;
                    }
                    self.dialog.addPushLive.loading = true;
                    ajax.post('/media/push/live/task/add', {
                        freq:self.dialog.addPushLive.freq,
                        audioPid:self.dialog.addPushLive.audioPid,
                        videoPid:self.dialog.addPushLive.videoPid,
                        audioType:self.dialog.addPushLive.audioType,
                        videoType:self.dialog.addPushLive.videoType,
                        name:self.dialog.addPushLive.name,
                        tags:self.dialog.addPushLive.tags,
                        keyWords:self.dialog.addPushLive.keyWords,
                        remark:self.dialog.addPushLive.remark,
                        folderId:self.current.id
                    }, function(data, status){
                        self.dialog.addPushLive.loading = false;
                        if(status !== 200) return;
                        prependExceptFolderRow(self.table.rows, data);
                        self.handleAddPushLiveClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //编辑音频流媒资任务
                editMediaPushLiveTask:function(){
                    var self = this;
                    if (!self.dialog.editPushLive.name) {
                        self.$message({
                            message: '您还没有为资源命名',
                            type: 'warning'
                        });
                        return;
                    }
                    self.dialog.editPushLive.loading = true;
                    ajax.post('/media/push/live/task/edit/' + self.dialog.editPushLive.id, {
                        freq:self.dialog.editPushLive.freq,
                        audioPid:self.dialog.editPushLive.audioPid,
                        videoPid:self.dialog.editPushLive.videoPid,
                        audioType:self.dialog.editPushLive.audioType,
                        videoType:self.dialog.editPushLive.videoType,
                        name:self.dialog.editPushLive.name,
                        tags:self.dialog.editPushLive.tags,
                        keyWords:self.dialog.editPushLive.keyWords,
                        remark:self.dialog.editPushLive.remark
                    }, function(data, status){
                        self.dialog.addPushLive.loading = false;
                        if(status !== 200) return;
                        var rows = self.table.rows;
                        for(var i=0; i<rows.length; i++){
                            if(rows[i].id === data.id){
                                rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditPushLiveClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                doProcessPreview:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/process/generate/url', {
                        processInstanceId:row.processInstanceId
                    }, function(url){
                        window.open(url, '_blank', 'status=no,menubar=yes,toolbar=no,width=1366,height=580,left=100,top=100');
                    });
                }
            },
            created:function(){
                var self = this;
                ajax.post('/media/push/live/load/' + folderId, null, function(data){
                    var rows = data.rows;
                    var breadCrumb = data.breadCrumb;

                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            initTableRow(rows[i]);
                            self.table.rows.push(rows[i]);
                        }
                    }

                    var items = [breadCrumb];
                    var current = breadCrumb;
                    while(current.next){
                        current = current.next;
                        items.push(current);
                    }

                    for(var i=0; i<items.length; i++){
                        self.breadCrumb.push(items[i]);
                    }

                    self.current = current;
                });
                ajax.post('/media/push/live/list/stream/type/audio', null, function(data) {
                    if (data && data.length > 0) {
                        self.typeList.audio.splice(0, self.typeList.audio.length);
                        self.typeList.audio = self.typeList.audio.concat(data);
                    }
                });
                ajax.post('/media/push/live/list/stream/type/video', null, function(data) {
                    if (data && data.length > 0) {
                        self.typeList.video.splice(0, self.typeList.video.length);
                        self.typeList.video = self.typeList.video.concat(data);
                    }
                });
            }
        });

    };

    var destroy = function(){

    };

    //表格隐藏数据
    var initTableRow = function(row){
        Vue.set(row, '__hover__', false);
        //行按钮loading
        //Vue.set(row, '__loading__', {});
    };

    //新增文件夹行
    var prependFolderRow = function(rows, row){
        initTableRow(row);
        rows.splice(0, 0, row);
    };

    //新增非文件夹行
    var prependExceptFolderRow = function(rows, row){
        initTableRow(row);
        var joinIndex = 0;
        for(var i=0; i<rows.length; i++){
            if(rows[i].type === 'FOLDER'){
                joinIndex++;
            }else{
                break;
            }
        }
        rows.splice(joinIndex, 0, row);
    };

    var groupList = {
        path:'/' + pageId + '/:folderId',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});