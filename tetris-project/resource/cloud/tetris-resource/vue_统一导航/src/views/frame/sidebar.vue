<template>
    <aside :class="[isCollapse? 'collapseMenu' : 'unCollapseMenu']">
		<!--导航菜单-->
		<el-menu v-if="menurouter" class="el-menu-vertical-demo" @open="handleopen" @close="handleclose" @select="handleselect" :collapse="isCollapse"  :default-active="active"
			 router background-color="#37404f" text-color="#bfcfd9" active-text-color="#ffffff">
			<sidebarItem v-for="item in routes" :key="item.path" :item="item" :menurouter="menurouter"></sidebarItem>
		</el-menu>
		<el-menu v-else class="el-menu-vertical-demo" @open="handleopen" @close="handleclose" @select="handleselect" :collapse="isCollapse"  :default-active="active"
			 background-color="#37404f" text-color="#bfcfd9" active-text-color="#ffffff">
			<sidebarItem v-for="item in routes" :key="item.path" :item="item" :menurouter="menurouter"></sidebarItem>
		</el-menu>
	</aside>
</template>

<script type="text/ecmascript-6">
	import bus from '../../router/util/menuMsgBus.js'
	import sidebarItem from './sidebarItem'

	export default {
		name: 'sidebar',
		components: { sidebarItem },
		props: {
			routes: {
				type: Array
			},
			active: {
				type: String
			},
			menurouter: {
				type: Boolean,
				required: true
			}
		},

		data() {
			return{
				isCollapse: false,
				logo:{
					img:'~assets/bvclogo.png',
				},
			}
		},

		methods: {
			handleopen() {
					// console.log('handleopen');
			},
			handleclose() {
					// console.log('handleclose');
			},

			handleselect: function (a, b) {
			},
		},

		mounted(){
			var self = this;
			bus.$on("collapseMenuEvent", function(msg){
				//console.log("collapseMenuEvent isCollapse=" + msg);
				self.isCollapse = msg;
			});
		}


	}
</script>
<style>
	.el-menu-vertical-demo {
 		height: 100%;
	}

	.el-menu-vertical-demo:not(.el-menu--collapse) {
    	width: 239px;
	}
	.el-menu--collapse  span,
	.el-menu--collapse  i.el-submenu__icon-arrow
	{
		height: 0;
		width: 0;
		overflow: hidden;
		visibility: hidden;
		display: inline-block;
	}
	.el-submenu-demo {
	 width: 100%;
	}
	.unCollapseMenu {
		width: 240px;
	}
	.collapseMenu {
		width: 65px;
	}
</style>
