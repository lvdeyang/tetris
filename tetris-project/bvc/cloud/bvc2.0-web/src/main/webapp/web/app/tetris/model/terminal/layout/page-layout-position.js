/**
 * Created by lvdeyang on 2020/6/24.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/layout/page-layout-position.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/terminal/layout/page-layout-position.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-layout-position';

    var init = function(p){

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
                layoutId:p.layoutId,
                layoutName:p.layoutName,
                screenPrimaryKeys:[],
                table:{
                    rows:[]
                },
                dialog: {
                    addPosition:{
                        visible:false,
                        loading:false,
                        screenPrimaryKeys:[],
                        props:{
                            label:'name',
                            children:'children',
                            isLeaf:'isLeaf'
                        }
                    },
                    editPosition:{
                        visible:false,
                        loading:false,
                        id:'',
                        x:'',
                        y:'',
                        width:'',
                        height:'',
                        zIndex:''
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                rowKey:function(row){
                    return 'position-' + row.id;
                },
                screenStyle:function(row){
                    var self = this;
                    var edit = self.dialog.editPosition;
                    var outerWidth = 400;
                    var outerHeight = 400;
                    var x = row.id==edit.id ? (edit.x||0) : (row.x||0);
                    var y = row.id==edit.id ? (edit.y||0) : (row.y||0);
                    var width = row.id==edit.id ? (edit.width||0) : (row.width||0);
                    var height = row.id==edit.id ? (edit.height||0) : (row.height||0);
                    var zIndex = row.id==edit.id ? (edit.zIndex||0) : (row.zIndex||0);
                    var style = [
                        'left:'+x*outerWidth/10000+'px;',
                        'top:'+y*outerHeight/10000+'px;',
                        'width:'+width*outerWidth/10000+'px;',
                        'height:'+height*outerHeight/10000+'px;',
                        'line-height:'+height*outerHeight/10000+'px;',
                        'zIndex:'+zIndex
                    ];
                    return style.join(' ');
                },
                handleCreate:function(){
                    var self = this;
                    for(var i=0; i<self.screenPrimaryKeys.length; i++){
                        var exist = false;
                        for(var j=0; j<self.table.rows.length; j++){
                            if(self.screenPrimaryKeys[i].screenPrimaryKey == self.table.rows[j].screenPrimaryKey){
                                exist = true;
                                break;
                            }
                        }
                        if(!exist){
                            self.dialog.addPosition.screenPrimaryKeys.push(self.screenPrimaryKeys[i]);
                        }
                    }
                    self.dialog.addPosition.visible = true;
                },
                handleDelete:function(){

                },
                handleAddPositionClose:function(){
                    var self = this;
                    self.dialog.addPosition.screenPrimaryKeys.splice(0, self.dialog.addPosition.screenPrimaryKeys.length);
                    self.dialog.addPosition.visible = false;
                },
                handleAddPositionSubmit:function(){
                    var self = this;
                    var nodes = self.$refs.addPositionScreenPrimaryKeyTree.getCheckedNodes();
                    if(!nodes || nodes.length<=0){
                        self.$message({
                            type:'error',
                            message:'请选择屏幕'
                        });
                        return;
                    }
                    var screenPrimaryKeys = [];
                    for(var i=0; i<nodes.length; i++){
                        screenPrimaryKeys.push(nodes[i].screenPrimaryKey);
                    }
                    self.dialog.addPosition.visible = true;
                    ajax.post('/tetris/bvc/model/terminal/layout/position/add/batch', {
                        layoutId:self.layoutId,
                        screenPrimaryKeys: $.toJSON(screenPrimaryKeys)
                    }, function(data, status){
                        self.dialog.addPosition.visible = false;
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRowEdit:function(scope){
                    var self = this;
                    var row = scope.row;
                    self.dialog.editPosition.id = row.id;
                    self.dialog.editPosition.x = row.x || 0;
                    self.dialog.editPosition.y = row.y || 0;
                    self.dialog.editPosition.width = row.width || 0;
                    self.dialog.editPosition.height = row.height || 0;
                    self.dialog.editPosition.zIndex = row.zIndex || 0;
                    self.dialog.editPosition.visible = true;
                },
                handleEditPositionClose:function(){
                    var self = this;
                    self.dialog.editPosition.id = '';
                    self.dialog.editPosition.x = '';
                    self.dialog.editPosition.y = '';
                    self.dialog.editPosition.width = '';
                    self.dialog.editPosition.height = '';
                    self.dialog.editPosition.zIndex = '';
                    self.dialog.editPosition.visible = false;
                    self.dialog.editPosition.loading = false;
                },
                handleEditPositionSubmit:function(){
                    var self = this;
                    self.dialog.editPosition.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/layout/position/edit', {
                        id:self.dialog.editPosition.id,
                        x:self.dialog.editPosition.x||0,
                        y:self.dialog.editPosition.y||0,
                        width:self.dialog.editPosition.width||0,
                        height:self.dialog.editPosition.height||0,
                        zIndex:self.dialog.editPosition.zIndex||0
                    }, function(data, status){
                        self.dialog.editPosition.loading = false;
                        if(status !== 200) return;
                        for(var i=0; i<self.table.rows.length; i++){
                            if(self.table.rows[i].id == data.id){
                                self.table.rows.splice(i, 1, data);
                                break;
                            }
                        }
                        self.handleEditPositionClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRowDelete:function(scope){
                    var self = this;
                    var row = scope.row;
                    var h = self.$createElement;
                    self.$msgbox({
                        title: '危险操作',
                        message: h('div', null, [
                            h('div', {class: 'el-message-box__status el-icon-warning'}, null),
                            h('div', {class: 'el-message-box__message'}, [
                                h('p', null, ['此操作将永久删除该排版，且不可恢复，是否继续?'])
                            ])
                        ]),
                        type: 'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose: function (action, instance, done) {
                            instance.confirmButtonLoading = true;
                            if (action === 'confirm') {
                                ajax.post('/tetris/bvc/model/terminal/layout/position/delete', {
                                    id: row.id
                                }, function (data, status) {
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if (status !== 200) return;
                                    for (var i = 0; i < self.table.rows.length; i++) {
                                        if (self.table.rows[i].id == row.id) {
                                            self.table.rows.splice(i, 1);
                                        }
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            } else {
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function () {
                    });
                },
                load:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/layout/position/load', {
                        layoutId:self.layoutId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.table.rows.push(data[i]);
                            }
                        }
                    });
                },
                loadScreenPrimaryKeys:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/screen/primary/key/load', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.screenPrimaryKeys.push(data[i]);
                            }
                        }
                    });
                }
            },
            created:function(){
                var self = this;
                self.load();
                self.loadScreenPrimaryKeys();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId + '/:layoutId/:layoutName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});