define([
    'text!' + window.APPPATH + 'component/bvc2-set-small-screen/bvc2-set-small-screen.html',
    'restfull',
    'context',
    'jquery',
    'vue',
    'element-ui'
], function(tpl, ajax, context, $, Vue){

    var app = context.getProp('app');

    //组件名称
    var bvc2SetSmallScreen = 'bvc2-set-small-screen';

    Vue.component(bvc2SetSmallScreen, {
        props:['members', 'small'],
        template: tpl,
        data:function(){
            return {
                name:'',
                defaultProps:{
                    children:'children',
                    label:'name',
                    isLeaf:'isLeaf'
                }
            }
        },
        watch:{
            small:function(){
                var button_instance = this;
                button_instance.refreshData();
            }
        },
        methods:{
            refreshData:function(){
                var button_instance = this;
                var small = button_instance.small;
                if(JSON.stringify(small) != "{}"){
                    var param = $.parseJSON(small.param);
                    if(param.bundleName){
                        button_instance.name = param.bundleName + '-' + small.name;
                    }else if(param.roleName){
                        button_instance.name = param.roleName + '-' + small.name;
                    }
                }else{
                    button_instance.name = "";
                }
            },
            renderContent:function(h, target){
                var node = target.node,
                    data = target.data;
                if(data.type == 'CHANNEL'){
                    node.isLeaf = true;
                    if(JSON.parse(data.param).channelType != 'VenusVideoIn'){
                        node.visible = false;
                    }
                }

                if(data.type == 'VIRTUAL'){
                    node.visible = false;
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

            handleDragStart:function(node, e){
                var data = node.data;
                e.dataTransfer.setData('node', $.toJSON(data));
            },

            allowDrop:function(){
                return false;
            },

            allowDrag:function(node){
                var data = node.data;
                if(data.type === 'CHANNEL'){
                    return true;
                }else{
                    return false;
                }
            },

            smallScreenSet:function(e){
                var button_instance = this;
                var node = $.parseJSON(e.dataTransfer.getData('node'));
                var param = $.parseJSON(node.param);
                button_instance.$emit("modify-small", node);
                if(param.bundleName){
                    button_instance.name = param.bundleName + '-' + node.name;
                }else if(param.roleName){
                    button_instance.name = param.roleName + '-' + node.name;
                }
            },

            allowNativeDrop:function(e){
                e.preventDefault();
            },

            //清空发言人
            removeSmallScreen:function(){
                var button_instance = this;
                button_instance.source = {};
                button_instance.name = '';

            }

        },
        mounted:function(){
            var button_instance = this;
            button_instance.refreshData();
        }
    });

});