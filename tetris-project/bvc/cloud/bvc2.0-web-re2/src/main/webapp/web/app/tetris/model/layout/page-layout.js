/**
 * Created by lvdeyang on 2020/6/5.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/layout/page-layout.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/layout/page-layout.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-layout';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                table:{
                    rows:[],
                    currentPage:0,
                    pageSizes:[20, 50, 100, 1000],
                    pageSize:20,
                    total:0,
                    current:''
                },
                positions:{
                    rows:[],
                    backup:[],
                    current:''
                },
                positionTypes:[],
                dialog:{
                    addLayout:{
                        visible:false,
                        loading:false,
                        name:''
                    },
                    editLayout:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    addPositions:{
                        visible:false,
                        loading:false,
                        layoutId:'',
                        number:1,
                        beginIndex:0
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadPositionTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/layout/position/load/types', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.positionTypes.push(data[i]);
                            }
                        }
                    })
                },
                load:function(currentPage){
                    var self = this;
                    ajax.post('/tetris/bvc/model/layout/load', {
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
                            self.table.current = rows[0];
                            self.loadPositions();
                        }
                        self.table.currentPage = currentPage;
                    });
                },
                handleCreate:function(){
                    var self = this;
                    self.dialog.addLayout.visible = true;
                },
                rowKey:function(row){
                    return 'layout-'+row.id;
                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editLayout.id = row.id;
                    self.dialog.editLayout.name = row.name;
                    self.dialog.editLayout.visible = true;
                },
                gotoLayoutForward:function(scope){
                    var self = this;
                    var row = scope.row;
                    window.location.hash = '#/page-layout-forward/' + row.id + '/' + row.name;
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
                                h('p', null, ['此操作将永久删除该虚拟源，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/layout/remove', {
                                    id:row.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.table.rows.length; i++){
                                        if(self.table.rows[i].id == row.id){
                                            self.table.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    if(self.table.current === row){
                                        self.table.current = '';
                                        self.positions.rows.splice(i, self.positions.rows.length);
                                        self.positions.current = '';
                                    }
                                    self.table.total -= 1;
                                    if(self.table.rows.length==0 && self.table.total>0){
                                        self.load(self.table.currentPage-1);
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleSizeChange:function(pageSize){
                    var self = this;
                    self.table.pageSize = pageSize;
                    self.load(1);
                },
                handleCurrentChange:function(currentPage){
                    var self = this;
                    self.load(currentPage);
                },
                currentRowChange:function(row){
                    var self = this;
                    self.table.current = row;
                    self.loadPositions();
                },
                handleAddLayoutClose:function(){
                    var self = this;
                    self.dialog.addLayout.visible = false;
                    self.dialog.addLayout.loading = false;
                    self.dialog.addLayout.name = '';
                },
                handleAddLayoutSubmit:function(){
                    var self = this;
                    self.dialog.addLayout.loading = true;
                    ajax.post('/tetris/bvc/model/layout/add', {
                        name:self.dialog.addLayout.name
                    }, function(data, status){
                        self.dialog.addLayout.loading = false;
                        if(status !== 200) return;
                        self.table.rows.push(data);
                        self.table.total += 1;
                        self.handleAddLayoutClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditLayoutClose:function(){
                    var self = this;
                    self.dialog.editLayout.visible = false;
                    self.dialog.editLayout.loading = false;
                    self.dialog.editLayout.id = '';
                    self.dialog.editLayout.name = '';
                },
                handleEditLayoutSubmit:function(){
                    var self = this;
                    self.dialog.editLayout.loading = true;
                    ajax.post('/tetris/bvc/model/layout/edit', {
                        id:self.dialog.editLayout.id,
                        name:self.dialog.editLayout.name
                    }, function(data, status){
                        self.dialog.editLayout.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditLayoutClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                loadPositions:function(){
                    var self = this;
                    self.positions.rows.splice(0, self.positions.rows.length);
                    self.positions.backup.splice(0, self.positions.backup.length);
                    ajax.post('/tetris/bvc/model/layout/position/load', {
                        layoutId:self.table.current.id
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                data[i].current = false;
                                self.positions.rows.push(data[i]);
                                self.positions.backup.push($.extend({}, data[i], true));
                            }
                        }
                    });
                },
                handleAddPositions:function(){
                    var self = this;
                    self.dialog.addPositions.visible = true;
                    self.dialog.addPositions.beginIndex = self.positions.rows.length+1;
                    self.dialog.addPositions.layoutId = self.table.current.id;
                },
                handleAddPositionsClose:function(){
                    var self = this;
                    self.dialog.addPositions.visible = false;
                    self.dialog.addPositions.loading = false;
                    self.dialog.addPositions.layoutId = '';
                    self.dialog.addPositions.number = 1;
                    self.dialog.addPositions.beginIndex = 0;
                },
                handleAddPositionsSubmit:function(){
                    var self = this;
                    self.dialog.addPositions.loading = true;
                    ajax.post('/tetris/bvc/model/layout/position/add', {
                        layoutId:self.dialog.addPositions.layoutId,
                        number:self.dialog.addPositions.number,
                        beginIndex:self.dialog.addPositions.beginIndex
                    }, function(data, status){
                        self.dialog.addPositions.loading = false;
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                data[i].current = false;
                                self.positions.rows.push(data[i]);
                            }
                        }
                        self.handleAddPositionsClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handlePositionCurrentChange:function(currentPosition){
                    var self = this;
                    for(var i=0; i<self.positions.rows.length; i++){
                        if(self.positions.rows[i].current){
                            self.positions.rows[i].current = false;
                        }
                    }
                    currentPosition.current = true;
                    self.positions.current = currentPosition;
                },
                handleRemovePosition:function(position){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'危险操作',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该虚拟源布局，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/layout/position/remove', {
                                    id:position.id
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    for(var i=0; i<self.positions.rows.length; i++){
                                        if(self.positions.rows[i].id == position.id){
                                            self.positions.rows.splice(i, 1);
                                            break;
                                        }
                                    }
                                    for(var i=0; i<self.positions.backup.length; i++){
                                        if(self.positions.backup[i].id == position.id){
                                            self.positions.backup.splice(i, 1);
                                            break;
                                        }
                                    }
                                    if(self.positions.current == position){
                                        self.positions.current = '';
                                    }
                                    if(data && data.length>0){
                                        for(var i=0; i<data.length; i++){
                                            for(var j=0; j<self.positions.rows.length; j++){
                                                if(data[i].id === self.positions.rows[j].id){
                                                    self.positions.rows[j].serialNum = data[i].serialNum;
                                                    break;
                                                }
                                            }
                                            for(var j=0; j<self.positions.backup.length; j++){
                                                if(data[i].id === self.positions.backup[j].id){
                                                    self.positions.backup[j].serialNum = data[i].serialNum;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                positionStyle:function(position, width, height){
                    var style = '';
                    style += 'left:' + parseInt(position.x)*width/10000 + 'px;';
                    style += 'top:' + parseInt(position.y)*height/10000 + 'px;';
                    style += 'width:' + ((parseInt(position.width)*width/10000)+1) + 'px;';
                    style += 'height:' + ((parseInt(position.height)*height/10000)+1) + 'px;';
                    //style += 'line-height:' + parseInt(position.height)*width/10000 + 'px;';
                    style += 'z-index:' + parseInt(position.zIndex);
                    return style;
                },
                savePositionsEdit:function(){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否保存此次编辑?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/layout/position/edit', {
                                    positions: $.toJSON(self.positions.rows)
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.positions.rows.splice(0, self.positions.rows.length);
                                    self.positions.backup.splice(0, self.positions.backup.length);
                                    if(data && data.length>0){
                                        for(var i=0; i<data.length; i++){
                                            if(self.positions.current && self.positions.current.id==data[i].id){
                                                data[i].current = true;
                                                self.positions.current = data[i];
                                            }else{
                                                data[i].current = false;
                                            }
                                            self.positions.rows.push(data[i]);
                                            self.positions.backup.push($.extend({}, data[i], true));
                                        }
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                cancelPositionsEdit:function(){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否取消此次编辑?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                for(var i=0; i<self.positions.rows.length; i++){
                                    var row = self.positions.rows[i];
                                    for(var j=0; j<self.positions.backup.length; j++){
                                        var back = self.positions.backup[j];
                                        if(row.id === back.id){
                                            row.x = back.x;
                                            row.y = back.y;
                                            row.width = back.width;
                                            row.height = back.height;
                                            row.zIndex = back.zIndex;
                                            break;
                                        }
                                    }
                                }
                                instance.confirmButtonLoading = false;
                                done();
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                }
            },
            created:function(){
                var self = this;
                self.load(1);
                self.loadPositionTypes();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});