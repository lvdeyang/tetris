define([
  'text!' + window.APPPATH + 'component/bvc2-monitor-forword-bandwidth/bvc2-monitor-forword-bandwidth.html',
  'context',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'date',
  'css!' + window.APPPATH + 'component/bvc2-monitor-forword-bandwidth/bvc2-monitor-forword-bandwidth.css'
], function (tpl, context, ajax, $, Vue) {

  //组件名称
  var pluginName = 'bvc2-monitor-forword-bandwidth';

  var app = context.getProp('app');
  Vue.component(pluginName, {
    props: [],
    template: tpl,
    data: function () {
      return {
        table: {
          data: []
        },
        title: "新建站点",
        activeName: '',
        dialog: {
          addStation: {
            visible: false,
            form: {
              id: "",
              stationName: '',
              totalWidth: '',
              singleWidth: ''
            }
          },

        }

      }
    },
    methods: {
      rowDelete: function (row) {
        var self = this;
        ajax.post('/command/station/bandwidth/delete', {
          id: row.id
        }, function (data, status) {
          if (status == 200) {
            self.$message({
              type: "success",
              message: "删除成功！"
            });
            self.load();
          }
        })
      },
      rowEdit: function (row) {
        var self = this;
        self.activeName = "1"
        self.title = "修改站点";
        self.dialog.addStation.form.id = row.id;
        self.dialog.addStation.form.stationName = row.stationName;
        self.dialog.addStation.form.totalWidth = row.totalWidth;
        self.dialog.addStation.form.singleWidth = row.singleWidth;
      },
      load: function () {
        var self = this;
        self.title = "新建站点"
        ajax.post('/command/station/bandwidth/query', null, function (data, status) {
          if (status == 200) {
            self.table.data = data.rows;
          }
        })
      },
      openAddStation: function (params) {
        var self = this;
        self.dialog.addStation.visible = true;
      },
      onSubmit: function () {
        var self = this;
        if (!self.dialog.addStation.form.stationName) {
          self.$message({
            type: "waring",
            message: "站点名称不能为空！"
          })
          return
        }
        if (!self.dialog.addStation.form.totalWidth) {
          self.$message({
            type: "waring",
            message: "总带宽不能为空！"
          })
          return
        }
        if (!self.dialog.addStation.form.singleWidth) {
          self.$message({
            type: "waring",
            message: "单路带宽不能为空！"
          })
          return
        }
        var url = "";
        if (self.title == "新建站点") {
          url = '/command/station/bandwidth/add'
        } else {
          url = '/command/station/bandwidth/edit'
        }
        ajax.post(url, self.dialog.addStation.form, function (data, status) {
          if (status == 200) {
            self.$message({
              type: "success",
              message: "保存成功！"
            });
            self.dialog.addStation.form.id = "";
            self.dialog.addStation.form.stationName = "";
            self.dialog.addStation.form.totalWidth = "";
            self.dialog.addStation.form.singleWidth = "";
            self.load();
          }
        })
      }
    },
    mounted: function () {
      var self = this;
      self.load()
    }
  });

  return Vue;
});