
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
                    <el-button type="primary" v-on:click="handleShowAddDeviceDialog()">添加存储</el-button>
                </el-form-item>
            </el-col>
        </el-form>
        <!--查询工具条结束-->

        <!--列表-->
        <el-table :data="storageVOs" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
            <el-table-column prop="id" label="序号" width="60"></el-table-column>
            
            <el-table-column prop="name" label="名称" width="200"></el-table-column>
            <el-table-column prop="localRecordPath" label="录制路径" width="200"></el-table-column>
            <el-table-column prop="deviceIP" label="空间占用" width="200"></el-table-column>
            
            <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" style="padding:0; margin-left:5px;" @click="handleShowDetail(scope.$index, scope.row)">
                    <span style="font-size:16px;" class="el-icon-connection"></span>
                  </el-button>
                <el-button type="text" size="mini" style="padding:0; margin-left:5px;" @click="handleEdit(scope.$index, scope.row)">
                    <span style="font-size:16px;" class="el-icon-connection"></span>
                  </el-button>
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

        <el-dialog title="添加存储" :visible.sync="addStorageDialogVisible" :close-on-click-modal="false" width="600px" >
            <el-form ref="addStorageForm" :model="addStorageForm" label-width="150px" size="small">
                <el-form-item label="名称">
                    <el-input v-model="addStorageForm.name"></el-input>
                </el-form-item>
                <el-form-item label="本地录制路径">
                    <el-input v-model="addStorageForm.localRecordPath"></el-input>
                </el-form-item>
                <el-form-item label="ftp地址">
                    <el-input v-model="addStorageForm.ftpBasePath"></el-input>
                </el-form-item> 
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="ftp用户名">
                            <el-input v-model="addStorageForm.ftpUserName"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="ftp密码">
                            <el-input v-model="addStorageForm.ftpPassword"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="http预览地址">
                    <el-input v-model="addStorageForm.httpBasePath"></el-input>
                </el-form-item>
                <el-form-item label="本地转码输出路径">
                    <el-input v-model="addStorageForm.localFFMpegOutputPath"></el-input>
                </el-form-item>
                
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="按保存时间清理">
                            <el-switch
                                v-model="addStorageForm.isCheckTimeThreshold">
                            </el-switch>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="最长保存时间 (天)">
                            <el-input v-model="addStorageForm.clean_timeThreshold" :disabled=!addStorageForm.isCheckTimeThreshold></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="按剩余空间清理">
                            <el-switch
                                v-model="addStorageForm.isCheckDiskUsedSpacePctThreshold">
                            </el-switch>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="最小剩余空间占比 (%)">
                            <el-input v-model="addStorageForm.clean_diskUsedSpaceThreshold" :disabled=!addStorageForm.isCheckDiskUsedSpacePctThreshold></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="按录制占用空间清理">
                            <el-switch
                                v-model="addStorageForm.isCheckRecordMaxSpaceThreshold">
                            </el-switch>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="最大占用空间 (G)">
                            <el-input v-model="addStorageForm.clean_recordMaxSpaceThreshold" :disabled=!addStorageForm.isCheckRecordMaxSpaceThreshold></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item>
                    <el-button type="primary" @click="handleAddStorage()">确定</el-button>
                    <el-button>取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </section>
</template>

<script>
import util from '../../common/js/util'
import { addStorage, queryStorage, delStorage, checkIsExistRecordTask} from '../../api/api'

export default {
  data () {
    return {
      filters: {},
      storageVOs: [],
      
      tempSelection: [],
      radio: '',
      sels: [], // 列表选中列
      
      addStorageForm: {
          isCheckTimeThreshold:false,
          isCheckDiskUsedSpacePctThreshold: false,
          isCheckRecordMaxSpaceThreshold: false
      },

      total: 0,
      page: 1,
      listLoading: false,
      deviceDialogListLoading: false,

      addStorageDialogVisible: false
    }
  },

  methods: {
    // 获取存储信息列表
    queryStorageList: function () {
      let para = {
        keyword: '1',
        pageIndex: this.page - 1,
        pageSize: 20
      }

      console.info(JSON.stringify(para))

      this.listLoading = true
      // NProgress.start();
      queryStorage(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          console.log("queryStorage ----===" + JSON.stringify(res.storageVOs) )
          this.total = res.totalNum
          this.storageVOs = res.storageVOs
        }

        this.listLoading = false
        // NProgress.done();
      })
    },

    handleShowAddDeviceDialog: function () {
      this.addStorageDialogVisible = true
      this.deviceDialogListLoading = true

      let para = {
        // keyword: '1'
      }
    },

    handleAddStorage: function () {
      
      let para = {
          storageVOStr: JSON.stringify(this.addStorageForm)
      }

      addStorage(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.addStorageDialogVisible = false
          this.queryStorageList()
        }
      })
    },

    handleDel: function (index, row) {
        console.log("row, id=" + row.id)

        let para = {
          storageId: row.id
        }


        this.$confirm('此操作将删除存储中的文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delStorage(para).then(res => {
            if (res.errMsg !== null && res.errMsg !== '') {
              this.$message({
                message: res.errMsg,
                type: 'error',
                duration: 3000
              })
            } else {
            
              this.queryStorageList()
            }
          })
        }).catch(() => {
               
        });
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
    this.queryStorageList()
  }
}
</script>
