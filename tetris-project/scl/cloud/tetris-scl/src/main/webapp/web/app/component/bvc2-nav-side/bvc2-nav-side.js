define([
    'text!' + window.APPPATH + 'component/bvc2-nav-side/bvc2-nav-side.html',
    'vue',
    'element-ui'
], function(tpl, Vue){

    var bvc2NavSide = 'bvc2-nav-side';

    Vue.component(bvc2NavSide, {
        props:['active'],
        template:tpl,
        data:function(){
            return {
                isCollapse:false,
                isSlideMini:this.isCollapse,
                menus:[
                    {
                        id:'0',
                        icon:'el-icon-circle-plus',
                        title:'配置转发器',
                        href:'#/bvc2-add-repeater'
                    },
                    //{
                    //    id:'1',
                    //    icon:'el-icon-menu',
                    //    title:'转发器级联',
                    //    href:'#/bvc2-repeater-mapping'
                    //},
                    {
                        id:'2',
                        icon:'el-icon-tickets',
                        title:'终端绑定转发器',
                        href:'#/bvc2-terminal-repeater'
                    },{
                        id:'3',
                        icon:'el-icon-menu',
                        title:'流转发器任务',
                        href:'#/bvc2-repeater-task'
                    },{
                        id:'4',
                        icon:'el-icon-more',
                        title:'手动切换任务',
                        href:'#/bvc2-task-control'
                    }
                ]
            }
        },
        methods: {
            toggleCollapse:function(){
                if(this.isCollapse){
                    this.isCollapse = false;
                }else{
                    this.isCollapse = true;
                }
            },
            menuClick:function(data){
                if(data.href){
                    if(data.href.indexOf('#/') === 0){
                        window.location.hash = data.href;
                    }else{
                        window.location.href = data.href;
                    }
                }
            }
        }
    });

});

