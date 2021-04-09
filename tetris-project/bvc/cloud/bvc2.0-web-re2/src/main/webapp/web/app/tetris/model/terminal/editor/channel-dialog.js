/**
 * Created by lvdeyang on 2020/11/12.
 */
define([
    'text!' + window.APPPATH + 'tetris/model/terminal/editor/channel-dialog.html',
    'vue',
    'element-ui',
    'css!' + window.APPPATH + 'tetris/model/terminal/editor/channel-dialog.css'
], function(tpl, Vue){

    Vue.component('terminal-editor-channel-dialog', {
        template:tpl,
        data:function(){
            return{
                visible:false,
                title:'',
                table:{
                    rows:[],
                    current:''
                },
                buff:'',
                columns:[]
            }
        },
        methods:{
            show:function(title, columns, channels, buff){
                var self = this;
                if(columns && columns.length>0){
                    for(var i=0; i<columns.length; i++){
                        self.columns.push(columns[i]);
                    }
                }
                this.title = title;
                this.buff = buff;
                if(channels!=null && channels.length>0){
                    for(var i=0; i<channels.length; i++){
                        self.table.rows.push(channels[i]);
                    }
                }
                self.visible = true;
            },
            handleClose:function(){
                var self = this;
                self.visible = false;
                self.table.rows.splice(0, self.table.rows.length);
                self.table.current = '';
                self.buff = '';
                self.columns.splice(0, self.columns.length);
            },
            handleSubmit:function(){
                var self = this;
                if(!self.table.current){
                    self.$message({
                        type:'error',
                        message:'您没有选择数据'
                    });
                    return;
                }
                this.$emit('on-channel-selected', self.table.current, self.buff, function(){
                   self.handleClose();
                });
            },
            currentChange:function(currentRow){
                var self = this;
                self.table.current = currentRow;
            }
        }

    });

});