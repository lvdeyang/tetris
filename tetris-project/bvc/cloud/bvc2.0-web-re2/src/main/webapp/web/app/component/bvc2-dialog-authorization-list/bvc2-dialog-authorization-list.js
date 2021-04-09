define([
    'text!' + window.APPPATH + 'component/bvc2-dialog-authorization-list/bvc2-dialog-authorization-list.html',
    'restfull',
    'vue',
    'element-ui'
], function(tpl, ajax, Vue){

    var bvc2DialogAuthorizationList = 'bvc2-dialog-authorization-list';

    Vue.component(bvc2DialogAuthorizationList, {
        props:["group","query","save"],
        template:tpl,
        data:function(){
            return {
                data:[],
                checked:[],
                defaultExpandAll:false,
                defaultProps:{
                    children:'children',
                    label:'name'
                },
                row:this.group,
                dialogVisible:false,
                treeLoading:false,
                filterText:''
            }
        },
        watch:{
            group:function(){
                this.row = this.group;
            },
            filterText: function(val){
                this.$refs.$authorizationTree.filter(val);
            }
        },
        methods:{
            filterNode: function(value, data){
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            renderContent:function(h, target){
                var node = target.node;

                if(node.level === 1){
                    node.expanded = true;
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
            //刷新成成员编辑器
            changeToMemberTreeEditor:function(instance, row, done){
                if(row){
                    //查询成员
                    ajax.get(instance.query + row.id, null, function(data){
                        instance.checked.splice(0,instance.checked.length);
                        var bundles = data.bundles;
                        for(var j=0;j<bundles.length;j++){
                            instance.checked.push(bundles[j].key);
                        }
                        if(typeof done === 'function') done();
                    });
                }else{
                    instance.checked.splice(0,instance.checked.length);
                    if(typeof done === 'function') done();
                }
            },
            //保存看会权限
            saveWatchAuthorization:function(){
                var button_instance = this;
                var group = button_instance.row;
                var members = button_instance.$refs.$authorizationTree.getCheckedNodes();

                var memberIds = [];
                var bundles = [];
                var bundleIds = [];
                for(var i=0;i<members.length;i++){
                    if(members[i].type === 'BUNDLE'){
                        memberIds.push($.parseJSON(members[i].param).bundleId);
                        bundles.push({bundleName: members[i].name,
                            bundleId: members[i].id});
                        bundleIds.push(members[i].id);
                    }
                }
                ajax.post(button_instance.save + group.id, {
                    bundleIds:$.toJSON(memberIds)
                }, function(data){
                    button_instance.$message({
                        type:'success',
                        message:'设置成功！'
                    });
                    button_instance.dialogVisible = false;
                });
            },
            //获取全部成员，进行授权
            refreshAllMember:function(row){
                var button_instance = this;
                button_instance.treeLoading = true;

                var done = function(instance){

                    ajax.get('/device/group/query/all/bundle/tree/', null, function(data){

                        setTimeout(function(){
                            if(data && data.length>0){

                                instance.data = data;
                            }

                            setTimeout(function(){
                                instance.treeLoading = false;

                            }, 500);
                        }, 500);
                    });
                };

                button_instance.changeToMemberTreeEditor(button_instance, row, done(button_instance));

            }
        },
        //渲染开始之前
        created:function(){

        },
        mounted:function(){

        }
    });
});