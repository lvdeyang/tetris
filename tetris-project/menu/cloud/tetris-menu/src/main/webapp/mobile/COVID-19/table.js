$(function(){

    var $app = new Vue({
        el: '#app',
        data:function(){
            return {
                table:{
                    columns: [
                        {title:'姓名', name:'name'},
                        {title:'日期', name:'updateTime'},
                        {title:'年龄', name:'age'},
                        {title:'单位', name:'company'},
                        {title:'部门', name:'department'},
                        {title:'电话', name:'phone'},
                        {title:'家庭住址', name:'homeAddress'},
                        {title:'现居地址', name:'liveAddress'},
                        {title:'工作地址', name:'workAddress'},
                        {title:'前往单位出行方式', name:'wayOfWork'},
                        {title:'2020年1月1后有无去过武汉', name:'beenToWuhanAfter20200101'},
                        {title:'2020年1月1后有无接触疑似确诊病人', name:'contactWithSuspectedOrConfirmedPatientsAfter20200101'},
                        {title:'自2020年1月1日起，在湖北省停留', name:'stayInHubeiSince20200101'},
                        {title:'近期有无咳嗽发烧症状', name:'coughOrFever'},
                        {title:'进入单位时间', name:'timeForWork'},
                        {title:'离开单位时间', name:'closingTime'}
                    ],
                    data:[],
                    page:{
                        currentPage:1,
                        total:0,
                        pageSize:50
                    },
                    loading:false
                }
            }
        },
        methods:{
            load:function(currentPage){
                var self = this;
                self.table.data.splice(0, self.table.data.length);
                $.post(BASEPATH + 'covid19/register/statistics/load', {
                    currentPage:currentPage,
                    pageSize:self.table.page.pageSize
                }, function(response){
                    if(response.status === 200){
                        var data = response.data;
                        var rows = data.rows;
                        var total = data.total;
                        if(rows && rows.length>0){
                            for(var i=0; i<rows.length; i++){
                                self.table.data.push(rows[i]);
                            }
                        }
                        self.table.page.total = total;
                    }else{
                        self.$toast.error(response.message);
                    }
                });
            }
        },
        mounted:function(){
            var self = this;
            self.load(1);
        }
    });

});
