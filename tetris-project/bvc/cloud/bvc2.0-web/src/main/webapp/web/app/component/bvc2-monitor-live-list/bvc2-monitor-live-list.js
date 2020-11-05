define([
  'text!' + window.APPPATH + 'component/bvc2-monitor-live-list/bvc2-monitor-live-list.html',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'date',
  'bvc2-dialog-single-osd',
  'bvc2-monitor-forword',
  'css!' + window.APPPATH + 'component/bvc2-monitor-live-list/bvc2-monitor-live-list.css'
], function (tpl, ajax, $, Vue) {

  //组件名称
  var pluginName = 'bvc2-monitor-live-list';

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
          }
        },
        dialog: {
          forword: {
            visible: false
          }
        },
        buttonIsShow: true
      }
    },
    computed: {

    },
    watch: {},
    methods: {
      load: function (currentPage) {
        var self = this;
        self.table.data.splice(0, self.table.data.length);
        ajax.post('/monitor/live/load/device/lives', {
          currentPage: currentPage,
          pageSize: self.table.page.pageSize
        }, function (data) {
          var total = data.total;
          var rows = data.rows;
          if (rows && rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
              self.table.data.push(rows[i]);
            }
          }
          self.table.page.total = total;
          self.table.page.currentPage = currentPage;
        });
      },
      rowDelete: function (scope) {
        var self = this;
        var row = scope.row;
        var h = self.$createElement;
        self.$msgbox({
          title: '操作提示',
          message: h('div', null, [
            h('div', {
              class: 'el-message-box__status el-icon-warning'
            }, null),
            h('div', {
              class: 'el-message-box__message'
            }, [
              h('p', null, ['是否要停止此调阅任务?'])
            ])
          ]),
          type: 'wraning',
          showCancelButton: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: function (action, instance, done) {
            instance.confirmButtonLoading = true;
            if (action === 'confirm') {
              ajax.post('/monitor/live/stop/live/device/' + row.id, null, function (data, status) {
                instance.confirmButtonLoading = false;
                done();
                if (status !== 200) return;
                for (var i = 0; i < self.table.data.length; i++) {
                  if (self.table.data[i].id === row.id) {
                    self.table.data.splice(i, 1);
                    break;
                  }
                }
                self.table.page.total -= 1;
                if (self.table.data.length <= 0 && self.table.page.currentPage > 1) {
                  self.load(self.table.page.currentPage - 1);
                }
              }, null, [403, 404, 409, 500]);
            } else {
              instance.confirmButtonLoading = false;
              done();
            }
          }
        }).catch(function () {});
      },
      changeOsd: function (scope) {
        var self = this;
        var row = scope.row;
        self.$refs.bvc2DialogSingleOsd.open(row.osdId ? row.osdId : null);
        self.$refs.bvc2DialogSingleOsd.setBuffer(row);
      },
      onSelectedOsd: function (osd, done, endLoading) {
        var self = this;
        var task = self.$refs.bvc2DialogSingleOsd.getBuffer();
        if (task.osdId != osd.id) {
          ajax.post('/monitor/live/change/osd', {
            liveId: task.id,
            osdId: osd.id
          }, function (data, status) {
            endLoading();
            if (status !== 200) return;
            task.osdId = data.id;
            task.osdName = data.name;
            task.osdUsername = data.username;
            done();
          }, null, [403, 404, 408, 409]);
        } else {
          done();
          endLoading();
        }
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
      osdControl: function () {

      },
      handleforwordClose: function () {
        this.dialog.forword.visible = false;

      },
      openForword() {
        this.dialog.forword.visible = true;
      }
    },
    mounted: function () {
      var self = this;
      self.load(1);

    },
    updated() {

    },
  });

  return Vue;
});