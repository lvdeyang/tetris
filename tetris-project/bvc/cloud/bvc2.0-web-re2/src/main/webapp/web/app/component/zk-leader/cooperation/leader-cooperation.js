define([
  'text!' + window.APPPATH + 'component/zk-leader/cooperation/leader-cooperation.html',
  'restfull',
  'jquery',
  'vue',
  'config',
  'element-ui',
  'css!' + window.APPPATH + 'component/zk-leader/cooperation/leader-cooperation.css'
], function (tpl, ajax, $, Vue, config) {

  //组件名称
  var pluginName = 'leader-cooperation';

  Vue.component(pluginName, {
    props: [],
    template: tpl,
    data: function () {
      return {
        baseUrl: window.BASEPATH,
        qt: '',
        groupId: '',
        groupType: '',
        page: '',//应该显示哪个页面
        pageName: '', //多个页面公用，区分
        tag: '',
        replace: {
          props: {
            children: 'children',
            label: 'name'
          },
          data: [],
          select: []
        },

        bereplace: {
          props: {
            children: 'children',
            label: 'name'
          },
          data: [],
          select: []
        },
        //协同的人员数组
        saveSelect: [],
        filterText: '',
      }
    },
    watch: {
      // filterText: function (val) {
      //   this.$refs.tree.filter(val);
      // }
    },
    methods: {

      //关闭弹窗事件
      handleWindowClose: function () {
        var self = this;
        self.qt.destroy();
      },
      //添加提交事件
      handleAddMemberCommit: function () {
        var self = this;
        console.log(self.replace.select)
        console.log(self.bereplace.select)
        if (self.replace.select.length !== 1 || self.bereplace.select.length !== 1) {
          self.$message.error('接替成员和被接替成员分别有且只有一位！')
          return;
        }

        ajax.post('/command/replace/grant', { id: self.groupId, opId: self.replace.select[0], targetId: self.bereplace.select[0] }, function (data) {
          self.qt.linkedWebview('rightBar', $.toJSON({ id: 'getCommandListFun' }));
          self.handleWindowClose()
        })

      }
    },
    mounted: function () {
      var self = this;
      self.qt = new QtContext('leaderCooperation', function () {
        var params = self.qt.getWindowParams();
        self.groupId = params.id;
        self.groupType = params.type;
        self.page = params.page;
        self.pageName = params.name;
        self.tag = params.tag;

        //初始化ajax
        ajax.init({
          login: config.ajax.login,
          authname: config.ajax.authname,
          debug: config.ajax.debug,
          messenger: {
            info: function (message, status) {
              self.qt.info(message)
            },
            success: function (message, status) {
              self.qt.success(message)
            },
            warning: function (message, status) {
              self.qt.warning(message)
            },
            error: function (message, status) {
              self.qt.error(message)
            }
          }
        });
        self.replace.data = [];
        self.bereplace.data = [];

        ajax.post('/command/basic/query/members', { id: self.groupId }, function (data) {
          self.replace.data = JSON.parse(JSON.stringify(data.members));
          self.bereplace.data = JSON.parse(JSON.stringify(data.members));
        })
      });
    }
  });

  return Vue;
});