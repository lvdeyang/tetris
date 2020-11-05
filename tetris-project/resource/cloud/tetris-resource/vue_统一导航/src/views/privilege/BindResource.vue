<template>
  <section>

    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left; width:100%;">
      <el-tab-pane label="绑定资源用户" name="BindResource"></el-tab-pane>
      <el-tab-pane label="绑定虚拟资源" name="BindVirtualResource"></el-tab-pane>
    </el-tabs>

    <el-card style="float:left;margin-top:10px;width:20%;font-size: 18px;" body-style="padding:0px">
      <el-table ref="roleTable" :data="roles" highlight-current-row v-loading="roleTableLoading" @current-change="handleRoleTableRowChange" style="width: 100%;">
        <el-table-column prop="name" label="角色名" sortable>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card style="float:left;margin-left:50px;margin-top:10px;width:70%;" body-style="padding:0px">

      <div slot="header" class="clearfix">
        <!--
        <span style="float: left;font-size: 18px;">设备资源</span>
        <el-select v-model="filters.bindType" placeholder="选择绑定状态" style="float: left;margin-left: 50px;width:120px;">
          <el-option v-for="item in bindTypeOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>

                -->
        <span style="float: left;font-size: 14px; height: 34px;line-height: 34px;">选择分组：</span>
        <el-select v-model="folderTreeSelected.label" size="medium" placeholder="选择分组"  style="float: left; width: 200px" filterable :filter-method="dataFilter" clearable @clear="clearHandle" ref="selectTree">
          <el-option class="tree-select" :value="folderTreeSelected.value" :label="folderTreeSelected.label" style="width: 200px;height: auto;overflow: hidden;">
            <el-tree
              ref="tree"
              :data="treeData"
              :props="defaultProps"
              :default-expand-all="defaultExpandAll"
              :filter-node-method="filterNode"
              :expand-on-click-node="expandOnClickNode"
              @node-click="handleNodeClick"
              ></el-tree>
          </el-option>
        </el-select>

        <el-select v-model="filters.deviceModel" size="medium" placeholder="选择资源类型" style="float: left;margin-left:10px;width:120px;">
          <el-option v-for="item in deviceModelOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>

        <el-input size="medium" v-model="filters.keyword" style="float: left;margin-left: 10px;width:200px;" placeholder="关键字" ></el-input>
        <el-button size="small" type="info" @click="getResources" style="float: left;margin-left: 10px">查询设备</el-button>
        <el-button size="small" type="info" @click="getUsers" style="float: left;">查询用户</el-button>
        <!--<el-button type="info" @click="getResources" style="float: left;margin-left: 30px;">查询</el-button>-->

      </div>

      <!--资源列表-->
      <el-table :data="resources" v-show="resourceTableShow" v-loading="resourceTableLoading" style="width: 100%;">
        <el-table-column type="index" width="100"></el-table-column>
        <el-table-column prop="name" label="名称" width="200" sortable>
        </el-table-column>
        <el-table-column prop="deviceModel" label="资源类型" width="200" sortable>
        </el-table-column>
        <!--<el-table-column prop="bundleId" label="资源ID" width="350"  sortable>-->
        <!--</el-table-column>-->
        <el-table-column prop="username" label="设备账号" width="270" >
        </el-table-column>
        <el-table-column width="100" :render-header="renderCheckReadHeader" >
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasReadPrivilege" @change="handleCheckReadChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column width="100" :render-header="renderCheckWriteHeader" >
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasWritePrivilege" @change="handleCheckWriteChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column width="100" :render-header="renderCheckCloudHeader" >
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasCloudPrivilege" @change="handleCheckCloudChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

      </el-table>

      <!--用户列表-->
      <el-table :data="users" v-show="userTableShow" v-loading="userTableLoading" style="width: 100%;">
        <el-table-column type="index" width="100"></el-table-column>
        <el-table-column prop="name" label="名称" width="200" sortable>
        </el-table-column>
        <el-table-column prop="userNo" label="用户编号" width="270" >
        </el-table-column>
        <el-table-column width="150" :render-header="renderCheckReadHeader">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasReadPrivilege" @change="handleCheckReadChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column width="150" :render-header="renderCheckWriteHeader">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasWritePrivilege" @change="handleCheckWriteChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column width="150" :render-header="renderCheckCloudHeader">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasCloudPrivilege" @change="handleCheckCloudChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column width="150" :render-header="renderCheckHJHeader">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasHJPrivilege" @change="handleCheckHJChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column width="150" :render-header="renderCheckZKHeader">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasZKPrivilege" @change="handleCheckZKChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column width="150" :render-header="renderCheckHYHeader">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasHYPrivilege" @change="handleCheckHYChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

      </el-table>

      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-button size="small" type="primary" @click="submitPrivilege">提交</el-button>
        <el-input size="small" v-model="filters.countPerPage" style="float: right;margin-right: 30px;width:200px;" placeholder="单页显示数量,默认20" ></el-input>
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentPageChange" :page-size="countPerPage" :current-page="pageNum" :total="total" style="float:right;">
        </el-pagination>
      </el-col>

    </el-card>

  </section>
</template>

<script type="text/ecmascript-6">
  import { getAllRoles, getDeviceModels, getBundlesOfRole, submitBundlePrivilege, getUsersOfRole, submitUserresPrivilege, initFolderTreeWithOutMember } from '../../api/api'

  export default {
    data:function () {
    return {
      treeData: [],
      folderTreeSelected:{
        value:'',
        label:''
      },
      defaultProps:{
        children:'children',
        label:'name'
      },
      defaultExpandAll: false,
      expandOnClickNode: false,
      activeTabName: 'BindResource',
      deviceModelOptions:[],
      roles: [
      ],
      currentRoleRow: null,
      resources: [],
      bindTypeOptions: [
        {
          value: 'all',
          label: '全部'
        },
        {
          value: 'binded',
          label: '已绑定'
        },
        {
          value: 'unbinded',
          label: '未绑定'
        }
      ],
      deviceModelOptions: [],
      filters: {
        deviceModel: '',
        keyword: '',
        bindType: 'all',
        countPerPage: ''
      },
      total: 0,
      pageNum: 1,
      countPerPage: 20,
      roleTableLoading: false,
      resourceTableLoading: false,
      prevReadChecks: [], // 之前的录制权限
      prevWriteChecks: [], // 之前的点播权限
      prevCloudChecks: [], // 之前的云台权限
      prevHJChecks: [],
      prevZKChecks: [],
      prevHYChecks: [],
      readChecks: [], // 新的录制权限
      writeChecks: [], // 新的点播权限
      cloudChecks: [], // 新的云台权限
      HJchecks: [],
      ZKchecks:[],
      HYchecks:[],
      detailDialogVisible: false,
      detailInfos: [],
      users: [],
      userTableLoading: false,
      resourceTableShow: true,
      userTableShow: false,
      checkReadAll: false,
      checkWriteAll: false,
      checkCloudAll: false,
      checkHJAll: false,
      checkZKAll: false,
      checkHYAll: false
    }
  },
  methods: {
    dataFilter:function(value){
       this.$refs.tree.filter(value);
    },
    filterNode:function(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
    },
    initTree: function(keepExpand) {
      initFolderTreeWithOutMember().then(res => {
        if (res.errMsg) {
        this.$message({
          message: res.errMsg,
          type: 'error'
        })
      } else {
        // var node1 = new Object()
        // node1.id = -2
        // node1.name = '请选择分组'
        // this.treeData.push(node1)
        // res.tree.shift()
        this.treeData = this.treeData.concat(res.tree)
        //this.treeData.push()
        console.log(JSON.stringify(this.treeData))
      }
    })
  },

  handleNodeClick:function(node){
    this.folderTreeSelected.value = node.id;
    this.folderTreeSelected.label = node.name;
    this.$refs.selectTree.blur();
  },

  clearHandle:function(){
      this.folderTreeSelected.value = "",
      this.folderTreeSelected.label = "",
      this.$refs.tree.filter("");
  },

  handleTabClick (tab, event){
    if (tab.name !== 'BindResource') {
      this.$router.push('/' + tab.name)
    }
  },
  handleRoleTableRowChange: function (val) {
    this.currentRoleRow = val
    // TODO 选中角色，查询资源
    if (this.resourceTableShow) {
      this.getResources()
    } else if (this.userTableShow) {
      this.getUsers()
    }
  },
  renderCheckReadHeader: function (h, data) { // 自定义特殊表格头单元
    return (
      <el-checkbox v-model={this.checkReadAll} onChange={this.handleCheckReadAllChange}>录制</el-checkbox>
  )
  },
  renderCheckWriteHeader: function (h, data) { // 自定义特殊表格头单元
    return (
      <el-checkbox v-model={this.checkWriteAll} onChange={this.handleCheckWriteAllChange}>点播</el-checkbox>
  )
  },
  renderCheckCloudHeader: function (h, data) { // 自定义特殊表格头单元
    return (
      <el-checkbox v-model={this.checkCloudAll} onChange={this.handleCheckCloudAllChange}>云台</el-checkbox>
  )
  },
  renderCheckHJHeader: function (h, data) { // 自定义特殊表格头单元
    return (
      <el-checkbox v-model={this.checkHJAll} onChange={this.handleCheckHJAllChange}>呼叫</el-checkbox>
  )
  },
  renderCheckZKHeader: function (h, data) { // 自定义特殊表格头单元
    return (
      <el-checkbox v-model={this.checkZKAll} onChange={this.handleCheckZKAllChange}>指挥</el-checkbox>
  )
  },
  renderCheckHYHeader: function (h, data) { // 自定义特殊表格头单元
    return (
      <el-checkbox v-model={this.checkHYAll} onChange={this.handleCheckHYAllChange}>会议</el-checkbox>
  )
  },
  handleCheckReadAllChange: function (val) {
    if (this.resourceTableShow) { // 设备
      if (val) {
        for (let resource of this.resources) {
          this.readChecks.push(resource.bundleId)
          resource.hasReadPrivilege = true
        }
      } else {
        this.readChecks = []
        for (let resource of this.resources) {
          resource.hasReadPrivilege = false
        }
      }
    } else if (this.userTableShow) { // 用户
      if (val) {
        for (let user of this.users) {
          this.readChecks.push(user.userNo)
          user.hasReadPrivilege = true
        }
      } else {
        this.readChecks = []
        for (let user of this.users) {
          user.hasReadPrivilege = false
        }
      }
    }
  },
  handleCheckWriteAllChange: function (val) {
    if (this.resourceTableShow) { // 设备
      if (val) {
        for (let resource of this.resources) {
          this.writeChecks.push(resource.bundleId)
          resource.hasWritePrivilege = true
        }
      } else {
        this.writeChecks = []
        for (let resource of this.resources) {
          resource.hasWritePrivilege = false
        }
      }
    } else if (this.userTableShow) { // 用户
      if (val) {
        for (let user of this.users) {
          this.writeChecks.push(user.userNo)
          user.hasWritePrivilege = true
        }
      } else {
        this.writeChecks = []
        for (let user of this.users) {
          user.hasWritePrivilege = false
        }
      }
    }
  },
  handleCheckCloudAllChange:function (val){
    if(this.resourceTableShow) {   //设备
      if(val) {
        for(let resource of this.resources){
          this.cloudChecks.push(resource.bundleId)
          resource.hasCloudPrivilege = true;
        }
      }else{
        this.cloudChecks = []
        for(let resource of this.resources){
          resource.hasCloudPrivilege = false
        }
      }
    }else if (this.userTableShow) { // 用户
      if (val) {
        for (let user of this.users) {
          this.cloudChecks.push(user.userNo)
          user.hasCloudPrivilege = true
        }
      } else {
        this.cloudChecks = []
        for (let user of this.users) {
          user.hasCloudPrivilege = false
        }
      }
    }
  },
  handleCheckHJAllChange: function (val) {
    if (this.userTableShow) { // 用户
      if (val) {
        for (let user of this.users) {
          this.HJchecks.push(user.userNo)
          user.hasHJPrivilege = true
        }
      } else {
        this.HJchecks = []
        for (let user of this.users) {
          user.hasHJPrivilege = false
        }
      }
    }
  },
  handleCheckZKAllChange: function (val) {
    if (this.userTableShow) { // 用户
      if (val) {
        for (let user of this.users) {
          this.ZKchecks.push(user.userNo)
          user.hasZKPrivilege = true
        }
      } else {
        this.ZKchecks = []
        for (let user of this.users) {
          user.hasZKPrivilege = false
        }
      }
    }
  },
  handleCheckHYAllChange: function (val) {
    if (this.userTableShow) { // 用户
      if (val) {
        for (let user of this.users) {
          this.HYchecks.push(user.userNo)
          user.hasHYPrivilege = true
        }
      } else {
        this.HYchecks = []
        for (let user of this.users) {
          user.hasHYPrivilege = false
        }
      }
    }
  },
  handleCheckReadChange: function (row) {
    if (this.resourceTableShow) {
      if (row.hasReadPrivilege) {
        this.readChecks.push(row.bundleId)
        // 全选判断
        if (this.readChecks.length === this.resources.length) {
          this.checkReadAll = true
        }
      } else {
        this.readChecks.splice(this.readChecks.indexOf(row.bundleId), 1)
        this.checkReadAll = false
      }
    } else if (this.userTableShow) {
      if (row.hasReadPrivilege) {
        this.readChecks.push(row.userNo)
        // 全选判断
        if (this.readChecks.length === this.users.length) {
          this.checkReadAll = true
        }
      } else {
        this.readChecks.splice(this.readChecks.indexOf(row.userNo), 1)
        this.checkReadAll = false
      }
    }
  },
  handleCheckWriteChange: function (row) {
    if (this.resourceTableShow) {
      if (row.hasWritePrivilege) {
        this.writeChecks.push(row.bundleId)
        if (this.writeChecks.length === this.resources.length) {
          this.checkWriteAll = true
        }
      } else {
        this.writeChecks.splice(this.writeChecks.indexOf(row.bundleId), 1)
        this.checkWriteAll = false
      }
    } else if (this.userTableShow) {
      if (row.hasWritePrivilege) {
        this.writeChecks.push(row.userNo)
        // 全选判断
        if (this.writeChecks.length === this.users.length) {
          this.checkWriteAll = true
        }
      } else {
        this.writeChecks.splice(this.writeChecks.indexOf(row.userNo), 1)
        this.checkWriteAll = false
      }
    }
  },
  handleCheckCloudChange: function (row) {
    if (this.resourceTableShow) {
      if (row.hasCloudPrivilege) {
        this.cloudChecks.push(row.bundleId)
        if (this.cloudChecks.length === this.resources.length) {
          this.checkCloudAll = true
        }
      } else {
        this.cloudChecks.splice(this.cloudChecks.indexOf(row.bundleId), 1)
        this.checkCloudAll = false
      }
    } else if (this.userTableShow) {
      if (row.hasCloudPrivilege) {
        this.cloudChecks.push(row.userNo)
        // 全选判断
        if (this.cloudChecks.length === this.users.length) {
          this.checkCloudAll = true
        }
      } else {
        this.cloudChecks.splice(this.cloudChecks.indexOf(row.userNo), 1)
        this.checkCloudAll = false
      }
    }
  },
  handleCheckHJChange: function (row) {
    if (this.userTableShow) {
      if (row.hasHJPrivilege) {
        this.HJchecks.push(row.userNo)
        // 全选判断
        if (this.HJchecks.length === this.users.length) {
          this.checkHJAll = true
        }
      } else {
        this.HJchecks.splice(this.HJchecks.indexOf(row.userNo), 1)
        this.checkHJAll = false
      }
    }
  },
  handleCheckZKChange: function (row) {
    if (this.userTableShow) {
      if (row.hasZKPrivilege) {
        this.ZKchecks.push(row.userNo)
        // 全选判断
        if (this.ZKchecks.length === this.users.length) {
          this.checkZKAll = true
        }
      } else {
        this.ZKchecks.splice(this.ZKchecks.indexOf(row.userNo), 1)
        this.checkZKAll = false
      }
    }
  },
  handleCheckHYChange: function (row) {
    if (this.userTableShow) {
      if (row.hasHYPrivilege) {
        this.HYchecks.push(row.userNo)
        // 全选判断
        if (this.HYchecks.length === this.users.length) {
          this.checkHYAll = true
        }
      } else {
        this.HYchecks.splice(this.HYchecks.indexOf(row.userNo), 1)
        this.checkHYAll = false
      }
    }
  },
  // 获取角色列表
  getRoles: function () {
    let param = {
    }

    this.roleTableLoading = true
    getAllRoles(param).then(res => {
      if (res.errMsg) {
      this.$message({
        message: res.errMsg,
        type: 'error'
      })
    } else {
      this.roles = res.roles
    }

    this.roleTableLoading = false
  })
  },
  // 获取资源类型
  getDeviceModels: function () {
    getDeviceModels().then(res => {
      if (!res.errMsg && res.deviceModels) {
      this.deviceModelOptions.push({
        value: '',
        label: '全部类型'
      })
      for (let deviceModel of res.deviceModels) {
        let deviceModelOption = {
          value: deviceModel,
          label: deviceModel
        }
        this.deviceModelOptions.push(deviceModelOption)
      }
    }
  })
  },
  // 获取资源列表
  getResources: function () {

    if(!this.resourceTableShow){
      this.pageNum = 1;
    }

    if (!this.currentRoleRow) {
      this.$message({
        message: '请先选择角色',
        type: 'error'
      })
      return
    }
    this.resourceTableShow = true
    this.userTableShow = false
    this.prevReadChecks = []
    this.prevWriteChecks = []
    this.prevCloudChecks = []
    this.readChecks = []
    this.writeChecks = []
    this.cloudChecks = []
    this.checkReadAll = false
    this.checkWriteAll = false
    this.checkCloudAll = false
    this.checkHJAll = false
    this.checkZKAll = false
    this.checkHYAll = false
    this.countPerPage = 20
    if (/^[1-9]+[0-9]*]*$/.test(this.filters.countPerPage)) {
      this.countPerPage = parseInt(this.filters.countPerPage)
    }

    if(this.resourceTableShow){
      if(this.countPerPage * (this.pageNum - 1) > this.total){
        this.pageNum = Math.ceil(this.total / this.countPerPage);
      }
    }

    if(this.filters.deviceModel != '' || this.filters.keyword != '' || this.folderTreeSelected.value != ''){
      this.pageNum = 1;
    }

    let param = {
      roleId: this.currentRoleRow.id,
      bindType: this.filters.bindType,
      deviceModel: this.filters.deviceModel,
      keyword: this.filters.keyword,
      folderId: this.folderTreeSelected.value,
      pageNum: this.pageNum,
      countPerPage: this.countPerPage
    }
    this.resourceTableLoading = true

    getBundlesOfRole(param).then((res) => {
      if (res.errMsg) {
      this.$message({
        message: res.errMsg,
        type: 'error'
      })
    } else {
      this.total = res.total
      this.resources = res.resources
      for (let resource of this.resources) {
        if (resource.hasReadPrivilege) {
          this.prevReadChecks.push(resource.bundleId)
          this.readChecks.push(resource.bundleId)
        }
        if (resource.hasWritePrivilege) {
          this.prevWriteChecks.push(resource.bundleId)
          this.writeChecks.push(resource.bundleId)
        }
        if (resource.hasCloudPrivilege) {
          this.prevCloudChecks.push(resource.bundleId)
          this.cloudChecks.push(resource.bundleId)
        }
      }

      // 全选判断
      if (this.readChecks.length === this.resources.length) {
        this.checkReadAll = true
      }
      if (this.writeChecks.length === this.resources.length) {
        this.checkWriteAll = true
      }
      if (this.cloudChecks.length === this.resources.length) {
        this.checkCloudAll = true
      }
    }

    this.resourceTableLoading = false
  })
  },
  // 资源列表分页
  handleCurrentPageChange (val) {
    this.pageNum = val
    if (this.resourceTableShow) {
      this.getResources()
    } else {
      this.getUsers()
    }
  },

  getUsers: function () {

    if(this.resourceTableShow){
      this.pageNum = 1;
    }

    if (!this.currentRoleRow) {
      this.$message({
        message: '请先选择角色',
        type: 'error'
      })
      return
    }

    this.resourceTableShow = false
    this.userTableShow = true
    this.prevReadChecks = []
    this.prevWriteChecks = []
    this.prevCloiudChecks = []
    this.readChecks = []
    this.writeChecks = []
    this.cloudChecks = []
    this.prevHJChecks = []
    this.prevZKChecks = []
    this.prevHYChecks = []
    this.HJchecks = []
    this.ZKchecks = []
    this.HYchecks = []
    this.checkReadAll = false
    this.checkWriteAll = false
    this.checkCloudAll = false
    this.checkHJAll = false
    this.checkZKAll = false
    this.checkHYAll = false
    this.countPerPage = 20
    if (/^[1-9]+[0-9]*]*$/.test(this.filters.countPerPage)) {
      this.countPerPage = parseInt(this.filters.countPerPage)
    }

    if(!this.resourceTableShow){
      if(this.countPerPage * (this.pageNum - 1) > this.total){
        this.pageNum = Math.ceil(this.total / this.countPerPage);
      }
    }

    if(this.filters.keyword != '' || this.folderTreeSelected.value != ''){
      this.pageNum = 1;
    }

    let param = {
      roleId: this.currentRoleRow.id,
      keyword: this.filters.keyword,
      folderId: this.folderTreeSelected.value,
      pageNum: this.pageNum,
      countPerPage: this.countPerPage
    }
    this.userTableLoading = true
    getUsersOfRole(param).then((res) => {
      if (res.errMsg) {
      this.$message({
        message: res.errMsg,
        type: 'error'
      })
    } else {
      this.total = res.total
      this.users = res.users
      for (let user of this.users) {
        if (user.hasReadPrivilege) {
          this.prevReadChecks.push(user.userNo)
          this.readChecks.push(user.userNo)
        }
        if (user.hasWritePrivilege) {
          this.prevWriteChecks.push(user.userNo)
          this.writeChecks.push(user.userNo)
        }
        if (user.hasCloudPrivilege) {
          this.prevCloudChecks.push(user.userNo)
          this.cloudChecks.push(user.userNo)
        }
        if (user.hasHJPrivilege) {
          this.prevHJChecks.push(user.userNo)
          this.HJchecks.push(user.userNo)
        }
        if (user.hasZKPrivilege) {
          this.prevZKChecks.push(user.userNo)
          this.ZKchecks.push(user.userNo)
        }
        if (user.hasHYPrivilege) {
          this.prevHYChecks.push(user.userNo)
          this.HYchecks.push(user.userNo)
        }
      }

      // 全选判断
      if (this.readChecks.length === this.users.length) {
        this.checkReadAll = true
      }
      if (this.writeChecks.length === this.users.length) {
        this.checkWriteAll = true
      }
      if (this.cloudChecks.length === this.users.length) {
        this.checkCloudAll = true
      }
      if (this.HJchecks.length === this.users.length) {
        this.checkHJAll = true
      }
      if (this.ZKchecks.length === this.users.length) {
        this.checkZKAll = true
      }
      if (this.HYchecks.length === this.users.length) {
        this.checkHYAll = true
      }
    }

    this.userTableLoading = false
  })
  },
  // 提交资源权限
  submitPrivilege: function () {
    if (this.resourceTableShow) {
      let param = {
        roleId: this.currentRoleRow.id,
        prevReadChecks: this.prevReadChecks.join(','),
        prevWriteChecks: this.prevWriteChecks.join(','),
        prevCloudChecks: this.prevCloudChecks.join(','),
        readChecks: this.readChecks.join(','),
        writeChecks: this.writeChecks.join(','),
        cloudChecks: this.cloudChecks.join(',')
      }

      this.resourceTableLoading = true
      submitBundlePrivilege(param).then((res) => {
        if (res.errMsg) {
        this.$message({
          message: res.errMsg,
          type: 'error'
        })
      } else {
        this.$message({
          message: '提交成功',
          type: 'success'
        })
        // 更新prevChecks
        this.prevReadChecks = [].concat(this.readChecks)
        this.prevWriteChecks = [].concat(this.writeChecks)
        this.prevCloudChecks = [].concat(this.cloudChecks)
      }
      this.resourceTableLoading = false
    })
  } else if (this.userTableShow) {
    let param = {
      roleId: this.currentRoleRow.id,
      prevReadChecks: this.prevReadChecks.join(','),
      prevWriteChecks: this.prevWriteChecks.join(','),
      prevCloudChecks: this.prevCloudChecks.join(','),
      prevHJChecks: this.prevHJChecks.join(','),
      prevZKChecks: this.prevZKChecks.join(','),
      prevHYChecks: this.prevHYChecks.join(','),
      readChecks: this.readChecks.join(','),
      writeChecks: this.writeChecks.join(','),
      cloudChecks: this.cloudChecks.join(','),
      hjChecks: this.HJchecks.join(','),
      zkChecks: this.ZKchecks.join(','),
      hyChecks: this.HYchecks.join(',')
    }
    this.userTableLoading = true
    submitUserresPrivilege(param).then((res) => {
      if (res.errMsg) {
      this.$message({
        message: res.errMsg,
        type: 'error'
      })
    } else {
      this.$message({
        message: '提交成功',
        type: 'success'
      })
      // 更新prevChecks
      this.prevReadChecks = [].concat(this.readChecks)
      this.prevWriteChecks = [].concat(this.writeChecks)
      this.prevCloudChecks = [].concat(this.cloudChecks)
      this.prevHJChecks = [].concat(this.HJchecks)
      this.prevZKChecks = [].concat(this.ZKchecks)
      this.prevHYChecks = [].concat(this.HYchecks)
    }
    this.userTableLoading = false
  })
  }
  }
  },
  mounted () {
    var self = this;
    self.$nextTick(function(){
      self.$parent.$parent.$parent.$parent.$parent.setActive('/BindResource');
    });
    self.getRoles();
    self.getDeviceModels();
    // this.getRoles()
    self.initTree();
  }
  }

</script>

<style>
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .tree-select {
    font-weight: 500 !important;
    padding: 0px 0px !important;
  }

</style>
