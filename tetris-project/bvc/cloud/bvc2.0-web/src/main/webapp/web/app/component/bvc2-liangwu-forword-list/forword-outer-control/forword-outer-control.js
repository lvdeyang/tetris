define([
  'text!' + window.APPPATH + 'component/bvc2-liangwu-forword-list/forword-outer-control/forword-outer-control.html',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'date',
  'bvc2-dialog-single-osd',
  'bvc2-auto-layout',
  'jquery.layout.auto',
  'bvc2-monitor-forword-bandwidth',
  'css!' + window.APPPATH + 'component/bvc2-liangwu-forword-list/forword-outer-control/forword-outer-control.css'
], function (tpl, ajax, $, Vue) {

  //组件名称
  var pluginName = 'forword-outer-control';
  Vue.component(pluginName, {
    template: tpl,
    data: function () {
      return {
        originType: 'OUTER',
        loading: false,
        encodetree: [],
        checked: [],
        defaultExpandAll: true,
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: 'isLeaf'
        },
        filterText: '',
        tableCurrgenData: [],
        multipleSelection: [],
        region: '',
        regionOption: [],
        singleWidth: '',
        totalWidth: '',
        forwordTotal: '',
        originType: 'OUTER',
        tableList: {},
        totalNum: {},
        pageSize: 10,
        currentPage: 0,
        total: 0,
        forwordIds: [],
        bandwidthVisible: false,
      }
    },
    props: [],
    computed: {
      tableData: function () {
        return this.tableCurrgenData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
      },
      currentWidth: function () {
        return this.forwordTotal * this.singleWidth
      },
      currentWidthTotleNum: function () {
        return Math.floor(this.totalWidth / this.singleWidth)
      }
    },
    watch: {
      filterText: function (val) {
        this.$refs.tree.filter(val);
      }
    },
    methods: {
      initTree: function (keepExpand) {
        this.treeLoading = true;
        var self = this;
        ajax.post('/command/query/find/institution/tree/bundle/2/false/0', null, function (data) {
          self.encodetree = data;
          this.treeLoading = false;
        });
        self.loadStation()
      },
      initTableData: function () {
        var self = this;
        var params = {
          privilegesStr: "['DIANBO']",
          returnBundleList: true,
          satisfyAll: false
        };
        ajax.post('/command/query/find/institution/tree/bundle/4/false/0', params, function (data) { //编码器列表
          var tableData = data[0].bundleList, newTableData = [];
          this.treeLoading = false;
          ajax.post('/monitor/live/load/device/lives', { //转发列表
            currentPage: 1,
            pageSize: 9999
          }, function (data) {
            var lives = data.rows;
            var encoder = null;
            tableData.forEach(function (item) {
              var forwordItem = null;
              forwordItem = lives.find(function (ele) {
                return ele.dstVideoBundleId == item.id
              })
              if (forwordItem) {
                item.videoBundleName = forwordItem.videoBundleName
                item.videoBundleId = forwordItem.videoBundleId
                item.forwordid = forwordItem.id
                item.status = forwordItem.status
                newTableData.unshift(item)
              } else {
                newTableData.push(item)
              }
            })
            // self.tableData = newTableData;
            // 区分解码器域
            var rows = newTableData;
            if (rows && rows.length > 0) {
              for (var i = 0; i < rows.length; i++) {
                // self.table.data.push(rows[i]);
                var parseExtend = ""
                if (rows[i].extraInfo) {
                  parseExtend = JSON.parse(rows[i].extraInfo)
                  if (parseExtend.extend_param) {
                    parseExtend = JSON.parse(parseExtend.extend_param).region

                  }
                }
                for (var j = 0; j < self.regionOption.length; j++) {
                  var item = self.regionOption[j];
                  if (item.identity == parseExtend) {
                    self.tableList[item.identity].push(rows[i])
                    if (rows[i].forwordid && rows[i].status == '运行中') { //计算转发路数
                      self.totalNum[item.identity] += 1
                    }
                  }
                }
              }
            }

            var region = self.region ? self.region : self.regionOption[0].identity;
            self.tableCurrgenData = self.tableList[region];
            self.currentPage = 1;
            self.total = self.tableList[region].length
            self.forwordTotal = self.totalNum[region];
            self.singleWidth = self.regionOption[0].singleWidth
            self.totalWidth = self.regionOption[0].totalWidth
            self.loading = false;
          })
        });

      },

      loadStation: function () {
        var self = this;
        self.loading = true;
        ajax.post('/command/station/bandwidth/query', null, function (data, status) {
          if (status == 200) {
            self.regionOption = data.rows;
            for (var j = 0; j < self.regionOption.length; j++) {
              var item = self.regionOption[j];
              if (!item.originType) {
                item.originType = "INNER"
              }
              self.tableList[item.identity] = [];
              self.totalNum[item.identity] = 0
            }
            // 设置tab栏初始选中值,和带宽初始值
            self.region = self.region ? self.region : self.regionOption[0].identity;
            self.initTableData()
          }
        })
      },
      handleSizeChange: function (pageSize) {
        var self = this;
        self.pageSize = pageSize;
        // self.load(1);
      },
      handleCurrentChange: function (currentPage) {
        var self = this;
        self.currentPage = currentPage
        // self.load(currentPage);
      },
      handleRegionChange (val) {
        var self = this;
        self.tableCurrgenData = self.tableList[val];
        self.currentPage = 1
        self.total = self.tableList[val].length;
        self.forwordTotal = self.totalNum[val];
        for (var i = 0; i < self.regionOption.length; i++) {
          var item = self.regionOption[i];
          if (item.identity == val) {
            self.singleWidth = item.singleWidth
            self.totalWidth = item.totalWidth
          }
        }

      },
      handleDragEnd (draggingNode, dropNode, dropType, ev) {
        console.log('tree drag end: ', dropNode && dropNode.label, dropType);
      },
      filterNode: function (value, data, node) {
        if (data.type === 'BUNDLE') {
          if (!value) return true;
          if (data.name.indexOf(value) !== -1) return true;
        } else if (data.type === 'CHANNEL') {
          if (JSON.parse(data.param).channelType === 'VenusVideoIn') {
            if (!value) return true;
            if ((node.parent && node.parent.level !== 0 && node.parent.data.name.indexOf(value) !== -1)) {
              node.parent.expanded = false;
              return true;
            }
          }
        }
      },

      renderContent: function (h, target) {
        var node = target.node,
          data = target.data;
        if (data.type == 'CHANNEL') {
          node.isLeaf = true;
          if (JSON.parse(data.param).channelType != 'VenusVideoIn') {
            node.visible = false;
          }
        }

        var c = {};
        c[node.icon] = true;
        var classes = [c];
        if (data.bundleStatus) {
          classes.push(data.bundleStatus);
        }

        //设备网管按钮
        var webButton = null;
        if (data.webUrl) {
          webButton = h('a', {
            class: ['icon-home'],
            attrs: {
              href: data.webUrl,
              target: "_blank",
              title: "设备网管页面"
            },
            style: {
              'margin-left': '5px'
            }
          }, '')
        }

        //接入层网管的按钮
        var layerWebButton = null;
        if (data.layerWebUrl) {
          layerWebButton = h('a', {
            class: ['icon-sitemap'],
            attrs: {
              href: data.layerWebUrl,
              target: "_blank",
              title: "接入层页面"
            },
            style: {
              'margin-left': '5px'
            }
          }, '');
        }

        return h('span', {
          class: {
            'bvc2-tree-node-custom': true
          }
        }, [
          h('span', null, [
            h('span', {
              class: classes,
              style: {
                'font-size': '16px',
                'position': 'relative',
                'top': '1px',
                'margin-right': '5px'
              }
            }, null),
            node.label,
            webButton,
            layerWebButton
          ])
        ]);
      },
      nodeExpand: function (data, node, instance) {
        if (data.icon === 'icon-folder-close') {
          data.icon = 'icon-folder-open';
        }

        //一级节点自定义展开
        if (node.level === 1) {
          this.nodeExpandFunc(node, instance);
        }
      },
      nodeCollapse: function (data, node, instance) {
        if (data.icon === 'icon-folder-open') {
          data.icon = 'icon-folder-close';
        }
      },
      getCheckedNodes: function () {
        return this.$refs.tree.getCheckedNodes();
      },
      handleDragStart: function (node, e) {
        //放置数据
        e.dataTransfer.setData('data', $.toJSON(node.data));

      },
      allowDrop: function (node) {
        return false;
      },
      allowDrag: function (node) {
        if (node.data.type === 'BUNDLE') {
          return true;
        }
        return false;
      }, //节点展开方法
      nodeExpandFunc: function (node, instance) {
        var children = node.data.children;
        if (children) {
          for (var i = 0; i < children.length; i++) {
            var _node = instance.tree.getNode(children[i]);
            if (_node.data.type === "FOLDER") {
              _node.expanded = true;
              this.nodeExpandFunc(_node, instance);
            }
          }
        }
      },
      handleSelectionChange (val) {
        var forwordIds = [];
        val.forEach(function (item) {
          forwordIds.push(item.forwordid)
        })
        this.forwordIds = forwordIds
      },
      rowStop (scope, stopAndDelete) {
        var row = scope.row, self = this;
        ajax.post('/monitor/live/stop/live/device/' + row.forwordid, { stopAndDelete: stopAndDelete }, function (data, status) {
          if (status == 200) {
            self.$message({
              'type': "success",
              'message': "操作成功！"
            })
            self.loadStation()
          }
        })
      },
      rowStart (scope) {
        var row = scope.row
        var self = this;
        ajax.post('/monitor/live/stop/to/restart', { ids: JSON.stringify([row.forwordid]) }, function (data, status) {
          if (status == 200) {
            self.$message({
              'type': "success",
              'message': "开始成功！"
            })
            self.loadStation()

          }
        })
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
              h('p', null, ['是否要删除此转发任务?'])
            ])
          ]),
          type: 'wraning',
          showCancelButton: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: function (action, instance, done) {
            instance.confirmButtonLoading = true;
            if (action === 'confirm') {
              ajax.post('/monitor/live/stop/live/device/' + row.forwordid, null, function (data, status) {
                instance.confirmButtonLoading = false;
                done();
                if (status == 200) {
                  self.$message({
                    'type': "success",
                    'message': "删除成功"
                  })
                  self.loadStation()
                }

              }, null, [403, 404, 409, 500]);
            } else {
              instance.confirmButtonLoading = false;
              done();
            }
          }
        }).catch(function () { });
      },
      treeDrop (e) {
        var self = this;
        console.log(e.dataTransfer.getData('data'))
        var srcBundleId = JSON.parse(e.dataTransfer.getData('data')).id;
        var dstBundleId = '';

        for (index in e.path) {
          if (e.path[index].attributes && e.path[index].attributes.class && e.path[index].attributes.class.nodeValue.indexOf('el-table__row') != -1) {
            console.log(e.path[index])
            dstBundleId = e.path[index].attributes.class.nodeValue.split(' ')[1]
            break
          } else {
            continue
          }
        }

        var param = {
          srcType: 'BUNDLE',
          srcBundleId: srcBundleId,
          dstType: 'BUNDLE',
          dstBundleId: dstBundleId,
          type: 'DEVICE',
        }
        ajax.post('/monitor/live/vod/device', param, function (data) {
          self.$message({
            type: 'success',
            message: '操作成功！'
          })
          self.loadStation()
        });
      },
      dragover: function (e) {
        e.preventDefault()
      },
      rowClassName: function (row, rowIndex) {
        return row.row.id
      },
      selectableFun: function (row, index) {
        if (row.videoBundleName) {
          return true
        } else {
          return false
        }
      },
      handleStart () {
        var self = this;
        if (self.forwordIds.length == 0) {
          self.$message({
            type: "waring",
            message: "请选择需要操作的数据！"
          })
          return
        }
        ajax.post('/monitor/live/stop/to/restart', { ids: JSON.stringify(self.forwordIds) }, function (data, status) {
          if (status == 200) {
            self.$message({
              'type': "success",
              'message': "开始成功！"
            })
            self.loadStation()

          }
        })
      },
      handleStop (stopAndDelete) {
        var self = this;
        if (self.forwordIds.length == 0) {
          self.$message({
            type: "waring",
            message: "请选择需要操作的数据！"
          })
          return
        }
        //@param Boolean stopAndDelete TRUE停止但不删除、FALSE删除、null停止且删除
        ajax.post('/monitor/live/stop/live/device/', { stopAndDelete: stopAndDelete, ids: JSON.stringify(self.forwordIds) }, function (data, status) {
          if (status == 200) {
            self.$message({
              'type': "success",
              'message': "操作成功！"
            })
            self.loadStation()
          }
        })
      },
      handleDelete () {
        var self = this;
        if (self.forwordIds.length == 0) {
          self.$message({
            type: "waring",
            message: "请选择需要操作的数据！"
          })
          return
        }
      },
      // 配置站点带宽

      openBandwidth () {
        this.bandwidthVisible = true;
      },
      handlebandwidthClose: function () {
        this.bandwidthVisible = false;
        // this.tableList = {};
        // this.tableCurrgenData = []
        this.loadStation()
      },
    },
    mounted: function () {
      var self = this;
      var opcityUrl = '/vedioCapacity/query'
      var resourceApiUrl = document.location.protocol + "//" + document.location.hostname + ':8213';
      self.resourceApiUrl = resourceApiUrl;
      console.log(self.resourceApiUrl + '/vedioCapacity/query')
      this.initTree();

    },
    updated () {

    },
  });

  return Vue;
});