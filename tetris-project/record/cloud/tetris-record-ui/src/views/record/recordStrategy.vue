<template>
  <section>
    <!--工具条-->
    <el-form :inline="true" :model="filters" ref="filters" label-width="80px" size="mini">
      <el-col :span="24" class="toolbar" style="padding-bottom: 0px; margin:0px">
        <el-form-item prop="keyword" label="关键字" :span="6">
          <el-input v-model="filters.keyword"></el-input>
        </el-form-item>
        <el-form-item :span="3">
          <el-button type="primary" v-on:click="queryRecordStg()">查询</el-button>
        </el-form-item>
        <el-form-item :span="3">
          <el-button v-on:click="resetForm('filters')">重置</el-button>
        </el-form-item>
        <el-form-item :span="3">
          <el-button  type="primary" v-on:click="handleShowAddStrategy()">新增录制策略</el-button>
        </el-form-item>
      </el-col>
    </el-form>

    <!--列表-->
    <el-table
      :data="recordStrategyVOs"
      highlight-current-row
      v-loading="listLoading"
      @selection-change="selsChange"
      style="width: 100%;"
    >
      <el-table-column prop="id" v-if=false label="序号" width="60"></el-table-column>
      <el-table-column prop="name" label="策略名称" width="200"></el-table-column>
      <el-table-column prop="sourceInfo" label="录制源" width="280"></el-table-column>
      <el-table-column prop="type" label="策略类型" width="120"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="200"></el-table-column>
      <!-- <el-table-column prop="strategyContent" label="策略内容" width="250"></el-table-column> -->
      <el-table-column prop="status" label="状态" width="80"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <!-- <el-button size="small" @click="handleShowEdit(scope.$index, scope.row)">编辑</el-button> -->
          <el-button type="text" size="small" style="padding:0; margin-left:5px;" v-if="scope.row.type=='手动启停' && scope.row.status!='执行中'"  @click="handleStartRecord(scope.$index, scope.row)">
            <span style="font-size:16px;" class="el-icon-caret-right"></span>
          </el-button>
          <el-button type="text" size="small" style="padding:0; margin-left:5px;" v-if="scope.row.type=='手动启停' && scope.row.status==='执行中'" @click="handleStopRecord(scope.$index, scope.row)">
            <span style="font-size:16px;" class="el-icon-video-pause"></span>
          </el-button>

          <el-button type="text" size="small" style="padding:0; margin-left:5px;"  @click="handleShowDetail(scope.$index, scope.row)">
                <span style="font-size:16px;" class="el-icon-document"></span>
          </el-button>
          <el-button type="text" size="small" style="padding:0; margin-left:5px;"  @click="handleDel(scope.$index, scope.row)">
                <span style="font-size:16px;" class="el-icon-delete"></span>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--表底工具条-->
    <el-col :span="24" class="toolbar">
      <!--<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>-->
      <el-pagination
        layout="prev, pager, next"
        @current-change="handleCurrentChange"
        :page-size="10"
        :total="total"
        style="float:right;"
      ></el-pagination>
    </el-col>

    <!--编辑、新增界面-->
    <el-dialog title="添加录制策略" :visible.sync="addStrategyFormVisible" :close-on-click-modal="false">
      <el-form :model="addStrategyForm"  ref="editForm">
        <el-form :inline="true" >
          <el-form-item label="输入源类型" prop="sourceType"  size="mini" label-width="100px">
            <el-select
                v-model="addStrategyForm.sourceType"
                placeholder="请选择"
                style="width: 150px"
              >
              <el-option
                v-for="item in sourceTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                ></el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="输入源Url" prop="sourceUrl" size="mini" label-width="80px" >
            <el-input v-model="addStrategyForm.sourceUrl" auto-complete="off" style="width:250px"></el-input>
          </el-form-item>
        </el-form>
        <el-form :inline="true" >
            <el-form-item label="录制策略类型" prop="type" size="mini" label-width="100px">
            <template>
              <el-select
                v-model="addStrategyForm.type"
                placeholder="请选择"
                @change="strategyTypeSelectChange()"
                style="width: 150px"
              >
              <el-option
                v-for="item in strategyTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                ></el-option>
              </el-select>
            </template>
          </el-form-item>
          <el-form-item label="策略名称" prop="name" size="mini" label-width="80px">
            <el-input v-model="addStrategyForm.name" auto-complete="off" v-bind:readonly="editBtnVisible" style="width:250px"></el-input>
          </el-form-item>
        </el-form>

        <el-form-item label="现在启动录制" prop="manualStrategyStart"  size="mini" v-if="addStrategyForm.type=='MANUAL'" label-width="100px">
            <el-switch
              v-model="addStrategyForm.manualStrategyStart"
              active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </el-form-item>

        <!--循环定时策略相关-->
        <el-form-item label="策略起止日期" prop="cyclesDateRange" v-if="addStrategyForm.type=='CYCLE_SCHEDULE'" size="mini" label-width="100px">
            <el-date-picker
              v-model="addStrategyForm.cyclesDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
        </el-form-item>
        <el-form-item prop="loopCycles" label="执行周期" v-if="addStrategyForm.type=='CYCLE_SCHEDULE'" size="mini" label-width="100px">
            <el-select v-model="addStrategyForm.loopCycles" multiple placeholder="请选择" size="mini" style="width:350px">
              <el-option
                v-for="item in loopPeriodOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
        </el-form-item>
        <el-table
          :data="cycleItemVOs"
          v-if="addStrategyForm.type=='CYCLE_SCHEDULE'"
          highlight-current-row
          border
          :row-style="{height: '35px'}"
          :header-cell-style="{padding:'5px 0'}"
          :header-row-style="{height: '35px'}"
          :cell-style="{padding:'5px 0'}"
          style="width: 602px;"
        >
          <el-table-column prop="name" label="时段名称" width="200px" style="height: 30px;">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.name"
                size="mini"
                controls-position="right"
                placeholder="时段名称"
              ></el-input>
              <!--<span>{{scope.row.name}}</span>-->
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" width="160px">
            <template slot-scope="scope">
              <el-time-picker
                size="mini"
                v-model="scope.row.startTime"
                placeholder="开始时间"
                value-format="HH:mm:ss"
              ></el-time-picker>
              <!--<span>{{scope.row.startTime}}</span>-->
            </template>
          </el-table-column>
          <el-table-column prop="endTime" label="结束时间"  width="160px">
            <template slot-scope="scope">
              <el-time-picker
                size="mini"
                v-model="scope.row.endTime"
                placeholder="结束时间"
                value-format="HH:mm:ss"
              ></el-time-picker>
              <!--<span>{{scope.row.endTime}}</span>-->
            </template>
          </el-table-column>

          <el-table-column width="80px">
            <template  slot="header">
                <el-button type="text" size="mini" style="padding:0; margin-left:5px;"  @click="addCycleTableRow()">
                  <span style="font-size:16px;" class="el-icon-circle-plus-outline"></span>
                </el-button>
            </template>
            <template slot-scope="scope">
              <el-button type="text" size="mini" style="padding:0; margin-left:5px;"  @click="handleDelScheduleRow(scope.$index, cycleItemVOs)">
                <span style="font-size:16px;" class="el-icon-delete"></span>
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!--自定义定时策略相关-->
        <el-table
          :data="customItemVOs"
          v-if="addStrategyForm.type=='CUSTOM_SCHEDULE'"
          highlight-current-row
          border
          :row-style="{height: '35px'}"
          :header-cell-style="{padding: '5px 0'}"
          :header-row-style="{height: '35px'}"
          :cell-style="{padding: '5px 0'}"
          style="width: 622px;"
        >
          <el-table-column prop="id" label="序号" width="60px">
            <template slot-scope="scope">
              <span :label="scope.$index"></span>
            </template>
          </el-table-column>
          <el-table-column prop="recordDate" label="录制日期" width="160px">
            <template slot-scope="scope">
              <el-date-picker
                size="mini"
                v-model="scope.row.recordDate"
                placeholder="录制日期"
                value-format="yyyy-MM-dd"
              ></el-date-picker>
              <!--<span>{{scope.row.startTime}}</span>-->
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间"  width="160px">
            <template slot-scope="scope">
              <el-time-picker
                size="mini"
                v-model="scope.row.startTime"
                placeholder="开始时间"
                value-format="HH:mm:ss"
              ></el-time-picker>
              <!--<span>{{scope.row.endTime}}</span>-->
            </template>
          </el-table-column>
          <el-table-column prop="endTime" label="结束时间"  width="160px">
            <template slot-scope="scope">
              <el-time-picker
                size="mini"
                v-model="scope.row.endTime"
                placeholder="结束时间"
                value-format="HH:mm:ss"
              ></el-time-picker>
              <!--<span>{{scope.row.endTime}}</span>-->
            </template>
          </el-table-column>
          <el-table-column width="80px">
            <template  slot="header" slot-scope="scope">
                <el-button type="text" size="mini" style="padding:0; margin-left:5px;"  @click="addCustomTableRow()">
                  <span style="font-size:16px;" class="el-icon-circle-plus-outline"></span>
                </el-button>
            </template>
            <template slot-scope="scope">
              <el-button type="text" size="mini" style="padding:0; margin-left:5px;"  @click="handleDelScheduleRow(scope.$index, customItemVOs)">
                <span style="font-size:16px;" class="el-icon-delete"></span>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button size="small" @click="handleEdit()" v-if="editBtnVisible">修改</el-button>-->
        <el-button size="small" @click="handleAddRecordStrategy()" v-if="addBtnVisible">确认</el-button>
        <el-button size="small" @click.native="addStrategyFormVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
// import util from '../../common/js/util';
import globalRouter from '../../router/util/globalRouter'
// import globalVar from '../../components/Global.vue'
// import NProgress from 'nprogress'
import {
  queryRecordStrategy, addRecordStrategy, queryStrategyItems, startRecord, stopRecord, delRecordStrategy
} from '../../api/api'

export default {
  data () {
    return {
      filters: {},
      recordStrategyVOs: [],
      total: 0,
      page: 1,
      listLoading: false,
      sels: [], // 列表选中列

      strategyTypeOptions: [
        {
          value: 'MANUAL',
          label: '手动启停'
        },
        {
          value: 'CYCLE_SCHEDULE',
          label: '循环排期'
        },
        {
          value: 'CUSTOM_SCHEDULE',
          label: '自定义排期'
        }
      ],

      sourceTypeOptions: [
        {
          value: 'udp',
          label: 'UDP_TS'
        },
        {
          value: 'hls',
          label: 'HLS'
        },
        {
          value: 'rtmp',
          label: 'RTMP'
        },
        {
          value: 'rtsp',
          label: 'RTSP'
        },
        {
          value: 'rtp',
          label: 'RTP'
        },
        {
          value: 'http',
          label: 'HTTP-TS'
        }
      ],

      loopPeriodOptions: [
        {
          value: 7,
          label: '每天'
        },
        {
          value: 1,
          label: '周一'
        },
        {
          value: 2,
          label: '周二'
        },
        {
          value: 3,
          label: '周三'
        },
        {
          value: 4,
          label: '周四'
        },
        {
          value: 5,
          label: '周五'
        },
        {
          value: 6,
          label: '周六'
        },
        {
          value: 0,
          label: '周日'
        }
      ],

      startOptions: [
        {
          value: '1',
          label: '是'
        },
        {
          value: '0',
          label: '否'
        }
      ],

      cycleItemVOs: [],

      customItemVOs: [],

      addStrategyFormVisible: false, // 新建界面是否显示

      editFormVisible: false, // 编辑界面是否显示
      editBtnVisible: false, // 弹出框中编辑按钮是否显示
      addBtnVisible: false, // 弹出框中新建按钮新否显示
      editLoading: false,

      cyclesFormVisible: false, //
      customFormVisible: false, //

      currentUserId: 0,

      addStrategyForm: {
        manualStrategyStart: true,
        loopCycles: []
      },

      editForm: {},

      editFormRules: {}
    }
  },
  methods: {
    // 获取录制策略列表
    queryRecordStrategyList () {
      let para = {
        // keyword: this.filters.keyword,
        pageIndex: this.page - 1,
        pageSize: 10
      }

      this.listLoading = true
      // NProgress.start();
      queryRecordStrategy(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.total = res.totalNum
          this.recordStrategyVOs = res.recordStrategyVOs
        }

        this.listLoading = false
        // NProgress.done();
      })
    },

    // 添加录制策略
    handleAddRecordStrategy () {
      var startDateTemp
      var endDateTemp
      var loopCyclesString = ''

      if (this.addStrategyForm.type === 'CYCLE_SCHEDULE') {
        console.log('CUSTOM_SCHEDULE')
        console.log(JSON.stringify(this.addStrategyForm.loopCycles))

        startDateTemp = this.addStrategyForm.cyclesDateRange[0]
        endDateTemp = this.addStrategyForm.cyclesDateRange[1]

        for (var value of this.addStrategyForm.loopCycles) {
          loopCyclesString = loopCyclesString + value + ','
          //console.log(loopCyclesString)
        }
      }
      let para = {
        // keyword: this.filters.keyword,
        name: this.addStrategyForm.name,
        type: this.addStrategyForm.type,
        sourceType: this.addStrategyForm.sourceType,
        sourceUrl: this.addStrategyForm.sourceUrl,
        loopCycles: loopCyclesString,
        startDate: startDateTemp,
        endDate: endDateTemp,
        recordTimeSlotJson: JSON.stringify(this.cycleItemVOs),
        taskListJson: JSON.stringify(this.customItemVOs),
        status: 'NEW',
        manualStrategyStart: this.addStrategyForm.manualStrategyStart,
        loopPeriod: 0,
        loopCnt: 1
      }

      this.listLoading = true
      // NProgress.start();
      addRecordStrategy(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.queryRecordStrategyList()
          this.addStrategyFormVisible = false
        }

        this.listLoading = false
        // NProgress.done();
      })
    },

    handleStartRecord: function (index, row) {
      console.log('start=' + row.id)

      let para = {
        recordStrategyId: row.id
      }

      startRecord(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.queryRecordStrategyList()
        }

        this.listLoading = false
        // NProgress.done();
      })
    },

    handleStopRecord: function (index, row) {
      console.log('stop=' + row.id)

      let para = {
        recordStrategyId: row.id
      }

      stopRecord(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          this.queryRecordStrategyList()
        }

        this.listLoading = false
        // NProgress.done();
      })
    },

    // 显示新建界面
    handleShowAddStrategy: function () {
      this.resetForm('addStrategyForm')
      this.resetItemVOs()
      this.addBtnVisible = true
      this.addStrategyFormVisible = true
      this.editBtnVisible = false
      this.cyclesFormVisible = false
    },

    handleShowDetail: function (index, row) {
      this.resetForm('addStrategyForm')
      this.resetItemVOs()
      console.log(JSON.stringify(row))

      if (row.type === 'CUSTOM_SCHEDULE') {
        let para = {
          strategyId: row.id
        }
        queryStrategyItems(para).then(res => {
          if (res.errMsg !== null && res.errMsg !== '') {
            this.$message({
              message: res.errMsg,
              type: 'error',
              duration: 3000
            })
          } else {
            console.log(JSON.stringify(res))
          }

          this.listLoading = false
        // NProgress.done();
        })
      }
    },

    handleDel: function (index, row) {
      console.log('del=' + row.id)

      let para = {
        recordStrategyId: row.id,
        delDiskFile: true
      }

      delRecordStrategy(para).then(res => {
        if (res.errMsg !== null && res.errMsg !== '') {
          this.$message({
            message: res.errMsg,
            type: 'error',
            duration: 3000
          })
        } else {
          console.log(JSON.stringify(res))
          this.queryRecordStrategyList()
        }

        this.listLoading = false
        // NProgress.done();
      })
    },

    // 显示编辑界面
    // TODO
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

    // 在自定义列表加一行红白行
    addCycleTableRow () {
      var newCycleRow = {
        name: '',
        startTime: '',
        endTime: ''
      }

      this.cycleItemVOs.push(newCycleRow)
    },

    handleDelScheduleRow (index, rows) {
      rows.splice(index, 1)
    },

    // 在自定义列表加一行红白行
    addCustomTableRow () {
      var newCustomRow = {
        id: '',
        recordDate: '',
        startTime: '',
        endTime: ''
      }

      this.customItemVOs.push(newCustomRow)
    },

    selsChange: function (sels) {
      this.sels = sels
    },

    handleCurrentChange (val) {
      this.page = val
      // this.queryAlarmBaseInfoList()
    },

    resetItemVOs: function () {
      this.cycleItemVOs = []
      this.customItemVOs = []
    },

    strategyTypeSelectChange: function () {
      this.resetItemVOs()

      if (this.addStrategyForm.type === 'CYCLE_SCHEDULE') {
        this.addCycleTableRow()
        this.cyclesFormVisible = true
        this.customFormVisible = false
      } else if (this.addStrategyForm.type === 'CUSTOM_SCHEDULE') {
        this.addCustomTableRow()
        this.cyclesFormVisible = false
        this.customFormVisible = true
      }
    }

  },
  mounted () {
    console.log('recordTask vue mounted')
    this.queryRecordStrategyList()
  }
}
</script>
