/**
 * Created by lvdeyang on 2018/12/18 0018.
 */
define([
    'text!' + window.APPPATH + 'backstage/access-point-param/page-backstage-access-point-param.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'backstage/access-point-param/page-backstage-access-point-param.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-backstage-access-point-param';

    var init = function(p){

        var serviceId = p.serviceId;
        var serviceName = p.serviceName;
        var serviceType = p.serviceType;
        var accessPointId = p.accessPointId;
        var accessPointName = p.accessPointName;

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                activeId:'page-backstage-service-rest',
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                serviceName:serviceName,
                accessPointName:accessPointName,
                paramTypes:[],
                paramDirections:[],
                internalConstraints:[],
                table:{
                    rows:[],
                    pageSize:50,
                    currentPage:0,
                    total:0
                },
                dialog:{
                    createAccessPointParam:{
                        visible:false,
                        name:'',
                        keyPrefix:'_' + accessPointId + '_',
                        keySuffix:'',
                        primaryKey:'',
                        defaultValue:'',
                        type:'',
                        values:[],
                        direction:'',
                        serial:'',
                        remarks:'',
                        enum:{
                            inputVisible:false,
                            inputValue:''
                        },
                        constraint:[],
                        loading:false
                    },
                    editParamConstraint:{
                        visible:false,
                        contentShow:false,
                        constraint:{
                            //custom or internal
                            type:'custom',
                            //if custom string else internalConstraint width params
                            content:'',
                            //if internalConstraint then current selected constraint
                            current:'',
                            //标明约束的来源
                            origin:'edit'
                        },
                        activeTab:'custom'
                    },
                    editAccessPointParam:{
                        visible:false,
                        loading:false
                    }
                }
            },
            computed:{
                dialogCreateAccessPointParamPrimaryKey:function(){
                    var self = this;
                    return self.dialog.createAccessPointParam.primaryKey;
                },
                dialogCreateAccessPointParamValuesLength:function(){
                    var self = this;
                    return self.dialog.createAccessPointParam.values.length;
                },
                dialogCreateAccessPointParamKeySuffix:function(){
                    var self = this;
                    return self.dialog.createAccessPointParam.keySuffix;
                }
            },
            watch:{
                dialogCreateAccessPointParamPrimaryKey:function(newValue, oldValue){
                    var self = this;
                    var newExpression = '#' + newValue;
                    var oldExpression = '#' + oldValue;
                    var constraint = self.dialog.createAccessPointParam.constraint;
                    if(constraint && constraint.length>0){
                        for(var i=0; i<constraint.length; i++){
                            var scope = constraint[i];
                            if(scope.type === 'custom'){
                                scope.content = scope.content.replace(new RegExp(oldExpression,'g'), newExpression);
                            }else if(scope.type === 'internal'){
                                var params = scope.current.params;
                                if(params && params.length>0){
                                    for(var j=0; j<params.length; j++){
                                        var param = params[j];
                                        if(param.paramType !== 'TARGET') continue;
                                        param.value = newExpression;
                                    }
                                }
                            }
                        }
                    }
                },
                dialogCreateAccessPointParamValuesLength:function(newValue, oldValue){
                    var self = this;
                    var constraint = self.dialog.createAccessPointParam.constraint;
                    var expression = '#' + self.dialog.createAccessPointParam.primaryKey;
                    var origin = 'paramType';
                    var targetConstraint = null;
                    for(var i=0; i<constraint.length; i++){
                        if(constraint[i].origin === origin){
                            targetConstraint = constraint[i];
                            break;
                        }
                    }
                    var content = 'T(java.util.Arrays).asList({';
                    var values = self.dialog.createAccessPointParam.values;
                    if(values && values.length>0){
                        for(var i=0; i<values.length; i++){
                            content += '\'';
                            content += values[i];
                            content += '\'';
                            if(i !== values.length-1){
                                content += ',';
                            }
                        }
                    }
                    content += '}).contains(';
                    content += expression;
                    content += ')';
                    targetConstraint.content = content;
                },
                dialogCreateAccessPointParamKeySuffix:function(newValue, oldValue){
                    var self = this;
                    self.dialog.createAccessPointParam.primaryKey = self.dialog.createAccessPointParam.keyPrefix + self.dialog.createAccessPointParam.keySuffix;
                }
            },
            methods:{
                rowKey:function(row){
                    return 'access-point-param-' + row.uuid;
                },
                gotoService:function(){
                    var self = this;
                    if(serviceType === 'REST'){
                        return '#/page-backstage-service-rest';
                    }
                },
                gotoAccessPoint:function(){
                    var self = this;
                    return '#/page-backstage-access-point/' + serviceId + '/' + serviceName + '/' + serviceType;
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createAccessPointParam.visible = true;
                },
                handleCreateAccessPointParamClose:function(){
                    var self = this;
                    self.dialog.createAccessPointParam.visible = false;
                    self.dialog.createAccessPointParam.name = '';
                    self.dialog.createAccessPointParam.primaryKey = '';
                    self.dialog.createAccessPointParam.defaultValue = '';
                    self.dialog.createAccessPointParam.type = '';
                    self.dialog.createAccessPointParam.values.splice(0, self.dialog.createAccessPointParam.values.length);
                    self.dialog.createAccessPointParam.direction = '';
                    self.dialog.createAccessPointParam.serial = '';
                    self.dialog.createAccessPointParam.remarks = '';
                    self.dialog.createAccessPointParam.enum.inputVisible = false;
                    self.dialog.createAccessPointParam.enum.inputValue = '';
                    self.dialog.createAccessPointParam.constraint.splice(0, self.dialog.createAccessPointParam.constraint.length);
                    self.dialog.createAccessPointParam.loading = false;
                },
                handleCreateAccessPointParamSubmit:function(){
                    var self = this;
                    self.dialog.createAccessPointParam.loading = true;
                    var paramMetadata = self.dialog.createAccessPointParam;
                    var constraint = paramMetadata.constraint;
                    var constraintExpressions = [];
                    if(constraint && constraint.length){
                        for(var i=0; i<constraint.length; i++){
                            if(constraint[i].type === 'custom'){
                                constraintExpressions.push(constraint[i].content);
                            }else if(constraint[i].type === 'internal'){
                                constraintExpressions.push(self.constraintIdValueTemplate(constraint[i].current));
                            }
                        }
                    }
                    ajax.post('/access/point/param/add', {
                        accessPointId:accessPointId,
                        primaryKey:paramMetadata.primaryKey,
                        name:paramMetadata.name,
                        defaultValue:paramMetadata.defaultValue,
                        remarks:paramMetadata.remarks,
                        serial:paramMetadata.serial,
                        type:paramMetadata.type,
                        values: $.toJSON(paramMetadata.values),
                        direction:paramMetadata.direction,
                        constraintExpressions: $.toJSON(constraintExpressions)
                    }, function(data, status){
                        self.dialog.createAccessPointParam.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateAccessPointParamClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleCreateAccessPointParamConstraintRemove:function(removeable){
                    var self = this;
                    var constraint = self.dialog.createAccessPointParam.constraint;
                    for(var i=0; i<constraint.length; i++){
                        if(constraint[i] === removeable){
                            constraint.splice(i, 1);
                            break;
                        }
                    }
                },
                handleRemoveValue:function(value, index){
                    var self = this;
                    self.dialog.createAccessPointParam.values.splice(index, 1);
                },
                handleCreateAccessPointParamTypeChange:function(v){
                    var self = this;
                    var constraint = self.dialog.createAccessPointParam.constraint;
                    var origin = 'paramType';
                    var expression = '#' + self.dialog.createAccessPointParam.primaryKey;
                    if(v === '枚举') {
                        var content = 'T(java.util.Arrays).asList({';
                        var values = self.dialog.createAccessPointParam.values;
                        if(values && values.length>0){
                            for(var i=0; i<values.length; i++){
                                content += '\'';
                                content += values[i];
                                content += '\'';
                                if(i !== values.length-1){
                                    content += ',';
                                }
                            }
                        }
                        content += '}).contains(';
                        content += expression;
                        content += ')';
                        constraint.push({
                            type:'custom',
                            content:content,
                            current:'',
                            origin:origin
                        });
                    }else{
                        for(var i=0; i<constraint.length; i++){
                            if(constraint[i].origin === origin){
                                constraint.splice(i, 1);
                                break;
                            }
                        }
                    };
                },
                handleDelete:function(){

                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
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
                                ajax.post('/access/point/param/delete/' + row.id, null, function(data, status){
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
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                },
                handleEditAccessPointClose:function(){
                    var self = this;
                },
                handleEditAccessPointSubmit:function(){
                    var self = this;
                    self.dialog.editAccessPointParam.loading = true;
                },
                handleSubParams:function(scope){

                },
                handleSubConstraints:function(scope){

                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/access/point/param/list', {
                        accessPointId:accessPointId,
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    });
                },
                handleEnumInputConfirm:function(event){
                    var self = this;
                    var value = event.target.value;
                    if(value){
                        var repeated = false;
                        for(var i=0; i<self.dialog.createAccessPointParam.values.length; i++){
                            if(self.dialog.createAccessPointParam.values[i] === value){
                                repeated = true;
                                break;
                            }
                        }
                        if(repeated){
                            self.$message({
                                status:'warning',
                                message:'重复的值'
                            });
                        }else{
                            self.dialog.createAccessPointParam.values.push(value);
                        }
                    }
                    self.dialog.createAccessPointParam.enum.inputVisible = false;
                    self.dialog.createAccessPointParam.enum.inputValue = '';
                },
                handleEnumShowInput:function(){
                    var self = this;
                    self.dialog.createAccessPointParam.enum.inputVisible = true;
                    self.$nextTick(function(){
                        self.$refs.enumInput.$refs.input.focus();
                    });
                },
                handleAddConstraint:function(){
                    var self = this;
                    self.dialog.editParamConstraint.contentShow = true;
                    self.dialog.editParamConstraint.visible = true;
                },
                handleEditParamConstraintInsertPrimaryKey:function(){
                    var self = this;
                    var expression = '#' + self.dialog.createAccessPointParam.primaryKey;
                    self.dialog.editParamConstraint.constraint.content += expression;
                    self.$refs.customParamConstraint.$refs.textarea.focus();
                },
                handleEditParamConstraintSubmit:function(){
                    var self = this;
                    self.dialog.createAccessPointParam.constraint.push($.extend(true, {}, self.dialog.editParamConstraint.constraint));
                    self.handleEditParamConstraintClose();
                },
                handleEditParamConstraintClose:function(){
                    var self = this;
                    self.dialog.editParamConstraint.activeTab = 'custom';
                    self.dialog.editParamConstraint.constraint.type = 'custom';
                    self.dialog.editParamConstraint.constraint.content = '';
                    self.dialog.editParamConstraint.constraint.current = '';
                    self.dialog.editParamConstraint.visible = false;
                    self.$nextTick(function(){
                        setTimeout(function(){
                            self.dialog.editParamConstraint.contentShow = false;
                        }, 200);
                    });
                },
                handleEditParamConstraintTabClick:function(tab){
                    var self = this;
                    self.dialog.editParamConstraint.constraint.type = tab.name;
                    self.dialog.editParamConstraint.constraint.content = '';
                },
                handleEditParamConstraintCurrentChange:function(current, old){
                    var self = this;$.extend(true, {}, current);
                    current = $.extend(true, {}, current);
                    //做一下排序
                    var params = current.params;
                    if(params && params.length>0){
                        current.params = params.sort(function(a, b){
                            return a.serial - b.serial;
                        });
                        for(var i=0; i<current.params.length; i++){
                            var param = current.params[i];
                            if(param.paramType === 'TARGET'){
                                param.value = '#' + self.dialog.createAccessPointParam.primaryKey;
                            }
                        }
                    }
                    self.dialog.editParamConstraint.constraint.current = $.extend(true, {}, current);
                },
                editParamConstraintRowKey:function(row){
                    return 'internal-constraint-'+row.primaryKey;
                },
                constraintNameTemplate:function(constraint){
                    var template = constraint.name + '(';
                    var params = constraint.params;
                    for(var i=0; i<params.length; i++){
                        var param = params[i];
                        template += param.name;
                        if(i !== params.length-1){
                            template += ', ';
                        }
                    }
                    template += ')';
                    return template;
                },
                constraintValueTemplate:function(constraint){
                    var template = constraint.name + '(';
                    var params = constraint.params;
                    for(var i=0; i<params.length; i++){
                        var param = params[i];
                        if(param.value || param.value===false) template += param.value;
                        if(i !== params.length-1){
                            template += ', ';
                        }
                    }
                    template += ')';
                    return template;
                },
                constraintRemarksTemplate:function(constraint){
                    var template = constraint.name + '(';
                    var params = constraint.params;
                    for(var i=0; i<params.length; i++){
                        var param = params[i];
                        template += param.remarks;
                        if(i !== params.length-1){
                            template += ', ';
                        }
                    }
                    template += ')';
                    return template;
                },
                constraintIdValueTemplate:function(constraint){
                    var template = constraint.primaryKey + '(';
                    var params = constraint.params;
                    for(var i=0; i<params.length; i++){
                        var param = params[i];
                        if(param.value || param.value===false) template += param.value;
                        if(i !== params.length-1){
                            template += ', ';
                        }
                    }
                    template += ')';
                    return template;
                }
            },
            created:function(){
                var self = this;
                ajax.post('/access/point/param/query/all/types', null, function(data){
                    var paramTypes = data.paramTypes;
                    var paramDirections = data.paramDirections;
                    self.paramTypes = paramTypes;
                    self.paramDirections = paramDirections;
                    self.internalConstraints = data.internalConstraints;
                });
                self.load(1);
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:serviceId/:serviceName/:serviceType/:accessPointId/:accessPointName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});