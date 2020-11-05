define([
    'text!' + window.APPPATH + 'group/resource/record-scheme/page-group-param-record-scheme.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-group-param-aside',
    'bvc2-system-table-base',
	'extral'
], function(tpl, ajax, config, commons, Vue){
	
	var pageId = 'page-group-param-record-scheme';
	
	var init = function(p){
		
		//设置标题
		commons.setTitle(pageId);
		
		ajax.get('/device/group/param/query/record/role/' + p.id, null, function(data){
			
			//转换数据
			var roleOptions = [];
			var roleArr = data.role;
			
			if(roleArr && roleArr.length>0){
				for(var i=0; i<roleArr.length; i++){
					roleOptions.push({
						label:roleArr[i].name,
						value:roleArr[i].id
					})
				}
			}
			
			var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;
            
			var v_recordScheme = new Vue({
				el:'#' + pageId + '-wrapper',
                data:{
					menurouter: false,
					shortCutsRoutes:commons.data,
					active:"/page-group-list",
                    header:commons.getHeader(0),
                    side:{
                        active:'0-4'
                    },
                    group:p,
                    scheme:{
                        buttonCreate:'新建录制',
                        buttonRemove:'删除录制',
                        columns:[{
                            label:'录制名称',
                            prop:'name',
                            type:'simple'
                        },{
                            label:'录制角色',
                            prop:'roleName',
                            hidden:'roleId',
                            type:'select',
                            options:roleOptions,
                            width:'240px'
                        }],
						load:'/device/group/param/query/record/scheme/' + p.id,
						save:'/device/group/param/save/record/scheme/' + p.id,
						update:'/device/group/param/update/record/scheme',
						remove:'/device/group/param/remove/record/scheme',
						removebatch:'/device/group/param/remove/all/record/scheme',
						pk:'id'
					}
				},
				methods:{
					beforeSave:function(row, done){
						var instance = this;
						var _columns = instance.scheme.columns;
						
						//空校验
						for(var i=0; i<_columns.length; i++){
							if(_columns[i].type){
								if(row[_columns[i].prop] == null || row[_columns[i].prop] == ''){
									instance.$message({
										message: _columns[i].label + "不能为空！",
										type:'warning'
									});
									return;
								}
							}
							
						}
					
						done();
						
					}
				}
			});
		});
	};
	
	var destroy = function(){
		
	};
	
	var groupList = {
		path:'/' + pageId + '/:id',
		component:{
			template:'<div id="' + pageId + '" class="page-wrapper"></div>'
		},
		init:init,
		destroy:destroy
	}
	
	return groupList;
	
})