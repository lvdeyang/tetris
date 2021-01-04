
define([
    'text!' + window.APPPATH + 'omms/hardware/database/backup/page-omms-hardware-database-backup.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/hardware/database/backup/page-omms-hardware-database-backup.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-hardware-database-backup';

    var charts = {};

    var init = function(p){

        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                basePath:window.BASEPATH,
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                databaseId: p.databaseId,
                databaseName: p.databaseName,
                rows:[],
                dialog:{
                    selectIndex:{
                        visible: false,
                    },
                    restore:{
                        visible: false,
                        backupDatabase:'',
                        backupServer:'',
                    },
                },
                form:
                {
                    imgSavePath: ''
                },
                hardwareListData:[

                ],//后台返回的多选项
                checkedData: [],//选择多选框时的选中值
                zz:[0],
                checkedId:[
                    {realName:''}
                ],

            },
            computed:{

            },
            watch:{

            },
            methods: {
                handleChange:function(e,id){
                    var self = this;
                    if(e){
                        self.checkedData.push(id);
                    }else{
                        self.delete(id)
                    }
                    console.log(self.checkedData);
                },
                delete(id){
                    var self = this;
                    var index = self.checkedData.findIndex(item => {
                        if(item == id){
                            return true;
                        }
                    });
                    self.checkedData.splice(index,1)
                },
                transArrToString(arr) {
                    if (!Array.isArray(arr)) return false;
                    var str = "";
                    for (var i = 0, len = arr.length; i < len; i++) {
                        i == 0 ? (str = arr[i]) : (str += "," + arr[i]);
                    }
                    return str;
                },
                handleRecoverClose:function(){

                },
                handleRecoverSubmit:function(){

                },
                load:function(){
                    var self = this;
                    ajax.post('/database/query/backup/databases',
                        {id:self.databaseId},
                        function(data){
                        for(var i = 0; i < data.length; i++){
                            self.rows.push(data[i]);
                        }
                    })
                },
                rowKey:function(row){
                    return row.id;
                },

                //handleRowBackup:function(scope){
                //    var self = this;
                //    var row = scope.row;
                //    self.dialog.restore.backupDatabase = row.id;
                //    self.dialog.restore.visible = true;
                //
                //},
                handleRowBackup:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.restore.backupDatabase = row.backupname;
                    ajax.post('/database/recover/database',
                        {
                            databaseBackupId:row.id,
                            databaseName:self.dialog.restore.backupDatabase,
                        },function(data,status){
                            if(status !== 200) return;
                        })

                },
                handleAddServerClose:function(){
                    var self = this;
                    self.dialog.selectIndex.visible = false;
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
                                h('p', null, ['此操作将永久删除该备份文件，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/database/delete/backup', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.rows.length; i++){
                                        if(self.rows[i].id === row.id){
                                            self.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    //self.rows.page.total -= 1;
                                    //if(self.rows.total>0 && self.rows.length===0){
                                    //    self.load(self.table.page.currentPage - 1);
                                    //}
                                    done();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleRowDownload:function(scope){
                    var self = this;
                    var row = scope.row;
                    ajax.post('/database/download/backup',
                        {
                            databaseBackupId:row.id
                        },
                        function(data){
                        var name = data.name;
                        var uri = data.uri;
                        var a = document.createElement('a');
                        var event = new MouseEvent('click');
                        a.download = name;
                        a.href = window.BASEPATH + uri;
                        a.dispatchEvent(event)
                    });
                },
                fileChange(e) {
                    try {
                        const fu = document.getElementById('file')
                        if (fu == null) return
                        this.form.imgSavePath = fu.files[0].path
                        console.log(fu.files[0].path)
                    } catch (error) {
                        console.debug('choice file err:', error)
                    }
                },
                btnChange() {
                    var file = document.getElementById('file')
                    file.click()
                }

            },
            mounted:function(){
                var self = this ;
                self.load(1)
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:databaseId/:databaseName',
        component:{
            template:'<div id="' + pageId + '"class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});