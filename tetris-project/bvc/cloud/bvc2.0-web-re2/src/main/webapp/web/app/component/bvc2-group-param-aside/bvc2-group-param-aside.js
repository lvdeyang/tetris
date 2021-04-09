define([
    'text!' + window.APPPATH + 'component/bvc2-group-param-aside/bvc2-group-param-aside.html',
    'vue',
    'element-ui'
], function(tpl, Vue){

    var bvc2GroupParamAside = 'bvc2-group-param-aside';

    Vue.component(bvc2GroupParamAside, {
        props:['active','group'],
        template:tpl,
        data:function(){
            return {
                isCollapse:false,
                isSlideMini:this.isCollapse,
                menus:[
                    {
                        id:'0',
                        icon:'el-icon-tickets',
                        title:'会议参数',
                        subs:[
                            {
                                id:'0-1',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:1px;',
                                title:'参数方案',
                                href:'#/page-group-param-avtpl/'+this.group.id
                            },{
                                id:'0-2',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:1px;',
                                title:'业务角色',
                                href:'#/page-group-param-business-role/' + this.group.id
                            },{
                                id:'0-3',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:1px;',
                                title:'布局方案',
                                href:'#/page-group-param-layout/' + this.group.id
                            },{
                                id:'0-4',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:1px;',
                                title:'录制方案',
                                href:'#/page-group-param-record-scheme/' + this.group.id
                            },{
                                id:'0-5',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:1px;',
                                title:'修改发言人',
                                href:'#/page-group-param-update-spokesman/' + this.group.id
                            },{
                                id:'0-6',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:1px;',
                                title:'修改角色',
                                href:'#/page-group-param-update-roles/' + this.group.id
                            }
                        ]
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

