/**
 * Created by lvdeyang on 2018/11/20 0020.
 */
define([
    'text!' + window.APPPATH + 'front/media/permissions/page-media-permissions.html',
    'config',
    'context',
    'commons',
    'restfull',
    'json',
    'file',
    'uploader',
    'vue',
    'element-ui',
    'mi-frame',
    'css!' + window.APPPATH + 'front/media/permissions/page-media-permissions.css'
], function(tpl, config, context, commons, ajax, $, File, Uploader, Vue){

    var pageId = 'page-media-permissions';

    var init = function(p){

        var folderId = p.folderId;

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menus:context.getProp('menus'),
                user:context.getProp('user'),
                groups:context.getProp('groups'),
                currentTab:'audio',
                table:{
                    rows:[],
                    pageSize:50,
                    currentPage:0,
                    total:0,
                    currentRow:''
                },
                tree:{
                    props:{
                        label: 'title',
                        children: 'children'
                    },
                    data:[],
                    permissions:[]
                }
            },
            methods:{
                tabClick:function(tab){
                    var self = this;
                    self.loadFolders(tab.name);
                },
                rowKey:function(row){
                    return 'role-' + row.id;
                },
                currentRowChange:function(row){
                    var self = this;
                    self.table.currentRow = row;
                    self.loadFolders(self.currentTab);
                },
                loadRole:function(currentPage){
                    var self = this;
                    self.table.rows.splice(0, self.table.rows.length);
                    ajax.post('/business/role/list', {
                        currentPage:currentPage,
                        pageSize:self.table.pageSize
                    }, function(data){
                        var total = data.total;
                        var rows = data.rows;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.rows.push(rows[i]);
                            }
                            self.table.total = total;
                        }
                        self.table.currentPage = currentPage;
                    });
                },
                loadFolders:function(folderType){
                    var self = this;
                    self.tree.data.splice(0, self.tree.data.length);
                    ajax.post('/folder/media/tree/with/permissions/' + folderType, {
                        roleId:self.table.currentRow.id
                    }, function(data){
                        var treeNodes = data.treeNodes;
                        var permissions = data.permissions;
                        if(treeNodes && treeNodes.length>0){
                            for(var i=0; i<treeNodes.length; i++){
                                self.tree.data.push(treeNodes[i]);
                            }
                        }
                        self.tree.permissions.splice(0, self.tree.permissions);
                        if(permissions && permissions.length>0){
                            for(var i=0; i<permissions.length; i++){
                                self.tree.permissions.push(permissions[i]);
                            }
                            self.$nextTick(function(){
                                self.$refs.folderTree.setCheckedKeys(self.tree.permissions);
                            });
                        }
                    });
                },
                folderCheckChange:function(folder, isCheck){
                    var self = this;
                    if(isCheck){
                        if(self.tree.permissions.length > 0){
                            for(var i=0; i<self.tree.permissions.length; i++){
                                if(self.tree.permissions[i] == folder.id){
                                    return;
                                }
                            }
                        }
                        //添加一个权限
                        ajax.post('/folder/role/permission/add', {
                            folderId:folder.id,
                            roleId:self.table.currentRow.id,
                            roleName:self.table.currentRow.name
                        }, function(data){
                            self.$message({
                                type:'success',
                                message:'授权成功！'
                            });
                            self.tree.permissions.push(folder.id);
                        });
                    }else{
                        //删除一个权限
                        ajax.post('/folder/role/permission/delete', {
                            folderId:folder.id,
                            roleId:self.table.currentRow.id
                        }, function(){
                            self.$message({
                                type:'success',
                                message:'解除授权成功！'
                            });
                            for(var i=0; i<self.tree.permissions.length; i++){
                                if(self.tree.permissions[i] == folder.id){
                                    self.tree.permissions.splice(i, 1);
                                    break;
                                }
                            }
                        });
                    }
                }
            },
            created:function(){
                var self = this;
                self.loadRole(1);
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