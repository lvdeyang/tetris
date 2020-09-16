<template>
  <div class="curTask">
    <el-tabs v-model="activeName">
      <el-tab-pane label="任务列表" name="0">
        <p>当前任务：{{curTask}}</p>
        <el-table :data="tableData" :row-class-name="tableRowClass">
          <el-table-column prop="name" label="任务名称" width="180"></el-table-column>
          <el-table-column prop="date" label="开始时间" width="180"></el-table-column>
          <!-- <el-table-column prop="address" label="地址"></el-table-column> -->
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="text" @click="setCur(scope.row)" v-if="scope.row.id!=curId">设为当前任务</el-button>
              <el-tag type="success" v-else>这条数据是当前任务</el-tag>
              <el-button @click="handleEdit(scope.row)" type="text" size="small">修改</el-button>
              <el-button type="text" size="small" @click="deleteRow(scope.row)" v-show="scope.row.id!=curId">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="添加任务" name="1">
        <el-form label-width="80px" :model="formLabelAlign">
          <el-form-item label="任务名称">
            <el-input v-model="formLabelAlign.name"></el-input>
          </el-form-item>
          <el-form-item label="开始时间">
            <el-date-picker v-model="formLabelAlign.time" type="datetime" placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
          <!-- <el-form-item label="活动地点">
            <el-input v-model="formLabelAlign.address"></el-input>
          </el-form-item> -->
          <el-formm-item>
            <el-button type="primary" @click="createTask" v-show="showBtn">创建任务</el-button>
            <el-button type="primary" @click="createTask" v-show="!showBtn">修改任务</el-button>
            <el-button @click="cancel">取消创建</el-button>
          </el-formm-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>

</template>

<style>
p {
  line-height: 30px;
  background-color: #ececec;
  font-size: 16px;
  color: #000;
}
.el-table .success-row {
  background: #f0f9eb;
}
</style>

 <script>
export default {
  data () {
    return {
      tableData: [{
        id: 1,
        date: '2020-05-02',
        name: '联试联调',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        id: 2,
        date: '2020-08-04',
        name: '05-XXX',
        address: '上海市普陀区金沙江路 1517 弄'
      },],
      formLabelAlign: {
        name: '',
        time: '',
        address: ''
      },
      activeName: '0',
      showBtn: true,
      row: '',
      curTask: "05-XXX", //当前任务
      curId: 2
    }
  },
  methods: {
    // 创建任务
    createTask: function () {
      var self = this;
      if (self.showBtn) {
        this.tableData.push({
          date: self.formLabelAlign.time,
          name: self.formLabelAlign.name,
          address: self.formLabelAlign.address,
        });
        this.$message('创建任务成功,请到任务列表查看');
      } else {
        console.log(self.row)
        console.log('xiugai')
        this.tableData.forEach(function (item, index) {
          if (item.id == self.row.id) {
            console.log(item)
            item.name = self.formLabelAlign.name;
            item.date = self.formLabelAlign.time;
            item.address = self.formLabelAlign.address;
          }
        })
        console.log(self.tableData)
      }

      self.formLabelAlign.time = "";
      self.formLabelAlign.name = "";
      self.formLabelAlign.address = "";
    },
    // 取消
    cancel: function () {
      self.formLabelAlign.time = "";
      self.formLabelAlign.name = "";
      self.formLabelAlign.address = "";
    },
    // 修改
    handleEdit: function (row) {
      var self = this;
      this.activeName = '1';
      this.row = row;
      self.formLabelAlign.time = row.date;
      self.formLabelAlign.name = row.name;
      self.formLabelAlign.address = row.address;
      self.showBtn = false;
    },
    // 设为当前任务
    setCur: function (row) {
      this.curTask = row.name;
      this.curId = row.id;
    },
    tableRowClass: function ({ row, rowIndex }) {
      if (row.id == this.curId) {
        return 'success-row';
      }
    },
    // 删除
    deleteRow: function (row) {
      this.tableData = this.tableData.filter(function (item) {
        return item.id != row.id;
      })
    }
  },
}
</script>
