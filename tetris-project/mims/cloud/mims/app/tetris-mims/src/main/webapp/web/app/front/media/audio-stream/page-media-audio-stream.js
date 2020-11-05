/**
 * Created by lvdeyang on 2018/11/20 0020.
 */
define([
    'text!' + window.APPPATH + 'front/media/audio-stream/page-media-audio-stream.html',
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
    'mi-tag-dialog',
    'mi-image-dialog',
    'mi-addition-dialog',
    'css!' + window.APPPATH + 'front/media/audio-stream/page-media-audio-stream.css'
], function(tpl, config, context, commons, ajax, $, File, Uploader, Vue){

    var pageId = 'page-media-audio-stream';

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
                activeId:window.BASEPATH + 'index/media/audioStream/' + window.TOKEN,
                current:'',
                breadCrumb:[],
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
                    addAudioStream:{
                        visible:false,
                        name:'',
                        remark:'',
                        tags:[],
                        keyWords:'',
                        previewUrl:'',
                        streamType: '',
                        thumbnail: '',
                        addition: {},
                        loading:false
                    },
                    editAudioStream:{
                        visible:false,
                        id:'',
                        name:'',
                        remark:'',
                        tags:[],
                        keyWords:'',
                        previewUrl:'',
                        streamType: '',
                        thumbnail: '',
                        addition: {},
                        loading:false
                    }
                },
                prepareUploadFileInfo:null,
                streamTypeList:[]
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
                addMediaAudioStreamFolder:function(){
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
                    ajax.post('/folder/media/add/audioStream', {
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
                addMediaAudioStreamFolderDialogClosed:function(){
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
                                    self.removeMediaAudioStreamFolder(scope, instance, done);
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
                                    self.removeMediaAudioStream(scope, instance, done);
                                }else{
                                    done();
                                }
                            }
                        });
                    }
                },
                //删除视频媒资库文件夹
                removeMediaAudioStreamFolder:function(scope, $confirm, done){
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
                removeMediaAudioStream:function(scope, $confirm, done){
                    var self = this;
                    var row = scope.row;
                    if($confirm) $confirm.confirmButtonLoading = true;
                    ajax.post('/media/audio/stream/remove/' + row.id, null, function(data, status, message){
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
                                        newEntity.previewUrl = rows[i].previewUrl;
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
                        if(row.type === 'AUDIO_STREAM'){
                            self.dialog.editAudioStream.id = row.id;
                            self.dialog.editAudioStream.name = row.name;
                            self.dialog.editAudioStream.remark = row.remarks;
                            self.dialog.editAudioStream.tags = row.tags;
                            self.dialog.editAudioStream.keyWords = row.keyWords;
                            self.dialog.editAudioStream.previewUrl = row.previewUrl;
                            self.dialog.editAudioStream.streamType = row.streamType;
                            self.dialog.editAudioStream.thumbnail = row.thumbnail;
                            if (row.addition) self.dialog.editAudioStream.addition = typeof row.addition==='string' ? JSON.parse(row.addition) : row.addition;
                            self.dialog.editAudioStream.visible = true;
                        }
                    }else if(command === '2'){
                        //移动
                        if(row.type === 'FOLDER'){
                            self.$refs.moveMediaAudioStreamFolderDialog.open('/folder/permission/media/tree/with/except/audioStream', row.id);
                            self.$refs.moveMediaAudioStreamFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.moveMediaAudioStreamDialog.open('/folder/permission/media/tree/with/except/audioStream');
                            self.$refs.moveMediaAudioStreamDialog.setBuffer(row);
                        }
                    }else if(command === '3'){
                        //复制
                        if(row.type === 'FOLDER'){
                            self.$refs.copyMediaAudioStreamFolderDialog.open('/folder/permission/media/tree/audioStream');
                            self.$refs.copyMediaAudioStreamFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.copyMediaAudioStreamDialog.open('/folder/permission/media/tree/audioStream');
                            self.$refs.copyMediaAudioStreamDialog.setBuffer(row);
                        }
                    }
                },
                //重命名素材库文件夹
                renameMediaAudioStreamFolder:function(){
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
                renameMediaAudioStreamFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.renameFolder.row = null;
                    self.dialog.renameFolder.folderName = '';
                },
                //文件夹移动
                moveMediaAudioStreamFolder:function(folder, buffer, startLoading, endLoading, close){
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
                moveMediaAudioStream:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/media/audio/stream/move', {
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
                copyMediaAudioStreamFolder:function(folder, buffer, startLoading, endLoading, close){
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
                copyMediaAudioStream:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/media/audio/stream/copy', {
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
                handleAddAudioStreamClose:function(){
                    var self = this;
                    self.dialog.addAudioStream.name = '';
                    self.dialog.addAudioStream.remark = '';
                    self.dialog.addAudioStream.tags = [];
                    self.dialog.addAudioStream.keyWords = '';
                    self.dialog.addAudioStream.previewUrl = '';
                    self.dialog.addAudioStream.streamType = '';
                    self.dialog.addAudioStream.thumbnail = '';
                    self.dialog.addAudioStream.addition = {};
                    self.dialog.addAudioStream.visible = false;
                    self.dialog.addAudioStream.loading = false;
                },
                //添加媒资对话框关闭
                handleEditAudioStreamClose:function(){
                    var self = this;
                    self.dialog.editAudioStream.id = '';
                    self.dialog.editAudioStream.name = '';
                    self.dialog.editAudioStream.remark = '';
                    self.dialog.editAudioStream.tags = [];
                    self.dialog.editAudioStream.keyWords = '';
                    self.dialog.editAudioStream.previewUrl = '';
                    self.dialog.editAudioStream.streamType = '';
                    self.dialog.editAudioStream.thumbnail = '';
                    self.dialog.editAudioStream.addition = {};
                    self.dialog.editAudioStream.visible = false;
                    self.dialog.editAudioStream.loading = false;
                },
                //编辑资源海报
                handleSelectThumbnail: function(buff) {
                    var self = this;
                    self.$refs.selectThumbnail.setBuffer(buff);
                    self.$refs.selectThumbnail.open();
                },
                selectedThumbnail:function(url, buff, startLoading, endLoading, done){
                    buff.thumbnail = url;
                    done();
                },
                //添加音频流媒资任务
                addMediaAudioStreamTask:function(){
                    var self = this;
                    if (!self.dialog.addAudioStream.name) {
                        self.$message({
                            message: '您还没有为资源命名',
                            type: 'warning'
                        });
                        return;
                    }
                    self.dialog.addAudioStream.loading = true;
                    ajax.post('/media/audio/stream/task/add', {
                        previewUrl:self.dialog.addAudioStream.previewUrl,
                        name:self.dialog.addAudioStream.name,
                        tags:(self.dialog.addAudioStream.tags.length > 0) ? self.dialog.addAudioStream.tags.join(',') : null,
                        keyWords:self.dialog.addAudioStream.keyWords,
                        remark:self.dialog.addAudioStream.remark,
                        streamType:self.dialog.addAudioStream.streamType,
                        thumbnail: self.dialog.addAudioStream.thumbnail,
                        addition: JSON.stringify(self.dialog.addAudioStream.addition),
                        folderId:self.current.id
                    }, function(data, status){
                        self.dialog.addAudioStream.loading = false;
                        if(status !== 200) return;
                        prependExceptFolderRow(self.table.rows, data);
                        self.handleAddAudioStreamClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //编辑音频流媒资任务
                editMediaAudioStreamTask:function(){
                    var self = this;
                    if (!self.dialog.editAudioStream.name) {
                        self.$message({
                            message: '您还没有为资源命名',
                            type: 'warning'
                        });
                        return;
                    }
                    self.dialog.editAudioStream.loading = true;
                    ajax.post('/media/audio/stream/task/edit/' + self.dialog.editAudioStream.id, {
                        previewUrl:self.dialog.editAudioStream.previewUrl,
                        name:self.dialog.editAudioStream.name,
                        tags:(self.dialog.editAudioStream.tags.length > 0) ? self.dialog.editAudioStream.tags.join(',') : null,
                        keyWords:self.dialog.editAudioStream.keyWords,
                        remark:self.dialog.editAudioStream.remark,
                        streamType:self.dialog.editAudioStream.streamType,
                        thumbnail: self.dialog.editAudioStream.thumbnail,
                        addition: JSON.stringify(self.dialog.editAudioStream.addition)
                    }, function(data, status){
                        self.dialog.addAudioStream.loading = false;
                        if(status !== 200) return;
                        var rows = self.table.rows;
                        for(var i=0; i<rows.length; i++){
                            if(rows[i].id === data.id){
                                rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditAudioStreamClose();
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
                },

                //标签处理
                handleTagAdd: function () {
                    var self = this;
                    self.$refs.tagDialog.open('/media/tag/list/get', self.dialog.addAudioStream.tags);
                },
                handleTagEdit: function () {
                    var self = this;
                    self.$refs.tagDialog.open('/media/tag/list/get', self.dialog.editAudioStream.tags);
                },
                selectedTags: function (buff, tags, startLoading, endLoading, close) {
                    var self = this;
                    startLoading();
                    buff.splice(0,buff.length);
                    for(var i=0; i<tags.length; i++){
                        buff.push(tags[i].name);
                    }
                    endLoading();
                    close();
                },
                handleTagRemove:function(tag, value){
                    for(var i=0; i<tag.length; i++){
                        if(tag[i] === value){
                            tag.splice(i, 1);
                            break;
                        }
                    }
                },

                //附加属性编辑
                handleAdditionAdd: function() {
                    var self = this;
                    self.$refs.editAddition.open(self.dialog.addAudioStream.addition);
                },
                handleAdditionEdit: function() {
                    var self = this;
                    self.$refs.editAddition.open(self.dialog.editAudioStream.addition);
                },
                editAddition: function(_buff, addition, closeFunc) {
                    var self = this;
                    var keys = Object.keys(_buff);
                    for (var i in keys) {
                        if (_buff.hasOwnProperty(keys[i])) {
                            delete _buff[keys[i]];
                        }
                    }
                    for (var key in addition) {
                        if (addition.hasOwnProperty(key)) {
                            _buff[key] = addition[key];
                        }
                    }
                    closeFunc();
                }
            },
            created:function(){
                var self = this;
                ajax.post('/media/audio/stream/load/' + folderId, null, function(data){
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
                ajax.post('/media/audio/stream/list/stream/type', null, function(data) {
                    if (data && data.length > 0) {
                        self.streamTypeList.splice(0, self.streamTypeList.length);
                        self.streamTypeList = self.streamTypeList.concat(data);
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