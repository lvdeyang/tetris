define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-file/bvc2-monitor-file.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/bvc2-monitor-file/bvc2-monitor-file.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-file';
    
    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                tree:{
                    data:[],
                    loading:false,
                    expand:true,
                    props:{
                        children:'children',
                        label:'name'
                    },
                    currentFolder:''
                },
                table:{
                    tmpPath:false,
                    fullPath:'',
                    data:[],
                    selectRows:[],
                    page:{
                        total:0,
                        pageSize:20,
                        currentPage:0
                    }
                },
                dialog:{
                    selectFolder:{
                        visible:false,
                        table:{
                            data:[],
                            page:{
                                currentPage:0,
                                pageSize:20,
                                total:0
                            },
                            currentRow:''
                        }
                    }
                }
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            loadTree:function(){
                var self = this;
                self.tree.loading = true;
                self.tree.data.splice(0, self.tree.data.length);
                ajax.post('/monitor/vod/query/resource/tree/for/edit', null, function(data, status){
                    self.tree.loading = false;
                    if(status !== 200) return;
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.tree.data.push(data[i]);
                        }
                    }
                }, null, [403, 404, 408, 409, 500]);
            },
            handleSelectFolder:function(){
                var self = this;
                self.loadFolders(1);
                self.dialog.selectFolder.visible = true;
            },
            hasResource:function(data){
                var self = this;
                if(data.type === 'VOD_RESOURCE') return true;
                var folders = [];
                var vods = [];
                scanningNode(data, folders, vods);
                if(vods.length > 0){
                    return true;
                }
                return false;
            },
            handleNodeDelete:function(data, node){
                var self = this;
                if(data.type === 'FOLDER'){
                    //清空文件夹
                    var folders = [];
                    var vods = [];
                    scanningNode(data, folders, vods);
                    if(vods.length > 0){
                        var h = self.$createElement;
                        self.$msgbox({
                            title:'操作提示',
                            message:h('div', null, [
                                h('div', {class:'el-message-box__status el-icon-warning'}, null),
                                h('div', {class:'el-message-box__message'}, [
                                    h('p', null, ['是否下架当前文件夹以及所有子文件夹下的资源（'+vods.length+'）?'])
                                ])
                            ]),
                            type:'wraning',
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            beforeClose:function(action, instance, done){
                                instance.confirmButtonLoading = true;
                                if(action === 'confirm'){
                                    var resourceIds = [];
                                    for(var i=0; i<vods.length; i++){
                                        resourceIds.push(vods[i].id);
                                    }
                                    resourceIds = $.toJSON(resourceIds);
                                    ajax.post('/monitor/vod/remove', {
                                        resourceIds:resourceIds
                                    }, function(dataIn, status){
                                        instance.confirmButtonLoading = false;
                                        done();
                                        if(status !== 200) return;
                                        for(var i=0; i<folders.length; i++){
                                            var folder = folders[i];
                                            if(!folder.children || folder.children.length<=0) continue;
                                            var removeIndex = [];
                                            for(var j=0; j<folder.children.length; j++){
                                                for(var k=0; k<vods.length; k++){
                                                    if(vods[k] === folder.children[j]){
                                                        removeIndex.push(j);
                                                        break;
                                                    }
                                                }
                                            }
                                            var copyNode = [];
                                            for(var j=0; j<folder.children.length; j++){
                                                copyNode.push(folder.children[j]);
                                            }
                                            folder.children.splice(0, folder.children.length);
                                            for(var j=0; j<copyNode.length; j++){
                                                var removed = false;
                                                for(var k=0; k<removeIndex.length; k++){
                                                    if(j === removeIndex[k]) {
                                                        removed = true;
                                                        break;
                                                    }
                                                }
                                                if(!removed){
                                                    folder.children.push(copyNode[j]);
                                                }
                                            }
                                        }
                                        if(self.table.fullPath) self.loadFiles(self.table.page.currentPage);
                                    }, null, [403,404,409,500]);
                                }else{
                                    instance.confirmButtonLoading = false;
                                    done();
                                }
                            }
                        }).catch(function(){});
                    }
                }else if(data.type === 'VOD_RESOURCE'){
                    //删除点播资源
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'操作提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否下架当前资源?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                var resourceIds = $.toJSON([data.id]);
                                ajax.post('/monitor/vod/remove', {
                                    resourceIds:resourceIds
                                }, function(dataIn, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<node.parent.data.children.length; i++){
                                        if(node.parent.data.children[i] === data){
                                            node.parent.data.children.splice(i, 1);
                                            break;
                                        }
                                    }
                                    if(self.table.fullPath) self.loadFiles(self.table.page.currentPage);
                                }, null, [403,404,409,500]);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                }
            },
            editTmpPath:function(){
                var self = this;
                self.table.fullPath = '';
                self.table.tmpPath = true;
            },
            handleQuery:function(){
                var self = this;
                self.table.tmpPath = false;
                self.loadFiles(1);
            },
            handleUploadBatch:function(){
                var self = this;
                if(!self.table.selectRows || self.table.selectRows.length<=0){
                    self.$message({
                        type:'warning',
                        message:'您没有选择任何数据！'
                    });
                }
                var resources = [];
                for(var i=0; i<self.table.selectRows.length; i++){
                    resources.push({
                        name:self.table.selectRows[i].name,
                        previewUrl:self.table.selectRows[i].fullPath
                    });
                }
                resources = $.toJSON(resources);
                ajax.post('/monitor/vod/add', {
                    folderId:self.tree.currentFolder.id,
                    resources:resources
                }, function(data){
                    if(data && data.length>0){
                        if(!self.tree.currentFolder.children){
                            Vue.set(self.tree.currentFolder, 'children', []);
                        }
                        for(var i=0; i<data.length; i++){
                            self.tree.currentFolder.children.push(data[i]);
                        }
                        for(var j=0; j<self.table.selectRows.length; j++){
                            var row = self.table.selectRows[j];
                            for(var i=0; i<self.table.data.length; i++){
                                if(self.table.data[i] === row){
                                    self.table.data.splice(i, 1);
                                    break;
                                }
                            }
                        }
                        self.table.page.total -= self.table.selectRows.length;
                        if(self.table.data.length<=0 && self.table.page.currentPage>1){
                            self.loadFiles(self.table.page.currentPage - 1);
                        }
                    }
                });

            },
            handleUpload:function(scope){
                var self = this;
                var row = scope.row;
                var resources = $.toJSON([{
                    name:row.name,
                    previewUrl:row.fullPath
                }]);
                ajax.post('/monitor/vod/add', {
                    folderId:self.tree.currentFolder.id,
                    resources:resources
                }, function(data){
                    if(data && data.length>0){
                        if(!self.tree.currentFolder.children){
                            Vue.set(self.tree.currentFolder, 'children', []);
                        }
                        for(var i=0; i<data.length; i++){
                            self.tree.currentFolder.children.push(data[i]);
                        }
                        for(var i=0; i<self.table.data.length; i++){
                            if(self.table.data[i] === row){
                                self.table.data.splice(i, 1);
                                break;
                            }
                        }
                        self.table.page.total -= 1;
                        if(self.table.data.length<=0 && self.table.page.currentPage>1){
                            self.loadFiles(self.table.page.currentPage - 1);
                        }
                    }
                });
            },
            selectUploadChange:function(selectedRow){
                var self = this;
                self.table.selectRows.splice(0, self.table.selectRows.length);
                for(var i=0; i<selectedRow.length; i++){
                    self.table.selectRows.push(selectedRow[i]);
                }
            },
            loadFiles:function(currentPage){
                var self = this;
                self.table.data.splice(0, self.table.data.length);
                ajax.post('/monitor/external/static/resource/folder/scanning', {
                    fullPath:self.table.fullPath,
                    currentPage:currentPage,
                    pageSize:self.table.page.pageSize
                }, function(data){
                    var total = data.total;
                    var rows = data.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.table.data.push(rows[i]);
                        }
                    }
                    self.table.page.total = total;
                    self.table.page.currentPage = currentPage;
                });
            },
            handleSizeChange:function(pageSize){
                var self = this;
                self.table.page.pageSize = pageSize;
                self.loadFiles(1);
            },
            handleCurrentChange:function(currentPage){
                var self = this;
                self.loadFiles(currentPage);
            },
            loadFolders:function(currentPage){
                var self = this;
                ajax.post('/monitor/external/static/resource/folder/load/all', {
                    currentPage:currentPage,
                    pageSize:self.dialog.selectFolder.table.page.pageSize
                }, function(data){
                    var total = data.total;
                    var rows = data.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.dialog.selectFolder.table.data.push(rows[i]);
                        }
                    }
                    self.dialog.selectFolder.table.page.total = total;
                    self.dialog.selectFolder.table.page.currentPage = currentPage;
                });
            },
            handleSelectFolderClose:function(){
                var self = this;
                self.dialog.selectFolder.table.data.splice(0, self.dialog.selectFolder.table.data.length);
                self.dialog.selectFolder.table.page.currentPage = 0;
                self.dialog.selectFolder.table.page.total = 0;
                self.dialog.selectFolder.table.currentRow = '';
                self.dialog.selectFolder.visible = false;
            },
            handleSelectFolderCurrentChange:function(currentPage){
                var self = this;
                self.loadFolders(currentPage);
            },
            handleSelectFolderCommit:function(){
                var self = this;
                self.table.fullPath = self.dialog.selectFolder.table.currentRow.fullPath;
                self.handleQuery();
                self.handleSelectFolderClose();
            },
            currentFolderChange:function(currentRow){
                var self = this;
                self.dialog.selectFolder.table.currentRow = currentRow;
            }
        },
        mounted:function(){
            var self = this;
            self.loadTree();
        }
    });

    var scanningNode = function(data, folders, vods){
        folders.push(data);
        if(data.children && data.children.length>0){
            for(var i=0; i<data.children.length; i++){
                if(data.children[i].type === 'FOLDER'){
                    scanningNode(data.children[i], folders, vods);
                }else if(data.children[i].type === 'VOD_RESOURCE'){
                    vods.push(data.children[i]);
                }
            }
        }
    };

    return Vue;
});