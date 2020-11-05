define([
    'text!' + window.APPPATH + 'system/resource/layout/page-layout.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base',
    'bvc2-dialog-auto-layout'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-layout';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        var $page = document.getElementById(pageId);
        $page.innerHTML = tpl;

        var v_layout = new Vue({
            el:'#' + pageId + '-wrapper',
            data:{
                header:commons.getHeader(1),
                side:{
                    active:'0-4'
                },
                table:{
                    buttonCreate:'新建布局',
                    buttonRemove:'删除布局',
                    columns:[{
                        label:'布局名称',
                        prop:'name',
                        type:'simple'
                    },{
                        label:'布局预览',
                        selector:'bvc2-table-layout-preview',
                        style:'display:inline-block; width:40px; height:40px; padding:0; margin:0; position:relative; top:4px;',
                        height:'48px',
                        type:'html',
                        editable:true,
                        width:'100'
                    }],
                    load:'/system/screen/layout/load',
                    save:'/system/screen/layout/save',
                    update:'/system/screen/layout/update',
                    remove:'/system/screen/layout/remove',
                    removebatch:'/system/screen/layout/remove/all',
                    pk:'id'
                }
            },
            methods:{
                rowChanged:function(rows){
                    var instance = this;
                    //这个让vue先渲染，然后再执行
                    setTimeout(function(){
                        //创建预览
                        $(instance.$refs.$layoutTable.tableSelector + ' .bvc2-table-layout-preview').each(function(){
                            var $this = $(this);
                            var pk = $this[0].getAttribute('data-pk');
                            var finded = false;
                            for(var i=0; i<rows.length; i++){
                                if(rows[i].websiteDraw && rows[i].id==pk){
                                    var layout = $.parseJSON(rows[i].websiteDraw);
                                    $this['layout-auto']('create', {
                                        cell:{
                                            column:layout.basic.column,
                                            row:layout.basic.row
                                        },
                                        cellspan:layout.cellspan,
                                        editable:false,
                                        theme:'gray'
                                    });
                                    finded = true;
                                    break;
                                }
                            }
                            if(!finded) $this.empty();
                        });
                    }, 0);
                },
                layoutEdit:function(p){
                    var instance = this;
                    instance.$refs.$dialogAutoLayout.show = true;
                    instance.$refs.$dialogAutoLayout.target = p.row;
                    if(p.row.websiteDraw){
                        var websiteDraw = $.parseJSON(p.row.websiteDraw);
                        instance.$refs.$dialogAutoLayout.layout = {
                            column:websiteDraw.basic.column,
                            row:websiteDraw.basic.row,
                            cellspan:websiteDraw.cellspan
                        }
                    }else{
                        instance.$refs.$dialogAutoLayout.layout = {
                            column:4,
                            row:4,
                            cellspan:[]
                        }
                    }
                },

                layoutSelected:function(data, done, e){
                    var instance = this;
                    var tpl = data.tpl;
                    var row = data.target;
                    var websiteDraw = {
                        basic:tpl.basic,
                        cellspan:tpl.cellspan
                    };
                    var position = tpl.layout;
                    row.websiteDraw = $.toJSON(websiteDraw);
                    row.position = $.toJSON(position);

                    $(instance.$refs.$layoutTable.tableSelector + ' .bvc2-table-layout-preview').each(function(){
                        var $this = $(this);
                        var pk = $this[0].getAttribute('data-pk');

                        if(pk == row.id){
                            var layout = $.parseJSON(row.websiteDraw);
                            $this['layout-auto']('create', {
                                cell:{
                                    column:layout.basic.column,
                                    row:layout.basic.row
                                },
                                cellspan:layout.cellspan,
                                editable:false,
                                theme:'gray'
                            });
                        }
                    });

                    done();
                }

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