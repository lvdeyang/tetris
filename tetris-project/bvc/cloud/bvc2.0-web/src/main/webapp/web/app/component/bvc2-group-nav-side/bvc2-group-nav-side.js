define([
    'text!' + window.APPPATH + 'component/bvc2-group-nav-side/bvc2-group-nav-side.html',
    'vue',
    'element-ui'
], function(tpl, Vue){

    var bvc2GroupNavSide = 'bvc2-group-nav-side';

    Vue.component(bvc2GroupNavSide, {
        props:['active', 'group'],
        template:tpl,
        data:function(){
            return {
                isCollapse:false,
                isSlideMini:this.isCollapse,
                menus:[
                    {
                        id:'0',
                        icon:'icon-reply',
                        title:'返回',
                        href:'#/page-group-control/' + this.group.id
                    },{
                        id:'1',
                        icon:'el-icon-tickets',
                        title:'会议业务资源',
                        subs:[
                            {
                                id:'1-1',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:2px;',
                                title:'合屏',
                                href:'#/page-group-info-combinevideo/'+ this.group.id + '/10/1'
                            },{
                                id:'1-2',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:2px;',
                                title:'混音',
                                href:'#/page-group-info-combineaudio/'+ this.group.id + '/10/1'
                            },{
                                id:'1-3',
                                icon:'icon-circle-blank',
                                style:'position:relative; top:2px;',
                                title:'转发',
                                href:'#/page-group-info-forward/'+ this.group.id + '/100/1'
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

