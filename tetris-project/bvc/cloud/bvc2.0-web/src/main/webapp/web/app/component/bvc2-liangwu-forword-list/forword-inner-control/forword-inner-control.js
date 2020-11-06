define([
  'text!' + window.APPPATH + 'component/bvc2-liangwu-forword-list/forword-inner-control/forword-inner-control.html',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'date',
  'bvc2-dialog-single-osd',
  'bvc2-auto-layout',
  'jquery.layout.auto',
  'css!' + window.APPPATH + 'component/bvc2-liangwu-forword-list/forword-inner-control/forword-inner-control.css'
], function (tpl, ajax, $, Vue) {

  //组件名称
  var pluginName = 'forword-inner-control';
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
        encodetree: [],
        checked: [],
        defaultExpandAll: true,
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: 'isLeaf'
        },
        filterText: '',
        layOutOptions: [{
          value: '1',
          label: '屏幕1'
        }, {
          value: '2',
          label: '屏幕2'
        }, {
          value: '3',
          label: '屏幕3'
        }, {
          value: '4',
          label: '屏幕4'
        },],
        templateId: '',
        screenInformation: []

      }
    },
    props: ['originType'],
    computed: {},
    watch: {
      filterText: function (val) {
        this.$refs.tree.filter(val);
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
        ajax.post('/command/query/find/institution/tree/bundle/2/false/0', null, function (data) {
          self.encodetree = data;
          this.treeLoading = false;
        });
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

      screenSelectChange: function (val) {

      },
      initLayoutAuto: function (params) {

      },
      refreshLayout: function () {
        var instance = this;
        var $container = getLayoutContainer(instance.$el);
        var options = instance.layout;
        var tdHtml = `<div class="cell-box"><p>编码器：<span  class="encode">无</span></p>
                            <p>解码器：<span  class="decode">无</span></p>
                            <button class="el-button el-button--text screen-config-stop">暂停<span class="icon-stop"></span></button>
                            <button class="el-button el-button--text screen-config-begin">开始<span class="el-icon-caret-right"></span></button>
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
                  locationX: od.x,
                  locationY: od.y,
                  locationTemplateLayoutId: instance.templateId,
                }
                // 屏幕绑定解码器
                ajax.post('/location/of/screen/wall/bind/encoder', params, function (data, status) {
                  if (status == 200) {
                    instance.$message({
                      type: "success",
                      message: "编码器绑定成功！"
                    })
                    $cell.find('.encode').text(data.encoderBundleName)
                  } else {
                    instance.$message({
                      type: "waring",
                      message: "编码器绑定失败！"
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
              return (item.locationX == cell.x && item.locationY == cell.y)
            })
            console.log(currentScreenInfo)
            // 绑定屏幕信息
            cell.$cell['layout-auto']('setData', currentScreenInfo);
            // 设置屏幕操作按钮
            if (currentScreenInfo && currentScreenInfo.status == "RUN") {
              cell.$cell.find('.screen-config-begin').hide()
              cell.$cell.find('.screen-config-stop').show()
            } else if (currentScreenInfo && currentScreenInfo.status == "STOP" && currentScreenInfo.decoderBundleId && currentScreenInfo.encoderBundleId) {
              cell.$cell.find('.screen-config-begin').show()
              cell.$cell.find('.screen-config-stop').hide()
            } else if (!currentScreenInfo) {
              cell.$cell.find('.screen-config-begin').hide()
              cell.$cell.find('.screen-config-stop').hide()
            }
            if (currentScreenInfo) {
              cell.$cell.find('.encode').text(currentScreenInfo.encoderBundleName)
              cell.$cell.find('.decode').text(currentScreenInfo.decoderBundleName)
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
            self.layOutOptions = data
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
    },
    mounted: function () {
      var self = this;
      this.initTree()
      this.getScreenLayout()
    },
    updated () {

    },
  });
  $(document).on('mousedown', '.screen-config-stop', function (e) {
    e.stopPropagation();
    var $button = $(this);
    $button.on('mouseup', function () {
      var $td = $button.closest('td');
      var od = $td['layout-auto']('getData');
      console.log(od)
      ajax.post('/location/of/screen/wall/exchange/location/Status', { locationTemplateLayoutId: val }, function (data, status) {
        if (status == 200) {
        }
      })
    });
    $(document).on('mouseup', function () {
      $button.unbind('mouseup');
      $(document).unbind('mouseup');
    });
  });
  return Vue;
});