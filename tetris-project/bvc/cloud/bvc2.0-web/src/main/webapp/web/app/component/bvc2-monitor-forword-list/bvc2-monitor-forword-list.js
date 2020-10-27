

define([
  'text!' + window.APPPATH + 'component/bvc2-monitor-forword-list/bvc2-monitor-forword-list.html',
  'restfull',
  'jquery',
  'vue',
  'element-ui',
  'date',
  'bvc2-dialog-single-osd',
  'bvc2-monitor-forword',
  'bvc2-monitor-forword-bandwidth',
  'css!' + window.APPPATH + 'component/bvc2-monitor-forword-list/bvc2-monitor-forword-list.css'
], function (tpl, ajax, $, Vue) {

  //组件名称
  var pluginName = 'bvc2-monitor-forword-list';

  Vue.component(pluginName, {
    template: tpl,
    data: function () {
      return {
        resourceApiUrl:'',
        table: {
          data: [],
          page: {
            currentPage: 1,
            pageSize: 10,
            total: 0
          },
          pageTab: {
            currentPage: 1,
            pageSize: 20000,
            total: 0
          }

        },
        stationList:[],
        // tableData:[],
        totleForword: '0',
        dialog: {
          forword: {
            visible: false
          },
          bandwidth: {
            visible: false
          }
        },
        buttonIsShow: true,
        extendForwordList:[],
        selfForwordList:[],
        activeName:"self",
        totalWidth:'',
        singleWidth:'',
        tableList:{},
        tableCurrgenData:[],
        outerDataLength:0
      }
    },
    props:['originType'],
    computed: {
      tableData: function () {
          return this.tableCurrgenData.slice((this.table.page.currentPage - 1) * this.table.page.pageSize, this.table.page.currentPage * this.table.page.pageSize);
      },
      currentWidth:function(){
        return this.table.page.total * this.singleWidth
      },
      currentWidthTotleNum:function(){
        return Math.floor(this.totalWidth/this.singleWidth)
      }
    },
    watch: {},
    methods: {
      load: function (currentPage) {
        var self = this;
        var extendForwordList=[]
        self.table.data.splice(0, self.table.data.length);
        ajax.post('/monitor/live/load/device/lives', {
          currentPage: currentPage,
          pageSize: self.table.pageTab.pageSize
        }, function (data) {
          var total = data.total;
          var rows = data.rows;
          var currentTotle =0;
          // if(self.originType == "OUTER"){
          //   self.totleForword = self.outerDataLength;
          // }else{
          //   self.totleForword = total - self.outerDataLength;
          // }
          if (rows && rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
              // self.table.data.push(rows[i]);
              var parseExtend = ""
              if(rows[i].dstExtraInfo){
                parseExtend = JSON.parse(rows[i].dstExtraInfo)
                if(parseExtend.extend_param){
                  parseExtend = JSON.parse(parseExtend.extend_param).region

                }
              }
              for(var j=0;j<self.stationList.length;j++){
                var item = self.stationList[j];
                    if(item.identity == parseExtend){
                    self.tableList[item.identity].push(rows[i])
                  }
              }
            }
            for(var i=0;i<self.stationList.length;i++){
              var item = self.stationList[i];
              if(item.originType == self.originType){
                currentTotle+= self.tableList[item.identity].length;
              }
            }
          }
          self.totleForword = currentTotle;
          self.tableCurrgenData = self.tableList[self.activeName];
          self.table.page.total = self.tableList[self.activeName].length;
          console.log(self.tableList[self.activeName].length)
          // self.getCapacity()
          // self.table.page.total = self.selfForwordList.length;
        });
      },
      loadStation: function () {
        var self = this;
        ajax.post('/command/station/bandwidth/query', null, function (data, status) {
          if (status == 200) {
            self.stationList = data.rows;
            self.stationList.unshift({
              id: 999,
              originType:"INNER",
              identity: "self",
              stationName: "本域",
            })
            for(var j=0;j<self.stationList.length;j++){
              var item = self.stationList[j];
                  if(!item.originType){
                    item.originType = "INNER"
                  }
                  self.tableList[item.identity]=[];
            }
            // 设置tab栏初始选中值,和带宽初始值
            var outerData=[],systemData=[];
            self.stationList.forEach(function(i){
              if(i.originType == "OUTER"){
                outerData.push(i)
              }else{
                systemData.push(i)
              }
            })
            if(self.originType == "OUTER"){
              self.activeName = outerData[0].identity
              self.singleWidth = outerData[0].singleWidth
              self.totalWidth = outerData[0].totalWidth
            }else{
              self.activeName = systemData[0].identity
            }
            
            self.outerDataLength = outerData.length;
            console.log(outerData.length,'outerData.length')
            self.load(1);
            // self.getCapacity()
          }
        })
      },
      getCapacity(){
        ajax.post(this.resourceApiUrl+'/vedioCapacity/query', null, function (data, status) {
          if (status == 200) {
            
            
          }
        })
      },
      handleClick(tab, event) {
        var self = this;
        self.tableCurrgenData = self.tableList[tab.name];
        self.table.page.currentPage = 1;
        self.table.page.total = self.tableList[tab.name].length;
       
        for(var i=0;i<self.stationList.length;i++){
          var item = self.stationList[i];
          if(item.identity == tab.name){
            self.singleWidth = item.singleWidth
            self.totalWidth = item.totalWidth
          }
        }

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
        // self.load(1);
      },
      handleCurrentChange: function (currentPage) {
        var self = this;
        self.table.page.currentPage = currentPage
        // self.load(currentPage);
      },
      osdControl: function () {

      },
      handleforwordClose: function () {
        this.dialog.forword.visible = false;

      },
      handlebandwidthClose: function () {
        this.dialog.bandwidth.visible = false;
        this.tableList={};
        this.tableCurrgenData=[]
        this.loadStation()
      },
      openForword() {
        this.dialog.forword.visible = true;
      },
      openBandwidth() {
        this.dialog.bandwidth.visible = true;
      },
      
    },
    mounted: function () {
      var self = this;
      console.log(self.originType)
      var resourceApiUrl = document.location.protocol +"//"+document.location.hostname+':8213';
      self.resourceApiUrl =resourceApiUrl;
      console.log(resourceApiUrl)
      self.loadStation()
      // self.getCapacity()
    },
    updated() {

    },
  });

  return Vue;
});