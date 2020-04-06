<template>
  <section>
    <el-tabs v-model="activeTab" @tab-click="handleClick">
      <el-tab-pane label="服务设备信息" name="serInfoTab"></el-tab-pane>
      <el-tab-pane label="服务节点信息" name="serNodeTab"></el-tab-pane>
    </el-tabs>

    <!--工具栏 TODO-->
    <el-form :inline="true" :model="filters" ref="filters" label-width="80px" size="mini">
      <el-col :span="24" class="toolbar" style="padding-bottom: 0px; margin:0px">
        <!---
                <el-form-item prop="keyword" label="关键字" :span="6">
                    <el-input v-model="filters.keyword"></el-input>
                </el-form-item>
                <el-form-item :span="3">
                    <el-button type="primary" v-on:click="queryList()">查询</el-button>
                </el-form-item>
                <el-form-item :span="3">
                    <el-button  v-on:click="resetForm('filters')">重置</el-button>
                </el-form-item>
        -->
        <el-form-item :span="3">
          <el-button  v-on:click="queryList()">查询</el-button>
        </el-form-item>

        <el-form-item :span="3">
            <el-button type="primary" v-on:click="handleShowAddDialog()">新增</el-button>
        </el-form-item>

        <el-form-item :span="3">
            <el-dropdown>
                <el-button type="primary" >
                    LDAP操作<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>

                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item  v-on:click.native="handleSyncToLdap()">上传到LDAP</el-dropdown-item>
                    <el-dropdown-item  v-on:click.native="handleSyncFromLdap()">从LDAP下载</el-dropdown-item>
                    <el-dropdown-item  v-on:click.native="handleCleanUpLdap()">重置LDAP数据</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-form-item>


        <!--
        <el-form-item :span="3">
          <el-button v-on:click="handleSyncFromLdap()">从LDAP下载</el-button>
        </el-form-item>
        <el-form-item :span="3">
          <el-button v-on:click="handleSyncToLdap()">上传到LDAP</el-button>
        </el-form-item>
        <el-form-item :span="3">
          <el-button v-on:click="handleCleanUpLdap()">清空LDAP数据</el-button>
        </el-form-item>
        -->
      </el-col>
    </el-form>

    <!-- SerInfo列表 -->
    <el-table :data="serInfoVOs" highlight-current-row v-loading="listLoading" v-if="activeTab==='serInfoTab'" @selection-change="selsChange" style="width: 100%;">
        <!--
        <el-table-column prop="id" v-if="false" width="60"></el-table-column>
        <el-table-column prop="serUuid" label="设备UUID"></el-table-column>
        -->
        <el-table-column prop="serNo" label="设备编号" width="180"></el-table-column>
        <el-table-column prop="serName" label="设备名称" width="200"></el-table-column>
        <el-table-column prop="serAddr" label="IP地址" width="180"></el-table-column>
        <el-table-column prop="serPort" label="端口" width="100"></el-table-column>
        <el-table-column prop="serType" label="设备类型" :formatter="serTypeFormat" width="180" sortable></el-table-column>
        <el-table-column prop="sourceType" label="来源" :formatter="sourceTypeFormat" width="100"></el-table-column>
        <el-table-column prop="syncStatus" label="同步状态" :formatter="syncStatusFormat" width="100"></el-table-column>

        <el-table-column label="操作">
            <template slot-scope="scope">
            <el-button
                size="small"
                v-if="scope.row.sourceType==='SYSTEM'"
                @click="handleShowEditDialog(scope.$index, scope.row)"
            >编辑</el-button>
            <el-button
                size="small"
                v-if="scope.row.sourceType==='SYSTEM'"
                @click="handleDelSerInfo(scope.$index, scope.row)"
            >删除</el-button>
            </template>
        </el-table-column>
        </el-table>

    <!--表底工具条 分页-->
    <!--数据量小, 不需要 -->
    <!--
        <el-col :span="24" class="toolbar">
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
            </el-pagination>
        </el-col>
    -->

    <!--SerInfo编辑、新增界面-->
    <el-dialog title="服务设备信息编辑" :visible.sync="serInfoEditFormVisible" :close-on-click-modal="false">
        <el-form :model="serInfoEditForm" label-width="160px" ref="serInfoEditForm" :rules="serInfoEditFormRules">
            <el-form-item label="序号" prop="id" v-if="false">
			    <el-input v-model="serInfoEditForm.id" auto-complete="off"></el-input>
			</el-form-item>
            <el-form-item label="设备UUID" prop="serUuid" v-if="serInfoEditBtnVisible">
                <el-input v-model="serInfoEditForm.serUuid" auto-complete="off" readonly></el-input>
            </el-form-item>
            <el-form-item label="设备编号" prop="serNo">
                <el-input v-model="serInfoEditForm.serNo" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="设备名称" prop="serName">
                <el-input v-model="serInfoEditForm.serName" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="IP地址" prop="serAddr">
                <el-input v-model="serInfoEditForm.serAddr" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="端口" prop="serPort">
                <el-input v-model="serInfoEditForm.serPort" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="serPwd">
                <el-input v-model="serInfoEditForm.serPwd" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="设备类型" prop="serType">
                <template>
                    <el-select v-model="serInfoEditForm.serType" placeholder="请选择">
                    <el-option
                        v-for="item in serTypeOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    ></el-option>
                    </el-select>
                </template>
            </el-form-item>
            <el-form-item label="协议类型" prop="serPro">
                <template>
                    <el-select v-model="serInfoEditForm.serPro" placeholder="请选择">
                    <el-option
                        v-for="item in serProOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    ></el-option>
                    </el-select>
                </template>
            </el-form-item>
            <el-form-item label="服务节点归属" prop="serNode">
                <el-input v-model="serInfoEditForm.serNode" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="厂商信息" prop="serFactInfo">
                <el-input type="textarea" v-model="serInfoEditForm.serFactInfo" auto-complete="off"></el-input>
            </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleEditSerInfo()" v-if="serInfoEditBtnVisible" >修改</el-button>
        <el-button size="small" @click="handleAddSerInfo()" v-if="serInfoAddBtnVisible">新增</el-button>
        <el-button size="small" @click.native="serInfoEditFormVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-------------------------------------------------------------------------------------------------------------->
    <!-------------------------------------------------------------------------------------------------------------->

    <!-- SerNode列表 -->
    <el-table :data="serNodeVOs" highlight-current-row v-loading="listLoading" v-if="activeTab==='serNodeTab'" @selection-change="selsChange" style="width: 100%;">
        <el-table-column prop="id" v-if="false" width="60"></el-table-column>
        <el-table-column prop="nodeName" label="节点名称" width="160"></el-table-column>
        <el-table-column prop="nodeUuid" label="节点UUID" width="280"></el-table-column>
        <el-table-column prop="nodeFather" label="上级服务节点ID" width="280" sortable></el-table-column>
        <el-table-column prop="nodeRelations" label="关联服务节点ID" width="160" sortable></el-table-column>
        <el-table-column prop="nodeFactInfo" label="厂商名称" width="160"></el-table-column>
        <el-table-column prop="sourceType" label="来源" :formatter="sourceTypeFormat" width="100"></el-table-column>
        <el-table-column prop="syncStatus" label="同步状态" :formatter="syncStatusFormat" width="100"></el-table-column>

        <el-table-column label="操作">
            <template slot-scope="scope">
            <el-button
                size="small"
                v-if="scope.row.sourceType==='SYSTEM'"
                @click="handleShowEditDialog(scope.$index, scope.row)"
            >编辑</el-button>
            <el-button
                size="small"
                v-if="scope.row.sourceType==='SYSTEM'"
                @click="handleDelSerNode(scope.$index, scope.row)"
            >删除</el-button>
            </template>
        </el-table-column>
    </el-table>

    <!--SerNode编辑、新增界面-->
    <el-dialog title="服务节点信息编辑" :visible.sync="serNodeEditFormVisible" :close-on-click-modal="false" :before-close="beforeClose">
      <el-form :model="serNodeEditForm" label-width="160px" ref="serNodeEditForm" :rules="serNodeEditFormRules" >

        <el-form-item label="序号" prop="id" v-if="false">
			<el-input v-model="serNodeEditForm.id" auto-complete="off"></el-input>
	    </el-form-item>
        <el-form-item label="服务节点UUID" prop="nodeUuid" v-if="serNodeEditBtnVisible">
            <el-input v-model="serNodeEditForm.nodeUuid" auto-complete="off" readonly ></el-input>
        </el-form-item>

        <el-form-item label="服务节点名称" prop="nodeName">
          <el-input v-model="serNodeEditForm.nodeName" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="上级服务节点" prop="nodeFather" v-if="serNodeEditBtnVisible">
          <el-tag
            v-for="tag in fatherTag"
            :key="tag.nodeUuid"
            closable
            @close="handleFatherClose(tag)">
            {{tag.nodeName}}
          </el-tag>

          <el-button
            icon="el-icon-plus"
            class="button-new-tag-father"
            size="small"
            @click="addFatherNode">
          </el-button>
        </el-form-item>

        <el-form-item label="关联服务节点" prop="nodeRelations" v-if="serNodeEditBtnVisible">
            <el-tag
              v-for="tag in relationTag"
              :key="tag.nodeUuid"
              closable
              @close="handleClose(tag)">
              {{tag.nodeName}}
            </el-tag>

            <el-button
              icon="el-icon-plus"
              class="button-new-tag-relation"
              size="small"
              @click="addRelationNode">
            </el-button>
        </el-form-item>

        <el-form-item label="厂商信息" prop="nodeFactInfo">
            <el-input type="textarea" v-model="serNodeEditForm.nodeFactInfo" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleEditSerNode()" v-if="serNodeEditBtnVisible">修改</el-button>
        <el-button size="small" @click="handleAddSerNode()" v-if="serNodeAddBtnVisible">新增</el-button>
        <el-button size="small" @click="handleEditSerNodeClose()">关闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
        title="选择节点"
        :visible.sync="dialog.node.visible"
        width="800px"
        :before-close="handleDialogClose">

        <div style="height:450px; position:relative;">
          <div style="position:absolute; left:-20px; top:-20px; right:-20px; bottom:-20px;">
            <div style="width:100%; height:100%; position:relative;">
              <div style="width:100%; position:absolute; left:0; top:0px; right:0; bottom:0; padding:0 10px; box-sizing:border-box;">
                <el-table
                  ref="selectNodeTable"
                  :data="dialog.node.table.data"
                  height="100%"
                  style="width:100%"
                  @selection-change="handleSelectionChange">
                  <el-table-column
                    type="selection"
                    width="55">
                  </el-table-column>
                  <el-table-column
                    prop="nodeName"
                    width="300"
                    label="节点名称">
                  </el-table-column>
                  <el-table-column
                    prop="nodeUuid"
                    label="节点Uuid">
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
        </div>

        <span slot="footer" class="dialog-footer">
            <el-button size="small" @click="handleDialogClose">取消</el-button>
            <el-button size="small" type="primary" @click="handleDialogCommit">确定</el-button>
        </span>

    </el-dialog>
  </section>
</template>

<script type="text/ecmascript-6">

import {
    querySerInfo,
    querySerNode,
    addSerInfo,
    addSerNode,
    cleanUpSerInfo,
    cleanUpSerNode,
    syncSerNodeFromLdap,
    syncSerNodeToLdap,
    syncSerInfoFromLdap,
    syncSerInfoToLdap,
    delSerInfo,
    delSerNode,
    queryFatherNodeOptions,
    modifySerNode,
    modifySerInfo
} from "../../api/api";

export default {
    data() {
        return {
            filters: {},
            activeTab: 'serInfoTab',

            serInfoVOs: [],
            total: 0,
            page: 1,
            listLoading: false,
            sels: [], //列表选中列

            serInfoEditFormVisible: false, //编辑界面是否显示
            serInfoEditBtnVisible:false,   //弹出框中编辑按钮是否显示
            serInfoAddBtnVisible: false,  //弹出框中新建按钮新否显示
            editLoading: false,

            serTypeOptions: [
                {
                    value: 4,
                    label: "应用服务单元"
                },
                {
                    value: 5,
                    label: "信令控制服务单元"
                },
                {
                    value: 6,
                    label: "媒体处理服务单元"
                }
            ],

            serProOptions: [
                {
                    value: 1,
                    label: "TCP"
                },
                {
                    value: 2,
                    label: "UDP"
                },
                {
                    value: 3,
                    label: "TCP和UDP"
                }
            ],

            serInfoEditForm: {
                id: 0,
                nodeUuid:"",
                nodeName:"",
                nodeFather:'',
                nodeRelations: '',
                nodeFactInfo: ""
            },

            serInfoEditFormRules: {
                //TODO
            },

            ///////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////
            serNodeVOs: [],

            serNodeEditFormVisible: false, //编辑界面是否显示
            serNodeEditBtnVisible:false,   //弹出框中编辑按钮是否显示
            serNodeAddBtnVisible: false,  //弹出框中新建按钮新否显示

            serNodeEditForm: {
                id: 0,
                serUuid:"",
                serName:"",
                serAddr:"",
                serPort: 0,
                serPwd: "",
                serNode: "",
                serFactInfo: ""
            },

            serNodeEditFormRules: {
                //TODO
            },

            fatherNodeOptions: [], //服务上级节点选择项
            relationNodeOptions: [], //服务关联节点选择项

            fatherTag:[],
            relationTag:[],

            dialog:{
              node:{
                type:'',
                table:{
                  data:[]
                },
                visible: false,
                selection:[]
              }
            }

        };

    },

    methods: {


        //标签页
        handleClick(tab, event) {
            this.queryList();
        },

        selsChange: function (sels) {
		    this.sels = sels;
        },

        //重置表单
        resetForm(formName) {
            if (this.$refs[formName] != undefined) {
                this.$refs[formName].resetFields();
            }
        },

        serTypeFormat: function(row, column) {

            var dateString = row[column.property];

            if (dateString == undefined) {
                return "";
            }

            switch (dateString) {
                case 1: return '业务控制单元'
                case 4: return '应用服务单元'
                case 5: return '信令控制服务单元'
                case 6: return '媒体处理服务单元'

            }

        },

        syncStatusFormat: function(row, column) {

            var dateString = row[column.property];

            if (dateString == undefined) {
                return "";
            }

            switch (dateString) {
                case 'ASYNC': return '未同步'
                case 'SYNC': return '已同步'
            }

        },

        sourceTypeFormat: function (row, column) {

            var dateString = row[column.property];

            if (dateString == undefined) {
                return "";
            }

            switch (dateString) {
                case 'SYSTEM': return 'BVC'
                case 'EXTERNAL': return 'LDAP'
            }
        },

        // 获取serInfo列表
        queryList() {
            let para = {
                // TODO
            };

            this.listLoading = true;

            if (this.activeTab === 'serInfoTab') {
                querySerInfo(para).then(res => {
                    if(null !== res.errMsg && res.errMsg !== "") {
                        this.$message({
                            message: res.errMsg,
                            type: "error",
                            duration: 3000
                        });
                    } else {
                        this.serInfoVOs = res.serInfoVOs;
                    }

                    this.listLoading = false;
                });
            } else if (this.activeTab === 'serNodeTab') {
                querySerNode(para).then(res => {
                    if(null !== res.errMsg && res.errMsg !== "") {
                        this.$message({
                            message: res.errMsg,
                            type: "error",
                            duration: 3000
                        });
                    } else {
                        this.serNodeVOs = res.serNodeVOs;
                    }

                    this.listLoading = false;
                });
            }
        },


        // 显示添加SerInfo对话框
        handleShowAddDialog: function() {
            console.log("add");
            if (this.activeTab === 'serInfoTab') {
                this.resetForm("serInfoEditForm");
                this.serInfoAddBtnVisible = true;
                this.serInfoEditFormVisible = true;
                this.serInfoEditBtnVisible = false;
            } else if (this.activeTab === 'serNodeTab') {

                let para = {};
                queryFatherNodeOptions(para).then(res => {
                    if (null !== res.errMsg && res.errMsg !== "") {
                        console.log(queryFatherNodeOptions, error);
                    } else {
                        this.fatherNodeOptions = res.serNodeVOs;
                        this.relationNodeOptions = res.serNodeVOs;
                        console.log("queryFatherNodeOptions, success", JSON.stringify(res.serNodeVOs));

                    }
                });

                this.resetForm("serNodeEditForm");
                this.serNodeAddBtnVisible = true;
                this.serNodeEditFormVisible = true;
                this.serNodeEditBtnVisible = false;
            }
        },

        // 显示修改对话框
        handleShowEditDialog: function(index, row) {
            console.log("edit");
            if (this.activeTab === 'serInfoTab') {
                this.resetForm("serInfoEditForm");
                this.serInfoAddBtnVisible = false;
                this.serInfoEditBtnVisible = false;
                this.serInfoEditFormVisible = true;
                this.serInfoEditBtnVisible = true;
                this.serInfoEditForm = Object.assign({}, row);

            } else if (this.activeTab === 'serNodeTab') {

                let para = {};
                queryFatherNodeOptions(para).then(res => {
                    if (null !== res.errMsg && res.errMsg !== "") {
                        console.log(queryFatherNodeOptions, error);
                    } else {
                        this.fatherNodeOptions = res.serNodeVOs;
                        //console.log("queryFatherNodeOptions, success", JSON.stringify(res.serNodeVOs));


                    }
                });

                var nodes = this.serNodeVOs;

                if(row.nodeFather != "NULL"){
                    var father = row.nodeFather.split(",");

                    for(var i=0;i<father.length;i++){
                      for(var j=0; j<nodes.length; j++){
                        if(nodes[j].nodeUuid === father[i]){
                          this.fatherTag.push(nodes[j]);
                          console.log(this.fatherTag);
                          break;
                        }
                      }
                    }
                }

                if(row.nodeRelations != "NULL"){
                  var relations = row.nodeRelations.split(",");
                  console.log(relations);
                  for(var i=0;i<relations.length;i++){
                    for(var j=0; j<nodes.length; j++){
                      if(nodes[j].nodeUuid === relations[i]){
                        this.relationTag.push(nodes[j]);
                        break;
                      }
                    }
                  }
                }

                this.resetForm("serNodeEditForm");
                this.serNodeAddBtnVisible = false;
                this.serNodeEditBtnVisible = false;
                this.serNodeEditFormVisible = true;
                this.serNodeEditBtnVisible = true;
                this.serNodeEditForm = Object.assign({}, row);

            }
        },

        handleAddSerInfo: function () {
            this.$refs.serInfoEditForm.validate((valid) => {
                if (valid) {
                    this.$confirm("确认新增服务设备信息吗？", "提示", {}).then(() => {
                        this.editLoading = true;
                        //NProgress.start();

                        let para = {

                            serNo: this.serInfoEditForm.serNo,
                            serName: this.serInfoEditForm.serName,
                            serAddr: this.serInfoEditForm.serAddr,
                            serPort: this.serInfoEditForm.serPort,
                            serPwd: this.serInfoEditForm.serPwd,
                            serType: this.serInfoEditForm.serType,
                            serPro: this.serInfoEditForm.serPro,
                            serNode: this.serInfoEditForm.serNode,
                            serFactInfo: this.serInfoEditForm.serFactInfo,
                        };

                        console.log(JSON.stringify(para));

                        addSerInfo(para).then(res => {
                            if (null !== res.errMsg && res.errMsg !== "") {
                                this.$message({
                                message: res.errMsg,
                                type: "error",
                                duration: 3000
                                });
                            } else {
                                this.$message({
                                    message: "新增成功",
                                    type: "success"
                                });
                                this.$refs["serInfoEditForm"].resetFields();
                                this.serInfoEditFormVisible = false;
                                this.addSerInfoBtnVisibale = false;
                                this.queryList();
                            }
                        });
                        this.editLoading = false;
                    });
                }
	        });
        },

        handleEditSerInfo: function () {
            this.$refs.serInfoEditForm.validate((valid) => {
                if (valid) {
                    this.$confirm("确认修改服务设备信息吗？", "提示", {}).then(() => {
                        this.editLoading = true;
                        //NProgress.start();

                        let para = {
                            id: this.serInfoEditForm.id,
                            serUuid: this.serInfoEditForm.serUuid,
                            serNo: this.serInfoEditForm.serNo,
                            serName: this.serInfoEditForm.serName,
                            serAddr: this.serInfoEditForm.serAddr,
                            serPort: this.serInfoEditForm.serPort,
                            serPwd: this.serInfoEditForm.serPwd,
                            serType: this.serInfoEditForm.serType,
                            serPro: this.serInfoEditForm.serPro,
                            serNode: this.serInfoEditForm.serNode,
                            serFactInfo: this.serInfoEditForm.serFactInfo,
                        };

                        console.log(JSON.stringify(para));

                        modifySerInfo(para).then(res => {
                            if (null !== res.errMsg && res.errMsg !== "") {
                                this.$message({
                                message: res.errMsg,
                                type: "error",
                                duration: 3000
                                });
                            } else {
                                this.$message({
                                    message: "修改成功",
                                    type: "success"
                                });
                                this.$refs["serInfoEditForm"].resetFields();
                                this.serInfoEditFormVisible = false;
                                this.serInfoEditBtnVisibale = false;
                                this.queryList();
                            }
                        });
                        this.editLoading = false;
                    });
                }
	        });
        },


        handleAddSerNode: function () {
            this.$refs.serNodeEditForm.validate((valid) => {
                if (valid) {
                    this.$confirm("确认新增服务节点信息吗？", "提示", {}).then(() => {
                        this.editLoading = true;
                        //NProgress.start();

                        let para = {
                            //id: this.editForm.id,
                            // nodeUuid: this.serNodeEditForm.nodeUuid,
                            nodeName: this.serNodeEditForm.nodeName,
                            nodeFather: this.serNodeEditForm.nodeFather,
                            nodeRelations: this.serNodeEditForm.nodeRelations,
                            nodeFactInfo: this.serNodeEditForm.nodeFactInfo,
                        };

                        console.log(JSON.stringify(para));

                        addSerNode(para).then(res => {
                            if (null !== res.errMsg && res.errMsg !== "") {
                                this.$message({
                                    message: res.errMsg,
                                    type: "error",
                                    duration: 3000
                                });
                            } else {
                                this.$message({
                                    message: "新增成功",
                                    type: "success"
                                });
                                this.$refs["serNodeEditForm"].resetFields();
                                this.serNodeEditFormVisible = false;
                                this.serNodeAddBtnVisibale = false;
                                this.queryList();
                            }
                        });
                        this.editLoading = false;
                    });
                }
	        });
        },

        handleEditSerNode: function () {
            this.$refs.serNodeEditForm.validate((valid) => {
                if (valid) {
                    this.$confirm("确认修改服务节点信息吗？", "提示", {}).then(() => {
                        this.editLoading = true;
                        //NProgress.start();

                        var nodeFatherArray = [];
                        var nodeRelationArray = [];

                        for(var i=0; i<this.fatherTag.length;i++){
                          nodeFatherArray.push(this.fatherTag[i].nodeUuid);
                        }

                        for(var i=0; i<this.relationTag.length;i++){
                          nodeRelationArray.push(this.relationTag[i].nodeUuid);
                        }

                        let para = {
                            //id: this.editForm.id,
                            nodeUuid: this.serNodeEditForm.nodeUuid,
                            nodeName: this.serNodeEditForm.nodeName,
                            nodeFather: nodeFatherArray.join(','),
                            nodeRelations: nodeRelationArray.join(','),
                            nodeFactInfo: this.serNodeEditForm.nodeFactInfo
                        };

                        modifySerNode(para).then(res => {
                            if (null !== res.errMsg && res.errMsg !== "") {
                                this.$message({
                                    message: res.errMsg,
                                    type: "error",
                                    duration: 3000
                                });
                            } else {
                                this.$message({
                                    message: "修改成功",
                                    type: "success"
                                });
                                this.$refs["serNodeEditForm"].resetFields();
                                this.serNodeEditFormVisible = false;
                                this.serNodeEditBtnVisible = false;
                                this.queryList();
                            }
                        });
                        this.editLoading = false;

                        this.fatherTag = [];
                        this.relationTag = [];
                    });
                }
	        });
        },

        handleEditSerNodeClose: function(){
           this.serNodeEditFormVisible = false;
           this.fatherTag = [];
           this.relationTag = [];
        },

        beforeClose: function(done) {
           this.fatherTag = [];
           this.relationTag = [];
           done();
        },

        handleDelSerInfo: function(index, row) {
            // alert(JSON.stringify(row));

            this.$confirm("删除此服务设备信息，确认？", "提示", {}).then(() => {
                let para = {
                    serUuid: row.serUuid
                };

                delSerInfo(para).then(res => {
                    if (null !== res.errMsg && res.errMsg !== "") {
                        this.$message({
                            message: res.errMsg,
                            type: "error",
                            duration: 3000
                        });
                    } else {
                        this.queryList();
                    }
                });
            });
        },


        handleDelSerNode: function(index, row) {

            this.$confirm("删除此服务节点信息，确认？", "提示", {}).then(() => {
                let para = {
                    nodeUuid: row.nodeUuid
                };

                delSerNode(para).then(res => {
                    if (null !== res.errMsg && res.errMsg !== "") {
                        this.$message({
                            message: res.errMsg,
                            type: "error",
                            duration: 3000
                        });
                    } else {
                        this.queryList();
                    }
                });
            });
        },

        ////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////

        handleSyncToLdap: function(){
            this.$confirm("确认将本地未同步信息同步到LDAP服务器么？", "提示", {}).then(() => {
                this.editLoading = true;
                //NProgress.start();

                let para = {};

                if(this.activeTab === 'serInfoTab'){
                    syncSerInfoToLdap(para).then(res => {
                        if (null !== res.errMsg && res.errMsg !== "") {
                            this.$message({
                                message: res.errMsg,
                                type: "error",
                                duration: 3000
                            });
                        } else {
                            this.$message({
                                message: "同步成功, 共上传" + res.successCnt + "条信息",
                                type: "success"
                            });

                            this.queryList();
                        }
                    });
                } else if (this.activeTab === 'serNodeTab'){
                    syncSerNodeToLdap(para).then(res => {
                        if (null !== res.errMsg && res.errMsg !== "") {
                            this.$message({
                                message: res.errMsg,
                                type: "error",
                                duration: 3000
                            });
                        } else {
                            this.$message({
                                 message: "同步成功, 共上传" + res.successCnt + "条信息",
                                type: "success"
                            });

                            this.queryList();
                        }
                    });
                }

                this.editLoading = false;
            });
        },

        handleSyncFromLdap: function(){
            this.$confirm("确认将从LDAP服务器同步下载信息么？", "提示", {}).then(() => {
                this.editLoading = true;
                //NProgress.start();

                let para = {};

                if(this.activeTab === 'serInfoTab'){
                    syncSerInfoFromLdap(para).then(res => {
                        if (null !== res.errMsg && res.errMsg !== "") {
                            this.$message({
                                message: res.errMsg,
                                type: "error",
                                duration: 3000
                            });
                        } else {
                            this.$message({
                                message: "同步成功, 共下载" + res.successCnt + "条信息",
                                type: "success"
                            });

                            this.queryList();
                        }
                    });
                } else if (this.activeTab === 'serNodeTab'){
                    syncSerNodeFromLdap(para).then(res => {
                        if (null !== res.errMsg && res.errMsg !== "") {
                            this.$message({
                                message: res.errMsg,
                                type: "error",
                                duration: 3000
                            });
                        } else {
                            this.$message({
                                message: "同步成功, 共下载" + res.successCnt + "条信息",
                                type: "success"
                            });

                            this.queryList();
                        }
                    });
                }

                this.editLoading = false;
            });
        },


        handleCleanUpLdap: function() {
            this.$confirm("确认清空上传到LDAP何从LDAP下载的所有信息么？", "提示", {}).then(() => {
                this.editLoading = true;
                //NProgress.start();

                let para = {};

                if(this.activeTab === 'serInfoTab'){
                    cleanUpSerInfo(para).then(res => {
                        if (null !== res.errMsg && res.errMsg !== "") {
                            this.$message({
                                message: res.errMsg,
                                type: "error",
                                duration: 3000
                            });
                        } else {
                            this.$message({
                                message: "重置成功",
                                type: "success"
                            });

                            this.queryList();
                        }
                    });
                } else if (this.activeTab === 'serNodeTab'){
                    cleanUpSerNode(para).then(res => {
                        if (null !== res.errMsg && res.errMsg !== "") {
                            this.$message({
                                message: res.errMsg,
                                type: "error",
                                duration: 3000
                            });
                        } else {
                            this.$message({
                                message: "重置成功",
                                type: "success"
                            });

                            this.queryList();
                        }
                    });
                }

                this.editLoading = false;
            });
        },

        handleClose: function(tag) {
          var self = this;
          var table = self.fatherNodeOptions;
          var selection = self.dialog.node.selection;
          table.push(tag);
          for(var i=self.relationTag.length-1;i>=0;i--){
            if(self.relationTag[i].nodeUuid === tag.nodeUuid){
              self.relationTag.splice(i,1);
              break;
            }
          }

        },

        handleFatherClose: function(tag) {
          var self = this;
          var table = self.fatherNodeOptions;
          var selection = self.dialog.node.selection;
          table.push(tag);
          for(var i=self.fatherTag.length-1;i>=0;i--){
            if(self.fatherTag[i].nodeUuid === tag.nodeUuid){
              self.fatherTag.splice(i,1);
              break;
            }
          }

        },

        handleDialogCommit: function(){
          var self = this;

          var table = self.dialog.node.table.data;
          for(var i=0; i<self.dialog.node.selection.length; i++){
            for(var j=table.length-1; j>=0; j--){
              if(table[j].nodeUuid === self.dialog.node.selection[i].nodeUuid){
                table.splice(j,1);
                break;
              }
            }
          }

          if(self.dialog.node.type === "relation"){
              for(var i=0; i<self.dialog.node.selection.length; i++){
                self.relationTag.push(self.dialog.node.selection[i]);
              }
          }

          if(self.dialog.node.type === "father"){
            for(var i=0; i<self.dialog.node.selection.length; i++){
              self.fatherTag.push(self.dialog.node.selection[i]);
            }
          }


          self.dialog.node.visible = false;
        },

        handleSelectionChange: function(val){
          var self = this;
          self.dialog.node.selection = val;
        },

        handleDialogClose: function(){
          var self = this;
          self.$refs.selectNodeTable.clearSelection();
          self.dialog.node.visible = false;
        },

        addRelationNode: function(){
          var self = this;
          self.dialog.node.type = "relation";
          self.dialog.node.table.data = self.fatherNodeOptions;
          self.dialog.node.visible = true;
        },

        addFatherNode: function(){
          var self = this;
          self.dialog.node.type = "father";
          self.dialog.node.table.data = self.fatherNodeOptions;
          self.dialog.node.visible = true;
        }
    },

    mounted() {
        this.queryList();
    }


}

</script>
