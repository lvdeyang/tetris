define([
    'text!' + window.APPPATH + 'group/resource/business-role/page-group-param-business-role.html',
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
	
	var pageId = 'page-group-param-business-role';
	
	var init = function(p){
		
		//设置标题
		commons.setTitle(pageId);
		
		ajax.get('/system/business/role/query/code', null, function(data){
			
			//数据转换
			var specialOptions = [];
			var specialArr = data.special;
			if(specialArr && specialArr.length>0){
				for(var i=0; i<specialArr.length; i++){
					specialOptions.push({
						label:specialArr[i],
						value:specialArr[i]
					});
				}
			}

			var typeOptions = [];
			var typeArr = data.type;
			if(typeArr && typeArr.length>0){
				for(var i=0; i<typeArr.length; i++){
					typeOptions.push({
						label:typeArr[i],
						value:typeArr[i]
					});
				}
			}

			var $page = document.getElementById(pageId);
			$page.innerHTML = tpl;
			
			var findChangeColumn = function(columns, special){
				var _changeColumn;
				for(var i=0;i<columns.length;i++){
					if(columns[i].prop === special){
						_changeColumn = columns[i];
						break;
					}
				}

				return _changeColumn;
			};

			var v_bussinessRole = new Vue({
				el:'#' + pageId + '-wrapper',
				data:{
					menurouter: false,
					shortCutsRoutes:commons.data,
					active:"/page-group-list",
					header:commons.getHeader(0),
					side:{
						active:'0-2'
					},
					group:p,
					role:{
						buttonCreate:'新建角色',
						buttonRemove:'删除角色',
						columns:[{
							label:'角色名称',
							prop:'name',
							type:'simple'
						},{
							label:'角色属性',
							prop:'special',
							type:'select',
							options:specialOptions,
							width:'240px',
							disabled:false
						},{
							label:'角色类型',
							prop:'type',
							type:'select',
							options:typeOptions,
							width:'240'
						}],
						load:'/device/group/param/query/role/' + p.id,
						save:'/device/group/param/save/role/' + p.id,
						update:'/device/group/param/update/role/' + p.id,
						remove:'/device/group/param/remove/role/' + p.id,
						removebatch:'/device/group/param/remove/all/role/' + p.id,
						pk:'id'
					}
				},
				methods:{
					beforeSave:function(row, done){
						 var instance = this,
                         _columns = instance.role.columns;

						 //空校验
						 for(var i=0;i<_columns.length;i++){
							 if(_columns[i].type){
								 if(row[_columns[i].prop] == null || row[_columns[i].prop] == ''){
									 instance.$message({
										 message: _columns[i].label + '不能为空！',
										 type: 'warning'
									 });

									 return;
								 }
							 }
						 }

						var _changeColumn = findChangeColumn(_columns, 'special');

						_changeColumn.disabled = false;

						 done();
						 
                    },
					beforeRemove:function(row, done){
						//主席和观众不能删除
						if(row.special === '主席' || row.special === '观众'){
							this.$message({
								message: '主席和观众属性不能删除！',
								type: 'warning'
							});
							return;
						}

						done();
					},
					beforeRemoveAll:function(rows, done){
						//过滤主席和观众
						for(var i=rows.length-1;i>=0;i--){
							if(rows[i].special === '主席' || rows[i].special === '观众'){
								rows.splice(i,1);
							}
						}

						done();
					},
					//联动--主席和观众禁用
					beforeEdit:function(row, done){
						var instance = this;
						var _columns = instance.role.columns;
						var _changeColumn = findChangeColumn(_columns, 'special');

						if(row.special === '主席' || row.special === '观众'){
							_changeColumn.disabled = true;
						}else{
							_changeColumn.disabled = false;
						}

						done();
					},
					cancelEdit:function(row, done){
						var instance = this;
						var _columns = instance.role.columns;
						var _changeColumn = findChangeColumn(_columns, 'special');

						_changeColumn.disabled = false;

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
	};
	
	return groupList;
});

