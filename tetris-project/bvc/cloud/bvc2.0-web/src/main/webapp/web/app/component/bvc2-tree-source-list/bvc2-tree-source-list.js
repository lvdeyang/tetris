define([
    'text!' + window.APPPATH + 'component/bvc2-tree-source-list/bvc2-tree-source-list.html',
    'restfull',
    'vue',
    'element-ui'
], function(tpl, ajax, Vue){

    var bvc2TreeMeetingMember = 'bvc2-tree-source-list';

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

    ajax.get('/device/group/query/all/source/tree', null, function (data) {

        Vue.component(bvc2TreeMeetingMember, {
            props:['checkedmembers'],
            template:tpl,
            data:function(){
                return {
                    data:data,
                    checked:this.checkedmembers,
                    defaultExpandAll:false,
                    defaultProps:{
                        children:'children',
                        label:'name'
                    },
                    filterText:''
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

                    //过滤选中
                    var _checked = this.checked;
                    if(_checked.length > 0){
                        for(var i=0;i<_checked.length;i++){
                            if(node.key === _checked[i]){
                                node.checked = true;
                                break;
                            }
                        }
                    }

                     //过滤Channel
                    if(data.type === 'CHANNEL'){
                        node.visible = false;
                    }else if(data.type === 'BUNDLE'){
                        node.isLeaf = true;
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
                            node.label
                        ])
                    ]);
                },
                filterNode: function(value, data, node){
                    if(data.type === 'BUNDLE' || data.type === 'FOLDER'){
                        if (!value) return true;
                        if(data.name.indexOf(value) !== -1 || (node.parent && node.parent.level !== 0 && node.parent.data.name.indexOf(value) !== -1)) return true;
                    }
                },
                nodeExpand:function(data, node, instance){
                    if(data.icon === 'icon-folder-close'){
                        data.icon = 'icon-folder-open';
                    }
                    //一级节点自定义展开
                    if(node.level === 1){
                        node.expanded = true;
                        //nodeExpandFunc(node, instance);
                    }
                },
                nodeCollapse:function(data, node, instance){
                    if(data.icon === 'icon-folder-open'){
                        data.icon = 'icon-folder-close';
                    }
                },
                getCheckedNodes:function(){
                    return this.$refs.tree.getCheckedNodes();
                }
            },
            //渲染开始之前
            created:function(){

            }
        });
    });
});