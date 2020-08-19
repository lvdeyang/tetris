<template>
	<section>

    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left ;width:100%;" >
      <el-tab-pane label="绑定设备资源" name="BindResource"></el-tab-pane>
      <el-tab-pane label="绑定虚拟资源" name="BindVirtualResource"></el-tab-pane>
    </el-tabs>

    <el-card style="float:left;margin-top:10px;width:20%;font-size: 18px;" body-style="padding:0px">
      <!--<div slot="header" class="clearfix">
        <span>角色</span>
      </div>-->

      <el-table ref="roleTable" :data="roles" highlight-current-row v-loading="roleTableLoading" @current-change="handleRoleTableRowChange" style="width: 100%;">
        <el-table-column prop="name" label="角色名" sortable>
        </el-table-column>
      </el-table>
    </el-card>


    <el-card style="float:left;margin-left:50px;margin-top:10px;width:70%;" body-style="padding:0px">

      <div slot="header" class="clearfix">
        <span style="float: left;font-size: 18px;">业务资源</span>
        <el-select size="small" v-model="filters.bindType" placeholder="选择绑定状态" style="float: left;margin-left: 100px;">
          <el-option v-for="item in bindTypeOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-input size="small" v-model="filters.keyword" style="float: left;margin-left: 50px;width:150px;" placeholder="关键字"></el-input>
        <el-button size="small" type="info" @click="getResources" style="float: left;margin-left: 50px;">查询</el-button>
      </div>

      <!--资源列表-->
      <el-table :data="resources" v-loading="resourceTableLoading" style="width: 100%;" @selection-change="handleSelectionChange">
        <el-table-column width="150" :render-header="renderCheckHeader">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.hasPrivilege" @change="handleCheckChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>
        </el-table-column>
        <el-table-column prop="name" label="名称" width="350" sortable>
        </el-table-column>
        <!--
        <el-table-column prop="type" label="类型" width="200" sortable>
        </el-table-column>
        -->
        <el-table-column prop="resourceId" label="资源ID" width="350"  sortable>
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="handleDetail(scope.row)" size="small">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-button type="primary" @click="submitPrivilege">提交</el-button>
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentPageChange" :page-size="countPerPage" :total="total" style="float:right;">
        </el-pagination>
      </el-col>

    </el-card>

    <el-dialog title="详情" :visible.sync="detailDialogVisible">
      <ul>
        <li v-for="detailInfo in detailInfos">
          {{ detailInfo.attrName }} :  {{ detailInfo.attrValue }}
        </li>
      </ul>
    </el-dialog>

	</section>
</template>

<script type="text/ecmascript-6">
	import { getAllRoles,getVirtualResourcesOfRole,submitVirtualResourcePrivilege,getVirtualResourceDetailInfo} from '../../api/api';

	export default {
		data() {
			return {
        activeTabName : "BindVirtualResource",
        roles : [
          ],
        currentRoleRow : null,
        roleTableLoading : false,
				resources: [
				  ],
        filters: {
          keyword: '',
          bindType : '',
        },
        bindTypeOptions : [
          {
            value : "all",
            label : "全部"
          },
          {
            value : "binded",
            label : "已绑定"
          },
          {
            value : "unbinded",
            label : "未绑定"
          }
        ],
				total: 0,
				pageNum: 1,
        countPerPage : 10,
        resourceTableLoading: false,
        checkAll : false,
        prevChecks : [],//之前的权限
        checks : [],//新的可写权限
        detailDialogVisible : false,
        detailInfos : []
			}
		},
		methods: {
      handleTabClick(tab, event) {
        if("BindVirtualResource" !== tab.name){
          this.$router.push('/' + tab.name);
        }
      },
      handleRoleTableRowChange : function(val){
        this.currentRoleRow = val;
        //TODO 选中角色，
        this.getResources();
      },
      handleSelectionChange : function(val){
        this.checks = val;
      },
			//获取角色列表
      getRoles : function() {
        let param = {
        };
        this.roleTableLoading = true;
        getAllRoles(param).then( res => {
          if(res.errMsg){
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          }else{
            this.roles = res.roles;
          }

          this.roleTableLoading = false;
        });

			},
      //获取资源列表
      getResources : function(){
        this.prevChecks = [];
        this.checks = [];
        let param = {
          roleId : this.currentRoleRow.id,
          bindType: this.filters.bindType,
          keyword : this.filters.keyword,
          pageNum : this.pageNum,
          countPerPage : this.countPerPage
        };
        this.resourceTableLoading = true;
        getVirtualResourcesOfRole(param).then((res) => {
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.total = res.total;
            this.resources = res.resources;
            for (let resource of this.resources){
              if(resource.hasPrivilege){
                this.prevChecks.push(resource.resourceId);
                this.checks.push(resource.resourceId);
              }
            }
          }

          this.resourceTableLoading = false;
        });
      },
      renderCheckHeader : function(h, data){//自定义特殊表格头单元
        return (
          <el-checkbox v-model={this.checkAll} onChange={this.handleCheckAllChange}>全选</el-checkbox>
        )
      },
      handleCheckAllChange : function(val){
        if(val){
          for (let resource of this.resources){
            this.checks.push(resource.resourceId);
            resource.hasPrivilege = true;
          }
        } else {
          this.checks = [];
          for (let resource of this.resources){
            resource.hasPrivilege = false;
          }
        }
      },
      handleCheckChange : function(row){
        if(row.hasPrivilege){
          this.checks.push(row.resourceId);
          //可读全选判断
          if(this.checks.length === this.resources.length){
            this.checkAll = true;
          }
        } else {
          this.checks.splice(this.checks.indexOf(row.resourceId),1);
          this.checkAll = false;
        }
      },
      //资源列表分页
      handleCurrentPageChange(val) {
        this.pageNum = val;
        this.getResources();
      },
			//资源详情
      handleDetail : function(row){
        let param = {
          resourceId : row.resourceId
        };
        getVirtualResourceDetailInfo(param).then(res => {
          if(res.errMsg){
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.detailDialogVisible = true;
            this.detailInfos = res.infos;
          }
        });
      },
      //提交资源权限
      submitPrivilege : function(){
        let param = {
          roleId : this.currentRoleRow.id,
          prevChecks : this.prevChecks.join(","),
          checks : this.checks.join(",")
        };

        this.resourceTableLoading = true;
        submitVirtualResourcePrivilege(param).then((res) => {
          if(res.errMsg){
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.$message({
              message: "提交成功",
              type: 'success'
            });
            //更新prevChecks
            this.prevChecks = [].concat(this.checks);
          }
          this.resourceTableLoading = false;
        });
      }
		},
		mounted() {
        var self = this;
        self.$nextTick(function(){
            self.$parent.$parent.$parent.$parent.$parent.setActive('/BindResource');
        });
			this.getRoles();
		}
	}

</script>

<style>
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
</style>
