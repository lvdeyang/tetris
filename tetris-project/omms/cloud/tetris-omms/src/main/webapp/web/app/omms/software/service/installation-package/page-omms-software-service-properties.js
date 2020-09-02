/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'omms/software/service/installation-package/page-omms-software-service-properties.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'date',
    'css!' + window.APPPATH + 'omms/software/service/installation-package/page-omms-software-service-properties.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-omms-software-service-properties';

    var charts = {};

    var init = function(i){

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
                valueTypes:[],
                table:{
                    rows:[],
                    pageSize:50,
                    pageSizes:[5,50, 100, 200, 400],
                    currentPage:0,
                    total:0,
                    installationPackageId:i.installationPackageId   //i.installationPackageId
                },
                dialog:{
                    addProperties:{
                        visible:false,
                        loading:false,
                        installationPackageId:i.installationPackageId,  //i.installationPackageId
                        propertyKey:'',
                        propertyName:'',
                        valueType:'文本',
                        propertyDefaultValue:'',
                        valueSelect:[],
                        loading:false
                    },
                    addEnum:{
                        visible:false,
                        valueSelect:[],
                        name:'',
                        value:'',
                        loading:false
                    },
                    editProperties:{
                        visible:false,
                        id:'',
                        propertyKey:'',
                        propertyName:'',
                        valueType:'',
                        propertyDefaultValue:'',
                        valueSelect:[],
                        loading:false
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                handleSizeChange:function(size){
                    var self = this;
                    self.table.pageSize = size;
                    self.load(self.table.currentPage);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                load:function(currentPage){
                    var self = this;
                    var param = {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    };
                    if(self.table.installationPackageId) param.installationPackageId = self.table.installationPackageId;
                    self.table.rows.splice(0, self.table.rows.length);
                     ajax.post('/properties/load', param, function(data){
                         var total = data.total;
                         var rows = data.rows;
                     if(rows && rows.length>0){
                         for(var i = 0;i<rows.length;i++){
                             rows[i].popvisible = false;
                             self.table.rows.push(rows[i])
                         }
                         self.table.total = total;
                     }
                         self.table.currentPage = currentPage;
                     });
                },
                rowKey:function(row){
                return 'installation-package-' + row.id;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addProperties.visible = true;
                },
                handleAddEnum:function(){
                    var self = this;
                    self.dialog.addEnum.visible = true;
                },
                handleEditPropertiesClose:function(){
                    var self = this;
                    self.dialog.editProperties.id = '';
                    self.dialog.editProperties.propertyKey = '';
                    self.dialog.editProperties.propertyName = '';
                    self.dialog.editProperties.valueType = '';
                    self.dialog.editProperties.propertyDefaultValue = '';
                    self.dialog.addEnum.valueSelect.splice(0,self.dialog.addEnum.valueSelect.length);
                    self.dialog.addEnum.visible = false;
                    self.dialog.editProperties.visible = false;
                },
                handleEditPropertiesSubmit:function(){
                    var self = this ;
                    self.dialog.editProperties.loading = true;
                    var params = {
                        id:self.dialog.editProperties.id,
                        propertyKey:self.dialog.editProperties.propertyKey,
                        propertyName:self.dialog.editProperties.propertyName,
                        valueType:self.dialog.editProperties.valueType,
                        propertyDefaultValue:self.dialog.editProperties.propertyDefaultValue,
//                        valueSelect:JSON.stringify(self.dialog.addEnum.valueSelect)
                    };
                    if(self.dialog.addEnum.valueSelect !== null) params.valueSelect = JSON.stringify(self.dialog.addEnum.valueSelect);
                    ajax.post('/properties/edit/property',
                        params, function(data, status){
                            self.dialog.editProperties.loading = false;
                            self.dialog.editProperties.visible = false;
                            if(status !== 200) return;
                            for(var i=0; i<self.table.rows.length; i++){
                                if(self.table.rows[i].id === self.dialog.editProperties.id){
                                    self.table.rows.splice(i, 1, data);
                                    break;
                                }
                            }
                            self.handleEditPropertiesClose();
                        }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                editPropertiesList:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editProperties.id = row.id
                    self.dialog.editProperties.propertyKey = row.propertyKey;
                    self.dialog.editProperties.propertyName = row.propertyName;
                    self.dialog.editProperties.valueType = row.valueTypeName;
                    self.dialog.editProperties.propertyDefaultValue = row.propertyDefaultValue;
                    self.dialog.addEnum.valueSelect = JSON.parse(row.valueSelect);
                    self.dialog.editProperties.visible = true;
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
                                h('p', null, ['此操作将永久删除该参数，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/properties/delete/' + row.id, null, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id === row.id){
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    done();
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleEnumDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    for(var i=0;i<self.dialog.addEnum.valueSelect.length;i++){
                        if(self.dialog.addEnum.valueSelect[i] === row ){
                            self.dialog.addEnum.valueSelect.splice(i,1);
                            break;
                        }
                    }
                },
                handleAddEnumClose: function () {
                    var self = this;
                    self.dialog.addEnum.name = '';
                    self.dialog.addEnum.value = '';
                    self.dialog.addEnum.visible =false;
                },
                handleAddEnumSubmit:function(){
                    var self = this ;
                    var param = {
                        name:self.dialog.addEnum.name,
                        value:self.dialog.addEnum.value};
                    self.dialog.addEnum.valueSelect.push(param);
                    self.handleAddEnumClose();
                },
                handleAddPropertiesClose:function(){
                    var self = this;
                    self.dialog.addProperties.propertyKey = '';
                    self.dialog.addProperties.propertyName = '';
                    self.dialog.addProperties.valueType = '文本';
                    self.dialog.addProperties.propertyDefaultValue = '';
                    self.dialog.addEnum.valueSelect.splice(0,self.dialog.addEnum.valueSelect.length);
                    self.dialog.addEnum.visible = false;
                    self.dialog.addProperties.visible = false;
                },
                handleAddPropertiesSubmit:function(){
                    var self = this;
                    self.dialog.addProperties.loading = true;
                    var params = {
                        installationPackageId:self.dialog.addProperties.installationPackageId,
                        propertyKey:self.dialog.addProperties.propertyKey,
                        propertyName:self.dialog.addProperties.propertyName,
                        propertyDefaultValue:self.dialog.addProperties.propertyDefaultValue,
                        valueType:self.dialog.addProperties.valueType,
                        valueSelect:JSON.stringify(self.dialog.addEnum.valueSelect)
                    };
                    ajax.post('/properties/add/property',params,function(data,status){
                        self.dialog.addProperties.loading = false;
                        self.dialog.addProperties.visible = false;
                        if(status != 200)return;
                        self.table.rows.push(data);//这里需要修改
                        self.handleAddPropertiesClose();
                    },null,ajax.NO_ERROR_CATCH_CODE);
                },

            },
            created:function(){
                var self = this;
                ajax.post('/properties/find/value/types', null, function(data){
                    for(var i=0; i<data.length; i++){
                        self.valueTypes.push(data[i]);
                    }
                });
                self.load(1);
            },
            mounted:function(){
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:installationPackageId',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});