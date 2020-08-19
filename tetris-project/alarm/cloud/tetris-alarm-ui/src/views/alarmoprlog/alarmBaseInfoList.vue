<template>
	<section>

	<!--工具条-->
    <el-form :inline="true" :model="filters" ref="filters" label-width="80px" size="mini">
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px; margin:0px">
            <el-form-item prop="keyword" label="关键字" :span="6">
                <el-input v-model="filters.keyword"></el-input>
            </el-form-item>
            <el-form-item :span="3">
                <el-button type="primary" v-on:click="queryAlarmBaseInfoList">查询</el-button>
            </el-form-item>
            <el-form-item :span="3">
                <el-button  v-on:click="resetForm('filters')">重置</el-button>
            </el-form-item>
            <el-form-item :span="3">
                <el-button  v-on:click="handleShowAdd()"> 新增</el-button>
            </el-form-item>

            <el-form-item :span="3">
            <el-upload
                class="upload-demo"
                :action=uploadUrl
                name="filePoster"
                :show-file-list="false"
                :on-success="uploadSuccess"
                :on-error="uploadError"
                :on-progress="onprogress">
                <el-button type="primary">导入</el-button>
            </el-upload>
            </el-form-item>
        </el-col>
    </el-form>

		<!--列表-->
		<el-table stripe :data="alarmInfoVOs" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
		  <el-table-column prop="id" v-if="false" width="60"></el-table-column>
			<el-table-column prop="alarmCode" label="编码" width="100" sortable></el-table-column>
			<el-table-column prop="alarmName" label="名称" width="250"></el-table-column>
			<el-table-column prop="alarmLevel" label="级别"  :formatter="alarmLevelFormat" width="180" sortable></el-table-column>
      <el-table-column prop="alarmBrief" label="简介" width="250"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="small" @click="handleShowEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--表底工具条-->
    <el-col :span="24" class="toolbar">
        <!--<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>-->
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="10" :total="total" style="float:right;">
        </el-pagination>
    </el-col>

		<!--编辑、新增界面-->
		<el-dialog title="告警基本信息编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="160px" ref="editForm" :rules="editFormRules" >
				<el-form-item label="序号" prop="id" v-if="editBtnVisible">
					<el-input v-model="editForm.id" auto-complete="off" v-bind:readonly="editBtnVisible"></el-input>
				</el-form-item>
            <el-form-item label="告警编码" prop="alarmCode">
                <el-input v-model="editForm.alarmCode" auto-complete="off" v-bind:readonly="editBtnVisible"></el-input>
            </el-form-item>
            <el-form-item label="告警名称" prop="alarmName">
                 <el-input v-model="editForm.alarmName"  auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="告警级别" prop="alarmLevel">
                <template>
                  <el-select v-model="editForm.alarmLevel" placeholder="请选择">
                    <el-option
                      v-for="item in alarmLevelOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </template>
            </el-form-item>
            <el-form-item label="告警简介" prop="alarmBrief">
                <el-input type="textarea" v-model="editForm.alarmBrief" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="解决建议" prop="alarmSolution">
                <el-input type="textarea" v-model="editForm.alarmSolution" auto-complete="off"></el-input>
            </el-form-item>
          </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="small" @click="handleEdit()" v-if="editBtnVisible" >修改</el-button>
                <el-button size="small" @click="handleAdd()" v-if="addBtnVisible" >新增</el-button>
                <el-button size="small" @click.native="editFormVisible = false">关闭</el-button>
            </div>
		</el-dialog>
	</section>
</template>

<script>
// import util from '../../common/js/util';
import alarmOprlogUtils from '../../common/js/alarmOprlogUtils'
import globalRouter from '../../router/util/globalRouter'
// import globalVar from '../../components/Global.vue'
// import NProgress from 'nprogress'
import {
  queryAlarmBaseInfoListPage,
  addAlarmBaseInfo,
  editAlarmBaseInfo,
  delAlarmBaseInfo,
  checkAlarmCode
} from '../../api/api'

export default {
  data () {
    // 表单校验部分
    // 新增，验证告警编码位数，纯数字，和重复
    var validateAlarmCode = (rule, value, callback) => {
      var reg = /^\d{8}$/
      if (!reg.test(value)) {
        callback(new Error('请输入8位数字告警编码'))
      } else {
        if (this.editBtnVisible) {
          callback()
        } else {
          let para = {
            'alarmCode': value
          }
          checkAlarmCode(para).then((res) => {
            if (res.check !== null && (res.check === 'exist')) {
              callback(new Error('告警编码已存在'))
            } else {
              callback()
            }
          })
        }
      }
    }

    var validateAlarmLevel = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请选择告警级别'))
      } else {
        callback()
      }
    }

    return {
      filters: {},
      alarmInfoVOs: [],
      total: 0,
      page: 1,
      listLoading: false,
      sels: [], // 列表选中列

      editFormVisible: false, // 编辑界面是否显示
      editBtnVisible: false, // 弹出框中编辑按钮是否显示
      addBtnVisible: false, // 弹出框中新建按钮新否显示
      editLoading: false,

      alarmLevelOptions: [
        {
          value: 'INFO',
          label: '提示'
        },
        {
          value: 'MINOR',
          label: '一般'
        },
        {
          value: 'MAJOR',
          label: '重要'
        },
        {
          value: 'CRITICAL',
          label: '严重'
        }
      ],

      alarmLevelSel: [],

      uploadUrl: globalRouter.alarmroot + '/alarmInfo/import',

      // 详情界面数据
      editForm: {
        id: 0,
        name: '',
        password: '',
        confirmpassword: '',
        phone: '',
        email: '',
        isAdmin: 0,
        recAlarmPhoneRSS: 0,
        recAlarmEmailRSS: 0,
        description: ''
      },
      bindRoleVisible: false,
      currentUserId: 0,

      editFormRules: {
        alarmCode: [
          { required: true, message: '请输入8位数字告警编码', trigger: 'blur' },
          {validator: validateAlarmCode}
        ],
        alarmName: [
          { required: true, message: '请输入告警名称', trigger: 'blur' },
          { min: 1, max: 20, message: '请输入不超过20字的告警名称', trigger: 'blur' }
        ],
        alarmLevel: [
          { required: true, message: '请选择告警级别', trigger: 'blur' }
        ],
        alarmBrief: [
          { max: 200, message: '请输入不超过200字', trigger: 'blur' }
        ],
        alarmSolution: [
          { max: 200, message: '请输入不超过200字', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 获取告警信息列表
    queryAlarmBaseInfoList () {
      let para = {
        keyword: this.filters.keyword,
        pageIndex: this.page - 1,
        pageSize: 10
      }

      this.listLoading = true
      // NProgress.start();
      queryAlarmBaseInfoListPage(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.total = res.total
          this.alarmInfoVOs = res.alarmInfoVOs
        }

        this.listLoading = false
        // NProgress.done();
      })
    },

    // 显示新建界面
    handleShowAdd: function () {
      this.resetForm('editForm')
      this.addBtnVisible = true
      this.editFormVisible = true
      this.editBtnVisible = false
    },

    // 新增告警基本信息
    handleAdd: function () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认新增告警基本信息吗？', '提示', {}).then(() => {
            this.editLoading = true
            // NProgress.start();
            let para = {
              // id: this.editForm.id,
              alarmCode: this.editForm.alarmCode,
              alarmName: this.editForm.alarmName,
              alarmLevel: this.editForm.alarmLevel,
              alarmBrief: this.editForm.alarmBrief,
              alarmSolution: this.editForm.alarmSolution
            }

            console.log(JSON.stringify(para))

            addAlarmBaseInfo(para).then(res => {
              if (res.errMsg !== null && res.errMsg !== '') {
                this.$message({
                  message: res.errMsg,
                  type: 'error',
                  duration: 3000
                })
              } else {
                this.$message({
                  message: '新增成功',
                  type: 'success'
                })
                this.$refs['editForm'].resetFields()
                this.editFormVisible = false
                this.addBtnVisibale = false
                this.queryAlarmBaseInfoList()
              }
            })
            this.editLoading = false
          })
        }
      })
    },

    // 显示编辑界面
    handleShowEdit: function (index, row) {
      this.resetForm('editForm')
      this.editFormVisible = true
      this.addBtnVisible = false
      this.editBtnVisible = true
      this.editForm = Object.assign({}, row)
    },

    // 重置表单
    resetForm (formName) {
      if (this.$refs[formName] != undefined) {
        this.$refs[formName].resetFields()
      }
    },

    // 修改告警基本信息
    handleEdit: function () {
      this.$refs.editForm.validate((valid) => {
        this.$confirm('确认修改告警基本信息吗？', '提示', {}).then(() => {
          this.editLoading = true
          // NProgress.start();
          let para = {
            id: this.editForm.id,
            alarmCode: this.editForm.alarmCode,
            alarmName: this.editForm.alarmName,
            alarmLevel: this.editForm.alarmLevel,
            alarmBrief: this.editForm.alarmBrief,
            alarmSolution: this.editForm.alarmSolution
          }

          editAlarmBaseInfo(para).then(res => {
            if (res.errMsg !== null && res.errMsg !== '') {
              this.$message({
                message: res.errMsg,
                type: 'error',
                duration: 3000
              })
            } else {
              this.$message({
                message: '修改成功',
                type: 'success'
              })
              this.$refs['editForm'].resetFields()
              this.editFormVisible = false
              this.queryAlarmBaseInfoList()
            }
          })
          this.editLoading = false
        })
      })
    },

    // 删除告警基本信息
    handleDel: function (index, row) {
      this.$confirm('删除告警基本信息会删除所有此类告警，确认？', '提示', {}).then(() => {
        let para = {
          id: row.id
        }

        delAlarmBaseInfo(para).then(res => {
          if (res.errMsg !== null && res.errMsg !== '') {
            this.$message({
              message: res.errMsg,
              type: 'error',
              duration: 3000
            })
          } else {
            this.queryAlarmBaseInfoList()
          }
        })
      })
    },

    selsChange: function (sels) {
      this.sels = sels
    },

    handleCurrentChange (val) {
      this.page = val
      this.queryAlarmBaseInfoList()
    },

    alarmLevelFormat: function (row, column) {
      var dateString = row[column.property]
      if (dateString == undefined) {
        return ''
      }

      return alarmOprlogUtils.alarmLevelToString(dateString)
    },

    uploadSuccess: function (res) {
      if (res.errMsg != '') {
        this.$message({
          message: res.errMsg,
          type: 'error'
        })
      } else {
        if (res.sameNum !== 0) {
          this.$message({
            message: '导入' + res.importNum + '条告警基本信息, 另外' + res.sameNum + '条编码重复信息未导入',
            type: 'success'
          })
        } else {
          this.$message({
            message: '导入' + res.importNum + '条告警基本信息',
            type: 'success'
          })
        }

        this.queryAlarmBaseInfoList()
      }
    },

    uploadError: function () {
      this.$message({
        message: '上传文件错误',
        type: 'error'
      })
    },

    onprogress: function () {
      this.editLoading = true
    },

    // 批量删除
    batchRemove: function () {
      /*
				var ids = this.sels.map(item => item.id).toString();
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					//NProgress.start();
					let para = { ids: ids };
					batchRemoveUser(para).then((res) => {
						this.listLoading = false;
						//NProgress.done();
						this.$message({
							message: '删除成功',
							type: 'success'
						});
						this.getUsers();
					});
				}).catch(() => {

        });
        */
    }
  },
  mounted () {
    this.queryAlarmBaseInfoList()
  }
}
</script>
