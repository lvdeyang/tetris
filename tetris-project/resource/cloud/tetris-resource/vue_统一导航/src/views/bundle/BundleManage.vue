<template>
    <section>
        <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left;width:100%;">
            <el-tab-pane label="资源列表" name="BundleManage"></el-tab-pane>
            <el-tab-pane label="添加资源" name="AddBundle"></el-tab-pane>
        </el-tabs>

        <div style="float: left;width: 100%">
            <el-select size="small" v-model="filters.deviceModel" placeholder="选择类型"
                       style="float: left;margin-left: 5px;width:180px;">
                <el-option v-for="item in deviceModelOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>
            <el-select size="small" v-model="filters.sourceType" placeholder="选择来源"
                       style="float: left;margin-left: 10px;width:180px;">
                <el-option v-for="item in sourceTypeOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>
            <el-select size="small" v-model="filters.userId" filterable placeholder="选择用户"
                       style="float: left;margin-left:10px;width:200px;">
                <el-option v-for="item in users" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
            </el-select>
            <el-input size="small" v-model="filters.keyword" style="float: left;margin-left: 15px;width:200px;"
                      placeholder="关键字"></el-input>
            <el-button size="small" @click="getResources(1)" style="float: left;margin-left: 10px;">查询</el-button>

            <el-dropdown style="float: left;margin-left: 20px;">
                <el-button type="primary" size="small">
                    LDAP操作<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-on:click.native="showHandleSyncToLdapDialog()">上传到LDAP</el-dropdown-item>
                    <el-dropdown-item v-on:click.native="handleSyncFromLdap()">从LDAP下载</el-dropdown-item>
                    <el-dropdown-item v-on:click.native="handleCleanUpLdap()">重置LDAP数据</el-dropdown-item>

                </el-dropdown-menu>
            </el-dropdown>

            <el-upload
                    class="upload-demo"
                    :action=uploadUrl
                    name="filePoster"
                    :headers="myHeaders"
                    :show-file-list="false"
                    :on-success="uploadSuccess"
                    :on-error="uploadError"
                    :on-progress="onprogress"
                    style="float: left; margin-left: 10px;">
                <el-button type="primary" size="small">导入</el-button>
            </el-upload>

            <el-button type="primary" size="small" @click="exportBundle" style="float: left; margin-left: 10px;">导出
            </el-button>
            <el-button type="primary" size="small" @click="showMultiSetAccessLayerDialog"
                       style="float: left; margin-left: 10px;">批量设置接入
            </el-button>
            <el-button type="danger" size="small" @click="handleMultiDelete" style="float: left; margin-left: 10px;">
                批量删除
            </el-button>
            <el-button type="danger" size="small" @click="syncUser" style="float: left; margin-left: 10px;">
              同步用户
            </el-button>


            <!--
            <el-button type="primary" size="small" v-on:click="handleCleanUpLdap()" style="float: right;margin-right: 10px;">重置LDAP数据</el-button>
            <el-button type="primary" size="small" v-on:click="showHandleSyncToLdapDialog()" style="float: right;margin-right: 10px;">上传到LDAP</el-button>
            <el-button type="primary" size="small" v-on:click="handleSyncFromLdap()" style="float: right;margin-right: 10px;">从LDAP下载</el-button>
            -->
        </div>

        <!--资源列表-->
        <el-table :data="resources" v-loading="resourceTableLoading" @selection-change="handleSelectionChange"
                  style="float: left;width: 100%;margin-top: 20px;">
            <el-table-column width="50" type="selection"></el-table-column>
            <el-table-column prop="bundleName" label="名称" width="200" sortable>
            </el-table-column>
            <el-table-column prop="deviceModel" label="类型" width="120" sortable>
            </el-table-column>
            <el-table-column prop="bundleAlias" label="别名" width="130" sortable>
            </el-table-column>
            <el-table-column prop="username" label="设备账号" width="200">
            </el-table-column>
            <el-table-column prop="accessNodeUid" label="所属接入层" width="220" sortable>
            </el-table-column>
            <el-table-column width="100" label="在线状态">
                <template slot-scope="scope">
                    <div v-if="scope.row.onlineStatus=='ONLINE'"
                         style="height:20px;width:20px;border-radius:50%;background-color:#0f0"></div>
                    <div v-else style="height:20px;width:20px;border-radius:50%;background-color:#f00"></div>
                </template>
            </el-table-column>

            <el-table-column width="100" label="锁定状态">
                <template slot-scope="scope">
                    <div v-if="scope.row.lockStatus=='IDLE'">空闲</div>
                    <div v-else-if="scope.row.lockStatus=='BUSY'">锁定</div>
                </template>
            </el-table-column>

            <el-table-column width="100" label="来源">
                <template slot-scope="scope">
                    <div v-if="scope.row.sourceType=='EXTERNAL'">LDAP</div>
                    <div v-else>BVC</div>
                </template>
            </el-table-column>

            <el-table-column label="操作" width="250">
                <template slot-scope="scope">
                    <el-button type="text" @click="handleDetail(scope.row)" size="small">详情</el-button>
                    <el-button type="text" @click="handleModify(scope.row)" size="small">修改</el-button>
                    <el-button type="text" @click="handleSetLayerId(scope.row)" size="small">设置接入</el-button>
                    <!--<el-button type="text" v-if="scope.row.onlineStatus=='ONLINE'" @click="handleLogout(scope.row)" size="small">踢出</el-button>-->
                    <el-button type="text" @click="handleClear(scope.row)" size="small">重置</el-button>
                    <el-button type="text" @click="handleDelete(scope.row)" size="small">删除</el-button>
                </template>
            </el-table-column>

            <el-table-column label="能力" width="150">
                <template slot-scope="scope">
                    <!--<el-button type="text" v-if="scope.row.bundleType!='VenusTerminal'" @click="abilityConfig(scope.row)" size="small">配置</el-button>-->
                    <el-button type="text" @click="abilityConfig(scope.row)" size="small">配置</el-button>
                    <el-button type="text" @click="abilityDetail(scope.row)" size="small">通道信息</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="toolbar">
          <el-input size="small" v-model="filters.countPerPage" style="float: right;margin-right: 30px;width:200px;" placeholder="单页显示数量,默认20" @change="pageChange"></el-input>
          <el-pagination layout="prev, pager, next" @current-change="handleCurrentPageChange" :page-size="countPerPage" :total="total" style="float:right;">
            </el-pagination>
        </el-col>

        <el-dialog title="详情" :visible.sync="detailDialogVisible" width="30%">
            <ul>
                <li v-for="detailInfo in detailInfos">
                    {{ detailInfo.name }} : {{ detailInfo.value }}
                </li>
            </ul>
        </el-dialog>

        <el-dialog title="能力详情" :visible.sync="abilityDetailDialogVisible" width="40%">
            <el-table :data="channelSchemes">
                <el-table-column property="channelId" label="通道ID" width="250"></el-table-column>
                <el-table-column property="channelName" label="通道类型" width="250"></el-table-column>
                <el-table-column width="150" label="通道状态">
                    <template slot-scope="scope">
                        <div v-if="scope.row.channelStatus=='IDLE'">空闲</div>
                        <div v-else-if="scope.row.channelStatus=='BUSY'">锁定</div>
                        <div v-else-if="scope.row.channelStatus=='OFFLINE'">掉线</div>
                        <div v-else>未知</div>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>

        <el-dialog title="设置接入" :visible.sync="setAccessLayerVisible" width="30%">
            <div style="margin-top:10px;">
                <el-input v-model="newAccessNodeUid" placeholder="输入接入层ID" style="width: 400px;" readOnly @click.native="handleSelectLayerNode">
                    <template slot="prepend">接入层ID</template>
                </el-input>
            </div>
            <div style="margin-top:30px;">
                <el-button type="primary" style="margin-left:150px;" @click="submitSetAccessLayer()">提交</el-button>
            </div>
        </el-dialog>

        <el-dialog title="提示" :visible.sync="confirmSyncToLdapVisible" width="500px" style="margin-top:260px;">
            <div>
                确认上传信息到LDAP服务器上么？
            </div>
            <div style="margin-top:40px;">
                <el-button size="small" style="margin-left:130px;" @click="confirmSyncToLdapVisible = false">取 消
                </el-button>
                <el-button type="primary" size="small" style="margin-left:10px;" @click="handleSyncToLdap('false')">
                    只上传未同步信息
                </el-button>
                <el-button type="primary" size="small" style="margin-left:10px;" @click="handleSyncToLdap('true')">
                    上传全部信息
                </el-button>
            </div>
        </el-dialog>

        <template>

            <selectLayerNode ref="selectLayerNode" @layer-node-selected="layerNodeSelected"></selectLayerNode>

        </template>

    </section>

</template>

<script type="text/ecmascript-6">
    import { getAllUsers,getDeviceModels,getBundles,getBundleDetailInfo,deleteBundle,getBundleChannels,logoutBundle,clearBundle,setAccessLayer,syncLdap,syncEquipInfoFromLdap,
            syncEquipInfToLdap, cleanUpEquipInfo, exportBundle, syncUser} from '../../api/api';
    // let requestIP = document.location.host.split(":")[0];

    import selectLayerNode from '../layernode/SelectLayerNode';

    let basePath = process.env.RESOURCE_ROOT

    export default {
        components: { selectLayerNode },
        data() {
        return {
            activeTabName: "BundleManage",
            resources: [],
            deviceModelOptions: [],
            filters: {
                deviceModel: '',
                keyword: '',
                userId: '',
                sourceType: '',
                countPerPage:''
            },
            users: [],
            resourceTableLoading: false,
            detailDialogVisible: false,
            abilityDetailDialogVisible: false,
            setAccessLayerVisible: false,
            confirmSyncToLdapVisible: false,
            detailInfos: [],
            channelSchemes: [],
            total: 0,
            pageNum: 1,
            countPerPage: 20,
            currentRow: {},
            bundleId: "",
            newAccessNodeUid: "",
            // uploadUrl : "http://" + requestIP + ":8887/suma-venus-resource/bundle/import",
            uploadUrl: basePath + "/bundle/import",
            myHeaders : {
                          'tetris-001': sessionStorage.getItem('token')
                      },

            sourceTypeOptions: [
                {
                    value: "",
                    label: "全部来源"
                },
                {
                    value: "SYSTEM",
                    label: "BVC"
                },
                {
                    value: "EXTERNAL",
                    label: "LDAP"
                }
            ],
            multipleSelection: []
        }
    },
    methods: {
        pageChange:function(){
            this.getResources(this.pageNum);
        },
        handleSelectLayerNode:function(){
            var self = this;
            self.$refs.selectLayerNode.show();
        },
        layerNodeSelected:function(row, done){
            var self = this;
            self.newAccessNodeUid = row.nodeUid;
            done();
        },
        handleTabClick(tab, event)
        {
            if ("BundleManage" !== tab.name) {
                this.$router.push('/' + tab.name);
            }
        }
    ,
        //获取用户列表
        getAllUsers : function () {
            getAllUsers().then(res => {
                if (res.errMsg) {
                    this.$message({
                        message: res.errMsg,
                        type: 'error'
                    });
                } else {
                    this.users = [];
                    this.users.push({
                        id: -1,
                        name: "全部用户"
                    });
                    this.users = this.users.concat(res.users);
                }
            });
        }
    ,
        //获取资源类型
        getDeviceModels : function () {
            getDeviceModels().then(res => {
                if (!res.errMsg && res.deviceModels) {
                    this.deviceModelOptions.push({
                        value: "",
                        label: "全部类型"
                    });
                    for (let deviceModel of res.deviceModels) {
                        let deviceModelOption = {
                            value: deviceModel,
                            label: deviceModel
                        };
                        this.deviceModelOptions.push(deviceModelOption);
                    }
                }
            });
        }
    ,
        //获取资源列表
        getResources : function (pageNum) {

            this.countPerPage = 20
            if (/^[1-9]+[0-9]*]*$/.test(this.filters.countPerPage)) {
              this.countPerPage = parseInt(this.filters.countPerPage)
            }
            let param = {
                deviceModel: this.filters.deviceModel,
                keyword: this.filters.keyword,
                sourceType: this.filters.sourceType,
                userId: this.filters.userId,
                pageNum: pageNum,
                countPerPage: this.countPerPage
            };
            this.resourceTableLoading = true;
            getBundles(param).then((res) => {
                if (res.errMsg) {
                    this.$message({
                        message: res.errMsg,
                        type: 'error'
                    });
                } else {
                    this.pageNum = pageNum;
                    this.total = res.total;
                    this.resources = res.resources;
                }

                this.resourceTableLoading = false;
            });
        }
    ,
        //资源列表分页
        handleCurrentPageChange(val)
        {
            this.pageNum = val;
            this.getResources(this.pageNum);
        }
    ,
        //资源详情
        handleDetail : function (row) {
            this.resourceTableLoading = true;
            let param = {
                bundleId: row.bundleId
            };
            getBundleDetailInfo(param).then(res => {
                if (res.errMsg) {
                    this.$message({
                        message: res.errMsg,
                        type: 'error'
                    });
                } else {
                    this.detailDialogVisible = true;
                    this.detailInfos = res.extraInfos;
                    this.detailInfos.push({
                        name: "bundle_id",
                        value: row.bundleId
                    });
                }
                this.resourceTableLoading = false;
            });
        }
    ,
        handleModify : function (row) {
            console.log(JSON.stringify(row));

            this.$router.push({
                path: '/ModifyBundle',
                query: {
                    bundleId: row.bundleId,
                    bundleName: row.bundleName,
                    deviceIp: row.deviceAddr.deviceIp,
                    devicePort: row.deviceAddr.devicePort
                }
            });
        }
    ,
        handleLogout : function (row) {
            this.resourceTableLoading = true;
            let param = {
                bundleId: row.bundleId
            };

            logoutBundle(param).then(res => {
                if (res.errMsg) {
                    this.$message({
                        message: res.errMsg,
                        type: 'error'
                    });
                } else {
                    row.onlineStatus = "OFFLINE";
                    this.$message({
                        message: "踢出账号成功",
                        type: 'success'
                    });
                }
                this.resourceTableLoading = false;
            });
        }
    ,
        handleClear : function (row) {
            this.$confirm('重置资源将会清空资源上的能力占用，是否确认?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {

                this.resourceTableLoading = true;
                let param = {
                    bundleId: row.bundleId
                };
                clearBundle(param).then(res => {

                    if (res.errMsg) {
                        this.$message({
                            message: res.errMsg,
                            type: 'error'
                        });
                    } else {
                        this.$message({
                            message: "重置成功",
                            type: 'success'
                        });
                    }
                    this.resourceTableLoading = false;
                });

            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消重置'
                });
            });
        }
    ,
        handleDelete : function (row) {
            this.$confirm('是否确认删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.resourceTableLoading = true;
                let param = {
                    bundleIds: row.bundleId
                };

                deleteBundle(param).then(res => {
                    this.resourceTableLoading = false;
                    if (res.errMsg) {
                        this.$message({
                            message: res.errMsg,
                            type: 'error'
                        });
                    } else {
                        this.$message({
                            message: "删除成功",
                            type: 'success'
                        });
                        this.getResources(1);
                        // window.location.reload();
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        }
    ,
        abilityConfig : function (row) {
            this.$router.push({
                path: '/ConfigBundle',
                query: {
                    bundleId: row.bundleId
                }
            });
        }
    ,
        abilityDetail: function (row) {
            let param = {
                bundleId: row.bundleId
            };

            this.resourceTableLoading = true;
            getBundleChannels(param).then(res => {
                if (res.errMsg) {
                    this.$message({
                        message: res.errMsg,
                        type: 'error'
                    });
                } else {
                    this.abilityDetailDialogVisible = true;
                    this.channelSchemes = res.channelSchemes;
                }

                this.resourceTableLoading = false;
            });
        }
    ,
        handleSetLayerId : function (row) {
            this.currentRow = row;
            this.bundleId = row.bundleId;
            this.newAccessNodeUid = row.accessNodeUid;
            this.setAccessLayerVisible = true;
        }
    ,

        submitSetAccessLayer : function () {
            let param = {
                bundleId: this.bundleId,
                accessLayerId: this.newAccessNodeUid
            };

            setAccessLayer(param).then(res => {
                if (res.errMsg) {
                    this.$message({
                        message: res.errMsg,
                        type: 'error'
                    });
                } else {
                    this.currentRow.accessNodeUid = this.newAccessNodeUid;
                    this.$message({
                        message: "设置接入成功",
                        type: 'success'
                    });
                }
                this.setAccessLayerVisible = false;
            });
        }
    ,

        showMultiSetAccessLayerDialog : function () {
            if (!this.multipleSelection.length) {
                this.$message({
                    message: '请至少选择一行',
                    type: 'warning'
                });
                return;
            }

            let bundleIdArr = [];

            for (let selection of this.multipleSelection) {
                bundleIdArr.push(selection.bundleId);
            }

            this.bundleId = bundleIdArr.join(",");

            console.log("bundleIds==" + this.bundleId);

            this.newAccessNodeUid = "";
            this.setAccessLayerVisible = true;
        }
    ,

        exportBundle : function () {

          exportBundle (null).then((res)=>{
            const blob =new Blob([res.data], {type:'application/octet-stream;charset=utf-8'});

            const fileName ='folder.csv';

            const elink =document.createElement('a');

            elink.download =fileName;

            elink.style.display ='none';

            elink.href =URL.createObjectURL(blob);

            document.body.appendChild(elink);

            elink.click();

            URL.revokeObjectURL(elink.href);

            document.body.removeChild(elink);

          }).catch((error)=>{

            this.$message({

              message:  error,

              type:'error'

            });

          })

        }
    ,

        uploadSuccess: function (res) {
            if (res.errMsg) {
                this.$message({
                    message: res.errMsg,
                    type: 'error'
                });
            } else {
                if (res.successCnt) {
                    this.$message({
                        message: "成功导入的设备数量：" + res.successCnt,
                        type: 'success'
                    });
                } else {
                    this.$message({
                        message: "导入失败",
                        type: 'error'
                    });
                }

                this.getResources(1);
            }
        }
    ,
        uploadError: function (err) {
            this.$message({
                message: "上传文件错误: " + JSON.stringify(err),
                type: 'error'
            });
        }
    ,
        onprogress : function () {
            this.resourceTableLoading = true;
        }
    ,

        handleMultiDelete : function () {
            if (!this.multipleSelection.length) {
                this.$message({
                    message: '请至少选择一行',
                    type: 'warning'
                });
                return;
            }

            this.$confirm('是否确认删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.resourceTableLoading = true;
                let bundleIdArr = [];
                for (let selection of this.multipleSelection) {
                    bundleIdArr.push(selection.bundleId);
                }
                let param = {
                    bundleIds: bundleIdArr.join(",")
                };

                deleteBundle(param).then(res => {
                    this.resourceTableLoading = false;
                    if (res.errMsg) {
                        this.$message({
                            message: res.errMsg,
                            type: 'error'
                        });
                    } else {
                        this.$message({
                            message: "删除成功",
                            type: 'success'
                        });
                        this.getResources(1);
                        // window.location.reload();
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });

        }
    ,

      syncUser : function () {

        this.$confirm('是否确认同步用户资源?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.resourceTableLoading = true

          syncUser(null).then(res => {
            this.resourceTableLoading = false;
            if (res.errMsg) {
              this.$message({
                message: res.errMsg,
                type: 'error'
              });
            } else {
              this.$message({
                message: "同步成功",
                type: 'success'
              });
              this.getResources(1);
              // window.location.reload();
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消同步'
          });
        });

      }
    ,

        handleSelectionChange : function (val) {
            this.multipleSelection = val;
        }
    ,

        handleSyncFromLdap: function () {
            this.$confirm("确认将从LDAP服务器同步下载信息么？", "提示", {}).then(() => {
                this.resourceTableLoading = true;
                //NProgress.start();

                let para = {};
                syncEquipInfoFromLdap(para).then(res => {
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

                        this.getResources(1);
                    }
                });

                this.resourceTableLoading = false;
            });


        }
    ,

        /*
         handleSyncToLdap: function(){
         this.$confirm("确认将本地未同步信息同步到LDAP服务器么？", "提示", {}).then(() => {
         this.resourceTableLoading = true;
         //NProgress.start();

         let para = {};

         syncEquipInfToLdap(para).then(res => {
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

         this.getResources(1);
         }
         });

         this.resourceTableLoading = false;
         });

         },
         */

        showHandleSyncToLdapDialog: function () {
            this.confirmSyncToLdapVisible = true;
        }
    ,

        handleSyncToLdap: function (value) {
            // console.log("handleSyncToLdap value=" + value);
            this.resourceTableLoading = true;
            //NProgress.start();

            let para = {
                syncAll: value
            };

            syncEquipInfToLdap(para).then(res => {
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

                    this.getResources(1);
                }
            });

            this.resourceTableLoading = false;

        }
    ,

        handleCleanUpLdap: function () {
            this.$confirm("确认清空所有上传到LDAP和从LDAP下载的信息么？", "提示", {}).then(() => {
                this.resourceTableLoading = true;
                //NProgress.start();

                let para = {};

                cleanUpEquipInfo(para).then(res => {
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

                        this.getResources(1);
                    }
                });

                this.resourceTableLoading = false;
            });

        }
    ,


    },
    mounted() {
        var self = this;
        this.$nextTick(function() {
            self.$parent.$parent.$parent.$parent.$parent.setActive('/BundleManage');
        });
        this.getDeviceModels();
        this.getAllUsers();
        this.getResources(1);

        if (this.uploadUrl.indexOf('__requestIP__') !== -1) {
            var requestIP = document.location.host.split(':')[0]
            this.uploadUrl = this.uploadUrl.replace('__requestIP__', requestIP)
        }
    }
    }

</script>

<style>
</style>
