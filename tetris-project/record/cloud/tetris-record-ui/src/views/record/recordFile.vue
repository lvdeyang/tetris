<template>
    <div style="width:100%; height:100%; position:relative;">
    <el-card class="box-card" shadow="hover" style="width:100%; height:100%;">
        <div style="box-sizing:border-box; width:420px; position:absolute; left:0; top:0; bottom:0; padding:10px; border-right:1px solid #ebeef5;">
            <span>录制策略</span>
            <div style="width:100%; height:100%; margin-top: 10px; position:relative">
                <el-scrollbar style="height:100%;">
                    <el-tree
                        v-loading="treeLoading"
                        ref="strategyTree"
                        :props = "tree.props"
                        :data = "tree.data"
                        node-key="uuid"
                        :current-node-key="tree.current"
                        check-strictly
                        :expand-on-click-node="false"
                        default-expand-all
                        highlight-current
                        @current-change="currentTreeNodeChange">

                        <span style="flex:1; display:flex; align-items:center; justify-content:space-between; padding-right:8px; position:relative; bottom:1px; padding-right:8px;" slot-scope="{node, data}">
                            <span style="font-size:14px;">
                                <span class="feather-icon-folder"></span>
                                <span>{{data.name}}</span>
                            </span>
                        </span>
                    </el-tree>
                </el-scrollbar>
            </div>
        </div>
        <div v-if="tree.current" style="box-sizing:border-box; position:absolute; left:420px; top:0; bottom:0; right:0; padding:10px;">
            <div style="width:100%; height:100%; position:relative">
                    <!--TODO 查询栏-->
                    <span>录制文件</span>
                    <!--收录文件表-->
                    <div style="position:absolute; top:30px; left:0; bottom:0; right:0;">
                            <el-scrollbar style="height:100%;">
                                <el-table :data="recordFileVOs" v-loading="tableLoading" style="width:100%">
                                    <el-table-column prop="id" label="序号" width="60"></el-table-column>
                                    <el-table-column prop="startTime" label="开始时间" width="200"></el-table-column>
                                    <el-table-column prop="stopTime" label="结束时间" width="200"></el-table-column>
                                    <el-table-column prop="status" label="录制状态" width="100"></el-table-column>
                                    <el-table-column label="操作">
                                        <template slot-scope="scope">
                                            <!--
                                            <el-button type="text" size="small" style="padding:0; margin-left:5px;"  @click="handleo=handlePreview(scope.$index, scope.row)">
                                                  <span style="font-size:16px;" class="el-icon-view"></span>
                                            </el-button>
                                            -->
                                            <el-button type="text" size="small" style="padding:0; margin-left:5px;"  @click="uploadRecordFile(scope.$index, scope.row)">
                                                  <span style="font-size:16px;" class="el-icon-upload"></span>
                                            </el-button>
                                            <!--
                                            <el-button type="text" size="small" style="padding:0; margin-left:5px;"  @click="delRecordFile(scope.$index, scope.row)">
                                                  <span style="font-size:16px;" class="el-icon-delete"></span>
                                            </el-button>
                                            -->

                                        </template>
                                    </el-table-column>
                                </el-table>
                                <!--
                                <div style="height:48px; width:100%;">
                                    <el-pagination
                                            style="float:right; margin-top:10px;"
                                            @current-change="handleCurrentChange"
                                            background
                                            layout="total, prev, pager, next, jumper"
                                            :total="table.total">
                                    </el-pagination>
                                </div>
                                -->
                            </el-scrollbar>
                        </div>

            </div>
        </div>
    </el-card>
    </div>
</template>

<script>
import util from '../../common/js/util'
import { queryAllStrategy, queryRecordFile, delRecordFile, previewRecordFile, uploadFileToMims } from '../../api/api'

export default {
  data () {
    return {
      filters: {},
      strategyTree: [],
      recordStrategyVOs: [],
      recordFileVOs: [],

      tree: {
        props: {
          label: 'name',
          children: 'sub',
          isLeaf: 'isLeaf'
        },
        expandOnClickNode: false,
        data: [],
        current: ''
      },

      total: 0,
      page: 1,
      treeLoading: false,
      tableLoading: false,
      editLoading: false,

      sels: [] // 列表选中列
    }
  },

  methods: {
    // 获取策略列表
    queryStrategyTree () {
      this.treeLoading = true

      let para = {
      }

      // NProgress.start();
      queryAllStrategy(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          console.log(JSON.stringify(res.recordStrategyVOs))
          this.tree.data = res.recordStrategyVOs
        }

        this.treeLoading = false
        // NProgress.done();
      })
    },

    // 获取录制文件列表
    queryRecordFileList (strategyId) {
      this.tableLoading = true

      let para = {
        recordStrategyId: strategyId,
        pageIndex: this.page - 1,
        pageSize: 10
      }

      queryRecordFile(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.recordFileVOs = res.recordFileVOs
        }

        this.tableLoading = false
      })
    },

    handlePreview: function (index, row) {
      let para = {
        id: row.id
      }

      previewRecordFile(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          console.log('previewRecordFile, url=' + res.previewUrl)
        }

        this.tableLoading = false
      })
    },

    uploadRecordFile: function (index, row) {
      let para = {
        id: row.id
      }

      uploadFileToMims(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          console.log('uploadFileToMims, resp=' + res.resp)
        }

        this.tableLoading = false
      })
    },

    currentNode: function (data) {
      console.log('currentNode-------')
      console.log('node=' + JSON.stringify(data))
      if (!data) {
        return
      }

      self.tree.current = data.id
      self.loadRoles(data.id, 1)
    },

    currentTreeNodeChange: function (data) {
      console.log('currentTreeNodeChange-------')
      console.log('node=' + JSON.stringify(data))
      this.tree.current = data.id
      this.queryRecordFileList(data.id)
    },

    handleCurrentChange (val) {
      console.log('handleCurrentChange-------')

      // this.page = val
      // this.queryOprlogList()
    },

    // 重置表单
    resetForm (formName) {
      if (this.$refs[formName] !== undefined) {
        this.$refs[formName].resetFields()
      }
    },

    selsChange: function (sels) {
      this.sels = sels
    },

    dateFormat: function (row, column) {
      var dateString = row[column.property]
      if (dateString === undefined) {
        return ''
      }

      return util.formatDate.format(new Date(Date.parse(dateString)), 'yyyy-MM-dd hh:mm:ss')
    }
  },

  mounted () {
    this.queryStrategyTree()
  }
}

</script>
