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
  Vue.component(pluginName, {
    template: tpl,
    data: function () {
      return {
        layout: '',
        decodetree: [],
        checked: [],
        defaultExpandAll: true,
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: 'isLeaf'
        },
        filterText: '',

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
            }
          ]
        },
        value8: ''

      }
    },
    props: ['originType'],
    computed: {},
    watch: {
      filterText: function (val) {
        this.$refs.tree.filter(val);
      }
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
          self.treeLoading = false;
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
      layoutChange: function (button) {
        if (!v_groupControl.config) {
          v_groupControl.$message({
            type: 'warning',
            message: '请先指定要操作的议程或方案！'
          });
          return;
        }
        if (!v_groupControl.video) {
          v_groupControl.$message({
            type: 'warning',
            message: '还没有要操作的视频！'
          });
          return;
        }
        v_groupControl.$confirm('此操作将清空屏幕上的配置，并且设置为：' + button.text + ', 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          beforeClose: function (action, instance, done) {
            if (action === 'confirm') {
              v_groupControl.$refs.autoLayout.setLayout({
                column: button.layout.basic.column,
                row: button.layout.basic.row,
                cellspan: button.layout.cellspan,
                name: button.layout.name,
                editable: true
              });
            }
            done();
          }
        });
      },

      columnChange: function (column) {
        var layout_instance = this;
        //获取设置前的值
        var $container = getLayoutContainer(layout_instance.$el);
        var oldColumn = $container['layout-auto']('queryColumn');
        layout_instance.$confirm('此操作将清空屏幕上的配置，并且设置为：' + layout_instance.layout.row + '行' + column + '列, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          beforeClose: function (action, instance, done) {
            if (action === 'confirm') {
              var newLayout = $.extend(true, {}, layout_instance.layout);
              newLayout.column = column;
              newLayout.cellspan = [];
              layout_instance.layout = newLayout;
            } else if (action === 'cancel') {
              layout_instance.layout.column = oldColumn;
            }
            done();
          }
        });
      },

      rowChange: function (row) {
        var layout_instance = this;
        //获取设置前的值
        var $container = getLayoutContainer(layout_instance.$el);
        var oldRow = $container['layout-auto']('queryRow');
        layout_instance.$confirm('此操作将清空屏幕上的配置，并且设置为：' + row + '行' + layout_instance.layout.column + '列, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          beforeClose: function (action, instance, done) {
            if (action === 'confirm') {
              var newLayout = $.extend(true, {}, layout_instance.layout);
              newLayout.row = row;
              newLayout.cellspan = [];
              layout_instance.layout = newLayout;
            } else {
              layout_instance.layout.row = oldRow;
            }
            done();
          }
        });
      },
      saveLayout: function (config, video, websiteDraw, position, dst, roleDst, done, layout, smallScreen) {
        var _uri = '';
        if (config.__businessType === 'agenda') {
          _uri = '/agenda/update/video/' + v_groupControl.video.id;
        } else if (config.__businessType === 'scheme') {
          _uri = '/scheme/update/video/' + v_groupControl.video.id;
        }
        ajax.post(_uri, {
          websiteDraw: $.toJSON(websiteDraw),
          position: $.toJSON(position),
          dst: $.toJSON(dst),
          roleDst: $.toJSON(roleDst),
          layout: layout,
          smallScreen: $.toJSON(smallScreen)
        }, function (data) {
          for (var i = 0; i < v_groupControl.config.videos.length; i++) {
            if (v_groupControl.config.videos[i].id === data.id) {
              v_groupControl.config.videos[i] = data;
              break;
            }
          }
          v_groupControl.video = data;
          v_groupControl.video.dstsCache = $.extend(true, [], data.dsts);
          done();
          v_groupControl.$message({
            type: 'success',
            message: '保存成功！'
          });
        });
      },
      screenSelectChange: function (val) {

      },
      initLayoutAuto: function (params) {

      }
    },
    mounted: function () {
      var self = this;
      var opcityUrl = '/vedioCapacity/query'
      var resourceApiUrl = document.location.protocol + "//" + document.location.hostname + ':8213';
      self.resourceApiUrl = resourceApiUrl;
      console.log(self.resourceApiUrl + '/vedioCapacity/query')
      this.initTree()

      var getLayoutContainer = function (el) {
        return $(el).find('.bvc2-auto-layout-container');
      };
      var $container = getLayoutContainer(this.$el);
      $container['layout-auto']('create', {
        cell: {
          column: 3,
          row: 3,
          // html:tdHtml
        },
        name: 'split_4x4_c3x3_lt',
        theme: 'dark',
        editable: false,
        event: {
          drop: function (e) {
            var $cell = $(this);
            console.log($cell)
            $cell['layout-auto']('setData', e.dataTransfer.getData('id'));
            $cell.text(e.dataTransfer.getData('id'));
          }
        }
      });
    },
    updated () {

    },
  });

  return Vue;
});