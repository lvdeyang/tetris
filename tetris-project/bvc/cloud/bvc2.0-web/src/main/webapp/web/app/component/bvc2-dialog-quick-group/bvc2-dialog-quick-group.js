define([
    'text!' + window.APPPATH + 'component/bvc2-dialog-quick-group/bvc2-dialog-quick-group.html',
    'restfull',
    'vue',
    'element-ui'
], function(tpl, ajax, Vue){

    var bvc2DialogQuickGroup = 'bvc2-dialog-quick-group';

    //节点展开方法
    var nodeExpandFunc = function(node, instance){
        var children = node.data.children;
        if(children){
            for(var i=0;i<children.length;i++){
                var _node = instance.tree.getNode(children[i]);
                if(_node.data.type === "FOLDER"){
                    _node.expanded = true;
                    nodeExpandFunc(_node, instance);
                }
            }
        }
    };

    Vue.component(bvc2DialogQuickGroup, {
        template:tpl,
        data:function(){
            return {
                data:[],
                defaultExpandAll:false,
                defaultProps:{
                    children:'children',
                    label:'name'
                },
                dialog:{
                    visible: false,
                    loading: false
                },
                filterText:'',
                chairman:{
                    bundleId:'',
                    bundleName:''
                },
                form:{
                    groupName:''
                },
                tree:{
                    loading:false
                }
            }
        },
        watch: {
            filterText: function(val){
                this.$refs.tree.filter(val);
            }
        },
        methods:{
            renderContent:function(h, target){
                var node = target.node,
                    data = target.data;

                var self = this;

                //展开一二级
                if(node.level === 1 || node.level === 2){
                    node.expanded = true;
                }

                //过滤Channel
                if(data.type === 'CHANNEL'){
                    node.visible = false;
                }else if(data.type === 'BUNDLE'){
                    node.isLeaf = true;
                }

                var chairman = null;
                if(data.type === 'BUNDLE'){
                    chairman = h('el-button', {
                        props:{
                            size: 'small',
                            type: 'primary'
                        },
                        on: {
                            click: function(){
                                node.checked = true;
                                self.chairman.bundleName = data.name;
                                self.chairman.bundleId = data.id;
                            }
                        },
                        style: {
                            'margin-left': '6px',
                            'right': '0',
                            'padding': '4px'
                        }
                    }, '设为主席');
                }

                var c = {};
                c[node.icon] = true;
                var classes = [c];
                if(data.bundleStatus){
                    classes.push(data.bundleStatus);
                }

                return h('span', {
                    class: {
                        'bvc2-tree-node-custom': true
                    }
                }, [
                    h('span', null, [
                        h('span', {
                            class: classes,
                            style: {
                                'font-size': '16px',
                                'position': 'relative',
                                'top': '1px',
                                'margin-right': '5px'
                            }
                        }, null),
                        node.label,
                        chairman
                    ])
                ]);
            },
            filterNode: function(value, data, node){
                if(data.type === 'BUNDLE' || data.type === 'FOLDER'){
                    if (!value) return true;
                    if(data.name.indexOf(value) !== -1 || (node.parent && node.parent.level !== 0 && node.parent.data.name.indexOf(value) !== -1)) return true;
                }
            },
            getCheckedNodes:function(){
                return this.$refs.tree.getCheckedNodes();
            },
            beforeClose: function(done){
                var self = this;
                self.chairman.bundleId = '';
                self.chairman.bundleName = '';
                done();
            },
            quickGroup:function(){
                var self = this;
                var groupName = self.form.groupName;
                var chairmanId = self.chairman.bundleId;
                var checkedSource = self.getCheckedNodes();
                var members = [];
                for(var i=checkedSource.length-1;i>=0;i--){
                    if(checkedSource[i].type != 'BUNDLE'){
                        checkedSource.splice(i,1);
                    }
                }

                if(groupName == null || groupName == ""){
                    self.$message({
                        message: '请填写会议名称！',
                        type: 'warning'
                    });
                    return;
                }

                if(checkedSource == null || checkedSource.length == 0){
                    self.$message({
                        message: '请选择设备！',
                        type: 'warning'
                    });
                    return;
                }

                if(chairmanId == null || chairmanId == ''){
                    self.$message({
                        message: '请选择主席！',
                        type: 'warning'
                    });
                    return;
                }

                for(var i=0; i<checkedSource.length; i++){
                    members.push(checkedSource[i].id);
                }

                var afterSuccess = function(){
                    self.dialog.loading = false;
                    self.dialog.visible = false;
                };

                self.dialog.loading = true;
                self.$emit("quick-group", groupName, chairmanId, members, afterSuccess);
            },
            //获取所有成员
            refreshMember:function(){
                var self = this;
                self.data.splice(0, self.data.length);
                self.tree.loading = true;
                ajax.get('/device/group/query/all/source/tree', null, function (data) {
                    if(data && data.length>0){
                        for(var i=0; i<data.length; i++){
                            self.data.push(data[i]);
                        }
                        self.tree.loading = false;
                    }
                });
            },
            checkChange:function(data, checked, indeterminate){
                var self = this;
                if(!checked){
                    if(data.id === self.chairman.bundleId){
                        self.chairman.bundleId = '';
                        self.chairman.bundleName = '';
                    }
                }
            },
            init: function(){
                var self = this;
                self.dialog.visible = true;
                self.refreshMember();
                self.form.groupName = '';
                self.chairman.bundleId = '';
                self.chairman.bundleName = '';
            }
        },
        //渲染开始之前
        created:function(){

        }
    });
});