define([
    'text!' + window.APPPATH + 'group/resource/layout/page-group-param-layout.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'bvc2-header',
    'bvc2-group-param-aside',
    'bvc2-system-table-base',
    'bvc2-dialog-auto-layout',
	'extral'
], function(tpl, ajax, config, commons, Vue){
	
	var pageId = "page-group-param-layout";
	
	var init = function(p){
		
		//设置标题
		commons.setTitle(pageId);
		
		var $page = document.getElementById(pageId);
		$page.innerHTML = tpl;
		
		var v_layout = new Vue({
			el:'#' + pageId + '-wrapper',
			data:{
				menurouter: false,
				shortCutsRoutes:commons.data,
				active:"/page-group-list",
				header:commons.getHeader(0),
				side:{
					active:'0-3'
				},
				group:p,
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
						editOK:false,
						width:'100'
					}],
					load:'/device/group/param/query/layout/' + p.id,
					save:'/device/group/param/save/layout/' + p.id,
					update:'/device/group/param/update/layout',
					remove:'/device/group/param/remove/layout',
					removebatch:'/device/group/param/remove/all/layout',
					pk:'id',
				}
			},
			
			methods:{
				
				rowChanged:function(rows){
					var instance = this;
					
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
					}
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
					instance.table.columns[1].editOK = true;
					done();
				},
				
				beforeSave:function(row, done){
					var instance = this,
						_columns = instance.table.columns;
						
					//空校验
					for(var i=0; i<_columns.length; i++){
						if(_columns[i].type == "simple"){
							if(row[_columns[i].prop] == null || row[_columns[i].prop] == ""){
								instance.$message({
									message: _columns[i].label + '不能为空',
									type: 'warning'
								})
								return;
							}
						}else{
							if(!_columns[i].editOK){
								instance.$message({
									message: _columns[i].label + '不能为空',
									type: 'warning'
								})
								return;
							}
						}
					}
					
					done();
					
				},
				
				beforeEditSave:function(row,done){
					var instance = this,
					_columns = instance.table.columns;

					for(var i=0; i<_columns[i].length; i++){
						if(_columns[i].type == 'simple'){
							if(row[_columns[i].prop] == null || row[_columns[i].prop] == ''){
								instance.$message({
									message: _columns[i].label + '不能为空',
									type: 'warning'
								})
							}
							return;
						}else{
							_columns[i].editOK = true;
							return;
						}
					}

					done();

				}
			},
		});
	};
	
	var destroy = function(){
		
	};
	
	var groupList = {
		path:'/' + pageId + '/:id',
		component:{
			template:'<div id="' + pageId + '" class="page-wrapper"></div>',			
		},
		init:init,
		destroy:destroy
	};
	
	return groupList;
	
})