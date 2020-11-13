/**
 * Created by lzp on 2019/5/29.
 */
define([
  'text!' + window.APPPATH + 'liangwu-user/page-liangwu-user.html',
  window.APPPATH + 'liangwu-user/page-liangwu-user.i18n',
  'config',
  'restfull',
  'jquery',
  'context',
  'commons',
  'vue',
  'element-ui',
  'mi-frame',
  'mi-sub-title',
  'mi-user-dialog',
  'mi-upload-dialog',
  'css!' + window.APPPATH + 'liangwu-user/page-liangwu-user.css'
], function (tpl, i18n, config, ajax, $, context, commons, Vue) {

  var locale = context.getProp('locale');
  var i18n = !locale ? i18n.default : i18n[locale] ? i18n[locale] : i18n.default;

  var pageId = 'page-liangwu-user';

  var vueInstance = null;

  var init = function () {

    //设置标题
    commons.setTitle(pageId);

    var $page = document.getElementById(pageId);
    $page.innerHTML = tpl;

    vueInstance = new Vue({
      el: '#' + pageId + '-wrapper',
      data: {
        menus: context.getProp('menus'),
        user: context.getProp('user'),
        groups: context.getProp('groups'),
        i18n: i18n,
        roleOption: [],
        activeId: window.BASEPATH + 'index#/page-liangwu-user',
        dialogBindRole: {
          bindRoleDialogTableVisible: false,
          bindRoleSelection: []
        },
        importInfo: {
          status: false,
          totalUsers: 0,
          currentUser: 0,
          importTimes: 0,
          interval: ''
        },
        table: {
          rows: [],
          pageSize: 50,
          pageSizes: [50, 100, 200, 400],
          currentPage: 0,
          total: 0,
          condition: {
            nickname: '',
            userno: ''
          }
        },
        dialog: {
          createUser: {
            visible: false,
            nickname: '',
            username: '',
            userno: '',
            password: '',
            repeat: '',
            mobile: '',
            mail: '',
            level: 1,
            classify: '企业用户',
            company: {
              id: '',
              name: ''
            },
            loading: false,
            remark: '',
            loginIp: '',
            lastlogintime: '',
            bindrole: '',
            bindRoles: '',
            isLoginIp: true
          },
          editUser: {
            visible: false,
            editPassword: false,
            id: '',
            nickname: '',
            mobile: '',
            mail: '',
            level: 1,
            oldPassword: '',
            newPassword: '',
            repeat: '',
            loading: false,
            remark: '',
            loginIp: '',
            bindrole: '',
            bindRoles: '',
            bindAccessNodeUidName: '',
            bindAccessNodeUid: ''
          },
          import: {
            requireType: ['csv'],
            multiple: false
          },
        },

        bindAccessNodeUidVisable: false,
        accessNodeTable: [],
        bindAccessNodeUid: '',
        bindAccessNodeUidName: '',
        bindAccessNodeUidRow: undefined,
        isLoginIpDisabled: true
      },
      methods: {
        rowKey: function (row) {
          return 'user-' + row.uuid;
        },
        rowKeyBindRole: function (row) {
          return 'role-' + row.id;
        },
        handleChangeFolder: function () {
          var self = this;
          self.dialogBindRole.bindRoleDialogTableVisible = true;
          // self.$refs.roleTable.clearSelection()
          self.$nextTick(function () {
            var role = JSON.parse(self.dialog.editUser.bindRoles)
            self.roleOption.forEach(item => {
              if (role.includes(item.id)) {
                self.$refs.roleTable && self.$refs.roleTable.toggleRowSelection(item, true)
              } else {
                self.$refs.roleTable && self.$refs.roleTable.toggleRowSelection(item, false)
              }
            })
          })


        },
        handleChangeNodeUid: function () {
          var self = this;
          self.bindAccessNodeUidVisable = true;
          // self.$refs.roleTable.clearSelection()
          var self = this;
          self.accessNodeTable = [];
          // self.table.rows.splice(0, self.table.rows.length);
          ajax.post('/user/load', {
            currentPage: 1,
            pageSize: 10000,
          }, function (data) {
            var rows = data.rows;
            if (rows && rows.length > 0) {
              for (var i = 0; i < rows.length; i++) {
                self.accessNodeTable.push(rows[i]);
              }
              console.log(self.accessNodeTable)
            }
          });


        },

        currentRowChange: function (currentRow, oldRow) {
          var self = this;
          self.tbindAccessNodeUidRow = currentRow;
        },

        handleBindAccessNodeUidSubmit: function () {
          this.bindAccessNodeUidVisable = false;
          this.bindAccessNodeUidName = this.dialog.editUser.bindAccessNodeUidName = this.tbindAccessNodeUidRow.name
          this.bindAccessNodeUid = this.dialog.editUser.bindAccessNodeUid = this.tbindAccessNodeUidRow.nodeUid
        },
        handleBindRoleSubmit: function () {
          this.dialogBindRole.bindRoleDialogTableVisible = false;
          var bindRoleNameArr = [];
          var bindRoleIdArr = [];
          this.dialogBindRole.bindRoleSelection.map(item => {
            if (item.id == 6 || item.id == 7) {

            } else {
              bindRoleNameArr.push(item.name);
              bindRoleIdArr.push(item.id)
            }
          })
          this.dialog.createUser.bindrole = this.dialog.editUser.bindrole = bindRoleNameArr.join(',');
          this.dialog.createUser.bindRoles = this.dialog.editUser.bindRoles = JSON.stringify(bindRoleIdArr);
          // this.dialogBindRole.bindRoleSelection = [];
          this.$refs.roleTable.clearSelection()
        },
        gotoBindBusinessRole: function (scope) {
          var slef = this;
          var row = scope.row;
          window.location.hash = '#/page-bind-system-role/' + row.id + '/' + row.nickname + '/business';
        },
        load: function (currentPage) {
          var self = this;
          var param = {
            currentPage: currentPage,
            pageSize: self.table.pageSize
          };
          if (self.table.condition.nickname) param.nickname = self.table.condition.nickname;
          if (self.table.condition.userno) param.userno = self.table.condition.userno;
          self.table.rows.splice(0, self.table.rows.length);
          ajax.post('/user/find/by/company/id/and/condition', param, function (data) {
            var total = data.total;
            var rows = data.rows;
            self.table.rows = data.rows;
            self.table.total = total;
            self.table.currentPage = currentPage;
          });
        },
        // 获取角色信息
        loadRoleList: function (currentPage) {
          var self = this;
          self.table.rows.splice(0, self.table.rows.length);
          ajax.post('/business/role/list', {
            currentPage: 1,
            pageSize: 10000,
          }, function (data) {
            var rows = data.rows;
            if (rows && rows.length > 0) {
              for (var i = 0; i < rows.length; i++) {
                if (rows[i].id != 7 && rows[i].id != 6) {
                  self.roleOption.push(rows[i]);
                }
              }
            }
          });
        },

        handleSelectionChange (val) {
          this.dialogBindRole.bindRoleSelection = val;
        },
        loadCompany: function () {
          var self = this;
          ajax.post('/company/subordinate', {}, function (data, status) {
            if (status == 200 && data != null) {
              self.dialog.createUser.company.id = data.id;
              self.dialog.createUser.company.name = data.name;
            }
          }, null, ajax.NO_ERROR_CATCH_CODE)
        },
        handleCreate: function () {
          var self = this;
          self.dialog.createUser.visible = true;
        },
        handelDelete: function () {
          var self = this;
        },
        handleExport: function () {
          var self = this;
          ajax.download('/user/handle/export', null, function (data) {
            var $a = $('#page-business-user-export');
            $a[0].download = 'user.csv';
            $a[0].href = window.URL.createObjectURL(data);
            $a[0].click();
            self.$message({
              type: 'success',
              message: '操作成功'
            });
          });
        },
        handleImport: function () {
          var self = this;
          self.$refs.miUploadDialog.open();
        },
        fileSelected: function (files, done) {
          var self = this;
          var csv = files[0];
          var data = new FormData();
          data.append('csv', csv);
          ajax.upload('/user/handle/import', data, function (data) {
            self.$message({
              type: 'success',
              message: '操作成功'
            });
            done();
            //self.load(1);
            self.loopImportStatus();
          });
        },
        handelUserLockStatusChange: function (scope) {
          var self = this;
          var rows = self.table.rows;
          if (scope.row.locked) {
            ajax.post('/user/lock/' + scope.row.id, null, function (data) {
              for (var i = 0; i < rows.length; i++) {
                if (rows[i].id === data.id) {
                  rows[i].errorLoginTimes = data.errorLoginTimes;
                  rows[i].locked = true;
                  break;
                }
              }
              self.$message({
                type: 'success',
                message: '锁定成功！'
              });
            });
          } else {
            ajax.post('/user/unlock/' + scope.row.id, null, function (data) {
              for (var i = 0; i < rows.length; i++) {
                if (rows[i].id === data.id) {
                  rows[i].errorLoginTimes = data.errorLoginTimes;
                  rows[i].locked = false;
                  break;
                }
              }
              self.$message({
                type: 'success',
                message: '解除锁定成功！'
              });
            });
          }
        },
        handleRowEdit: function (scope) {
          var self = this;
          var row = scope.row;
          var businessRoles = JSON.parse(row.businessRoles),
            rolesName = [],
            rolesId = [];
          for (var i = 0; i < businessRoles.length; i++) {
            if (businessRoles[i].id != '7' && businessRoles[i].id != '6') {//去掉媒资用户
              rolesName.push(businessRoles[i].name)
              rolesId.push(businessRoles[i].id)
            }
          }
          self.dialog.editUser.id = row.id;
          self.dialog.editUser.nickname = row.nickname;
          self.dialog.editUser.mobile = row.mobile;
          self.dialog.editUser.mail = row.mail;
          self.dialog.editUser.level = row.level ? row.level : 1;
          self.dialog.editUser.visible = true;
          self.dialog.editUser.remark = row.remark;
          self.dialog.editUser.loginIp = row.loginIp;
          self.dialog.editUser.bindrole = rolesName.join(',');
          self.dialog.editUser.bindRoles = JSON.stringify(rolesId);
        },
        handleEditUserClose: function () {
          var self = this;
          self.dialog.editUser.id = '';
          self.dialog.editUser.nickname = '';
          self.dialog.editUser.mobile = '';
          self.dialog.editUser.mail = '';
          self.dialog.editUser.level = 1;
          self.dialog.editUser.editPassword = false;
          self.dialog.editUser.oldPassword = '';
          self.dialog.editUser.newPassword = '';
          self.dialog.editUser.repeat = '';
          self.dialog.editUser.visible = false;
          self.dialog.editUser.remark = '';
          self.dialog.editUser.loginIp = '';
          self.dialog.editUser.bindrole = '';
          self.dialog.editUser.bindRoles = ''
        },
        handleEditUserSubmit: function () {
          var self = this;
          if (!self.dialog.editUser.bindAccessNodeUid) {
            self.$message({
              'type': 'waring',
              'message': "服务节点不能为空"
            })
            return
          }
          if (!self.dialog.editUser.bindrole) {
            self.$message({
              'type': 'waring',
              'message': "绑定角色不能为空"
            })
            return
          }
          self.dialog.editUser.loading = true;
          self.dialog.editUser.resetPermissions = true;
          ajax.post('/user/edit/' + self.dialog.editUser.id, self.dialog.editUser, function (data, status) {
            self.dialog.editUser.loading = false;
            if (status !== 200) return;
            for (var i = 0; i < self.table.rows.length; i++) {
              if (self.table.rows[i].id === self.dialog.editUser.id) {
                self.table.rows.splice(i, 1, data);
                break;
              }
            }
            self.handleEditUserClose();
          }, null, ajax.NO_ERROR_CATCH_CODE);
        },
        handleRowDelete: function (scope) {
          var self = this;
          var row = scope.row;
          var h = self.$createElement;
          self.$msgbox({
            title: '危险操作',
            message: h('div', null, [
              h('div', {
                class: 'el-message-box__status el-icon-warning'
              }, null),
              h('div', {
                class: 'el-message-box__message'
              }, [
                h('p', null, ['此操作将永久删除该用户，且不可恢复，是否继续?'])
              ])
            ]),
            type: 'wraning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            beforeClose: function (action, instance, done) {
              instance.confirmButtonLoading = true;
              if (action === 'confirm') {
                ajax.post('/user/delete/' + row.id, null, function (data, status) {
                  instance.confirmButtonLoading = false;
                  if (status !== 200) return;
                  for (var i = 0; i < self.table.rows.length; i++) {
                    if (self.table.rows[i].id === row.id) {
                      self.table.rows.splice(i, 1);
                      break;
                    }
                  }
                  done();
                }, null, ajax.NO_ERROR_CATCH_CODE);
              } else {
                instance.confirmButtonLoading = false;
                done();
              }
            }
          }).catch(function () { });
        },
        handleCreateUserClose: function () {
          var self = this;
          self.dialog.createUser.nickname = '';
          self.dialog.createUser.username = '';
          self.dialog.createUser.userno = '';
          self.dialog.createUser.password = '';
          self.dialog.createUser.repeat = '';
          self.dialog.createUser.mobile = '';
          self.dialog.createUser.mail = '';
          self.dialog.createUser.level = 1;
          self.dialog.createUser.classify = '企业用户';
          self.dialog.createUser.visible = false;
          self.dialog.createUser.remark = "";
          self.dialog.createUser.loginIp = "";
          self.dialog.createUser.bindrole = [];
          self.dialog.createUser.bindRoles = []
        },
        handleCreateUserSubmit: function () {
          var self = this;
          if (!self.bindAccessNodeUid) {
            self.$message({
              'type': 'waring',
              'message': "服务节点不能为空"
            })
            return
          }
          if (!self.dialog.createUser.bindRoles) {
            self.$message({
              'type': 'waring',
              'message': "绑定角色不能为空"
            })
            return
          }
          var rundemNum = self.randomString(11);
          self.dialog.createUser.loading = true;
          var params = {
            nickname: self.dialog.createUser.nickname,
            username: self.dialog.createUser.username,
            userno: rundemNum,
            password: self.dialog.createUser.password,
            repeat: self.dialog.createUser.repeat,
            mobile: self.dialog.createUser.mobile,
            mail: self.dialog.createUser.mail,
            level: self.dialog.createUser.level,
            classify: self.dialog.createUser.classify,
            companyId: self.dialog.createUser.company.id,
            remark: self.dialog.createUser.remark,
            loginIp: self.dialog.createUser.loginIp,
            bindrole: self.dialog.createUser.bindrole,
            bindRoles: self.dialog.createUser.bindRoles,
            worknodeUid: self.bindAccessNodeUid,
            isLoginIp: self.dialog.createUser.isLoginIp,
          };

          ajax.post('/user/add', params, function (data, status) {
            self.dialog.createUser.loading = false;
            if (status !== 200) return;
            self.table.rows.push(data);
            self.handleCreateUserClose();
          }, null, ajax.NO_ERROR_CATCH_CODE);
        },
        handleSizeChange: function (size) {
          var self = this;
          self.table.pageSize = size;
          self.load(self.table.currentPage);
        },
        handleCurrentChange: function (currentPage) {
          var self = this;
          self.load(currentPage);
        },
        importStatus: function () {
          var self = this;
          ajax.post('/user/query/import/status', null, function (data) {
            self.importInfo.status = data.status;
            self.importInfo.totalUsers = data.totalUsers;
            self.importInfo.currentUser = data.currentUser;
            self.importInfo.importTimes = data.importTimes;
            if (data.status && !self.importInfo.interval) {
              self.loopImportStatus();
            }
            if (!data.status && self.importInfo.interval) {
              clearInterval(self.importInfo.interval);
              self.load(1);
              console.log('清除interval');
            }
          });
        },
        loopImportStatus: function () {
          var self = this;
          self.importStatus();
          self.importInfo.interval = setInterval(function () {
            self.importStatus();
          }, 5000);
        },
        //格式化时间
        format: function (str) {
          if (str) {
            str = str.toString();
            var str = str.replace(/ GMT.+$/, ''); // Or str = str.substring(0, 24)
            var d = new Date(str);
            var a = [d.getFullYear(), d.getMonth() + 1, d.getDate() - Math.floor(Math.random() * 5), Math.floor(Math.random() * 24), Math.floor(Math.random() * 58), Math.floor(Math.random() * 58)];
            for (var i = 0, len = a.length; i < len; i++) {
              if (a[i] < 10) {
                a[i] = '0' + a[i];
              }
            }
            str = a[0] + '-' + a[1] + '-' + a[2] + ' ' + a[3] + ':' + a[4] + ':' + a[5];
            return str;
          }
        },
        randomString: function (len) {
          len = len || 11;
          var $chars = '0123456789';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
          var maxPos = $chars.length;
          var pwd = '';
          for (var i = 0; i < len; i++) {
            pwd += $chars.charAt(Math.floor(Math.random() * maxPos));

          }
          return pwd;

        },
        isLoginIpChange: function (val) {
          if (val) {
            this.dialog.createUser.loginIp = "";
            this.isLoginIpDisabled = true
          } else {
            this.isLoginIpDisabled = false

          }
        }
      },
      created: function () {
        var self = this;
        self.load(1);
        self.loadCompany();
        self.importStatus();
        self.loadRoleList(1)
      }
    });
  };

  var destroy = function () {
    if (vueInstance.importInfo.interval) clearInterval(vueInstance.importInfo.interval);
  };

  var groupList = {
    path: '/' + pageId,
    component: {
      template: '<div id="' + pageId + '" class="page-wrapper"></div>'
    },
    init: init,
    destroy: destroy
  };

  return groupList;
});