define([
    'text!' + window.APPPATH + 'component/bvc2-monitor-subtitle/bvc2-monitor-subtitle.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'date',
    'css!' + window.APPPATH + 'component/bvc2-monitor-subtitle/bvc2-monitor-subtitle.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'bvc2-monitor-subtitle';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
                fonts:[],
                table:{
                    data:[],
                    page:{
                        total:0,
                        currentPage:0,
                        pageSize:20
                    }
                },
                dialog:{
                    addSubtitle:{
                        visible:false,
                        name:'',
                        content:'',
                        font:'',
                        height:'',
                        color:'',
                        loading:false
                    },
                    editSubtitle:{
                        visible:false,
                        id:'',
                        name:'',
                        content:'',
                        font:'',
                        height:'',
                        color:'',
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
                ajax.post('/monitor/subtitle/load', {
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
                self.load(1);
            },
            handleCurrentChange:function(currentPage){
                var self = this;
                self.load(currentPage);
            },
            rowDelete:function(scope){
                var self = this;
                var row = scope.row;
                var h = self.$createElement;
                self.$msgbox({
                    title:'危险操作',
                    message:h('div', null, [
                        h('div', {class:'el-message-box__status el-icon-warning'}, null),
                        h('div', {class:'el-message-box__message'}, [
                            h('p', null, ['此操作将永久删除字幕，且不可恢复，是否继续?'])
                        ])
                    ]),
                    type:'wraning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose:function(action, instance, done){
                        instance.confirmButtonLoading = true;
                        if(action === 'confirm'){
                            ajax.post('/monitor/subtitle/remove/' + row.id, null, function(data, status){
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
            rowEdit:function(scope){
                var self = this;
                var row = scope.row;
                self.dialog.editSubtitle.id = row.id;
                self.dialog.editSubtitle.name = row.name;
                self.dialog.editSubtitle.content = row.content;
                self.dialog.editSubtitle.font = row.font;
                self.dialog.editSubtitle.height = parseInt(row.height);
                self.dialog.editSubtitle.color = '#' + row.color;
                self.dialog.editSubtitle.visible = true;
            },
            handleAddSubtitle:function(){
                var self = this;
                self.dialog.addSubtitle.visible = true;
                self.dialog.addSubtitle.height = 3;
                self.dialog.addSubtitle.font = '黑体';
                self.dialog.addSubtitle.color = '#ffffff';
            },
            handleAddSubtitleClose:function(){
                var self = this;
                self.dialog.addSubtitle.name = '';
                self.dialog.addSubtitle.content = '';
                self.dialog.addSubtitle.font = '';
                self.dialog.addSubtitle.height = '';
                self.dialog.addSubtitle.color = '';
                self.dialog.addSubtitle.visible = false;
                self.dialog.addSubtitle.loading = false;
            },
            handleAddSubtitleCommit:function(){
                var self = this;
                self.dialog.addSubtitle.loading = true;
                ajax.post('/monitor/subtitle/add', {
                    name:self.dialog.addSubtitle.name,
                    content:self.dialog.addSubtitle.content,
                    font:self.dialog.addSubtitle.font,
                    height:self.dialog.addSubtitle.height,
                    color:(!self.dialog.addSubtitle.color?null:self.dialog.addSubtitle.color.substring(1, self.dialog.addSubtitle.color.length))
                }, function(data, status){
                    self.dialog.addSubtitle.loading = false;
                    if(status !== 200) return;
                    self.table.data.push(data);
                    self.handleAddSubtitleClose();
                }, null, [403, 404, 408, 409, 500]);
            },
            handleEditSubtitleClose:function(){
                var self = this;
                self.dialog.editSubtitle.id = '';
                self.dialog.editSubtitle.name = '';
                self.dialog.editSubtitle.content = '';
                self.dialog.editSubtitle.font = '';
                self.dialog.editSubtitle.height = '';
                self.dialog.editSubtitle.color = '';
                self.dialog.editSubtitle.visible = false;
                self.dialog.editSubtitle.loading = false;
            },
            handleEditSubtitleCommit:function(){
                var self = this;
                self.dialog.editSubtitle.loading = true;
                ajax.post('/monitor/subtitle/edit/' + self.dialog.editSubtitle.id, {
                    name:self.dialog.editSubtitle.name,
                    content:self.dialog.editSubtitle.content,
                    font:self.dialog.editSubtitle.font,
                    height:self.dialog.editSubtitle.height,
                    color:(!self.dialog.editSubtitle.color?null:self.dialog.editSubtitle.color.substring(1, self.dialog.editSubtitle.color.length))
                }, function(data, status){
                    self.dialog.editSubtitle.loading = false;
                    if(status !== 200) return;
                    for(var i=0; i<self.table.data.length; i++){
                        if(self.table.data[i].id === self.dialog.editSubtitle.id){
                            self.table.data.splice(i, 1, data);
                            break;
                        }
                    }
                    self.handleEditSubtitleClose();
                }, null, [403, 404, 408, 409, 500]);
            }
        },
        mounted:function(){
            var self = this;
            self.load(1);
            ajax.post('/monitor/subtitle/query/fonts', null, function(data){
                if(data && data.length>0){
                    for(var i=0; i<data.length; i++){
                        self.fonts.push(data[i]);
                    }
                }
            });
        }
    });

    return Vue;
});