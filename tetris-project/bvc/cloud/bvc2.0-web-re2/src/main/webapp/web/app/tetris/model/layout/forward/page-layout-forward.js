/**
 * Created by lvdeyang on 2020/6/5.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/layout/forward/page-layout-forward.html',
    'config',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'tetris/model/layout/forward/page-layout-forward.css'
], function(tpl, config, ajax, context, commons, Vue){

    var pageId = 'page-layout-forward';

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
                layoutId: p.layoutId,
                layoutName: p.layoutName,
                positions:{
                    rows:[]
                },
                terminal:{
                    rows:[],
                    current:''
                },
                physicalScreen:{
                    rows:[],
                    current:''
                },
                decodeSettings:{
                    data:[],
                    props:{
                        children: 'children',
                        label: 'name'
                    },
                    current:''
                },
                dialog:{
                    addDecode:{
                        visible:false,
                        loading:false,
                        mode:'',
                        confirm:[],
                        adaptable:[]
                    },
                    addCombinePosition:{
                        visible:false,
                        loading:false,
                        data:[],
                        treeNode:''
                    },
                    addForwardPosition:{
                        visible:false,
                        loading:false,
                        data:[],
                        treeNode:'',
                        selectedPosition:''
                    }
                }
            },
            computed:{
                terminalCurrent:function(){
                    var self = this;
                    return self.terminal.current;
                },
                physicalScreenCurrent:function(){
                    var self = this;
                    return self.physicalScreen.current;
                }
            },
            watch:{
                terminalCurrent:function(){
                    var self = this;
                    self.loadPhysicalScreens();
                },
                physicalScreenCurrent:function(){
                    var self = this;
                    self.loadDecodesSettings();
                }
            },
            methods:{
                loadPositions:function(){
                    var self = this;
                    self.positions.rows.splice(0, self.positions.rows.length);
                    ajax.post('/tetris/bvc/model/layout/position/load', {
                        layoutId:self.layoutId
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                data[i].selected = false;
                                data[i].disabled = false;
                                self.positions.rows.push(data[i]);
                            }
                        }
                    });
                },
                handleSelectPosition:function(position){
                    var self = this;
                    if(position.disable) return;
                    for(var i=0; i<self.positions.rows.length; i++){
                        self.positions.rows[i].selected = false;
                    }
                    position.selected = true;
                },
                loadTerminals:function(){
                    var self = this;
                    self.terminal.rows.splice(0, self.terminal.rows.length);
                    ajax.post('/tetris/bvc/model/terminal/load/all', null, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.terminal.rows.push(data[i]);
                            }
                            self.terminal.current = data[0].id;
                        }
                    });
                },
                loadPhysicalScreens:function(){
                    var self = this;
                    self.physicalScreen.rows.splice(0, self.physicalScreen.rows.length);
                    ajax.post('/tetris/bvc/model/terminal/physical/screen/find/by/terminal/id', {
                        terminalId:self.terminal.current
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.physicalScreen.rows.push(data[i]);
                            }
                            self.physicalScreen.current = data[0].id;
                        }
                    });
                },
                loadDecodesSettings:function(){
                    var self = this;
                    self.decodeSettings.data.splice(0, self.decodeSettings.data.length);
                    for(var i=0; i<self.positions.rows.length; i++){
                        self.positions.rows[i].disabled = false;
                    }
                    ajax.post('/tetris/bvc/model/layout/forward/find/by/layout/id/and/terminal/physical/screen/id', {
                        layoutId:self.layoutId,
                        terminalPhysicalScreenId:self.physicalScreen.current
                    }, function(data){
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.decodeSettings.data.push(data[i]);
                            }
                        }
                        var settedSerialNums = [];
                        for(var i=0; i<self.decodeSettings.data.length; i++){
                            var layoutPositions = self.decodeSettings.data[i].children[0].children;
                            if(layoutPositions!=null && layoutPositions.length>0){
                                for(var j=0; j<layoutPositions.length; j++){
                                    settedSerialNums.push(layoutPositions[j].serialNum);
                                }
                            }
                        }
                        for(var i=0; i<settedSerialNums.length; i++){
                            for(var j=0; j<self.positions.rows.length; j++){
                                if(self.positions.rows[j].serialNum == settedSerialNums[i]){
                                    self.positions.rows[j].disabled = true;
                                    self.positions.rows[j].selected = false;
                                    break;
                                }
                            }
                        }
                    });
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
                handleAddDecodes:function(){
                    var self = this;
                    self.dialog.addDecode.mode = '';
                    self.dialog.addDecode.confirm.splice(0, self.dialog.addDecode.confirm.length);
                    self.dialog.addDecode.adaptable.splice(0, self.dialog.addDecode.adaptable.length);
                    ajax.post('/tetris/bvc/model/layout/forward/load/decodes', {
                        layoutId:self.layoutId,
                        terminalPhysicalScreenId:self.physicalScreen.current
                    }, function(data){
                        self.dialog.addDecode.mode = data.mode;
                        if(data.confirmChannels && data.confirmChannels.length>0){
                            for(var i=0; i<data.confirmChannels.length; i++){
                                data.confirmChannels[i].checked = false;
                                self.dialog.addDecode.confirm.push(data.confirmChannels[i]);
                            }
                        }
                        if(data.adaptableChannels && data.adaptableChannels.length>0){
                            for(var i=0; i<data.adaptableChannels.length; i++){
                                data.adaptableChannels[i].checked = false;
                                self.dialog.addDecode.adaptable.push(data.adaptableChannels[i]);
                            }
                        }
                        self.dialog.addDecode.visible = true;
                    });
                },
                handleAddDecodeClose:function(){
                    var self = this;
                    self.dialog.addDecode.visible = false;
                    self.dialog.addDecode.loading = false;
                    self.dialog.addDecode.mode = '';
                    self.dialog.addDecode.confirm.splice(0, self.dialog.addDecode.confirm.length);
                    self.dialog.addDecode.adaptable.splice(0, self.dialog.addDecode.adaptable.length);
                },
                handleAddDecodeSubmit:function(){
                    var self = this;
                    var decodeIds = [];
                    for(var i=0; i<self.dialog.addDecode.confirm.length; i++){
                        if(self.dialog.addDecode.confirm[i].checked){
                            decodeIds.push(self.dialog.addDecode.confirm[i].id);
                        }
                    }
                    for(var i=0; i<self.dialog.addDecode.adaptable.length; i++){
                        if(self.dialog.addDecode.adaptable[i].checked){
                            decodeIds.push(self.dialog.addDecode.adaptable[i].id);
                        }
                    }
                    if(decodeIds.length <= 0){
                        self.$message({
                            type:'error',
                            message:'您还没有选择通道!'
                        });
                        return;
                    }
                    self.dialog.addDecode.loading = true;
                    ajax.post('/tetris/bvc/model/layout/forward/add', {
                        layoutId:self.layoutId,
                        terminalId:self.terminal.current,
                        terminalPhysicalScreenId:self.physicalScreen.current,
                        decodeIds: $.toJSON(decodeIds)
                    }, function(data, status, message){
                        self.dialog.addDecode.loading = false;
                        self.$message({
                            type:'error',
                            message:message
                        });
                        if(status !== 200) return;
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.decodeSettings.data.push(data[i]);
                            }
                        }
                        self.handleAddDecodeClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                },
                decodeCheckedChange:function(channel){
                    var self = this;
                    if(channel.checked){
                        if(channel.id == -1){
                            for(var i=0; i<self.dialog.addDecode.confirm.length; i++){
                                self.dialog.addDecode.confirm[i].checked = false;
                            }
                        }else{
                            for(var i=0; i<self.dialog.addDecode.adaptable.length; i++){
                                self.dialog.addDecode.adaptable[i].checked = false;
                            }
                        }
                    }
                },
                decodePositionStyle:function(decodeSetting){
                    var $physicalScreen = $('#page-layout-forward-wrapper .physical-screen-scope')[0];
                    var width = $physicalScreen.offsetWidth - 20;
                    var height = $physicalScreen.offsetHeight - 20;
                    var style = '';
                    var _x = decodeSetting.enablePosition?decodeSetting.x:'0';
                    var _y = decodeSetting.enablePosition?decodeSetting.y:'0';
                    var _width = decodeSetting.enablePosition?decodeSetting.width:'10000';
                    var _height = decodeSetting.enablePosition?decodeSetting.height:'10000';
                    var _zIndex = decodeSetting.enablePosition?decodeSetting.zIndex:'0';
                    if(decodeSetting.current){
                        style += 'left:' + ((parseInt(_x)*width/10000) - 1) + 'px;';
                        style += 'top:' + ((parseInt(_y)*height/10000) - 1) + 'px;';
                    }else{
                        style += 'left:' + parseInt(_x)*width/10000 + 'px;';
                        style += 'top:' + parseInt(_y)*height/10000 + 'px;';
                    }
                    decodeSetting._width = parseInt(_width)*width/10000;
                    decodeSetting._height = parseInt(_height)*height/10000;
                    if(decodeSetting.current){
                        style += 'width:' + (decodeSetting._width + 2) + 'px;';
                        style += 'height:' + (decodeSetting._height + 2) + 'px;';
                    }else{
                        style += 'width:' + decodeSetting._width + 'px;';
                        style += 'height:' + decodeSetting._height + 'px;';
                    }
                    style += 'z-index:' + parseInt(_zIndex);
                    return style;
                },
                sourcePositionStyle:function(sourcePositionSetting, width, height){
                    var style = '';
                    var _x = sourcePositionSetting.enablePosition?sourcePositionSetting.x:'0';
                    var _y = sourcePositionSetting.enablePosition?sourcePositionSetting.y:'0';
                    var _width = sourcePositionSetting.enablePosition?sourcePositionSetting.width:'10000';
                    var _height = sourcePositionSetting.enablePosition?sourcePositionSetting.height:'10000';
                    var _zIndex = sourcePositionSetting.enablePosition?sourcePositionSetting.zIndex:'0';
                    style += 'left:' + parseInt(_x)*width/10000 + 'px;';
                    style += 'top:' + parseInt(_y)*height/10000 + 'px;';
                    style += 'width:' + ((parseInt(_width)*width/10000)+1) + 'px;';
                    style += 'height:' + ((parseInt(_height)*height/10000)+1) + 'px;';
                    style += 'z-index:' + parseInt(_zIndex);
                    return style;
                },
                handleCurrentNodeChange:function(data, node){
                    var self = this;
                    if(self.decodeSettings.current){
                        self.decodeSettings.current.current = false;
                    }
                    if(data.type === 'FORWARD' || data.type === 'COMBINE_TEMPLATE'){
                        node.parent.data.current = true;
                        self.decodeSettings.current = node.parent.data;
                    }else if(data.type==='DECODE_CHANNEL' || data.type==='LAYOUT_POSITION' || data.type==='COMBINE_TEMPLATE_POSITION'){
                        data.current = true;
                        self.decodeSettings.current = data;
                    }
                },
                handleDecodeSelect:function(data){
                    var self = this;
                    if(self.decodeSettings.current){
                        self.decodeSettings.current.current = false;
                    }
                    data.current = true;
                    self.decodeSettings.current = data;
                },
                handleDecodeForwardSourceTypeChange:function(treeNodeType){
                    var self = this;
                    ajax.post('/tetris/bvc/model/layout/forward/change/source/type', {
                        layoutForwardId:self.decodeSettings.current.layoutForwardId,
                        treeNodeType:treeNodeType
                    }, function(data){
                        if(!data) return;
                        self.decodeSettings.current.children.splice(0, 1, data);
                    });
                },
                handleDecodeEnablePositionChange:function(enablePosition){
                    var self = this;
                    ajax.post('/tetris/bvc/model/layout/forward/change/enable/position', {
                        layoutForwardId:self.decodeSettings.current.layoutForwardId,
                        enablePosition:enablePosition
                    }, function(){});
                },
                handleCancelDecodeEdit:function(){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否取消存本次编辑?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/layout/forward/query/position', {
                                    layoutForwardId:self.decodeSettings.current.layoutForwardId
                                }, function(data, status, message){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) {
                                        self.$message({
                                            type:'error',
                                            message:message
                                        });
                                        return;
                                    }
                                    self.decodeSettings.current.x = parseInt(data.x);
                                    self.decodeSettings.current.y = parseInt(data.y);
                                    self.decodeSettings.current.width = parseInt(data.width);
                                    self.decodeSettings.current.height = parseInt(data.height);
                                    self.decodeSettings.current.zIndex = parseInt(data.zIndex);
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleSaveDecodeEdit:function(){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否保存本次编辑?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/layout/forward/save/position', {
                                    layoutForwardId:self.decodeSettings.current.layoutForwardId,
                                    x:self.decodeSettings.current.x,
                                    y:self.decodeSettings.current.y,
                                    width:self.decodeSettings.current.width,
                                    height:self.decodeSettings.current.height,
                                    zIndex:self.decodeSettings.current.zIndex
                                }, function(data, status, message){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) {
                                        self.$message({
                                            type:'error',
                                            message:message
                                        });
                                        return;
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleAddPosition:function(scope){
                    var self = this;
                    var data = scope.data;
                    if(data.type === 'DECODE_CHANNEL'){
                        data = data.children[0];
                    }
                    if(data.type === 'FORWARD'){
                        self.dialog.addForwardPosition.visible = true;
                        self.dialog.addForwardPosition.treeNode = data;
                        for(var i=0; i<self.positions.rows.length; i++){
                            if(!self.positions.rows[i].disabled){
                                var p = $.extend({checked:false}, self.positions.rows[i], true);
                                self.dialog.addForwardPosition.data.push(p);
                            }
                        }
                    }else if(data.type === 'COMBINE_TEMPLATE'){
                        self.dialog.addCombinePosition.visible = true;
                        self.dialog.addCombinePosition.treeNode = data;
                        for(var i=0; i<self.positions.rows.length; i++){
                            if(!self.positions.rows[i].disabled){
                                var p = $.extend({checked:false}, self.positions.rows[i], true);
                                self.dialog.addCombinePosition.data.push(p);
                            }
                        }
                    }
                },
                handleAddForwardPositionClose:function(){
                    var self = this;
                    self.dialog.addForwardPosition.visible = false;
                    self.dialog.addForwardPosition.loading = false;
                    self.dialog.addForwardPosition.treeNode = '';
                    self.dialog.addForwardPosition.selectedPosition = '';
                    self.dialog.addForwardPosition.data.splice(0, self.dialog.addForwardPosition.data.length);
                },
                handleAddForwardPositionSubmit:function(){
                    var self = this;
                    if(!self.dialog.addForwardPosition.selectedPosition){
                        self.$message({
                            type:'error',
                            message:'您没有选择布局'
                        });
                        return;
                    }
                    var currentPosition = '';
                    for(var i=0; i<self.dialog.addForwardPosition.data.length; i++){
                        if(self.dialog.addForwardPosition.data[i].id == self.dialog.addForwardPosition.selectedPosition){
                            currentPosition = self.dialog.addForwardPosition.data[i];
                            break;
                        }
                    }
                    self.dialog.addForwardPosition.loading = true;
                    ajax.post('/tetris/bvc/model/layout/forward/add/forward/position', {
                        layoutForwardId:self.dialog.addForwardPosition.treeNode.layoutForwardId,
                        layoutPositionSerialNum:currentPosition.serialNum
                    }, function(data, status, message){
                        self.dialog.addForwardPosition.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(self.dialog.addForwardPosition.treeNode.children[0]){
                            var oldSerialNum = self.dialog.addForwardPosition.treeNode.children[0].serialNum;
                            for(var i=0; i<self.positions.rows.length; i++){
                                if(self.positions.rows[i].serialNum == oldSerialNum){
                                    self.positions.rows[i].selected = false;
                                    self.positions.rows[i].disabled = false;
                                    break;
                                }
                            }
                            self.dialog.addForwardPosition.treeNode.children.splice(0, self.dialog.addForwardPosition.treeNode.children.length);
                        }
                        self.dialog.addForwardPosition.treeNode.children.push(data);
                        for(var i=0; i<self.positions.rows.length; i++){
                            if(self.positions.rows[i].id === currentPosition.id){
                                self.positions.rows[i].selected = false;
                                self.positions.rows[i].disabled = true;
                                break;
                            }
                        }
                        self.handleAddForwardPositionClose();
                    }, null, ajax.TOTAL_CATCH_CODE);
                    console.log(currentPosition);
                },
                handleAddCombinePositionClose:function(){
                    var self = this;
                    self.dialog.addCombinePosition.visible = false;
                    self.dialog.addCombinePosition.loading = false;
                    self.dialog.addCombinePosition.treeNode = '';
                    self.dialog.addCombinePosition.data.splice(0, self.dialog.addCombinePosition.data.length);
                },
                handleAddCombinePositionSubmit:function(){
                    var self = this;
                    var checked = [];
                    for(var i=0; i<self.dialog.addCombinePosition.data.length; i++){
                        if(self.dialog.addCombinePosition.data[i].checked){
                            checked.push(self.dialog.addCombinePosition.data[i]);
                        }
                    }
                    if(checked.length <= 0){
                        self.$message({
                            type:'error',
                            message:'您没有选中布局'
                        });
                        return;
                    }
                    self.dialog.addCombinePosition.loading = true;
                    ajax.post('/tetris/bvc/model/layout/forward/add/combine/position', {
                        layoutForwardId:self.dialog.addCombinePosition.treeNode.layoutForwardId,
                        layoutPositions: $.toJSON(checked)
                    }, function(data, status, message){
                        self.dialog.addCombinePosition.loading = false;
                        if(status !== 200){
                            self.$message({
                                type:'error',
                                message:message
                            });
                            return;
                        }
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.dialog.addCombinePosition.treeNode.children.push(data[i]);
                                for(var j=0; j<self.positions.rows.length; j++){
                                    if(self.positions.rows[j].serialNum == data[i].serialNum){
                                        self.positions.rows[j].selected = false;
                                        self.positions.rows[j].disabled = true;
                                        break;
                                    }
                                }
                            }
                        }
                        self.handleAddCombinePositionClose();
                    });
                },
                handleRemovePosition:function(scope){
                    var self = this;
                    var data = scope.data;
                    var node = scope.node;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否删除此布局?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                if(data.type === 'LAYOUT_POSITION'){
                                    ajax.post('/tetris/bvc/model/layout/forward/remove/forward/position', {
                                        layoutForwardId:data.layoutForwardId
                                    }, function(d, status, message){
                                        instance.confirmButtonLoading = false;
                                        done();
                                        if(status !== 200){
                                            self.$message({
                                                type:'error',
                                                message:message
                                            });
                                            return;
                                        }
                                        for(var i=0; i<self.positions.rows.length; i++){
                                            if(self.positions.rows[i].serialNum == data.serialNum){
                                                self.positions.rows[i].disabled = false;
                                                break;
                                            }
                                        }
                                        for(var i=0; i<node.parent.data.children.length; i++){
                                            if(node.parent.data.children[i] === data){
                                                node.parent.data.children.splice(i, 1);
                                                break;
                                            }
                                        }
                                    }, null, ajax.TOTAL_CATCH_CODE);
                                }else if(data.type === 'COMBINE_TEMPLATE_POSITION'){
                                    ajax.post('/tetris/bvc/model/layout/forward/remove/combine/template/position', {
                                        combineTemplatePositionId:data.id
                                    }, function(d, status, message){
                                        instance.confirmButtonLoading = false;
                                        done();
                                        if(status !== 200){
                                            self.$message({
                                                type:'error',
                                                message:message
                                            });
                                            return;
                                        }
                                        for(var i=0; i<self.positions.rows.length; i++){
                                            if(self.positions.rows[i].serialNum == data.serialNum){
                                                self.positions.rows[i].disabled = false;
                                                break;
                                            }
                                        }
                                        for(var i=0; i<node.parent.data.children.length; i++){
                                            if(node.parent.data.children[i] === data){
                                                node.parent.data.children.splice(i, 1);
                                                break;
                                            }
                                        }
                                    }, null, ajax.TOTAL_CATCH_CODE);
                                }
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleRemoveLayoutForward:function(scope){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否删除当前转发?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/layout/forward/remove', {
                                    layoutForwardId:scope.data.layoutForwardId
                                }, function(data, status, message){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) {
                                        self.$message({
                                            type:'error',
                                            message:message
                                        });
                                        return;
                                    }
                                    for(var i=0; i<self.decodeSettings.data.length; i++){
                                        if(self.decodeSettings.data[i] == scope.data){
                                            self.$refs.decodeSettingTree.remove(scope.data);
                                            self.decodeSettings.data.splice(i, 1);
                                            break;
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
                handleCancelCombineTemplatePositionEdit:function(){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否取消存本次编辑?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/layout/forward/query/combine/template/position', {
                                    combineTemplatePositionId:self.decodeSettings.current.id
                                }, function(data, status, message){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) {
                                        self.$message({
                                            type:'error',
                                            message:message
                                        });
                                        return;
                                    }
                                    self.decodeSettings.current.x = parseInt(data.x);
                                    self.decodeSettings.current.y = parseInt(data.y);
                                    self.decodeSettings.current.width = parseInt(data.width);
                                    self.decodeSettings.current.height = parseInt(data.height);
                                    self.decodeSettings.current.zIndex = parseInt(data.zIndex);
                                }, null, ajax.TOTAL_CATCH_CODE);
                            }else{
                                instance.confirmButtonLoading = false;
                                done();
                            }
                        }
                    }).catch(function(){});
                },
                handleSaveCombineTemplatePositionEdit:function(){
                    var self = this;
                    var h = self.$createElement;
                    self.$msgbox({
                        title:'提示',
                        message:h('div', null, [
                            h('div', {class:'el-message-box__status el-icon-warning'}, null),
                            h('div', {class:'el-message-box__message'}, [
                                h('p', null, ['是否保存本次编辑?'])
                            ])
                        ]),
                        type:'wraning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        beforeClose:function(action, instance, done){
                            instance.confirmButtonLoading = true;
                            if(action === 'confirm'){
                                ajax.post('/tetris/bvc/model/layout/forward/save/combine/template/position', {
                                    combineTemplatePositionId:self.decodeSettings.current.id,
                                    x:self.decodeSettings.current.x,
                                    y:self.decodeSettings.current.y,
                                    width:self.decodeSettings.current.width,
                                    height:self.decodeSettings.current.height,
                                    zIndex:self.decodeSettings.current.zIndex
                                }, function(data, status, message){
                                    instance.confirmButtonLoading = false;
                                    done();
                                    if(status !== 200) {
                                        self.$message({
                                            type:'error',
                                            message:message
                                        });
                                        return;
                                    }
                                }, null, ajax.TOTAL_CATCH_CODE);
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
                self.loadPositions();
                self.loadTerminals();
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