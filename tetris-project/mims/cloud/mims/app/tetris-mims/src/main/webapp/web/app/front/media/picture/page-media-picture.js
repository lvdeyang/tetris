/**
 * Created by lvdeyang on 2018/11/20 0020.
 */
define([
    'text!' + window.APPPATH + 'front/media/picture/page-media-picture.html',
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
    'css!' + window.APPPATH + 'front/media/picture/page-media-picture.css'
], function(tpl, config, context, commons, ajax, $, File, Uploader, Vue){

    var pageId = 'page-media-picture';

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
                activeId:window.BASEPATH + 'index/media/picture',
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
                //添加素材库文件夹
                addMediaPictureFolder:function(){
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
                    ajax.post('/folder/media/add/picture', {
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
                addMediaPictureFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.createFolder.folderName = '';
                },
                //下载文件
                handleDownload:function(scope){
                    var row = scope.row;
                    ajax.post('/material/preview/uri/' + row.id, null, function(data){
                        var name = data.name;
                        var uri = data.uri;
                        var a = document.createElement('a');
                        var event = new MouseEvent('click');
                        a.download = name;
                        a.href = window.BASEPATH + uri;
                        a.dispatchEvent(event)
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
                        ajax.post('/material/preview/uri/' + row.id, null, function(data){
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
                                    self.removeMediaPictureFolder(scope, instance, done);
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
                                    self.removeMaterialFile(scope, instance, done);
                                }else{
                                    done();
                                }
                            }
                        });
                    }
                },
                //删除素材库文件夹
                removeMediaPictureFolder:function(scope, $confirm, done){
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
                //删除素材库文件
                removeMaterialFile:function(scope, $confirm, done){
                    var self = this;
                    var row = scope.row;
                    if($confirm) $confirm.confirmButtonLoading = true;
                    ajax.post('/material/remove/' + row.id, null, function(data, status, message){
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
                        //移动
                        if(row.type === 'FOLDER'){
                            self.$refs.moveMediaPictureFolderDialog.open(row.id);
                            self.$refs.movePersonalFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.moveMaterialFileDialog.open();
                            self.$refs.moveMaterialFileDialog.setBuffer(row);
                        }
                    }else if(command === '2'){
                        //复制
                        if(row.type === 'FOLDER'){
                            self.$refs.copyPersonalFolderDialog.open();
                            self.$refs.copyPersonalFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.copyMaterialFileDialog.open();
                            self.$refs.copyMaterialFileDialog.setBuffer(row);
                        }
                    }
                },
                //重命名素材库文件夹
                renameMediaPictureFolder:function(){
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
                renameMediaPictureFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.renameFolder.row = null;
                    self.dialog.renameFolder.folderName = '';
                },
                //文件夹移动
                moveMediaPictureFolder:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/folder/personal/move', {
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
                //素材文件移动
                moveMaterialFile:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/material/move', {
                        materialId:buffer.id,
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
                copyPersonalFolder:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/folder/personal/copy', {
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
                copyMaterialFile:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/material/copy', {
                        materialId:buffer.id,
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
                    self.$refs.taskView.open('/material/query/tasks');
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
                //上传按钮点击
                handleUpload:function(){
                    var self = this;
                    self.$refs.uploadDialog.open();
                },
                fileSelected:function(files, done){
                    var self = this;
                    var tasks = [];
                    for(var i=0; i<files.length; i++){
                        var file = files[i];
                        tasks.push({
                            name:file.name,
                            size:file.size,
                            mimetype:file.type,
                            lastModified:file.lastModified
                        });
                    }
                    ajax.post('/material/tasks/add', {
                        tasks: $.toJSON(tasks),
                        folderId:current.id
                    }, function(data, status){
                        if(status !== 200) return;
                        if(self.$refs.taskView.isVisible()){
                            self.$refs.taskView.open(data);
                        }else{
                            self.$refs.taskView.open('/material/query/tasks');
                        }
                        var uploadfiles = [];
                        for(var i=0; i<data.length; i++){
                            var task = data[i];
                            for(var j=0; j<files.length; j++){
                                var file = files[j];
                                if(task.lastModified===file.lastModified &&
                                    task.name===file.name &&
                                    task.size===file.size &&
                                    task.mimetype===file.type){
                                    uploadfiles.push(new File(task.uuid, 0, file));
                                    break;
                                }
                            }
                        }
                        var materialUploader = context.getProp('materialUploader');
                        if(materialUploader){
                            materialUploader.setContext(self);
                            materialUploader.push(uploadfiles);
                        }else{
                            createUploader(self, uploadfiles);
                        }
                        done();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                taskCancel:function(row, done){
                    var materialUploader = context.getProp('materialUploader');
                    if(materialUploader && materialUploader.contains('uuid', row.uuid)){
                        materialUploader.cancel([row.uuid]);
                    }else{
                        ajax.post('/material/upload/cancel/' + row.uuid, null, function(data){
                            done();
                        });
                    }
                },
                taskPause:function(row){
                    var materialUploader = context.getProp('materialUploader');
                    materialUploader.pause('uuid', row.uuid);
                },
                taskRestart:function(row){
                    var self = this;
                    var materialUploader = context.getProp('materialUploader');
                    if(materialUploader && materialUploader.contains('uuid', row.uuid)){
                        materialUploader.restart('uuid', row.uuid);
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
                                ajax.post('/material/query/upload/info/' + row.uuid, null, function(data, status){
                                    if(status !== 200){
                                        return;
                                    }
                                    if(file.name!==data.name
                                        || file.lastModified!==data.lastModified
                                        || file.size!==data.size
                                        || file.type!==data.mimetype){
                                        self.$message.error('您选择的文件与当前任务的文件不同！');
                                    }else{
                                        var uploadFile = new File(data.uuid, data.offset, file);
                                        if(!materialUploader){
                                            createUploader(self, [uploadFile]);
                                        }else{
                                            materialUploader.push([uploadFile]);
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
                }
            },
            created:function(){
                var self = this;
                ajax.post('/media/picture/load/' + folderId, null, function(data){
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
        var materialUploader = new Uploader('/material/upload', uploadfiles, 1024*1024*49);
        materialUploader.setContext(instance);
        materialUploader.onUploadProgress = function(file, progress){
            var instance = this;
            instance.$refs.taskView.progress('uuid', file.uuid, progress);
        };

        materialUploader.onUpLoadCanceled = function(file){
            var instance = this;
            ajax.post('/material/upload/cancel/' + file.uuid, null, function(data){
                instance.$refs.taskView.cancel(file.uuid);
            });
        };

        materialUploader.onUploadPaused = function(file){
            var instance = this;
            instance.$refs.taskView.pause(file.uuid);
        };

        materialUploader.onUploadSuccess = function(file, data){
            var instance = this;
            instance.$refs.taskView.success('uuid', file.uuid);
            prependExceptFolderRow(instance.table.rows, data);
        };

        materialUploader.onUploadError = function(file){
            var instance = this;
            ajax.post('/material/upload/error/' + file.uuid, null, function(data){
                instance.$refs.taskView.error('uuid', file.uuid);
            });
        };

        context.setProp('materialUploader', materialUploader);
        materialUploader.run();

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