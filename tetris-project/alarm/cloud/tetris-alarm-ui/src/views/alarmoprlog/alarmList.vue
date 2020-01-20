<template>
	<section>
    <el-tabs v-model="activeTab" @tab-click="handleClick">
      <el-tab-pane label="实时告警" name="alarm"></el-tab-pane>
      <el-tab-pane label="历史告警" name="history"></el-tab-pane>
      <el-tab-pane label="已屏蔽告警" name="shield"></el-tab-pane>
    </el-tabs>

		<!--工具条-->
    <el-form :inline="true" :model="filters" ref="filters" label-width="80px" size="mini">
			<el-col :span="24" class="toolbar" style="padding-bottom: 0px; margin:0px">
        <el-form-item label="来源服务" prop="sourceService" :span="6">
					<el-input v-model="filters.sourceService"></el-input>
				</el-form-item>
				<el-form-item label="IP地址" prop="sourceIP" :span="6">
					<el-input v-model="filters.sourceIP"></el-input>
				</el-form-item>
        <el-form-item label="告警编号" prop="alarmCode" :span="6">
					<el-input v-model="filters.alarmCode"></el-input>
				</el-form-item>

      </el-col>
      <el-col :span="24" class="toolbar" style="padding-top: 0px; padding-bottom: 5px; margin:0px" >
        <el-form-item label="告警级别" prop="alarmLevelSel"  :span="6">
            <el-select v-model="filters.alarmLevelSel" clearable placeholder="请选择">
              <el-option
                v-for="item in alarmLevelOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
				</el-form-item>
				<el-form-item label="告警时间" prop="lastTimeRange" :span="12">
          <el-date-picker v-model="filters.lastTimeRange" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
				</el-form-item>
        <el-form-item :span="3">
					<el-button type="primary" v-on:click="queryAlarmList">查询</el-button>
				</el-form-item>
        <el-form-item :span="3">
          <el-button  v-on:click="resetForm('filters')">重置</el-button>
        </el-form-item>
		  </el-col>

    </el-form>

		<!--列表-->
		<el-table :data="alarmVOs" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column prop="id" v-if="false" width="60"></el-table-column>
      <el-table-column prop="alarmName" label="告警名称" width="180" ></el-table-column>
      <el-table-column prop="alarmDevice" label="告警设备" width="150"  ></el-table-column>
      <el-table-column prop="alarmLevel" label="级别" width="80" :formatter="alarmLevelFormat" sortable></el-table-column>
			<el-table-column prop="sourceService" label="来源服务" width="180" ></el-table-column>
			<!--<el-table-column prop="alarmCode" label="告警编码" width="100" sortable></el-table-column>-->
      <el-table-column prop="lastCreateTime" label="最后告警时间" :formatter="dateFormat" width="160" v-if="activeTab!='history'" sortable></el-table-column>
      <el-table-column prop="recoverTime" label="告警恢复时间" :formatter="dateFormat" width="160" v-if="activeTab==='history'" sortable></el-table-column>

      <el-table-column prop="alarmCount" label="次数" width="60" ></el-table-column>
      <el-table-column label="操作">
          <template slot-scope="scope">
              <el-button size="small" @click="handleShowDetail(scope.$index, scope.row)">详情</el-button>
              <el-button size="small" @click="handleIgnore(scope.$index, scope.row)" v-if="activeTab==='alarm'">忽略</el-button>
              <el-button size="small" @click="handleRecover(scope.$index, scope.row)" v-if="activeTab==='alarm'">恢复</el-button>
              <el-button type="danger" size="small" @click="handleShield(scope.$index, scope.row)" v-if="(activeTab==='alarm')||(activeTab==='history')">屏蔽</el-button>
              <el-button type="small" @click="handleUnShield(scope.$index, scope.row)" v-if="activeTab==='shield'">解除屏蔽</el-button>
              <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)" v-if="activeTab==='history'">删除</el-button>
          </template>
      </el-table-column>
    </el-table>

          <!--工具条-->
            <el-col :span="24" class="toolbar">
                <!--<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>-->
                <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
                </el-pagination>
            </el-col>

		<!--详情界面-->
		<el-dialog title="告警详情" :visible.sync="editFormVisible" :close-on-click-modal="false">
      <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="dataName"  width="180"></el-table-column>
            <el-table-column prop="dataContent" width="180"></el-table-column>
      </el-table>
		</el-dialog>
	</section>
</template>

<script>

import util from '../../common/js/util'
import alarmOprlogUtils from '../../common/js/alarmOprlogUtils'
// import apiConfig from '../../../config/api.config'

// import NProgress from 'nprogress'
import { queryAlarmListPage, ignoreAlarm, manualRecoverAlarm, shieldAlarm, unShieldAlarm, delAlarm } from '../../api/api'

export default {
  data () {
    return {

      filters: {
      },
      alarmVOs: [],
      total: 0,
      page: 1,
      listLoading: false,
      sels: [], // 列表选中列

      editFormVisible: false, // 编辑界面是否显示
      editLoading: false,

      activeTab: 'alarm',
      tableData: [],

      alarmLevelOptions: [{
        value: 'INFO',
        label: '提示'
      }, {
        value: 'MINOR',
        label: '一般'
      }, {
        value: 'MAJOR',
        label: '重要'
      }, {
        value: 'CRITICAL',
        label: '严重'
      }],

      lastTimeRange: '',

      websock: null,

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
      currentUserId: 0
    }
  },
  methods: {
    // 标签页
    handleClick (tab, event) {
      this.queryAlarmList()
    },

    // 获取告警列表
    queryAlarmList () {
      var alarmStatusArr
      var blockStatus
      var timeStart, timeEnd

      switch (this.activeTab) {
        case 'alarm':
          alarmStatusArr = 'UNTREATED'
          blockStatus = 'NORMAL'
          break
        case 'history':
          alarmStatusArr = 'AUTO_RECOVER,MANUAL_RECOVER,IGNORE'
          blockStatus = 'NORMAL'
          break
        case 'shield':
          blockStatus = 'BLOCKED'
          break
        default:
          break
      }

      if (this.filters.lastTimeRange != undefined) {
        if (typeof (this.filters.lastTimeRange[0]) !== undefined) {
          timeStart = util.formatDate.format(this.filters.lastTimeRange[0], 'yyyy-MM-dd hh:mm:ss')
        };

        if (typeof (this.filters.lastTimeRange[1]) !== undefined) {
          timeEnd = util.formatDate.format(this.filters.lastTimeRange[1], 'yyyy-MM-dd hh:mm:ss')
        };
      };

      let para = {
        sourceService: this.filters.sourceService,
        sourceIP: this.filters.sourceIP,
        alarmCode: this.filters.alarmCode,
        // alarmLevels: JSON.stringify(this.alarmLevelSel),
        firstCreateTime: timeStart,
        lastCreateTime: timeEnd,
        alarmLevel: this.filters.alarmLevelSel,
        alarmStatusArrString: alarmStatusArr,
        alarmBlockStatus: blockStatus,
        pageIndex: this.page - 1,
        pageSize: 20
      }

      console.info(JSON.stringify(para))

      this.listLoading = true
      // NProgress.start();
      queryAlarmListPage(para).then((res) => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {

          console.log(JSON.stringify(res.alarmVOs))
          this.total = res.total
          this.alarmVOs = res.alarmVOs
        }

        this.listLoading = false
        // NProgress.done();
      })
    },
    // 获取告警详情
    handleShowDetail: function (index, row) {
      this.resetForm('editForm')
      this.editFormVisible = true
      this.editForm = Object.assign({}, row)
      var json = []
      json.push({ dataName: 'id', dataContent: this.editForm.id })
      json.push({ dataName: '告警编码', dataContent: this.editForm.alarmCode })
      json.push({ dataName: '告警名称', dataContent: this.editForm.sourceService })
      json.push({ dataName: '告警设备', dataContent: this.editForm.alarmDevice })
      json.push({ dataName: '告警对象', dataContent: this.editForm.alarmObj })
      json.push({ dataName: '告警级别', dataContent: this.editForm.alarmLevel })
      json.push({ dataName: '告警次数', dataContent: this.editForm.alarmCount })
      json.push({ dataName: '最新告警时间', dataContent: util.formatDate.formatString(this.editForm.lastCreateTime, 'yyyy-MM-dd hh:mm:ss') })
      json.push({ dataName: '最新告警参数', dataContent: this.editForm.alarmParams })
      json.push({ dataName: '首次告警时间', dataContent: util.formatDate.formatString(this.editForm.firstCreateTime, 'yyyy-MM-dd hh:mm:ss') })
      json.push({ dataName: '解决建议', dataContent: this.editForm.alarmSolution })
      json.push({ dataName: '来源微服务名称', dataContent: this.editForm.sourceService })
      json.push({ dataName: '来源微服务IP', dataContent: this.editForm.sourceServiceIP })    
      this.tableData = json
    },

    // 重置表单
    resetForm (formName) {
      if (this.$refs[formName] != undefined) {
        this.$refs[formName].resetFields()
      }
    },

    handleIgnore: function (index, row) {
      let para = {
        id: row.id
      }

      ignoreAlarm(para).then((res) => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.queryAlarmList()
        }
      })
    },

    handleRecover: function (index, row) {
      let para = {
        id: row.id
      }

      manualRecoverAlarm(para).then((res) => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.queryAlarmList()
        }
      })
    },

    handleDel: function (index, row) {
      let para = {
        id: row.id
      }

      delAlarm(para).then((res) => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.queryAlarmList()
        }
      })
    },

    handleShield: function (index, row) {
      this.$confirm('确认屏蔽此类告警吗？', '提示', {}).then(() => {
        let para = {
          id: row.id
        }

        shieldAlarm(para).then((res) => {
          if (res.errMsg !== null && res.errMsg !== '') {
            this.$message({
              message: res.errMsg,
              type: 'error',
              duration: 3000
            })
          } else {
            this.queryAlarmList()
          }
        })
      })
    },

    handleUnShield: function (index, row) {
      this.$confirm('确认解除此类告警的屏蔽吗？', '提示', {}).then(() => {
        let para = {
          id: row.id
        }

        unShieldAlarm(para).then((res) => {
          if (res.errMsg !== null && res.errMsg !== '') {
            this.$message({
              message: res.errMsg,
              type: 'error',
              duration: 3000
            })
          } else {
            this.queryAlarmList()
          }
        })
      })
    },

    dateFormat: function (row, column) {
      return alarmOprlogUtils.dateFormat(row, column)
    },

    alarmLevelFormat: function (row, column) {
      return alarmOprlogUtils.alarmLevelToString(row[column.property])
    },

    selsChange: function (sels) {
      this.sels = sels
    },

    handleCurrentChange (val) {
      this.page = val
      this.queryAlarmList()
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
    },

    initWebpack () { // 初始化websocket
      var requestIP = document.location.host.split(':')[0]

      var wsuri = process.env.WEBSOCKET_URL + '/websocket/alarm'
      if (wsuri.indexOf('__requestIP__') !== -1) {
        wsuri = wsuri.replace('__requestIP__', requestIP)
        console.log('wsuri replace=' + wsuri)
      }

      this.websock = new WebSocket(wsuri)// 这里面的this都指向vue
      this.websock.onopen = this.websocketopen
      this.websock.onmessage = this.websocketonmessage
      this.websock.onclose = this.websocketclose
      this.websock.onerror = this.websocketerror
    },

    websocketopen () { // 打开
      console.log('WebSocket连接成功')
    },
    websocketonmessage (e) { // 数据接收
      console.log(e)

      if (e.data === 'new') {
        this.$notify({
          title: '警告',
          type: 'warning',
          position: 'bottom-right',
          message: '有新的告警',
          duration: 3000
        })

        this.queryAlarmList()
      }
    },
    websocketclose () { // 关闭
      console.log('WebSocket关闭')
    },
    websocketerror () { // 失败
      console.log('WebSocket连接失败')
    }

  },
  mounted () {
    this.initWebpack()// websocket连接
    this.queryAlarmList()
  }
}

</script>

<style scoped>
  /*.textarea_font{*/
   /*font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;*/
  /*}*/
</style>
