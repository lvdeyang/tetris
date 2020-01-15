define([
    'text!' + window.APPPATH + 'group/resource/virtual-source/page-group-virtual-source.html',
    'restfull',
    'config',
    'commons',
    'jquery.layout.auto',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-table-base',
    'extral'
], function(tpl, ajax, config, commons, $, Vue){

    var pageId = 'page-group-virtual-source';

    var getLayoutContainer = function(el){
        return $(el).find('.bvc2-dialog-auto-layout-editor');
    };

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/virtual/query/tree/' + p.id, null, function(roots){
            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
                    menurouter: false,
                    shortCutsRoutes:commons.data,
                    active:"/page-group-list",
                    header:commons.getHeader(0),
                    table:{
                        buttonCreate:'新建虚拟源',
                        buttonRemove:'删除虚拟源',
                        columns:[{
                            label:'虚拟源名称',
                            prop:'name',
                            type:'simple'
                        }],
                        load:'/virtual/load/' + p.id,
                        save:'/virtual/save/' + p.id,
                        update:'/virtual/update',
                        remove:'/virtual/remove',
                        removebatch:'/virtual/remove/all',
                        options:[{
                            label:'布局设置',
                            icon:'el-icon-setting',
                            click:'edit-layout'
                        }],
                        pk:'id',
                        currentRow:''
                    },
                    dialog:{
                        show:false,
                        tree:{
                            defaultProps:{
                                children:'children',
                                label:'name',
                                isLeaf:'isLeaf'
                            },
                            defaultExpandAll:false,
                            data:roots
                        }
                    },
                    layout:{row:2,column:2,cellspan:[]},
                    target:'',
                    options:{
                        columnOptions:[
                            {
                                value:1,
                                label:1
                            },{
                                value:2,
                                label:2
                            },{
                                value:3,
                                label:3
                            },{
                                value:4,
                                label:4
                            }
                        ],
                        rowOptions:[
                            {
                                value:1,
                                label:1
                            },{
                                value:2,
                                label:2
                            },{
                                value:3,
                                label:3
                            },{
                                value:4,
                                label:4
                            }
                        ]
                    }
                },
                watch:{
                    layout:function(){
                        var instance = this;
                        setTimeout(function(){
                            instance.refreshLayout();
                        }, 0);
                    }
                },
                methods:{
                    itemBack:function(){
                        window.location.hash = '#/page-group-control/' + p.id;
                    },
                    refreshLayout:function(){
                        var $container = getLayoutContainer(this.$el);
                        var options = this.layout;
                        $container['layout-auto']('create', {
                            cell:{
                                column:options.column,
                                row:options.row
                            },
                            cellspan:options.cellspan,
                            theme:'dark',
                            name:options.name,
                            editable:true,
                            event:{
                                drop:function(e){
                                    var data = $.parseJSON(e.dataTransfer.getData('data'));
                                    var param = $.parseJSON(data.param);
                                    var $cell = $(this);
                                    $cell.empty().append('<span style="color:#fff; font-size:14px;">'+param.bundleName+'('+data.name+')</span>');
                                    $cell['layout-auto']('setData', {
                                        bundleId:param.bundleId,
                                        bundleName:param.bundleName,
                                        layerId:param.nodeUid,
                                        channelId:param.channelId,
                                        channelName:param.channelName,
                                        memberId:param.memberId,
                                        channelMemberId:param.channelMemberId,
                                        channelType:param.channelType,
                                        memberChannelName:data.name
                                    });
                                }
                            }
                        });
                    },
                    rowChange:function(row){
                        var layout_instance = this;
                        //获取设置前的值
                        var $container = getLayoutContainer(layout_instance.$el);
                        var oldRow = $container['layout-auto']('queryRow');
                        layout_instance.$confirm('此操作将清空屏幕上的配置，并且设置为：' + row + '行' + layout_instance.layout.column + '列, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning',
                            beforeClose:function(action, instance, done){
                                if(action === 'confirm'){
                                    var newLayout = $.extend(true, {}, layout_instance.layout);
                                    newLayout.row = row;
                                    newLayout.cellspan = [];
                                    layout_instance.layout = newLayout;
                                }else{
                                    layout_instance.layout.row = oldRow;
                                }
                                done();
                            }
                        });
                    },
                    columnChange:function(column){
                        var layout_instance = this;
                        //获取设置前的值
                        var $container = getLayoutContainer(layout_instance.$el);
                        var oldColumn = $container['layout-auto']('queryColumn');
                        layout_instance.$confirm('此操作将清空屏幕上的配置，并且设置为：' + layout_instance.layout.row + '行' + column + '列, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning',
                            beforeClose:function(action, instance, done){
                                if(action === 'confirm'){
                                    var newLayout = $.extend(true, {}, layout_instance.layout);
                                    newLayout.column = column;
                                    newLayout.cellspan = [];
                                    layout_instance.layout = newLayout;
                                }else if(action === 'cancel'){
                                    layout_instance.layout.column = oldColumn;
                                }
                                done();
                            }
                        });
                    },
                    editLayout:function(row, done, instance_use){
                        var instance = this;
                        if(instance_use){
                            instance = instance_use;
                        }
                        instance.table.currentRow = row;
                        instance.dialog.show = true;
                        ajax.get('/virtual/query/setted/layout/' + row.id, null, function(data){
                            var websiteDraw = $.parseJSON(data.websiteDraw);
                            var _cellspan = [];
                            var srcArr = data.srcs;
                            if(websiteDraw){
                                instance.layout.row = websiteDraw.basic.row;
                                instance.layout.column = websiteDraw.basic.column;
                                _cellspan = websiteDraw.cellspan;
                            }
                            var $container = $('#layout-auto-wrapper');
                            $container['layout-auto']('create', {
                                cell:{
                                    column:instance.layout.column,
                                    row:instance.layout.row
                                },
                                cellspan:_cellspan,
                                theme:'dark',
                                editable:true,
                                event:{
                                    drop:function(e){
                                        var data = $.parseJSON(e.dataTransfer.getData('data'));
                                        var param = $.parseJSON(data.param);
                                        var $cell = $(this);
                                        $cell.empty().append('<span style="color:#fff; font-size:14px;">'+param.bundleName+'('+data.name+')</span>');
                                        $cell['layout-auto']('setData', {
                                            bundleId:param.bundleId,
                                            bundleName:param.bundleName,
                                            layerId:param.nodeUid,
                                            channelId:param.channelId,
                                            channelName:param.channelName,
                                            memberId:param.memberId,
                                            channelMemberId:param.channelMemberId,
                                            memberChannelName:data.name
                                        });
                                    }
                                }
                            });
                            //初始化数据
                            if(srcArr && srcArr.length>0){
                                var cells = $container['layout-auto']('getCells');
                                for(var i=0; i<cells.length; i++){
                                    var cell = cells[i];
                                    var data = null;
                                    for(var j=0; j<srcArr.length; j++){
                                        if(cell.serialNum == srcArr[j].serialnum){
                                            data = {
                                                bundleId:srcArr[j].bundleId,
                                                bundleName:srcArr[j].bundleName,
                                                layerId:srcArr[j].layerId,
                                                channelId:srcArr[j].channelId,
                                                channelName:srcArr[j].channelName,
                                                memberId:srcArr[j].memberId,
                                                channelMemberId:srcArr[j].memberChannelId,
                                                memberChannelName:srcArr[j].memberChannelName
                                            };
                                            break;
                                        }
                                    }
                                    if(data){
                                        cell.$cell['layout-auto']('setData', data);
                                        cell.$cell.empty().append('<span style="color:#fff; font-size:14px;">'+data.bundleName+'('+data.memberChannelName+')</span>');
                                    }
                                }
                            }
                        });
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
                        if(node.data.type === 'CHANNEL'){
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
                        var tpl = $container['layout-auto']('generateTpl');
                        var webSiteDraw = {
                            basic:tpl.basic,
                            cellspan:tpl.cellspan
                        };
                        var effectiveLayout = [];
                        var effectData =[];
                        for(var i=0; i<tpl.layout.length; i++){
                            if(tpl.layout[i].data) effectData.push(tpl.layout[i]);
                            effectiveLayout.push(tpl.layout[i]);
                        }
                        if(effectData.length > 0){
                            ajax.post('/virtual/do/config/' + row.id, {
                                webSiteDraw: $.toJSON(webSiteDraw),
                                config: $.toJSON(effectiveLayout)
                            }, function(){
                                instance.dialog.show = false;
                                instance.$message({
                                    message:'保存成功',
                                    type:'success'
                                });
                            });
                        }else{
                            instance.$message({
                                message:'虚拟源未配置',
                                type:'warning'
                            });
                        }
                    },
                    afterAdd:function(row){
                        var instance = this;
                        instance.$confirm('是否立即设置布局？', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'info',
                            beforeClose:function(action, ins, d){
                                if(action === 'confirm'){
                                    var instance2 = instance;
                                    instance2.editLayout(row, null, instance2);
                                    d();
                                }else if(action === 'cancel'){
                                    d();
                                }
                            }
                        });
                    }
                }

            });
        });
    };

    var destroy = function(){

    };

    var virtualSource = {
        path:'/' + pageId + '/:id',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return virtualSource;
});