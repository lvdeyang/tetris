$(function(){

    var $app = new Vue({
        el: '#app',
        data:{
            key:"covid19",
            /**
             * 0:无状态上班
             * 1:有状态上班
             * 2:有状态下班
             * 3:打卡完成
             */
            status:'',
            editForm:'',
            form:{
                id:'',
                name:'',
                age:'',
                company:'',
                department:'',
                phone:'',
                homeAddress:'',
                liveAddress:'',
                workAddress:'',
                wayOfWork:'',
                beenToWuhanAfter20200101:'false',
                contactWithSuspectedOrConfirmedPatientsAfter20200101:'false',
                stayInHubeiSince20200101:'false',
                coughOrFever:'false',
                timeForWork:'',
                closingTime:''
            }
        },
        methods:{
            initStatus:function(){
                var self = this;
                var today = new Date().format('yyyy-MM-dd');
                var localCache = localStorage.getItem(self.key);
                var status;
                if(!localCache){
                    self.status = 0;
                    self.editForm = true;
                }else{
                    localCache = $.parseJSON(localCache);
                    if(!localCache.updateTime.startsWith(today)){
                        self.status = 1;
                        self.editForm = false;
                    }else{
                        if(localCache.closingTime){
                            //self.status = 3;
                            self.status = 1;
                            self.editForm = false;
                        }else{
                            self.status = 2;
                            self.editForm = false;
                        }
                    }
                }
            },
            initBaseInfo:function(){
                var self = this;
                var localCache = localStorage.getItem(self.key);
                if(localCache){
                    localCache = $.parseJSON(localCache);
                    for(var i in localCache){
                        self.form[i] = ''+localCache[i];
                        if(i === 'timeForWork'){
                            if(self.status===0 || self.status===1){
                                self.form.timeForWork = '';
                            }else{
                                self.form.timeForWork = localCache.timeForWork;
                            }
                        }else if(i === 'closingTime'){
                            if(self.status === 3){
                                self.form.closingTime = localCache.closingTime;
                            }else{
                                self.form.closingTime = '';
                            }
                        }
                    }
                    /*self.form.id = localCache.id,
                    self.form.name = localCache.name,
                    self.form.age = localCache.age,
                    self.form.company = localCache.company,
                    self.form.department = localCache.department,
                    self.form.phone = localCache.phone,
                    self.form.homeAddress = localCache.homeAddress;
                    self.form.liveAddress = localCache.liveAddress;
                    self.form.workAddress = localCache.workAddress;
                    self.form.wayOfWork = localCache.wayOfWork,
                    self.form.beenToWuhanAfter20200101 = localCache.beenToWuhanAfter20200101;
                    self.form.contactWithSuspectedOrConfirmedPatientsAfter20200101 = localCache.contactWithSuspectedOrConfirmedPatientsAfter20200101;
                    self.form.stayInHubeiSince20200101 = localCache.stayInHubeiSince20200101;
                    self.form.coughOrFever = localCache.coughOrFever;
                    if(self.status===0 || self.status===1){
                        self.form.timeForWork = '';
                    }else{
                        self.form.timeForWork = localCache.timeForWork;
                    }
                    if(self.status === 3){
                        self.form.closingTime = localCache.closingTime;
                    }else{
                        self.form.closingTime = '';
                    }*/
                }
            },
            handleTimeForWork:function(){
                var self = this;
                $.post(BASEPATH+'covid19/register/statistics/save', self.form, function(response){
                    if(response.status === 200){
                        var data = response.data;
                        localStorage.setItem(self.key, $.toJSON(data));
                        self.status = 2;
                        self.editForm = false;
                        for(var i in data){
                            self.form[i] = ''+ data[i];
                        }
                    }else{
                        self.$toast.error(response.message);
                    }
                });
            },
            handleClosingTime:function(){
                var self  = this;
                $.post(BASEPATH+'covid19/register/statistics/close', self.form, function(response){
                    if(response.status === 200){
                        var data = response.data;
                        localStorage.setItem(self.key, $.toJSON(data));
                        //self.status = 3;
                        self.status = 1;
                        self.editForm = false;
                        self.form.closingTime = data.closingTime;
                    }else{
                        self.$toast.error(response.message);
                    }
                });
            }
        },
        mounted:function(){
            var self = this;
            self.initStatus();
            self.initBaseInfo();
            self.$nextTick(function(){
                setTimeout(function(){
                    $('#blank').hide();
                }, 10);
            });
        }
    });

});