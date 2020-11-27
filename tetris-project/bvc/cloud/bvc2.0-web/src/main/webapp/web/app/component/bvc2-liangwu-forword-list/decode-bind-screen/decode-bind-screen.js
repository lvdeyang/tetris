define([
  'text!' + window.APPPATH + 'component/bvc2-liangwu-forword-list/decode-bind-screen/decode-bind-screen.html',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'date',
  'bvc2-dialog-single-osd',
  'bvc2-auto-layout',
  'jquery.layout.auto',
  'css!' + window.APPPATH + 'component/bvc2-liangwu-forword-list/decode-bind-screen/decode-bind-screen.css'
], function (tpl, ajax, $, Vue) {

  //组件名称
  var pluginName = 'decode-bind-screen';
  var getLayoutContainer = function (el) {
    return $(el).find('.bvc2-auto-layout-container');
  };

  Vue.component(pluginName, {
    template: tpl,
    data: function () {
      return {
        layout: {
          row: 3,
          column: 3,
          name: ""
        },
        createLayout: {
          row: 1,
          column: 1,
          name: ""
        },
        layOutOptions: [],
        decodetree: [],
        checked: [],
        defaultExpandAll: true,
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: 'isLeaf'
        },
        filterText: '0',
        templateId: '',
        options: {
          columnOptions: [
            {
              value: 1,
              label: 1
            }, {
              value: 2,
              label: 2
            }, {
              value: 3,
              label: 3
            }, {
              value: 4,
              label: 4
            }, {
              value: 5,
              label: 5
            }
          ],
          rowOptions: [
            {
              value: 1,
              label: 1
            }, {
              value: 2,
              label: 2
            }, {
              value: 3,
              label: 3
            }, {
              value: 4,
              label: 4
            }, {
              value: 5,
              label: 5
            }
          ]
        },
        screenConfigDialogVisible: false,
        screenInformation: []

      }
    },
    props: ['originType'],
    computed: {},
    watch: {
      filterText: function (val) {
        this.$nextTick(function () {
          this.$refs.tree.filter(val);
        })
        // this.$refs.tree.filter(val);
      },
      layout: function () {
        this.refreshLayout();
      },
    },
    methods: {
      initTree: function (keepExpand) {
        this.treeLoading = true;
        var params = {
          privilegesStr: "['DIANBO']",
          satisfyAll: false
        };
        var self = this;
        ajax.post('/command/query/find/institution/tree/bundle/4/false/0', params, function (data) {
          self.decodetree = data;
          self.filterText = ''
          self.treeLoading = false;
        });
      },
      handleDragEnd (draggingNode, dropNode, dropType, ev) {
        console.log('tree drag end: ', dropNode && dropNode.label, dropType);
      },
      // filterNode(value, data) {

      //   var extraInfo = JSON.parse(data.extraInfo)
      //   if (!extraInfo || !(extraInfo.extend_param)) {
      //     // return true
      //   } else {
      //     var extend_param = JSON.parse(extraInfo.extend_param)
      //     return (value == extend_param.region)
      //   }
      // },
      filterNode: function (value, data, node) {
        var extraInfo = JSON.parse(data.extraInfo);
        var extend_param = 'self'
        if (extraInfo && extraInfo.extend_param) {
          extend_param = JSON.parse(extraInfo.extend_param)
        }
        if (data.type === 'BUNDLE') {
          if (!value && extend_param.region == "self") return true;
          if (data.name.indexOf(value) !== -1 && extend_param.region == "self") return true;
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
        // return false;
      },
      allowDrag: function (node) {
        console.log(node)
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
      handleScreenConfig: function () {
        var self = this;
        if (!self.createLayout.name) {
          self.$message({
            type: 'danger',
            message: "屏幕名称不能为空！"
          })
          return
        }
        var params = {
          templateName: self.createLayout.name,
          screenNumberOfX: self.createLayout.row,
          screenNumberOfY: self.createLayout.column,
        }
        ajax.post('/location/template/layout/add', params, function (data, status) {
          if (status == 200) {
            self.screenConfigDialogVisible = false
            self.layOutOptions.push(data)
            self.templateId = data.id
            self.handleTemplateChange(data.id);
          }
        })
      },
      initLayoutAuto: function (params) {

      },
      refreshLayout: function () {
        var instance = this;
        var $container = getLayoutContainer(instance.$el);
        var options = instance.layout;
        var tdHtml = `<div class="cell-box">
                            <p>解码器：<span  class="decode">无</span></p>
                            <button class="el-button el-button--text screen-config-unbind-decoder"> 删除 <span class="el-icon-delete"></span></button>
                      </div>`
        if (options) {
          $container['layout-auto']('create', {
            cell: {
              column: options.column,
              row: options.row,
              html: tdHtml
            },
            // cellspan: options.cellspan,
            theme: 'dark',
            name: options.name,
            editable: false,
            event: {
              drop: function (e) {
                var data = $.parseJSON(e.dataTransfer.getData('data'));
                var $cell = $(this);
                var od = $cell['layout-auto']('getData');
                // $cell['layout-auto']('setData', od);
                var params = {
                  bundleId: data.id,
                  bundleName: data.name,
                  locationX: od.locationX,
                  locationY: od.locationY,
                  locationTemplateLayoutId: instance.templateId,
                }
                // 屏幕绑定解码器
                ajax.post('/location/of/screen/wall/bind/decoder', params, function (data, status) {
                  if (status == 200) {
                    instance.$message({
                      type: "success",
                      message: "解码器绑定成功！"
                    })

                    instance.handleTemplateChange(instance.templateId)
                    // $cell.find('.decode').text(data.decoderBundleName)
                  } else {
                    instance.$message({
                      type: "waring",
                      message: "解码器绑定失败！"
                    })
                  }
                })

              }
            },
          });
          var cells = $container['layout-auto']('getCells');
          var screenInfo = instance.screenInformation
          for (var i = 0; i < cells.length; i++) {
            var cell = cells[i];
            var currentScreenInfo = screenInfo.find(function (item) {
              return (item.locationX == cell.locationX && item.locationY == cell.locationY)
            })
            if (currentScreenInfo) {
              cell.$cell['layout-auto']('setData', currentScreenInfo);
              cell.$cell.find('.decode').text(currentScreenInfo.decoderBundleName)
            } else {
              cell.$cell.find('.screen-config-unbind-decoder').hide()
              cell.$cell['layout-auto']('setData', cell);
            }
          }
        } else {
          $container['layout-auto']('destroy');
        }
      },
      getScreenLayout: function () {
        var self = this;
        ajax.post('/location/template/layout/query/all', null, function (data, status) {
          if (status == 200) {
            self.layOutOptions = data;
            self.templateId = data[0].id
            self.handleTemplateChange(data[0].id)
          }
        })
      },
      handleTemplateChange (val) {
        console.log(val)
        var currgenTempalte = this.layOutOptions.find(function (item) {
          return item.id == val
        })

        var self = this;
        ajax.post('/location/of/screen/wall/all/screen/information', { locationTemplateLayoutId: val }, function (data, status) {
          if (status == 200) {
            self.screenInformation = data;
            var newLayout = $.extend(true, {}, this.layout);
            newLayout.row = currgenTempalte.screenNumberOfX
            newLayout.column = currgenTempalte.screenNumberOfY
            newLayout.name = currgenTempalte.templateName
            newLayout.cellspan = [];
            self.layout = newLayout;
          }
        })
      },
      createdScreen () {
        this.screenConfigDialogVisible = true
      },
      // 删除屏幕
      deleteScreen () {
        var self = this;
        self.$confirm('此操作将永久删除该屏幕设置, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          ajax.post('/location/template/layout/delete', { id: self.templateId }, function (data, status) {
            if (status == 200) {
              self.$message({
                type: "success",
                message: "删除成功！"
              })
              self.getScreenLayout()
            }
          })
        }).catch(function () {
          self.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      // 解绑所有解码器
      deleteAllDncode () {
        var self = this;
        var params = {
          locationTemplateLayoutId: self.templateId,
          unbindAll: true
        }
        self.$confirm('此操作将解绑所有未执行转发的解码器, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          ajax.post('/location/of/screen/wall/unbind/all/decoder', params, function (data, status) {
            if (status == 200) {
              self.$message({
                type: "success",
                message: "操作成功！"
              })
              self.handleTemplateChange(self.templateId)
            }
          })
        }).catch(function () {
          self.$message({
            type: 'info',
            message: '已取消删除'
          });
        });


      },
    },

    mounted: function () {
      var self = this;
      this.initTree()
      this.getScreenLayout()

    },
    updated () {

    },
  });
  // 删除按钮
  $(document).on('mousedown', '.screen-config-unbind-decoder', function (e) {
    e.stopPropagation();
    var vm = Vue;
    var $button = $(this);
    $button.on('mouseup', function () {
      var $td = $button.closest('td');
      var od = $td['layout-auto']('getData');
      console.log(od)
      ajax.post('/location/of/screen/wall/unbind/decoder', { id: od.id }, function (data, status) {
        if (status !== 200) return;
        $td.find('.decode').text("无")
        // $td.find('.screen-config-begin').hide()
        // $td.find('.screen-config-stop').hide()
        $td.find('.screen-config-unbind-decoder').hide()
        // $td.find('.status').text("STOP")
      }, null, [403, 404, 409, 500]);
    });
    $(document).on('mouseup', function () {
      $button.unbind('mouseup');
      $(document).unbind('mouseup');
    });
  });
  return Vue;
});