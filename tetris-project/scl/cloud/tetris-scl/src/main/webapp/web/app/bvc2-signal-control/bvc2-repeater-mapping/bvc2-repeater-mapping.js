define([
    'text!' + window.APPPATH + 'bvc2-signal-control/bvc2-repeater-mapping/bvc2-repeater-mapping.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-nav-side'
], function(tpl, ajax, $, Vue){

    //组件名称
    var pageId = 'bvc2-repeater-mapping';

    var init = function(){

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                header:{
                    links:[],
                    user:''
                },
                active:'1',
                tree:{
                    in:{
                        data:[],
                        props:{
                            label: 'name',
                            children: 'children'

                        },
                        treeLoading: false
                    },
                    out:{
                        data:[],
                        props:{
                            label: 'name',
                            children: 'children'

                        },
                        treeLoading: false
                    }
                },
                card:{
                    in:{
                        value:''
                    },
                    out:{
                        value:''
                    }
                },
                table:{
                    treeLoading: false
                }

            },
            methods: {
                renderContent:function(h, target){
                    var node = target.node;

                    var c = {};
                    c[node.icon] = true;
                    var classes = [c];

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
                loadReperterTree:function(){
                    var self = this;
                    self.tree.in.data.splice(0, self.tree.in.data.length);
                    self.tree.out.data.splice(0, self.tree.in.data.length);
                    self.tree.in.treeLoading = true;
                    self.tree.out.treeLoading = true;
                    ajax.post('/signal/control/mapping/repeater/tree', null, function(data){
                        self.tree.in.data.push(data.in);
                        self.tree.out.data.push(data.out);
                        self.tree.in.treeLoading = false;
                        self.tree.out.treeLoading = false;
                    });
                },
                inHandleDragStart:function(node, e){
                    var data = node.data;
                    e.dataTransfer.setData('node', $.toJSON(data));
                },

                inAllowDrop:function(){
                    return false;
                },

                inAllowDrag:function(node){
                    var data = node.data;
                    if(data.type === 'INTERNET_ACCESS'){
                        return true;
                    }else{
                        return false;
                    }
                },

                outHandleDragStart:function(node, e){
                    var data = node.data;
                    e.dataTransfer.setData('node', $.toJSON(data));
                },

                outAllowDrop:function(){
                    return false;
                },

                outAllowDrag:function(node){
                    var data = node.data;
                    if(data.type === 'INTERNET_ACCESS'){
                        return true;
                    }else{
                        return false;
                    }
                },
            },
            mounted:function(){
                var self = this;
                self.loadReperterTree();
            }

        });
    };

    var destroy = function(){

    };

    var page = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return page;
});