/**
 * Created by lvdeyang on 2018/12/18 0018.
 */
define([
    'text!' + window.APPPATH + 'access-point/page-access-point.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-access-point';

    var init = function(p){

        var serviceId = p.serviceId;
        var serviceName = p.serviceName;
        var serviceType = p.serviceType;

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                activeId:'page-service-rest',
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                serviceName:serviceName,
                accessPointTypes:'',
                paramTypes:[],
                paramDirections:[],
                internalConstraints:[],
                table:{
                    rows:[],
                    pageSize:50,
                    currentPage:0,
                    total:0,
                    current:''
                },
                tree:{
                    pValue:{
                        props:{
                            label: 'name',
                            children: 'sub'
                        },
                        expandOnClickNode:false,
                        data:[],
                        loading:false
                    },
                    rValue:{
                        props:{
                            label: 'name',
                            children: 'sub'
                        },
                        expandOnClickNode:false,
                        data:[],
                        loading:false
                    }
                },
                dialog:{
                    createAccessPoint:{
                        visible:false,
                        scope:'系统域',
                        name:'',
                        type:'',
                        method:'',
                        remarks:'',
                        loading:false
                    },
                    editAccessPoint:{
                        visible:false,
                        id:'',
                        uuid:'',
                        name:'',
                        type:'',
                        method:'',
                        remarks:'',
                        loading:false
                    },
                    createAccessPointParam:{
                        visible:false,
                        action:'',
                        parentKeyPath:'',
                        parentReferenceKeyPath:'',
                        accessPointId:'',
                        name:'',
                        //保证id的为一性，以“_pa + 接入点id + _”开头
                        keyPrefix:'',
                        keySuffix:'',
                        primaryKey:'',
                        referenceKey:'',
                        defaultValue:'',
                        type:'',
                        values:[],
                        serial:'',
                        remarks:'',
                        parent:'',
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
                    var expression = '#' + self.dialog.createAccessPointParam.parentKeyPath + self.dialog.createAccessPointParam.primaryKey;
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
                    if(targetConstraint) targetConstraint.content = content;
                },
                dialogCreateAccessPointParamKeySuffix:function(newValue, oldValue){
                    var self = this;
                    self.dialog.createAccessPointParam.primaryKey = self.dialog.createAccessPointParam.keyPrefix + self.dialog.createAccessPointParam.keySuffix;
                }
            },
            methods:{
                handleCurrentRowChange:function(currentRow, oldCurrentRow){
                    var self = this;
                    self.tree.pValue.data.splice(0, self.tree.pValue.data.length);
                    self.tree.rValue.data.splice(0, self.tree.rValue.data.length);
                    ajax.post('/access/point/param/list/tree', {
                        accessPointId:currentRow.id
                    }, function(data){
                        self.table.current = currentRow;
                        self.dialog.createAccessPointParam.keyPrefix = '_pa' + currentRow.id + '_';
                        self.dialog.createAccessPointParam.accessPointId = currentRow.id;
                        var paramValues = data.paramValues;
                        var returnValues = data.returnValues;
                        if(paramValues && paramValues.length>0){
                            for(var i=0; i<paramValues.length; i++){
                                self.tree.pValue.data.push(paramValues[i]);
                            }
                        }
                        if(returnValues && returnValues.length>0){
                            for(var i=0; i<returnValues.length; i++){
                                self.tree.rValue.data.push(returnValues[i]);
                            }
                        }
                    });
                },
                rowKey:function(row){
                    return 'access-point-' + row.uuid;
                },
                gotoService:function(){
                    var self = this;
                    if(serviceType === 'REST'){
                        return '#/page-service-rest';
                    }
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.createAccessPoint.visible = true;
                },
                handleCreateAccessPointClose:function(){
                    var self = this;
                    self.dialog.createAccessPoint.name = '';
                    self.dialog.createAccessPoint.type = '';
                    self.dialog.createAccessPoint.method = '';
                    self.dialog.createAccessPoint.remarks = '';
                    self.dialog.createAccessPoint.loading = false;
                    self.dialog.createAccessPoint.visible = false;
                },
                handleCreateAccessPointSubmit:function(){
                    var self = this;
                    self.dialog.createAccessPoint.loading = true;
                    ajax.post('/access/point/add', {
                        serviceId:serviceId,
                        serviceType:serviceType,
                        scope:self.dialog.createAccessPoint.scope,
                        name:self.dialog.createAccessPoint.name,
                        type:self.dialog.createAccessPoint.type,
                        method:self.dialog.createAccessPoint.method,
                        remarks:self.dialog.createAccessPoint.remarks
                    }, function(data, status){
                        self.dialog.createAccessPoint.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.handleCreateAccessPointClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
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
                                h('p', null, ['此操作将永久删除该接口，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/access/point/delete/' + row.id, null, function(data, status){
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
                    self.dialog.editAccessPoint.id = row.id;
                    self.dialog.editAccessPoint.uuid = row.uuid;
                    self.dialog.editAccessPoint.name = row.name;
                    self.dialog.editAccessPoint.type = row.type;
                    self.dialog.editAccessPoint.method = row.method;
                    self.dialog.editAccessPoint.remarks = row.remarks;
                    self.dialog.editAccessPoint.visible = true;
                },
                handleEditAccessPointClose:function(){
                    var self = this;
                    self.dialog.editAccessPoint.id = '';
                    self.dialog.editAccessPoint.uuid = '';
                    self.dialog.editAccessPoint.name = '';
                    self.dialog.editAccessPoint.type = '';
                    self.dialog.editAccessPoint.method = '';
                    self.dialog.editAccessPoint.remarks = '';
                    self.dialog.editAccessPoint.visible = false;
                    self.dialog.editAccessPoint.loading = false;
                },
                handleEditAccessPointSubmit:function(){
                    var self = this;
                    self.dialog.editAccessPoint.loading = true;
                    ajax.post('/access/point/edit/' + self.dialog.editAccessPoint.id, {
                        name:self.dialog.editAccessPoint.name,
                        type:self.dialog.editAccessPoint.type,
                        method:self.dialog.editAccessPoint.method,
                        remarks:self.dialog.editAccessPoint.remarks
                    }, function(data, status){
                        self.dialog.editAccessPoint.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            var row = self.table.rows[i];
                            if(row.id === self.dialog.editAccessPoint.id){
                                row.name = self.dialog.editAccessPoint.name;
                                row.type = self.dialog.editAccessPoint.type;
                                row.method = self.dialog.editAccessPoint.method;
                                row.remarks = self.dialog.editAccessPoint.remarks;
                                break;
                            }
                        }
                        self.handleEditAccessPointClose();
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                handleSubParams:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-access-point-param/' + serviceId + '/' + serviceName + '/' + serviceType + '/' + row.id + '/' + row.name;
                },
                handleSubConstraints:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-joint-constraint-expression/' + serviceId + '/' + serviceName + '/' + serviceType + '/' + row.id + '/' + row.name;
                },
                load:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/access/point/list', {
                        serviceId:serviceId,
                        serviceType:serviceType,
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
                            self.table.currentPage = currentPage;
                        }
                    });
                },

                /*******************
                 ******参数编辑*****
                 *******************/

                handleDeleteAccessPointParam:function(param, node, paramDerection){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该参数以及所有子参数，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/access/point/param/delete/' + param.id, null, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    if(status !== 200) return;
                                    if(paramDerection === 'pValue'){
                                        self.$refs.pValueTree.remove(node);
                                    }else if(paramDerection === 'rValue'){
                                        self.$refs.rValueTree.remove(node);
                                    }
                                    done();
                                    self.$nextTick(function(){
                                        console.log(self.tree.pValue);
                                    });
                                }, null, ajax.NO_ERROR_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },

                handleCreateAccessPointParam:function(action, parent, parentNode){
                    var self = this;
                    self.dialog.createAccessPointParam.visible = true;
                    self.dialog.createAccessPointParam.action = action;
                    if(parent) self.dialog.createAccessPointParam.parent = parent;
                    if(parentNode){
                        var parentKeyPath = '';
                        var parentReferenceKeyPath = '';
                        var currentNode = parentNode;
                        while(true){
                            parentKeyPath = currentNode.data.primaryKey + '.' + parentKeyPath;
                            parentReferenceKeyPath = currentNode.data.referenceKey + '.' + parentReferenceKeyPath;
                            if(currentNode.level <= 1){
                                break;
                            }else{
                                currentNode = currentNode.parent;
                            }
                        }
                        self.dialog.createAccessPointParam.parentKeyPath = parentKeyPath;
                        self.dialog.createAccessPointParam.parentReferenceKeyPath = parentReferenceKeyPath;
                    }else{
                        self.dialog.createAccessPointParam.parentKeyPath = '';
                        self.dialog.createAccessPointParam.parentReferenceKeyPath = '';
                    }
                },
                handleCreateAccessPointParamClose:function(){
                    var self = this;
                    self.dialog.createAccessPointParam.visible = false;
                    self.dialog.createAccessPointParam.name = '';
                    self.dialog.createAccessPointParam.keySuffix = '';
                    self.dialog.createAccessPointParam.primaryKey = '';
                    self.dialog.createAccessPointParam.parentKeyPath = '';
                    self.dialog.createAccessPointParam.referenceKey = '';
                    self.dialog.createAccessPointParam.parentReferenceKeyPath = '';
                    self.dialog.createAccessPointParam.defaultValue = '';
                    self.dialog.createAccessPointParam.type = '';
                    self.dialog.createAccessPointParam.values.splice(0, self.dialog.createAccessPointParam.values.length);
                    self.dialog.createAccessPointParam.serial = '';
                    self.dialog.createAccessPointParam.remarks = '';
                    self.dialog.createAccessPointParam.enum.inputVisible = false;
                    self.dialog.createAccessPointParam.enum.inputValue = '';
                    self.dialog.createAccessPointParam.constraint.splice(0, self.dialog.createAccessPointParam.constraint.length);
                    self.dialog.createAccessPointParam.parent = '';
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

                    var action = self.dialog.createAccessPointParam.action;
                    var parent = self.dialog.createAccessPointParam.parent;
                    var uri = '';
                    if(action === 'pValue'){
                        if(parent){
                            uri = '/access/point/param/add/param/value'
                        }else{
                            uri = '/access/point/param/add/root/param/value';
                        }
                    }else if(action === 'rValue'){
                        if(parent){
                            uri = '/access/point/param/add/return/value';
                        }else{
                            uri = '/access/point/param/add/root/return/value';
                        }
                    }

                    var params = {
                        accessPointId:paramMetadata.accessPointId,
                        primaryKey:paramMetadata.primaryKey,
                        primaryKeyPath:paramMetadata.parentKeyPath + paramMetadata.primaryKey,
                        referenceKey:paramMetadata.referenceKey,
                        referenceKeyPath:paramMetadata.parentReferenceKeyPath + paramMetadata.referenceKey,
                        name:paramMetadata.name,
                        defaultValue:paramMetadata.defaultValue,
                        remarks:paramMetadata.remarks,
                        serial:paramMetadata.serial,
                        type:paramMetadata.type,
                        values: $.toJSON(paramMetadata.values),
                        constraintExpressions: $.toJSON(constraintExpressions)
                    };

                    if(parent) params.parentId = parent.id;

                    ajax.post(uri, params, function(data, status){
                        self.dialog.createAccessPointParam.loading = false;
                        if(status !== 200) return;

                        if(action === 'pValue'){
                            if(parent){
                                if(!parent.sub) Vue.set(parent, 'sub', []);
                                parent.sub.push(data);
                            }else{
                                self.tree.pValue.data.push(data);
                            }
                        }else if(action === 'rValue'){
                            if(parent){
                                if(!parent.sub) Vue.set(parent, 'sub', []);
                                parent.sub.push(data);
                            }else{
                                self.tree.rValue.data.push(data);
                            }
                        }

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
                    var expression = '#' + self.dialog.createAccessPointParam.parentKeyPath + self.dialog.createAccessPointParam.primaryKey;
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
                    var expression = '#' + self.dialog.createAccessPointParam.parentKeyPath + self.dialog.createAccessPointParam.primaryKey;
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
                                param.value = '#' + self.dialog.createAccessPointParam.parentKeyPath + self.dialog.createAccessPointParam.primaryKey;
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
                ajax.post('/access/point/query/all/types', null, function(data){
                    self.accessPointTypes = data;
                });
                self.load(1);
                ajax.post('/access/point/param/query/all/types', null, function(data){
                    var paramTypes = data.paramTypes;
                    var paramDirections = data.paramDirections;
                    self.paramTypes = paramTypes;
                    self.paramDirections = paramDirections;
                    self.internalConstraints = data.internalConstraints;
                });
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:serviceId/:serviceName/:serviceType',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});