<template>
  <section>

    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left; width:100%;">
      <el-tab-pane label="设备模板管理" name="BundleTemplateManage"></el-tab-pane>
      <!--<el-tab-pane label="通道模板管理" name="ChannelTemplateManage"></el-tab-pane>-->
    </el-tabs>

    <el-card style="float:left;margin-top:10px;width:50%;min-height: 700px;" body-style="padding:0px">
      <div slot="header" style="height: 30px">
        <el-input size="small" v-model="filters.keyword" style="float: left;width:150px;" placeholder="关键字"></el-input>
        <el-button size="small" type="info" @click="getTemplates" style="float: left;margin-left: 30px;">查询</el-button>

        <el-upload
          class="upload-demo"
          :action=uploadUrl
          name="filePoster"
          :headers="myHeaders"
          :show-file-list="false"
          :on-success="uploadSuccess"
          :on-error="uploadError"
          style="float: right;margin-right: 10px;">
          <el-button size="small" type="primary">导入模板</el-button>
        </el-upload>


        <el-button size="small" type="danger" @click="handleMultiDelete" style="float: right;margin-right: 10px;">批量删除</el-button>
      </div>

      <!--模板列表-->
      <el-table :data="templates" v-loading="tableLoading" style="width: 100%;" @selection-change="handleSelectionChange">
        <el-table-column width="100" type="selection">
        </el-table-column>
        <el-table-column prop="deviceModel" label="设备类型" width="300" sortable>
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="handleDetail(scope.row)" size="small">详情</el-button>
            <el-button type="text" @click="handleSingleDelete(scope.row)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>


    <el-card style="float:left;margin-left:50px;margin-top:10px;width:40%;min-height: 700px;" body-style="padding:0px">
      <div slot="header" style="height: 30px">
        <span style="float: left;font-size: 18px;">模板属性</span>
      </div>

      <el-tree :data="templateTree" :props="treeProps" default-expand-all></el-tree>
    </el-card>
  </section>
</template>

<script type="text/ecmascript-6">
  import { getTemplates,getTemplateTree,deleteTemplate} from '../../api/api';
  // import apiConfig from '../../../config/api.config';
  // let requestIP = document.location.host.split(":")[0];

  let basePath = document.location.origin;

  export default {
      name: "templateManage",
      data() {
        return {
          activeTabName:"BundleTemplateManage",
          filters:{
            keyword : ""
          },
          tableLoading: false,
          templates: [],
          multipleSelection : [],
          templateTree : [],
          treeProps : {
            children: 'children',
            label: 'label',
          },
          // uploadUrl : "http://" + requestIP + ":8887/suma-venus-resource/template/import",
          uploadUrl : basePath + "/template/import",
          myHeaders : {
              'tetris-001': sessionStorage.getItem('token')
          }
        }
      },
      methods:{
        handleTabClick(tab, event) {
          if("BundleTemplateManage" !== tab.name){
            this.$router.push('/' + tab.name);
          }
        },
        getTemplates : function(){
          this.tableLoading = true;
          let param = {
            name : this.filters.keyword
          };
          getTemplates(param).then(res => {
            if(res.errMsg){
              this.$message({
                message: res.errMsg,
                type: 'error'
              });
            }else{
              this.templates = res.templates;
            }

            this.tableLoading = false;
          });
        },
        handleSelectionChange : function(val){
          this.multipleSelection = val;
        },
        handleDetail : function(row){
          let param = {
            channelTemplateId : row.id
          };

          getTemplateTree(param).then(res => {
            if(res.errMsg){
              this.$message({
                message: res.errMsg,
                type: 'error'
              });
            }else{
              this.templateTree = res.tree;
            }
          });
        },
        handleSingleDelete : function(row){
          this.$confirm('是否确认删除?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let param = {
              templateIds : row.id
            };
            deleteTemplate(param).then(res => {
              if(res.errMsg){
                this.$message({
                  message: res.errMsg,
                  type: 'error'
                });
              }else{
                this.$message({
                  message: "删除成功",
                  type: 'success'
                });
                this.getTemplates();
                this.templateTree = [];
              }
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        },
        handleMultiDelete : function(){
          if(!this.multipleSelection.length){
            this.$message({
              message: '请至少选择一行',
              type: 'warning'
            });
            return;
          }

          this.$confirm('是否确认删除所有选定行?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let templateIds = "";
            for(let selection of this.multipleSelection){
              templateIds += selection.id;
              templateIds += ";";
            }

            let param = {
              templateIds : templateIds
            };

            deleteTemplate(param).then(res => {
              if(res.errMsg){
                this.$message({
                  message: res.errMsg,
                  type: 'error'
                });
              }else{
                this.$message({
                  message: "删除成功",
                  type: 'success'
                });
                this.getTemplates();
                this.templateTree = [];
              }
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        },
        uploadSuccess: function(res){
          if(res.errMsg){
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          }else{
            this.$message({
              message: "导入成功",
              type: 'success'
            });
            this.getTemplates();
          }
        },
        uploadError: function(err, file, fileList){
          this.$message({
            message: "上传文件错误" + JSON.stringify(err),
            type: 'error'
          });
        }
      },
      mounted(){
      var self = this;
      this.$nextTick(function() {
        self.$parent.$parent.$parent.$parent.$parent.setActive('/BundleTemplateManage');
      });
        this.getTemplates();

        if (this.uploadUrl.indexOf('__requestIP__') !== -1) {
						var requestIP = document.location.host.split(':')[0]
            this.uploadUrl = this.uploadUrl.replace('__requestIP__', requestIP)
			  }
      }
    }
</script>

<style scoped>

</style>
