define([
  'text!' + window.APPPATH + 'component/bvc2-monitor-forword/bvc2-monitor-forword.html',
  'context',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'sortablejs',
  'vue-draggable',
  'date',
  'css!' + window.APPPATH + 'component/bvc2-monitor-forword/bvc2-monitor-forword.css'
], function (tpl, context, ajax, $, Vue) {

  //组件名称
  var pluginName = 'bvc2-monitor-forword';

  var app = context.getProp('app');
  Vue.component(pluginName, {
    props: [],
    template: tpl,
    data: function () {
      return {
        recodetree: [],
        encodetree: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        treeLoading: false
      }
    },
    methods: {
      filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      initTree: function (keepExpand) {
        this.treeLoading = true;
        var self = this;
        ajax.post('/command/query/find/institution/tree/bundle/2/false/1', null, function (data) {
          self.encodetree = data;
          this.treeLoading = false;
        });
        ajax.post('/command/query/find/institution/tree/bundle/2/false/1', null, function (data) {
          self.recodetree = data;
          this.treeLoading = false;
        });
      },
      handleNodeClick: function (data) {
        if (data.children) {
          return
        } else {
          console.log(data.name)
          this.inputDevice = data.name;
          this.dialogFormVisible = false;

        }
      },
      //开始拖拽事件
      onStart(e) {
        this.drag = true;
        console.log(e, 'start')

      },
      //拖拽结束事件
      onEnd(e) {
        var self = this;
        console.log(e, 'end')


      },
      onMove2(e, originalEvent) {
        //false表示阻止拖拽
        return false;
      },
      handleDelete(arr, index) {
        arr.splice(index, 1)
      },

      handleTabClick(tab, event) {
        if ("EncodeManage" !== tab.name) {
          this.$router.push('/' + tab.name);
        }
      },
      add: function (e) {
        var from = e.from.childNodes[0].attributes.bundleid.value
        var to = e.to.childNodes[0].attributes.bundleid.value
        console.log(from, 'from')
        console.log(to, 'to')
        console.log(e)
      }
    },
    mounted: function () {
      var self = this;
      this.initTree()
      // self.load(1);
    }
  });

  return Vue;
});