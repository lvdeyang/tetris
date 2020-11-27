<template>
  <div style="position:absolute; left:20px; top:20px; right:20px; bottom:20px;">
    <el-row>
      <el-button type="primary" size="mini" @click="createOutLand">创建外域</el-button>
      <el-button type="primary" size="mini" @click="localConfig">本域设置</el-button>
    </el-row>
    <el-table :data="outLandTableData" style="width: 75%" @current-change="currentRowChange" highlight-current-row>
      <el-table-column prop="nodeName" label="外域名称" width="180" sortable>
      </el-table-column>
      <el-table-column prop="password" label="口令" width="300">
      </el-table-column>
      <el-table-column prop="role" label="绑定角色">
      </el-table-column>
      <el-table-column prop="status" label="连接状态" sortable>
      </el-table-column>
      <el-table-column prop="operate" label="操作状态" sortable>
        <template slot-scope="scope">
          <el-tag size="mini" v-if="scope.row.operate == 'ON'">正在连接...</el-tag>
          <el-tag size="mini" v-if="scope.row.operate == 'OFF'" type="warning">正在断开...</el-tag>
          <el-tag size="mini" v-if="scope.row.operate == 'DONE'" type="info">无操作</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="操作">
        <template slot-scope="scope">
          <el-button v-if="scope.row.operate == 'OFF'" type="text" size="small" @click="linkOutland(scope.row.id)">连接</el-button>
          <el-button v-if="scope.row.operate == 'ON'" type="text" size="small" @click="unLinkOutland(scope.row.id)">断开</el-button>
          <el-button type="text" size="small" @click="editOutland(scope.row)">修改</el-button>
          <el-button type="text" size="small" @click="deleteOutland(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <el-pagination background layout=" total , prev, pager, next" :total="1000" style="width:75%">
    </el-pagination> -->
    <!-- 组织树 -->
    <div class="bundle-tree">
      组织结构：
      <el-tree class="filter-tree" :data="treeData" :props="defaultProps" default-expand-all ref="tree2">
      </el-tree>
    </div>
    <el-dialog title="外域设置" :visible.sync="createRegionDialog.visable" width="500px" v-if="createRegionDialog.visable">
      <el-form ref="createRegionDialogForm" label-width="80px" :model="createRegionDialog" size=small :rules="createRegionDialog.rules">
        <el-form-item label="名称" prop="name">
          <el-input v-model="createRegionDialog.name"></el-input>
        </el-form-item>
        <el-form-item label="口令" prop="password">
          <el-input v-model="createRegionDialog.password"></el-input>
        </el-form-item>
        <el-form-item label="绑定角色" prop="roleIds">
          <el-input v-model="createRegionDialog.roleNames" @click.native="handleChangeFolder"></el-input>
          <el-input v-show="false" v-model="createRegionDialog.roleIds"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitCreateRegionForm()">确定</el-button>
          <el-button @click="cancleForm()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 本域设置 -->
    <el-dialog title="本域设置" :visible.sync="localConfigDialog.visable" width="500px">
      <el-form ref="localConfigDialogForm" label-width="80px" :model="localConfigDialog" size=small :rules="localConfigDialog.rules">
        <el-form-item label="名称" prop="name">
          <el-input v-model="localConfigDialog.name"></el-input>
        </el-form-item>
        <el-form-item label="口令" prop="password">
          <el-input v-model="localConfigDialog.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitLocalConfigForm()">确定</el-button>
          <el-button @click="cancleForm1()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 绑定角色对话框 -->
    <el-dialog title="绑定角色" :visible.sync="dialogBindRole.bindRoleDialogTableVisible">

      <el-table ref="roleTable" :data="roleOption" @selection-change="handleSelectionChange" :row-key="roleOption.id" width="100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="名称" width="200"></el-table-column>
      </el-table>
      <el-button type="primary" size="small" @click="handleBindRoleSubmit"><span class="icon-plus" style="position:relative; right:1px;"></span>&nbsp;确定</el-button>

      <!-- <div>弹框</div> -->
    </el-dialog>
  </div>

</template>
<script type="text/ecmascript-6">

import { editInland, queryOutland, createOutload, linkOutland, unLinkOutland, queryInland, editOutland, deleteOutland, getAllRoles, queryOutlandBundle } from '../../api/api'

export default {
  data: function () {
    return {
      outLandTableData: [],
      createRegionDialog: {
        visable: false,
        name: '',
        password: '',
        roleIds: '',
        roleNames: '',
        iscreate: '',
        selectArr: [],
        serNodeId: null,
        rules: {
          'name': [{
            required: true,
            message: '请输入外域名称',
            trigger: 'blur'
          }],
          'password': [{
            required: true,
            message: '请输入口令',
            trigger: 'blur'
          }],
          'roleIds': [{
            required: true,
            message: '请输入角色',
            trigger: 'change'
          }],
        }
      },
      localConfigDialog: {
        visable: false,
        name: '',
        password: '',
        rules: {
          'name': [{
            required: true,
            message: '请输入外域名称',
            trigger: 'blur'
          }],
          'password': [{
            required: true,
            message: '请输入口令',
            trigger: 'blur'
          }]
        }
      },
      dialogBindRole: {
        bindRoleDialogTableVisible: false,
      },
      roleOption: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      treeData: []
    }
  },
  methods: {
    createOutLand () {
      this.createRegionDialog.iscreate = true;
      this.createRegionDialog.visable = true
    },
    localConfig () {
      var self = this;
      this.localConfigDialog.visable = true
      queryInland().then((data) => {
        var { data } = data
        if (data.nodeName) {
          self.localConfigDialog.name = data.nodeName
          self.localConfigDialog.password = data.password
        }
      })
    },
    // 修改外域弹窗
    editOutland (row) {
      var self = this;
      var businessRoles = JSON.parse(row.businessRoles),
        rolesName = [],
        rolesId = [];
      for (var i = 0; i < businessRoles.length; i++) {
        if (businessRoles[i].id != '7' && businessRoles[i].id != '6') {//去掉媒资用户
          rolesName.push(businessRoles[i].name)
          rolesId.push(businessRoles[i].id)
        }
      }

      self.createRegionDialog.iscreate = false;
      self.createRegionDialog.serNodeId = row.id;
      self.createRegionDialog.name = row.nodeName
      self.createRegionDialog.password = row.password
      self.createRegionDialog.roleNames = rolesName.join(',')
      self.createRegionDialog.roleIds = rolesId.join(',')
      self.createRegionDialog.visable = true
    },
    // 外域设置
    submitCreateRegionForm () {
      var self = this;
      self.$nextTick(function () {
        this.$refs['createRegionDialogForm'].validate((valid) => {
          if (valid) {
            if (self.createRegionDialog.iscreate) { //新建

              var params = {
                name: self.createRegionDialog.name,
                password: self.createRegionDialog.password,
                roleIds: self.createRegionDialog.roleIds
              }
              createOutload(params).then((data) => {
                console.log(data)
                if (data.status == 200) {
                  self.$message({
                    type: "success",
                    message: "创建成功！"
                  })
                  self.queryOutland();
                  self.createRegionDialog.visable = false
                }
              })
            } else {//修改
              var params = {
                serNodeId: self.createRegionDialog.serNodeId,
                name: self.createRegionDialog.name,
                password: self.createRegionDialog.password,
                roleIds: self.createRegionDialog.roleIds
              }
              editOutland(params).then((data) => {
                console.log(data)
                if (data.status == 200) {
                  self.$message({
                    type: "success",
                    message: "修改成功！"
                  })
                  self.queryOutland();
                  self.createRegionDialog.visable = false
                }
              })
            }

          } else {
            console.log('error submit!!')
            return false
          }
        })
      })
    },
    // 修改本域
    submitLocalConfigForm () {
      var self = this;
      self.$nextTick(function () {
        this.$refs['localConfigDialogForm'].validate((valid) => {
          if (valid) {
            var params = {
              name: self.localConfigDialog.name,
              password: self.localConfigDialog.password
            }
            editInland(params).then(function (data) {
              self.$message({
                type: "success",
                message: "修改成功！"
              })
              self.localConfigDialog.visable = false
            })
          } else {
            return false
          }
        })
      })

    },
    // 外域列表 
    queryOutland () {
      var self = this;
      queryOutland().then((data) => {
        console.log(data)
        if (data.status == 200) {
          self.outLandTableData = data.data
        }
      })
    },
    //连接外域
    linkOutland (id) {
      var self = this;
      linkOutland({ serNodeId: id }).then(function (data) {
        self.$message({
          type: "success",
          message: "连接成功！"
        })
        self.queryOutland()
        self.localConfigDialog.visable = false
      })
    },
    // 断开外域
    unLinkOutland (id) {
      var self = this;
      unLinkOutland({ serNodeId: id }).then(function (data) {
        self.$message({
          type: "success",
          message: "断开成功！"
        })
        self.queryOutland()
        self.localConfigDialog.visable = false
      })
    },
    // 删除外域
    deleteOutland (id) {
      var self = this;
      deleteOutland({ serNodeId: id }).then(function (data) {
        self.$message({
          type: "success",
          message: "删除成功！"
        })
        self.queryOutland()
      })
    },
    handleChangeFolder: function (role) {
      var self = this;
      self.dialogBindRole.bindRoleDialogTableVisible = true;
      self.$nextTick(function () {
        var role = self.createRegionDialog.roleIds.split(',')
        console.log(self.roleOption)
        self.roleOption.forEach(item => {
          if (role.includes(String(item.id))) {
            self.$refs.roleTable && self.$refs.roleTable.toggleRowSelection(item, true)
          } else {
            self.$refs.roleTable && self.$refs.roleTable.toggleRowSelection(item, false)
          }
        })
      })
    },
    handleSelectionChange (val) {
      console.log(val)
      this.createRegionDialog.selectArr = val;
    },
    // 获取角色列表
    getRoles: function () {
      let param = {
      }
      var self = this;
      self.roleOption = []
      getAllRoles(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          })
        } else {
          for (let i = 0; i < res.roles.length; i++) {
            const item = res.roles[i];
            if (item.id == 7 || item.id == 4 || item.id == 6) {

            } else {
              self.roleOption.push(item)
            }

          }
          console.log(self.roleOption)
        }

      })
    },
    handleBindRoleSubmit: function () {
      this.dialogBindRole.bindRoleDialogTableVisible = false;
      var bindRoleNameArr = [];
      var bindRoleIdArr = [];
      this.createRegionDialog.selectArr.map(item => {
        bindRoleNameArr.push(item.name);
        bindRoleIdArr.push(item.id)
      })
      this.createRegionDialog.roleNames = bindRoleNameArr.join(',');
      this.createRegionDialog.roleIds = bindRoleIdArr.join(',');
      // this.$refs.roleTable.clearSelection()
    },
    queryOutlandBundle (id, nodeName) {
      var self = this;
      queryOutlandBundle({ serNodeId: id }).then(data => {
        if (data.status == 200) {
          self.treeData = [{
            name: nodeName,
            children: data.data
          }]
        }
      })
    },
    rowClick (row, event, column) {
      console.log(row)
    },
    currentRowChange: function (currentRow, oldRow) {
      var self = this;
      console.log(currentRow)
      self.queryOutlandBundle(currentRow.id, currentRow.nodeName)

    },
    cancleForm (formName) {
      this.createRegionDialog.visable = false
      this.createRegionDialog.name = ''
      this.createRegionDialog.password = ''
      this.createRegionDialog.role = ''
    },
    cancleForm1 (formName) {
      this.localConfigDialog.visable = false
      this.localConfigDialog.name = ''
      this.localConfigDialog.password = ''
    }
  },
  mounted: function () {
    var self = this
    self.$nextTick(function () {
      self.queryOutland()
      self.getRoles()
      self.$parent.$parent.$parent.$parent.$parent.setActive('/LwRegionManage')
    })
  }
}

</script>
<style scoped type="text/css">
.el-scrollbar__wrap {
  overflow-x: hidden;
}
.el-table {
  height: calc(100% - 50px);
}
.el-pagination {
  text-align: right;
}
.bundle-tree {
  box-sizing: border-box;
  width: 20%;
  position: fixed;
  right: 10px;
  top: 100px;
  bottom: 50px;
  border: 1px solid #ebeef5;
  /* background: gold; */
  overflow: auto;
  padding: 20px;
}
</style>
