/**
 * Created by lvdeyang on 2018/11/20 0020.
 */
define([
    'text!' + window.APPPATH + 'front/media/audio/page-media-audio.html',
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
    'mi-txt-dialog',
    'mi-tag-dialog',
    'mi-image-dialog',
    'mi-addition-dialog',
    'css!' + window.APPPATH + 'front/media/audio/page-media-audio.css'
], function(tpl, config, context, commons, ajax, $, File, Uploader, Vue){

    var pageId = 'page-media-audio';

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
                activeId:window.BASEPATH + 'index/media/audio/' + window.TOKEN,
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
                    addAudio:{
                        visible:false,
                        name:'',
                        remark:'',
                        tags:[],
                        keyWords:'',
                        way:'0',
                        encryption:false,
                        mediaEdit:false,
                        mediaEditTemplate:'',
                        mediaEditTemplates:[],
                        thumbnail: '',
                        addition: {},
                        txt:'',
                        txtTask:'',
                        task:'',
                        loading:false
                    },
                    editAudio:{
                        id:'',
                        visible:false,
                        name:'',
                        remark:'',
                        tags:[],
                        keywords:'',
                        thumbnail:'',
                        addition: {},
                        loading:false
                    },
                    upload:{
                        fileType:['audio'],
                        multiple:false
                    },
                    exchange:{
                        fileType:['txt'],
                        multiple:false
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
                //是否有下载
                downloadShow:function(row){
                    var self = this;
                    if(self.$refs.uploadDialog.isImage(row.mimetype)){
                        return true;
                    }
                    return false;
                },
                //格式化文件大小
                formatSize:function(size){
                    return File.prototype.formatSize(size);
                },
                //添加图片媒资库文件夹
                addMediaAudioFolder:function(){
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
                    ajax.post('/folder/media/add/audio', {
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
                addMediaAudioFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.createFolder.folderName = '';
                },
                //搜多文件
                handleQuerySearch:function(queryString, cb) {
                    var self = this;
                    if (queryString==undefined) {
                        cb([]);
                        return;
                    }
                    self.table.rows.splice(0, self.table.rows.length);
                    if (queryString.trim() == ""){
                        ajax.post('/media/audio/load/' + self.current.id, null, function(data){
                            var rows = data.rows;
                            if(rows && rows.length>0){
                                for(var i=0; i<rows.length; i++){
                                    initTableRow(rows[i]);
                                    self.table.rows.push(rows[i]);
                                }
                            }
                        });
                    } else {
                        var questData = {
                            name: queryString
                        };
                        ajax.post('/media/audio/search', questData, function(data){
                            var rows = data;
                            if(rows && rows.length>0){
                                for(var i=0; i<rows.length; i++){
                                    initTableRow(rows[i]);
                                    self.table.rows.push(rows[i]);
                                }
                            }
                        });
                    }
                    cb([]);
                },
                //下载文件
                handleDownload:function(scope){
                    var row = scope.row;
                    ajax.post('/media/audio/download/uri/' + row.id, null, function(data){
                        var name = data.name;
                        var uri = data.uri;
                        var a = document.createElement('a');
                        var event = new MouseEvent('click');
                        a.download = name;
                        a.href = window.BASEPATH + uri;
                        a.dispatchEvent(event);
                        row.downloadCount += 1;
                    });
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
                        ajax.post('/media/audio/preview/uri/' + row.id, null, function(data){
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
                                    self.removeMediaVideoFolder(scope, instance, done);
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
                                    self.removeMediaVideo(scope, instance, done);
                                }else{
                                    done();
                                }
                            }
                        });
                    }
                },
                //删除视频媒资库文件夹
                removeMediaVideoFolder:function(scope, $confirm, done){
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
                removeMediaVideo:function(scope, $confirm, done){
                    var self = this;
                    var row = scope.row;
                    if($confirm) $confirm.confirmButtonLoading = true;
                    ajax.post('/media/audio/remove/' + row.id, null, function(data, status, message){
                        if($confirm) $confirm.confirmButtonLoading = false;
                        if(typeof done === 'function') done();
                        if(status === 200){
                            var rows = self.table.rows;
                            var deleted = data.deleted;
                            var processed = data.processed;
                            if(deleted && deleted.length>0){
                                for(var i=0; i<rows.length; i++){
                                    if(rows[i].uuid === row.uuid){
                                        rows.splice(i, 1);
                                        break;
                                    }
                                }
                            }
                            if(processed && processed.length>0){
                                var newEntity = processed[0];
                                for(var i=0; i<rows.length; i++){
                                    if(rows[i].uuid === row.uuid){
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
                        if(row.type === 'AUDIO'){
                            self.dialog.editAudio.id = row.id;
                            self.dialog.editAudio.name = row.name;
                            self.dialog.editAudio.remark = row.remarks;
                            self.dialog.editAudio.tags = row.tags;
                            self.dialog.editAudio.keyWords = typeof row.keyWords==='string'||!row.keyWords?row.keyWords:row.keyWords.join(',');
                            self.dialog.editAudio.thumbnail = row.thumbnail;
                            if (row.addition) self.dialog.editAudio.addition = typeof row.addition==='string' ? JSON.parse(row.addition) : row.addition;
                            self.dialog.editAudio.visible = true;
                        }
                    }else if(command === '2'){
                        //移动
                        if(row.type === 'FOLDER'){
                            self.$refs.moveMediaAudioFolderDialog.open('/folder/permission/media/tree/with/except/audio', row.id);
                            self.$refs.moveMediaAudioFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.moveMediaAudioDialog.open('/folder/permission/media/tree/with/except/audio');
                            self.$refs.moveMediaAudioDialog.setBuffer(row);
                        }
                    }else if(command === '3'){
                        //复制
                        if(row.type === 'FOLDER'){
                            self.$refs.copyMediaAudioFolderDialog.open('/folder/permission/media/tree/audio');
                            self.$refs.copyMediaAudioFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.copyMediaAudioDialog.open('/folder/permission/media/tree/audio');
                            self.$refs.copyMediaAudioDialog.setBuffer(row);
                        }
                    }else if(command === '4'){
                    	ajax.post('/media/audio/task/recomm/' + row.id, {
                            status:1
                        }, function(data, status, message){
                            if(status === 200){
                               row.isTop=1;
                            }
                        }, null, ajax.NO_ERROR_CATCH_CODE)
                    }else if(command === '5'){
                    	ajax.post('/media/audio/task/recomm/' + row.id, {
                            status:0
                        }, function(data, status, message){
                            if(status === 200){
                               row.isTop=0;
                            }
                        }, null, ajax.NO_ERROR_CATCH_CODE)
                    }
                },
                //重命名素材库文件夹
                renameMediaAudioFolder:function(){
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
                renameMediaAudioFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.renameFolder.row = null;
                    self.dialog.renameFolder.folderName = '';
                },
                //文件夹移动
                moveMediaAudioFolder:function(folder, buffer, startLoading, endLoading, close){
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
                moveMediaAudio:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/media/audio/move', {
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
                copyMediaAudioFolder:function(folder, buffer, startLoading, endLoading, close){
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
                copyMediaAudio:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/media/audio/copy', {
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
                //展示任务列表
                taskViewShow:function(){
                    var self = this;
                    self.$refs.taskView.open('/media/audio/query/tasks');
                },
                //任务列表关闭
                afterTaskViewClose:function(metadata){
                    var self = this;
                    self.$refs.frame.removeMinimize(metadata);
                },
                //任务列表最小化
                taskViewMinimizeState:function(metadata){
                    var self = this;
                    self.$refs.frame.addMinimize(metadata);
                },
                //退出任务列表最小化
                exitTaskViewMinimizeState:function(){
                    var self = this;
                    self.$refs.taskView.exitMinimizeState();
                },
                //添加媒资对话框关闭
                handleAddAudioClose:function(){
                    var self = this;
                    self.dialog.addAudio.name = '';
                    self.dialog.addAudio.remark = '';
                    self.dialog.addAudio.tags = [];
                    self.dialog.addAudio.keyWords = '';
                    self.dialog.addAudio.way = '0';
                    self.dialog.addAudio.encryption = false;
                    self.dialog.addAudio.mediaEdit = false;
                    self.dialog.addAudio.mediaEditTemplate = '';
                    self.dialog.addAudio.thumbnail = '';
                    self.dialog.addAudio.addition = {};
                    self.dialog.addAudio.txt = '';
                    self.dialog.addAudio.txtTask = '';
                    self.dialog.addAudio.task = '';
                    self.dialog.addAudio.visible = false;
                    self.dialog.addAudio.loading = false;
                },
                //编辑媒资对话框关闭
                handleEditAudioClose:function(){
                    var self = this;
                    self.dialog.editAudio.id = '';
                    self.dialog.editAudio.name = '';
                    self.dialog.editAudio.remark = '';
                    self.dialog.editAudio.tags = [];
                    self.dialog.editAudio.keyWords = '';
                    self.dialog.editAudio.thumbnail = '';
                    self.dialog.editAudio.addition = {};
                    self.dialog.editAudio.visible = false;
                    self.dialog.editAudio.loading = false;
                },
                //开启转码按钮获取模板列表
                handleMediaEditChange: function(ifOpen) {
                    //var self = this;
                    //if (ifOpen) {
                    //    self.dialog.addAudio.mediaEditTemplates.splice(0, self.dialog.addAudio.mediaEditTemplates.length);
                    //    ajax.post('/media/editor/feign/template/list', null , function (data, status) {
                    //        if (status == 200) {
                    //            if (data != null && data.length > 0) {
                    //                for (var i = 0; i < data.length; i++) {
                    //                    self.dialog.addAudio.mediaEditTemplates.push(data[i]);
                    //                }
                    //            }
                    //        }
                    //    })
                    //} else {
                    //    self.dialog.addAudio.mediaEditTemplate = '';
                    //}
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
                //添加视频媒资任务
                addMediaAudioTask:function(){
                    var self = this;
                    if (!self.dialog.addAudio.name) {
                        self.$message({
                            message: '您还没有为资源命名',
                            type: 'warning'
                        });
                        return;
                    }
                    if (self.dialog.addAudio.way == '0' || self.dialog.addAudio.way == '1'){
                        var task = self.dialog.addAudio.way == '0' ? {
                            name:self.dialog.addAudio.task.name,
                            size:self.dialog.addAudio.task.size,
                            mimetype:self.dialog.addAudio.task.mimetype,
                            lastModified:self.dialog.addAudio.task.lastModified
                        } : {
                            name:self.dialog.addAudio.txtTask.name,
                            size:self.dialog.addAudio.txtTask.size,
                            mimetype:self.dialog.addAudio.txtTask.mimetype,
                            lastModified:self.dialog.addAudio.txtTask.lastModified
                        };
                        self.dialog.addAudio.loading = true;
                        ajax.post('/media/audio/task/add', {
                            task: $.toJSON(task),
                            name:self.dialog.addAudio.name,
                            tags:(self.dialog.addAudio.tags.length > 0) ? self.dialog.addAudio.tags.join(',') : null,
                            keyWords:self.dialog.addAudio.keyWords,
                            remark:self.dialog.addAudio.remark,
                            folderId:self.current.id,
                            encryption: self.dialog.addAudio.encryption,
                            mediaEdit: self.dialog.addAudio.mediaEdit,
                            mediaEditTemplate: self.dialog.addAudio.mediaEditTemplate,
                            thumbnail: self.dialog.addAudio.thumbnail,
                            addition: JSON.stringify(self.dialog.addAudio.addition)
                        }, function(data, status){
                            self.dialog.addAudio.loading = false;
                            if(status !== 200) return;
                            if(self.$refs.taskView.isVisible()){
                                self.$refs.taskView.open([data]);
                            }else{
                                self.$refs.taskView.open('/media/audio/query/tasks');
                            }
                            var uploadfiles = [];
                            uploadfiles.push(new File(data.uuid, 0, self.dialog.addAudio.way == '0' ? self.dialog.addAudio.task.file: self.dialog.addAudio.txtTask.file));
                            var mediaAudioUploader = context.getProp('mediaAudioUploader');
                            if(mediaAudioUploader){
                                mediaAudioUploader.setContext(self);
                                mediaAudioUploader.push(uploadfiles);
                            }else{
                                createUploader(self, uploadfiles);
                            }
                            self.handleAddAudioClose();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }else{
                        var questData = {
                            name:self.dialog.addAudio.name,
                            tags:(self.dialog.addAudio.tags.length > 0) ? self.dialog.addAudio.tags.join(',') : null,
                            keyWords:self.dialog.addAudio.keyWords,
                            remark:self.dialog.addAudio.remark,
                            folderId:self.current.id,
                            encryption:self.dialog.addAudio.encryption,
                            mediaEdit:self.dialog.addAudio.mediaEdit,
                            mediaEditTemplate: self.dialog.addAudio.mediaEditTemplate,
                            thumbnail: self.dialog.addAudio.thumbnail,
                            addition: JSON.stringify(self.dialog.addAudio.addition),
                            txtId:self.dialog.addAudio.txt.id
                        };
                        ajax.post('/media/audio/task/add/from/txt', questData, function(data, status){
                            self.dialog.addAudio.loading = false;
                            if(status !== 200) return;
                            self.table.rows.push(data);
                            self.handleAddAudioClose();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                    }
                },
                //编辑音频媒资任务
                editMediaAudioTask:function(){
                    var self = this;
                    if (!self.dialog.editAudio.name) {
                        self.$message({
                            message: '您还没有为资源命名',
                            type: 'warning'
                        });
                        return;
                    }
                    self.dialog.editAudio.loading = true;
                    ajax.post('/media/audio/task/edit/' + self.dialog.editAudio.id, {
                        name:self.dialog.editAudio.name,
                        tags:(self.dialog.editAudio.tags.length > 0) ? self.dialog.editAudio.tags.join(",") : null,
                        keyWords:self.dialog.editAudio.keyWords,
                        thumbnail: self.dialog.editAudio.thumbnail,
                        addition: JSON.stringify(self.dialog.editAudio.addition),
                        remark:self.dialog.editAudio.remark
                    }, function(data, status){
                        self.dialog.editAudio.loading = false;
                        if(status !== 200) return;
                        var rows = self.table.rows;
                        for(var i=0; i<rows.length; i++){
                            if(rows[i].id === data.id){
                                rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditAudioClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //选择文本仓库点击
                handleExchange:function(){
                    var self = this;
                    self.$refs.selectTxt.open();
                },
                selectedTxt:function(txt, startLoading, endLoading, done){
                    var self = this;
                    self.dialog.addAudio.txt = txt;
                    done();
                },
                //选择本地文本点击
                handleUploadExchange:function(){
                    var self = this;
                    self.$refs.exchangeDialog.open();
                },
                exchangeFileSelected:function(files, done){
                    var self = this;
                    var file = files[0];
                    self.dialog.addAudio.txtTask = {
                        name:file.name,
                        size:file.size,
                        mimetype:file.type,
                        lastModified:file.lastModified || file.lastModifiedDate.getTime(),
                        file:file
                    };
                    done();
                },
                //上传按钮点击
                handleUpload:function(){
                    var self = this;
                    self.$refs.uploadDialog.open();
                },
                fileSelected:function(files, done){
                    var self = this;
                    var file = files[0];
                    var task = {
                        name:file.name,
                        size:file.size,
                        mimetype:file.type,
                        lastModified:file.lastModified || file.lastModifiedDate.getTime(),
                        file:file
                    };
                    self.dialog.addAudio.task = task;
                    done();
                },
                taskCancel:function(row, done){
                    var mediaAudioUploader = context.getProp('mediaAudioUploader');
                    if(mediaAudioUploader && mediaAudioUploader.contains('uuid', row.uuid)){
                        mediaAudioUploader.cancel([row.uuid]);
                    }else{
                        ajax.post('/media/audio/upload/cancel/' + row.uuid, null, function(data){
                            done();
                        });
                    }
                },
                taskPause:function(row){
                    var mediaAudioUploader = context.getProp('mediaAudioUploader');
                    mediaAudioUploader.pause('uuid', row.uuid);
                },
                taskRestart:function(row){
                    var self = this;
                    var mediaAudioUploader = context.getProp('mediaAudioUploader');
                    if(mediaAudioUploader && mediaAudioUploader.contains('uuid', row.uuid)){
                        mediaAudioUploader.restart('uuid', row.uuid);
                        self.$refs.taskView.restart(row.uuid);
                    }else{
                        var $file = $('#tmp-file');
                        if(!$file[0]){
                            $file = $('<input id="tmp-file" type="file" style="display:none;" accept="'+self.$refs.uploadDialog.accept()+'"/>');
                            $('body').append($file);
                        }
                        $file.on('change', function(){
                            var file = $file[0].files[0];
                            if(!file){
                                self.$message.error('您没有选择任何文件!');
                            }else{
                                ajax.post('/media/audio/query/upload/info/' + row.uuid, null, function(data, status){
                                    if(status !== 200){
                                        return;
                                    }
                                    if(file.name!==data.name
                                        || (file.lastModified||file.lastModifiedDate.getTime())!==data.lastModified
                                        || file.size!==data.size
                                        || file.type!==data.mimetype){
                                        self.$message.error('您选择的文件与当前任务的文件不同！');
                                    }else{
                                        var uploadFile = new File(data.uuid, data.offset, file);
                                        if(!mediaAudioUploader){
                                            createUploader(self, [uploadFile]);
                                        }else{
                                            mediaAudioUploader.push([uploadFile]);
                                        }
                                        self.$refs.taskView.restart(row.uuid);
                                    }
                                },  null, ajax.NO_ERROR_CATCH_CODE);
                            }
                            $file.remove();
                        }).click();
                        self.$notify.warning({
                            title: '提示',
                            message: '由于您在上传时关闭了页面，需要再次选择相同文件才能续传！'
                        });
                    }
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

                handleTagAdd: function () {
                    var self = this;
                    self.$refs.tagDialog.open('/media/tag/list/get', self.dialog.addAudio.tags);
                },
                handleTagEdit: function () {
                    var self = this;
                    self.$refs.tagDialog.open('/media/tag/list/get', self.dialog.editAudio.tags);
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
                    self.$refs.editAddition.open(self.dialog.addAudio.addition);
                },
                handleAdditionEdit: function() {
                    var self = this;
                    self.$refs.editAddition.open(self.dialog.editAudio.addition);
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
                ajax.post('/media/audio/load/' + folderId, null, function(data){
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
            }
        });

    };

    var destroy = function(){

    };

    //创建文件上传工具
    var createUploader = function(instance, uploadfiles){
        var mediaAudioUploader = new Uploader('/media/audio/upload', uploadfiles, 1024*1024*49);
        mediaAudioUploader.setContext(instance);
        mediaAudioUploader.onUploadProgress = function(file, progress){
            var instance = this;
            instance.$refs.taskView.progress('uuid', file.uuid, progress);
        };

        mediaAudioUploader.onUpLoadCanceled = function(file){
            var instance = this;
            ajax.post('/media/audio/upload/cancel/' + file.uuid, null, function(data){
                instance.$refs.taskView.cancel(file.uuid);
            });
        };

        mediaAudioUploader.onUploadPaused = function(file){
            var instance = this;
            instance.$refs.taskView.pause(file.uuid);
        };

        mediaAudioUploader.onUploadSuccess = function(file, data){
            var instance = this;
            instance.$refs.taskView.success('uuid', file.uuid);
            prependExceptFolderRow(instance.table.rows, data);
        };

        mediaAudioUploader.onUploadError = function(file){
            var instance = this;
            ajax.post('/media/audio/upload/error/' + file.uuid, null, function(data){
                instance.$refs.taskView.error('uuid', file.uuid);
            });
        };

        context.setProp('mediaAudioUploader', mediaAudioUploader);
        mediaAudioUploader.run();

        return;
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