define([
    'text!' + window.APPPATH + 'component/zk/footer/dialog/change-scheme/zk-footer-dialog-change-scheme.html',
    'restfull',
    'jquery',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'component/zk/footer/dialog/change-scheme/zk-footer-dialog-change-scheme.css'
], function(tpl, ajax, $, Vue){

	//组件名称
    var pluginName = 'zk-footer-dialog-change-scheme';

    Vue.component(pluginName, {
        props: [],
        template: tpl,
        data:function(){
            return {
            	baseUrl:window.BASEPATH,
                table:{
                    data:[{
                        id:1,
                        name:'方案1'
                    },{
                        id:2,
                        name:'方案2'
                    },{
                        id:3,
                        name:'方案3'
                    }],
                    currentRow:''
                },
                page:{
                    currentPage:1,
                    pageSize:50,
                    total:3
                },
                dialog:{
                    editScheme:{
                        visible:false,
                        id:'',
                        name:'',
                        newName:''
                    }
                }
            }
        },
        computed:{

        },
        watch:{

        },
        methods:{
            rowKey:function(row){
                return 'scheme_'+row.id;
            },
            load:function(currentPage){
                var self = this;
                self.page.currentPage = currentPage;
            },
            handleCurrentPageChange:function(currentPage){
                var self = this;
                self.load(currentPage);
            },
            currentRowChange:function(currentRow){
                var self = this;
                self.table.currentRow = currentRow;
            },
            handleRowEdit:function(scope){
                var self = this;
                var row = scope.row;
                self.dialog.editScheme.id = row.id;
                self.dialog.editScheme.name = row.name;
                self.dialog.editScheme.visible = true;
            },
            handleRowRemove:function(scope){
                var self = this;
                var row = scope.row;
            },
            handleEditSchemeClose:function(){
                var self = this;
                self.dialog.editScheme.id = '';
                self.dialog.editScheme.name = '';
                self.dialog.editScheme.newName = '';
                self.dialog.editScheme.visible = false;
            },
            handleEditSchemeCommit:function(){
                var self = this;
                self.handleEditSchemeClose();
            },
            handleChangeSchemeCommit:function(){
                var self = this;
                alert(self.table.currentRow.name);
            }
        },
        mounted:function(){
            var self = this;
            self.load(1);
        }
    });

    return Vue;
});