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
        table: {
          data: [],
          page: {
            currentPage: 0,
            pageSize: 20,
            total: 0
          },
          prependRow: ''
        },
        dialog: {
          visible: false,
          isCreate: false,
          editId: '',
          form: {
            id: '',
            titleName: '',
            beginTime: '',
            isCurrentTask: true
          },
        },
        tableData: [],
        activeName: '0',
        showBtn: true,
        row: '',
        curTask: "", //当前任务
        curId: 2,
        addDisabled: false,
      }
    },
    methods: {
      addTaskSubmit: function () {
        var self = this;
        if (!self.dialog.form.titleName) {
          self.$message({
            type: "waring",
            message: "任务名称不能为空!"
          })
          return
        } else if (!self.dialog.form.beginTime) {
          self.$message({
            type: "waring",
            message: "开始时间不能为空！"
          })
          return
        } else if (self.dialog.form.titleName.length > 20) {
          self.$message({
            type: "waring",
            message: "任务名不能大于20个字符！"
          })
          return
        }
        self.addDisabled = true;
        if (self.dialog.isCreate) {

          ajax.post('/command/system/title/add', self.dialog.form, function (data) {
            // self.table.data.push(data)
            self.$message({
              type: 'success',
              message: "新建任务成功！"
            })

            self.dialog.form = {
              id: '',
              titleName: '',
              beginTime: '',
              isCurrentTask: true
            }
            self.addDisabled = false;
            self.load(1)
            self.dialog.visible = false;
          });
        } else {
          ajax.post('/command/system/title/edit', self.dialog.form, function (data) {
            // self.table.data.push(data)
            self.$message({
              type: 'success',
              message: "修改任务成功！"
            })
            self.dialog.form = {
              id: '',
              titleName: '',
              beginTime: '',
              isCurrentTask: true
            }
            self.addDisabled = false;
            self.load(1)
            self.dialog.visible = false;
          });
        }
      },

      addTask: function () {
        this.dialog.visible = true;
        this.dialog.isCreate = true;
        this.dialog.title = "新建任务"
      },
      editTask: function (row) {
        this.dialog.visible = true;
        this.dialog.isCreate = false;
        this.dialog.editId = row.id;
        this.dialog.title = "修改任务";
        this.dialog.form.titleName = row.titleName;
        this.dialog.form.beginTime = row.beginTime;
        this.dialog.form.isCurrentTask = row.currentTask;
        this.dialog.form.id = row.id;
      },
      // 取消
      cancel: function () {
        this.dialog.visible = false;
        this.dialog.form.titleName = '';
        this.dialog.form.beginTime = '';
        this.dialog.form.isCurrentTask = true;
      },
      // 设为当前任务
      setCur: function (row) {
        var self = this;
        row.isCurrentTask = true;
        ajax.post('/command/system/title/edit', row, function (data) {
          // self.table.data.push(data)
          self.$message({
            type: 'success',
            message: "设置当前任务成功！"
          })
          self.load(1)
        });
        self.curTask = row.titleName;
        self.curId = row.id;
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
        var self = this;
        ajax.post('/command/system/title/delete', {
          id: row.id
        }, function (data) {
          self.table.data = self.table.data.filter(function (item) {
            return item.id != row.id;
          })
        });

      },
      load: function (current) {
        var self = this;
        var condition = {
          pageSize: self.table.page.pageSize,
          currentPage: current
        }
        self.table.data = [];
        ajax.post('/command/system/title/query', condition, function (data) {
          var total = data.total;
          var rows = data.rows;
          self.table.page.currentPage = current;
          self.table.page.total = total;
          if (rows && rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
              if (rows[i].currentTask) {
                self.curTask = rows[i].titleName;
                self.curId = rows[i].id;
              }
              self.table.data.push(rows[i]);

            }
          }

        });
      },

      handleSizeChange: function (pageSize) {
        var self = this;
        self.table.page.pageSize = pageSize;
        self.load(1);
      },
      handleCurrentChange: function (currentPage) {
        var self = this;
        self.load(currentPage);
      },
    },
    mounted: function () {
      var self = this;
      self.load(1)
    }
  });

  return Vue;
});