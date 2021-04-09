/**
 * Created by lqxuhv on 2020/10/26.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/editor/page-terminal-editor.html',
    'jquery',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'echarts',
    window.APPPATH + 'tetris/model/terminal/editor/graph-options',
    'element-ui',
    window.APPPATH + 'tetris/model/terminal/editor/channel-dialog',
    'mi-frame',
    'string',
    'css!' + window.APPPATH + 'tetris/model/terminal/editor/page-terminal-editor.css',
    'css!' + window.APPPATH + 'tetris/model/terminal/editor/graph-node-icons/style.css'
], function(tpl, $, config, ajax, context, commons, Vue, echarts, DefaultTreeSeries){

    var pageId = 'page-terminal-editor';

    var vueInstance = null;

    var init = function(p){

        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        vueInstance = new Vue({
            el: '#' + pageId + '-wrapper',
            data: {
                menus: context.getProp('menus'),
                user: context.getProp('user'),
                groups: context.getProp('groups'),
                terminalId: p.terminalId,
                terminalName: p.terminalName,
                physicalScreenLayout:{
                    enable: false,
                    columns:0,
                    rows:0,
                    physicalScreens:[]
                },
                videoChannelParams:[],
                contextmenu:{
                    visible:false,
                    x:0,
                    y:0,
                    move:false,
                    edit:false,
                    remove:false
                },
                graph:{
                    $el:'',
                    echarts:'',
                    data:'',
                    current:'',
                    options:{
                        series:[]
                    },
                    typeSort:{
                        'PHYSICAL_SCREEN':0,
                        'VIDEO_ENCODE_CHANNEL':1,
                        'VIDEO_DECODE_CHANNEL':2,
                        'AUDIO_ENCODE_CHANNEL':3,
                        'AUDIO_DECODE_CHANNEL':4,
                        'CHANNEL_PERMISSION':5,
                        'AUDIO_OUTPUT':6
                    },
                    typeAction:{
                        'TERMINAL':{move:false, disconnect:false, remove:false},
                        'PHYSICAL_SCREEN':{move:false, edit:true, remove:true},
                        'VIDEO_ENCODE_CHANNEL':{move:true, edit:true, remove:true},
                        'VIDEO_DECODE_CHANNEL':{move:true, edit:true, remove:true},
                        'AUDIO_ENCODE_CHANNEL':{move:false, edit:true, remove:true},
                        'AUDIO_DECODE_CHANNEL':{move:true, edit:true, remove:true},
                        'CHANNEL_PERMISSION':{move:false, edit:false, remove:false},
                        'AUDIO_OUTPUT':{move:true, edit:true, remove:true}
                    }
                },
                dialog:{
                    terminalChannelColumns:[{prop:'name', label:'通道名称'}, {prop:'typeName', label:'通道类型'}],
                    terminalBundleChannelColumns:[{prop:'terminalBundleName', label:'终端设备'},{prop:'channelId', label:'通道'},{prop:'typeName', label:'类型'}],
                    physicalScreenColumns:[{prop:'name', label:'物理屏幕'}],
                    audioOutputColumns:[{prop:'name', label:'音频输出'}],
                    addPhysicalScreen:{
                        visible:false,
                        loading:false,
                        name:''
                    },
                    editPhysicalScreen:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    removePhysicalScreen:{
                        visible:false,
                        loading:false,
                        id:'',
                        removeChildren:false
                    },
                    addVideoEncodeChannel:{
                        visible:false,
                        loading:false,
                        name:'',
                        selectAudioChannel:''
                    },
                    editVideoEncodeChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    moveVideoEncodeChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        selectAudioChannel:''
                    },
                    addVideoDecodeChannel:{
                        visible:false,
                        loading:false,
                        name:'',
                        selectPhysicalScreen:''
                    },
                    editVideoDecodeChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    moveVideoDecodeChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        selectPhysicalScreen:''
                    },
                    addAudioEncodeChannel:{
                        visible:false,
                        loading:false,
                        name:'',
                        selectTerminalBundleAudioChannel:''
                    },
                    editAudioEncodeChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        selectTerminalBundleAudioChannel:''
                    },
                    removeAudioEncodeChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        removeChildren:false
                    },
                    addAudioDecodeChannel:{
                        visible:false,
                        loading:false,
                        name:'',
                        selectTerminalAudioOutput:'',
                        selectTerminalBundleAudioChannel:''
                    },
                    editAudioDecodeChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:'',
                        selectTerminalBundleAudioChannel:''
                    },
                    moveAudioDecodeChannel:{
                        visible:false,
                        loading:false,
                        id:'',
                        selectTerminalAudioOutput:''
                    },
                    addAudioOutput:{
                        visible:false,
                        loading:false,
                        name:'',
                        selectPhysicalScreen:''
                    },
                    editAudioOutput:{
                        visible:false,
                        loading:false,
                        id:'',
                        name:''
                    },
                    moveAudioOutput:{
                        visible:false,
                        loading:false,
                        id:'',
                        selectPhysicalScreen:''
                    },
                    removeAudioOutput:{
                        visible:false,
                        loading:false,
                        id:'',
                        removeChildren:false
                    }
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                handlePhysicalScreenLayoutEnableChange:function(enableLayout){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/layout/enable/change', {
                        terminalId:self.terminalId,
                        enableLayout:enableLayout
                    });
                },
                handleLayoutColumnsChange:function(columns){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/layout/columns/change', {
                        terminalId:self.terminalId,
                        columns:columns
                    }, function(){
                        for(var i=0; i<self.physicalScreenLayout.physicalScreens.length; i++){
                            self.physicalScreenLayout.physicalScreens[i].column = null;
                            self.physicalScreenLayout.physicalScreens[i].row = null;
                            self.physicalScreenLayout.physicalScreens[i].x = null;
                            self.physicalScreenLayout.physicalScreens[i].y = null;
                            self.physicalScreenLayout.physicalScreens[i].width = null;
                            self.physicalScreenLayout.physicalScreens[i].height = null;
                        }
                    });
                },
                handleLayoutRowsChange:function(rows){
                    var self = this;
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/layout/rows/change', {
                        terminalId:self.terminalId,
                        rows:rows
                    }, function(){
                        for(var i=0; i<self.physicalScreenLayout.physicalScreens.length; i++){
                            self.physicalScreenLayout.physicalScreens[i].column = null;
                            self.physicalScreenLayout.physicalScreens[i].row = null;
                            self.physicalScreenLayout.physicalScreens[i].x = null;
                            self.physicalScreenLayout.physicalScreens[i].y = null;
                            self.physicalScreenLayout.physicalScreens[i].width = null;
                            self.physicalScreenLayout.physicalScreens[i].height = null;
                        }
                    });
                },
                getPhysicalScreenName:function(column, row){
                    var self = this;
                    if(!self.physicalScreenLayout.enable){
                        return '未启用';
                    }
                    if(self.physicalScreenLayout.physicalScreens && self.physicalScreenLayout.physicalScreens.length>0){
                        for(var i=0; i<self.physicalScreenLayout.physicalScreens.length; i++){
                            if(self.physicalScreenLayout.physicalScreens[i].column===column &&
                                self.physicalScreenLayout.physicalScreens[i].row===row){
                                return self.physicalScreenLayout.physicalScreens[i].name
                            }
                        }
                    }
                    return '未设置';
                },
                setPhysicalScreenLayout:function(column, row){
                    var self = this;
                    if(!self.physicalScreenLayout.enable){
                        return;
                    }
                    self.loadPhysicalScreen(function(physicalScreens){
                        self.$refs.setPhysicalScreenLayout.show('选择物理屏幕', self.dialog.physicalScreenColumns, physicalScreens, [column, row]);
                    });
                },
                onPhysicalScreenLayoutSet:function(physicalScreen, buff, done){
                    var self = this;
                    var column = buff[0];
                    var row = buff[1];
                    var width = parseInt(10000/self.physicalScreenLayout.columns);
                    var height = parseInt(10000/self.physicalScreenLayout.rows);
                    ajax.post('/tetris/bvc/model/terminal/physical/screen/set/physical/screen/layout', {
                        terminalPhysicalScreenId:physicalScreen.id,
                        column:column,
                        row:row,
                        x:(column-1)*width,
                        y:(row-1)*height,
                        width:width,
                        height:height
                    }, function(data){
                        var finded = false;
                        var needPush = [];
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                var physicalScreen = data[i];
                                for(var j=0; j<self.physicalScreenLayout.physicalScreens.length; j++){
                                    if(self.physicalScreenLayout.physicalScreens[j].id == physicalScreen.id){
                                        self.physicalScreenLayout.physicalScreens.splice(j, 1, physicalScreen);
                                        finded = true;
                                        break;
                                    }
                                }
                                if(!finded){
                                    needPush.push(physicalScreen);
                                }
                            }
                        }
                        for(var i=0; i<needPush.length; i++){
                            self.physicalScreenLayout.physicalScreens.push(needPush[i]);
                        }
                        done();
                    });
                },
                loadGraph:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/graph/load', {
                        terminalId:self.terminalId
                    }, function(data){
                        if(data){
                            self.graph.data = data;
                        }
                        self.initGraph();
                    });
                },
                initGraph:function(){
                    var self = this;
                    self.graph.$el = self.$el.querySelector('.terminal-graph');
                    self.graph.echarts = echarts.init(self.graph.$el);
                    self.graph.$el.addEventListener('contextmenu', function(e){
                        e.preventDefault();
                    });
                    /*self.graph.echarts.on('click', function(params){
                        self.onGraphNodeClick(params);
                    });*/
                    self.graph.echarts.on('mouseover', function(params){
                        self.onGraphNodeMouseover(params);
                    });
                    self.graph.echarts.on('mouseout', function(params){
                        self.onGraphNodeMouseout(params);
                    });
                    self.graph.echarts.on('contextmenu', function(params){
                        if(params.data.type==='TERMINAL' || params.data.type==='CHANNEL_PERMISSION'){
                            self.$message({
                                type:'warning',
                                message:'当前节点无法编辑'
                            });
                            return;
                        }
                        self.onGraphNodeClick(params);
                        self.onGraphNodeContextmenu(params);
                    });
                    var series = new DefaultTreeSeries(self.graph.data.id, self.graph.data.name, self.graph.data);
                    self.graph.options.series.push(series);
                    self.render();
                },
                sortNode:function(nodes){
                    var self = this;
                    if(!nodes || nodes.length<=0) return;
                    nodes.sort(function(n1, n2){
                        if(n1.type === n2.type){
                            if(n1.name > n2.name){
                                return 1;
                            }else{
                                return -1;
                            }
                        }else{
                            return self.graph.typeSort[n1.type] - self.graph.typeSort[n2.type];
                        }
                    });
                },
                sortTree:function(root){
                    var self = this;
                    if(!root) root = self.graph.data;
                    if(root.children && root.children.length>0){
                        self.sortNode(root.children);
                        for(var i=0; i<root.children.length; i++){
                            self.sortTree(root.children[i]);
                        }
                    }else{
                        return;
                    }
                },
                render:function(sort){
                    var self = this;
                    sort = typeof sort==='undefined'?true:sort;
                    if(sort){
                        self.sortTree();
                    }
                    if(self.graph.echarts){
                        self.graph.echarts.clear();
                    }
                    self.graph.echarts.setOption(self.graph.options);
                },
                findNode:function(target, root){
                    var self = this;
                    root = root || self.graph.data;
                    if(root.id === target.id){
                        return root;
                    }else{
                        if(root.children && root.children.length>0){
                            for(var i=0; i<root.children.length; i++){
                                var node = self.findNode(target, root.children[i]);
                                if(node) return node;
                            }
                        }
                    }
                },
                findParent:function(target, root, parent){
                    var self = this;
                    root = root || self.graph.data;
                    if(root.id === target.id){
                        return parent;
                    }else{
                        if(root.children && root.children.length>0){
                            for(var i=0; i<root.children.length; i++){
                                var node = self.findParent(target, root.children[i], root);
                                if(node) return node;
                            }
                        }
                    }
                },
                removeNode:function(target, nodes){
                    var self = this;
                    nodes = nodes || self.graph.data.children;
                    if(nodes && nodes.length>0){
                        for(var i=0; i<nodes.length; i++){
                            var node = nodes[i];
                            if(nodes[i].id === target.id){
                                nodes.splice(i, 1);
                                return node;
                            }
                        }
                        for(var i=0; i<nodes.length; i++){
                            var node = nodes[i];
                            if(node && node.children && node.children.length>0){
                                var n = self.removeNode(target, node.children);
                                if(n) return n;
                            }
                        }
                    }
                },
                setChannelParamsTarget:function(nodes){
                    var self = this;
                    for(var i=0; i<nodes.length; i++){
                        var bundleChannelNode = nodes[i];
                        for(var j=0; j<self.videoChannelParams.length; j++){
                            var channelParam = self.videoChannelParams[j];
                            if(channelParam.key === bundleChannelNode.params.channelParamsType){
                                channelParam.target = {
                                    id:bundleChannelNode.params.terminalBundleChannelId,
                                    channelId:bundleChannelNode.params.channelId,
                                    type:bundleChannelNode.params.type,
                                    typeName:bundleChannelNode.params.typeName,
                                    terminalBundleId:bundleChannelNode.params.terminalBundleId,
                                    terminalBundleName:bundleChannelNode.params.bundleName,
                                    bundleType:bundleChannelNode.params.deviceMode,
                                    terminalBundleType:bundleChannelNode.params.bundleType,
                                    terminalBundleTypeName:bundleChannelNode.params.bundleTypeName,
                                    terminalId:bundleChannelNode.params.terminalId
                                };
                                break;
                            }
                        }
                    }
                },
                clearChannelParamsTarget:function(){
                    var self = this;
                    for(var i=0; i<self.videoChannelParams.length; i++){
                        self.videoChannelParams[i].target = '';
                    }
                },
                onGraphNodeClick:function(params){
                    var self = this;
                    var currentNode = self.findNode(params.data);
                    console.log(currentNode);
                    if(self.graph.current === currentNode) return;
                    if(self.graph.current){
                        self.graph.current.current = false;
                        self.graph.current.itemStyle = '';
                        self.graph.current.label = '';
                    }
                    self.graph.current = currentNode;
                    currentNode.itemStyle = {
                        borderColor:'#409EFF'
                    };
                    currentNode.label = {
                        color:'#409EFF'
                    };
                    currentNode.current = true;
                    self.render(false);
                },
                onGraphNodeMouseover:function(params){
                    var self = this;
                    var currentNode = self.findNode(params.data);
                    if(currentNode.current) return;
                    currentNode.itemStyle = {
                        borderColor:'#e78888'
                    };
                    currentNode.label = {
                        color:'#606266'
                    };
                    self.render(false);
                },
                onGraphNodeMouseout:function(params){
                    var self = this;
                    var currentNode = self.findNode(params.data);
                    if(currentNode.current) return;
                    currentNode.itemStyle = null;
                    currentNode.label = null;
                    self.render(false);
                },
                onGraphNodeContextmenu:function(params){
                    var self = this;
                    var x = params.event.event.clientX;
                    var y = params.event.event.clientY;
                    self.contextmenu.visible = true;
                    self.contextmenu.x = x;
                    self.contextmenu.y = y;
                    var buttons = self.graph.typeAction[params.data.type];
                    self.contextmenu.move = buttons.move;
                    self.contextmenu.edit = buttons.edit;
                    self.contextmenu.remove = buttons.remove;
                },
                onContextmenuClose:function(){
                    var self = this;
                    self.contextmenu.visible = false;
                    self.contextmenu.x = 0;
                    self.contextmenu.y = 0;
                    self.graph.current.current = false;
                    self.graph.current.itemStyle = '';
                    self.graph.current.label = '';
                    self.graph.current = '';
                    self.render();
                },
                handleEditGraphNode:function(){
                    var self = this;
                    if(self.graph.current.type === 'PHYSICAL_SCREEN'){
                        self.handleEditPhysicalScreen(self.graph.current);
                    }else if(self.graph.current.type === 'VIDEO_ENCODE_CHANNEL'){
                        self.handleEditVideoEncodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'VIDEO_DECODE_CHANNEL'){
                        self.handleEditVideoDecodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'AUDIO_ENCODE_CHANNEL'){
                        self.handleEditAudioEncodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'AUDIO_DECODE_CHANNEL'){
                        self.handleEditAudioDecodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'AUDIO_OUTPUT'){
                        self.handleEditAudioOutput(self.graph.current);
                    }
                    self.onContextmenuClose();
                },
                handleMoveGraphNode:function(){
                    var self = this;
                    if(self.graph.current.type === 'VIDEO_ENCODE_CHANNEL'){
                        self.handleMoveVideoEncodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'VIDEO_DECODE_CHANNEL'){
                        self.handleMoveVideoDecodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'AUDIO_DECODE_CHANNEL'){
                        self.handleMoveAudioDecodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'AUDIO_OUTPUT'){
                        self.handleMoveAudioOutput(self.graph.current);
                    }
                    self.onContextmenuClose();
                },
                handleRemoveGraphNode:function(){
                    var self = this;
                    if(self.graph.current.type === 'PHYSICAL_SCREEN'){
                        self.handleRemovePhysicalScreen(self.graph.current);
                    }else if(self.graph.current.type === 'VIDEO_ENCODE_CHANNEL'){
                        self.handleRemoveVideoEncodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'VIDEO_DECODE_CHANNEL'){
                        self.handleRemoveVideoDecodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'AUDIO_ENCODE_CHANNEL'){
                        self.handleRemoveAudioEncodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'AUDIO_DECODE_CHANNEL'){
                        self.handleRemoveAudioDecodeChannel(self.graph.current);
                    }else if(self.graph.current.type === 'AUDIO_OUTPUT'){
                        self.handleRemoveAudioOutput(self.graph.current);
                    }
                    self.onContextmenuClose();
                },
                loadVideoChannelParamTypes:function(){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/channel/bundle/channel/permission/query/param/types', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                data[i].target = '';
                                self.videoChannelParams.push(data[i]);
                            }
                        }
                    });
                },
                loadTerminalBundleChannels:function(type, fn){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/bundle/channel/load/all/by/type', {
                        terminalId:self.terminalId,
                        type:type
                    }, function(data){
                        if(typeof fn === 'function') fn(data);
                    });
                },
                loadTerminalChannels:function(type, fn){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/channel/load/by/type', {
                        terminalId:self.terminalId,
                        type:type
                    }, function(data){
                        if(typeof fn === 'function') fn(data);
                    });
                },
                loadPhysicalScreen:function(fn){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/physical/screen/find/by/terminal/id', {
                        terminalId:self.terminalId
                    }, function(data){
                        if(typeof fn === 'function') fn(data);
                    });
                },
                loadAudioOutput:function(fn){
                    var self = this;
                    ajax.post('/tetris/bvc/model/terminal/audio/output/load', {
                        terminalId:self.terminalId
                    }, function(data){
                        if(typeof fn==='function') fn(data);
                    });
                },
                handleAddPhysicalScreen:function(){
                    var self = this;
                    self.dialog.addPhysicalScreen.visible = true;
                },
                handleAddPhysicalScreenClose:function(){
                    var self = this;
                    self.dialog.addPhysicalScreen.visible = false;
                    self.dialog.addPhysicalScreen.loading = false;
                    self.dialog.addPhysicalScreen.name = '';
                },
                handleAddPhysicalScreenSubmit:function(){
                    var self = this;
                    self.dialog.addPhysicalScreen.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/physical/screen/add', {
                        terminalId:self.terminalId,
                        name:self.dialog.addPhysicalScreen.name
                    }, function(data, status, message){
                        self.dialog.addPhysicalScreen.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.graph.data.children.push(data);
                        self.handleAddPhysicalScreenClose();
                        self.render();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditPhysicalScreen:function(physicalScreen){
                    var self = this;
                    self.dialog.editPhysicalScreen.id = physicalScreen.id;
                    self.dialog.editPhysicalScreen.name = physicalScreen.name;
                    self.dialog.editPhysicalScreen.visible = true;
                },
                handleEditPhysicalScreenClose:function(){
                    var self = this;
                    self.dialog.editPhysicalScreen.id = '';
                    self.dialog.editPhysicalScreen.name = '';
                    self.dialog.editPhysicalScreen.visible = false;
                    self.dialog.editPhysicalScreen.loading = false;
                },
                handleEditPhysicalScreenSubmit:function(){
                    var self = this;
                    if(!self.dialog.editPhysicalScreen.name){
                        self.$message({
                            type:'error',
                            message:'请输入名称'
                        });
                        return;
                    }
                    self.dialog.editPhysicalScreen.loading = true;
                    var targetId = self.dialog.editPhysicalScreen.id.replace('PHYSICAL_SCREEN_', '');
                    ajax.post('/tetris/bvc/model/terminal/physical/screen/edit', {
                        id:targetId,
                        name:self.dialog.editPhysicalScreen.name
                    }, function(data, status, message){
                        self.dialog.editPhysicalScreen.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        for(var i=0; i<self.physicalScreenLayout.physicalScreens.length; i++){
                            if(self.physicalScreenLayout.physicalScreens[i].id == targetId){
                                self.physicalScreenLayout.physicalScreens[i].name = self.dialog.editPhysicalScreen.name;
                                break;
                            }
                        }
                        var node = self.findNode({id:self.dialog.editPhysicalScreen.id});
                        node.name = self.dialog.editPhysicalScreen.name;
                        self.render();
                        self.handleEditPhysicalScreenClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemovePhysicalScreen:function(physicalScreen){
                    var self = this;
                    self.dialog.removePhysicalScreen.id = physicalScreen.id;
                    self.dialog.removePhysicalScreen.visible = true;
                },
                handleRemovePhysicalScreenClose:function(){
                    var self = this;
                    self.dialog.removePhysicalScreen.visible = false;
                    self.dialog.removePhysicalScreen.loading = false;
                    self.dialog.removePhysicalScreen.id = '';
                    self.dialog.removePhysicalScreen.removeChildren = false;
                },
                handleRemovePhysicalScreenSubmit:function(){
                    var self = this;
                    var targetId = self.dialog.removePhysicalScreen.id.replace('PHYSICAL_SCREEN_', '');
                    self.dialog.removePhysicalScreen.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/physical/screen/remove', {
                        id:targetId,
                        removeChildren:self.dialog.removePhysicalScreen.removeChildren
                    }, function(data, status, message){
                        self.dialog.removePhysicalScreen.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        for(var i=0; i<self.physicalScreenLayout.physicalScreens.length; i++){
                            if(self.physicalScreenLayout.physicalScreens[i].id == targetId){
                                self.physicalScreenLayout.physicalScreens.splice(i, 1);
                                break;
                            }
                        }
                        var screenNode = self.removeNode({id:self.dialog.removePhysicalScreen.id})
                        if(!self.dialog.removePhysicalScreen.removeChildren){
                            if(screenNode && screenNode.children && screenNode.children.length>0){
                                for(var i=0; i<screenNode.children.length; i++){
                                    self.graph.data.children.push(screenNode.children[i]);
                                }
                            }
                        }
                        self.render();
                        self.handleRemovePhysicalScreenClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleAddVideoEncodeChannel:function(){
                    var self = this;
                    self.dialog.addVideoEncodeChannel.visible = true;
                },
                handleAddVideoEncodeChannelClose:function(){
                    var self = this;
                    self.dialog.addVideoEncodeChannel.visible = false;
                    self.dialog.addVideoEncodeChannel.loading = false;
                    self.dialog.addVideoEncodeChannel.name = '';
                    self.dialog.addVideoEncodeChannel.selectAudioChannel = '';
                    self.clearChannelParamsTarget();
                },
                handleAddVideoEncodeChannelSelectChannel:function(title, type, params){
                    var self = this;
                    if(type === 'AUDIO_ENCODE'){
                        self.loadTerminalChannels(type, function(channels){
                            self.$refs.addVideoEncodeChannelSelectChannel.show(title, self.dialog.terminalChannelColumns, channels, [type, params]);
                        });
                    }else{
                        self.loadTerminalBundleChannels(type, function(channels){
                            self.$refs.addVideoEncodeChannelSelectChannel.show(title, self.dialog.terminalBundleChannelColumns, channels, [type, params]);
                        });
                    }
                },
                onVideoEncodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    var type = buff[0];
                    if(type === 'AUDIO_ENCODE'){
                        self.dialog.addVideoEncodeChannel.selectAudioChannel = channel;
                    }else if(type === 'VIDEO_ENCODE'){
                        var params = buff[1];
                        params.target = channel;
                    }
                    done();
                },
                handleAddVideoEncodeChannelRemoveChannel:function(params){
                    var self = this;
                    if(params){
                        params.target = '';
                    }else{
                        self.dialog.addVideoEncodeChannel.selectAudioChannel = '';
                    }
                },
                handleAddVideoEncodeChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.addVideoEncodeChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    var paramsPermissions = [];
                    for(var i=0; i<self.videoChannelParams.length; i++){
                        if(self.videoChannelParams[i].target){
                            paramsPermissions.push({
                                channelParams:self.videoChannelParams[i].key,
                                terminalBundleId:self.videoChannelParams[i].target.terminalBundleId,
                                terminalBundleChannelId:self.videoChannelParams[i].target.id
                            });
                        }
                    }
                    if(paramsPermissions.length <= 0){
                        self.$message({
                            type:'error',
                            message:'请绑定视频通道'
                        });
                        return;
                    }
                    self.dialog.addVideoEncodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/add/video/encode', {
                        terminalId:self.terminalId,
                        name:self.dialog.addVideoEncodeChannel.name,
                        terminalAudioEncodeChannelId:(self.dialog.addVideoEncodeChannel.selectAudioChannel?self.dialog.addVideoEncodeChannel.selectAudioChannel.id:null),
                        paramsPermissions: $.toJSON(paramsPermissions)
                    }, function(data, status, message){
                        self.dialog.addVideoEncodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(self.dialog.addVideoEncodeChannel.selectAudioChannel){
                            for(var i=0; i<self.graph.data.children.length; i++){
                                var freeNode = self.graph.data.children[i];
                                if(freeNode.type !== 'AUDIO_ENCODE_CHANNEL') continue;
                                if(freeNode.id.endWith(self.dialog.addVideoEncodeChannel.selectAudioChannel.id)){
                                    freeNode.children.push(data);
                                    break;
                                }
                            }
                        }else{
                            self.graph.data.children.push(data);
                        }
                        self.render();
                        self.handleAddVideoEncodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditVideoEncodeChannel:function(videoEncodeChannel){
                    var self = this;
                    self.dialog.editVideoEncodeChannel.id = videoEncodeChannel.id;
                    self.dialog.editVideoEncodeChannel.name = videoEncodeChannel.name;
                    self.setChannelParamsTarget(videoEncodeChannel.children);
                    self.dialog.editVideoEncodeChannel.visible = true;
                },
                handleEditVideoEncodeChannelClose:function(){
                    var self = this;
                    self.dialog.editVideoEncodeChannel.id = '';
                    self.dialog.editVideoEncodeChannel.name = '';
                    self.clearChannelParamsTarget();
                    self.dialog.editVideoEncodeChannel.visible = false;
                    self.dialog.editVideoEncodeChannel.loading = false;
                },
                handleEditVideoEncodeChannelSelectChannel:function(videoChannelParam){
                    var self = this;
                    self.loadTerminalBundleChannels('VIDEO_ENCODE', function(channels){
                        self.$refs.editVideoEncodeChannelSelectChannel.show('选择视频编码通道', self.dialog.terminalBundleChannelColumns, channels, [videoChannelParam]);
                    });
                },
                onEditVideoEncodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    var videoChannelParam = buff[0];
                    videoChannelParam.target = channel;
                    done();
                },
                handleEditVideoEncodeChannelRemoveChannel:function(videoChannelParam){
                    var self = this;
                    videoChannelParam.target = '';
                },
                handleEditVideoEncodeChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.editVideoEncodeChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    var paramsPermissions = [];
                    for(var i=0; i<self.videoChannelParams.length; i++){
                        if(self.videoChannelParams[i].target){
                            paramsPermissions.push({
                                channelParams:self.videoChannelParams[i].key,
                                terminalBundleId:self.videoChannelParams[i].target.terminalBundleId,
                                terminalBundleChannelId:self.videoChannelParams[i].target.id
                            });
                        }
                    }
                    if(paramsPermissions.length <= 0){
                        self.$message({
                            type:'error',
                            message:'请绑定视频通道'
                        });
                        return;
                    }
                    self.dialog.editVideoEncodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/edit/video/encode', {
                        id:self.dialog.editVideoEncodeChannel.id.replace('VIDEO_ENCODE_CHANNEL_', ''),
                        name:self.dialog.editVideoEncodeChannel.name,
                        paramsPermissions: $.toJSON(paramsPermissions)
                    }, function(data, status, message){
                        self.dialog.editVideoEncodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.findNode({id:self.dialog.editVideoEncodeChannel.id});
                        node.children.splice(0, node.children.length);
                        $.extend(node, data, true);
                        self.render();
                        self.handleEditVideoEncodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleMoveVideoEncodeChannel:function(videoEncodeChannel){
                    var self = this;
                    self.dialog.moveVideoEncodeChannel.id = videoEncodeChannel.id;
                    var parentNode = self.findParent(videoEncodeChannel);
                    if(parentNode.type === 'AUDIO_ENCODE_CHANNEL'){
                        self.dialog.moveVideoEncodeChannel.selectAudioChannel = {
                            id:parentNode.id.replace('AUDIO_ENCODE_CHANNEL_', ''),
                            name:parentNode.name
                        };
                    }
                    self.dialog.moveVideoEncodeChannel.visible = true;
                },
                handleMoveVideoEncodeChannelClose:function(){
                    var self = this;
                    self.dialog.moveVideoEncodeChannel.id = '';
                    self.dialog.moveVideoEncodeChannel.selectAudioChannel = '';
                    self.dialog.moveVideoEncodeChannel.visible = false;
                    self.dialog.moveVideoEncodeChannel.loading = false;
                },
                handleMoveVideoEncodeChannelSelectChannel:function(){
                    var self = this;
                    self.loadTerminalChannels('AUDIO_ENCODE', function(channels){
                        self.$refs.moveVideoEncodeChannelSelectChannel.show('选择音频通道', self.dialog.terminalChannelColumns, channels);
                    });
                },
                onMoveVideoEncodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    self.dialog.moveVideoEncodeChannel.selectAudioChannel = channel;
                    done();
                },
                handleMoveVideoEncodeChannelRemoveChannel:function(){
                    var self = this;
                    self.dialog.moveVideoEncodeChannel.selectAudioChannel = '';
                },
                handleMoveVideoEncodeChannelSubmit:function(){
                    var self = this;
                    self.dialog.moveVideoEncodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/move/video/encode', {
                        id:self.dialog.moveVideoEncodeChannel.id.replace('VIDEO_ENCODE_CHANNEL_', ''),
                        terminalAudioEncodeChannelId:self.dialog.moveVideoEncodeChannel.selectAudioChannel==null?null:self.dialog.moveVideoEncodeChannel.selectAudioChannel.id
                    }, function(data, status, message){
                        self.dialog.moveVideoEncodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.removeNode({id:self.dialog.moveVideoEncodeChannel.id});
                        if(self.dialog.moveVideoEncodeChannel.selectAudioChannel){
                            var parent = self.findNode({id:'AUDIO_ENCODE_CHANNEL_' + self.dialog.moveVideoEncodeChannel.selectAudioChannel.id});
                            parent.children.push(node);
                        }else{
                            self.graph.data.children.push(node);
                        }
                        self.render();
                        self.handleMoveVideoEncodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveVideoEncodeChannel:function(videoEncodeChannel){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否删除当前视频编码通道？'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/channel/remove/video/encode', {
                                    id:videoEncodeChannel.id.replace('VIDEO_ENCODE_CHANNEL_', '')
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.removeNode(videoEncodeChannel);
                                    self.render();
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleAddVideoDecodeChannel:function(){
                    var self = this;
                    self.dialog.addVideoDecodeChannel.visible = true;
                },
                handleAddVideoDecodeChannelClose:function(){
                    var self = this;
                    self.dialog.addVideoDecodeChannel.visible = false;
                    self.dialog.addVideoDecodeChannel.loading = false;
                    self.dialog.addVideoDecodeChannel.name = '';
                    self.dialog.addVideoDecodeChannel.selectPhysicalScreen = '';
                    self.clearChannelParamsTarget();
                },
                handleAddVideoDecodeChannelSelectChannel:function(title, type, params){
                    var self = this;
                    if(type === 'PHYSICAL_SCREEN'){
                        self.loadPhysicalScreen(function(screens){
                            self.$refs.addVideoDecodeChannelSelectChannel.show(title, self.dialog.physicalScreenColumns, screens, [type, params]);
                        });
                    }else{
                        self.loadTerminalBundleChannels(type, function(channels){
                            self.$refs.addVideoDecodeChannelSelectChannel.show(title, self.dialog.terminalBundleChannelColumns, channels, [type, params]);
                        });
                    }
                },
                onAddVideoDecodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    var type = buff[0];
                    if(type === 'PHYSICAL_SCREEN'){
                        self.dialog.addVideoDecodeChannel.selectPhysicalScreen = channel;
                    }else if(type === 'VIDEO_DECODE'){
                        var params = buff[1];
                        params.target = channel;
                    }
                    done();
                },
                handleAddVideoDecodeChannelRemoveChannel:function(params){
                    var self = this;
                    if(params){
                        params.target = '';
                    }else{
                        self.dialog.addVideoDecodeChannel.selectPhysicalScreen = '';
                    }
                },
                handleAddVideoDecodeChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.addVideoDecodeChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    var paramsPermissions = [];
                    for(var i=0; i<self.videoChannelParams.length; i++){
                        if(self.videoChannelParams[i].target){
                            paramsPermissions.push({
                                channelParams:self.videoChannelParams[i].key,
                                terminalBundleId:self.videoChannelParams[i].target.terminalBundleId,
                                terminalBundleChannelId:self.videoChannelParams[i].target.id
                            });
                        }
                    }
                    if(paramsPermissions.length <= 0){
                        self.$message({
                            type:'error',
                            message:'请绑定视频通道'
                        });
                        return;
                    }
                    self.dialog.addVideoDecodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/add/video/decode', {
                        terminalId:self.terminalId,
                        name:self.dialog.addVideoDecodeChannel.name,
                        terminalPhysicalScreenId:(self.dialog.addVideoDecodeChannel.selectPhysicalScreen?self.dialog.addVideoDecodeChannel.selectPhysicalScreen.id:null),
                        paramsPermissions: $.toJSON(paramsPermissions)
                    }, function(data, status, message){
                        self.dialog.addVideoDecodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(self.dialog.addVideoDecodeChannel.selectPhysicalScreen){
                            for(var i=0; i<self.graph.data.children.length; i++){
                                var freeNode = self.graph.data.children[i];
                                if(freeNode.type !== 'PHYSICAL_SCREEN') continue;
                                if(freeNode.id.endWith(self.dialog.addVideoDecodeChannel.selectPhysicalScreen.id)){
                                    freeNode.children.push(data);
                                    break;
                                }
                            }
                        }else{
                            self.graph.data.children.push(data);
                        }
                        self.render();
                        self.handleAddVideoDecodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditVideoDecodeChannel:function(videoDecodeChannel){
                    var self = this;
                    self.dialog.editVideoDecodeChannel.id = videoDecodeChannel.id;
                    self.dialog.editVideoDecodeChannel.name = videoDecodeChannel.name;
                    self.setChannelParamsTarget(videoDecodeChannel.children);
                    self.dialog.editVideoDecodeChannel.visible = true;
                },
                handleEditVideoDecodeChannelClose:function(){
                    var self = this;
                    self.dialog.editVideoDecodeChannel.id = '';
                    self.dialog.editVideoDecodeChannel.name = '';
                    self.clearChannelParamsTarget();
                    self.dialog.editVideoDecodeChannel.visible = false;
                    self.dialog.editVideoDecodeChannel.loading = false;
                },
                handleEditVideoDecodeChannelSelectChannel:function(videoChannelParam){
                    var self = this;
                    self.loadTerminalBundleChannels('VIDEO_DECODE', function(channels){
                        self.$refs.editVideoDecodeChannelSelectChannel.show('选择视频解码通道', self.dialog.terminalBundleChannelColumns, channels, [videoChannelParam]);
                    });
                },
                onEditVideoDecodeChannellSelectedChannel:function(channel, buff, done){
                    var self = this;
                    var videoChannelParam = buff[0];
                    videoChannelParam.target = channel;
                    done();
                },
                handleEditVideoDecodeChannelRemoveChannel:function(videoChannelParam){
                    videoChannelParam.target = '';
                },
                handleEditVideoDecodeChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.editVideoDecodeChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    var paramsPermissions = [];
                    for(var i=0; i<self.videoChannelParams.length; i++){
                        if(self.videoChannelParams[i].target){
                            paramsPermissions.push({
                                channelParams:self.videoChannelParams[i].key,
                                terminalBundleId:self.videoChannelParams[i].target.terminalBundleId,
                                terminalBundleChannelId:self.videoChannelParams[i].target.id
                            });
                        }
                    }
                    if(paramsPermissions.length <= 0){
                        self.$message({
                            type:'error',
                            message:'请绑定视频通道'
                        });
                        return;
                    }
                    self.dialog.editVideoDecodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/edit/video/decode', {
                        id:self.dialog.editVideoDecodeChannel.id.replace('VIDEO_DECODE_CHANNEL_', ''),
                        name:self.dialog.editVideoDecodeChannel.name,
                        paramsPermissions: $.toJSON(paramsPermissions)
                    }, function(data, status, messasge){
                        self.dialog.editVideoDecodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.findNode({id:self.dialog.editVideoDecodeChannel.id});
                        node.children.splice(0, node.children.length);
                        $.extend(node, data, true);
                        self.render();
                        self.handleEditVideoDecodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleMoveVideoDecodeChannel:function(videoDecodeChannel){
                    var self = this;
                    self.dialog.moveVideoDecodeChannel.id = videoDecodeChannel.id;
                    var parent = self.findParent(videoDecodeChannel);
                    if(parent.type === 'PHYSICAL_SCREEN'){
                        self.dialog.moveVideoDecodeChannel.selectPhysicalScreen = {
                            id:parent.id.replace('PHYSICAL_SCREEN_', ''),
                            name:parent.name
                        };
                    }
                    self.dialog.moveVideoDecodeChannel.visible = true;
                },
                handleMoveVideoDecodeChannelClose:function(){
                    var self = this;
                    self.dialog.moveVideoDecodeChannel.id = '';
                    self.dialog.moveVideoDecodeChannel.selectPhysicalScreen = '';
                    self.dialog.moveVideoDecodeChannel.visible = false;
                    self.dialog.moveVideoDecodeChannel.loading = false;
                },
                handleMoveVideoDecodeChannelSelectChannel:function(){
                    var self = this;
                    self.loadPhysicalScreen(function(physicalScreens){
                        self.$refs.moveVideoDecodeChannelSelectChannel.show('选择物理屏幕', self.dialog.physicalScreenColumns, physicalScreens);
                    });
                },
                onMoveVideoDecodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    self.dialog.moveVideoDecodeChannel.selectPhysicalScreen = channel;
                    done();
                },
                handleMoveVideoDecodeChannelRemoveChannel:function(){
                    var self = this;
                    self.dialog.moveVideoDecodeChannel.selectPhysicalScreen = '';
                },
                handleMoveVideoDecodeChannelSubmit:function(){
                    var self = this;
                    self.dialog.moveVideoDecodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/move/video/decode', {
                        id:self.dialog.moveVideoDecodeChannel.id.replace('VIDEO_DECODE_CHANNEL_', ''),
                        terminalPhysicalScreenId:self.dialog.moveVideoDecodeChannel.selectPhysicalScreen==null?null:self.dialog.moveVideoDecodeChannel.selectPhysicalScreen.id
                    }, function(data, status, message){
                        self.dialog.moveVideoDecodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.removeNode({id:self.dialog.moveVideoDecodeChannel.id});
                        if(!self.dialog.moveVideoDecodeChannel.selectPhysicalScreen){
                            self.graph.data.children.push(node);
                        }else{
                            var parent = self.findNode({id:'PHYSICAL_SCREEN_' + self.dialog.moveVideoDecodeChannel.selectPhysicalScreen.id});
                            parent.children.push(node);
                        }
                        self.render();
                        self.handleMoveVideoDecodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveVideoDecodeChannel:function(videoDecodeChannel){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否删除当前视频解码通道？'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/channel/remove/video/decode', {
                                    id:videoDecodeChannel.id.replace('VIDEO_DECODE_CHANNEL_', '')
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.removeNode(videoDecodeChannel);
                                    self.render();
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleAddAudioEncodeChannel:function(){
                    var self = this;
                    self.dialog.addAudioEncodeChannel.visible = true;
                },
                handleAddAudioEncodeChannelClose:function(){
                    var self = this;
                    self.dialog.addAudioEncodeChannel.visible = false;
                    self.dialog.addAudioEncodeChannel.loading = false;
                    self.dialog.addAudioEncodeChannel.name = '';
                    self.dialog.addAudioEncodeChannel.selectTerminalBundleAudioChannel = '';
                },
                handleAddAudioEncodeChannelSelectChannel:function(){
                    var self = this;
                    self.loadTerminalBundleChannels('AUDIO_ENCODE', function(channels){
                        self.$refs.addAudioEncodeChannelSelectChannel.show('选择音频编码通道', self.dialog.terminalBundleChannelColumns, channels);
                    });
                },
                onAddAudioEncodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    self.dialog.addAudioEncodeChannel.selectTerminalBundleAudioChannel = channel;
                    done();
                },
                handleAddAudioEncodeChannelRemoveChannel:function(){
                    var self = this;
                    self.dialog.addAudioEncodeChannel.selectTerminalBundleAudioChannel = '';
                },
                handleAddAudioEncodeChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.addAudioEncodeChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    if(!self.dialog.addAudioEncodeChannel.selectTerminalBundleAudioChannel){
                        self.$message({
                            type:'error',
                            message:'请关联音频通道！'
                        });
                        return;
                    }
                    self.dialog.addAudioEncodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/add/audio/encode', {
                        terminalId:self.terminalId,
                        name:self.dialog.addAudioEncodeChannel.name,
                        terminalBundleChannelId:self.dialog.addAudioEncodeChannel.selectTerminalBundleAudioChannel.id
                    }, function(data, status, message){
                        self.dialog.addAudioEncodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        self.graph.data.children.push(data);
                        self.render();
                        self.handleAddAudioEncodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditAudioEncodeChannel:function(audioEncodeChannel){
                    var self = this;
                    var bundleChannelNode = null;
                    for(var i=0; i<audioEncodeChannel.children.length; i++){
                        if(audioEncodeChannel.children[i].type === 'CHANNEL_PERMISSION'){
                            bundleChannelNode = audioEncodeChannel.children[i];
                            break;
                        }
                    }
                    self.dialog.editAudioEncodeChannel.id = audioEncodeChannel.id;
                    self.dialog.editAudioEncodeChannel.name = audioEncodeChannel.name;
                    self.dialog.editAudioEncodeChannel.selectTerminalBundleAudioChannel = {
                        id:bundleChannelNode.params.terminalBundleChannelId,
                        channelId:bundleChannelNode.params.channelId,
                        type:bundleChannelNode.params.type,
                        typeName:bundleChannelNode.params.typeName,
                        terminalBundleId:bundleChannelNode.params.terminalBundleId,
                        terminalBundleName:bundleChannelNode.params.bundleName,
                        bundleType:bundleChannelNode.params.deviceMode,
                        terminalBundleType:bundleChannelNode.params.bundleType,
                        terminalBundleTypeName:bundleChannelNode.params.bundleTypeName,
                        terminalId:bundleChannelNode.params.terminalId
                    };
                    self.dialog.editAudioEncodeChannel.visible = true;
                },
                handleEditAudioEncodeChannelClose:function(){
                    var self = this;
                    self.dialog.editAudioEncodeChannel.id = '';
                    self.dialog.editAudioEncodeChannel.name = '';
                    self.dialog.editAudioEncodeChannel.selectTerminalBundleAudioChannel = '';
                    self.dialog.editAudioEncodeChannel.visible = false;
                    self.dialog.editAudioEncodeChannel.loading = false;
                },
                handleEditAudioEncodeChannelSelectChannel:function(){
                    var self = this;
                    self.loadTerminalBundleChannels('AUDIO_ENCODE', function(channels){
                        self.$refs.editAudioEncodeChannelSelectChannel.show('选择音频编码通道', self.dialog.terminalBundleChannelColumns, channels);
                    });
                },
                onEditAudioEncodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    self.dialog.editAudioEncodeChannel.selectTerminalBundleAudioChannel = channel;
                    done();
                },
                handleEditAudioEncodeChannelRemoveChannel:function(){
                    var self = this;
                    self.dialog.editAudioEncodeChannel.selectTerminalBundleAudioChannel = '';
                },
                handleEditAudioEncodeChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.editAudioEncodeChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    if(!self.dialog.editAudioEncodeChannel.selectTerminalBundleAudioChannel){
                        self.$message({
                            type:'error',
                            message:'请关联音频通道！'
                        });
                        return;
                    }
                    self.dialog.editAudioEncodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/edit/audio/encode', {
                        id:self.dialog.editAudioEncodeChannel.id.replace('AUDIO_ENCODE_CHANNEL_', ''),
                        name:self.dialog.editAudioEncodeChannel.name,
                        terminalBundleChannelId:self.dialog.editAudioEncodeChannel.selectTerminalBundleAudioChannel?self.dialog.editAudioEncodeChannel.selectTerminalBundleAudioChannel.id:null
                    }, function(data, status, message){
                        self.dialog.editAudioEncodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.findNode({id:self.dialog.editAudioEncodeChannel.id});
                        var videoEncodeChannel = [];
                        for(var i=0; i<node.children.length; i++){
                            if(node.children[i].type === 'VIDEO_ENCODE_CHANNEL'){
                                videoEncodeChannel.push(node.children[i]);
                            }
                        }
                        node.children.splice(0, node.children.length);
                        $.extend(node, data, true);
                        for(var i=0; i<videoEncodeChannel.length; i++){
                            node.children.push(videoEncodeChannel[i]);
                        }
                        self.render();
                        self.handleEditAudioEncodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveAudioEncodeChannel:function(audioEncodeChannel){
                    var self = this;
                    self.dialog.removeAudioEncodeChannel.id = audioEncodeChannel.id;
                    self.dialog.removeAudioEncodeChannel.visible = true;
                },
                handleRemoveAudioEncodeChannelClose:function(){
                    var self = this;
                    self.dialog.removeAudioEncodeChannel.id = '';
                    self.dialog.removeAudioEncodeChannel.removeChildren = false;
                    self.dialog.removeAudioEncodeChannel.visible = false;
                    self.dialog.removeAudioEncodeChannel.loading = false;
                },
                handleRemoveAudioEncodeChannelSubmit:function(){
                    var self = this;
                    self.dialog.removeAudioEncodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/remove/audio/encode', {
                        id:self.dialog.removeAudioEncodeChannel.id.replace('AUDIO_ENCODE_CHANNEL_', ''),
                        removeChildren:self.dialog.removeAudioEncodeChannel.removeChildren
                    }, function(data, status, message){
                        self.dialog.removeAudioEncodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.removeNode({id:self.dialog.removeAudioEncodeChannel.id});
                        if(!self.dialog.removeAudioEncodeChannel.removeChildren){
                            for(var i=0; i<node.children.length; i++){
                                if(node.children[i].type !== 'CHANNEL_PERMISSION'){
                                    self.graph.data.children.push(node.children[i]);
                                }
                            }
                        }
                        self.render();
                        self.handleRemoveAudioEncodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleAddAudioDecodeChannel:function(){
                    var self = this;
                    self.dialog.addAudioDecodeChannel.visible = true;
                },
                handleAddAudioDecodeChannelClose:function(){
                    var self = this;
                    self.dialog.addAudioDecodeChannel.visible = false;
                    self.dialog.addAudioDecodeChannel.loading = false;
                    self.dialog.addAudioDecodeChannel.name = '';
                    self.dialog.addAudioDecodeChannel.selectTerminalAudioOutput = '';
                    self.dialog.addAudioDecodeChannel.selectTerminalBundleAudioChannel = '';
                },
                handleAddAudioDecodeChannelSelectChannel:function(title, type){
                    var self = this;
                    if(type === 'AUDIO_DECODE'){
                        self.loadTerminalBundleChannels('AUDIO_DECODE', function(channels){
                            self.$refs.addAudioDecodeChannelSelectChannel.show(title, self.dialog.terminalBundleChannelColumns, channels, type);
                        });
                    }else if(type === 'AUDIO_OUTPUT'){
                        self.loadAudioOutput(function(audioOutputs){
                            self.$refs.addAudioDecodeChannelSelectChannel.show(title, self.dialog.audioOutputColumns, audioOutputs, type);
                        });
                    }
                },
                onAddAudioDecodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    if(buff === 'AUDIO_DECODE'){
                        self.dialog.addAudioDecodeChannel.selectTerminalBundleAudioChannel = channel;
                    }else if(buff === 'AUDIO_OUTPUT'){
                        self.dialog.addAudioDecodeChannel.selectTerminalAudioOutput = channel;
                    }
                    done();
                },
                handleAddAudioDecodeChannelRemoveChannel:function(type){
                    var self = this;
                    if(type === 'AUDIO_DECODE'){
                        self.dialog.addAudioDecodeChannel.selectTerminalBundleAudioChannel = '';
                    }else if(type === 'AUDIO_OUTPUT'){
                        self.dialog.addAudioDecodeChannel.selectTerminalAudioOutput = '';
                    }
                },
                handleAddAudioDecodeChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.addAudioDecodeChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    if(!self.dialog.addAudioDecodeChannel.selectTerminalBundleAudioChannel){
                        self.$message({
                            type:'error',
                            message:'请关联音频通道！'
                        });
                        return;
                    }
                    self.dialog.addAudioDecodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/add/audio/decode', {
                        terminalId:self.terminalId,
                        name:self.dialog.addAudioDecodeChannel.name,
                        terminalAudioOutputId:self.dialog.addAudioDecodeChannel.selectTerminalAudioOutput?self.dialog.addAudioDecodeChannel.selectTerminalAudioOutput.id:null,
                        terminalBundleChannelId:self.dialog.addAudioDecodeChannel.selectTerminalBundleAudioChannel.id
                    }, function(data, status, message){
                        self.dialog.addAudioDecodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(!self.dialog.addAudioDecodeChannel.selectTerminalAudioOutput){
                            self.graph.data.children.push(data);
                        }else{
                            for(var i=0; i<self.graph.data.children.length; i++){
                                var finded = false;
                                if(self.graph.data.children[i].type === 'AUDIO_OUTPUT'){
                                    if(self.graph.data.children[i].id.endWith(self.dialog.addAudioDecodeChannel.selectTerminalAudioOutput.id)){
                                        self.graph.data.children[i].children.push(data);
                                        finded = true;
                                    }
                                }else if(self.graph.data.children[i].type === 'PHYSICAL_SCREEN'){
                                    var physicalScreenNode = self.graph.data.children[i];
                                    for(var j=0; j<physicalScreenNode.children.length; j++){
                                        if(physicalScreenNode.children[j].type === 'AUDIO_OUTPUT'){
                                            if(physicalScreenNode.children[j].id.endWith(self.dialog.addAudioDecodeChannel.selectTerminalAudioOutput.id)){
                                                physicalScreenNode.children[j].children.push(data);
                                                finded = true;
                                            }
                                        }
                                    }
                                }
                                if(finded) break;
                            }
                        }
                        self.render();
                        self.handleAddAudioDecodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditAudioDecodeChannel:function(audioDecode){
                    var self  = this;
                    self.dialog.editAudioDecodeChannel.id = audioDecode.id;
                    self.dialog.editAudioDecodeChannel.name = audioDecode.name;
                    var bundleChannelNode = audioDecode.children[0];
                    self.dialog.editAudioDecodeChannel.selectTerminalBundleAudioChannel = {
                        id:bundleChannelNode.params.terminalBundleChannelId,
                        channelId:bundleChannelNode.params.channelId,
                        type:bundleChannelNode.params.type,
                        typeName:bundleChannelNode.params.typeName,
                        terminalBundleId:bundleChannelNode.params.terminalBundleId,
                        terminalBundleName:bundleChannelNode.params.bundleName,
                        bundleType:bundleChannelNode.params.deviceMode,
                        terminalBundleType:bundleChannelNode.params.bundleType,
                        terminalBundleTypeName:bundleChannelNode.params.bundleTypeName,
                        terminalId:bundleChannelNode.params.terminalId
                    };
                    self.dialog.editAudioDecodeChannel.visible = true;
                },
                handleEditAudioDecodeChannelClose:function(){
                    var self = this;
                    self.dialog.editAudioDecodeChannel.id = '';
                    self.dialog.editAudioDecodeChannel.name = '';
                    self.dialog.editAudioDecodeChannel.selectTerminalBundleAudioChannel = '';
                    self.dialog.editAudioDecodeChannel.visible = false;
                    self.dialog.editAudioDecodeChannel.loading = false;
                },
                handleEditAudioDecodeChannelSelectChannel:function(){
                    var self = this;
                    self.loadTerminalBundleChannels('AUDIO_DECODE', function(channels){
                        self.$refs.editAudioDecodeChannelSelectChannel.show('选择音频通道', self.dialog.terminalBundleChannelColumns, channels);
                    });
                },
                onEditAudioDecodeChannelSelectedChannel:function(channel, buff, done){
                    var self = this;
                    self.dialog.editAudioDecodeChannel.selectTerminalBundleAudioChannel = channel;
                    done();
                },
                handleEditAudioDecodeChannelRemoveChannel:function(){
                    var self = this;
                    self.dialog.editAudioDecodeChannel.selectTerminalBundleAudioChannel = '';
                },
                handleEditAudioDecodeChannelSubmit:function(){
                    var self = this;
                    if(!self.dialog.editAudioDecodeChannel.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    if(!self.dialog.editAudioDecodeChannel.selectTerminalBundleAudioChannel){
                        self.$message({
                            type:'error',
                            message:'请关联音频通道！'
                        });
                        return;
                    }
                    self.dialog.editAudioDecodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/edit/audio/decode', {
                        id:self.dialog.editAudioDecodeChannel.id.replace('AUDIO_DECODE_CHANNEL_', ''),
                        name:self.dialog.editAudioDecodeChannel.name,
                        terminalBundleChannelId:self.dialog.editAudioDecodeChannel.selectTerminalBundleAudioChannel.id
                    }, function(data, status, message){
                        self.dialog.editAudioDecodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.findNode({id:self.dialog.editAudioDecodeChannel.id});
                        node.children.splice(0, node.children.length);
                        $.extend(node, data, true);
                        self.render();
                        self.handleEditAudioDecodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleMoveAudioDecodeChannel:function(audioDecode){
                    var self  = this;
                    self.dialog.moveAudioDecodeChannel.id = audioDecode.id;
                    var parent = self.findParent(audioDecode);
                    if(parent.type === 'AUDIO_OUTPUT'){
                        self.dialog.moveAudioDecodeChannel.selectTerminalAudioOutput = {
                            id:parent.id.replace('AUDIO_OUTPUT_', ''),
                            name:parent.name
                        };
                    }
                    self.dialog.moveAudioDecodeChannel.visible = true;
                },
                handleMoveAudioDecodeChannelClose:function(){
                    var self = this;
                    self.dialog.moveAudioDecodeChannel.id = '';
                    self.dialog.moveAudioDecodeChannel.selectTerminalAudioOutput = '';
                    self.dialog.moveAudioDecodeChannel.visible = false;
                    self.dialog.moveAudioDecodeChannel.loading = false;
                },
                handleMoveAudioDecodeChannelSelectChannel:function(){
                    var self = this;
                    self.loadAudioOutput(function(audioOutputs){
                        self.$refs.moveAudioDecodeChannelSelectChannel.show('选择音频输出', self.dialog.audioOutputColumns, audioOutputs);
                    });
                },
                onMoveAudioDecodeChannelSelectedChannel:function(audioOutput, buff, done){
                    var self = this;
                    self.dialog.moveAudioDecodeChannel.selectTerminalAudioOutput = audioOutput;
                    done();
                },
                handleMoveAudioDecodeChannelRemoveChannel:function(){
                    var self = this;
                    self.dialog.moveAudioDecodeChannel.selectTerminalAudioOutput = '';
                },
                handleMoveAudioDecodeChannelSubmit:function(){
                    var self = this;
                    self.dialog.moveAudioDecodeChannel.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/channel/move/audio/decode', {
                        id:self.dialog.moveAudioDecodeChannel.id.replace('AUDIO_DECODE_CHANNEL_', ''),
                        terminalAudioOutputId:self.dialog.moveAudioDecodeChannel.selectTerminalAudioOutput?self.dialog.moveAudioDecodeChannel.selectTerminalAudioOutput.id:null
                    }, function(data, status, message){
                        self.dialog.moveAudioDecodeChannel.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.removeNode({id:self.dialog.moveAudioDecodeChannel.id});
                        if(self.dialog.moveAudioDecodeChannel.selectTerminalAudioOutput){
                            var parent = self.findNode({id:'AUDIO_OUTPUT_' + self.dialog.moveAudioDecodeChannel.selectTerminalAudioOutput.id});
                            parent.children.push(node);
                        }else{
                            self.graph.data.children.push(node);
                        }
                        self.render();
                        self.handleMoveAudioDecodeChannelClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveAudioDecodeChannel:function(audioDecode){
                    var self  = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否删除当前音频解码通道？'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/terminal/channel/remove/audio/decode', {
                                    id:audioDecode.id.replace('AUDIO_DECODE_CHANNEL_', '')
                                }, function(data, status){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) return;
                                    self.removeNode(audioDecode);
                                    self.render();
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleAddAudioOutput:function(){
                    var self = this;
                    self.dialog.addAudioOutput.visible = true;
                },
                handleAddAudioOutputClose:function(){
                    var self = this;
                    var self = this;
                    self.dialog.addAudioOutput.visible = false;
                    self.dialog.addAudioOutput.loading = false;
                    self.dialog.addAudioOutput.name = '';
                    self.dialog.addAudioOutput.selectPhysicalScreen = '';
                },
                handleAddAudioOutputSelectChannel:function(){
                    var self = this;
                    self.loadPhysicalScreen(function(physicalScreens){
                        self.$refs.AddAudioOutputSelectChannel.show('选择物理屏幕', self.dialog.physicalScreenColumns, physicalScreens);
                    });
                },
                onAddAudioOutputSelectedChannel:function(physicalScreen, buff, done){
                    var self = this;
                    self.dialog.addAudioOutput.selectPhysicalScreen = physicalScreen;
                    done();
                },
                handleAddAudioOutputRemoveChannel:function(){
                    var self = this;
                    self.dialog.addAudioOutput.selectPhysicalScreen = '';
                },
                handleAddAudioOutputSubmit:function(){
                    var self = this;
                    if(!self.dialog.addAudioOutput.name){
                        self.$message({
                            type:'error',
                            message:'名称不能为空！'
                        });
                        return;
                    }
                    self.dialog.addAudioOutput.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/audio/output/add', {
                        terminalId:self.terminalId,
                        name:self.dialog.addAudioOutput.name,
                        terminalPhysicalScreenId:self.dialog.addAudioOutput.selectPhysicalScreen?self.dialog.addAudioOutput.selectPhysicalScreen.id:null,
                    }, function(data, status, message){
                        self.dialog.addAudioOutput.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(!self.dialog.addAudioOutput.selectPhysicalScreen){
                            self.graph.data.children.push(data);
                        }else{
                            for(var i=0; i<self.graph.data.children.length; i++){
                                if(self.graph.data.children[i].type === 'PHYSICAL_SCREEN'){
                                    var physicalScreen = self.graph.data.children[i];
                                    if(physicalScreen.id.endWith(self.dialog.addAudioOutput.selectPhysicalScreen.id)){
                                        for(var j=0; j<physicalScreen.children.length; j++){
                                            if(physicalScreen.children[j].type === 'AUDIO_OUTPUT'){
                                                self.graph.data.children.push(physicalScreen.children[j]);
                                                physicalScreen.children.splice(j, 1);
                                                break;
                                            }
                                        }
                                        physicalScreen.children.push(data);
                                        break;
                                    }
                                }
                            }
                        }
                        self.render();
                        self.handleAddAudioOutputClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleEditAudioOutput:function(audioOutput){
                    var self = this;
                    self.dialog.editAudioOutput.id = audioOutput.id;
                    self.dialog.editAudioOutput.name = audioOutput.name;
                    self.dialog.editAudioOutput.visible = true;
                },
                handleEditAudioOutputClose:function(){
                    var self = this;
                    self.dialog.editAudioOutput.id = '';
                    self.dialog.editAudioOutput.name = '';
                    self.dialog.editAudioOutput.visible = false;
                    self.dialog.editAudioOutput.loading = false;
                },
                handleEditAudioOutputSubmit:function(){
                    var self = this;
                    if(!self.dialog.editAudioOutput.name){
                        self.$message({
                            type:'error',
                            message:'请输入名称'
                        });
                        return;
                    }
                    self.dialog.editAudioOutput.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/audio/output/edit', {
                        id:self.dialog.editAudioOutput.id.replace('AUDIO_OUTPUT_', ''),
                        name:self.dialog.editAudioOutput.name
                    }, function(data, status, message){
                        self.dialog.editAudioOutput.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.findNode({id:self.dialog.editAudioOutput.id});
                        node.name = self.dialog.editAudioOutput.name;
                        self.render();
                        self.handleEditAudioOutputClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleMoveAudioOutput:function(audioOutput){
                    var self = this;
                    self.dialog.moveAudioOutput.id = audioOutput.id;
                    var parent = self.findParent(audioOutput);
                    if(parent.type === 'PHYSICAL_SCREEN'){
                        self.dialog.moveAudioOutput.selectPhysicalScreen = {
                            id:parent.id.replace('PHYSICAL_SCREEN_', ''),
                            name:parent.name
                        };
                    }
                    self.dialog.moveAudioOutput.visible = true;
                },
                handleMoveAudioOutputClose:function(){
                    var self = this;
                    self.dialog.moveAudioOutput.id = '';
                    self.dialog.moveAudioOutput.selectPhysicalScreen = '';
                    self.dialog.moveAudioOutput.visible = false;
                    self.dialog.moveAudioOutput.loading = false;
                },
                handleMoveAudioOutputSelectChannel:function(){
                    var self = this;
                    self.loadPhysicalScreen(function(physicalScreens){
                        self.$refs.moveAudioOutputSelectChannel.show('选择物理屏幕', self.dialog.physicalScreenColumns, physicalScreens);
                    });
                },
                onMoveAudioOutputSelectedChannel:function(physicalScreen, buff, done){
                    var self = this;
                    self.dialog.moveAudioOutput.selectPhysicalScreen = physicalScreen;
                    done();
                },
                handleMoveAudioOutputRemoveChannel:function(){
                    var self = this;
                    self.dialog.moveAudioOutput.selectPhysicalScreen = '';
                },
                handleMoveAudioOutputSubmit:function(){
                    var self = this;
                    self.dialog.moveAudioOutput.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/audio/output/move', {
                        id:self.dialog.moveAudioOutput.id.replace('AUDIO_OUTPUT_', ''),
                        physicalScreenId:self.dialog.moveAudioOutput.selectPhysicalScreen?self.dialog.moveAudioOutput.selectPhysicalScreen.id:null
                    }, function(data, status, message){
                        self.dialog.moveAudioOutput.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        };
                        var node = self.removeNode({id:self.dialog.moveAudioOutput.id});
                        if(!self.dialog.moveAudioOutput.selectPhysicalScreen){
                            self.graph.data.children.push(node);
                        }else{
                            var parentNode = self.findNode({id:'PHYSICAL_SCREEN_' + self.dialog.moveAudioOutput.selectPhysicalScreen.id});
                            parentNode.children.push(node);
                        }
                        self.render();
                        self.handleMoveAudioOutputClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                handleRemoveAudioOutput:function(audioOutput){
                    var self = this;
                    self.dialog.removeAudioOutput.id = audioOutput.id;
                    self.dialog.removeAudioOutput.visible = true;
                },
                handleRemoveAudioOutputClose:function(){
                    var self = this;
                    self.dialog.removeAudioOutput.id = '';
                    self.dialog.removeAudioOutput.removeChildren = false;
                    self.dialog.removeAudioOutput.visible = false;
                    self.dialog.removeAudioOutput.loading = false;
                },
                handleRemoveAudioOutputSubmit:function(){
                    var self = this;
                    self.dialog.removeAudioOutput.loading = true;
                    ajax.post('/tetris/bvc/model/terminal/audio/output/remove', {
                        id:self.dialog.removeAudioOutput.id.replace('AUDIO_OUTPUT_', ''),
                        removeChildren:self.dialog.removeAudioOutput.removeChildren
                    }, function(data, status, message){
                        self.dialog.removeAudioOutput.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        var node = self.removeNode({id:self.dialog.removeAudioOutput.id});
                        if(!self.dialog.removeAudioOutput.removeChildren){
                            if(node.children && node.children.length>0){
                                for(var i=0; i<node.children.length; i++){
                                    self.graph.data.children.push(node.children[i]);
                                }
                            }
                        }
                        self.render();
                        self.handleRemoveAudioOutputClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                }
            },
            mounted:function(){
                var self= this;
                self.loadGraph();
                self.loadVideoChannelParamTypes();
                ajax.post('/tetris/bvc/model/terminal/find/by/id', {
                    id:self.terminalId
                }, function(data){
                    self.physicalScreenLayout.enable = data.physicalScreenLayout;
                    self.physicalScreenLayout.columns = data.columns || 2;
                    self.physicalScreenLayout.rows = data.rows || 2;
                    if(data.physicalScreens && data.physicalScreens.length > 0){
                        for(var i=0; i<data.physicalScreens.length; i++){
                            self.physicalScreenLayout.physicalScreens.push(data.physicalScreens[i]);
                        }
                    }
                });
            }
        });

        $(document).on('click.contextmenu.close', function(e){
            var $contextMenu = $(e.target).closest('.contextmenu');
            if(!$contextMenu[0]){
                vueInstance.onContextmenuClose();
            }
        });

    };

    var destroy = function(){
        $(document).off('click.contextmenu.close');
    };

    var groupList = {
        path:'/' + pageId + '/:terminalId/:terminalName',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});