define([
    'text!' + window.APPPATH + 'group/record/page-group-record.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-group-table-base',
    'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-group-record';

    var init = function(p){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_monitor = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                menurouter: false,
                shortCutsRoutes:commons.data,
                active:"/page-group-list",
                header:commons.getHeader(0),
                group:p,
                table:{
                    breadcrumb:[
                        {
                            label:'返回',
                            icon:'icon-reply',
                            href:'#/page-group-control/'+ p.id
                        },{
                            label:'会议记录'
                        }
                    ],
                    columns:[
                        {
                            label:'点播',
                            prop:'name',
                            type:'simple',
                            width:'500'
                        },{
                            label:'播放地址',
                            prop:'assetPlayUrl',
                            type:'simple'
                        }],
                    options:[
                        {
                            label:'下载',
                            icon:'el-icon-download',
                            iconStyle:'font-size:14px;',
                            click:'record-download'
                        }
                    ],
                    load:'/device/group/record/query/playlist/' + p.id,
                    start:'',//'/device/group/monitor/start/record/' + p.id,
                    stop:'',//'/device/group/monitor/stop/record/' + p.id,
                    remove:'/device/group/record/asset/remove',
                    pk:'id',
                    search:true,
                    pagesizes:[100, 200, 300, 400],
                    pagesize: 100,
                    currentpage: 1
                },
                download:{
                    loading:''
                }
            },
            methods:{
                recordDownload: function(row, e){
                    var self = this;
                    self.download.loading = self.$loading({
                        target: e.target.parentElement.parentElement.parentElement,
                        body: true,
                        spinner: 'el-icon-loading',
                        customClass: 'download'
                    });
                    //setTimeout(self.download.loading.close(),3000);
                    this.downloadInterval(row.id);
                },

                downloadInterval: function(id){
                    var self = this;
                    ajax.post("/device/group/record/download/" + id, null, function(data, status){

                        if(status == 200){
                            if(data){
                                self.download.loading.close();
                                setTimeout(window.open("/device/group/record/download/file?storePath=" + encodeURI(data)), 500);
                            }else{
                                setTimeout(self.downloadInterval(id),5000);
                            }
                        }else{
                            self.download.loading.close();
                        }

                    }, null, [403, 500, 409]);
                }
            }

        });

    };

    var destroy = function(){

    };

    var groupInfo = {
        path:'/' + pageId + '/:id',
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupInfo;
});
