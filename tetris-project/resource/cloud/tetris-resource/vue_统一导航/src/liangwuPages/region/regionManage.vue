<template>
  <div style="position:absolute; left:20px; top:20px; right:20px; bottom:20px;">
    <el-row>
      <el-button type="primary" size="mini" @click="createRegion">创建外域</el-button>
      <el-button type="primary" size="mini" @click="localConfig">本域设置</el-button>
    </el-row>
    <el-table :data="tableData" style="width: 75%">
      <el-table-column prop="name" label="外域名称" width="180" sortable>
      </el-table-column>
      <el-table-column prop="password" label="口令" width="300">
      </el-table-column>
      <el-table-column prop="role" label="绑定角色">
      </el-table-column>
      <el-table-column prop="status" label="状态" sortable>
      </el-table-column>
      <el-table-column prop="status" label="操作">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status == '断开中'" type="text" size="small">链接</el-button>
          <el-button v-if="scope.row.status == '连接中'" type="text" size="small">断开</el-button>
          <el-button type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="total , prev, pager, next" :total="1000" style="width:75%">
    </el-pagination>
    <div class="bundle-tree">
      组织树
    </div>
    <el-dialog title="创建外域" :visible.sync="createRegionDialog.visable" width="500px">
      <el-form ref="createRegionDialogForm" label-width="80px" :model="createRegionDialog" size=small>
        <el-form-item label="名称">
          <el-input v-model="createRegionDialog.name"></el-input>
        </el-form-item>
        <el-form-item label="口令">
          <el-input v-model="createRegionDialog.password"></el-input>
        </el-form-item>
        <el-form-item label="绑定角色">
          <el-input v-model="createRegionDialog.role"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitCreateRegionForm('ruleForm')">立即创建</el-button>
          <el-button @click="cancleForm()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="本域设置" :visible.sync="localConfigDialog.visable" width="500px">
      <el-form ref="localConfigDialogForm" label-width="80px" :model="localConfigDialog" size=small>
        <el-form-item label="名称">
          <el-input v-model="localConfigDialog.name"></el-input>
        </el-form-item>
        <el-form-item label="口令">
          <el-input v-model="localConfigDialog.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitLocalConfigForm()">保存</el-button>
          <el-button @click="cancleForm1()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>

</template>
<script type="text/ecmascript-6">

import { } from '../../api/api'

export default {
  data: function () {
    return {
      tableData: [{
        password: '4rqdqtqeqrqrqrqrqeqweq',
        name: '级联1',
        status: '连接中',
        role: '级联1管理员'
      }, {
        password: 'dqfqdafadadafarqdad',
        name: '级联2',
        status: '断开中',
        role: '级联2管理员'
      }, {
        password: 'dqfqdafadadaddwadadad',
        name: '级联3',
        status: '连接中',
        role: '级联3管理员'
      }, {
        password: 'dqfqdafadadadrqwqdqead',
        name: '级联4',
        status: '断开中',
        role: '级联4管理员'
      }],
      createRegionDialog: {
        visable: false,
        name: '',
        password: '',
        role: ''
      },
      localConfigDialog: {
        visable: false,
        name: '',
        password: ''
      }
    }
  },
  methods: {
    createRegion () {
      this.createRegionDialog.visable = true
    },
    localConfig () {
      this.localConfigDialog.visable = true
    },
    submitCreateRegionForm (formName) {
      // this.$refs[formName].validate((valid) => {
      //   if (valid) {
      //     alert('submit!')
      //   } else {
      //     console.log('error submit!!')
      //     return false
      //   }
      // })
    },
    submitLocalConfigForm () { },
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
  width: 20%;
  position: fixed;
  right: 10px;
  top: 100px;
  bottom: 50px;
  background: gold;
}
</style>
