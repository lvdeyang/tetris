define([
    'text!' + window.APPPATH + 'component/bvc2-group-table-base/bvc2-group-table-base.html',
    'restfull',
    'vue',
    'element-ui',
    'date'
], function(tpl, ajax, Vue){

    //组件名称
    var bvc2SystemTableBase = 'bvc2-group-table-base';
    console.log(bvc2SystemTableBase);

    //id序列
    var key_suffix = 0;

    var tableSelector = '.el-table__body-wrapper';

    var __rowChanged = 'row-changed';

    var __rowRemove = 'row-remove';

    var __rowRefresh = 'row-refresh';

    var __rowStart = 'row-start';

    var __rowStop = 'row-stop';

    //发射事件方法
    var __emit = function(_self, eName, data, e, done){
        if(typeof _self.$listeners[eName] === 'function'){
            if(typeof done === 'function'){
                _self.$emit(eName, data, done, e);
            }else{
                _self.$emit(eName, data, e);
            }
        }else{
            if(typeof done === 'function'){
                done();
            }
        }
    };

    Vue.component(bvc2SystemTableBase, {
        props:[
            'columns',
            'load',
            'remove',
            'refresh',
            'start',
            'stop',
            'pk',
            'options',
            'search',
            'breadcrumb',
            'pagesizes',
            'pagesize',
            'currentpage',
            'combine',
            'combineparam',
            'normalbutton'],
        template:tpl,
        data:function(){
            return {
                tableSelector:tableSelector,
                buttons:{
                    normal: [
                        {
                            label:'重发',
                            icon:'el-icon-refresh',
                            click:'row-refresh'
                        },{
                            label:'删除',
                            icon:'el-icon-delete',
                            click:'row-remove'
                        }
                    ],
                    onlyRefresh:[
                        {
                            label:'重发',
                            icon:'el-icon-refresh',
                            click:'row-refresh'
                        }
                    ],
                    start:{
                            label:'开始录制',
                            icon:'icon-play',
                            click:'row-start'
                        },
                    stop:{
                            label:'停止录制',
                            icon:'icon-stop',
                            click:'row-stop'
                        }
                },
                rows:[],
                selectedRow:[],
                pageSizes:this.pagesizes,
                pageSize:0,
                currentPage:0,
                total:0
            }
        },
        computed:{

        },
        methods: {

            /***********
             * 功能api
             ***********/

            //id生成器
            keyGenerator:function(){
                key_suffix+=1;
                return new Date().getTime() + '__' + key_suffix;
            },

            generateHref:function(row, column){
                var _pageSize = this.pageSize,
                    _currentPage = this.currentPage;

                if(row.combineVideoUuid){
                    return column.href + '/'+ row.combineVideoUuid + '/0/'+ _pageSize + '/' + _currentPage;
                }
                if(row.combineAudioUuid){
                    return column.href + '/'+ row.combineAudioUuid + '/1/'+ _pageSize + '/' + _currentPage;
                }
                if(row.combineUuid){
                    if(row.forwardSourceType === '合屏转发'){
                        return column.href + '-combinevideo' + '/' + column.groupId +'/' + row.combineUuid + '/' + _pageSize + '/' + _currentPage;
                    }
                    if(row.forwardSourceType === '混音转发'){
                        return column.href + '-combineaudio' + '/' + column.groupId + '/'+ row.combineUuid + '/' + _pageSize + '/' + _currentPage;
                    }

                }
            },

            //合并列
            objectSpanMethod:function(data){
                var table_instance = this;
                var _combine = table_instance.combine;
                var _combineParam = table_instance.combineparam;
                var _rows = table_instance.rows;

                if(_combine){
                    if(_combine.length > 0){
                        for(var i=0;i<_combine.length;i++){
                            if(data.columnIndex === _combine[i]){
                                if(data.rowIndex > 0){
                                    if(data.row[_combineParam] === _rows[data.rowIndex - 1][_combineParam]){
                                        return{
                                            rowspan: 0,
                                            colspan: 0
                                        }
                                    }else{
                                        var count = 0;
                                        for(var j=data.rowIndex; j<_rows.length; j++){
                                            if(data.row[_combineParam] === _rows[j][_combineParam]){
                                                count++;
                                            }
                                        }
                                        return{
                                            rowspan: count,
                                            colspan: 1
                                        }
                                    }
                                }else{
                                    var count = 0;
                                    for(var j=data.rowIndex; j<_rows.length; j++){
                                        if(data.row[_combineParam] === _rows[j][_combineParam]){
                                            count++;
                                        }
                                    }
                                    return{
                                        rowspan: count,
                                        colspan: 1
                                    }
                                }
                            }
                        }
                    }else{
                        return{
                            rowspan: 1,
                            colspan: 1
                        }
                    }
                }else{
                    return{
                        rowspan: 1,
                        colspan: 1
                    }
                }
            },

            //加载数据
            loadData:function(){
                var table_instance = this;
                var _pageSize, _currentPage;
                if(table_instance.pageSize === 0){
                    _pageSize = table_instance.pagesize;
                }else{
                    _pageSize = table_instance.pageSize;
                }

                if(table_instance.currentPage === 0){
                    _currentPage = table_instance.currentpage;
                }else{
                    _currentPage = table_instance.currentPage;
                }

                ajax.get(table_instance.load, {
                        pageSize:_pageSize,
                        currentPage:_currentPage
                    }, function(data){
                    if(data.rows && data.rows.length>0){
                        table_instance.rows.splice(0, table_instance.rows.length);
                        table_instance.total = data.total;
                        table_instance.pageSize = _pageSize;
                        table_instance.currentPage = _currentPage;
                        for(var i=0; i<data.rows.length; i++){
                            table_instance.rows.push(data.rows[i]);
                        }
                    }
                    if(typeof success === 'function') success.apply(table_instance, data.rows);

                    //数据行发生变化
                    __emit(table_instance, __rowChanged, table_instance.rows, tableSelector);
                });
            },

            /***********
             * 处理分页
             ***********/

            handleSizeChange:function(val) {
                var table_instance = this;
                table_instance.pageSize = parseInt(val);
                //刷新数据
                table_instance.loadData();
            },

            handleCurrentChange:function(val) {
                var table_instance = this;
                table_instance.currentPage = parseInt(val);
                //刷新数据
                table_instance.loadData();
            },

            /***********
             * 行操作
             ***********/

            rowButtonClick:function(e, scope, eName){
                var table_instance = this;
                if(eName === __rowRefresh){
                    table_instance.rowRefresh(e, scope, eName);
                }else if(eName === __rowRemove) {
                    table_instance.rowRemove(e, scope, eName);
                }else if(eName === __rowStart){
                    table_instance.rowStart(e, scope, eName);
                }else if(eName === __rowStop){
                    table_instance.rowStop(e, scope, eName);
                }else{
                    //发射事件
                    __emit(table_instance, eName, scope.row, e);
                }
            },

            //开始
            rowStart:function(e, scope, eName){
                var table_instance = this;
                var url = table_instance.start;
                ajax.post(url, {memberId: scope.row[table_instance.pk]}, function(data){
                    table_instance.$message({
                        type:'success',
                        message:'开始成功！'
                    });
                    scope.row.recordStatus = '录制中';
                });
            },

            //停止
            rowStop:function(e, scope, eName){
                var table_instance = this;
                var url = table_instance.stop;
                ajax.post(url, {memberId: scope.row[table_instance.pk]}, function(data){
                    table_instance.$message({
                        type:'success',
                        message:'停止成功！'
                    });
                    scope.row.recordStatus = '未录制';
                });
            },

            //行数据重发
            rowRefresh:function(e, scope, eName){
                var table_instance = this;
                var url = table_instance.refresh;
                ajax.post(url, {id: scope.row[table_instance.pk]}, function(data){
                    table_instance.$message({
                        type:'success',
                        message:'发送成功！'
                    });
                });
            },

            //删除行
            rowRemove:function(e, scope, eName){
                var table_instance = this;
                table_instance.$confirm('是否要删除本条数据？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    beforeClose:function(action, instance, d){
                        if(action === 'confirm'){
                            var done = function(){
                                instance.confirmButtonLoading = true;
                                instance.confirmButtonText = '执行中...';
                                var url = table_instance.remove + '/' + scope.row[table_instance.pk];
                                ajax.remove(url, null, function(data, status){

                                    instance.confirmButtonLoading = false;
                                    instance.confirmButtonText = '确定';

                                    if(status !== 200){
                                        d();
                                        return;
                                    }

                                    for(var i=table_instance.rows.length-1; i>=0; i--){
                                        if(table_instance.rows[i][table_instance.pk] === scope.row[table_instance.pk]){
                                            table_instance.rows.splice(i, 1);
                                        }
                                    }
                                    d();

                                    //处理分页
                                    table_instance.total = table_instance.total - 1;
                                    //数据行发生变化
                                    __emit(table_instance, __rowChanged, table_instance.rows, tableSelector);

                                }, null, [403, 404, 409]);
                            };
                            //删除前
                            __emit(table_instance, '', scope.row, e, done);
                        }else if(action === 'cancel'){
                            d();
                            table_instance.$message({
                                type:'info',
                                message:'操作取消！'
                            });
                        }
                    }

                });

            }


        },

        //渲染完成
        mounted:function(){
            var table_instance = this;
            //合并按钮
            if(table_instance.options && table_instance.options.length>0){
                var arr = table_instance.buttons.normal;
                var arr0 = table_instance.options;
                var indexBegin = 1;
                for(var i=0; i<arr0.length; i++){
                    arr.splice(indexBegin+i, 0, arr0[i]);
                }
            }
            //初始化数据
            table_instance.loadData();
        }
    });

});