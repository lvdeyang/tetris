define([
  'text!' + window.APPPATH + 'component/bvc2-monitor-currenttask/bvc2-monitor-currenttask.html',
  'context',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'sortablejs',
  'vue-draggable',
  'date',
  'css!' + window.APPPATH + 'component/bvc2-monitor-currenttask/bvc2-monitor-currenttask.css'
], function (tpl, context, ajax, $, Vue) {

  //组件名称
  var pluginName = 'bvc2-monitor-currenttask';

  var app = context.getProp('app');
  Vue.component(pluginName, {
    props: [],
    template: tpl,
    data: function () {
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
        }, ],
        formLabelAlign: {
          name: '',
          time: '',
          address: '',
          isCurrent: true,
        },
        activeName: '0',
        showBtn: true,
        row: '',
        curTask: "05-XXX", //当前任务
        curId: 2
      }
    },
    methods: {
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
      tableRowClass: function ({
        row,
        rowIndex
      }) {
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
    mounted: function () {
      var self = this;
    }
  });

  return Vue;
});