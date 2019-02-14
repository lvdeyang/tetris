/**
 * Created by lvdeyang on 2018/12/24 0024.
 */
define([
    'text!' + window.APPPATH + 'cms/template/page-cms-template.html',
    'config',
    'jquery',
    'restfull',
    'context',
    'commons',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'cms/template/page-cms-template.css'
], function(tpl, config, $, ajax, context, commons, Vue){

    var pageId = 'page-cms-template';

    var init = function(){

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
                tree:{
                    props:{
                        label: 'name',
                        children: 'subTags'
                    },
                    expandOnClickNode:false,
                    data:[],
                    current:''
                },
                loading:{
                    menu:false,
                    addRoot:false
                }
            },
            computed:{

            },
            watch:{

            },
            methods:{
                loadTagTree:function(){
                    var self = this;
                    self.tree.data.splice(0, self.tree.data.length);
                    ajax.post('/cms/template/tag/list/tree', null, function(data){
                        self.tree.data.push({
                            id:-1,
                            uuid:'-1',
                            name:'无标签',
                            icon:'icon-tag',
                            style:'font-size:15px; position:relative; top:1px; margin-right:1px;'
                        });
                        if(data && data.length>0){
                            for(var i=0; i<data.length; i++){
                                self.tree.data.push(data[i]);
                            }
                        }
                    });
                },
                addRootTreeNode:function(){
                    var self = this;
                    self.loading.menu = true;
                    self.loading.addRoot = true;
                    ajax.post('/cms/template/tag/add/root', null, function(data, status){
                        self.loading.menu = false;
                        self.loading.addRoot = false;
                        if(status !== 200) return;
                        self.tree.data.push(data);
                    }, null, ajax.NO_ERROR_CATCH_CODE);
                },
                currentTreeNodeChange:function(){
                    console.log(arguments);
                },
                treeNodeEdit:function(node, data){

                },
                treeNodeAppend:function(node, data){

                }
            },
            created:function(){
                var self = this;
                self.loadTagTree();
            }
        });

    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;

});