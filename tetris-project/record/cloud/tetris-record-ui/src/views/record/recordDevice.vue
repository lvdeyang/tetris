<template>
    <section>
        <!--工具条-->
        <el-form :inline="true" :model="filters" ref="filters" label-width="80px" size="mini">
            <el-col :span="24" class="toolbar" style="padding-bottom: 0px; margin:0px">
                <el-form-item prop="keyword" label="关键字" :span="6">
                    <el-input v-model="filters.keyword"></el-input>
                </el-form-item>
                <el-form-item :span="3">
                    <el-button type="primary" v-on:click="queryRecordDeviceList()">查询</el-button>
                </el-form-item>
                <el-form-item :span="3">
                    <el-button v-on:click="resetForm('filters')">重置</el-button>
                </el-form-item>
                <el-form-item :span="3">
                    <el-button type="primary" v-on:click="handleShowAddDeviceDialog()">添加录制设备</el-button>
                </el-form-item>
            </el-col>
        </el-form>
        <!--查询工具条结束-->

        <!--列表-->
        <el-table :data="recordDeviceVOs" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
            <el-table-column prop="id" label="序号" width="60"></el-table-column>
            <el-table-column prop="deviceName" label="设备名称" width="200"></el-table-column>
            <el-table-column prop="deviceIP" label="设备IP" width="200"></el-table-column>
            <el-table-column prop="devicePort" label="端口" width="80"></el-table-column>
            <el-table-column prop="onlineStatus" label="在线状态" width="80"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="text" size="mini" style="padding:0; margin-left:5px;" @click="handleDel(scope.$index, scope.row)">
                      <span style="font-size:16px;" class="el-icon-delete"></span>
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--表底工具条-->
        <el-col :span="24" class="toolbar">
            <!--<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>-->
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
            </el-pagination>
        </el-col>
        <!--表底工具条结束-->

        <el-dialog title="添加录制设备" :visible.sync="addDeviceDialogVisible" :close-on-click-modal="false">
          <!--列表-->
          <el-table
            :data="deviceFeignVOs"
            highlight-current-row
            v-loading="deviceDialogListLoading"
            @selection-change="selsChange"
            style="width: 100%;"
          >
            <el-table-column label="选择" width="65">
              <template slot-scope="scope">
                <el-radio v-model="radio" :label="scope.$index" @change.native="getCurrentDeviceRow(scope.row)"></el-radio>
              </template>
            </el-table-column>
            <el-table-column prop="bundle_name" label="设备名称" width="250"></el-table-column>
            <el-table-column prop="deviceIp" label="设备IP" width="250"></el-table-column>
            <el-table-column prop="devicePort" label="端口" width="80"></el-table-column>
            <el-table-column prop="bundle_status" label="在线状态" width="80"></el-table-column>
          </el-table>

          <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="handleAddDevice()">确认</el-button>
            <el-button size="small" @click.native="addDeviceDialogVisible = false">关闭</el-button>
          </div>
        </el-dialog>
    </section>
</template>

<script>
import util from '../../common/js/util'
import { queryRecordDevice, queryDeviceFromFeignAPI, addDevice, delDevice } from '../../api/api'

export default {
  data () {
    return {
      filters: {},
      recordDeviceVOs: [],
      deviceFeignVOs: [],
      tempSelection: [],
      radio: '',
      sels: [], // 列表选中列

      total: 0,
      page: 1,
      listLoading: false,
      deviceDialogListLoading: false,

      addDeviceDialogVisible: false
    }
  },

  methods: {
    // 获取告警信息列表
    queryRecordDeviceList: function () {
      let para = {
        keyword: '1',
        pageIndex: this.page - 1,
        pageSize: 20
      }

      console.info(JSON.stringify(para))

      this.listLoading = true
      // NProgress.start();
      queryRecordDevice(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.total = res.totalNum
          this.recordDeviceVOs = res.recordDeviceVOs
        }

        this.listLoading = false
        // NProgress.done();
      })
    },

    handleShowAddDeviceDialog: function () {
      this.addDeviceDialogVisible = true
      this.deviceDialogListLoading = true

      let para = {
        // keyword: '1'
      }

      queryDeviceFromFeignAPI(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.deviceFeignVOs = res.deviceFeignVOs
        }

        this.deviceDialogListLoading = false
      })
    },

    handleAddDevice: function () {
      let para = {
        // keyword: '1'
        deviceName: this.tempSelection.bundle_name,
        deviceUuid: this.tempSelection.bundle_id,
        deviceIp: this.tempSelection.deviceIp,
        devicePort: this.tempSelection.devicePort,
        status: this.tempSelection.bundle_status
      }

      addDevice(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.addDeviceDialogVisible = false
          this.queryRecordDeviceList()
        }
      })
    },

    handleDel: function (index, row) {
      this.$confirm('删除设备会影响所有使用此设备的录制策略，确认？', '提示', {}).then(() => {
        let para = {
          id: row.id
        }

        delDevice(para).then(res => {
          if (res.errMsg !== null && res.errMsg !== '') {
            this.$message({
              message: res.errMsg,
              type: 'error',
              duration: 3000
            })
          } else {
            this.queryRecordDeviceList()
          }
        })
      })
    },

    getCurrentDeviceRow (row) { // 获取选中数据
      this.tempSelection = row
      console.log(JSON.stringify(row))
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

    handleCurrentChange (val) {
      this.page = val
      this.queryOprlogList()
    },

    dateFormat: function (row, column) {
      var dateString = row[column.property]
      if (dateString == undefined) {
        return ''
      }

      return util.formatDate.format(new Date(Date.parse(dateString)), 'yyyy-MM-dd hh:mm:ss')
    }
  },

  mounted () {
    this.queryRecordDeviceList()
  }
}
</script>
