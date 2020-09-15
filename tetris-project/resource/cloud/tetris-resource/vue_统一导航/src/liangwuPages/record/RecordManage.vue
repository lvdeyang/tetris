<template>
  <div class="record">
    <el-tabs v-model="activeName">
      <el-tab-pane label="录制列表" name="0">
        <!--筛选条件-->
        <div class="search">
          <el-input placeholder="输入录制任务名" v-model="filterText" style="width:300px; border:0;"></el-input>
          <el-select v-model="recordMode" placeholder="请选择">
            <el-option value="" label=""></el-option>
            <el-option value="手动" label="手动录制">手动录制</el-option>
            <el-option value="排期" label="排期录制">排期录制</el-option>
            <el-option value="定时" label="排期录制">定时录制</el-option>
            <el-option value="循环" label="循环录制">排期录制</el-option>
          </el-select>
          <el-date-picker v-model="date" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" v-if="recordMode!='手动'">
          </el-date-picker>
          <el-button type="default" @click="search">搜索</el-button>
        </div>
        <!--内容-->
        <template>
          <el-table :data="pageData">
            <el-table-column prop="fileName" label="录制任务名"></el-table-column>
            <el-table-column prop="type" label="录制模式">
              <template></template>
            </el-table-column>
            <el-table-column prop="videoSource" label="录制设备"></el-table-column>
            <!-- <el-table-column prop="audioSource" label="音频源"></el-table-column> -->
            <el-table-column prop="startTime" label="开始时间">
            </el-table-column>
            <el-table-column prop="endTime" label="结束时间"></el-table-column>
            <el-table-column prop="status" label="状态"></el-table-column>
            <el-table-column label="操作" width="250">
              <template slot-scope="scope">
                <el-button type="text" style="padding:0; font-size:20px;" title="导出">
                  <span class="feather-icon-log-out" @click="downLoadTask(scope.row)"></span>
                </el-button>
                <el-button type="text" style="padding:0; font-size:20px;" v-if="scope.row.status=='录制中'" title="停止">
                  <span class="feather-icon-pause-circle" @click="stopTask(scope.row)"></span>
                </el-button>
                <template v-else>
                  <el-button type="text" style="padding:0; font-size:20px;" title="开始录制">
                    <span class="feather-icon-youtube" @click="startTask(scope.row)"></span>
                  </el-button>
                  <el-button type="text" title="删除" style="padding:0; font-size:20px;">
                    <span class="icon-trash" @click="delTask(scope.row)"></span>
                  </el-button>
                </template>
                <!-- <el-tag type="primary" v-else>已停止</el-tag> -->
              </template>
            </el-table-column>
          </el-table>
        </template>
        <div style="text-align: right;background-color: #f2f2f2;">
          <el-pagination background @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize" :pager-count="5" layout="total, prev, pager, next" :total="filterDatas.length">
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="添加录制任务" name="1">
        <div style="height:50px">
          <el-select v-model="Mode" placeholder="请选择">
            <el-option value="手动" label="手动录制">手动录制</el-option>
            <el-option value="排期" label="排期录制">排期录制</el-option>
            <el-option value="定时" label="排期录制">定时录制</el-option>
            <el-option value="循环" label="排期录制">循环录制</el-option>
          </el-select>
        </div>

        <el-input v-model="inputDevice" placeholder="请选择录制设备" style="margin:10px 0" @click.native="handleDeviceDia"></el-input>
        <el-input v-model="inputName" placeholder="请输入录制任务名" style="margin:10px 0"></el-input>
        <el-input v-model="inputBit" placeholder="请输入磁盘大小" style="margin:10px 0" v-if="Mode==='循环'"></el-input>

        <el-date-picker v-if="Mode==='排期'" v-model="date" type="datetimerange" :picker-options='pickerBeginDateBefore' range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
        </el-date-picker>

        <div v-if="Mode=='定时' || Mode==='循环'">
          <el-date-picker v-model="startMonth" type="datetime" placeholder="选择日期时间" default-time="12:00:00">
          </el-date-picker>
          <span>至</span>
          <el-date-picker v-model="endMonth" type="datetime" placeholder="选择日期时间" default-time="12:00:00">
          </el-date-picker>
        </div>

        <!--底部按钮-->
        <div style="margin:10px 0;height:50px; width:95%; box-sizing:border-box;">
          <el-button size="small" @click="submit" type="primary">确定提交</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 设备树 -->
    <el-dialog title="录像设备" :visible.sync="dialogFormVisible">
      <div class="custom-tree-container" v-loading="treeLoading">
        <div>
          <el-tree ref="tree" :data="tree" default-expand-all node-key="id" highlight-current :expand-on-click-node="false" :props="defaultProps" @node-click="handleNodeClick">
          </el-tree>
          <!-- <el-tree :data="tree" :props="defaultProps" @node-click="handleNodeClick"></el-tree> -->
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button> -->
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.search {
  height: 45px;
  line-height: 45px;
}
</style>

<script>
import { getRecordDatas, initFolderTree } from '../../api/api'
export default {
  data: function () {
    return {
      filterText: '',
      recordMode: '', //录制模式  MANUAL(手动)|SCHEDULING(排期)
      date: '', //查询日期
      currentPage: 1,
      tasks: [],
      filterDatas: [],
      pageSize: 5,
      total: 0,
      stopId: "",
      Mode: '手动', //录制模式
      activeName: '0',
      inputName: '',
      inputDevice: '',
      date: '', //查询日期
      startMonth: '',
      endMonth: '',
      inputBit: '',
      dialogFormVisible: false,
      treeLoading: false,
      tree: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      defaultExpandKeys: [],
      currentSelectNode: null,
      expandNodeKeysArr: [],
    }
  },
  computed: {
    pageData: function () {
      return this.filterDatas.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    }
  },
  methods: {
    //当前页改变
    handleCurrentChange: function (val) {
      this.currentPage = val;
    },
    handleDeviceDia: function () {
      this.initTree();
      this.dialogFormVisible = true;

    },
    //  data:待过度的数据， type:
    search: function () {
      var self = this;
      var mode = this.recordMode;
      var name = this.filterText;
      var date = this.date;
      var page = this.currentPage;
      self.filterData(self.tasks, mode, name, date);
    },
    // 过滤数据的方法
    filterData: function (data, type, name, date) {
      var self = this;
      var startDate = this.format(date[0]);
      var endDate = this.format(date[1]);
      var temArr;
      if (!type && !name && !date) {
        return;
      }
      if (type) {
        temArr = data.filter(function (item) {
          return item.type.indexOf(type) > -1;
        })
      }
      if (name) {
        temArr = data.filter(function (item) {
          return item.fileName.indexOf(name) > -1;
        })
      }
      if (date) {
        temArr = data.filter(function (item) {
          return self.nowInDateBetwen(startDate, endDate, item.date)
        })
      }
      self.filterDatas = temArr;
    },

    nowInDateBetwen: function (d1, d2, date) {
      if (!d1 || !d2 || !date) return;
      //如果时间格式是正确的，那下面这一步转化时间格式就可以不用了
      var dateBegin = new Date(d1.replace(/-/g, "/"));//将-转化为/，使用new Date
      var dateEnd = new Date(d2.replace(/-/g, "/"));//将-转化为/，使用new Date
      var dateNow = new Date(date.replace(/-/g, "/"));//将-转化为/，使用new Date

      var beginDiff = dateNow.getTime() - dateBegin.getTime();//时间差的毫秒数
      var beginDayDiff = Math.floor(beginDiff / (24 * 3600 * 1000));//计算出相差天数

      var endDiff = dateEnd.getTime() - dateNow.getTime();//时间差的毫秒数
      var endDayDiff = Math.floor(endDiff / (24 * 3600 * 1000));//计算出相差天数
      if (endDayDiff < 0) {//已过期
        return false
      }
      if (beginDayDiff < 0) {//没到开始时间
        return false;
      }
      if (endDayDiff == 0) {
        return true
      }
      if (beginDayDiff == 0) {
        return true;
      }
      return true;
    },
    submit: function () {
      var self = this;
      var time1, time2;
      if (!self.inputName) {
        self.$message.warning('名称不能为空！');
        return;
      }
      if (self.Mode === '定时' || self.Mode === '循环') {
        time1 = self.format(self.startMonth);
        time2 = self.format(self.endMonth);
      } else if (self.Mode === '排期') {
        time1 = self.format(self.date[0]);
        time2 = self.format(self.date[1]);
      }
      self.filterDatas.push({
        type: self.Mode,
        fileName: self.inputName,
        startTime: time1,
        endTime: time2,
        mode: self.Mode,
        videoSource: "buzhiddao",
        audioSource: "kdsdfks",
        size: self.inputBit
      });
      this.$message('创建录制任务成功,请到录制列表查看');
      self.Mode = "";
      self.inputName = "";
      self.date = "";
      self.startMonth = "";
      self.endMonth = "";
      self.inputBit = "";
    },

    // 删除
    delTask: function (row) {
      this.filterDatas = this.filterDatas.filter(function (item) {
        return item.id != row.id;
      })
    },

    //停止任务
    stopTask: function (row) {
      var self = this;
      row.status = "未开始"
    },
    //开始任务
    startTask: function (row) {
      var self = this;
      row.status = "录制中"
    },
    //导出
    downLoadTask: function (row) {
      alert("该任务下无导出数据！")
    },
    //获取数据
    refreshRecord: function () {
      var self = this;
      self.tasks.splice(0, self.tasks.length);
      getRecordDatas().then(data => {
        var rows = data.rows;
        var total = data.total;
        if (rows && rows.length > 0) {
          for (var i = 0; i < rows.length; i++) {
            self.tasks.push(rows[i]);
            self.filterDatas.push(rows[i]);
          }
          self.total = total;
        }
      })
    },
    //格式化时间
    format: function (str) {
      if (str) {
        str = str.toString();
        var str = str.replace(/ GMT.+$/, '');// Or str = str.substring(0, 24)
        var d = new Date(str);
        var a = [d.getFullYear(), d.getMonth() + 1, d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds()];
        for (var i = 0, len = a.length; i < len; i++) {
          if (a[i] < 10) {
            a[i] = '0' + a[i];
          }
        }
        str = a[0] + '-' + a[1] + '-' + a[2] + ' ' + a[3] + ':' + a[4] + ':' + a[5];
        return str;
      }
    },
    initTree: function (keepExpand) {
      this.treeLoading = true;
      initFolderTree().then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
          this.treeLoading = false;
        } else {
          this.tree = res.tree;

          this.treeLoading = false;
          // console.log(JSON.stringify(res.tree));

          // if (!keepExpand) {
          //   // console.log('not keepExpand')
          //   this.expandNodeKeysArr.splice(0, this.expandNodeKeysArr.length);
          //   this.expandNodeKeysArr = this.tree.map(rootNode => rootNode.id);
          // }

          // this.defaultExpandKeys = this.expandNodeKeysArr;
          // this.treeLoading = false;
          // this.disableSetFolderBtn = true;
          // this.disableAddFolderBtn = true;
          // this.disableModifyFolderBtn = true;
          // this.disableDeleteFolderBtn = true;
          // this.disableResetRootBtn = false;
          // this.disableReleaseRootBtn = true;
        }
      });
    },
    handleNodeClick: function (data) {
      console.log(data)
      if (data.children) {
        return
      } else {
        console.log(data.name)
        this.inputDevice = data.name;
        this.dialogFormVisible = false;

      }
    }
  },
  mounted () {
    var self = this;
    self.refreshRecord();
  }
}
</script>  
