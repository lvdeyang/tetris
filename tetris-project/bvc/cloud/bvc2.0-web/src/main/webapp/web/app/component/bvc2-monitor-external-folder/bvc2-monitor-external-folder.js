define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-external-folder/bvc2-monitor-external-folder.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/bvc2-monitor-external-folder/bvc2-monitor-external-folder.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-external-folder';
    
    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                protocolTypes:[],
                table:{
                    data:[],
                    page:{
                        currentPage:0,
                        pageSize:20,
                        total:0
                    }
                },
                dialog:{
                    addFolder:{
                        visible:false,
                        name:'',
                        ip:'',
                        port:'',
                        folderPath:'',
                        protocolType:'',
                        username:'',
                        password:'',
                        loading:false
                    },
                    editFolder:{
                        visible:false,
                        id:'',
                        name:'',
                        ip:'',
                        port:'',
                        folderPath:'',
                        protocolType:'',
                        username:'',
                        password:'',
                        loading:false
                    }
                }
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            load:function(currentPage){
                var self = this;
                self.table.data.splice(0, self.table.data.length);
                ajax.post('/monitor/external/static/resource/folder/load', {
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
            handleAddFolder:function(){
                var self = this;
                self.dialog.addFolder.visible = true;
            },
            rowEdit:function(scope){
                var self = this;
                var row = scope.row;
                self.dialog.editFolder.id = row.id;
                self.dialog.editFolder.name = row.name;
                self.dialog.editFolder.ip = row.ip;
                self.dialog.editFolder.port = row.port;
                self.dialog.editFolder.folderPath = row.folderPath;
                self.dialog.editFolder.protocolType = row.protocolType;
                self.dialog.editFolder.username = row.username;
                self.dialog.editFolder.password = row.password;
                self.dialog.editFolder.visible = true;
            },
            rowDelete:function(scope){
                var self = this;
                var row = scope.row;
                var h = self.$createElement;
                self.$msgbox({
                    title:'操作提示',
                    message:h('div', null, [
                        h('div', {class:'el-message-box__status el-icon-warning'}, null),
                        h('div', {class:'el-message-box__message'}, [
                            h('p', null, ['是否要删除此外部文件夹?'])
                        ])
                    ]),
                    type:'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose:function(action, instance, done){
                        instance.confirmButtonLoading = true;
                        if(action === 'confirm'){
                            ajax.post('/monitor/external/static/resource/folder/remove/' + row.id, null, function(data, status){
                                instance.confirmButtonLoading = false;
                                done();
                                if(status !== 200) return;
                                for(var i=0; i<self.table.data.length; i++){
                                    if(self.table.data[i].id === row.id){
                                        self.table.data.splice(i ,1);
                                        break;
                                    }
                                }
                                self.table.page.total -= 1;
                                if(self.table.data.length<=0 && self.table.page.currentPage>1){
                                    self.load(self.table.page.currentPage - 1);
                                }
                            }, null, [403,404,409,500]);
                        }else{
                            instance.confirmButtonLoading = false;
                            done();
                        }
                    }
                }).catch(function(){});
            },
            handleSizeChange:function(pageSize){
                var self = this;
                self.table.page.pageSize = pageSize;
                self.load(1);
            },
            handleCurrentChange:function(currentPage){
                var self = this;
                self.load(currentPage);
            },
            handleAddFolderClose:function(){
                var self = this;
                self.dialog.addFolder.name = '';
                self.dialog.addFolder.ip = '';
                self.dialog.addFolder.port = '';
                self.dialog.addFolder.folderPath = '';
                self.dialog.addFolder.protocolType = '';
                self.dialog.addFolder.username = '';
                self.dialog.addFolder.password = '';
                self.dialog.addFolder.loading = false;
                self.dialog.addFolder.visible = false;
            },
            handleAddFolderCommit:function(){
                var self = this;
                self.dialog.addFolder.loading = true;
                ajax.post('/monitor/external/static/resource/folder/add', {
                    name:self.dialog.addFolder.name,
                    ip:self.dialog.addFolder.ip,
                    port:self.dialog.addFolder.port,
                    folderPath:self.dialog.addFolder.folderPath,
                    protocolType:self.dialog.addFolder.protocolType,
                    username:self.dialog.addFolder.username,
                    password:self.dialog.addFolder.password
                }, function(data, status){
                    self.dialog.addFolder.loading = false;
                    if(status !== 200) return;
                    self.table.data.push(data);
                    self.table.page.total += 1;
                    self.handleAddFolderClose();
                }, null, [403, 404, 408, 409, 500]);
            },
            handleEditFolderClose:function(){
                var self = this;
                self.dialog.editFolder.id = '';
                self.dialog.editFolder.name = '';
                self.dialog.editFolder.ip = '';
                self.dialog.editFolder.port = '';
                self.dialog.editFolder.folderPath = '';
                self.dialog.editFolder.protocolType = '';
                self.dialog.editFolder.username = '';
                self.dialog.editFolder.password = '';
                self.dialog.editFolder.loading = false;
                self.dialog.editFolder.visible = false;
            },
            handleEditFolderCommit:function(){
                var self = this;
                self.dialog.editFolder.loading = true;
                ajax.post('/monitor/external/static/resource/folder/edit/' + self.dialog.editFolder.id, {
                    name:self.dialog.editFolder.name,
                    ip:self.dialog.editFolder.ip,
                    port:self.dialog.editFolder.port,
                    folderPath:self.dialog.editFolder.folderPath,
                    protocolType:self.dialog.editFolder.protocolType,
                    username:self.dialog.editFolder.username,
                    password:self.dialog.editFolder.password
                }, function(data, status){
                    self.dialog.editFolder.loading = false;
                    if(status !== 200) return;
                    for(var i=0; i<self.table.data.length; i++){
                        if(self.table.data[i].id === data.id){
                            self.table.data.splice(i, 1, data);
                            break;
                        }
                    }
                    self.handleEditFolderClose();
                }, null, [403, 404, 408, 409, 500]);
            }
        },
        mounted:function(){
            var self = this;
            ajax.post('/monitor/external/static/resource/folder/query/types', null, function(data){
                self.protocolTypes.splice(0, self.protocolTypes.length);
                if(data && data.length>0){
                    for(var i=0; i<data.length; i++){
                        self.protocolTypes.push(data[i]);
                    }
                }
            });
            self.load(1);
        }
    });

    return Vue;
});