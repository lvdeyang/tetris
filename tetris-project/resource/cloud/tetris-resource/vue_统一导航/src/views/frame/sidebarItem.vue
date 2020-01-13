<template>
	<div v-if="menurouter">
	  <div class="aaa" v-if="item.children">
		<template v-if="item.children.length == 0">
			<el-menu-item :index="item.path">
			  <i :class="item.meta.icon"></i>
			  <span slot="title" style="padding-left:4px">{{item.name}}</span>
			</el-menu-item>
		</template>

		<el-submenu v-else :index="item.path">
		  <template slot="title" >
			<i :class="item.meta.icon"></i>
			<span slot="title" style="padding-left:4px">{{item.name}}</span>
		  </template>

		  <template v-for="child in item.children" v-if="!child.hidden">
			<sidebar-item v-if="child.children&&child.children.length>0" :item="child" :menurouter="menurouter"/>
			<el-menu-item v-else :index="child.path">
				<i :class="child.meta.icon"></i>
			  <span slot="title" style="padding-left:4px">{{child.name}}</span>
			</el-menu-item>
		  </template>
		</el-submenu>
	  </div>
	  <el-menu-item v-else :index="item.path">
		<i :class="item.meta.icon"></i>
		<span slot="title" style="padding-left:4px">{{item.name}}</span>
	  </el-menu-item>
	</div>
	<!-- <div v-else>
		<div class="bbb" v-if="item.children">
		<template v-if="item.children.length == 0">
			<el-menu-item :index="item.path" @click="menuClick(item)">
			  <i :class="item.meta.icon"></i>
			  <span slot="title" style="padding-left:4px">{{item.name}}</span>
			</el-menu-item>
		</template>

		<el-submenu v-else :index="item.path">
		  <template slot="title" >
			<i :class="item.meta.icon"></i>
			<span slot="title" style="padding-left:4px">{{item.name}}</span>
		  </template>

		  <template v-for="child in item.children" :key="child.path" v-if="!child.hidden">
			<sidebar-item v-if="child.children&&child.children.length>0" :item="child" :menurouter="menurouter"/>
			<el-menu-item v-else :index="child.path" @click="menuClick(child)">
				<i :class="child.meta.icon"></i>
			  <span slot="title" style="padding-left:4px">{{child.name}}</span>
			</el-menu-item>
		  </template>
		</el-submenu>
	  </div>
	  <el-menu-item v-else :index="item.path" @click="menuClick(item)">
		<i :class="item.meta.icon"></i>
		<span slot="title" style="padding-left:4px">{{item.name}}</span>
	  </el-menu-item>
	</div> -->
	<div v-else>
  		<div class="bbb" v-if="item.sub">
        <template v-if="item.sub.length == 0">
          <el-menu-item :index="item.path" @click="menuClick(item)">
            <i :class="item.icon" :style="item.style"></i>
            <span slot="title" style="padding-left:4px">{{item.title}}</span>
          </el-menu-item>
        </template>

        <el-submenu v-else :index="item.path">
          <template slot="title" >
          <i :class="item.icon" :style="item.style"></i>
            <span slot="title" style="padding-left:4px">{{item.title}}</span>
          </template>

          <template v-for="child in item.sub">
            <sidebar-item v-if="child.sub&&child.sub.length>0" :item="child" :menurouter="menurouter"/>
            <el-menu-item v-else :index="child.path" @click="menuClick(child)">
              <i :class="child.icon" :style="item.style"></i>
              <span slot="title" style="padding-left:4px">{{child.title}}</span>
            </el-menu-item>
          </template>
        </el-submenu>
  	  </div>
  	  <el-menu-item v-else :index="item.path" @click="menuClick(item)">
  		<i :class="item.icon" :style="item.style"></i>
  		  <span slot="title" style="padding-left:4px">{{item.title}}</span>
  	  </el-menu-item>
  	</div>
</template>

<script type="text/ecmascript-6">
import routerConfig from '../../../config/router.config.js'

export default {
  name: 'sidebarItem',
  props: {
    item: {
      type: Object,
      required: true
    },
	menurouter: {
		type: Boolean,
		required: true
	}
  },
  methods: {
    menuClick:function(data){
      window.location.href = data.link;
    }
  }
}
</script>
