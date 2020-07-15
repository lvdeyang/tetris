define([
    'text!' + window.APPPATH + 'system/jv230/page-jv230-control.html',
    'restfull',
    'config',
    'commons',
    'jquery.layout.auto',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base',
    'extral'
], function(tpl, ajax, config, commons, $, Vue){

    var pageId = 'page-jv230-control';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/jv230/query/useable/jav230', null, function(roots){
            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-jv230-control",
                    header:commons.getHeader(1),
                    side:{
                        active:'1'
                    },
                    table:{
                        buttonCreate:'新建拼接屏',
                        buttonRemove:'删除拼接屏',
                        columns:[{
                            label:'拼接屏名称',
                            prop:'name',
                            type:'simple'
                        },{
                            label:'行',
                            prop:'row',
                            type:'simple'
                        },{
                            label:'列',
                            prop:'column',
                            type:'simple'
                        }],
                        load:'/jv230/load',
                        save:'/jv230/save',
                        update:'/jv230/update',
                        remove:'/jv230/remove',
                        removebatch:'/jv230/remove/all',
                        options:[{
                            label:'布局设置',
                            icon:'el-icon-setting',
                            click:'edit-layout'
                        },{
                            label:'大屏配置',
                            icon:'el-icon-document',
                            click:'edit-config'
                        }],
                        pk:'id',
                        currentRow:''
                    },
                    largescreen:{
                        combineJv230Id:'',
                        buttonCreate:'新建配置',
                        buttonRemove:'删除配置',
                        columns:[{
                            label:'名称',
                            prop:'name',
                            type:'simple'
                        },{
                            label:'上屏状态',
                            prop:'status',
                            type:'simple',
                            editable:false
                        },{
                            label:'备注',
                            prop:'remark',
                            type:'simple'
                        }],
                        load:'/jv230/query/combineJv230/config',
                        save:'/jv230/save/combineJv230/config',
                        update:'/jv230/update/combineJv230/config',
                        remove:'/jv230/remove/combineJv230/config',
                        removebatch:'/jv230/remove/all/combineJv230/config',
                        options:[{
                            label:'大屏控制',
                            icon:'icon-signin',
                            iconStyle:'font-size:16px;',
                            click:'enter-control'
                        }],
                        pk:'id',
                        currentRow:''
                    },
                    dialog:{
                        layout:{
                            show:false,
                            tree:{
                                defaultProps:{
                                    children:'children',
                                    label:'name',
                                    isLeaf:'isLeaf'
                                },
                                defaultExpandAll:true,
                                data:roots
                            }
                        },
                        largescreen:{
                            show:false
                        }
                    }
                },
                methods:{
                    beforeSave:function(row, done){
                        if(!row.column) Vue.set(row, 'column', '4');
                        if(!row.row) Vue.set(row, 'row', '4');
                        done();
                    },
                    editLayout:function(row, done){
                        var instance = this;
                        instance.table.currentRow = row;
                        instance.dialog.layout.show = true;
                        ajax.get('/jv230/query/setted/jv230/' + row.id, null, function(data){
                            var jv230Arr = data;
                            var $container = $('#layout-auto-wrapper');
                            $container['layout-auto']('create', {
                                cell:{
                                    column:row.column,
                                    row:row.row
                                },
                                theme:'dark',
                                editable:false,
                                event:{
                                    drop:function(e){
                                        var data = $.parseJSON(e.dataTransfer.getData('data'));
                                        var param = $.parseJSON(data.param);
                                        var $cell = $(this);
                                        $cell.empty().append('<span style="color:#fff; font-size:14px;">'+data.name+'</span>');
                                        $cell['layout-auto']('setData', {
                                            bundleId:param.bundleId,
                                            bundleName:data.name,
                                            layerId:param.nodeUid
                                        });
                                    }
                                }
                            });
                            //初始化数据
                            if(jv230Arr && jv230Arr.length>0){
                                var cells = $container['layout-auto']('getCells');
                                for(var i=0; i<cells.length; i++){
                                    var cell = cells[i];
                                    var data = null;
                                    for(var j=0; j<jv230Arr.length; j++){
                                        if(cell.serialNum == jv230Arr[j].serialnum){
                                            data = {
                                                bundleId:jv230Arr[j].bundleId,
                                                bundleName:jv230Arr[j].bundleName,
                                                layerId:jv230Arr[j].layerId
                                            };
                                            break;
                                        }
                                    }
                                    if(data){
                                        cell.$cell['layout-auto']('setData', data);
                                        cell.$cell.empty().append('<span style="color:#fff; font-size:14px;">'+data.bundleName+'</span>');
                                    }
                                }
                            }
                        });
                    },
                    editConfig:function(row, done){
                        var instance = this;
                        instance.largescreen.load = '/jv230/query/combineJv230/config/' + row.id;
                        instance.largescreen.save = '/jv230/save/combineJv230/config/' + row.id;
                        instance.largescreen.combineJv230Id = row.id;
                        instance.dialog.largescreen.show = true;
                    },
                    rowAddSaveBefore:function(row, done){
                        row.status = "未上屏";
                        done();
                    },
                    renderContent:function(h, target){
                        var node = target.node,
                            data = target.data;
                        if(data.type == 'CHANNEL'){
                            if(JSON.parse(data.param).channelType != 'VenusVideoIn'){
                                node.visible = false;
                            }
                        }

                        var c = {};
                        c[node.icon] = true;
                        return h('span', {
                            class: {
                                'bvc2-tree-node-custom': true
                            }
                        }, [
                            h('span', null, [
                                h('span', {
                                    class: c,
                                    style: {
                                        'font-size': '16px',
                                        'position': 'relative',
                                        'top': '1px',
                                        'margin-right': '5px'
                                    }
                                }, null),
                                node.label
                            ])
                        ]);
                    },
                    nodeExpand:function(data, node, instance){
                        if(data.icon === 'icon-folder-close'){
                            data.icon = 'icon-folder-open';
                        }
                    },
                    nodeCollapse:function(data, node, instance){
                        if(data.icon === 'icon-folder-open'){
                            data.icon = 'icon-folder-close';
                        }
                    },
                    handleDragStart:function(node, e){
                        //放置数据
                        e.dataTransfer.setData('data', $.toJSON(node.data));
                    },
                    allowDrop:function(node){
                        return false;
                    },
                    allowDrag:function(node){
                        if(node.data.type === 'BUNDLE'){
                            return true;
                        }
                        return false;
                    },
                    dialogClose:function(){
                        this.table.currentRow = '';
                    },
                    saveLayout:function(){
                        var instance = this;
                        var row = instance.table.currentRow;
                        var $container = $('#layout-auto-wrapper');
                        var layout = $container['layout-auto']('generateLayout');
                        var effectiveLayout = [];
                        for(var i=0; i<layout.length; i++){
                            if(layout[i].data) effectiveLayout.push(layout[i]);
                        }
                        if(effectiveLayout.length > 0){
                            ajax.post('/jv230/do/config/' + row.id, {
                                config: $.toJSON(effectiveLayout)
                            }, function(){
                                instance.dialog.layout.show = false;
                                instance.$message({
                                    message:'保存成功',
                                    type:'success'
                                });
                            });
                        }
                    },
                    enterControl:function(row, e){
                        var instance = this;
                        window.location.href = "/web/app/group/largescreen/html/page-group-largescreen-control.html?" + instance.largescreen.combineJv230Id + "&" + row.id;
                    }
                }

            });
        });
    };

    var destroy = function(){

    };

    var jv210status = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return jv210status;



});