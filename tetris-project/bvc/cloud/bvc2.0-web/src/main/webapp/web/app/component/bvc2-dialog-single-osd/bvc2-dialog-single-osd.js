define([
	'text!' + window.APPPATH + 'component/bvc2-dialog-single-osd/bvc2-dialog-single-osd.html',
	'restfull',
    'vue',
    'element-ui'
], function(tpl, ajax, Vue){
	
	//组件名称
	var bvc2DialogSetGroupTemplate = 'bvc2-dialog-single-osd';

	Vue.component(bvc2DialogSetGroupTemplate, {
		template:tpl,
		data:function(){
			return {
				visible:false,
				table:{
					data:[],
					page:{
						total:0,
						currentPage:0,
						pageSize:20
					},
					currentRow:''
				},
				initCurrent:'',
				buffer:'',
				loading:false
			}
		},
		methods:{
			setBuffer:function(buffer){
				var self = this;
				self.buffer = buffer;
			},
			getBuffer:function(){
				var self = this;
				return self.buffer;
			},
			open:function(currentRow){
				var self = this;
				self.initCurrent = currentRow;
				self.load(1);
				self.visible = true;
			},
			load:function(currentPage){
				var self = this;
				self.table.data.splice(0, self.table.data.length);
				ajax.post('/monitor/osd/load/all', {
					currentPage:currentPage,
					pageSize:self.table.page.pageSize
				}, function(data){
					var currentRow = null;
					var total = data.total;
					var rows = data.rows;
					if(rows && rows.length){
						for(var i=0; i<rows.length; i++){
							self.table.data.push(rows[i]);
							if(rows[i].id === self.initCurrent){
								currentRow = rows[i];
							}
						}
					}
					if(currentRow){
						self.$refs.table.setCurrentRow(currentRow);
					}
					self.table.page.total = total;
				});
			},
			handleSizeChange:function(pageSize){
				var self = this;
				self.table.page.pageSize = pageSize;
				self.load(1);
			},
			handleCurrentChange:function(currentPage){
				var self = this;
				self.load(currentPage);
			},
			handleClose:function(){
				var self = this;
				self.table.data.splice(0, self.table.data.length);
				self.table.currentRow = '';
				self.initCurrent = '';
				self.buffer = '';
				self.loading = false;
				self.visible = false;
			},
			currentChange:function(currentRow, oldRow){
				var self = this;
				if(!currentRow) return;
				self.table.currentRow = currentRow;
				self.initCurrent = currentRow.id;
			},
			handleCommit:function(){
				var self = this;
				self.loading = true;
				var done = function(){
					self.visible = false;
				};
				var endLoading = function(){
					self.loading = false;
				};
				self.$emit('selected-osd', self.table.currentRow, done, endLoading);
			}

		}
	});
	
});