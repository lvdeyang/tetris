define([
  'text!' + window.APPPATH + 'component/zk-leader/dialog/create-group/leader-dialog-create-group.html',
  'restfull',
  'jquery',
  'vue',
  'config',
  'element-ui',
  'css!' + window.APPPATH + 'component/zk-leader/dialog/create-group/leader-dialog-create-group.css'
], function (tpl, ajax, $, Vue, config) {

  //组件名称
  var pluginName = 'leader-dialog-create-group';

  Vue.component(pluginName, {
    props: [],
    template: tpl,
    data: function () {
      return {
        baseUrl: window.BASEPATH,
        commandName: '',
        filterText: '', //搜索用户输入框
        searchData: [], //搜索结果数据
        tree: {
          props: {
            children: 'children',
            label: 'name',
            isLeaf: 'leaf'
          }
        },
        level2: [], //2级
        //下拉菜单选中的
        selected1: '',
        selected2: '',
        selected3: '',
        selected4: '',
        selected5: '',
        //下拉选项的
        options2: [],
        options3: [], //3级菜单
        options4: [], //4级菜单
        options5: [], //5级菜单
        options6: [], //6级菜单
        tableData: [], //表格的数据
        //分页
        leftCurrentPage: 1, //左侧当前页
        rightCurrentPage: 1, //右侧当前页
        checkBoxData: [],
        selectDelete: [], //多选删除
        searchText: '', //搜索
        // 对话框的
        dialogVisible: false,
        dialogInput: '嘻嘻',
      }
    },
    watch: {
      //监听左侧table页面变化，保持勾选的状态
      pageData: function () {
        var self = this;
        this.$nextTick(function () {
          self.checkBoxData.forEach(function (item) {
            if (self.pageData.indexOf(item) > -1) {
              //保证翻页后，勾选状态不变
              self.$refs.multipleTable.toggleRowSelection(item, true);
            }
          })
        })
      }
    },
    computed: {
      //左侧的分页
      pageData: function () {
        if (this.searchData.length) {
          return this.searchData.slice((this.leftCurrentPage - 1) * 10, this.leftCurrentPage * 10);
        } else if (this.tableData.length) {
          return this.tableData.slice((this.leftCurrentPage - 1) * 10, this.leftCurrentPage * 10);
        }
      },
      //右侧的分页
      selectedPage: function () {
        return this.searchSelectedData.slice((this.rightCurrentPage - 1) * 10, this.rightCurrentPage * 10);
      },
      // 搜索出来的数据渲染到右侧table里
      searchSelectedData: function () {
        const val = this.searchText;
        if (val) {
          return this.checkBoxData.filter(function (item) {
            return item.name == val || item.name.indexOf(val) > -1;
          })
        }
        return this.checkBoxData;
      },
      // 搜索出来的数据渲染到左侧table里
      searchTableData: function () {
        if (this.searchData.length > 0) {
          return this.searchData;
        }
        return this.tableData;
      }
    },
    methods: {
      // 循环遍历
      searchFolder: function (l, text) {
        var self = this;
        if (l.type !== 'USER') {
          if (l.name.indexOf(text) > -1) {
            self.deepPush(l);
            return;
          }
          if (l.children && l.children.length) {
            l.children.forEach(function (l2) {
              self.searchFolder(l2, text);
            })
          }
        } else {
          if (l.name.indexOf(text) > -1) {
            if (self.searchData.indexOf(l) === -1) {
              self.searchData.push(l);
            }
          }
        }
      },
      deepPush: function (l) {
        var self = this;
        if (l.children && l.children.length) {
          l.children.forEach(function (l2) {
            if (l2.type === 'USER') {
              self.searchData.push(l2);
            } else {
              self.deepPush(l2);
            }
          })
        }
      },
      //搜索功能，先前台处理，需要后台写接口
      changeSearch: function () {
        var text = this.filterText.trim();
        var self = this;
        this.searchData = [];
        this.level2.forEach(function (l) {
          self.searchFolder(l, text);
        });
      },

      //绑定事件，给二级菜单赋值
      func2: function () {
        var self = this;
        self.tableData = []; //先清空
        self.searchData=[];
        self.selected2 = '';
        self.selected3 = '';
        self.selected4 = '';
        self.selected5 = '';
        self.options3 = []; //3级菜单
        self.options4 = []; //4级菜单
        self.options5 = []; //5级菜单
        self.options6 = []; //6级菜单
        this.level2.forEach(function (value) {
          if (self.selected1 == value.id) {
            if (value.children && value.children.length > 0) {
              value.children.forEach(function (value2) {
                if (value2.type == 'FOLDER') {
                  self.options3.push(value2);
                } else {
                  self.tableData.push(value2);
                }
              })
            } else {
              self.tableData = [];
            }
          }
        });
      },

      //绑定事件，给三级菜单赋值
      func3: function () {
        var self = this;
        self.tableData = []; //先清空
        self.searchData=[];

        self.selected3 = '';
        self.selected4 = '';
        self.selected5 = '';

        self.options4 = []; //4级菜单
        self.options5 = []; //5级菜单
        self.options6 = []; //6级菜单`
        this.options3.forEach(function (value) {
          if (self.selected2 == value.id) {
            if (value.children && value.children.length > 0) {
              value.children.forEach(function (value2) {
                if (value2.type == 'FOLDER') {
                  self.options4.push(value2);
                } else {
                  self.tableData.push(value2);
                }
              })
            } else {
              self.tableData = [];
            }
          }
        })
      },

      //绑定事件，给四级菜单赋值
      func4: function () {
        var self = this;
        self.tableData = []; //先清空
        self.searchData=[];

        self.selected4 = '';
        self.selected5 = '';

        self.options5 = []; //5级菜单
        self.options6 = []; //6级菜单
        this.options4.forEach(function (value) {
          if (self.selected3 == value.id) {
            if (value.children && value.children.length > 0) {
              value.children.forEach(function (value2) {
                if (value2.type == 'FOLDER') {
                  self.options5.push(value2);
                } else {
                  self.tableData.push(value2);
                }
              })
            } else {
              self.tableData = [];
            }
          }
        })
      },

      //绑定事件，给五级菜单赋值
      func5: function () {
        var self = this;
        self.tableData = []; //先清空
        self.searchData=[];
        self.selected5 = '';
        self.options6 = []; //6级菜单
        this.options5.forEach(function (value) {
          if (self.selected4 == value.id) {
            if (value.children && value.children.length > 0) {
              value.children.forEach(function (value2) {
                if (value2.type == 'FOLDER') {
                  self.options6.push(value2);
                } else {
                  self.tableData.push(value2);
                }
              })
            } else {
              self.tableData = [];
            }
          }
        })
      },

      //绑定事件，给六级菜单赋值
      func6: function () {
        var self = this;
        self.tableData = []; //先清空
        self.searchData=[];
        this.options6.forEach(function (value) {
          if (self.selected5 == value.id) {
            self.tableData = value.children;
          }
        })
      },

      //左侧table的当前页改变
      handleCurrentChange: function (val) {
        this.leftCurrentPage = val;
      },

      //右侧table的当前页改变
      selectCurrentChange: function (val) {
        this.rightCurrentPage = val;
      },

      //左侧 table的 复选框点击事件
      handleSelect: function (selectArr, val) {
        var self = this;
        var idx = self.checkBoxData.indexOf(val);
        if (idx > -1) {
          self.checkBoxData.splice(idx, 1);
        } else {
          self.checkBoxData.push(val);
        }
      },

      // 右侧表格翻页 保持选中状态 ：保存选中的数据id,row-key就是要指定一个key标识这一行的数据
      getRowKey: function (row) {
        return row.id
      },

      //右侧表格的选中事件,把选中的存一个新的变量里
      deleteChange: function (val) {
        this.selectDelete = val;
      },

      //左侧 表格上的全选功能
      selectCurrentPage: function (val) {
        var self = this;
        if (val.length === 0) { //取消全选
          self.checkBoxData = self.checkBoxData.filter(function (item) {
            return self.pageData.indexOf(item) === -1;
          })
        } else if (val.length === self.pageData.length) { //全选，有的话不管，没有的话添加
          self.pageData.forEach(function (item) {
            if (self.checkBoxData.indexOf(item) === -1) {
              self.checkBoxData.push(item)
            }
          })
        }
      },

      //左侧一键全选按钮
      selectAll: function () {
        var self = this;
        self.tableData.forEach(function (item) {
          if (self.checkBoxData.indexOf(item) === -1) {
            self.checkBoxData.push(item)
          }
        })
        self.pageData.forEach(function (item) {
          //让页面上全是勾选状态
          self.$refs.multipleTable.toggleRowSelection(item, true);
        })
      },

      //左侧一键取消全选按钮
      unsetAll: function () {
        var self = this;
        self.pageData.forEach(function (item) {
          self.checkBoxData.forEach(function (val, index) {
            if (val.id === item.id) {
              self.checkBoxData.splice(index, 1);
            }
          })
          self.$refs.multipleTable.toggleRowSelection(item, false);
        })
      },

      //右侧 取消多个按钮
      unsetSomeAll: function () {
        var self = this;
        this.selectDelete.forEach(function (value, index) {
          self.checkBoxData.forEach(function (value2, index2) {
            if (value.id === value2.id) {
              self.checkBoxData.splice(index2, 1);
              self.$refs.multipleTable.toggleRowSelection(value, false);
            }
          })
        })
      },

      //右侧Table的删除（单个）
      deleteSelected: function (val) {
        this.checkBoxData.splice(this.checkBoxData.indexOf(val), 1);
        this.$refs.multipleTable.toggleRowSelection(val);
      },

      //下拉菜单移开时赋值
      searchBlur: function (e, num) {
        if (num === 1) {
          this.selected1 = e.target.value;
        } else if (num === 2) {
          this.selected2 = e.target.value;
        } else if (num === 3) {
          this.selected3 = e.target.value;
        } else if (num === 4) {
          this.selected4 = e.target.value;
        } else if (num === 5) {
          this.selected5 = e.target.value;
        }
      },

      //清空按钮
      reset: function () {
        this.selected1 = '';
        this.selected2 = '';
        this.selected3 = '';
        this.selected4 = '';
        this.selected5 = '';
        this.tableData = [];
      },

      // 获取菜单数据
      refreshInstitution: function () {
        var self = this;
        ajax.post('/command/query/find/institution/tree/user/filter/0', null, function (data) {
          if (data && data.length > 0) {
            for (var i = 0; i < data.length; i++) {
              self.level2 = self.level2.concat(data[i].children);
            }
            self.options2 = self.level2;
          }
        });
      },

      // 提交按钮
      addCommand: function (params) {
        var self = this;
        var text = ''; //要传的参数
        if (params === '提交') {
          text = self.commandName;
          if (!self.commandName || !(self.commandName.trim())) {
            self.qt.error('请填写会议名');
            return;
          }else{
            if(self.commandName && self.commandName.length>255){
              self.qt.error('会议名称字数不能超过255个字符');
              return;
            }
          }
        } else if (params === '同意') {
          text = self.dialogInput;
          if (!self.dialogInput || !(self.dialogInput.trim())) {
            self.qt.error('会议名称不能为空！');
            return;
          }else{
            if(self.dialogInput && self.dialogInput.length>255){
              self.qt.error('会议名称字数不能超过255个字符');
              return;
            }
          }
        }

        // 本来就没写，我先注释了  yan
        // if(self.checkBoxData.length<=0){
        //   self.qt.error('您还没有选择成员呢');
        //   return;
        // }

        var userIds = [];
        for (var i = 0; i < self.checkBoxData.length; i++) {
          userIds.push(self.checkBoxData[i].id);
        }

        ajax.post('/command/basic/save', {
          members: $.toJSON(userIds),
          name: text.trim()
        }, function (data) {
          // error代表有重名的情况
          if (data.status == 'error') {
            self.dialogVisible = true;
            self.dialogInput = data.errorInfo.recommendedName;
            return;
          }
          self.dialogVisible = false;
          self.startCommand(data.id, function () {
            self.qt.linkedWebview('rightBar', {
              id: 'currentGroupChange',
              params: $.toJSON([data])
            });
          });
        });
      },

      startCommand: function (commandId, fn) {
        var self = this;
        ajax.post('/command/basic/start', {
          id: commandId
        }, function (data) {
          fn();
          setTimeout(function () {
            self.goHome();
          }, 1000);
        });
      },
      goHome: function () {
        var self = this;
        self.qt.invoke('slotOpenMeetingPage');
        self.qt.destroy();
      },
      cancel: function () {
        var self = this;
        self.qt.invoke('slotOpenStartUpPage');
        self.qt.destroy();
      }
    },
    mounted: function () {
      var self = this;
      self.qt = new QtContext('leaderDialogCreateGroup', function () {
        //初始化ajax
        ajax.init({
          login: config.ajax.login,
          redirectLogin: false,
          authname: config.ajax.authname,
          debug: config.ajax.debug,
          messenger: {
            info: function (message, status) {
              self.qt.info(message);
            },
            success: function (message, status) {
              self.qt.success(message);
            },
            warning: function (message, status) {
              self.qt.warning(message);
            },
            error: function (message, status) {
              self.qt.error(message);
            }
          }
        });

        //获取用户数据
        self.qt.get(['user'], function (variables) {
          self.user = variables.user ? $.parseJSON(variables.user) : {};
          self.gateIp = variables.user ? $.parseJSON(variables.user).gateIp : '';
          self.gatePort = variables.user ? $.parseJSON(variables.user).gatePort : '';
        });

        self.refreshInstitution();

        self.qt.on('goHome', function () {
          self.goHome();
        });

        self.qt.on('close', function () {
          self.qt.destroy();
        });
      });
    }
  });

  return Vue;
});