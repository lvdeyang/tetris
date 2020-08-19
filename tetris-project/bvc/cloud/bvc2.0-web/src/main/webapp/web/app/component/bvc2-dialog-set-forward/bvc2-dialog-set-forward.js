define([
    'text!' + window.APPPATH + 'component/bvc2-dialog-set-forward/bvc2-dialog-set-forward.html',
    'vue',
    'element-ui',
    'bvc2-transfer-source-target',
    'bvc2-transfer-role-target'
], function(tpl, Vue){

    //组件名称
    var bvc2DialogSetForward = 'bvc2-dialog-set-forward';

    //containsValue判断
    var containsValue = function(value, arr){
        for(var i=0;i<arr.length;i++){
            if(arr[i] == value){
                return true;
            }
        }
        return false;
    };

    var removeValue = function(value, arr){
        for(var i=0;i<arr.length;i++){
            if(arr[i] == value){
                arr.splice(i,1);
            }
        }
    };

    Vue.component(bvc2DialogSetForward, {
        props:['members', 'values', 'roles', 'roleValues', 'video'],
        template:tpl,
        data:function(){
            return {
                dst:[],
                roleDst:[],
                dialogVisible:false,
                currentTab:'role'
            }
        },
        methods:{
            changeSource:function(data, direction, changeData){
                var instance = this;
                instance.dst.splice(0, instance.dst.length);
                for(var i=0;i<data.length;i++){
                    instance.dst.push(data[i]);
                }
                if(direction == 'right'){
                    for(var j=0;j<changeData.length;j++){
                        if(changeData[j].roleId){
                            if(changeData[j].type == 'RECORDABLE'){
                                instance.roleValues.push(changeData[j].id);
                            }
                        }
                    }
                }else if(direction == 'left'){
                    for(var k=0;k<changeData.length;k++){
                        if(changeData[k].roleId){
                            if(changeData[k].type == 'RECORDABLE'){
                                if(containsValue(changeData[k].id, instance.roleValues)){
                                    removeValue(changeData[k].id, instance.roleValues)
                                }
                            }
                        }
                    }
                }
            },
            changeRole:function(data, direction, changeData){
                var instance = this;
                instance.roleDst.splice(0, instance.roleDst.length);
                for(var i=0;i<data.length;i++){
                    instance.roleDst.push(data[i]);
                }
                if(direction == 'right'){
                    for(var j=0;j<changeData.length;j++){
                        if(changeData[j].roleId){
                            if(changeData[j].type == 'RECORDABLE'){
                                instance.values.push(changeData[j].id);
                            }
                        }
                    }
                }else if(direction == 'left'){
                    for(var k=0;k<changeData.length;k++){
                        if(changeData[k].roleId){
                            if(changeData[k].type == 'RECORDABLE'){
                                if(containsValue(changeData[k].id, instance.values)){
                                    removeValue(changeData[k].id, instance.values)
                                }
                            }
                        }
                    }
                }
            },
            save:function(){
                var instance = this;
                var _dst = instance.dst;
                var _roleDst = instance.roleDst;

                if(!instance.video.dstsCache) {
                    instance.video.dstsCache = [];
                }

                var _dsts= instance.video.dstsCache;
                _dsts.splice(0, _dsts.length);
                for(var i=0;i<_dst.length;i++){
                    if(!containsValue(_dst[i], _dsts)){
                        _dsts.push(_dst[i]);
                    }
                }
                for(var j=0;j<_roleDst.length;j++){
                    if(!containsValue(_roleDst[j], _dsts)){
                        _dsts.push(_roleDst[j]);
                    }
                }

                instance.dialogVisible = false;
            }
        },
        watch:{
            values:function(){
                var instance = this,
                    _members = instance.members,
                    _values = instance.values,
                    _dst = instance.dst;

                _dst.splice(0, _dst.length);
                for(var i=0;i<_values.length;i++){
                    for(var j=0;j<_members.length;j++){
                        if(_values[i] == _members[j].id){
                            _dst.push(_members[j]);
                        }
                    }
                }
            },
            roleValues:function(){
                var instance = this,
                    _roles = instance.roles,
                    _roleValues = instance.roleValues,
                    _roleDst = instance.roleDst;

                _roleDst.splice(0, _roleDst.length);
                for(var m=0;m<_roleValues.length;m++){
                    for(var n=0;n<_roles.length;n++){
                        if(_roleValues[m] == _roles[n].id){
                            _roleDst.push(_roles[n]);
                        }
                    }
                }
            }
        }
    });

});