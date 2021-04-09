define([
    'text!' + window.APPPATH + 'component/bvc2-transfer-authority-target/bvc2-transfer-authority-target.html',
    'vue',
    'element-ui',
], function(tpl, Vue){
	
	var bvc2TransferAuthorityTarget = 'bvc2-transfer-authority-target';
	
	Vue.component(bvc2TransferAuthorityTarget, {
		props:['members', 'values'],
		template:tpl,
		data:function(){
			return {
				value:this.values,
				data:this.members,
				defaultProps:{
					key: 'id',
					label: 'videoName'
				}
			}
		},
		methods:{
			getSelect:function(){
				var value = this.value;
				
				var target = [];
				if(!value) return target;
				for(var i=0; i<value.length; i++){
					for(var j=0; j<this.data.length; j++){
						if(value[i] == this.data[j].id){
							target.push(this.data[j]);
							break;
						}
					}
				}
				return target;
			},
			handleChange: function(value, direction, movedKeys){
				if(direction === 'right'){

				}
			},
			leftCheckChange: function(check, change){

			}
		},
		//渲染开始之前
		created:function(){
			
		}
	});
});