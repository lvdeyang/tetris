/**
 * Created by lvdeyang on 2018/11/20 0020.
 */
define([
    'text!' + window.APPPATH + 'front/media/txt/page-media-txt.html',
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
    'css!' + window.APPPATH + 'front/media/txt/page-media-txt.css'
], function(tpl, config, context, commons, ajax, $, File, Uploader, Vue){

    var pageId = 'page-media-txt';

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
                activeId:window.BASEPATH + 'index/media/txt/' + window.TOKEN,
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
                    addTxt:{
                        visible:false,
                        name:'',
                        remark:'',
                        tags:[],
                        keyWords:'',
                        thumbnail: '',
                        addition: {},
                        content:'',
                        loading:false
                    },
                    editTxt:{
                        visible:false,
                        id:'',
                        name:'',
                        remark:'',
                        tags:[],
                        keyWords:'',
                        thumbnail: '',
                        addition: {},
                        content:'',
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
                addMediaTxtFolder:function(){
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
                    ajax.post('/folder/media/add/txt', {
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
                addMediaTxtFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.createFolder.folderName = '';
                },
                //下载文件
                handleDownload:function(scope){
                    var row = scope.row;
                    ajax.post('/media/txt/query/content/' + row.id, null, function(data){
                    	if(typeof data === 'object'){
                    		data = $.toJSON(data);
                    	}
                        var name = row.name + '.txt';
                        var a = document.createElement('a');
                        var event = new MouseEvent('click');
                        a.download = name;
                        a.href = URL.createObjectURL(new Blob([data]));
                        a.dispatchEvent(event)
                    });
                },
                //预览文件
                handlePreview:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/media/txt/query/content/' + row.id, null, function(data){
                        self.$refs.Lightbox.preview(data, 'txt', row);
                    });
                },
                handleTxtSave:function(txt, row, done){
                    var self = this;
                    ajax.post('/media/txt/save/content/' + row.id, {
                        content:txt
                    }, function(){
                        self.$message({
                            type:'success',
                            message:'保存成功!'
                        });
                        row.content = txt;
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
                                    self.removeMediaTxtFolder(scope, instance, done);
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
                                    self.removeMediaTxt(scope, instance, done);
                                }else{
                                    done();
                                }
                            }
                        });
                    }
                },
                //删除视频媒资库文件夹
                removeMediaTxtFolder:function(scope, $confirm, done){
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
                removeMediaTxt:function(scope, $confirm, done){
                    var self = this;
                    var row = scope.row;
                    if($confirm) $confirm.confirmButtonLoading = true;
                    ajax.post('/media/txt/remove/' + row.id, null, function(data, status, message){
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
                        if(row.type === 'TXT'){
                            self.dialog.editTxt.id = row.id;
                            self.dialog.editTxt.name = row.name;
                            self.dialog.editTxt.remark = row.remarks;
                            self.dialog.editTxt.tags = row.tags;
                            self.dialog.editTxt.keyWords = row.keyWords;
                            self.dialog.editTxt.thumbnail = row.thumbnail;
                            if (row.addition) self.dialog.editTxt.addition = typeof row.addition==='string' ? JSON.parse(row.addition) : row.addition;
                            self.dialog.editTxt.content = row.content;
                            self.dialog.editTxt.visible = true;
                        }
                    }else if(command === '2'){
                        //移动
                        if(row.type === 'FOLDER'){
                            self.$refs.moveMediaTxtFolderDialog.open('/folder/permission/media/tree/with/except/txt', row.id);
                            self.$refs.moveMediaTxtFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.moveMediaTxtDialog.open('/folder/permission/media/tree/with/except/txt');
                            self.$refs.moveMediaTxtDialog.setBuffer(row);
                        }
                    }else if(command === '3'){
                        //复制
                        if(row.type === 'FOLDER'){
                            self.$refs.copyMediaTxtFolderDialog.open('/folder/permission/media/tree/txt');
                            self.$refs.copyMediaTxtFolderDialog.setBuffer(row);
                        }else{
                            self.$refs.copyMediaTxtDialog.open('/folder/permission/media/tree/txt');
                            self.$refs.copyMediaTxtDialog.setBuffer(row);
                        }
                    }
                },
                //重命名素材库文件夹
                renameMediaTxtFolder:function(){
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
                renameMediaTxtFolderDialogClosed:function(){
                    var self = this;
                    self.dialog.renameFolder.row = null;
                    self.dialog.renameFolder.folderName = '';
                },
                //文件夹移动
                moveMediaTxtFolder:function(folder, buffer, startLoading, endLoading, close){
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
                moveMediaTxt:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/media/txt/move', {
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
                copyMediaTxtFolder:function(folder, buffer, startLoading, endLoading, close){
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
                copyMediaTxt:function(folder, buffer, startLoading, endLoading, close){
                    var self = this;
                    startLoading();
                    ajax.post('/media/txt/copy', {
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
                handleAddTxtClose:function(){
                    var self = this;
                    self.dialog.addTxt.name = '';
                    self.dialog.addTxt.remark = '';
                    self.dialog.addTxt.tags = [];
                    self.dialog.addTxt.keyWords = '';
                    self.dialog.addTxt.thumbnail = '';
                    self.dialog.addTxt.addition = {};
                    self.dialog.addTxt.content = '';
                    self.dialog.addTxt.visible = false;
                    self.dialog.addTxt.loading = false;
                },
                //编辑媒资对话框关闭
                handleEditTxtClose:function(){
                    var self = this;
                    self.dialog.editTxt.id = '';
                    self.dialog.editTxt.name = '';
                    self.dialog.editTxt.remark = '';
                    self.dialog.editTxt.tags = [];
                    self.dialog.editTxt.keyWords = '';
                    self.dialog.editTxt.thumbnail = '';
                    self.dialog.editTxt.addition = {};
                    self.dialog.editTxt.content = '';
                    self.dialog.editTxt.visible = false;
                    self.dialog.editTxt.loading = false;
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
                //添加文本媒资任务
                addMediaTxtTask:function(){
                    var self = this;
                    if (!self.dialog.addTxt.name) {
                        self.$message({
                            message: '您还没有为资源命名',
                            type: 'warning'
                        });
                        return;
                    }
                    self.dialog.addTxt.loading = true;
                    ajax.post('/media/txt/task/add', {
                        content:self.dialog.addTxt.content,
                        name:self.dialog.addTxt.name,
                        tags:(self.dialog.addTxt.tags.length > 0) ? self.dialog.addTxt.tags.join(',') : null,
                        keyWords:self.dialog.addTxt.keyWords,
                        remark:self.dialog.addTxt.remark,
                        thumbnail:self.dialog.addTxt.thumbnail,
                        addition: JSON.stringify(self.dialog.addTxt.addition),
                        folderId:self.current.id
                    }, function(data, status){
                        self.dialog.addTxt.loading = false;
                        if(status !== 200) return;
                        prependExceptFolderRow(self.table.rows, data);
                        self.handleAddTxtClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                //编辑文本媒资任务
                editMediaTxtTask:function(){
                    var self = this;
                    if (!self.dialog.editTxt.name) {
                        self.$message({
                            message: '您还没有为资源命名',
                            type: 'warning'
                        });
                        return;
                    }
                    self.dialog.editTxt.loading = true;
                    var tagString = (self.dialog.editTxt.tags.length > 0) ? self.dialog.editTxt.tags.join(',') : null;
                    ajax.post('/media/txt/task/edit/' + self.dialog.editTxt.id, {
                        content:self.dialog.editTxt.content,
                        name:self.dialog.editTxt.name,
                        tags:tagString,
                        keyWords:self.dialog.editTxt.keyWords,
                        thumbnail: self.dialog.editTxt.thumbnail,
                        addition: JSON.stringify(self.dialog.editTxt.addition),
                        remark:self.dialog.editTxt.remark
                    }, function(data, status){
                        self.dialog.editTxt.loading = false;
                        if(status !== 200) return;
                        var rows = self.table.rows;
                        for(var i=0; i<rows.length; i++){
                            if(rows[i].id === data.id){
                                rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditTxtClose();
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
                    self.$refs.tagDialog.open('/media/tag/list/get', self.dialog.addTxt.tags);
                },
                handleTagEdit: function () {
                    var self = this;
                    self.$refs.tagDialog.open('/media/tag/list/get', self.dialog.editTxt.tags);
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
                    self.$refs.editAddition.open(self.dialog.addTxt.addition);
                },
                handleAdditionEdit: function() {
                    var self = this;
                    self.$refs.editAddition.open(self.dialog.editTxt.addition);
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
                ajax.post('/media/txt/load/' + folderId, null, function(data){
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